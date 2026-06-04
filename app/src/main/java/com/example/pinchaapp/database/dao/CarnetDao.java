package com.example.pinchaapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pinchaapp.database.entities.Carnet;

import java.util.List;

@Dao
public interface CarnetDao {

    @Insert
    void insertar(Carnet carnet);

    @Update
    void actualizar(Carnet carnet);

    @Delete
    void eliminar(Carnet carnet);

    @Query("SELECT * FROM carnets ORDER BY id DESC")
    LiveData<List<Carnet>> obtenerTodos();

    @Query("SELECT * FROM carnets WHERE id = :id")
    LiveData<Carnet> obtenerPorId(int id);

    @Query("SELECT * FROM carnets WHERE idPerfil = :idPerfil ORDER BY id DESC")
    LiveData<List<Carnet>> obtenerPorPerfil(int idPerfil);
}