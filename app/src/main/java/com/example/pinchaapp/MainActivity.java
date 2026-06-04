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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.dao.UsuarioDao;
import com.example.pinchaapp.database.entities.Usuario;
import com.example.pinchaapp.dto.AuthDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.example.pinchaapp.session.SessionManager;

// IMPORTS PARA GOOGLE SIGN IN
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.SignInButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnCrearCuenta, btnGoogle; // Añadido btnGoogle
    private UsuarioDao usuarioDao;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // DAO e inicialización de sesión
        usuarioDao = VacunAppDatabase.getInstance(this).usuarioDao();
        SessionManager.init(this);

        if (SessionManager.haySesion()) {
            startActivity(new Intent(this, pantalla_dashboard.class));
            finish();
            return;
        }

        // Inicializar vistas
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        SignInButton btnGoogle = findViewById(R.id.btnGoogle);

        // Configurar credenciales de Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Usa el string de la API de Google Cloud
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // ==========================================
        // 1. CAMINO TRADICIONAL (CORREGIDO)
        // ==========================================
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService api = ApiClient.getInstance().create(ApiService.class);

            // SOLUCIÓN: Instanciamos la clase interna LoginDto en lugar de la maestra
            AuthDto.LoginDto loginBody = new AuthDto.LoginDto(email, password);

            api.login(loginBody).enqueue(new Callback<RespuestaDto<AuthDto.AuthResponseDto>>() {
                @Override
                public void onResponse(Call<RespuestaDto<AuthDto.AuthResponseDto>> call, Response<RespuestaDto<AuthDto.AuthResponseDto>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                        AuthDto.AuthResponseDto data = response.body().getData();
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
                public void onFailure(Call<RespuestaDto<AuthDto.AuthResponseDto>> call, Throwable t) {
                    loginOffline(email, password);
                }
            });
        });

        // ==========================================
        // 2. CAMINO GOOGLE SIGN IN
        // ==========================================
        if (btnGoogle != null) {
            btnGoogle.setOnClickListener(v -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            });
        }

        btnCrearCuenta.setOnClickListener(v -> startActivity(new Intent(this, CrearCuenta.class)));
    }

    // Captura la respuesta de Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null && account.getIdToken() != null) {
                    // Mandar el IdToken al backend de tu equipo
                    loginConBackendGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Error al autenticar con Google: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginConBackendGoogle(String idToken) {
        ApiService api = ApiClient.getInstance().create(ApiService.class);
        AuthDto.GoogleAuthDto googleDto = new AuthDto.GoogleAuthDto(idToken);

        api.loginGoogle(googleDto).enqueue(new Callback<RespuestaDto<AuthDto.AuthResponseDto>>() {
            @Override
            public void onResponse(Call<RespuestaDto<AuthDto.AuthResponseDto>> call, Response<RespuestaDto<AuthDto.AuthResponseDto>> response) {

                // 1. Petición exitosa (HTTP 200) y lógica de negocio correcta
                if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                    AuthDto.AuthResponseDto data = response.body().getData();
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
                    return;
                }

                // 2. Control limpio de errores basado en códigos de estado HTTP
                String mensajeUsuario;
                switch (response.code()) {
                    case 401: // Unauthorized
                        mensajeUsuario = "Sesión de Google inválida o expirada.";
                        break;
                    case 403: // Forbidden
                        mensajeUsuario = "Acceso denegado. Usuario no autorizado.";
                        break;
                    case 404: // Not Found
                        mensajeUsuario = "Servicio de autenticación no encontrado.";
                        break;
                    case 409: // Conflict (Por ejemplo, problemas controlados de BD)
                        mensajeUsuario = "Conflicto al registrar la cuenta. Intente de nuevo.";
                        break;
                    case 500: // Internal Server Error
                        mensajeUsuario = "Error interno en el servidor. Inténtelo más tarde.";
                        break;
                    default:
                        mensajeUsuario = "Error inesperado (Código " + response.code() + ").";
                        break;
                }

                Toast.makeText(MainActivity.this, mensajeUsuario, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RespuestaDto<AuthDto.AuthResponseDto>> call, Throwable t) {
                // Error de red limpio (Sin Internet, servidor apagado, etc.)
                Toast.makeText(MainActivity.this, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
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
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas o sin red", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
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
                if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
