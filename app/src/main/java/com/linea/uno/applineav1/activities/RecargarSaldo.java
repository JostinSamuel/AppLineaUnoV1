package com.linea.uno.applineav1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.linea.uno.applineav1.R;
import com.linea.uno.applineav1.entities.Tarjeta;
import com.linea.uno.applineav1.entities.Usuario;
import com.linea.uno.applineav1.entities.dto.GenerarMovimientoDTO;
import com.linea.uno.applineav1.utils.DateSerializer;
import com.linea.uno.applineav1.utils.TimeSerializer;
import com.linea.uno.applineav1.viewmodel.MovimientoViewModel;
import com.linea.uno.applineav1.viewmodel.TarjetaViewModel;

import java.sql.Date;
import java.sql.Time;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RecargarSaldo extends AppCompatActivity {

    private Button btn_recargarSaldo;
    private TextInputLayout txtInputMonto;
    private TextInputEditText txtmontoAcargar;
    private MovimientoViewModel movimientoViewModel;
    private TarjetaViewModel tarjetaViewModel;


    final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recargar_saldo);
        inicializar();
        inicializarViewModel();
    }

    private void inicializarViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.movimientoViewModel = vmp.get(MovimientoViewModel.class);
        this.tarjetaViewModel = vmp.get(TarjetaViewModel.class);
    }

    private void inicializar() {
        txtInputMonto = findViewById(R.id.txtInputMonto);
        txtmontoAcargar = findViewById(R.id.txtmontoAcargar);
        btn_recargarSaldo = findViewById(R.id.btn_recargarSaldo);

        btn_recargarSaldo.setOnClickListener(v -> {
            //sesion del usuario
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String pref = preferences.getString("UsuarioJson", "");

            Usuario u = g.fromJson(pref, Usuario.class);

            int idC = u.getCliente().getId();

            System.out.println("id cliente -> "+ idC);

            if (idC != 0) {
                toastCorrecto("Procesando recarga...");
                registrarPedido(idC);
            } else {
                toastIncorrecto("Falló operación recarga...");
                startActivity(new Intent(getApplicationContext(), InicioActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }

    private void registrarPedido(int idC) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sp.getString("tokenCard", null);
        String email = sp.getString("emailRecargar", null);
        System.out.println("EMAIL: --> "+email);
        if (token != null){

            GenerarMovimientoDTO dto = new GenerarMovimientoDTO();

            java.util.Date date = new java.util.Date();
            System.out.println(new Date(date.getTime()));

            /*this.tarjetaViewModel.getTarjetaUsuario(idC).observe(this, response -> {
                if (response.getRpta()==1){
                    Log.e("tarjeta id : ", String.valueOf(response.getBody()));

                }
                else{
                    Log.e("Error: ", "Falló al obtener tarjeta del usuario");
                }
            });*/

            //movimiento
            dto.getMovimiento().setTarjeta(new Tarjeta(1));
            dto.getMovimiento().setFecha(new Date(date.getTime()));
            dto.getMovimiento().setMonto_total(Double.parseDouble(txtmontoAcargar.getText().toString()));
            dto.getMovimiento().setToken(token);
            dto.getMovimiento().setEmail(email);
            dto.getCliente().setId(idC);

            this.movimientoViewModel.recargarSaldo(dto).observe(this, rpta -> {
                if(rpta.getRpta()==1){
                    successMessage("Recarga exitosa");
                    System.out.println("Response: "+rpta.getBody()+"\nTodo: "+rpta);
                    txtmontoAcargar.setText("");
                }else{
                    System.out.println("Response: "+rpta.getBody()+"\nTodo: "+rpta);
                    toastIncorrecto("Ups! , No se pudo recargar");
                }
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("cardToken");
                editor.apply();
            });

        }else{
            startActivity(new Intent(RecargarSaldo.this,CardActivity.class));
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }

    public void toastCorrecto(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.toast_ok, (ViewGroup) findViewById(R.id.linear_toast_ok));
        TextView textView = layouView.findViewById(R.id.txtMsjOk);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
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

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen trabajo!")
                .setContentText(message)
                .setConfirmClickListener(sweetAlertDialog -> {
                    finish();
                    startActivity(new Intent(RecargarSaldo.this, InicioActivity.class));
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                })
                .show();
    }
}