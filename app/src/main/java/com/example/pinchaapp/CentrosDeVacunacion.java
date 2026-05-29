package com.example.pinchaapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.adapters.CentroAdapter;
import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.dto.CentroDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.example.pinchaapp.util.UbicacionHelper;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentrosDeVacunacion extends BasePerfilActivity {

    VacunAppDatabase db;

    // =========================
    // GEOLOCALIZACIÓN - CAMPOS
    // =========================
    RecyclerView recyclerCentros;
    TextView txtEstadoCentros;
    CentroAdapter centroAdapter;
    List<CentroDto> listaCentros = new ArrayList<>();
    private static final double RADIO_KM = 10.0; // radio de búsqueda

    @Override
    protected int getLayoutId()      { return R.layout.activity_centros_de_vacunacion; }

    @Override
    protected int getNavItemActivo() { return R.id.nav_centros; }

    @Override
    protected void onPerfilReady() {

        // =========================
        // BASE DE DATOS Y BOTONES
        // =========================
        db = VacunAppDatabase.getInstance(this);

        // =========================
        // GEOLOCALIZACIÓN - CENTROS
        // =========================
        recyclerCentros  = findViewById(R.id.recyclerCentros);
        txtEstadoCentros = findViewById(R.id.txtEstadoCentros);

        recyclerCentros.setLayoutManager(new LinearLayoutManager(this));
        centroAdapter = new CentroAdapter(listaCentros, this::abrirEnMapa);
        recyclerCentros.setAdapter(centroAdapter);

        iniciarBusquedaCentros();
    }

    // =========================
    // GEOLOCALIZACIÓN - MÉTODOS
    // =========================

    // Verifica permiso y arranca la búsqueda
    private void iniciarBusquedaCentros() {
        if (UbicacionHelper.tienePermiso(this)) {
            obtenerUbicacionYBuscar();
        } else {
            UbicacionHelper.pedirPermiso(this);
        }
    }

    // Obtiene ubicación GPS y consulta centros cercanos
    private void obtenerUbicacionYBuscar() {
        txtEstadoCentros.setText("Obteniendo tu ubicación...");

        UbicacionHelper.obtenerUbicacion(this, new UbicacionHelper.UbicacionCallback() {
            @Override
            public void onUbicacionObtenida(double latitud, double longitud) {
                cargarCentrosCercanos(latitud, longitud);
            }

            @Override
            public void onError(String mensaje) {
                txtEstadoCentros.setText(mensaje);
                // Plan B: si falla el GPS, mostramos todos los centros sin distancia
                cargarTodosLosCentros();
            }
        });
    }

    // Llama al endpoint /api/centros/cercanos
    private void cargarCentrosCercanos(double lat, double lng) {
        txtEstadoCentros.setText("Buscando centros cercanos...");

        ApiService api = ApiClient.getInstance().create(ApiService.class);
        api.getCentrosCercanos(lat, lng, RADIO_KM).enqueue(
                new Callback<RespuestaDto<List<CentroDto>>>() {
                    @Override
                    public void onResponse(@NonNull Call<RespuestaDto<List<CentroDto>>> call,
                                           @NonNull Response<RespuestaDto<List<CentroDto>>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isExito()
                                && response.body().getData() != null) {
                            mostrarCentros(response.body().getData());
                        } else {
                            String msg = (response.body() != null)
                                    ? response.body().getMensaje()
                                    : "No se encontraron centros cercanos";
                            txtEstadoCentros.setText(msg);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RespuestaDto<List<CentroDto>>> call,
                                          @NonNull Throwable t) {
                        txtEstadoCentros.setText("Error de conexión: " + t.getMessage());
                    }
                });
    }

    // Plan B: lista todos los centros (sin filtrar por distancia)
    private void cargarTodosLosCentros() {
        ApiService api = ApiClient.getInstance().create(ApiService.class);
        api.getCentros().enqueue(new Callback<RespuestaDto<List<CentroDto>>>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaDto<List<CentroDto>>> call,
                                   @NonNull Response<RespuestaDto<List<CentroDto>>> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().isExito()
                        && response.body().getData() != null) {
                    mostrarCentros(response.body().getData());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaDto<List<CentroDto>>> call,
                                  @NonNull Throwable t) {
                txtEstadoCentros.setText("Error de conexión: " + t.getMessage());
            }
        });
    }

    // Refresca la lista en pantalla
    private void mostrarCentros(List<CentroDto> centros) {
        listaCentros.clear();
        listaCentros.addAll(centros);
        centroAdapter.notifyDataSetChanged();

        if (listaCentros.isEmpty()) {
            txtEstadoCentros.setText("No hay centros disponibles");
        } else {
            txtEstadoCentros.setText("");
        }
    }

    // Abre el centro seleccionado en Google Maps
    private void abrirEnMapa(CentroDto centro) {
        Uri uri = Uri.parse("geo:" + centro.getLatitud() + "," + centro.getLongitud()
                + "?q=" + centro.getLatitud() + "," + centro.getLongitud()
                + "(" + Uri.encode(centro.getNombre()) + ")");

        Intent mapaIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapaIntent.setPackage("com.google.android.apps.maps");

        if (mapaIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapaIntent);
        } else {
            // Si no tiene Google Maps, abre en navegador
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    // Respuesta del usuario al diálogo de permiso
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UbicacionHelper.REQUEST_PERMISO_UBICACION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionYBuscar();
            } else {
                txtEstadoCentros.setText(
                        "Sin permiso de ubicación. Mostrando todos los centros.");
                cargarTodosLosCentros();
            }
        }
    }

}
