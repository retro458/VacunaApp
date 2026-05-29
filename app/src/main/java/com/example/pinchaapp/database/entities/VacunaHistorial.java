package com.example.pinchaapp.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "historial")
public class VacunaHistorial {

    @PrimaryKey(autoGenerate = true)
    private int idLocal;

    private int idPerfil;
    private int idVacuna;
    private int idCentro;

    private String nombreVacuna;
    private String nombreCentro;

    private String fechaAplicacion;

    private int dosisNumero;

    private String lote;

    private String nombreMedico;

    private String observaciones;
    public VacunaHistorial() {}

    public VacunaHistorial(
            int idPerfil,
            int idVacuna,
            int idCentro,
            String nombreVacuna,
            String nombreCentro,
            String fechaAplicacion,
            int dosisNumero,
            String lote,
            String nombreMedico,
            String observaciones
    ) {

        this.idPerfil = idPerfil;
        this.idVacuna = idVacuna;
        this.idCentro = idCentro;
        this.nombreVacuna = nombreVacuna;
        this.nombreCentro = nombreCentro;
        this.fechaAplicacion = fechaAplicacion;
        this.dosisNumero = dosisNumero;
        this.lote = lote;
        this.nombreMedico = nombreMedico;
        this.observaciones = observaciones;
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public int getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(int idVacuna) {
        this.idVacuna = idVacuna;
    }

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public String getNombreVacuna() {
        return nombreVacuna;
    }

    public void setNombreVacuna(String nombreVacuna) {
        this.nombreVacuna = nombreVacuna;
    }

    public String getNombreCentro() {
        return nombreCentro;
    }

    public void setNombreCentro(String nombreCentro) {
        this.nombreCentro = nombreCentro;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public int getDosisNumero() {
        return dosisNumero;
    }

    public void setDosisNumero(int dosisNumero) {
        this.dosisNumero = dosisNumero;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}