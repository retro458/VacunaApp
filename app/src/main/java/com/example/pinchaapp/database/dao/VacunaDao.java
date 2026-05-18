package com.example.pinchaapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.pinchaapp.database.entities.Vacuna;
import com.example.pinchaapp.database.entities.VacunaHistorial;

import java.util.List;

@Dao
public interface VacunaDao {

    // Insertar nueva vacuna agendada
    @Insert
    void insertarHistorial(VacunaHistorial historial);

    // Traer todas las vacunas de un perfil
    @Query("SELECT * FROM historial_vacuna WHERE id_perfil = :idPerfil ORDER BY id DESC")
    List<VacunaHistorial> obtenerTodasDeUnPerfil(int idPerfil);

    // Marcar como aplicada (poner fecha)
    @Query("UPDATE historial_vacuna SET fecha_aplicacion = :fecha WHERE id = :id")
    void marcarAplicada(int id, String fecha);

    // Contar completadas (para gráfica)
    @Query("SELECT COUNT(*) FROM historial_vacuna WHERE id_perfil = :idPerfil AND fecha_aplicacion IS NOT NULL")
    int contarCompletadas(int idPerfil);

    // Contar pendientes (para gráfica)
    @Query("SELECT COUNT(*) FROM historial_vacuna WHERE id_perfil = :idPerfil AND fecha_aplicacion IS NULL")
    int contarPendientes(int idPerfil);

    // Eliminar una vacuna
    @Delete
    void eliminar(VacunaHistorial historial);
}