package com.example.pinchaapp;

import com.example.pinchaapp.database.VacunAppDatabase;

public class ExportarPDF extends BasePerfilActivity {

    VacunAppDatabase db;
    // ── Obligatorios de la base ──────────────────────────────────────
    @Override
    protected int getLayoutId()      { return R.layout.activity_exportar_pdf; }

    @Override
    protected int getNavItemActivo() { return R.id.nav_pdf; }

    @Override
    protected void onPerfilReady() {

        // =========================
        // BASE DE DATOS Y BOTONES
        // =========================
        db = VacunAppDatabase.getInstance(this);

    }
}
