package com.example.pinchaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
import com.example.pinchaapp.adapters.VacunaAdapter;
import com.example.pinchaapp.database.entities.IMCEntity;
import com.example.pinchaapp.adapters.ImcAdapter;


public class carnet_de_vacunacion extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    String nombrePerfil;
    String fechaNacimiento;
    String sexo;
    String tipoPerfil;
    int idPerfil;

    TextView txtNombreMenu, txtEdadMenu;
    RecyclerView rvVacunas;
    RecyclerView rvIMC;
    VacunaAdapter adapter;
    ImcAdapter imcAdapter;
    List<VacunaDto> listaVacunas = new ArrayList<>();
    List<IMCEntity> listaIMC = new ArrayList<>();
    VacunAppDatabase db;

    LinearLayout layoutIMC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carnet_de_vacunacion);

        // =========================
        // VISTAS
        // =========================
        drawerLayout   = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar        = findViewById(R.id.toolbar);
        rvVacunas      = findViewById(R.id.rvVacunasComplementarias);
        rvIMC = findViewById(R.id.rvIMCRegistros);

        rvIMC.setLayoutManager(new LinearLayoutManager(this));
        rvIMC.setNestedScrollingEnabled(false);
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
        tipoPerfil = getIntent().getStringExtra("tipoPerfil");

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

        // SI ES MASCOTA
        if (tipoPerfil != null && tipoPerfil.equals("Mascota")) {
            Menu menu = navigationView.getMenu();
            // ocultar opciones humanas
            menu.findItem(R.id.nav_centros).setVisible(false);
            menu.findItem(R.id.nav_campanias).setVisible(false);
            // ocultar botón IMC
            findViewById(R.id.btnIMC).setVisibility(View.GONE);
            //ocultar txtIMC
            findViewById(R.id.txtIMC).setVisibility(View.GONE);
            //Ocultar linearlayout IMC
            findViewById(R.id.layoutEsquemaIMC).setVisibility(View.GONE);
        }

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
                intent.putExtra("tipoPerfil",      tipoPerfil);
                startActivity(intent);

            } else if (id == R.id.nav_escanear) {
                Intent intent = new Intent(this, EscanearCarnet.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                intent.putExtra("tipoPerfil",      tipoPerfil);
                startActivity(intent);

            } else if (id == R.id.nav_carnets) {
                Intent intent = new Intent(this, CarnetEscaneados.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                intent.putExtra("tipoPerfil",      tipoPerfil);
                startActivity(intent);

            } else if (id == R.id.nav_centros) {
                Intent intent = new Intent(this, CentrosDeVacunacion.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_campanias) {
                Intent intent = new Intent(this, Campanias.class);
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
                intent.putExtra("tipoPerfil",      tipoPerfil);
                startActivity(intent);


            } else if (id == R.id.nav_perfiles) {
                startActivity(new Intent(this, pantalla_dashboard.class));
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // =========================
        // RECYCLERVIEW
        // =========================
        rvVacunas.setLayoutManager(new LinearLayoutManager(this));
        rvVacunas.setNestedScrollingEnabled(false);

        // =========================
        // BASE DE DATOS Y BOTONES
        // =========================
        db = VacunAppDatabase.getInstance(this);

        findViewById(R.id.btnVacuna).setOnClickListener(v -> {
            Intent intent = new Intent(this, AgendarVacuna.class);
            intent.putExtra("idPerfil", idPerfil);
            startActivityForResult(intent, 100);
        });

        findViewById(R.id.btnIMC).setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroImc.class);
            intent.putExtra("idPerfil", idPerfil);
            startActivityForResult(intent, 200);
        });



        // =========================
        // CARGAR VACUNAS E IMC
        // =========================
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

                if (completadas == 0 && pendientes == 0) {
                    pieChart.setNoDataText("Sin vacunas registradas");
                    pieChart.invalidate();
                    return;
                }

                List<PieEntry> entries = new ArrayList<>();
                if (completadas > 0)
                    entries.add(new PieEntry(completadas, "Completas"));
                if (pendientes > 0)
                    entries.add(new PieEntry(pendientes, "Pendientes"));

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(
                        ContextCompat.getColor(this, R.color.skyblue),
                        ContextCompat.getColor(this, R.color.blue_light)
                );
                dataSet.setValueTextSize(12f);
                dataSet.setValueTextColor(
                        ContextCompat.getColor(this, R.color.white));

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
    // VOLVER DE AGENDAR VACUNA
    // =========================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            cargarVacunas();
        }

        if (requestCode == 200 && resultCode == RESULT_OK) {
            cargarIMC();
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
                lista.add(new VacunaDto(
                        h.getId(),
                        h.getNombreVacuna(),
                        h.getDosisNumero(),
                        h.getTotalDosis(),
                        h.getFechaAplicacion(),
                        h.getProximaDosis(),
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

        new Thread(() -> {

            List<IMCEntity> registros =
                    db.imcDao().obtenerPorPerfil(idPerfil);

            runOnUiThread(() -> {

                listaIMC.clear();
                listaIMC.addAll(registros);

                if (imcAdapter == null) {

                    imcAdapter =
                            new ImcAdapter(listaIMC);

                    rvIMC.setAdapter(imcAdapter);

                } else {

                    imcAdapter.notifyDataSetChanged();
                }
            });

        }).start();
    }
}
