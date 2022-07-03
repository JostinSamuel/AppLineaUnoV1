package com.linea.uno.applineav1.api;

import com.linea.uno.applineav1.entities.Usuario;
import com.linea.uno.applineav1.utils.GenericResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UsuarioApi {

    String base = "api/v1/usuario";


    @FormUrlEncoded
    @POST(base + "/login")
    Call<GenericResponse<Usuario>> login(@Field("email") String email, @Field("clave") String clave);

    @POST(base)
    Call<GenericResponse<Usuario>> guardar(@Body Usuario usuario);
}
