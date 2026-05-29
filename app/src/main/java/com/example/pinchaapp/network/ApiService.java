package com.example.pinchaapp.network;


import com.example.pinchaapp.dto.ActualizarEstadoDto;
import com.example.pinchaapp.dto.AlergiasDto;
import com.example.pinchaapp.dto.AsignarAlergiaDto;
import com.example.pinchaapp.dto.AuthDto;
import com.example.pinchaapp.dto.CampaniaDto;
import com.example.pinchaapp.dto.CarnetDto;

import com.example.pinchaapp.dto.AppManejoDto;

import com.example.pinchaapp.dto.CentroDto;
import com.example.pinchaapp.dto.CertificadoDto;
import com.example.pinchaapp.dto.DispositivoDto;
import com.example.pinchaapp.dto.HistorialDto;
import com.example.pinchaapp.dto.ImcDto;
import com.example.pinchaapp.dto.MiembroDto;
import com.example.pinchaapp.dto.PerfilMiembroDto;
import com.example.pinchaapp.dto.RecordatorioDto;
import com.example.pinchaapp.dto.RegistrarVacunacionDto;
import com.example.pinchaapp.dto.RegistroDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.dto.VacunaDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ==================== AUTH ====================
    @POST("api/auth/login")
    Call<RespuestaDto<AuthDto.AuthResponseDto>> login(@Body AuthDto.LoginDto body);

    @POST("api/auth/registro")
    Call<RespuestaDto<Object>> registro(@Body RegistroDto body);

    @POST("api/auth/google")
    Call<RespuestaDto<AuthDto.AuthResponseDto>> loginGoogle(@Body AuthDto.GoogleAuthDto body);

    // ==================== MIEMBROS ====================
    @GET("api/miembros")
    Call<RespuestaDto<List<MiembroDto.MiembroResponseDto>>> getMiembros();

    @GET("api/miembros/{id}/perfil")
    Call<RespuestaDto<PerfilMiembroDto>> getPerfilMiembro(@Path("id") int id);

    @POST("api/miembros")
    Call<RespuestaDto<Object>> crearMiembro(@Body MiembroDto.CrearMiembroDto body);

    // NUEVO: Endpoint PUT para actualizar perfiles que te faltaba
    @PUT("api/miembros/{id}")
    Call<RespuestaDto<Void>> actualizarMiembro(
            @Path("id") int id,
            @Body MiembroDto.ActualizarMiembroDto body
    );

    // ==================== VACUNAS ====================
    @GET("api/vacunas")
    Call<RespuestaDto<List<VacunaDto>>> getVacunas();

    @GET("api/vacunas")
    Call<RespuestaDto<List<VacunaDto>>> getVacunasPorTipo(@Query("tipo") String tipo);

    @GET("api/vacunas/{id}")
    Call<RespuestaDto<VacunaDto>> getVacuna(@Path("id") int id);

    // ==================== HISTORIAL ====================
    @GET("api/historial/{idMiembro}")
    Call<RespuestaDto<List<HistorialDto>>> getHistorial(@Path("idMiembro") int idMiembro);

    @POST("api/historial")
    Call<RespuestaDto<Object>> registrarVacunacion(@Body RegistrarVacunacionDto body);

    @GET("api/historial/proximas-dosis")
    Call<RespuestaDto<List<HistorialDto>>> getProximasDosis();

    // ==================== RECORDATORIOS ====================
    @GET("api/recordatorios")
    Call<RespuestaDto<List<RecordatorioDto>>> getRecordatorios();

    @GET("api/recordatorios")
    Call<RespuestaDto<List<RecordatorioDto>>> getRecordatoriosPorEstado(@Query("estado") String estado);

    @GET("api/recordatorios/proximos")
    Call<RespuestaDto<List<RecordatorioDto>>> getProximos(@Query("dias") int dias);

    @GET("api/recordatorios/campanias")
    Call<RespuestaDto<List<RecordatorioDto>>> getCampaniasRecordatorios();

    @PATCH("api/recordatorios/{id}/estado")
    Call<RespuestaDto<Object>> actualizarEstadoRecordatorio(
            @Path("id") int id,
            @Body ActualizarEstadoDto body
    );

    // ==================== CENTROS ====================
    @GET("api/centro")
    Call<RespuestaDto<List<CentroDto>>> getCentros();

    @GET("api/centro/{id}")
    Call<RespuestaDto<CentroDto>> getCentro(@Path("id") int id);

    @GET("api/centro/{id}/detalle")
    Call<RespuestaDto<CentroDto>> getCentroDetalle(@Path("id") int id);

    @GET("api/centro/cercanos")
    Call<RespuestaDto<List<CentroDto>>> getCentrosCercanos(
            @Query("latitud") double latitud,
            @Query("longitud") double longitud,
            @Query("radioKm") double radioKm
    );

    @GET("api/centro/campania/{idCampania}")
    Call<RespuestaDto<List<CentroDto>>> getCentrosPorCampania(@Path("idCampania") int idCampania);

    // ==================== CAMPAÑAS ====================
    @GET("api/campanias")
    Call<RespuestaDto<List<CampaniaDto>>> getCampanias();

    @GET("api/campanias/{id}")
    Call<RespuestaDto<CampaniaDto>> getCampania(@Path("id") int id);

    // ==================== IMC / CARNET ====================
    @POST("api/imc")
    Call<RespuestaDto<ImcDto.ImcResponseDto>> registrarImc(@Body ImcDto.RegistrarImcDto body);

    @GET("api/imc/{idMiembro}")
    Call<RespuestaDto<List<ImcDto.ImcResponseDto>>> getHistorialImc(@Path("idMiembro") int idMiembro);

    @GET("api/imc/carnet/{idMiembro}")
    Call<RespuestaDto<List<CarnetDto>>> getCarnets(@Path("idMiembro") int idMiembro);

    @POST("api/imc/carnet")
    Call<RespuestaDto<Object>> registrarCarnet(@Body CarnetDto body);

    @DELETE("api/imc/carnet/{id}")
    Call<RespuestaDto<Object>> eliminarCarnet(@Path("id") int id);

    // ==================== ALERGIAS ====================
    @GET("api/alergias")
    Call<RespuestaDto<List<AlergiasDto.AlergiaDto>>> getAlergias();

    @GET("api/alergias/miembro/{idMiembro}")
    Call<RespuestaDto<List<AlergiasDto.AlergiaMiembroDto>>> getAlergiasMiembro(
            @Path("idMiembro") int idMiembro
    );

    @POST("api/alergias/miembro/{idMiembro}")
    Call<RespuestaDto<Object>> asignarAlergia(
            @Path("idMiembro") int idMiembro,
            @Body AsignarAlergiaDto body
    );

    @DELETE("api/alergias/miembro/{idMiembro}/{idAlergia}")
    Call<RespuestaDto<Object>> quitarAlergia(
            @Path("idMiembro") int idMiembro,
            @Path("idAlergia") int idAlergia
    );

    // ==================== CERTIFICADO ====================
    @GET("api/certificado/miembro/{idMiembro}")
    Call<RespuestaDto<List<CertificadoDto>>> getCertificados(@Path("idMiembro") int idMiembro);

    @POST("api/certificado")
    Call<RespuestaDto<Object>> generarCertificado(@Body CertificadoDto body);

    @DELETE("api/certificado/{id}")
    Call<RespuestaDto<Object>> eliminarCertificado(@Path("id") int id);

    // ==================== DISPOSITIVO / PUSH ====================
    @GET("api/dispositivousuario")
    Call<RespuestaDto<List<DispositivoDto>>> getDispositivos();

    @POST("api/dispositivousuario")
    Call<RespuestaDto<Object>> registrarDispositivo(@Body DispositivoDto body);

    @DELETE("api/dispositivousuario/{id}")
    Call<RespuestaDto<Object>> desactivarDispositivo(@Path("id") int id);

    // ==================== DASHBOARD ====================
    @GET("api/Dashboard/admin")
    Call<RespuestaDto<AppManejoDto.DashboardAdminResponseDto>> getDashboardAdmin();

    @GET("api/Dashboard/usuario")
    Call<RespuestaDto<AppManejoDto.DashboardUsuarioResponseDto>> getDashboardUsuario();
}