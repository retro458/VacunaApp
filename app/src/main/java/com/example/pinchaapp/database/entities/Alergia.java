package com.example.pinchaapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alergia")
public class Alergia {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idPerfil;

    private String nombre;

    public Alergia(int idPerfil, String nombre) {
        this.idPerfil = idPerfil;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
