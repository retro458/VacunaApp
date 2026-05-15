package com.example.pinchaapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "perfilMascota")
public class PerfilMascota {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public PerfilMascota(String nombre, String especie, String fechaNacimiento) {
        this.nombre = nombre;
        this.especie = especie;
        this.fechaNacimiento = fechaNacimiento;
    }

    @ColumnInfo(name = "nombre")
    private String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @ColumnInfo(name = "especie")
    private String especie;

    @ColumnInfo(name = "fechaNacimiento")
    private String fechaNacimiento;

}
