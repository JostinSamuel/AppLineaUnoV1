package com.linea.uno.applineav1.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.linea.uno.applineav1.api.ConfigApi;
import com.linea.uno.applineav1.api.UsuarioApi;
import com.linea.uno.applineav1.entities.Usuario;
import com.linea.uno.applineav1.utils.GenericResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {

    private static UsuarioRepository usuarioRepository;
    private final UsuarioApi usuarioApi;


    public UsuarioRepository() {
        this.usuarioApi = ConfigApi.getUsuarioApi();
    }

    public static UsuarioRepository getInstance(){
        if (usuarioRepository == null){
            usuarioRepository = new UsuarioRepository();
        }
        return  usuarioRepository;
    }

    public LiveData<GenericResponse<Usuario>> login(String email, String clave){
        final MutableLiveData<GenericResponse<Usuario>> mld = new MutableLiveData<>();
        this.usuarioApi.login(email, clave).enqueue(new Callback<GenericResponse<Usuario>>() {
            @Override
            public void onResponse(Call<GenericResponse<Usuario>> call, Response<GenericResponse<Usuario>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Usuario>> call, Throwable t) {
                mld.setValue(new GenericResponse());
                System.out.println("Se ha producido un error al iniciar sesión: "+t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }

    //guardar datos del usuario
    public LiveData<GenericResponse<Usuario>> guardar(Usuario usuario){
        final MutableLiveData<GenericResponse<Usuario>> mld = new MutableLiveData<>();
        this.usuarioApi.guardar(usuario).enqueue(new Callback<GenericResponse<Usuario>>() {
            @Override
            public void onResponse(Call<GenericResponse<Usuario>> call, Response<GenericResponse<Usuario>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Usuario>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                System.out.println("Error " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
