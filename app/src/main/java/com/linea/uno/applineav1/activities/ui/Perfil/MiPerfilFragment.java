package com.linea.uno.applineav1.activities.ui.Perfil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.linea.uno.applineav1.R;
import com.linea.uno.applineav1.activities.MainActivity;
import com.linea.uno.applineav1.entities.Cliente;
import com.linea.uno.applineav1.entities.Usuario;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MiPerfilFragment extends Fragment {

    TextView tvdniperfil,tvemaildni,tvapellidosdni,tvnombresdni;
    Button sign_out_button;

    private static final int File = 1 ;
    DatabaseReference myRef;

    Cliente cliente = new Cliente();

    /*@SuppressLint("NonConstantResourceId")
    @BindView(R.id.profile_image)
    CircleImageView mUploadImageView;*/
    //ImageView mUploadImageView;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializar(view);
        cargarDatos();

        /*ButterKnife.bind(getActivity());*/

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef= database.getReference("user1");

        mUploadImageView.setOnClickListener(v -> fileUpload());*/

    }

    public void fileUpload() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,File);
    }




    private void inicializar(View view) {
        tvdniperfil = view.findViewById(R.id.tvdniperfil);
        tvemaildni = view.findViewById(R.id.tvemaildni);
        tvapellidosdni = view.findViewById(R.id.tvapellidosdni);
        tvnombresdni = view.findViewById(R.id.tvnombresdni);
        sign_out_button = view.findViewById(R.id.sign_out_button);
        //mUploadImageView = view.findViewById(R.id.profile_image);

        //cerrar sesión
        sign_out_button.setOnClickListener(v->{cerrarSesion();});


    }

    public void cargarDatos(){
        SharedPreferences preferences =PreferenceManager.getDefaultSharedPreferences(getActivity());;
        String apellidosCliente = preferences.getString("apellidosCliente", null);
        String nombresCliente = preferences.getString("nombresCliente",null);
        String emailCliente = preferences.getString("emailCliente",null);
        String dniCliente = preferences.getString("dniCliente",null);

        System.out.println(apellidosCliente+' '+nombresCliente+' '+emailCliente+' '+dniCliente);

        if (apellidosCliente!=null && nombresCliente!=null && emailCliente!=null && dniCliente!=null ){
            tvdniperfil.setText(dniCliente);
            tvnombresdni.setText(nombresCliente);
            tvapellidosdni.setText(apellidosCliente);
            tvemaildni.setText(emailCliente);
        }else{
            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
        }
    }


    private void cerrarSesion() {
        SharedPreferences preferences =PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.remove("UsuarioJson");
        editor.remove("nombretoInicio");
        editor.apply();
        /*
        editor.remove("email");
        editor.remove("cardToken");
        this.finish();
        */
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //@Override
    /*public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == File){

            if(resultCode == getActivity().RESULT_OK){

                Uri fileUri = data.getData();

                cliente.setFoto(fileUri);

                mUploadImageView.setImageURI(cliente.getFoto());

                StorageReference Folder = FirebaseStorage.getInstance().getReference().child("User");

                final StorageReference file_name = Folder.child("file"+fileUri.getLastPathSegment());


                file_name.putFile(fileUri).addOnSuccessListener(taskSnapshot -> file_name.getDownloadUrl().addOnSuccessListener(uri -> {

                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("link", String.valueOf(uri));
                    myRef.setValue(hashMap);

                    Log.d("Mensaje", "Se subió correctamente");

                }));

            }

        }

    }*/

}