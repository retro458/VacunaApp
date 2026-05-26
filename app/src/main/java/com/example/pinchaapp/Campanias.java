package com.example.pinchaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.adapters.CampaniaAdapter;
import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.dto.CampaniaDto;
import com.example.pinchaapp.dto.CentroDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Campanias extends AppCompatActivity {
    // ========================= PUEDE CAMBIAR
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    String nombrePerfil;
    String fechaNacimiento;
    String sexo;
    int idPerfil;

    TextView txtNombreMenu, txtEdadMenu;
    VacunAppDatabase db;

    // =========================
    // CAMPAÑAS - CAMPOS
    // =========================
    RecyclerView recyclerCampanias;
    TextView txtEstadoCampanias;
    CampaniaAdapter campaniaAdapter;
    List<CampaniaDto> listaCampanias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_campanias);

        // =========================
        // VISTAS
        // =========================
        drawerLayout   = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar        = findViewById(R.id.toolbar);
        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtEdad   = findViewById(R.id.txtEdad);

        View header = navigationView.getHeaderView(0);
        txtNombreMenu = header.findViewById(R.id.txtNombreMenu);
        txtEdadMenu   = header.findViewById(R.id.txtEdadMenu);

        // =========================
        // RECIBIR DATOS
        // =========================
        idPerfil        = getIntent().getIntExtra("idPerfil", 0);
        nombrePerfil    = getIntent().getStringExtra("nombre");
        fechaNacimiento = getIntent().getStringExtra("fechaNacimiento");
        sexo            = getIntent().getStringExtra("sexo");

        // =========================
        // CARD PERFIL
        // =========================
        txtNombre.setText(nombrePerfil != null ? nombrePerfil : "Sin nombre");
        txtEdad.setText(fechaNacimiento != null
                ? calcularEdadCompleta(fechaNacimiento)
                : "Edad no disponible");

        // =========================
        // HEADER MENÚ
        // =========================
        txtNombreMenu.setText(nombrePerfil != null ? nombrePerfil : "Sin nombre");
        txtEdadMenu.setText(fechaNacimiento != null
                ? calcularEdadCompleta(fechaNacimiento)
                : "Edad no disponible");

        // =========================
        // COLOR TOOLBAR
        // =========================
        int color = ContextCompat.getColor(this, R.color.skyblue);
        toolbar.setBackgroundColor(color);
        txtNombre.setTextColor(color);

        // =========================
        // MENU HAMBURGUESA
        // =========================
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // =========================
        // EVENTOS MENU
        // =========================
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_alergias) {
                Intent intent = new Intent(this, AlergiasMiembro.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_escanear) {
                Intent intent = new Intent(this, EscanearCarnet.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_carnets) {
                Intent intent = new Intent(this, CarnetEscaneados.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_centros) {
                Intent intent = new Intent(this, CentrosDeVacunacion.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_carnet) {
                Intent intent = new Intent(this, carnet_de_vacunacion.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_pdf) {
                Intent intent = new Intent(this, ExportarPDF.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_perfiles) {
                startActivity(new Intent(this, pantalla_dashboard.class));
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });


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

    // =========================
    // MÉTODOS EDAD
    // =========================
    private String calcularEdadCompleta(String fecha) {
        try {
            SimpleDateFormat sdf;
            if (fecha.contains("/")) {
                sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }
            Date fechaNac = sdf.parse(fecha);
            Calendar nac = Calendar.getInstance();
            nac.setTime(fechaNac);
            Calendar hoy = Calendar.getInstance();
            int años  = hoy.get(Calendar.YEAR)  - nac.get(Calendar.YEAR);
            int meses = hoy.get(Calendar.MONTH) - nac.get(Calendar.MONTH);
            if (meses < 0) { años--; meses += 12; }
            return años + " años y " + meses + " meses";
        } catch (Exception e) {
            return "Edad no disponible";
        }
    }

    private int calcularEdadEnAnios(String fecha) {
        try {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date fechaNac = sdf.parse(fecha);
            Calendar nac = Calendar.getInstance();
            nac.setTime(fechaNac);
            Calendar hoy = Calendar.getInstance();
            return hoy.get(Calendar.YEAR) - nac.get(Calendar.YEAR);
        } catch (Exception e) { return 0; }
    }

}
// ========================= PUEDE CAMBIAR