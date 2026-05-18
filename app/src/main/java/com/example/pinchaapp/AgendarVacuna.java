package com.example.pinchaapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;
import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.entities.VacunaHistorial;

public class AgendarVacuna extends AppCompatActivity {

    int idPerfil;
    VacunAppDatabase db;

    TextInputEditText etNombre, etDosisNumero, etTotalDosis,
            etProximaDosis, etObservaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_vacuna);

        // TOOLBAR
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // RECIBIR DATOS
        idPerfil = getIntent().getIntExtra("idPerfil", 0);
        db = VacunAppDatabase.getInstance(this);

        // VISTAS
        etNombre        = findViewById(R.id.etNombre);
        etDosisNumero   = findViewById(R.id.etDosisNumero);
        etTotalDosis    = findViewById(R.id.etTotalDosis);
        etProximaDosis  = findViewById(R.id.etProximaDosis);
        etObservaciones = findViewById(R.id.etObservaciones);

        // DATE PICKER PRÓXIMA DOSIS
        etProximaDosis.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();
            new DatePickerDialog(this,
                    (view, year, month, day) -> {
                        String fecha = String.format(
                                Locale.getDefault(),
                                "%02d/%02d/%04d", day, month + 1, year);
                        etProximaDosis.setText(fecha);
                    },
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // BOTON GUARDAR
        findViewById(R.id.btnGuardar).setOnClickListener(v -> guardarVacuna());
    }

    private void guardarVacuna() {

        String nombre       = etNombre.getText().toString().trim();
        String dosisStr     = etDosisNumero.getText().toString().trim();
        String totalStr     = etTotalDosis.getText().toString().trim();
        String proxima      = etProximaDosis.getText().toString().trim();
        String observaciones = etObservaciones.getText().toString().trim();

        // VALIDACIONES
        if (nombre.isEmpty()) {
            etNombre.setError("Ingresa el nombre de la vacuna");
            return;
        }
        if (dosisStr.isEmpty()) {
            etDosisNumero.setError("Ingresa el número de dosis");
            return;
        }
        if (totalStr.isEmpty()) {
            etTotalDosis.setError("Ingresa el total de dosis");
            return;
        }

        int dosisNumero = Integer.parseInt(dosisStr);
        int totalDosis  = Integer.parseInt(totalStr);

        VacunaHistorial historial = new VacunaHistorial(
                idPerfil,
                nombre,
                dosisNumero,
                totalDosis,
                proxima.isEmpty()      ? null : proxima,
                observaciones.isEmpty() ? null : observaciones
        );

        new Thread(() -> {
            db.vacunaDao().insertarHistorial(historial);
            runOnUiThread(() -> {
                Toast.makeText(this,
                        "Vacuna guardada", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }
}
