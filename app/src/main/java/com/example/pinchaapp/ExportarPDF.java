package com.example.pinchaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pinchaapp.database.VacunAppDatabase;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExportarPDF extends AppCompatActivity {
    // ========================= PUEDE CAMBIAR
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    String nombrePerfil;
    String fechaNacimiento;
    String sexo;
    int idPerfil;

    TextView txtNombreMenu, txtEdadMenu;
    VacunAppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exportar_pdf);

        // =========================
        // VISTAS
        // =========================
        drawerLayout   = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar        = findViewById(R.id.toolbar);
        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtEdad   = findViewById(R.id.txtEdad);

        View header = navigationView.getHeaderView(0);
        txtNombreMenu = header.findViewById(R.id.txtNombreMenu);
        txtEdadMenu   = header.findViewById(R.id.txtEdadMenu);

        // =========================
        // RECIBIR DATOS
        // =========================
        idPerfil        = getIntent().getIntExtra("idPerfil", 0);
        nombrePerfil    = getIntent().getStringExtra("nombre");
        fechaNacimiento = getIntent().getStringExtra("fechaNacimiento");
        sexo            = getIntent().getStringExtra("sexo");

        // =========================
        // CARD PERFIL
        // =========================
        txtNombre.setText(nombrePerfil != null ? nombrePerfil : "Sin nombre");
        txtEdad.setText(fechaNacimiento != null
                ? calcularEdadCompleta(fechaNacimiento)
                : "Edad no disponible");

        // =========================
        // HEADER MENÚ
        // =========================
        txtNombreMenu.setText(nombrePerfil != null ? nombrePerfil : "Sin nombre");
        txtEdadMenu.setText(fechaNacimiento != null
                ? calcularEdadCompleta(fechaNacimiento)
                : "Edad no disponible");

        // =========================
        // COLOR TOOLBAR
        // =========================
        int color = ContextCompat.getColor(this, R.color.skyblue);
        toolbar.setBackgroundColor(color);
        txtNombre.setTextColor(color);

        // =========================
        // MENU HAMBURGUESA
        // =========================
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // =========================
        // EVENTOS MENU
        // =========================
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_alergias) {
                Intent intent = new Intent(this, AlergiasMiembro.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_escanear) {
                Intent intent = new Intent(this, EscanearCarnet.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_carnets) {
                Intent intent = new Intent(this, CarnetEscaneados.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_centros) {
                Intent intent = new Intent(this, CentrosDeVacunacion.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_campanias) {
                Intent intent = new Intent(this, Campanias.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_carnet) {
                Intent intent = new Intent(this, carnet_de_vacunacion.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                startActivity(intent);

            } else if (id == R.id.nav_perfiles) {
                startActivity(new Intent(this, pantalla_dashboard.class));
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });


        // =========================
        // BASE DE DATOS Y BOTONES
        // =========================
        db = VacunAppDatabase.getInstance(this);

    }

    // =========================
    // MÉTODOS EDAD
    // =========================
    private String calcularEdadCompleta(String fecha) {
        try {
            SimpleDateFormat sdf;
            if (fecha.contains("/")) {
                sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }
            Date fechaNac = sdf.parse(fecha);
            Calendar nac = Calendar.getInstance();
            nac.setTime(fechaNac);
            Calendar hoy = Calendar.getInstance();
            int años  = hoy.get(Calendar.YEAR)  - nac.get(Calendar.YEAR);
            int meses = hoy.get(Calendar.MONTH) - nac.get(Calendar.MONTH);
            if (meses < 0) { años--; meses += 12; }
            return años + " años y " + meses + " meses";
        } catch (Exception e) {
            return "Edad no disponible";
        }
    }

    private int calcularEdadEnAnios(String fecha) {
        try {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date fechaNac = sdf.parse(fecha);
            Calendar nac = Calendar.getInstance();
            nac.setTime(fechaNac);
            Calendar hoy = Calendar.getInstance();
            return hoy.get(Calendar.YEAR) - nac.get(Calendar.YEAR);
        } catch (Exception e) { return 0; }
    }

}
// ========================= PUEDE CAMBIAR
