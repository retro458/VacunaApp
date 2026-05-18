package com.example.pinchaapp.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacuna")
public class Vacuna {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "tipo")
    private String tipo;

    @ColumnInfo(name = "activo")
    private boolean activo;

    // Constructor vacío para Room
    public Vacuna() {}

    // Constructor que usamos nosotros
    @Ignore
    public Vacuna(String nombre, String tipo, boolean activo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}