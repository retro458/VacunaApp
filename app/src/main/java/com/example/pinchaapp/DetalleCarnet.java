package com.example.pinchaapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.pinchaapp.database.CarnetViewModel;
import com.example.pinchaapp.database.entities.Carnet;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DetalleCarnet extends BasePerfilActivity {

    private CarnetViewModel viewModel;
    private int carnetId;

    @Override
    protected int getLayoutId()      { return R.layout.activity_detalle_carnet; }

    @Override
    protected int getNavItemActivo() { return 0; } // ningún ítem activo

    @Override
    protected void onPerfilReady() {

        viewModel = new ViewModelProvider(this).get(CarnetViewModel.class);
        carnetId  = getIntent().getIntExtra("carnet_id", -1);

        configurarToolbar();
        observarCarnet();
    }

    // ── Toolbar con flecha atrás ──────────────────────────────────
    private void configurarToolbar() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    // ── Observar el carnet desde Room ─────────────────────────────
    private void observarCarnet() {
        if (carnetId == -1) { finish(); return; }

        viewModel.getPorId(carnetId).observe(this, carnet -> {
            if (carnet == null) { finish(); return; }
            poblarVistas(carnet);
            configurarEliminar(carnet);
        });
    }

    // ── Llenar todas las vistas con los datos ─────────────────────
    private void poblarVistas(Carnet carnet) {

        // Fotos
        cargarFoto(carnet.getFotoFrontal(),
                findViewById(R.id.imgFrontal),
                findViewById(R.id.placeholderFrontal));

        cargarFoto(carnet.getFotoTrasera(),
                findViewById(R.id.imgTrasera),
                findViewById(R.id.placeholderTrasera));

        // Filas de información
        setFila(R.id.filaPropietario,
                R.drawable.outline_account_circle_24,
                "Propietario",
                carnet.getNombrePropietario());

        setFila(R.id.filaClinica,
                R.drawable.outline_add_home_work_24,
                "Clínica / Hospital",
                carnet.getClinica());

        setFila(R.id.filaFecha,
                R.drawable.outline_calendar_month_24,
                "Fecha de registro",
                carnet.getFechaRegistro());

        // Chip esquema
        ((Chip) findViewById(R.id.chipEsquema))
                .setText(carnet.getEsquema());
    }

    /** Carga una foto con Glide o muestra el placeholder si no hay */
    private void cargarFoto(String uriStr, ImageView img, View placeholder) {
        if (uriStr != null && !uriStr.isEmpty()) {
            placeholder.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(Uri.parse(uriStr))
                    .centerCrop()
                    .into(img);
        } else {
            img.setVisibility(View.GONE);
            placeholder.setVisibility(View.VISIBLE);
        }
    }

    /** Llena una fila reutilizable con ícono, label y valor */
    private void setFila(int filaId, int iconoRes, String label, String valor) {
        View fila = findViewById(filaId);
        ((ImageView) fila.findViewById(R.id.filaIcono)).setImageResource(iconoRes);
        ((TextView)  fila.findViewById(R.id.filaLabel)).setText(label);
        ((TextView)  fila.findViewById(R.id.filaValor)).setText(valor);
    }

    // ── Confirmar y eliminar ──────────────────────────────────────
    private void configurarEliminar(Carnet carnet) {
        findViewById(R.id.btnEliminar).setOnClickListener(v ->
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Eliminar carnet")
                        .setMessage("¿Estás seguro de que quieres eliminar este carnet? Esta acción no se puede deshacer.")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            viewModel.eliminar(carnet);
                            finish();
                        })
                        .show()
        );
    }
}