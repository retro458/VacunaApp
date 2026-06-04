package com.example.pinchaapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pinchaapp.dto.HistorialDto;
import com.example.pinchaapp.dto.MiembroDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.dto.VacunaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendarVacuna extends AppCompatActivity {

    private final boolean MOCK_OFFLINE = false;

    private int idPerfil = -1;
    private String tipoMiembro = "humano";
    private int idVacunaSeleccionada = -1;
    private int idCentro = -1;
    private String nombreCentro;

    private ApiService api;
    private AutoCompleteTextView etMiembro, etNombre;
    private TextInputEditText etCentroSeleccionado, etDosisNumero, etTotalDosis, etProximaDosis, etObservaciones;
    private Button btnGuardar;

    private static class MiembroCombo {
        int id;
        String nombre;
        String tipo;

        MiembroCombo(int id, String nombre, String tipo) {
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
        }

        @Override
        public String toString() { return nombre; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_vacuna);

        api = ApiClient.getInstance().create(ApiService.class);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        idPerfil = getIntent().getIntExtra("idPerfil", -1);
        tipoMiembro = getIntent().getStringExtra("tipoMiembro");
        if (tipoMiembro == null) tipoMiembro = "humano";

        idCentro = getIntent().getIntExtra("idCentro", -1);
        nombreCentro = getIntent().getStringExtra("nombreCentro");

        etCentroSeleccionado = findViewById(R.id.etCentroSeleccionado);
        etMiembro            = findViewById(R.id.etMiembro);
        etNombre             = findViewById(R.id.etNombre);
        etDosisNumero        = findViewById(R.id.etDosisNumero);
        etTotalDosis         = findViewById(R.id.etTotalDosis);
        etProximaDosis       = findViewById(R.id.etProximaDosis);
        etObservaciones      = findViewById(R.id.etObservaciones);
        btnGuardar           = findViewById(R.id.btnGuardar);

        if (nombreCentro != null && !nombreCentro.isEmpty()) {
            etCentroSeleccionado.setText(nombreCentro);
        } else {
            etCentroSeleccionado.setText("No se seleccionó ningún centro");
        }

        cargarMiembrosDelUsuario();

        btnGuardar.setOnClickListener(v -> guardarVacunaAPI());
    }

    private void cargarMiembrosDelUsuario() {
        api.getMiembros().enqueue(new Callback<RespuestaDto<List<MiembroDto.MiembroResponseDto>>>() {
            @Override
            public void onResponse(Call<RespuestaDto<List<MiembroDto.MiembroResponseDto>>> call,
                                   Response<RespuestaDto<List<MiembroDto.MiembroResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                    List<MiembroCombo> listaCombo = new ArrayList<>();

                    for (MiembroDto.MiembroResponseDto m : response.body().getData()) {
                        String tipo = (m.getTipo() != null && m.getTipo().equalsIgnoreCase("mascota")) ? "animal" : "humano";
                        listaCombo.add(new MiembroCombo(m.getId(), m.getNombre(), tipo));
                    }
                    configurarDropdownMiembros(listaCombo);
                } else {
                    Toast.makeText(AgendarVacuna.this, "Error al traer miembros de la familia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaDto<List<MiembroDto.MiembroResponseDto>>> call, Throwable t) {
                Toast.makeText(AgendarVacuna.this, "Error de red al cargar miembros: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarDropdownMiembros(List<MiembroCombo> lista) {
        ArrayAdapter<MiembroCombo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, lista);
        etMiembro.setAdapter(adapter);

        if (idPerfil != -1) {
            for (MiembroCombo m : lista) {
                if (m.id == idPerfil) {
                    etMiembro.setText(m.nombre, false);
                    tipoMiembro = m.tipo;
                    cargarCatalogoVacunas();
                    break;
                }
            }
        }

        etMiembro.setOnItemClickListener((parent, view, position, id) -> {
            MiembroCombo seleccionado = (MiembroCombo) parent.getItemAtPosition(position);
            idPerfil = seleccionado.id;
            tipoMiembro = seleccionado.tipo;

            idVacunaSeleccionada = -1;
            etNombre.setText("");
            etTotalDosis.setText("");
            etProximaDosis.setText("");
            etObservaciones.setText("");

            cargarCatalogoVacunas();
        });
    }

    private void cargarCatalogoVacunas() {
        if (idPerfil == -1) return;

        api.obtenerVacunas(idCentro, tipoMiembro).enqueue(new Callback<RespuestaDto<List<VacunaDto.VacunaResponseDto>>>() {
            @Override
            public void onResponse(Call<RespuestaDto<List<VacunaDto.VacunaResponseDto>>> call,
                                   Response<RespuestaDto<List<VacunaDto.VacunaResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                    configurarDropdownVacunas(response.body().getData());
                } else {
                    Toast.makeText(AgendarVacuna.this, "Sin stock o vacunas disponibles para este perfil", Toast.LENGTH_SHORT).show();
                    etNombre.setAdapter(null);
                }
            }

            @Override
            public void onFailure(Call<RespuestaDto<List<VacunaDto.VacunaResponseDto>>> call, Throwable t) {
                Toast.makeText(AgendarVacuna.this, "Error de comunicación con el catálogo de vacunas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarDropdownVacunas(List<VacunaDto.VacunaResponseDto> listaVacunas) {
        ArrayAdapter<VacunaDto.VacunaResponseDto> adapter = new ArrayAdapter<>(
                AgendarVacuna.this, android.R.layout.simple_dropdown_item_1line, listaVacunas);
        etNombre.setAdapter(adapter);

        etNombre.setOnItemClickListener((parent, view, position, id) -> {
            VacunaDto.VacunaResponseDto seleccionada = (VacunaDto.VacunaResponseDto) parent.getItemAtPosition(position);
            idVacunaSeleccionada = seleccionada.getId();
            cargarEsquemaDeVacuna(seleccionada.getId());
        });
    }

    private void cargarEsquemaDeVacuna(int idVacuna) {
        api.obtenerEsquemaVacuna(idVacuna).enqueue(new Callback<VacunaDto.EsquemaVacunaDto>() {
            @Override
            public void onResponse(Call<VacunaDto.EsquemaVacunaDto> call,
                                   Response<VacunaDto.EsquemaVacunaDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VacunaDto.EsquemaVacunaDto esquema = response.body();
                    etTotalDosis.setText(String.valueOf(esquema.getNumeroDosis()));

                    Integer intervalo = esquema.getIntervaloDias();
                    if (intervalo != null && intervalo > 0) {
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_YEAR, intervalo);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        etProximaDosis.setText(sdf.format(c.getTime()));
                    } else {
                        etProximaDosis.setText("");
                    }
                    etObservaciones.setText(esquema.getDescripcion() != null ? esquema.getDescripcion() : "");
                }
            }

            @Override
            public void onFailure(Call<VacunaDto.EsquemaVacunaDto> call, Throwable t) {
                // Falla silenciosa
            }
        });
    }

    private void guardarVacunaAPI() {
        String dosisStr = etDosisNumero.getText().toString().trim();
        String observaciones = etObservaciones.getText().toString().trim();

        if (idPerfil == -1) {
            etMiembro.setError("Selecciona el paciente");
            return;
        }
        if (idVacunaSeleccionada == -1) {
            etNombre.setError("Selecciona una vacuna");
            return;
        }
        if (dosisStr.isEmpty()) {
            etDosisNumero.setError("Ingresa la dosis actual");
            return;
        }

        HistorialDto.RegistrarVacunacionDto requestDto = new HistorialDto.RegistrarVacunacionDto();
        requestDto.setIdMiembro(idPerfil);
        requestDto.setIdVacuna(idVacunaSeleccionada);
        requestDto.setDosisNumero(Integer.parseInt(dosisStr));
        requestDto.setObservaciones(observaciones.isEmpty() ? null : observaciones);
        requestDto.setIdCentro(idCentro != -1 ? idCentro : null);
        requestDto.setLote("LOTE-SISTEMA");
        requestDto.setNombreMedico("Asignado de Catálogo");

        try {
            SimpleDateFormat formatoISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            requestDto.setFechaAplicacion(formatoISO.format(new Date()));
        } catch (Exception e) {
            Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        btnGuardar.setEnabled(false);

        api.registrarVacunacion(requestDto).enqueue(new Callback<RespuestaDto<Object>>() {
            @Override
            public void onResponse(Call<RespuestaDto<Object>> call,
                                   Response<RespuestaDto<Object>> response) {
                btnGuardar.setEnabled(true);
                if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                    Toast.makeText(AgendarVacuna.this, "Cita agendada con éxito", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AgendarVacuna.this, "Error: " + response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaDto<Object>> call, Throwable t) {
                btnGuardar.setEnabled(true);
                Toast.makeText(AgendarVacuna.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatePicker() {
        Calendar calendario = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) -> {
                    String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
                    etProximaDosis.setText(fecha);
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
