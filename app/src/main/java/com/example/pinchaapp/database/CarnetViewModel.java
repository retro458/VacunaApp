package com.example.pinchaapp.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pinchaapp.database.entities.Carnet;

import java.util.List;

public class CarnetViewModel extends AndroidViewModel {

    private final CarnetRepository repo;
    private final LiveData<List<Carnet>> todos;

    public CarnetViewModel(Application app) {
        super(app);
        repo = new CarnetRepository(app);
        todos = repo.obtenerTodos();
    }

    public LiveData<List<Carnet>> getTodos() { return todos; }
    public void insertar(Carnet c) { repo.insertar(c); }
    public void eliminar(Carnet c) { repo.eliminar(c); }
    public LiveData<Carnet> getPorId(int id) { return repo.obtenerPorId(id); }
    public LiveData<List<Carnet>> getTodosPorPerfil(int idPerfil) {
        return repo.obtenerPorPerfil(idPerfil);
    }
}