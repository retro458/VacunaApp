package com.example.pinchaapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pinchaapp.database.entities.PerfilHumano;

import java.util.List;

@Dao
public interface PerfilHumanoDao {

    @Insert
    void insertarPerfil(PerfilHumano perfil);

    @Update
    void actualizarPerfil(PerfilHumano perfil);

    @Delete
    void eliminarPerfil(PerfilHumano perfil);

    @Query("SELECT * FROM PerfilHumano")
    List<PerfilHumano> obtenerPerfiles();

    @Query("SELECT * FROM PerfilHumano WHERE id = :id")
    PerfilHumano obtenerPerfilPorId(int id);


}
