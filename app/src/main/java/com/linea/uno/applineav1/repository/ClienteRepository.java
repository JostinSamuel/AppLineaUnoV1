package com.linea.uno.applineav1.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.linea.uno.applineav1.api.ClienteApi;
import com.linea.uno.applineav1.api.ConfigApi;
import com.linea.uno.applineav1.entities.Cliente;
import com.linea.uno.applineav1.utils.GenericResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteRepository {

    private static ClienteRepository repository;
    private final ClienteApi clienteApi;

    public static ClienteRepository getInstance(){
        if (repository == null){
            repository = new ClienteRepository();
        }
        return repository;
    }

    public ClienteRepository(){
        clienteApi = ConfigApi.getClienteApi();
    }

    public LiveData<GenericResponse<Cliente>> guardar(Cliente cliente){
        final MutableLiveData<GenericResponse<Cliente>> mld = new MutableLiveData<>();
        this.clienteApi.guardar(cliente).enqueue(new Callback<GenericResponse<Cliente>>() {
            @Override
            public void onResponse(Call<GenericResponse<Cliente>> call, Response<GenericResponse<Cliente>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Cliente>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                System.out.println("Error repository : " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }

}
