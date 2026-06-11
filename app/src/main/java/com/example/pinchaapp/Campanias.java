package com.example.pinchaapp;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.adapters.CampaniaAdapter;
import com.example.pinchaapp.dto.CampaniaDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Campanias extends BasePerfilActivity {

    // =========================
    // VISTAS Y ADAPTADOR
    // =========================
    private RecyclerView recyclerCampanias;
    private TextView txtEstadoCampanias;
    private CampaniaAdapter campaniaAdapter;
    private ProgressBar progressBar;
    private List<CampaniaDto> listaCampanias = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_campanias;
    }

    @Override
    protected int getNavItemActivo() {
        return R.id.nav_campanias; // Asegúrate de que este ID coincida con tu XML del menú
    }

    @Override
    protected void onPerfilReady() {
        // Enlazamos las vistas
        recyclerCampanias = findViewById(R.id.recyclerCampanias);
        txtEstadoCampanias = findViewById(R.id.txtEstadoCampanias);
        // progres bar
        progressBar = findViewById(R.id.progressCampanias);
        // Configuramos el RecyclerView
        recyclerCampanias.setLayoutManager(new LinearLayoutManager(this));

        // Pasamos la lista y la función lambda para el botón del mapa
        campaniaAdapter = new CampaniaAdapter(listaCampanias, this::abrirCampaniaEnMapa);
        recyclerCampanias.setAdapter(campaniaAdapter);

        // Llamamos a tu API en C#
        cargarCampanias();
    }

    // =========================
    // CONEXIÓN CON LA API EN C#
    // =========================
    private void cargarCampanias() {
        txtEstadoCampanias.setText("Buscando campañas...");
        progressBar.setVisibility(View.VISIBLE);
        ApiService api = ApiClient.getInstance().create(ApiService.class);
        api.getCampanias().enqueue(new Callback<RespuestaDto<List<CampaniaDto>>>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaDto<List<CampaniaDto>>> call,
                                   @NonNull Response<RespuestaDto<List<CampaniaDto>>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    RespuestaDto<List<CampaniaDto>> respuesta = response.body();

                    if (respuesta.isExito() && respuesta.getData() != null) {
                        mostrarCampanias(respuesta.getData());
                    } else {
                        txtEstadoCampanias.setText(respuesta.getMensaje() != null ? respuesta.getMensaje() : "No se pudieron cargar las campañas.");
                    }
                } else {
                    // Si el servidor responde con 401 (token vencido), 404 o 500
                    progressBar.setVisibility(View.GONE);
                    txtEstadoCampanias.setText("Error del servidor. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaDto<List<CampaniaDto>>> call,
                                  @NonNull Throwable t) {
                txtEstadoCampanias.setText("Problema de conexión: " + t.getMessage());
            }
        });
    }

    private void mostrarCampanias(List<CampaniaDto> campanias) {
        listaCampanias.clear();
        listaCampanias.addAll(campanias);
        campaniaAdapter.notifyDataSetChanged();

        if (listaCampanias.isEmpty()) {
            txtEstadoCampanias.setText("No hay campañas activas en este momento.");
        } else {
            txtEstadoCampanias.setText(""); // Limpiamos el texto si todo está bien
        }
    }

    // =========================
    // GEOLOCALIZACIÓN OPTIMIZADA
    // =========================
    // Ahora usa directamente la Latitud y Longitud que  manda el DTO de C#
    private void abrirCampaniaEnMapa(CampaniaDto campania) {
        if (campania.getLatitud() != null && campania.getLongitud() != null) {
            Toast.makeText(this, "Abriendo mapa...", Toast.LENGTH_SHORT).show();

            // Formato correcto para lanzar la app de Google Maps
            Uri uri = Uri.parse("geo:" + campania.getLatitud() + "," + campania.getLongitud()
                    + "?q=" + campania.getLatitud() + "," + campania.getLongitud()
                    + "(" + Uri.encode(campania.getNombre()) + ")");

            Intent mapaIntent = new Intent(Intent.ACTION_VIEW, uri);
            mapaIntent.setPackage("com.google.android.apps.maps");

            // Verifica si el celular tiene instalada la app de Maps
            if (mapaIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapaIntent);
            } else {
                // Si no tiene la app, la abre en el navegador web
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        } else {
            Toast.makeText(this, "Esta campaña no tiene una ubicación registrada.", Toast.LENGTH_LONG).show();
        }
    }
}