package com.example.pinchaapp;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.adapters.CarnetAdapter;
import com.example.pinchaapp.database.CarnetViewModel;
import com.example.pinchaapp.database.entities.Carnet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.pinchaapp.DetalleCarnet;


public class CarnetEscaneados extends BasePerfilActivity {

    // ── Room ─────────────────────────────────────────────────────
    private CarnetViewModel viewModel;

    // ── Vistas ───────────────────────────────────────────────────
    private RecyclerView recyclerCarnets;
    private LinearLayout layoutVacio;
    private CarnetAdapter adapter;

    // ── Obligatorios de la base ──────────────────────────────────
    @Override
    protected int getLayoutId()      { return R.layout.activity_carnet_escaneados; }

    @Override
    protected int getNavItemActivo() { return R.id.nav_carnets; }

    // ════════════════════════════════════════════════════════════
    @Override
    protected void onPerfilReady() {

        viewModel = new ViewModelProvider(this).get(CarnetViewModel.class);

        inicializarVistas();
        configurarRecycler();
        observarCarnets();
        configurarFab();
    }

    // ─────────────────────────────────────────────────────────────
    // VISTAS
    // ─────────────────────────────────────────────────────────────
    private void inicializarVistas() {
        recyclerCarnets = findViewById(R.id.recyclerCarnets);
        layoutVacio     = findViewById(R.id.layoutVacio);
    }

    // ─────────────────────────────────────────────────────────────
    // RECYCLER
    // ─────────────────────────────────────────────────────────────
    private void configurarRecycler() {
        adapter = new CarnetAdapter(carnet -> {
            // Al tocar un carnet → ir al detalle
            Intent intent = crearIntent(DetalleCarnet.class);
            intent.putExtra("carnet_id", carnet.getId());
            startActivity(intent);
        });

        recyclerCarnets.setLayoutManager(new LinearLayoutManager(this));
        recyclerCarnets.setAdapter(adapter);

        // Animación suave al actualizar items
        recyclerCarnets.setItemAnimator(new DefaultItemAnimator());
    }

    // ─────────────────────────────────────────────────────────────
    // OBSERVAR ROOM — se actualiza automáticamente
    // ─────────────────────────────────────────────────────────────
    private void observarCarnets() {
        viewModel.getTodosPorPerfil(idPerfil).observe(this, lista -> {
            boolean vacio = lista == null || lista.isEmpty();

            recyclerCarnets.setVisibility(vacio ? View.GONE  : View.VISIBLE);
            layoutVacio.setVisibility    (vacio ? View.VISIBLE : View.GONE);

            if (!vacio) adapter.setLista(lista);
        });
    }

    // ─────────────────────────────────────────────────────────────
    // FAB → ir a registrar nuevo carnet
    // ─────────────────────────────────────────────────────────────
    private void configurarFab() {
        findViewById(R.id.fabAgregar).setOnClickListener(v ->
                startActivity(crearIntent(EscanearCarnet.class)));
    }
}