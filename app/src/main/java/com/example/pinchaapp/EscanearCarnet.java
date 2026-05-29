package com.example.pinchaapp;


import com.example.pinchaapp.database.VacunAppDatabase;


public class EscanearCarnet extends BasePerfilActivity {

    VacunAppDatabase db;
    // ── Obligatorios de la base ──────────────────────────────────────
    @Override
    protected int getLayoutId()      { return R.layout.activity_escanear_carnet; }

    @Override
    protected int getNavItemActivo() { return R.id.nav_escanear; }

    @Override
    protected void onPerfilReady() {

        // =========================
        // BASE DE DATOS Y BOTONES
        // =========================
        db = VacunAppDatabase.getInstance(this);

    }
}
