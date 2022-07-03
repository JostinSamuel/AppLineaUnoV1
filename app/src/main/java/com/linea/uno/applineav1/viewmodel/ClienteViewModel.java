package com.linea.uno.applineav1.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.linea.uno.applineav1.entities.Cliente;
import com.linea.uno.applineav1.repository.ClienteRepository;
import com.linea.uno.applineav1.utils.GenericResponse;

public class ClienteViewModel extends AndroidViewModel {

    private final ClienteRepository clienteRepository;

    public ClienteViewModel(@NonNull Application application) {
        super(application);
        this.clienteRepository = ClienteRepository.getInstance();
    }

    public LiveData<GenericResponse<Cliente>> guardarCliente(Cliente cliente){
        return clienteRepository.guardar(cliente);
    }
}
