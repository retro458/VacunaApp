package com.example.pinchaapp;

import com.example.pinchaapp.database.VacunAppDatabase;


public class CarnetEscaneados extends BasePerfilActivity {

    VacunAppDatabase db;

    @Override
    protected int getLayoutId()      { return R.layout.activity_carnet_escaneados; }

    @Override
    protected int getNavItemActivo() { return R.id.nav_carnets; }

    @Override
    protected void onPerfilReady() {

        // =========================
        // BASE DE DATOS Y BOTONES
        // =========================
        db = VacunAppDatabase.getInstance(this);

    }
}
