package com.linea.uno.applineav1.api;

import com.linea.uno.applineav1.entities.Cliente;
import com.linea.uno.applineav1.utils.GenericResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClienteApi {

    String base = "api/v1/cliente";

    @POST(base)
    Call<GenericResponse<Cliente>> guardar(@Body Cliente cliente);


}
