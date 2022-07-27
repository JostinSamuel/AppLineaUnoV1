package com.linea.uno.applineav1.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.linea.uno.applineav1.api.ConfigApi;
import com.linea.uno.applineav1.api.MovimientoApi;
import com.linea.uno.applineav1.entities.Movimiento;
import com.linea.uno.applineav1.entities.dto.GenerarMovimientoDTO;
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
                Log.e("valorResponse",response.body().toString());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Movimiento>>> call, Throwable t) {
                System.out.println("Error al mostrar historial: " + t.getMessage());
            }
        });
        return mld;
    }

    public LiveData<GenericResponse<GenerarMovimientoDTO>> save(GenerarMovimientoDTO dto) {
        MutableLiveData<GenericResponse<GenerarMovimientoDTO>> data = new MutableLiveData<>();
        movimientoApi.recargarSaldo(dto).enqueue(new Callback<GenericResponse<GenerarMovimientoDTO>>() {
            @Override
            public void onResponse(Call<GenericResponse<GenerarMovimientoDTO>> call, Response<GenericResponse<GenerarMovimientoDTO>> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body());
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<GenerarMovimientoDTO>> call, Throwable t) {
                data.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return data;
    }

    public LiveData<Double> getMontoTotal(String email) {
        final MutableLiveData<Double> mld = new MutableLiveData<>();
        this.movimientoApi.getMontoTotal(email).enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                mld.setValue(response.body());
                Log.e("valorMontototal",response.body().toString());
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                System.out.println("Error al mostrar monto total: " + t.getMessage());
            }
        });
        return mld;
    }

}
