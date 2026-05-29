package com.example.pinchaapp;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.adapters.CampaniaAdapter;
import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.dto.CampaniaDto;
import com.example.pinchaapp.dto.CentroDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Campanias extends BasePerfilActivity {
    VacunAppDatabase db;

    // =========================
    // CAMPAÑAS - CAMPOS
    // =========================
    RecyclerView recyclerCampanias;
    TextView txtEstadoCampanias;
    CampaniaAdapter campaniaAdapter;
    List<CampaniaDto> listaCampanias = new ArrayList<>();

    @Override
    protected int getLayoutId()      { return R.layout.activity_campanias; }

    @Override
    protected int getNavItemActivo() { return R.id.nav_campanias; }

    @Override
    protected void onPerfilReady() {
        // =========================
        // BASE DE DATOS Y BOTONES
        // =========================
        db = VacunAppDatabase.getInstance(this);

        // =========================
        // CAMPAÑAS - GEOLOCALIZACIÓN
        // =========================
        recyclerCampanias  = findViewById(R.id.recyclerCampanias);
        txtEstadoCampanias = findViewById(R.id.txtEstadoCampanias);

        recyclerCampanias.setLayoutManager(new LinearLayoutManager(this));
        campaniaAdapter = new CampaniaAdapter(listaCampanias, this::abrirCampaniaEnMapa);
        recyclerCampanias.setAdapter(campaniaAdapter);

        cargarCampanias();
    }

    // =========================
    // CAMPAÑAS - MÉTODOS
    // =========================

    // Llama al endpoint /api/campanias
    private void cargarCampanias() {
        txtEstadoCampanias.setText("Cargando campañas...");

        ApiService api = ApiClient.getInstance().create(ApiService.class);
        api.getCampanias().enqueue(new Callback<RespuestaDto<List<CampaniaDto>>>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaDto<List<CampaniaDto>>> call,
                                   @NonNull Response<RespuestaDto<List<CampaniaDto>>> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().isExito()
                        && response.body().getData() != null) {
                    mostrarCampanias(response.body().getData());
                } else {
                    String msg = (response.body() != null)
                            ? response.body().getMensaje()
                            : "No se encontraron campañas";
                    txtEstadoCampanias.setText(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaDto<List<CampaniaDto>>> call,
                                  @NonNull Throwable t) {
                txtEstadoCampanias.setText("Error de conexión: " + t.getMessage());
            }
        });
    }

    // Refresca la lista en pantalla
    private void mostrarCampanias(List<CampaniaDto> campanias) {
        listaCampanias.clear();
        listaCampanias.addAll(campanias);
        campaniaAdapter.notifyDataSetChanged();

        if (listaCampanias.isEmpty()) {
            txtEstadoCampanias.setText("No hay campañas activas");
        } else {
            txtEstadoCampanias.setText("");
        }
    }

    // Abre el centro asociado a la campaña en Google Maps
    private void abrirCampaniaEnMapa(CampaniaDto campania) {
        Toast.makeText(this, "Buscando ubicación del centro...",
                Toast.LENGTH_SHORT).show();

        ApiService api = ApiClient.getInstance().create(ApiService.class);
        api.getCentro(campania.getIdCentro()).enqueue(
                new Callback<RespuestaDto<CentroDto>>() {
                    @Override
                    public void onResponse(@NonNull Call<RespuestaDto<CentroDto>> call,
                                           @NonNull Response<RespuestaDto<CentroDto>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isExito()
                                && response.body().getData() != null) {
                            CentroDto centro = response.body().getData();
                            abrirEnMapa(centro);
                        } else {
                            Toast.makeText(Campanias.this,
                                    "No se encontró la ubicación del centro",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RespuestaDto<CentroDto>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(Campanias.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Abre un centro en Google Maps
    private void abrirEnMapa(CentroDto centro) {
        Uri uri = Uri.parse("geo:" + centro.getLatitud() + "," + centro.getLongitud()
                + "?q=" + centro.getLatitud() + "," + centro.getLongitud()
                + "(" + Uri.encode(centro.getNombre()) + ")");

        Intent mapaIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapaIntent.setPackage("com.google.android.apps.maps");

        if (mapaIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapaIntent);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

}