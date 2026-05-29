package com.example.pinchaapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.entities.VacunaHistorial;
import com.example.pinchaapp.dto.VacunaDto;
import com.example.pinchaapp.dto.RegistrarVacunacionDto;
import com.example.pinchaapp.dto.CentroDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AgendarVacuna extends AppCompatActivity {

    // ── Vistas ───────────────────────────────────────────────────────
    private Spinner spVacunas, spCentros, spDosisNumero, spTotalDosis, spMl;
    private TextInputEditText etFechaAplicacion, etMedico;
    private MaterialButton btnGuardar, btnCancelar;

    // ── Datos ────────────────────────────────────────────────────────
    private int idPerfil;
    private String fechaNacimiento, sexo, tipoPerfil;

    // ── API y Room ───────────────────────────────────────────────────
    private ApiService apiService;
    private VacunAppDatabase db;

    // ── Listas ──────────────────────────────────────────────────────
    private List<VacunaDto> listaVacunasFiltradas = new ArrayList<>();
    private List<CentroDto> listaCentros          = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_vacuna);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Datos del Intent
        idPerfil        = getIntent().getIntExtra("idPerfil", 0);
        fechaNacimiento = getIntent().getStringExtra("fechaNacimiento");
        sexo            = getIntent().getStringExtra("sexo");
        tipoPerfil      = getIntent().getStringExtra("tipoPerfil");

        // API y Room
        apiService = ApiClient.getInstance().create(ApiService.class);
        db         = VacunAppDatabase.getInstance(this);

        initViews();
        cargarVacunas();
        cargarCentros();
        configurarSpinnersEstaticos();
    }

    // ── Vistas ───────────────────────────────────────────────────────
    private void initViews() {
        spVacunas        = findViewById(R.id.spVacunas);
        spCentros        = findViewById(R.id.spCentros);
        spDosisNumero    = findViewById(R.id.spDosisNumero);
        spTotalDosis     = findViewById(R.id.spTotalDosis);
        spMl             = findViewById(R.id.spMl);
        etFechaAplicacion = findViewById(R.id.etFechaAplicacion);
        etMedico         = findViewById(R.id.etMedico);
        btnGuardar       = findViewById(R.id.btnGuardar);
        btnCancelar      = findViewById(R.id.btnCancelar);

        // DatePicker
        etFechaAplicacion.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this,
                    (view, year, month, day) -> {
                        String fecha = String.format(
                                Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
                        etFechaAplicacion.setText(fecha);
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        btnGuardar.setOnClickListener(v -> guardarVacuna());
        btnCancelar.setOnClickListener(v -> finish());
    }

    // ── Spinners estáticos ───────────────────────────────────────────
    private void configurarSpinnersEstaticos() {
        // Dosis número 1-10
        List<String> dosis = new ArrayList<>();
        for (int i = 1; i <= 10; i++) dosis.add(String.valueOf(i));
        spDosisNumero.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dosis));

        // Total dosis 1-10 + Dosis Única
        List<String> totalDosis = new ArrayList<>();
        totalDosis.add("Dosis Única");
        for (int i = 1; i <= 10; i++) totalDosis.add(String.valueOf(i));
        spTotalDosis.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, totalDosis));

        // ML
        List<String> ml = new ArrayList<>(Arrays.asList("0.05 ml", "0.5 ml", "1.0 ml"));
        spMl.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ml));
    }

    // ── Esquemas ─────────────────────────────────────────────────────
    private String determinarEsquema() {
        if ("Mascota".equals(tipoPerfil)) return "Esquema Mascota";

        int edadMeses = calcularEdadEnMeses(fechaNacimiento);
        int edadAnios = edadMeses / 12;

        if (edadMeses == 0)          return "Esquema Recién Nacidos";
        if (edadMeses <= 2)          return "Esquema Lactante (2 meses)";
        if (edadMeses <= 4)          return "Esquema Lactante (4 meses)";
        if (edadMeses <= 6)          return "Esquema Lactante (6 meses)";
        if (edadMeses <= 12)         return "Esquema Bebé (12 meses)";
        if (edadMeses <= 15)         return "Esquema Bebé (15 meses)";
        if (edadMeses <= 18)         return "Esquema Bebé (18 meses)";
        if (edadAnios <= 4)          return "Esquema Infantil (4 años)";
        if (edadAnios <= 18) {
            if ("F".equalsIgnoreCase(sexo) || "Femenino".equalsIgnoreCase(sexo))
                return "Esquema Niñas (9 – 18 años)";
            return "Esquema Niños (9 – 18 años)";
        }
        if (edadAnios >= 60)         return "Esquema Adultos Mayores";
        if ("F".equalsIgnoreCase(sexo) || "Femenino".equalsIgnoreCase(sexo)) {
            if (edadAnios <= 45)     return "Esquema Mujeres (19 – 45 años)";
            return "Esquema Adultos Mayores";
        }
        return "Esquema Adultos Mayores";
    }

    private int calcularEdadEnMeses(String fecha) {
        try {
            SimpleDateFormat sdf = fecha.contains("/")
                    ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    : new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar nac = Calendar.getInstance();
            nac.setTime(sdf.parse(fecha));
            Calendar hoy = Calendar.getInstance();
            int anios = hoy.get(Calendar.YEAR)  - nac.get(Calendar.YEAR);
            int meses = hoy.get(Calendar.MONTH) - nac.get(Calendar.MONTH);
            return anios * 12 + meses;
        } catch (Exception e) { return 0; }
    }

    // ── Cargar vacunas desde API filtradas por esquema ───────────────
    private void cargarVacunas() {
        String tipo = "Mascota".equals(tipoPerfil) ? "mascota" : "humano";

        apiService.getVacunasPorTipo(tipo).enqueue(
                new Callback<RespuestaDto<List<VacunaDto>>>() {
                    @Override
                    public void onResponse(Call<RespuestaDto<List<VacunaDto>>> call,
                                           Response<RespuestaDto<List<VacunaDto>>> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isExito()) {

                            String esquema = determinarEsquema();
                            List<VacunaDto> todas = response.body().getData();

                            // Filtrar por esquema — observaciones debe coincidir
                            listaVacunasFiltradas.clear();
                            for (VacunaDto v : todas) {
                                if (v.getObservaciones() != null
                                        && v.getObservaciones().equalsIgnoreCase(esquema)) {
                                    listaVacunasFiltradas.add(v);
                                }
                            }

                            // Si no hay vacunas del esquema, mostrar todas
                            if (listaVacunasFiltradas.isEmpty()) {
                                listaVacunasFiltradas.addAll(todas);
                            }

                            List<String> nombres = new ArrayList<>();
                            for (VacunaDto v : listaVacunasFiltradas) {
                                nombres.add(v.getNombreVacuna());
                            }

                            runOnUiThread(() -> spVacunas.setAdapter(new ArrayAdapter<>(
                                    AgendarVacuna.this,
                                    android.R.layout.simple_spinner_item, nombres)));

                        } else {
                            Toast.makeText(AgendarVacuna.this,
                                    "Error cargando vacunas", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaDto<List<VacunaDto>>> call, Throwable t) {
                        Toast.makeText(AgendarVacuna.this,
                                "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ── Cargar centros desde API ──────────────────────────────────────
    private void cargarCentros() {
        apiService.getCentros().enqueue(new Callback<RespuestaDto<List<CentroDto>>>() {
            @Override
            public void onResponse(Call<RespuestaDto<List<CentroDto>>> call,
                                   Response<RespuestaDto<List<CentroDto>>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isExito()) {

                    listaCentros = response.body().getData();
                    List<String> nombres = new ArrayList<>();
                    for (CentroDto c : listaCentros) nombres.add(c.getNombre());

                    runOnUiThread(() -> spCentros.setAdapter(new ArrayAdapter<>(
                            AgendarVacuna.this,
                            android.R.layout.simple_spinner_item, nombres)));
                }
            }

            @Override
            public void onFailure(Call<RespuestaDto<List<CentroDto>>> call, Throwable t) {
                Toast.makeText(AgendarVacuna.this,
                        "Sin conexión al cargar centros", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ── Guardar: API + Room ──────────────────────────────────────────
    private void guardarVacuna() {
        // Validaciones
        if (listaVacunasFiltradas.isEmpty()) {
            Toast.makeText(this, "No hay vacunas disponibles", Toast.LENGTH_SHORT).show();
            return;
        }
        if (listaCentros.isEmpty()) {
            Toast.makeText(this, "No hay centros disponibles", Toast.LENGTH_SHORT).show();
            return;
        }
        String fecha = etFechaAplicacion.getText().toString().trim();
        if (fecha.isEmpty()) {
            etFechaAplicacion.setError("Seleccioná una fecha");
            return;
        }
        String medico = etMedico.getText().toString().trim();
        if (medico.isEmpty()) {
            etMedico.setError("Ingresá el nombre del médico");
            return;
        }

        // Datos seleccionados
        VacunaDto vacunaSeleccionada = listaVacunasFiltradas.get(spVacunas.getSelectedItemPosition());
        CentroDto centroSeleccionado = listaCentros.get(spCentros.getSelectedItemPosition());
        int dosisNumero              = spDosisNumero.getSelectedItemPosition() + 1;
        String lote                  = spTotalDosis.getSelectedItem().toString(); // total dosis → lote
        String ml                    = spMl.getSelectedItem().toString();         // ml → observaciones

        RegistrarVacunacionDto request = new RegistrarVacunacionDto(
                idPerfil,
                vacunaSeleccionada.getId(),
                centroSeleccionado.getIdCentro(),
                fecha,
                dosisNumero,
                lote,
                medico,
                ml
        );

        btnGuardar.setEnabled(false);

        apiService.registrarVacunacion(request).enqueue(new Callback<RespuestaDto<Object>>() {
            @Override
            public void onResponse(Call<RespuestaDto<Object>> call,
                                   Response<RespuestaDto<Object>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isExito()) {

                    // Guardar en Room también
                    VacunaHistorial historial = new VacunaHistorial(
                            idPerfil,
                            vacunaSeleccionada.getId(),
                            centroSeleccionado.getIdCentro(),
                            vacunaSeleccionada.getNombreVacuna(),
                            centroSeleccionado.getNombre(),
                            fecha, dosisNumero, lote, medico, ml
                    );

                    new Thread(() -> {
                        db.vacunaDao().insertarHistorial(historial);
                        runOnUiThread(() -> {
                            Toast.makeText(AgendarVacuna.this,
                                    "Vacuna registrada", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        });
                    }).start();

                } else {
                    try {
                        String error = response.errorBody().string();
                        Toast.makeText(AgendarVacuna.this,
                                "Error: " + error, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(AgendarVacuna.this,
                                "No se pudo registrar", Toast.LENGTH_SHORT).show();
                    }
                    btnGuardar.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<RespuestaDto<Object>> call, Throwable t) {
                // Sin internet → guardar solo local
                VacunaHistorial historial = new VacunaHistorial(
                        idPerfil,
                        vacunaSeleccionada.getId(),
                        centroSeleccionado.getIdCentro(),
                        vacunaSeleccionada.getNombreVacuna(),
                        centroSeleccionado.getNombre(),
                        fecha, dosisNumero, lote, medico, ml
                );

                new Thread(() -> {
                    db.vacunaDao().insertarHistorial(historial);
                    runOnUiThread(() -> {
                        Toast.makeText(AgendarVacuna.this,
                                "Sin conexión — guardado localmente",
                                Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    });
                }).start();
            }
        });
    }
}