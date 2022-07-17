package com.linea.uno.applineav1.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.linea.uno.applineav1.entities.Movimiento;
import com.linea.uno.applineav1.repository.MovimientoRepository;
import com.linea.uno.applineav1.utils.GenericResponse;

import java.util.List;

public class MovimientoViewModel extends AndroidViewModel {

    private final MovimientoRepository repository;


    public MovimientoViewModel(@NonNull Application application) {
        super(application);
        repository = MovimientoRepository.getInstance();
    }

    public LiveData<GenericResponse<List<Movimiento>>> getLastFiveMovements(String email){
        return this.repository.getLastFiveMovements(email);
    }
}
