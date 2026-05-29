package com.example.pinchaapp;

import static android.app.Activity.RESULT_OK;
import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.entities.Vacuna;
import com.example.pinchaapp.database.entities.VacunaHistorial;
import com.example.pinchaapp.dto.VacunaDto;
import com.example.pinchaapp.database.dao.VacunaDao;
import com.example.pinchaapp.adapters.VacunaAdapter;
import com.example.pinchaapp.database.entities.IMCEntity;
import com.example.pinchaapp.adapters.ImcAdapter;

import com.example.pinchaapp.dto.ImcDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class carnet_de_vacunacion extends BasePerfilActivity {

    ApiService apiService;
    RecyclerView rvVacunas, rvIMC;
    VacunaAdapter adapter;
    ImcAdapter imcAdapter;
    List<VacunaDto>  listaVacunas = new ArrayList<>();
    List<IMCEntity>  listaIMC     = new ArrayList<>();
    VacunAppDatabase db;

    // ── Launchers como campos de la clase ──────────────────────────
    private final ActivityResultLauncher<Intent> launcherVacuna =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) cargarVacunas();
                    });

    private final ActivityResultLauncher<Intent> launcherIMC =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) cargarIMC();
                    });

    @Override protected int getLayoutId()      { return R.layout.activity_carnet_de_vacunacion; }
    @Override protected int getNavItemActivo() { return R.id.nav_carnet; }

    @Override
    protected void onPerfilReady() {
        rvVacunas = findViewById(R.id.rvVacunasComplementarias);
        rvIMC     = findViewById(R.id.rvIMCRegistros);
        rvVacunas.setLayoutManager(new LinearLayoutManager(this));
        rvVacunas.setNestedScrollingEnabled(false);
        rvIMC.setLayoutManager(new LinearLayoutManager(this));
        rvIMC.setNestedScrollingEnabled(false);

        apiService = ApiClient.getInstance().create(ApiService.class);
        db         = VacunAppDatabase.getInstance(this);

        findViewById(R.id.btnVacuna).setOnClickListener(v -> {
            Intent intent = new Intent(this, AgendarVacuna.class);
            intent.putExtra("idPerfil", idPerfil);
            launcherVacuna.launch(intent);
        });

        findViewById(R.id.btnIMC).setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroImc.class);
            intent.putExtra("idPerfil", idPerfil);
            launcherIMC.launch(intent);
        });

        cargarVacunas();
        cargarIMC();
    }

    // =========================
    // GRÁFICA PIE CHART
    // =========================
    private void cargarGrafica() {
        new Thread(() -> {
            int completadas = db.vacunaDao().contarCompletadas(idPerfil);
            int pendientes  = db.vacunaDao().contarPendientes(idPerfil);

            runOnUiThread(() -> {
                PieChart pieChart = findViewById(R.id.pieChart);

                // ← PROTECCIÓN: si no existe en el XML no crashea
                if (pieChart == null) return;

                if (completadas == 0 && pendientes == 0) {
                    pieChart.setNoDataText("Sin vacunas registradas");
                    pieChart.invalidate();
                    return;
                }

                List<PieEntry> entries = new ArrayList<>();
                if (completadas > 0) entries.add(new PieEntry(completadas, "Completas"));
                if (pendientes > 0)  entries.add(new PieEntry(pendientes, "Pendientes"));

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(
                        ContextCompat.getColor(this, R.color.skyblue),
                        ContextCompat.getColor(this, R.color.blue_light));
                dataSet.setValueTextSize(12f);
                dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.white));

                PieData data = new PieData(dataSet);
                pieChart.setData(data);
                pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(false);
                pieChart.setHoleRadius(40f);
                pieChart.setTransparentCircleRadius(45f);
                pieChart.setCenterText("Vacunas");
                pieChart.setCenterTextSize(14f);
                pieChart.animateY(800);
                pieChart.invalidate();
            });
        }).start();
    }
    // =========================
    // CARGAR VACUNAS DEL PERFIL
    // =========================
    private void cargarVacunas() {
        new Thread(() -> {
            List<VacunaHistorial> historial =
                    db.vacunaDao().obtenerTodasDeUnPerfil(idPerfil);

            List<VacunaDto> lista = new ArrayList<>();
            for (VacunaHistorial h : historial) {
                boolean aplicada = h.getFechaAplicacion() != null;

                // ← CAMBIO AQUÍ
                int totalDosis = 1;
                try {
                    totalDosis = Integer.parseInt(h.getLote());
                } catch (Exception e) {
                    totalDosis = 1;
                }

                lista.add(new VacunaDto(
                        h.getIdVacuna(),
                        h.getNombreVacuna(),
                        h.getDosisNumero(),
                        totalDosis,
                        h.getFechaAplicacion(),
                        h.getNombreMedico(),
                        h.getObservaciones(),
                        aplicada
                ));
            }

            runOnUiThread(() -> {
                listaVacunas.clear();
                listaVacunas.addAll(lista);

                if (adapter == null) {
                    adapter = new VacunaAdapter(this, listaVacunas,
                            (idHistorial, fecha) -> {
                                new Thread(() -> {
                                    db.vacunaDao().marcarAplicada(idHistorial, fecha);
                                    runOnUiThread(this::cargarVacunas);
                                }).start();
                            });
                    rvVacunas.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }

                cargarGrafica();
            });
        }).start();
    }

    private void cargarIMC() {
        apiService.getHistorialImc(idPerfil).enqueue(new Callback<RespuestaDto<List<ImcDto.ImcResponseDto>>>() {
            @Override
            public void onResponse(Call<RespuestaDto<List<ImcDto.ImcResponseDto>>> call,
                                   Response<RespuestaDto<List<ImcDto.ImcResponseDto>>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isExito()) {

                    List<ImcDto.ImcResponseDto> listaApi = response.body().getData();

                    // Convertir API → IMCEntity para el adapter
                    List<IMCEntity> entidades = new ArrayList<>();
                    for (ImcDto.ImcResponseDto dto : listaApi) {
                        IMCEntity e = new IMCEntity();
                        e.setIdImcApi(dto.getId());
                        e.setIdPerfil(idPerfil);
                        e.setPeso(dto.getPeso());
                        e.setAltura(dto.getAltura());
                        e.setImc(dto.getResultado());
                        e.setCategoria(dto.getClasificacion());
                        e.setFecha(dto.getFecha());
                        entidades.add(e);
                    }

                    // Sincronizar Room
                    new Thread(() -> {
                        db.imcDao().eliminarTodosDePerfil(idPerfil);
                        for (IMCEntity e : entidades) {
                            db.imcDao().insertar(e);
                        }
                    }).start();

                    mostrarIMC(entidades);

                } else {
                    cargarIMCDesdeRoom();
                }
            }

            @Override
            public void onFailure(Call<RespuestaDto<List<ImcDto.ImcResponseDto>>> call, Throwable t) {
                cargarIMCDesdeRoom();
            }
        });
    }

    private void cargarIMCDesdeRoom() {
        new Thread(() -> {
            List<IMCEntity> registros = db.imcDao().obtenerPorPerfil(idPerfil);
            runOnUiThread(() -> {
                mostrarIMC(registros);
                Toast.makeText(this, "Sin conexión — datos locales", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    private void mostrarIMC(List<IMCEntity> registros) {
        listaIMC.clear();
        listaIMC.addAll(registros);
        if (imcAdapter == null) {
            imcAdapter = new ImcAdapter(listaIMC);
            rvIMC.setAdapter(imcAdapter);
        } else {
            imcAdapter.notifyDataSetChanged();
        }
    }
}
