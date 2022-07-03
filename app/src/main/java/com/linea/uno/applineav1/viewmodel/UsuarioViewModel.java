package com.linea.uno.applineav1.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.linea.uno.applineav1.entities.Usuario;
import com.linea.uno.applineav1.repository.UsuarioRepository;
import com.linea.uno.applineav1.utils.GenericResponse;

public class UsuarioViewModel extends AndroidViewModel {

    private final UsuarioRepository usuarioRepository;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        this.usuarioRepository= UsuarioRepository.getInstance();
    }

    public LiveData<GenericResponse<Usuario>> login(String email, String clave){
        return this.usuarioRepository.login(email,clave);
    }

    public LiveData<GenericResponse<Usuario>> guardar(Usuario usuario){
        return this.usuarioRepository.guardar(usuario);
    }

}
