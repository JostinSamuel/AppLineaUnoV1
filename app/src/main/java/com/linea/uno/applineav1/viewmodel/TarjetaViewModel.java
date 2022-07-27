package com.linea.uno.applineav1.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.linea.uno.applineav1.entities.Tarjeta;
import com.linea.uno.applineav1.repository.TarjetaRepository;
import com.linea.uno.applineav1.utils.GenericResponse;

public class TarjetaViewModel extends AndroidViewModel {

    private final TarjetaRepository tarjetaRepository;

    public TarjetaViewModel(@NonNull Application application) {
        super(application);
        this.tarjetaRepository= TarjetaRepository.getInstance();
    }

    public LiveData<GenericResponse<Tarjeta>> getTarjetaUsuario(int id){
        return this.tarjetaRepository.getTarjetaUsuario(id);
    }
}
