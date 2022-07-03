package com.linea.uno.applineav1.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.linea.uno.applineav1.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.linea.uno.applineav1.databinding.ActivityInicioBinding;
import com.linea.uno.applineav1.entities.Usuario;
import com.linea.uno.applineav1.utils.DateSerializer;
import com.linea.uno.applineav1.utils.TimeSerializer;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Date;
import java.sql.Time;
import de.hdodenhof.circleimageview.CircleImageView;

public class InicioActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityInicioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarInicio.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_movimientos, R.id.nav_perfil)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrarSesion:
                this.cerrarSesion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cerrarSesion() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("UsuarioJson");
        editor.remove("email");
        //editor.remove("cardToken");
        editor.apply();
        this.finish();
        startActivity(new Intent(InicioActivity.this,MainActivity.class));
        this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cargarDatos();
    }

    private void cargarDatos() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();

        String usuario = sharedPreferences.getString("UsuarioJson", null);

        if (usuario != null) {
            final Usuario u = gson.fromJson(usuario, Usuario.class);
            final View vista = binding.navView.getHeaderView(0);
            final TextView txtUserName = vista.findViewById(R.id.txtUserName);
            final TextView txtUserEmail = vista.findViewById(R.id.txtUserEmail);
            //final CircleImageView imageViewPerfil = vista.findViewById(R.id.imageViewPerfil);
            txtUserName.setText(u.getCliente().getNombreCompleto());
            txtUserEmail.setText(u.getEmail());
            System.out.println(usuario);
            //email para la tarjeta CardActivity
            //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            //SharedPreferences.Editor editor=preferences.edit();
            //String emailUser = u.getEmail();
            //editor.putString("email", emailUser);
            //editor.apply();

            //String url = u.getCliente().getFoto_url();
            //System.out.println("URL FOTO: "+url);
            /*
            imageViewPerfil.setImageBitmap(null);
            if(url == null) {
                final Picasso picasso = new Picasso.Builder(this).downloader(new OkHttp3Downloader(ConfigApi.getClient())).build();
                picasso.load(R.drawable.image_not).error(R.drawable.image_not).into(imageViewPerfil);
            }else{
                Picasso.get().load(url).into(imageViewPerfil);
            }
            */
        }
    }
}