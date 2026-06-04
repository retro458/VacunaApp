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
import com.example.pinchaapp.dto.ImcDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistroImc extends AppCompatActivity {

    int idPerfil;
    double imcCalculado = 0;
    TextInputEditText etPeso, etAltura;
    TextView txtResultado, txtCategoria;
    MaterialButton btnGuardar;
    VacunAppDatabase db;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_imc);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        idPerfil   = getIntent().getIntExtra("idPerfil", 0);
        db         = VacunAppDatabase.getInstance(this);
        apiService = ApiClient.getInstance().create(ApiService.class);

        etPeso       = findViewById(R.id.etPeso);
        etAltura     = findViewById(R.id.etAltura);
        txtResultado = findViewById(R.id.txtResultadoIMC);
        txtCategoria = findViewById(R.id.txtCategoriaIMC);
        btnGuardar   = findViewById(R.id.btnGuardar);

        findViewById(R.id.btnCalcular).setOnClickListener(v -> calcularIMC());
        btnGuardar.setOnClickListener(v -> guardarIMC());
    }

    private void calcularIMC() {
        String pesoStr   = etPeso.getText().toString().trim();
        String alturaStr = etAltura.getText().toString().trim();

        if (pesoStr.isEmpty()) { etPeso.setError("Ingresa tu peso"); return; }
        if (alturaStr.isEmpty()) { etAltura.setError("Ingresa tu altura"); return; }

        double peso   = Double.parseDouble(pesoStr);
        double altura = Double.parseDouble(alturaStr) / 100;
        imcCalculado  = peso / (altura * altura);

        txtResultado.setText(String.format(Locale.getDefault(), "%.1f", imcCalculado));
        txtCategoria.setText(obtenerCategoria(imcCalculado));
        txtResultado.setTextColor(obtenerColor(imcCalculado));
        btnGuardar.setVisibility(View.VISIBLE);
    }

    private void guardarIMC() {
        if (idPerfil == 0) {
            Toast.makeText(this, "Error: perfil no válido", Toast.LENGTH_SHORT).show();
            return;
        }
        String pesoStr   = etPeso.getText().toString().trim();
        String alturaStr = etAltura.getText().toString().trim();
        if (pesoStr.isEmpty() || alturaStr.isEmpty()) return;

        double peso   = Double.parseDouble(pesoStr);
        double altura = Double.parseDouble(alturaStr) / 100; // metros para la API

        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        // Construir request para la API
        ImcDto.RegistrarImcDto request = new ImcDto.RegistrarImcDto();
        request.setIdMiembro(idPerfil);
        request.setPeso(peso);
        request.setAltura(altura);
        request.setFecha(fechaHoy);

        btnGuardar.setEnabled(false);

        apiService.registrarImc(request).enqueue(new Callback<RespuestaDto<ImcDto.ImcResponseDto>>() {
            @Override
            public void onResponse(Call<RespuestaDto<ImcDto.ImcResponseDto>> call,
                                   Response<RespuestaDto<ImcDto.ImcResponseDto>> response) {

                // ← AGREGAR ESTO
                try {
                    if (!response.isSuccessful()) {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(RegistroImc.this,
                                "Error " + response.code() + ": " + errorBody,
                                Toast.LENGTH_LONG).show();
                        btnGuardar.setEnabled(true);
                        return;
                    }
                } catch (Exception e) { e.printStackTrace(); }

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getData() != null) {

                    ImcDto.ImcResponseDto resultado = response.body().getData();

                    // Guardar en Room con el id de la API y clasificacion que calculó el servidor
                    IMCEntity entity = new IMCEntity(
                            idPerfil,
                            resultado.getId(),
                            peso,
                            altura * 100,
                            resultado.getResultado(),
                            resultado.getClasificacion(),
                            fechaHoy
                    );

                    new Thread(() -> {
                        db.imcDao().insertar(entity);
                        runOnUiThread(() -> {
                            Toast.makeText(RegistroImc.this,
                                    "IMC guardado", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        });
                    }).start();

                } else {
                    guardarSoloLocal(peso, altura * 100, fechaHoy);
                }
            }

            @Override
            public void onFailure(Call<RespuestaDto<ImcDto.ImcResponseDto>> call, Throwable t) {
                guardarSoloLocal(peso, altura * 100, fechaHoy);
            }
        });
    }

    private void guardarSoloLocal(double peso, double alturaCm, String fecha) {
        IMCEntity entity = new IMCEntity(
                idPerfil, 0, peso, alturaCm,
                imcCalculado, obtenerCategoria(imcCalculado), fecha
        );
        new Thread(() -> {
            db.imcDao().insertar(entity);
            runOnUiThread(() -> {
                Toast.makeText(this,
                        "Sin conexión — guardado localmente",
                        Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
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
