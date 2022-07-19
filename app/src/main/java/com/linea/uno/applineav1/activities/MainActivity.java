package com.linea.uno.applineav1.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.reflect.TypeToken;
import com.linea.uno.applineav1.R;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.linea.uno.applineav1.entities.Usuario;
import com.linea.uno.applineav1.utils.DateSerializer;
import com.linea.uno.applineav1.utils.TimeSerializer;
import com.linea.uno.applineav1.viewmodel.UsuarioViewModel;
import java.sql.Date;
import java.sql.Time;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    /*Normal Authentication*/
    private EditText edtEmail, edtClave;
    private Button btnIniciarSesion;
    private UsuarioViewModel uViewModel;
    private TextInputLayout txtInputUsuario, txtInputClave;
    private TextView txtNuevoUsuario;

    /*SharedPreferences*/
    SharedPreferences preferences ;
    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initViewModel();
        this.init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = preferences.getString("UsuarioJson","");
        if (!pref.equals("")){
            ToastOk("Sesión activa");
            preferences.edit().remove("tokenCard").apply();
            preferences.edit().remove("email").apply();
            this.startActivity(new Intent(this,InicioActivity.class));
            this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }

    private void initViewModel() {
        uViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
    }

    private void init() {
        edtEmail = findViewById(R.id.edtMail);
        edtClave = findViewById(R.id.edtPassword);
        txtInputUsuario = findViewById(R.id.txtInputUsuario);
        txtInputClave = findViewById(R.id.txtInputPassword);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        txtNuevoUsuario = findViewById(R.id.txtNuevoUsuario);

        /* inicializando SharedPreferences*/
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        editor.remove("cardToken");
        editor.apply();

        /*Normal Authentication*/
        btnIniciarSesion.setOnClickListener(v -> {
            try {
                if (validarInput()) {
                    uViewModel.login(edtEmail.getText().toString(), edtClave.getText().toString()).observe(this, response -> {
                        if (response.getRpta()==1) {
                            ToastOk(response.getMessage());
                            Usuario u = response.getBody();
                            Log.e("Usuario Main Activity: ",u.toString());
                            final Gson g = new GsonBuilder()
                                    .registerTypeAdapter(Date.class, new DateSerializer())
                                    .registerTypeAdapter(Time.class, new TimeSerializer())
                                    .create();
                            editor.putString("UsuarioJson", g.toJson(u, new TypeToken<Usuario>() {}.getType()));
                            editor.putString("emailCliente", edtEmail.getText().toString());
                            editor.putString("nombretoInicio", u.getCliente().getNombreCompleto());
                            editor.putString("dniCliente",u.getCliente().getNumero_documento());
                            editor.putString("nombresCliente",u.getCliente().getNombres());
                            editor.putString("apellidosCliente",u.getCliente().getApellidoPaterno()+" "+u.getCliente().getApellidoMaterno());
                            editor.apply();
                            edtEmail.setText("");
                            edtClave.setText("");
                            startActivity(new Intent(this, InicioActivity.class));
                        } else {
                            ToastWarning(response.getMessage());
                        }
                    });
                } else {
                    ToastWarning("Campos incompletos. Intente otra vez");
                }
            } catch (Exception e) {
                ToastWarning("Error al iniciar sesión: "+e.getMessage());
            }
        });

        //ocultar el msj de error de campos incompletos del TextInput al escribir
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputUsuario.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        edtClave.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputClave.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        //registrar usuario
        txtNuevoUsuario.setOnClickListener(v->{
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
            //cargar animación
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

    }

    private boolean validarInput() {
        boolean retorno = true;
        String usuario, clave;
        usuario = edtEmail.getText().toString();
        clave = edtClave.getText().toString();
        if (usuario.isEmpty()) {
            txtInputUsuario.setError("Ingrese su correo electrónico");
            retorno = false;
        } else {
            txtInputUsuario.setErrorEnabled(false);
        }
        if (clave.isEmpty()) {
            txtInputClave.setError("Ingrese su contraseña");
            retorno = false;
        } else {
            txtInputClave.setErrorEnabled(false);
        }
        return retorno;
    }

    public void ToastWarning(String msj) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.toast_warning, findViewById(R.id.linear_toast_warning));
        TextView txtMsj = view.findViewById(R.id.txtMsjWarning);
        txtMsj.setText(msj);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public void ToastOk(String msj) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.toast_ok, findViewById(R.id.linear_toast_ok));
        TextView txtMsj = view.findViewById(R.id.txtMsjOk);
        txtMsj.setText(msj);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("¿Quieres salir de la aplicación?")
                .setCancelText("Cancelar").setConfirmText("Salir")
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    System.exit(0);
                }).show();
    }

}