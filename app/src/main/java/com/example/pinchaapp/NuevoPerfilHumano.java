package com.example.pinchaapp;

import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pinchaapp.dto.MiembroDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoPerfilHumano extends AppCompatActivity {

    Spinner spSexo;
    EditText etFecha, etNombre;
    LinearLayout layoutEmbarazo;
    Button btnCrearPerfil, btnCancelar;
    Switch swEmbarazo;
    private ApiService api; // Reemplazamos el DAO local por el servicio de red

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_perfil_humano);

        // Inicializar el cliente API de Retrofit
        api = ApiClient.getInstance().create(ApiService.class);

        spSexo = findViewById(R.id.spSexo);
        etFecha = findViewById(R.id.etFecha);
        etNombre = findViewById(R.id.etNombre);
        layoutEmbarazo = findViewById(R.id.layoutEmbarazo);
        btnCrearPerfil = findViewById(R.id.btnCrearPerfil);
        swEmbarazo = findViewById(R.id.swEmbarazo);
        btnCancelar = findViewById(R.id.btnCancelar);

        String[] opciones = {
                "Seleccionar sexo",
                "Femenino",
                "Masculino"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                opciones
        );

        adapter.setDropDownViewResource(
                R.layout.spinner_dropdown_item
        );

        spSexo.setAdapter(adapter);

        spSexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sexo = parent.getItemAtPosition(position).toString();

                if (sexo.equals("Femenino")) {
                    layoutEmbarazo.setVisibility(View.VISIBLE);
                } else {
                    layoutEmbarazo.setVisibility(View.GONE);
                    swEmbarazo.setChecked(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etFecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String fecha = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etFecha.setText(fecha);
                    },
                    year, month, day
            );

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // ==========================================
        // GUARDAR MIEMBRO VÍA API (Humano)
        // ==========================================
        btnCrearPerfil.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String fechaStr = etFecha.getText().toString().trim();
            String sexo = spSexo.getSelectedItem().toString();

            if (nombre.isEmpty()) {
                etNombre.setError("Ingresa un nombre");
                etNombre.requestFocus();
                return;
            }

            if (sexo.equals("Seleccionar sexo")) {
                Toast.makeText(this, "Selecciona un sexo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fechaStr.isEmpty()) {
                etFecha.setError("Selecciona una fecha");
                etFecha.requestFocus();
                return;
            }

            // Usar tu DTO real y unificado
            MiembroDto.CrearMiembroDto nuevoMiembro = new MiembroDto.CrearMiembroDto();
            nuevoMiembro.setNombre(nombre);
            nuevoMiembro.setTipo("persona");
            nuevoMiembro.setGenero(sexo); // Mapeamos sexo al campo genero de la API
            nuevoMiembro.setEspecie(null); // No aplica para humanos
            nuevoMiembro.setFotoUrl(null);
            nuevoMiembro.setNumeroDocumento(null); // Si el backend lo pide opcional

            // Convertir fecha de dd/MM/yyyy a ISO (yyyy-MM-dd) para C#
            try {
                java.text.SimpleDateFormat formatoInput = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
                java.text.SimpleDateFormat formatoISO = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

                java.util.Date fechaParseada = formatoInput.parse(fechaStr);
                String fechaFormatoAPI = formatoISO.format(fechaParseada);

                nuevoMiembro.setFechaNacimiento(fechaFormatoAPI);
            } catch (Exception e) {
                Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Bloquear botón para evitar doble envío
            btnCrearPerfil.setEnabled(false);

            // Disparar la llamada HTTP POST al C#
            api.crearMiembro(nuevoMiembro).enqueue(new Callback<RespuestaDto<Object>>() {
                @Override
                public void onResponse(Call<RespuestaDto<Object>> call, Response<RespuestaDto<Object>> response) {
                    btnCrearPerfil.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                        Toast.makeText(NuevoPerfilHumano.this, "Perfil creado con éxito", Toast.LENGTH_SHORT).show();

                        // Cerramos el formulario de forma limpia; onResume() en el Dashboard hará el resto.
                        finish();
                    } else {
                        Toast.makeText(NuevoPerfilHumano.this, "Error del servidor al crear perfil", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaDto<Object>> call, Throwable t) {
                    btnCrearPerfil.setEnabled(true);
                    Toast.makeText(NuevoPerfilHumano.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Cancelar cierra el formulario sin hacer nada
        btnCancelar.setOnClickListener(v -> finish());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view instanceof EditText) {
            Rect outRect = new Rect();
            view.getGlobalVisibleRect(outRect);
            if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                view.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}