package com.example.pinchaapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.pinchaapp.dto.CentroDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.example.pinchaapp.util.UbicacionHelper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentrosDeVacunacion extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MaterialToolbar toolbar;
    private TextView txtEstadoCentros;
    private CardView cardEstado;

    private int idPerfil;
    private static final double RADIO_KM = 50.0;

    private Map<Marker, CentroDto> markerCentroMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centros_de_vacunacion);

        toolbar = findViewById(R.id.toolbar);
        txtEstadoCentros = findViewById(R.id.txtEstadoCentros);
        cardEstado = findViewById(R.id.cardEstado);

        toolbar.setNavigationOnClickListener(v -> finish());

        idPerfil = getIntent().getIntExtra("idPerfil", 0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.setOnInfoWindowClickListener(marker -> {
            CentroDto centroSeleccionado = markerCentroMap.get(marker);
            if (centroSeleccionado != null) {
                Intent intent = new Intent(this, AgendarVacuna.class);
                intent.putExtra("idPerfil", idPerfil);
                intent.putExtra("idCentro", centroSeleccionado.getId());
                intent.putExtra("nombreCentro", centroSeleccionado.getNombre());
                startActivity(intent);
            }
        });

        comprobarPermisosYUbicacion();
    }

    private void comprobarPermisosYUbicacion() {
        if (UbicacionHelper.tienePermiso(this)) {
            obtenerCoordenadasDispositivo();
        } else {
            UbicacionHelper.pedirPermiso(this);
        }
    }

    private void obtenerCoordenadasDispositivo() {
        cardEstado.setVisibility(android.view.View.VISIBLE);
        txtEstadoCentros.setText("Obteniendo coordenadas GPS...");

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException ignored) {}

        UbicacionHelper.obtenerUbicacion(this, new UbicacionHelper.UbicacionCallback() {
            @Override
            public void onUbicacionObtenida(double latitud, double longitud) {
                LatLng miUbicacion = new LatLng(latitud, longitud);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 13f));
                buscarCentrosEnServidor(latitud, longitud);
            }

            @Override
            public void onError(String mensaje) {
                txtEstadoCentros.setText("Error GPS: " + mensaje + ". Cargando mapa base.");
                cardEstado.postDelayed(() -> cardEstado.setVisibility(android.view.View.GONE), 3000);
            }
        });
    }

    private void buscarCentrosEnServidor(double lat, double lng) {
        txtEstadoCentros.setText("Buscando centros de vacunación cercanos...");
        ApiService api = ApiClient.getInstance().create(ApiService.class);

        api.getCentrosCercanos(lat, lng, RADIO_KM).enqueue(new Callback<RespuestaDto<List<CentroDto>>>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaDto<List<CentroDto>>> call,
                                   @NonNull Response<RespuestaDto<List<CentroDto>>> response) {
                cardEstado.setVisibility(android.view.View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                    pintarCentrosEnMapa(response.body().getData());
                } else {
                    android.widget.Toast.makeText(CentrosDeVacunacion.this, "Sin centros en el rango de cobertura.", android.widget.Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaDto<List<CentroDto>>> call, @NonNull Throwable t) {
                txtEstadoCentros.setText("Falla de red: " + t.getMessage());
            }
        });
    }

    private void pintarCentrosEnMapa(List<CentroDto> centros) {
        if (centros == null || centros.isEmpty()) return;

        mMap.clear();
        markerCentroMap.clear();

        for (CentroDto centro : centros) {
            if (centro.getLatitud() == null || centro.getLongitud() == null) continue;

            LatLng posicion = new LatLng(centro.getLatitud().doubleValue(), centro.getLongitud().doubleValue());

            MarkerOptions opcionesMarcador = new MarkerOptions()
                    .position(posicion)
                    .title(centro.getNombre())
                    .snippet("Horario: " + centro.getHorario() + " | Toca aquí para ver vacunas")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            Marker marker = mMap.addMarker(opcionesMarcador);
            markerCentroMap.put(marker, centro);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UbicacionHelper.REQUEST_PERMISO_UBICACION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerCoordenadasDispositivo();
            } else {
                txtEstadoCentros.setText("Permiso denegado. No se pueden mapear centros cercanos.");
            }
        }
    }
}
