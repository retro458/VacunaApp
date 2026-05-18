package com.example.pinchaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.entities.IMCEntity;


public class RegistroImc extends AppCompatActivity {

    int idPerfil;
    double imcCalculado = 0;
    TextInputEditText etPeso, etAltura;
    TextView txtResultado, txtCategoria;
    MaterialButton btnGuardar;
    VacunAppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_imc);

        // TOOLBAR
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // RECIBIR DATOS
        idPerfil = getIntent().getIntExtra("idPerfil", 0);
        db = VacunAppDatabase.getInstance(this);

        // VISTAS
        etPeso       = findViewById(R.id.etPeso);
        etAltura     = findViewById(R.id.etAltura);
        txtResultado = findViewById(R.id.txtResultadoIMC);
        txtCategoria = findViewById(R.id.txtCategoriaIMC);
        btnGuardar   = findViewById(R.id.btnGuardar);

        // CALCULAR
        findViewById(R.id.btnCalcular).setOnClickListener(v -> calcularIMC());

        // GUARDAR
        btnGuardar.setOnClickListener(v -> guardarIMC());
    }

    private void calcularIMC() {

        String pesoStr   = etPeso.getText().toString().trim();
        String alturaStr = etAltura.getText().toString().trim();

        if (pesoStr.isEmpty()) {
            etPeso.setError("Ingresa tu peso");
            return;
        }
        if (alturaStr.isEmpty()) {
            etAltura.setError("Ingresa tu altura");
            return;
        }

        double peso   = Double.parseDouble(pesoStr);
        double altura = Double.parseDouble(alturaStr) / 100; // cm a metros

        imcCalculado = peso / (altura * altura);

        // Mostrar resultado
        txtResultado.setText(String.format(Locale.getDefault(), "%.1f", imcCalculado));
        txtCategoria.setText(obtenerCategoria(imcCalculado));
        txtResultado.setTextColor(obtenerColor(imcCalculado));

        // Mostrar botón guardar
        btnGuardar.setVisibility(View.VISIBLE);
    }

    private void guardarIMC() {

        String pesoStr = etPeso.getText().toString().trim();
        String alturaStr = etAltura.getText().toString().trim();

        double peso = Double.parseDouble(pesoStr);
        double altura = Double.parseDouble(alturaStr);

        String categoria = obtenerCategoria(imcCalculado);

        String fecha = new SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
        ).format(new Date());

        IMCEntity imc = new IMCEntity(
                idPerfil,
                peso,
                altura,
                imcCalculado,
                categoria,
                fecha
        );

        new Thread(() -> {

            db.imcDao().insertar(imc);

            runOnUiThread(() -> {
                Toast.makeText(this,
                        "IMC guardado",
                        Toast.LENGTH_SHORT).show();

                finish();
            });

        }).start();
    }

    private String obtenerCategoria(double imc) {
        if (imc < 18.5) return "Bajo peso";
        if (imc < 25.0) return "Peso normal";
        if (imc < 30.0) return "Sobrepeso";
        return "Obesidad";
    }

    private int obtenerColor(double imc) {
        if (imc < 18.5) return ContextCompat.getColor(this, R.color.skyblue);
        if (imc < 25.0) return ContextCompat.getColor(this, android.R.color.holo_green_dark);
        if (imc < 30.0) return ContextCompat.getColor(this, android.R.color.holo_orange_dark);
        return ContextCompat.getColor(this, android.R.color.holo_red_dark);
    }
}