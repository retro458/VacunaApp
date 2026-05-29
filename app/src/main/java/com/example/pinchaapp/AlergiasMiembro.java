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

public class AlergiasMiembro extends BasePerfilActivity {

    private Spinner spinnerAlergias;
    private MaterialButton btnAgregarAlergia;
    private RecyclerView rvAlergias;

    private ApiService apiService;
    private AlergiaDao alergiaDao;

    private List<AlergiasDto.AlergiaDto> listaCatalogo = new ArrayList<>();
    private List<AlergiasDto.AlergiaDto> listaMiembro  = new ArrayList<>();
    private AlergiaAdapter adapter;



    // ── Obligatorios de la base ──────────────────────────────────────
    @Override
    protected int getLayoutId()      { return R.layout.activity_alergias_miembro; }

    @Override
    protected int getNavItemActivo() { return R.id.nav_alergias; }

    @Override
    protected void onPerfilReady() {

        // API y Room
        apiService = ApiClient.getInstance().create(ApiService.class);
        alergiaDao = VacunAppDatabase.getInstance(this).alergiaDao();

        initViews();
        cargarCatalogo();
        cargarAlergiasMiembro(); // híbrido: API primero, Room si falla
    }

    // ── Cargar alergias: API primero, Room como respaldo ─────────────
    private void cargarAlergiasMiembro() {
        apiService.getAlergiasMiembro(idPerfil)
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

        apiService.asignarAlergia(idPerfil, new AsignarAlergiaDto(idAlergia))
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
        apiService.quitarAlergia(idPerfil, idAlergia)
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



}