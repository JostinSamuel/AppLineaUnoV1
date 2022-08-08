package com.linea.uno.applineav1.api;

import com.linea.uno.applineav1.entities.Movimiento;
import com.linea.uno.applineav1.entities.dto.GenerarMovimientoDTO;
import com.linea.uno.applineav1.utils.GenericResponse;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface MovimientoApi {

    String base = "/api/v1/movimiento";

    @GET(base+"/historial/{email}")
    Call<GenericResponse<List<Movimiento>>> getLastFiveMovements(@Path("email") String email);

    @POST(base+"/recargarSaldo")
    Call<GenericResponse<GenerarMovimientoDTO>> recargarSaldo(@Body GenerarMovimientoDTO dto);

    @GET(base+"/montoTotal/{email}")
    Call<Double> getMontoTotal(@Path("email") String email);

    @Streaming
    @GET(base+"/exportInvoice")
    Call<ResponseBody> exportInvoice();
}
