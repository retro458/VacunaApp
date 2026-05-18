package com.example.pinchaapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.pinchaapp.database.entities.Usuario;

@Dao
public interface UsuarioDao {

    // Insertar un usuario nuevo
    @Insert
    void insertar(Usuario usuario);

    // Buscar por el email (para login)
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario obtenerPorEmail(String email);

    // Buscar por email y password (login directo)
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    Usuario login(String email, String password);

    // Actualiza datos del usuario
    @Update
    void actualizar(Usuario usuario);

    // Elimina usuario
    @Delete
    void eliminar(Usuario usuario);

    // Checa si ya existe un email registrado
    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email")
    int existeEmail(String email);

}
