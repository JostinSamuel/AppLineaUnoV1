package com.linea.uno.applineav1.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.linea.uno.applineav1.api.ConfigApi;
import com.linea.uno.applineav1.api.MovimientoApi;
import com.linea.uno.applineav1.entities.Movimiento;
import com.linea.uno.applineav1.utils.GenericResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovimientoRepository {

    private final MovimientoApi movimientoApi;
    private static MovimientoRepository repository;

    public MovimientoRepository() {
        this.movimientoApi = ConfigApi.getMovimientoApi();
    }

    public static MovimientoRepository getInstance() {
        if (repository == null) {
            repository = new MovimientoRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<Movimiento>>> getLastFiveMovements(String email) {
        final MutableLiveData<GenericResponse<List<Movimiento>>> mld = new MutableLiveData<>();
        this.movimientoApi.getLastFiveMovements(email).enqueue(new Callback<GenericResponse<List<Movimiento>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Movimiento>>> call, Response<GenericResponse<List<Movimiento>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Movimiento>>> call, Throwable t) {
                System.out.println("Error al mostrar historial: " + t.getMessage());
            }
        });
        return mld;
    }
    
}
