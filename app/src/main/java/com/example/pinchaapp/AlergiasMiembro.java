package com.example.pinchaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.adapters.VacunaAdapter;
import com.example.pinchaapp.adapters.AlergiaAdapter;
import com.example.pinchaapp.database.dao.AlergiaDao;
import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.entities.VacunaHistorial;
import com.example.pinchaapp.database.entities.Alergia;
import com.example.pinchaapp.dto.VacunaDto;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlergiasMiembro extends AppCompatActivity {
// ========================= PUEDE CAMBIAR
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    String nombrePerfil;
    String fechaNacimiento;
    String sexo;
    int idPerfil;
    String tipoPerfil;
    RecyclerView rvAlergias;
    MaterialButton btnAgregar;

    List<Alergia> lista = new ArrayList<>();

    AlergiaAdapter adapter;

    VacunAppDatabase db;

    TextView txtNombreMenu, txtEdadMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alergias_miembro);

        rvAlergias = findViewById(R.id.rvAlergias);
        btnAgregar = findViewById(R.id.btnAgregarAlergia);

        rvAlergias.setLayoutManager(
                new LinearLayoutManager(this)
        );

        db = VacunAppDatabase.getInstance(this);

        idPerfil = getIntent().getIntExtra("idPerfil", 0);

        cargarAlergias();

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
        tipoPerfil = getIntent().getStringExtra("tipoPerfil");
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

        // SI ES MASCOTA
        if (tipoPerfil != null && tipoPerfil.equals("Mascota")) {
            Menu menu = navigationView.getMenu();
            // ocultar opciones humanas
            menu.findItem(R.id.nav_centros).setVisible(false);
            menu.findItem(R.id.nav_campanias).setVisible(false);
        }


        // =========================
        // EVENTOS MENU
        // =========================
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_carnet) {
                Intent intent = new Intent(this, carnet_de_vacunacion.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                intent.putExtra("tipoPerfil",      tipoPerfil);
                startActivity(intent);

            } else if (id == R.id.nav_escanear) {
                Intent intent = new Intent(this, EscanearCarnet.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                intent.putExtra("tipoPerfil",      tipoPerfil);
                startActivity(intent);

            } else if (id == R.id.nav_carnets) {
                Intent intent = new Intent(this, CarnetEscaneados.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                intent.putExtra("tipoPerfil",      tipoPerfil);
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

            } else if (id == R.id.nav_pdf) {
                Intent intent = new Intent(this, ExportarPDF.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);
                intent.putExtra("tipoPerfil",      tipoPerfil);
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

        btnAgregar.setOnClickListener(v -> {

            EditText editText = new EditText(this);

            new AlertDialog.Builder(this)
                    .setTitle("Nueva alergia")
                    .setView(editText)

                    .setPositiveButton("Guardar", (dialog, which) -> {

                        String nombre =
                                editText.getText().toString().trim();

                        if (!nombre.isEmpty()) {

                            new Thread(() -> {

                                db.alergiaDao().insertar(
                                        new Alergia(idPerfil, nombre)
                                );

                                runOnUiThread(this::cargarAlergias);

                            }).start();
                        }
                    })

                    .setNegativeButton("Cancelar", null)
                    .show();
        });

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

    private void cargarAlergias() {

        new Thread(() -> {

            List<Alergia> nuevas =
                    db.alergiaDao()
                            .obtenerPorPerfil(idPerfil);

            runOnUiThread(() -> {

                lista.clear();
                lista.addAll(nuevas);

                if (adapter == null) {

                    adapter = new AlergiaAdapter(
                            this,
                            lista,
                            alergia -> {

                                new Thread(() -> {

                                    db.alergiaDao()
                                            .eliminar(alergia);

                                    runOnUiThread(this::cargarAlergias);

                                }).start();
                            }
                    );

                    rvAlergias.setAdapter(adapter);

                } else {

                    adapter.notifyDataSetChanged();
                }
            });

        }).start();
    }

}
// ========================= PUEDE CAMBIAR
