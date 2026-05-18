package com.example.pinchaapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.pinchaapp.database.entities.IMCEntity;
import java.util.List;


@Dao
public interface IMCDao {

    @Insert
    void insertar(IMCEntity imc);

    @Query("SELECT * FROM imc WHERE idPerfil = :idPerfil ORDER BY id DESC")
    List<IMCEntity> obtenerPorPerfil(int idPerfil);

}
