package com.example.pinchaapp.database.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "centros_vacunacion")
public class CentroEntity {
    @PrimaryKey
    public int id;

    public String nombre;
    public String direccion;
    public Double latitud;
    public Double longitud;
    public String tipo;
    public String horario;
    public String telefono;
    public boolean activo;
}