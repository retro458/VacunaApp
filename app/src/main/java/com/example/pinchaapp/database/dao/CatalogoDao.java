package com.example.pinchaapp.database.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pinchaapp.database.entities.CampaniaEntity;
import com.example.pinchaapp.database.entities.CentroEntity;
import com.example.pinchaapp.database.entities.VacunaEntity;

import java.util.List;

@Dao
public interface CatalogoDao {

    // --- CENTROS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarCentros(List<CentroEntity> centros);

    @Query("SELECT * FROM centros_vacunacion WHERE activo = 1")
    List<CentroEntity> obtenerTodosLosCentros();

    // --- VACUNAS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarVacunas(List<VacunaEntity> vacunas);

    @Query("SELECT * FROM vacunas WHERE tipo = :tipoMiembro AND activo = 1")
    List<VacunaEntity> obtenerVacunasPorTipo(String tipoMiembro);

    // --- CAMPAÑAS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarCampanias(List<CampaniaEntity> campanias);

    @Query("SELECT * FROM campanias WHERE activo = 1")
    List<CampaniaEntity> obtenerCampaniasActivas();
}
