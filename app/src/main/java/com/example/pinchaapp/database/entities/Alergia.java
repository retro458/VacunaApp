package com.example.pinchaapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alergia")
public class Alergia {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idPerfil;
    private int idAlergiaApi;
    private String nombre;

    public Alergia(int idPerfil, int idAlergiaApi, String nombre) {
        this.idPerfil = idPerfil;
        this.idAlergiaApi = idAlergiaApi;
        this.nombre = nombre;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }

    public int getIdAlergiaApi() { return idAlergiaApi; }
    public void setIdAlergiaApi(int idAlergiaApi) { this.idAlergiaApi = idAlergiaApi; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}