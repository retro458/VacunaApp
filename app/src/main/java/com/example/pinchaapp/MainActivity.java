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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.dao.UsuarioDao;
import com.example.pinchaapp.database.entities.Usuario;
import com.example.pinchaapp.dto.AuthResponseDto;
import com.example.pinchaapp.dto.LoginDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.example.pinchaapp.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnCrearCuenta;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // DAO
        usuarioDao = VacunAppDatabase
                .getInstance(this)
                .usuarioDao();

        // Inicializa el SessionManager
        SessionManager.init(this);

        // Si ya hay sesion activa se salta al dashboard
        if (SessionManager.haySesion()) {
            startActivity(new Intent(this, pantalla_dashboard.class));
            finish();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // =========================
            // INTENTO API
            // =========================
            ApiService api = ApiClient.getInstance().create(ApiService.class);
            LoginDto loginDto = new LoginDto(email, password);

            api.login(loginDto).enqueue(new Callback<RespuestaDto<AuthResponseDto>>() {
                @Override
                public void onResponse(Call<RespuestaDto<AuthResponseDto>> call, Response<RespuestaDto<AuthResponseDto>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                        AuthResponseDto data = response.body().getData();
                        SessionManager.guardarSesion(
                                data.getToken(),
                                data.getIdUsuario(),
                                data.getNombre(),
                                data.getCorreo(),
                                data.getRol()
                        );

                        Toast.makeText(MainActivity.this, "Bienvenido " + data.getNombre(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, pantalla_dashboard.class));
                        finish();
                    } else {
                        loginOffline(email, password);
                    }
                }

                @Override
                public void onFailure(Call<RespuestaDto<AuthResponseDto>> call, Throwable t) {
                    // Si falla internet usa Room
                    loginOffline(email, password);
                }
            });
        });



        btnCrearCuenta.setOnClickListener(v ->
                startActivity(new Intent(this, CrearCuenta.class))
        );

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


    private void loginOffline(String email, String password) {
        new Thread(() -> {
            Usuario usuario = usuarioDao.login(email, password);
            runOnUiThread(() -> {
                if (usuario != null) {
                    Toast.makeText(MainActivity.this, "Login offline exitoso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, pantalla_dashboard.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
