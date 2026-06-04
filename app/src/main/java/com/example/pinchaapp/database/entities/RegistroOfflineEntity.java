package com.example.pinchaapp.database.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "registros_offline")
public class RegistroOfflineEntity {
    @PrimaryKey(autoGenerate = true)
    public int idLocal; // ID exclusivo de SQLite

    public int idMiembro;
    public int idVacuna;
    public Integer idCentro; // Puede ser null si no fue en un centro

    public String fechaAplicacion;
    public int dosisNumero;

    // Campos opcionales
    public String lote;
    public String nombreMedico;

    // Bandera para saber si ya se envió a la api
    public boolean sincronizado;
}
