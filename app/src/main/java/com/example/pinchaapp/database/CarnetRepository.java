package com.example.pinchaapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.pinchaapp.database.dao.CarnetDao;
import com.example.pinchaapp.database.entities.Carnet;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarnetRepository {

    private final CarnetDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CarnetRepository(Application app) {
        VacunAppDatabase db = VacunAppDatabase.getInstance(app);
        dao = db.carnetDao();
    }

    public void insertar(Carnet c) { executor.execute(() -> dao.insertar(c)); }
    public void actualizar(Carnet c) { executor.execute(() -> dao.actualizar(c)); }
    public void eliminar(Carnet c) { executor.execute(() -> dao.eliminar(c)); }
    public LiveData<List<Carnet>> obtenerTodos() { return dao.obtenerTodos(); }
    public LiveData<Carnet> obtenerPorId(int id) { return dao.obtenerPorId(id); }
    public LiveData<List<Carnet>> obtenerPorPerfil(int idPerfil) {
        return dao.obtenerPorPerfil(idPerfil);
    }
}