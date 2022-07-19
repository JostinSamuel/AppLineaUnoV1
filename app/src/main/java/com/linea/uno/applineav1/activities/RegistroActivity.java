package com.linea.uno.applineav1.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.linea.uno.applineav1.R;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.linea.uno.applineav1.entities.Cliente;
import com.linea.uno.applineav1.entities.Usuario;
import com.linea.uno.applineav1.viewmodel.ClienteViewModel;
import com.linea.uno.applineav1.viewmodel.UsuarioViewModel;
import org.json.JSONException;
import org.json.JSONObject;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistroActivity extends AppCompatActivity {

    private ClienteViewModel clienteViewModel;
    private UsuarioViewModel usuarioViewModel;
    private Button btnGuardarDatos, btnCancelar ;
    private AutoCompleteTextView dropdownTipoDoc;
    private EditText edtNameUser, edtApellidoPaternoU, edtApellidoMaternoU, edtNumDocU, edtPasswordUser, edtEmailUser;
    private TextInputLayout txtInputNameUser, txtInputApellidoPaternoU, txtInputApellidoMaternoU, txtInputTipoDoc, txtInputNumeroDocU, txtInputEmailUser, txtInputPasswordUser;
    private ProgressDialog progressDialog;

    /*SharedPreferences*/
    SharedPreferences preferences ;
    SharedPreferences.Editor editor ;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //showToolbar();
        this.inicializar();
        this.initViewModel();
        this.spinners(); // MÉTODO PARA CARGAR LOS DATOS AL DROPDOWN (ARRAY)
    }

    //INICIALIZANDO LAS CLASES
    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.clienteViewModel = vmp.get(ClienteViewModel.class);
        this.usuarioViewModel = vmp.get(UsuarioViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void inicializar() {

        //EditText
        btnGuardarDatos = findViewById(R.id.btnGuardarDatos);
        edtNameUser = findViewById(R.id.edtNameUser);
        edtApellidoPaternoU = findViewById(R.id.edtApellidoPaternoU);
        edtApellidoMaternoU = findViewById(R.id.edtApellidoMaternoU);
        edtNumDocU = findViewById(R.id.edtNumDocU);
        edtPasswordUser = findViewById(R.id.edtPasswordUser);
        edtEmailUser = findViewById(R.id.edtEmailUser);
        btnCancelar = findViewById(R.id.btnCancelar);

        //AutoCompleteTextView
        dropdownTipoDoc = findViewById(R.id.dropdownTipoDoc);

        //TextInputLayout
        txtInputNameUser = findViewById(R.id.txtInputNameUser);
        txtInputApellidoPaternoU = findViewById(R.id.txtInputApellidoPaternoU);
        txtInputApellidoMaternoU = findViewById(R.id.txtInputApellidoMaternoU);
        txtInputTipoDoc = findViewById(R.id.txtInputTipoDoc);
        txtInputNumeroDocU = findViewById(R.id.txtInputNumeroDocU);
        txtInputEmailUser = findViewById(R.id.txtInputEmailUser);
        txtInputPasswordUser = findViewById(R.id.txtInputPasswordUser);

        btnGuardarDatos.setOnClickListener(v -> { this.guardarDatos(); });
        btnCancelar.setOnClickListener(v-> { this.cancelar();});

        edtNameUser.setEnabled(false);
        edtApellidoMaternoU.setEnabled(false);
        edtApellidoPaternoU.setEnabled(false);

        /* inicializando SharedPreferences*/
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        edtNumDocU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNumeroDocU.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()==8){
                    IniciasDialog();
                    mostrarDatos();
                }
            }
        });

        dropdownTipoDoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTipoDoc.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void cancelar() {
        dropdownTipoDoc.setText("");
        edtNumDocU.setText("");
        edtNameUser.setText("");
        edtApellidoPaternoU.setText("");
        edtApellidoMaternoU.setText("");
        edtEmailUser.setText("");
        edtPasswordUser.setText("");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //cargar animación
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    public void IniciasDialog (){
        progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setMessage("Procesando Informacion...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
    private void spinners() {
        String[] tipoDoc = getResources().getStringArray(R.array.tipoDoc);
        ArrayAdapter arrayTipoDoc = new ArrayAdapter(this, R.layout.dropdown_item, tipoDoc);
        dropdownTipoDoc.setAdapter(arrayTipoDoc);
    }

    private void mostrarDatos() {
        RequestQueue colapeticiones = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://api.apis.net.pe/v1/dni?numero="+edtNumDocU.getText(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject myJsonObject = new JSONObject(response.toString());
                            edtApellidoPaternoU.setText(myJsonObject.getString("apellidoPaterno"));
                            edtNameUser.setText(myJsonObject.getString("nombres"));
                            edtApellidoMaternoU.setText(myJsonObject.getString("apellidoMaterno"));
                            Log.e("DATOS {}",myJsonObject.getString("apellidoPaterno")+"/n"+myJsonObject.getString("nombres")+"/n"+myJsonObject.getString("apellidoMaterno"));
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        colapeticiones.add(jsonObjectRequest);
    }

    private void guardarDatos() {

        Cliente c;
        if (validar()) {
            c = new Cliente();
            try {
                c.setNombres(edtNameUser.getText().toString());
                c.setApellidoPaterno(edtApellidoPaternoU.getText().toString());
                c.setApellidoMaterno(edtApellidoMaternoU.getText().toString());
                c.setTipo_documento(dropdownTipoDoc.getText().toString());
                c.setNumero_documento(edtNumDocU.getText().toString());
                c.setId(0);

                this.clienteViewModel.guardarCliente(c).observe(this, cResponse -> {
                    if (cResponse.getRpta() == 1) {
                        int idc = cResponse.getBody().getId();//OBTENIENDO ID DEL CLIENTE
                        //SETEANDO LAS CREDENCIALES DE USUARIO QUE PERTENEN A TAL CLIENTE
                        Usuario u = new Usuario();
                        u.setEmail(edtEmailUser.getText().toString());
                        u.setClave(edtPasswordUser.getText().toString());
                        u.setVigencia(true);
                        u.setCliente(new Cliente(idc)); //ASIGNANDO CLIENTE AL USUARIO

                        this.usuarioViewModel.guardar(u).observe(this, uResponse -> {
                            if (uResponse.getRpta() == 1) {
                                ToastOk("Información guardada exitosamente");
                                startActivity(new Intent(this,MainActivity.class));
                                //cargar animación
                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            } else {
                                toastIncorrecto("Error al guardar los datos, inténtelo de nuevo");
                            }
                        });
                    } else {
                        toastIncorrecto("Error al guardar los datos, inténtelo de nuevo");
                    }
                });
            } catch (Exception e) {
                warningMessage("Se ha producido un error : " + e.getMessage());
            }
        } else {
            errorMessage("Por favor, complete todos los campos del formulario");
        }

    }

    private boolean validar() {
        boolean retorno = true;
        String nombres, apellidoPaterno, apellidoMaterno, numDoc, correo, clave, dropTipoDoc;
        nombres = edtNameUser.getText().toString();
        apellidoPaterno = edtApellidoPaternoU.getText().toString();
        apellidoMaterno = edtApellidoMaternoU.getText().toString();
        numDoc = edtNumDocU.getText().toString();
        correo = edtEmailUser.getText().toString();
        clave = edtPasswordUser.getText().toString();
        dropTipoDoc = dropdownTipoDoc.getText().toString();

        if (nombres.isEmpty()) {
            txtInputNameUser.setError("Ingresar nombres");
            retorno = false;
        } else {
            txtInputNameUser.setErrorEnabled(false);
        }
        if (apellidoPaterno.isEmpty()) {
            txtInputApellidoPaternoU.setError("Ingresar apellido paterno");
            retorno = false;
        } else {
            txtInputApellidoPaternoU.setErrorEnabled(false);
        }
        if (apellidoMaterno.isEmpty()) {
            txtInputApellidoMaternoU.setError("Ingresar apellido materno");
            retorno = false;
        } else {
            txtInputApellidoMaternoU.setErrorEnabled(false);
        }
        if (numDoc.isEmpty()) {
            txtInputNumeroDocU.setError("Ingresar número de documento");
            retorno = false;
        } else {
            txtInputNumeroDocU.setErrorEnabled(false);
        }
        if (correo.isEmpty()) {
            txtInputEmailUser.setError("Ingresar correo electrónico");
            retorno = false;
        } else {
            txtInputEmailUser.setErrorEnabled(false);
        }
        if (clave.isEmpty()) {
            txtInputPasswordUser.setError("Ingresar clave para el inicio de sesión");
            retorno = false;
        } else {
            txtInputPasswordUser.setErrorEnabled(false);
        }
        if (dropTipoDoc.isEmpty()) {
            txtInputTipoDoc.setError("Seleccionar tipo de Documento");
            retorno = false;
        } else {
            txtInputTipoDoc.setErrorEnabled(false);
        }

        return retorno;
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(message).show();
    }

    public void warningMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.WARNING_TYPE).setTitleText("Notificación del Sistema").setContentText(message).setConfirmText("Ok").show();
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

    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.linear_toast_warning));
        TextView txtMensaje = view.findViewById(R.id.txtMsjWarning);
        txtMensaje.setText(msg);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
}