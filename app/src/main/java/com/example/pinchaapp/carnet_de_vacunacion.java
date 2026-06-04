package com.example.pinchaapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

// Importaciones de Red (Retrofit) y Sesión
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.example.pinchaapp.session.SessionManager;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.dto.HistorialDto;
import com.example.pinchaapp.dto.ImcDto;

import com.example.pinchaapp.adapters.VacunaAdapter;
import com.example.pinchaapp.adapters.ImcAdapter;
import com.example.pinchaapp.database.entities.IMCEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class carnet_de_vacunacion extends AppCompatActivity {

    private static final String API_PDF_URL = "https://api.nodesv.com/api/Certificado/descargar-pdf/";

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    String nombrePerfil, fechaNacimiento, sexo, tipoPerfil;
    int idPerfil;

    TextView txtNombreMenu, txtEdadMenu;
    RecyclerView rvVacunas, rvIMC;
    VacunaAdapter adapter;
    ImcAdapter imcAdapter;

    List<HistorialDto.VacunaHistorialDto> listaVacunas = new ArrayList<>();
    List<ImcDto.ImcResponseDto> listaIMC = new ArrayList<>();

    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carnet_de_vacunacion);

        api = ApiClient.getInstance().create(ApiService.class);

        drawerLayout   = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar        = findViewById(R.id.toolbar);
        rvVacunas      = findViewById(R.id.rvVacunasComplementarias);
        rvIMC          = findViewById(R.id.rvIMCRegistros);

        rvIMC.setLayoutManager(new LinearLayoutManager(this));
        rvIMC.setNestedScrollingEnabled(false);
        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtEdad   = findViewById(R.id.txtEdad);

        View header = navigationView.getHeaderView(0);
        txtNombreMenu = header.findViewById(R.id.txtNombreMenu);
        txtEdadMenu   = header.findViewById(R.id.txtEdadMenu);

        idPerfil        = getIntent().getIntExtra("idPerfil", 0);
        nombrePerfil    = getIntent().getStringExtra("nombre");
        fechaNacimiento = getIntent().getStringExtra("fechaNacimiento");
        sexo            = getIntent().getStringExtra("sexo");
        tipoPerfil      = getIntent().getStringExtra("tipoPerfil");

        txtNombre.setText(nombrePerfil != null ? nombrePerfil : "Sin nombre");
        txtEdad.setText(fechaNacimiento != null ? calcularEdadCompleta(fechaNacimiento) : "Edad no disponible");
        txtNombreMenu.setText(nombrePerfil != null ? nombrePerfil : "Sin nombre");
        txtEdadMenu.setText(fechaNacimiento != null ? calcularEdadCompleta(fechaNacimiento) : "Edad no disponible");

        int color = ContextCompat.getColor(this, R.color.skyblue);
        toolbar.setBackgroundColor(color);
        txtNombre.setTextColor(color);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if ("Mascota".equalsIgnoreCase(tipoPerfil)) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_centros).setVisible(false);
            menu.findItem(R.id.nav_campanias).setVisible(false);
            findViewById(R.id.btnIMC).setVisibility(View.GONE);
            findViewById(R.id.txtIMC).setVisibility(View.GONE);
            findViewById(R.id.layoutEsquemaIMC).setVisibility(View.GONE);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_pdf) {
                intent = new Intent(this, ExportarPDF.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                intent.putExtra("tipoPerfil",      tipoPerfil);
            } else if (id == R.id.nav_proximas_dosis) {
                intent = new Intent(this, ProximasDosisActivity.class);
                intent.putExtra("idPerfil", idPerfil);
                intent.putExtra("nombre",   nombrePerfil);
            } else if (id == R.id.nav_alergias) {
                intent = new Intent(this, AlergiasMiembro.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                intent.putExtra("tipoPerfil",      tipoPerfil);
            } else if (id == R.id.nav_centros) {
                intent = new Intent(this, CentrosDeVacunacion.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
            } else if (id == R.id.nav_campanias) {
                intent = new Intent(this, Campanias.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
            } else if (id == R.id.nav_perfiles) {
                startActivity(new Intent(this, pantalla_dashboard.class));
                finish();
                drawerLayout.closeDrawers();
                return true;
            }

            if (intent != null) startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        });

        rvVacunas.setLayoutManager(new LinearLayoutManager(this));
        rvVacunas.setNestedScrollingEnabled(false);
        rvIMC.setLayoutManager(new LinearLayoutManager(this));
        rvIMC.setNestedScrollingEnabled(false);

        findViewById(R.id.btnVacuna).setOnClickListener(v -> {
            Intent intent = new Intent(this, AgendarVacuna.class);
            intent.putExtra("idPerfil", idPerfil);
            intent.putExtra("tipoMiembro", tipoPerfil != null ? tipoPerfil.toLowerCase() : "humano");
            startActivityForResult(intent, 100);
        });

        findViewById(R.id.btnIMC).setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroImc.class);
            intent.putExtra("idPerfil", idPerfil);
            startActivityForResult(intent, 200);
        });

        cargarVacunasApi();
        cargarIMCApi();
    }

    // ===================================================
    // CARGAR VACUNAS DESDE EL SERVIDOR (.NET 9)
    // ===================================================
    private void cargarVacunasApi() {
        api.getHistorial(idPerfil).enqueue(new Callback<RespuestaDto<List<HistorialDto.HistorialResponseDto>>>() {
            @Override
            public void onResponse(Call<RespuestaDto<List<HistorialDto.HistorialResponseDto>>> call,
                                   Response<RespuestaDto<List<HistorialDto.HistorialResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                    List<HistorialDto.HistorialResponseDto> historialApi = response.body().getData();
                    listaVacunas.clear();

                    java.util.Map<String, Integer> dosisAplicadasPorVacuna = new java.util.HashMap<>();
                    java.util.Map<String, Integer> totalDosisPorVacuna = new java.util.HashMap<>();

                    for (HistorialDto.HistorialResponseDto h : historialApi) {
                        boolean aplicada = (h.getFechaAplicacion() != null && !h.getFechaAplicacion().trim().isEmpty());

                        HistorialDto.VacunaHistorialDto dto = new HistorialDto.VacunaHistorialDto();
                        dto.setId(h.getId());
                        dto.setNombreVacuna(h.getVacuna());
                        dto.setDosisNumero(h.getDosisNumero());
                        dto.setTotalDosis(h.getTotalDosis());

                        if (aplicada) {
                            dto.setFechaAplicacion(formatearFechaIso(h.getFechaAplicacion()));

                            String nombreVac = h.getVacuna();
                            int dosisActual = h.getDosisNumero();
                            int total = h.getTotalDosis();

                            int dosisMaxRegistrada = Math.max(dosisAplicadasPorVacuna.getOrDefault(nombreVac, 0), dosisActual);
                            dosisAplicadasPorVacuna.put(nombreVac, dosisMaxRegistrada);
                            totalDosisPorVacuna.put(nombreVac, total);
                        } else {
                            dto.setFechaAplicacion("No aplicada");
                        }

                        if (h.getProximaDosis() != null && !h.getProximaDosis().trim().isEmpty()) {
                            dto.setProximaDosis(formatearFechaIso(h.getProximaDosis()));
                        } else {
                            dto.setProximaDosis("Dosis única / Al día");
                        }

                        dto.setAplicada(aplicada);
                        listaVacunas.add(dto);
                    }

                    int completadasReales = 0;
                    int pendientesReales = 0;

                    for (String nombreVac : dosisAplicadasPorVacuna.keySet()) {
                        int aplicadas = dosisAplicadasPorVacuna.get(nombreVac);
                        int total = totalDosisPorVacuna.get(nombreVac);

                        completadasReales += aplicadas;
                        if (total > aplicadas) {
                            pendientesReales += (total - aplicadas);
                        }
                    }

                    if (adapter == null) {
                        adapter = new VacunaAdapter(carnet_de_vacunacion.this, listaVacunas, (idHistorial, fecha) -> {
                            Toast.makeText(carnet_de_vacunacion.this, "Requiere endpoint PATCH en la API", Toast.LENGTH_SHORT).show();
                        });
                        rvVacunas.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                    cargarGrafica(completadasReales, pendientesReales);
                } else {
                    Toast.makeText(carnet_de_vacunacion.this, "No se encontraron registros de vacunación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaDto<List<HistorialDto.HistorialResponseDto>>> call, Throwable t) {
                Log.e("RETROFIT_ERROR", "Error de parseo o red: ", t);
                Toast.makeText(carnet_de_vacunacion.this, "Error de comunicación: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // ===================================================
    // CARGAR IMC DESDE EL SERVIDOR
    // ===================================================
    private void cargarIMCApi() {
        api.getHistorialImc(idPerfil).enqueue(new Callback<RespuestaDto<List<ImcDto.ImcResponseDto>>>() {
            @Override
            public void onResponse(Call<RespuestaDto<List<ImcDto.ImcResponseDto>>> call,
                                   Response<RespuestaDto<List<ImcDto.ImcResponseDto>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().isExito()
                            && response.body().getData() != null) {
                        listaIMC.clear();
                        listaIMC.addAll(response.body().getData());

                        if (imcAdapter == null) {
                            imcAdapter = new ImcAdapter(listaIMC);
                            rvIMC.setAdapter(imcAdapter);
                        } else {
                            imcAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(carnet_de_vacunacion.this,
                                "Error en API: " + (response.body() != null ? response.body().getMensaje() : "Sin mensaje"),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    String errorMsg = "Error HTTP " + response.code();
                    if (response.code() == 401) errorMsg = "Sesión expirada. Inicie sesión nuevamente.";
                    Toast.makeText(carnet_de_vacunacion.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaDto<List<ImcDto.ImcResponseDto>>> call, Throwable t) {
                Toast.makeText(carnet_de_vacunacion.this, "Falla de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // ========================= GRÁFICA PIE CHART =========================
    private void cargarGrafica(int completadas, int pendientes) {
        PieChart pieChart = findViewById(R.id.pieChart);

        if (completadas == 0 && pendientes == 0) {
            pieChart.setNoDataText("Sin vacunas registradas");
            pieChart.invalidate();
            return;
        }

        List<PieEntry> entries = new ArrayList<>();
        if (completadas > 0) entries.add(new PieEntry(completadas, "Completas"));
        if (pendientes > 0) entries.add(new PieEntry(pendientes, "Pendientes"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ContextCompat.getColor(this, R.color.skyblue), ContextCompat.getColor(this, R.color.blue_light));
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
    }

    // ========================= DESCARGA PDF NATIVA =========================
    private void ejecutarDescargaPdfDirecta() {
        try {
            String urlCompleta = API_PDF_URL + idPerfil;
            String nombreArchivo = "Carnet_" + (nombrePerfil != null ? nombrePerfil.replace(" ", "_") : "Miembro") + ".pdf";

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlCompleta));

            String token = SessionManager.getToken();
            request.addRequestHeader("Authorization", "Bearer " + token);
            request.addRequestHeader("Content-Type", "application/json");

            request.setTitle("Carnet Digital: " + (nombrePerfil != null ? nombrePerfil : ""));
            request.setDescription("Descargando certificado oficial...");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setMimeType("application/pdf");
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nombreArchivo);

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager != null) {
                manager.enqueue(request);
                Toast.makeText(this, "Descargando carnet de " + nombrePerfil + "...", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error de comunicación: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) cargarVacunasApi();
        if (requestCode == 200 && resultCode == RESULT_OK) cargarIMCApi();
    }

    private String calcularEdadCompleta(String fecha) {
        try {
            SimpleDateFormat sdf = fecha.contains("/")
                    ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    : new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date fechaNac = sdf.parse(fecha);
            Calendar nac = Calendar.getInstance(); nac.setTime(fechaNac);
            Calendar hoy = Calendar.getInstance();
            int años = hoy.get(Calendar.YEAR) - nac.get(Calendar.YEAR);
            int meses = hoy.get(Calendar.MONTH) - nac.get(Calendar.MONTH);
            if (meses < 0) { años--; meses += 12; }
            return años + " años y " + meses + " meses";
        } catch (Exception e) {
            return "Edad no disponible";
        }
    }

    private String formatearFechaIso(String fechaIso) {
        if (fechaIso == null || fechaIso.trim().isEmpty()) return "No registrada";
        try {
            String limpia = fechaIso.split("\\.")[0].replace("Z", "");
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date fecha = parser.parse(limpia);
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return formateador.format(fecha);
        } catch (Exception e) {
            Log.e("FECHA_PARSER", "Error al parsear fecha: " + fechaIso, e);
            return fechaIso;
        }
    }
}
