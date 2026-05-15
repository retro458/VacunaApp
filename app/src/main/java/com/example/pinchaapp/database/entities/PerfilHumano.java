package com.example.pinchaapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "perfilHumano")
public class PerfilHumano {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public PerfilHumano(String nombre, String sexo, String fechaNacimiento, boolean embarazada) {
        this.nombre = nombre;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.embarazada = embarazada;
    }
    public PerfilHumano() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "sexo")
    private String sexo;

    @ColumnInfo(name = "fechaNacimiento")
    private String fechaNacimiento;

    public boolean isEmbarazada() {
        return embarazada;
    }

    public void setEmbarazada(boolean embarazada) {
        this.embarazada = embarazada;
    }

    @ColumnInfo(name = "embarazada")
    private boolean embarazada;
}
