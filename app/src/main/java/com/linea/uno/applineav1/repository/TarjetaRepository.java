package com.linea.uno.applineav1.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.linea.uno.applineav1.api.ConfigApi;
import com.linea.uno.applineav1.api.TarjetaApi;
import com.linea.uno.applineav1.entities.Tarjeta;
import com.linea.uno.applineav1.utils.GenericResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TarjetaRepository {

    private static TarjetaRepository tarjetaRepository;
    private final TarjetaApi tarjetaApi;

    public TarjetaRepository() {
        this.tarjetaApi = ConfigApi.getTarjetaApi();
    }

    public static TarjetaRepository getInstance(){
        if (tarjetaRepository == null){
            tarjetaRepository = new TarjetaRepository();
        }
        return  tarjetaRepository;
    }

    public LiveData<GenericResponse<Tarjeta>> getTarjetaUsuario(int id){
        final MutableLiveData<GenericResponse<Tarjeta>> mld = new MutableLiveData<>();
        this.tarjetaApi.getTarjetaUsuario(id).enqueue(new Callback<GenericResponse<Tarjeta>>() {
            @Override
            public void onResponse(Call<GenericResponse<Tarjeta>> call, Response<GenericResponse<Tarjeta>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Tarjeta>> call, Throwable t) {
                mld.setValue(new GenericResponse());
                System.out.println("Se ha producido un error al obtener tarjeta: "+t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
