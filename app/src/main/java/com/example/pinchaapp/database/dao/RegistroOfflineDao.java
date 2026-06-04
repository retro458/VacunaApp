package com.example.pinchaapp.database.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pinchaapp.database.entities.RegistroOfflineEntity;

import java.util.List;

@Dao
public interface RegistroOfflineDao {

    // Guarda la vacuna cuando el usuario le da a "Guardar" sin internet
    @Insert
    long insertarRegistro(RegistroOfflineEntity registro);

    // Obtiene todos los registros que la app aún no ha podido enviar a C#
    @Query("SELECT * FROM registros_offline WHERE sincronizado = 0")
    List<RegistroOfflineEntity> obtenerRegistrosPendientes();

    // Actualiza el registro a sincronizado = true cuando C# responde 200 OK
    @Update
    void actualizarRegistro(RegistroOfflineEntity registro);

    // Limpiar la tabla de los que ya se subieron para no llenar el teléfono
    @Query("DELETE FROM registros_offline WHERE sincronizado = 1")
    void limpiarRegistrosSincronizados();
}