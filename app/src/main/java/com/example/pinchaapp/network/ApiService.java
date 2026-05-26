package com.example.pinchaapp.network;

import com.example.pinchaapp.dto.AlergiasDto;
import com.example.pinchaapp.dto.AppManejoDto;
import com.example.pinchaapp.dto.CentroDto;
import com.example.pinchaapp.dto.HistorialDto;
import com.example.pinchaapp.dto.AuthDto;
import com.example.pinchaapp.dto.ImcDto;
import com.example.pinchaapp.dto.MiembroDto;
import com.example.pinchaapp.dto.RecordatorioDto;
import com.example.pinchaapp.dto.RespuestaDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Auth
    @POST("api/auth/login")
    Call<RespuestaDto<AuthDto.AuthResponseDto>> login(@Body AuthDto.LoginDto body);

    @POST("api/auth/registro")
    Call<RespuestaDto<Object>> registro(@Body AuthDto.RegistroDto body);

    @POST("api/auth/google")
    Call<RespuestaDto<AuthDto.AuthResponseDto>> loginGoogle(@Body AuthDto.GoogleAuthDto body);

    // Miembros
    @GET("api/miembros")
    Call<RespuestaDto<List<MiembroDto.MiembroResponseDto>>> getMiembros();

    @GET("api/miembros/{id}/perfil")
    Call<RespuestaDto<AppManejoDto.PerfilMiembroResponseDto>> getPerfilMiembro(@Path("id") int id);

    @POST("api/miembros")
    Call<RespuestaDto<Void>> crearMiembro(@Body MiembroDto.CrearMiembroDto dto);
    @PUT("api/miembros/{id}")
    Call<RespuestaDto<Void>> actualizarMiembro(@Path("id") int id, @Body MiembroDto.ActualizarMiembroDto dto);

    // Historial
    @GET("api/historial/{idMiembro}")
    Call<RespuestaDto<List<HistorialDto>>> getHistorial(@Path("idMiembro") int idMiembro);

    @POST("api/historial")
    Call<RespuestaDto<Object>> registrarVacunacion(@Body HistorialDto.RegistrarVacunacionDto body);

    // Recordatorios
    @GET("api/recordatorios/proximos")
    Call<RespuestaDto<List<RecordatorioDto>>> getProximos(@Query("dias") int dias);

    @PATCH("api/recordatorios/{id}/estado")
    Call<RespuestaDto<Object>> actualizarEstado(
            @Path("id") int id,
            @Body RecordatorioDto.ActualizarRecordatorioDto body
    );

    // Centros
    @GET("api/centros/cercanos")
    Call<RespuestaDto<List<CentroDto>>> getCentrosCercanos(
            @Query("latitud") double latitud,
            @Query("longitud") double longitud,
            @Query("radioKm") double distanciaKm
    );

    // IMC
    @POST("api/imc")
    Call<RespuestaDto<ImcDto.ImcResponseDto>> registrarImc(@Body ImcDto.RegistrarImcDto body);

    // Alergias
    @GET("api/alergias")
    Call<RespuestaDto<List<AlergiasDto.AlergiaDto>>> getAlergias();

    @POST("api/alergias/miembro/{idMiembro}")
    Call<RespuestaDto<Object>> asignarAlergia(
            @Path("idMiembro") int idMiembro,
            @Body MiembroDto.AsignarAlergiaDto body
    );

    @GET("api/Dashboard/admin")
    Call<RespuestaDto<AppManejoDto.DashboardAdminResponseDto>> getDashboardAdmin();
    @GET("api/Dashboard/usuario")
    Call<RespuestaDto<AppManejoDto.DashboardUsuarioResponseDto>> getDashboardUsuario();
}