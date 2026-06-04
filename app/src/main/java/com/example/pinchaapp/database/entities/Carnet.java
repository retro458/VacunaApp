package com.example.pinchaapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "carnets")
public class Carnet {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    private int idPerfil;
    private String nombrePropietario;   // "Daniela Corea"
    private String esquema;             // "Esquema Mujeres"
    private String clinica;             // nombre de la clínica
    private String fechaRegistro;       // "2024-03-15"
    private String fotoFrontal;         // ruta URI de la imagen
    private String fotoTrasera;         // ruta URI de la imagen

    // Constructor
    public Carnet(int idPerfil, String nombrePropietario, String esquema,
                  String clinica, String fechaRegistro,
                  String fotoFrontal, String fotoTrasera) {
        this.idPerfil = idPerfil;
        this.nombrePropietario = nombrePropietario;
        this.esquema = esquema;
        this.clinica = clinica;
        this.fechaRegistro = fechaRegistro;
        this.fotoFrontal = fotoFrontal;
        this.fotoTrasera = fotoTrasera;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombrePropietario() { return nombrePropietario; }
    public void setNombrePropietario(String v) { this.nombrePropietario = v; }
    public String getEsquema() { return esquema; }
    public void setEsquema(String v) { this.esquema = v; }
    public String getClinica() { return clinica; }
    public void setClinica(String v) { this.clinica = v; }
    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String v) { this.fechaRegistro = v; }
    public String getFotoFrontal() { return fotoFrontal; }
    public void setFotoFrontal(String v) { this.fotoFrontal = v; }
    public String getFotoTrasera() { return fotoTrasera; }
    public void setFotoTrasera(String v) { this.fotoTrasera = v; }
}