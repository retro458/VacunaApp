package com.example.pinchaapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pinchaapp.database.entities.PerfilMascota;

import java.util.List;

@Dao
public interface PerfilMascotaDao {

    @Insert
    void insertarPerfil(PerfilMascota perfil);

    @Update
    void actualizarPerfil(PerfilMascota perfil);

    @Delete
    void eliminarPerfil(PerfilMascota perfil);

    @Query("SELECT * FROM PerfilMascota")
    List<PerfilMascota> obtenerPerfiles();

    @Query("SELECT * FROM PerfilMascota WHERE id = :id")
    PerfilMascota obtenerPerfilPorId(int id);

}
