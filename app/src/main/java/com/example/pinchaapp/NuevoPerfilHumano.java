package com.example.pinchaapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.dao.PerfilHumanoDao;
import com.example.pinchaapp.database.dao.UsuarioDao;
import com.example.pinchaapp.database.entities.PerfilHumano;
import com.example.pinchaapp.database.entities.Usuario;

import java.util.Calendar;

public class NuevoPerfilHumano extends AppCompatActivity {

    Spinner spSexo;
    EditText etFecha, etNombre;
    LinearLayout layoutEmbarazo;
    Button btnCrearPerfil, btnCancelar;
    Switch swEmbarazo;
    private PerfilHumanoDao perfilHumanoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_perfil_humano);

        // DAO
        perfilHumanoDao = VacunAppDatabase.getInstance(this).perfilHumanoDao();

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
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etFecha.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            this,
                            (view, selectedYear, selectedMonth, selectedDay) -> {

                                String fecha =
                                        selectedDay + "/" +
                                                (selectedMonth + 1) + "/" +
                                                selectedYear;

                                etFecha.setText(fecha);

                            },
                            year,
                            month,
                            day
                    );

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            datePickerDialog.show();
        });


        btnCrearPerfil.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString().trim();
            String fecha = etFecha.getText().toString().trim();
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

            if (fecha.isEmpty()) {
                etFecha.setError("Selecciona una fecha");
                etFecha.requestFocus();
                return;
            }

            // Instanciar el DTO para el Servidor
            com.example.pinchaapp.dto.MiembroDto.CrearMiembroDto nuevoMiembro = new com.example.pinchaapp.dto.MiembroDto.CrearMiembroDto();
            nuevoMiembro.setNombre(nombre);
            nuevoMiembro.setTipo("persona");
            nuevoMiembro.setGenero(sexo);
            nuevoMiembro.setEspecie(null);
            nuevoMiembro.setNumeroDocumento(""); // Opcional, puedes mockearlo o dejarlo vacío
            nuevoMiembro.setFotoUrl("");

            // Convertir la fecha de "dd/MM/yyyy" a "yyyy-MM-dd" para C#
            try {
                java.text.SimpleDateFormat formatoInput = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
                java.text.SimpleDateFormat formatoISO = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
                java.util.Date fechaParseada = formatoInput.parse(fecha);
                nuevoMiembro.setFechaNacimiento(formatoISO.format(fechaParseada));
            } catch (Exception e) {
                Toast.makeText(this, "Error en el formato de fecha", Toast.LENGTH_SHORT).show();
                return;
            }

            // Inicializar ApiService y lanzar POST
            com.example.pinchaapp.network.ApiService api = com.example.pinchaapp.network.ApiClient.getInstance().create(com.example.pinchaapp.network.ApiService.class);
            api.crearMiembro(nuevoMiembro).enqueue(new retrofit2.Callback<com.example.pinchaapp.dto.RespuestaDto<Void>>() {
                @Override
                public void onResponse(retrofit2.Call<com.example.pinchaapp.dto.RespuestaDto<Void>> call, retrofit2.Response<com.example.pinchaapp.dto.RespuestaDto<Void>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                        Toast.makeText(NuevoPerfilHumano.this, "Perfil humano creado correctamente", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(NuevoPerfilHumano.this, pantalla_dashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(NuevoPerfilHumano.this, "Error de servidor al guardar perfil", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<com.example.pinchaapp.dto.RespuestaDto<Void>> call, Throwable t) {
                    Toast.makeText(NuevoPerfilHumano.this, "Falla de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        btnCancelar.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            NuevoPerfilHumano.this,
                            pantalla_dashboard.class
                    )
            );

            finish();

        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        View view = getCurrentFocus();

        if (view instanceof EditText) {

            Rect outRect = new Rect();
            view.getGlobalVisibleRect(outRect);

            if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {

                view.clearFocus();

                InputMethodManager imm =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}