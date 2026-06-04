package com.example.pinchaapp.database.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacunas")
public class VacunaEntity {
    @PrimaryKey
    public int id;

    public String nombre;
    public String tipo;
    public String fabricante;
    public String descripcion;
    public boolean activo;
}