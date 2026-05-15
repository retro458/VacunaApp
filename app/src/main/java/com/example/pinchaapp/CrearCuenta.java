package com.example.pinchaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.dao.UsuarioDao;
import com.example.pinchaapp.database.entities.Usuario;

public class CrearCuenta extends AppCompatActivity {

    private EditText etNombre, etApellido, etEmail, etTelefono, etPassword, etConfirmPassword;
    private Button btnCrearCuenta;
    private UsuarioDao usuarioDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_cuenta);

        // DAO
        usuarioDao = VacunAppDatabase.getInstance(this).usuarioDao();

        etNombre          = findViewById(R.id.etNombre);
        etApellido        = findViewById(R.id.etApellido);
        etEmail           = findViewById(R.id.etEmail);
        etTelefono        = findViewById(R.id.etTelefono);
        etPassword        = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCrearCuenta    = findViewById(R.id.btnCrearCuenta);

        btnCrearCuenta.setOnClickListener(v -> {
            String nombre   = etNombre.getText().toString().trim();
            String apellido = etApellido.getText().toString().trim();
            String email    = etEmail.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm  = etConfirmPassword.getText().toString().trim();

            // Validaciones
            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) ||
                    TextUtils.isEmpty(email)  || TextUtils.isEmpty(telefono) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Ingresa un correo valido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (telefono.length() != 8) {
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

                // Insertar usuario en Room
                Usuario nuevoUsuario = new Usuario(nombre, apellido, email, telefono, password);
                usuarioDao.insertar(nuevoUsuario);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, infoApp.class));
                    finish();
                });
            }).start();
        });
    }

}