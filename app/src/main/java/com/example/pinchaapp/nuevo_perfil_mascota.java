package com.example.pinchaapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.dao.PerfilHumanoDao;
import com.example.pinchaapp.database.dao.PerfilMascotaDao;
import com.example.pinchaapp.database.entities.PerfilHumano;
import com.example.pinchaapp.database.entities.PerfilMascota;

import java.util.Calendar;

public class nuevo_perfil_mascota extends AppCompatActivity {

    Spinner spEspecie;
    EditText etFecha, etNombre;
    Button btnCrearPerfil, btnCancelar;
    private PerfilMascotaDao perfilMascotaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_perfil_mascota);

        // DAO
        perfilMascotaDao = VacunAppDatabase.getInstance(this).perfilMascotaDao();

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
            String especie = spEspecie.getSelectedItem().toString();

            if (nombre.isEmpty()) {
                etNombre.setError("Ingresa uqn nombre");
                etNombre.requestFocus();
                return;
            }

            if (especie.equals("Seleccionar Especie")) {
                Toast.makeText(
                        this,
                        "Selecciona una Especie",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (fecha.isEmpty()) {
                etFecha.setError("Selecciona una fecha");
                etFecha.requestFocus();
                return;
            }
            // Insertar perfil en Room
            PerfilMascota nuevoPerfil = new PerfilMascota(nombre, especie, fecha);
            perfilMascotaDao.insertarPerfil(nuevoPerfil);

            Toast.makeText(
                    nuevo_perfil_mascota.this,
                    "Perfil creado correctamente",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    nuevo_perfil_mascota.this,
                    pantalla_dashboard.class
            );

            startActivity(intent);

            finish();

        });
        btnCancelar.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            nuevo_perfil_mascota.this,
                            pantalla_dashboard.class
                    )
            );

            finish();

        });
    }
}