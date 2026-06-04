package com.example.pinchaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public abstract class BasePerfilActivity extends AppCompatActivity {

    // ── Datos del perfil ──────────────────────────────────────────
    protected int    idPerfil;
    protected String nombrePerfil, fechaNacimiento, sexo, tipoPerfil;

    // ── Vistas comunes ────────────────────────────────────────────
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected MaterialToolbar toolbar;
    protected TextView txtNombreMenu, txtEdadMenu;

    // ─────────────────────────────────────────────────────────────
    // Cada subclase dice qué layout usar
    // ─────────────────────────────────────────────────────────────
    protected abstract int getLayoutId();

    // ─────────────────────────────────────────────────────────────
    // Qué ítem del menú está ACTIVO en esta pantalla
    // (para ocultarlo)
    // ─────────────────────────────────────────────────────────────
    protected abstract int getNavItemActivo();

    // ─────────────────────────────────────────────────────────────
    // Hook opcional: código extra que cada Activity ejecuta
    // después de que la base termine su setup
    // ─────────────────────────────────────────────────────────────
    protected void onPerfilReady() { }

    // ═════════════════════════════════════════════════════════════
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(getLayoutId());

        recibirDatos();
        inicializarVistasComunes();
        configurarCardPerfil();
        configurarMenuHeader();
        configurarToolbar();
        configurarDrawerToggle();
        configurarMenuSegunTipoPerfil();
        configurarEventosMenu();

        onPerfilReady();
    }

    // ─────────────────────────────────────────────────────────────
    // RECIBIR DATOS DEL INTENT
    // ─────────────────────────────────────────────────────────────
    private void recibirDatos() {
        idPerfil        = getIntent().getIntExtra("idPerfil", 0);
        nombrePerfil    = getIntent().getStringExtra("nombre");
        fechaNacimiento = getIntent().getStringExtra("fechaNacimiento");
        sexo            = getIntent().getStringExtra("sexo");
        tipoPerfil      = getIntent().getStringExtra("tipoPerfil");
    }

    // ─────────────────────────────────────────────────────────────
    // VISTAS COMUNES (drawer, toolbar, navigationView)
    // ─────────────────────────────────────────────────────────────
    private void inicializarVistasComunes() {
        drawerLayout   = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar        = findViewById(R.id.toolbar);

        View header   = navigationView.getHeaderView(0);
        txtNombreMenu = header.findViewById(R.id.txtNombreMenu);
        txtEdadMenu   = header.findViewById(R.id.txtEdadMenu);
    }

    // ─────────────────────────────────────────────────────────────
    // CARD PERFIL  (txtNombre / txtEdad en el layout)
    // ─────────────────────────────────────────────────────────────
    private void configurarCardPerfil() {
        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtEdad   = findViewById(R.id.txtEdad);
        if (txtNombre == null || txtEdad == null) return;

        txtNombre.setText(nombrePerfil != null ? nombrePerfil : "Sin nombre");
        txtEdad.setText(fechaNacimiento != null
                ? calcularEdadCompleta(fechaNacimiento)
                : "Edad no disponible");

        // Color toolbar + texto nombre
        int color = ContextCompat.getColor(this, R.color.skyblue);
        toolbar.setBackgroundColor(color);
        txtNombre.setTextColor(color);
    }

    // ─────────────────────────────────────────────────────────────
    // HEADER DEL MENÚ LATERAL
    // ─────────────────────────────────────────────────────────────
    private void configurarMenuHeader() {
        txtNombreMenu.setText(nombrePerfil != null ? nombrePerfil : "Sin nombre");
        txtEdadMenu.setText(fechaNacimiento != null
                ? calcularEdadCompleta(fechaNacimiento)
                : "Edad no disponible");
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
    }

    // ─────────────────────────────────────────────────────────────
    // TOGGLE HAMBURGUESA
    // ─────────────────────────────────────────────────────────────
    private void configurarDrawerToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // ─────────────────────────────────────────────────────────────
    // FILTRAR MENÚ SEGÚN TIPO DE PERFIL Y PANTALLA ACTIVA
    // ─────────────────────────────────────────────────────────────
    private void configurarMenuSegunTipoPerfil() {
        Menu menu = navigationView.getMenu();

        // Ocultar el ítem de la pantalla actual (ya estás aquí)
        int itemActivo = getNavItemActivo();
        if (itemActivo != 0) {
            MenuItem item = menu.findItem(itemActivo);
            if (item != null) item.setVisible(false);
        }

        // Si es mascota → ocultar opciones exclusivas de humanos
        if ("mascota".equals(tipoPerfil)) {
            ocultarItem(menu, R.id.nav_centros);
            ocultarItem(menu, R.id.nav_campanias);

            // Ocultar elementos de IMC si existen en este layout
            setViewVisibility(R.id.btnIMC,          View.GONE);
            setViewVisibility(R.id.txtIMC,          View.GONE);
            setViewVisibility(R.id.layoutEsquemaIMC, View.GONE);
        }
    }

    private void ocultarItem(Menu menu, int itemId) {
        MenuItem item = menu.findItem(itemId);
        if (item != null) item.setVisible(false);
    }

    /** Aplica visibilidad solo si la vista existe en el layout actual */
    protected void setViewVisibility(int viewId, int visibility) {
        View v = findViewById(viewId);
        if (v != null) v.setVisibility(visibility);
    }

    // ─────────────────────────────────────────────────────────────
    // EVENTOS DEL MENÚ LATERAL
    // ─────────────────────────────────────────────────────────────
    private void configurarEventosMenu() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_alergias) {
                intent = crearIntent(AlergiasMiembro.class);

            } else if (id == R.id.nav_escanear) {
                intent = crearIntent(EscanearCarnet.class);

            } else if (id == R.id.nav_carnets) {
                intent = crearIntent(CarnetEscaneados.class);

            } else if (id == R.id.nav_centros) {
                // centros no necesita tipoPerfil
                intent = new Intent(this, CentrosDeVacunacion.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);

            } else if (id == R.id.nav_campanias) {
                intent = new Intent(this, Campanias.class);
                intent.putExtra("idPerfil",        idPerfil);
                intent.putExtra("nombre",          nombrePerfil);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("sexo",            sexo);

            } else if (id == R.id.nav_proximas_dosis) {
                intent = new Intent(this, ProximasDosisActivity.class);
                intent.putExtra("idPerfil", idPerfil);
                intent.putExtra("nombre",   nombrePerfil);

            } else if (id == R.id.nav_pdf) {
                intent = crearIntent(ExportarPDF.class);

            } else if (id == R.id.nav_carnet) {
                intent = crearIntent(carnet_de_vacunacion.class);

            } else if (id == R.id.nav_perfiles) {
                startActivity(new Intent(this, pantalla_dashboard.class));
                finish();
                drawerLayout.closeDrawers();
                return true;
            }

            if (intent != null) startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        });
    }

    // ─────────────────────────────────────────────────────────────
    // HELPER: Intent con todos los extras del perfil
    // ─────────────────────────────────────────────────────────────
    protected Intent crearIntent(Class<?> destino) {
        Intent intent = new Intent(this, destino);
        intent.putExtra("idPerfil",        idPerfil);
        intent.putExtra("nombre",          nombrePerfil);
        intent.putExtra("fechaNacimiento", fechaNacimiento);
        intent.putExtra("sexo",            sexo);
        intent.putExtra("tipoPerfil",      tipoPerfil);
        return intent;
    }

    // ═════════════════════════════════════════════════════════════
    // UTILIDADES DE FECHA / EDAD  (un solo lugar para todos)
    // ═════════════════════════════════════════════════════════════
    protected String calcularEdadCompleta(String fecha) {
        try {
            SimpleDateFormat sdf = fecha.contains("/")
                    ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    : new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Calendar nac = Calendar.getInstance();
            nac.setTime(sdf.parse(fecha));
            Calendar hoy = Calendar.getInstance();

            int años  = hoy.get(Calendar.YEAR)  - nac.get(Calendar.YEAR);
            int meses = hoy.get(Calendar.MONTH) - nac.get(Calendar.MONTH);
            if (meses < 0) { años--; meses += 12; }
            return años + " años y " + meses + " meses";
        } catch (Exception e) {
            return "Edad no disponible";
        }
    }

    protected int calcularEdadEnAnios(String fecha) {
        try {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar nac = Calendar.getInstance();
            nac.setTime(sdf.parse(fecha));
            Calendar hoy = Calendar.getInstance();
            return hoy.get(Calendar.YEAR) - nac.get(Calendar.YEAR);
        } catch (Exception e) { return 0; }
    }
}
