package com.example.pinchaapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.pinchaapp.database.entities.Alergia;
import java.util.List;

@Dao
public interface AlergiaDao {

    @Insert
    void insertar(Alergia alergia);

    @Delete
    void eliminar(Alergia alergia);

    @Query("SELECT * FROM alergia WHERE idPerfil = :idPerfil")
    List<Alergia> obtenerPorPerfil(int idPerfil);

    @Query("DELETE FROM alergia WHERE idPerfil = :idPerfil AND idAlergiaApi = :idAlergiaApi")
    void eliminarPorApiId(int idPerfil, int idAlergiaApi);

    @Query("DELETE FROM alergia WHERE idPerfil = :idPerfil")
    void eliminarTodosDePerfil(int idPerfil);
}