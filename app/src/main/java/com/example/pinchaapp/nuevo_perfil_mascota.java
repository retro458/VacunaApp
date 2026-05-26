package com.example.pinchaapp;

import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class nuevo_perfil_mascota extends AppCompatActivity {

    Spinner spEspecie;
    EditText etFecha, etNombre;
    Button btnCrearPerfil, btnCancelar;
    private ApiService api; // Reemplazamos el DAO local por el servicio de red

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_perfil_mascota);

        // Inicializar el cliente API de Retrofit
        api = ApiClient.getInstance().create(ApiService.class);

        spEspecie = findViewById(R.id.spEspecie);
        etFecha = findViewById(R.id.etFecha);
        etNombre = findViewById(R.id.etNombre);
        btnCrearPerfil = findViewById(R.id.btnCrearPerfil);
        btnCancelar = findViewById(R.id.btnCancelar);

        String[] opciones = {
                "Seleccionar Especie",
                "Perro",
                "Gato"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                opciones
        );

        adapter.setDropDownViewResource(
                R.layout.spinner_dropdown_item
        );
        spEspecie.setAdapter(adapter);

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
        // GUARDAR MIEMBRO VÍA API (Mascota)
        // ==========================================
        btnCrearPerfil.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String fechaStr = etFecha.getText().toString().trim();
            String especie = spEspecie.getSelectedItem().toString();

            if (nombre.isEmpty()) {
                etNombre.setError("Ingresa un nombre");
                etNombre.requestFocus();
                return;
            }

            if (especie.equals("Seleccionar Especie")) {
                Toast.makeText(this, "Selecciona una Especie", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fechaStr.isEmpty()) {
                etFecha.setError("Selecciona una fecha");
                etFecha.requestFocus();
                return;
            }

            MiembroDto.CrearMiembroDto nuevaMascota = new MiembroDto.CrearMiembroDto();
            nuevaMascota.setNombre(nombre);
            nuevaMascota.setTipo("mascota");
            nuevaMascota.setEspecie(especie);
            nuevaMascota.setGenero(null);
            nuevaMascota.setFotoUrl(null);

            // Convertir fecha de dd/MM/yyyy a ISO (yyyy-MM-dd) para C#
            try {
                java.text.SimpleDateFormat formatoInput = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
                java.text.SimpleDateFormat formatoISO = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

                java.util.Date fechaParseada = formatoInput.parse(fechaStr);
                String fechaFormatoAPI = formatoISO.format(fechaParseada);

                nuevaMascota.setFechaNacimiento(fechaFormatoAPI);
            } catch (Exception e) {
                Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            btnCrearPerfil.setEnabled(false);

            // Mandar el DTO unificado al endpoint
            api.crearMiembro(nuevaMascota).enqueue(new Callback<RespuestaDto<Object>>() {
                @Override
                public void onResponse(Call<RespuestaDto<Object>> call, Response<RespuestaDto<Object>> response) {
                    btnCrearPerfil.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                        Toast.makeText(nuevo_perfil_mascota.this, "Perfil creado con éxito", Toast.LENGTH_SHORT).show();
                        finish(); // Regresa al Dashboard de forma limpia
                    } else {
                        Toast.makeText(nuevo_perfil_mascota.this, "Error del servidor al crear miembro", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaDto<Object>> call, Throwable t) {
                    btnCrearPerfil.setEnabled(true);
                    Toast.makeText(nuevo_perfil_mascota.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Cancelar simplemente cierra el formulario y regresa al dashboard
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