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
}
