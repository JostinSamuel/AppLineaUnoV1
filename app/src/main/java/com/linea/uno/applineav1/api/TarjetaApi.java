package com.linea.uno.applineav1.api;

import com.linea.uno.applineav1.entities.Tarjeta;
import com.linea.uno.applineav1.utils.GenericResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface TarjetaApi {

    String base = "api/v1/tarjeta";

    @GET(base + "/usuario/{id}")
    Call<GenericResponse<Tarjeta>> getTarjetaUsuario(@Path("id") int id);


}
