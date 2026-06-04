package com.example.pinchaapp.database.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "campanias")
public class CampaniaEntity {
    @PrimaryKey
    public int id;

    public String nombre;
    public String lugar;
    public String fecha; // Guardar como formato ISO 8601 (Ej: "2026-05-29T10:00:00")
    public Double latitud;
    public Double longitud;
    public Integer idVacuna; // Integer (objeto) permite valores null
    public Integer idCentro;
    public boolean activo;
}