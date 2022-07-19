package com.linea.uno.applineav1.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.linea.uno.applineav1.R;
import com.linea.uno.applineav1.entities.Culqi.Card;
import com.linea.uno.applineav1.entities.Culqi.Token;
import com.linea.uno.applineav1.entities.Culqi.TokenCallback;
import com.linea.uno.applineav1.utils.Validation.Validation;
import org.json.JSONObject;

public class CardActivity extends AppCompatActivity {

    Validation validation;
    ProgressDialog progress;
    TextView txtcardnumber, txtcvv, txtmonth, txtyear, txtemail, kind_card, result;
    Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        validation = new Validation();
        progress = new ProgressDialog(this);
        progress.setMessage("Validando informacion de la tarjeta");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        txtcardnumber = (TextView) findViewById(R.id.txt_cardnumber);

        txtcvv = (TextView) findViewById(R.id.txt_cvv);

        txtmonth = (TextView) findViewById(R.id.txt_month);

        txtyear = (TextView) findViewById(R.id.txt_year);

        txtemail = (TextView) findViewById(R.id.txt_email);

        kind_card = (TextView) findViewById(R.id.kind_card);

        result = (TextView) findViewById(R.id.token_id);

        btnPay = (Button) findViewById(R.id.btn_pay);

        txtcvv.setEnabled(false);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String email = sp.getString("emailCliente", null);
        System.out.println("Email: --> "+ email);
        txtemail.setText(email);
        txtemail.setEnabled(false);

        txtcardnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    txtcvv.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtcardnumber.getText().toString();
                if(s.length() == 0) {
                    txtcardnumber.setBackgroundResource(R.drawable.border_error);
                }

                if(validation.luhn(text)) {
                    txtcardnumber.setBackgroundResource(R.drawable.border_sucess);
                } else {
                    txtcardnumber.setBackgroundResource(R.drawable.border_error);
                }

                int cvv = validation.bin(text, kind_card);
                if(cvv > 0) {
                    txtcvv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cvv)});
                    txtcvv.setEnabled(true);
                } else {
                    txtcvv.setEnabled(false);
                    txtcvv.setText("");
                }
            }
        });

        txtyear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtyear.getText().toString();
                if(validation.year(text)){
                    txtyear.setBackgroundResource(R.drawable.border_error);
                } else {
                    txtyear.setBackgroundResource(R.drawable.border_sucess);
                }
            }
        });

        txtmonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtmonth.getText().toString();
                if(validation.month(text)){
                    txtmonth.setBackgroundResource(R.drawable.border_error);
                } else {
                    txtmonth.setBackgroundResource(R.drawable.border_sucess);
                }
            }
        });


        btnPay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                progress.show();

                Card card = new Card(txtcardnumber.getText().toString(), txtcvv.getText().toString(), 9, 2025, txtemail.getText().toString());

                Token token = new Token("sk_test_6033e299086641e3");

                token.createToken(getApplicationContext(), card, new TokenCallback() {
                    @Override
                    public void onSuccess(JSONObject token) {
                        try {
                            result.setText(token.get("id").toString());
                            String tk = token.get("id").toString();
                            SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor=preferencias.edit();
                            editor.putString("tokenCard", tk);
                            editor.apply();
                            System.out.println("Token card activity: -->"+ tk);
                        } catch (Exception ex){
                            progress.hide();
                            toastIncorrecto("Error al registrar tarjeta");
                            pasarActivity();
                        }

                        progress.hide();
                        toastCorrecto("Tarjeta registrada!");
                        pasarActivity();

                    }
                    @Override
                    public void onError(Exception error) {
                        progress.hide();
                        toastIncorrecto("Error al registrar tarjeta");
                        pasarActivity();
                    }
                });
            }
        });
    }

    public void pasarActivity(){
        startActivity(new Intent(CardActivity.this,InicioActivity.class));
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
}