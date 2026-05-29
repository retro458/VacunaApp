package com.example.pinchaapp;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pinchaapp.dto.AuthDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.dao.UsuarioDao;
import com.example.pinchaapp.database.entities.Usuario;
import com.example.pinchaapp.dto.RegistroDto;


public class CrearCuenta extends AppCompatActivity {

    private EditText etNombre, etApellido, etEmail, etTelefono, etPassword, etConfirmPassword;
    private Button btnCrearCuenta, btnCancelar;
    private UsuarioDao usuarioDao;
    private ApiService api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_cuenta);

        // DAO
        usuarioDao = VacunAppDatabase.getInstance(this).usuarioDao();
        // Inicializar el cliente API de Retrofit
        api = ApiClient.getInstance().create(ApiService.class);

        etNombre          = findViewById(R.id.etNombre);
        etApellido        = findViewById(R.id.etApellido);
        etEmail           = findViewById(R.id.etEmail);
        etTelefono        = findViewById(R.id.etTelefono);
        etPassword        = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCrearCuenta    = findViewById(R.id.btnCrearCuenta);
        btnCancelar       = findViewById(R.id.btnCancelar);


        btnCrearCuenta.setOnClickListener(v -> {
            String nombre   = etNombre.getText().toString().trim();
            String apellido = etApellido.getText().toString().trim();
            String email    = etEmail.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm  = etConfirmPassword.getText().toString().trim();

            // Validaciones
            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) ||
                    TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Ingresa un correo valido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!telefono.isEmpty() && telefono.length() != 8) {
                Toast.makeText(this, "Ingresa un telefono valido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() <= 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirm)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                // Checa si el email ya esta registrado
                if (usuarioDao.existeEmail(email) > 0) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Este correo ya esta registrado", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                RegistroDto registroDto =
                        new RegistroDto(
                                nombre + " " + apellido,
                                email,
                                password,
                                telefono
                        );

                api.registro(registroDto)

                        .enqueue(new Callback<RespuestaDto<Object>>() {

                            @Override
                            public void onResponse(
                                    Call<RespuestaDto<Object>> call,

                                    Response<RespuestaDto<Object>> response
                            ) {

                                if (response.isSuccessful()
                                        && response.body() != null
                                        && response.body().isExito()) {

                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Cuenta creada",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                } else {

                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Error al registrar",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }

                            @Override
                            public void onFailure(
                                    Call<RespuestaDto<Object>> call,
                                    Throwable t
                            ) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Error conexión",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });

                // ENTITY - Fix: Use 5 arguments required by Usuario constructor
                Usuario nuevoUsuario =
                        new Usuario(
                                nombre,
                                apellido,
                                email,
                                telefono,
                                password
                        );

                // DAO
                usuarioDao.insertar(nuevoUsuario);
            }).start();
        });

        btnCancelar.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            CrearCuenta.this,
                            MainActivity.class
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
