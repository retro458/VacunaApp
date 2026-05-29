package com.example.pinchaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.adapters.AlergiaAdapter;
import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.entities.Alergia;
import com.example.pinchaapp.database.dao.AlergiaDao;
import com.example.pinchaapp.dto.AlergiasDto;
import com.example.pinchaapp.dto.AsignarAlergiaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.example.pinchaapp.dto.RespuestaDto;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
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

public class AlergiasMiembro extends AppCompatActivity {

    private Spinner spinnerAlergias;
    private MaterialButton btnAgregarAlergia;
    private RecyclerView rvAlergias;

    private ApiService apiService;
    private AlergiaDao alergiaDao;

    private List<AlergiasDto.AlergiaDto> listaCatalogo = new ArrayList<>();
    private List<AlergiasDto.AlergiaDto> listaMiembro  = new ArrayList<>();
    private AlergiaAdapter adapter;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    private int idMiembro;
    private int idPerfil;
    String nombrePerfil, fechaNacimiento, sexo, tipoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alergias_miembro);

        // IDs
        idMiembro = getIntent().getIntExtra("idPerfil", 0);
        idPerfil  = idMiembro; // son el mismo

        // API y Room
        apiService = ApiClient.getInstance().create(ApiService.class);
        alergiaDao = VacunAppDatabase.getInstance(this).alergiaDao();

        initViews();
        configurarDrawer();
        cargarCatalogo();
        cargarAlergiasMiembro(); // híbrido: API primero, Room si falla
    }

    // ── Cargar alergias: API primero, Room como respaldo ─────────────
    private void cargarAlergiasMiembro() {
        apiService.getAlergiasMiembro(idMiembro)
                .enqueue(new Callback<RespuestaDto<List<AlergiasDto.AlergiaMiembroDto>>>() {
                    @Override
                    public void onResponse(Call<RespuestaDto<List<AlergiasDto.AlergiaMiembroDto>>> call,
                                           Response<RespuestaDto<List<AlergiasDto.AlergiaMiembroDto>>> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isExito()) {

                            // Convertir AlergiaMiembroDto → AlergiaDto para el adapter
                            List<AlergiasDto.AlergiaDto> lista = new ArrayList<>();
                            for (AlergiasDto.AlergiaMiembroDto m : response.body().getData()) {
                                AlergiasDto.AlergiaDto dto = new AlergiasDto.AlergiaDto();
                                dto.setId(m.getIdAlergia());
                                dto.setNombre(m.getNombre());
                                lista.add(dto);
                            }

                            // Sincronizar Room
                            new Thread(() -> {
                                alergiaDao.eliminarTodosDePerfil(idPerfil);
                                for (AlergiasDto.AlergiaDto a : lista) {
                                    alergiaDao.insertar(new Alergia(idPerfil, a.getId(), a.getNombre()));
                                }
                            }).start();

                            mostrarAlergias(lista);

                        } else {
                            cargarDesdeRoom();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaDto<List<AlergiasDto.AlergiaMiembroDto>>> call,
                                          Throwable t) {
                        cargarDesdeRoom();
                    }
                });
    }

    private void cargarDesdeRoom() {
        new Thread(() -> {
            List<Alergia> locales = alergiaDao.obtenerPorPerfil(idPerfil);

            // Convertir Alergia (Room) → AlergiaDto (para el adapter)
            List<AlergiasDto.AlergiaDto> dtos = new ArrayList<>();
            for (Alergia a : locales) {
                AlergiasDto.AlergiaDto dto = new AlergiasDto.AlergiaDto();
                dto.setId(a.getIdAlergiaApi());
                dto.setNombre(a.getNombre());
                dtos.add(dto);
            }

            runOnUiThread(() -> {
                mostrarAlergias(dtos);
                Toast.makeText(this,
                        "Sin conexión — mostrando datos locales",
                        Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    private void mostrarAlergias(List<AlergiasDto.AlergiaDto> lista) {
        listaMiembro = lista;
        adapter = new AlergiaAdapter(this, listaMiembro,
                alergia -> eliminarAlergia(alergia.getId(), alergia.getNombre()));
        rvAlergias.setAdapter(adapter);
    }

    // ── Agregar: API + Room ──────────────────────────────────────────
    private void agregarAlergia() {
        int position = spinnerAlergias.getSelectedItemPosition();
        if (position == -1 || listaCatalogo.isEmpty()) {
            Toast.makeText(this, "Seleccioná una alergia", Toast.LENGTH_SHORT).show();
            return;
        }

        AlergiasDto.AlergiaDto seleccionada = listaCatalogo.get(position);
        int idAlergia = seleccionada.getId();

        apiService.asignarAlergia(idMiembro, new AsignarAlergiaDto(idAlergia))
                .enqueue(new Callback<RespuestaDto<Object>>() {
                    @Override
                    public void onResponse(Call<RespuestaDto<Object>> call,
                                           Response<RespuestaDto<Object>> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isExito()) {

                            // Guardar también en Room
                            new Thread(() ->
                                    alergiaDao.insertar(
                                            new Alergia(idPerfil, idAlergia, seleccionada.getNombre())
                                    )
                            ).start();

                            Toast.makeText(AlergiasMiembro.this,
                                    "Alergia agregada", Toast.LENGTH_SHORT).show();
                            cargarAlergiasMiembro();

                        } else {
                            try {
                                String error = response.errorBody().string();
                                Toast.makeText(AlergiasMiembro.this,
                                        error, Toast.LENGTH_LONG).show();
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaDto<Object>> call, Throwable t) {
                        Toast.makeText(AlergiasMiembro.this,
                                "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ── Eliminar: API + Room ─────────────────────────────────────────
    private void eliminarAlergia(int idAlergia, String nombreAlergia) {
        apiService.quitarAlergia(idMiembro, idAlergia)
                .enqueue(new Callback<RespuestaDto<Object>>() {
                    @Override
                    public void onResponse(Call<RespuestaDto<Object>> call,
                                           Response<RespuestaDto<Object>> response) {
                        if (response.isSuccessful()) {
                            new Thread(() ->
                                    alergiaDao.eliminarPorApiId(idPerfil, idAlergia)
                            ).start();
                            cargarAlergiasMiembro();
                            Toast.makeText(AlergiasMiembro.this,
                                    "Alergia eliminada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AlergiasMiembro.this,
                                    "No se pudo eliminar la alergia", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaDto<Object>> call, Throwable t) {
                        Toast.makeText(AlergiasMiembro.this,
                                "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ── Catálogo spinner ─────────────────────────────────────────────
    private void cargarCatalogo() {
        apiService.getAlergias()
                .enqueue(new Callback<RespuestaDto<List<AlergiasDto.AlergiaDto>>>() {
                    @Override
                    public void onResponse(Call<RespuestaDto<List<AlergiasDto.AlergiaDto>>> call,
                                           Response<RespuestaDto<List<AlergiasDto.AlergiaDto>>> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isExito()) {

                            listaCatalogo = response.body().getData();
                            List<String> nombres = new ArrayList<>();
                            for (AlergiasDto.AlergiaDto a : listaCatalogo) {
                                nombres.add(a.getNombre());
                            }

                            ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                                    AlergiasMiembro.this,
                                    android.R.layout.simple_spinner_item, nombres);
                            adapterSpinner.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item);
                            spinnerAlergias.setAdapter(adapterSpinner);
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaDto<List<AlergiasDto.AlergiaDto>>> call,
                                          Throwable t) {
                        Toast.makeText(AlergiasMiembro.this,
                                "Error cargando catálogo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ── Vistas y drawer ──────────────────────────────────────────────
    private void initViews() {
        spinnerAlergias   = findViewById(R.id.spinnerAlergias);
        btnAgregarAlergia = findViewById(R.id.btnAgregarAlergia);
        rvAlergias        = findViewById(R.id.rvAlergias);
        rvAlergias.setLayoutManager(new LinearLayoutManager(this));

        btnAgregarAlergia.setOnClickListener(v -> agregarAlergia());
    }

    private void configurarDrawer() {
        drawerLayout   = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar        = findViewById(R.id.toolbar);

        nombrePerfil    = getIntent().getStringExtra("nombre");
        fechaNacimiento = getIntent().getStringExtra("fechaNacimiento");
        sexo            = getIntent().getStringExtra("sexo");
        tipoPerfil      = getIntent().getStringExtra("tipoPerfil");

        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtEdad   = findViewById(R.id.txtEdad);
        View header        = navigationView.getHeaderView(0);

        ((TextView) header.findViewById(R.id.txtNombreMenu))
                .setText(nombrePerfil != null ? nombrePerfil : "Sin nombre");
        ((TextView) header.findViewById(R.id.txtEdadMenu))
                .setText(fechaNacimiento != null
                        ? calcularEdadCompleta(fechaNacimiento) : "Edad no disponible");

        txtNombre.setText(nombrePerfil != null ? nombrePerfil : "Sin nombre");
        txtNombre.setTextColor(ContextCompat.getColor(this, R.color.skyblue));
        txtEdad.setText(fechaNacimiento != null
                ? calcularEdadCompleta(fechaNacimiento) : "Edad no disponible");

        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.skyblue));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if ("Mascota".equals(tipoPerfil)) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_centros).setVisible(false);
            menu.findItem(R.id.nav_campanias).setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if      (id == R.id.nav_carnet)    intent = crearIntent(carnet_de_vacunacion.class);
            else if (id == R.id.nav_escanear)  intent = crearIntent(EscanearCarnet.class);
            else if (id == R.id.nav_carnets)   intent = crearIntent(CarnetEscaneados.class);
            else if (id == R.id.nav_centros)   intent = crearIntent(CentrosDeVacunacion.class);
            else if (id == R.id.nav_campanias) intent = crearIntent(Campanias.class);
            else if (id == R.id.nav_pdf)       intent = crearIntent(ExportarPDF.class);
            else if (id == R.id.nav_perfiles) {
                startActivity(new Intent(this, pantalla_dashboard.class));
                finish();
            }

            if (intent != null) startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private Intent crearIntent(Class<?> destino) {
        Intent intent = new Intent(this, destino);
        intent.putExtra("idPerfil",        idPerfil);
        intent.putExtra("nombre",          nombrePerfil);
        intent.putExtra("fechaNacimiento", fechaNacimiento);
        intent.putExtra("sexo",            sexo);
        intent.putExtra("tipoPerfil",      tipoPerfil);
        return intent;
    }

    private String calcularEdadCompleta(String fecha) {
        try {
            SimpleDateFormat sdf = fecha.contains("/")
                    ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    : new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar nac = Calendar.getInstance();
            nac.setTime(sdf.parse(fecha));
            Calendar hoy = Calendar.getInstance();
            int años  = hoy.get(Calendar.YEAR)  - nac.get(Calendar.YEAR);
            int meses = hoy.get(Calendar.MONTH) - nac.get(Calendar.MONTH);
            if (meses < 0) { años--; meses += 12; }
            return años + " años y " + meses + " meses";
        } catch (Exception e) { return "Edad no disponible"; }
    }
}