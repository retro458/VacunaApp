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

    @Insert
    void insertarHistorial(VacunaHistorial historial);

    @Query("SELECT * FROM historial WHERE idPerfil = :idPerfil")
    List<VacunaHistorial> obtenerPorPerfil(int idPerfil);

    // ← el que faltaba (mismo que obtenerPorPerfil)
    @Query("SELECT * FROM historial WHERE idPerfil = :idPerfil")
    List<VacunaHistorial> obtenerTodasDeUnPerfil(int idPerfil);

    @Query("DELETE FROM historial WHERE idPerfil = :idPerfil")
    void eliminarTodosDePerfil(int idPerfil);

    @Query("SELECT COUNT(*) FROM historial WHERE idPerfil = :idPerfil")
    int contarCompletadas(int idPerfil);

    @Query("SELECT COUNT(*) FROM historial WHERE idPerfil = :idPerfil AND CAST(dosisNumero AS INTEGER) < CAST(lote AS INTEGER)")
    int contarPendientes(int idPerfil);

    // ← para marcar aplicada desde el adapter
    @Query("UPDATE historial SET fechaAplicacion = :fecha WHERE idLocal = :idHistorial")
    void marcarAplicada(int idHistorial, String fecha);
}