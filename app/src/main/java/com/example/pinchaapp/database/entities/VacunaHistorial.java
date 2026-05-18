package com.example.pinchaapp.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "historial_vacuna")
public class VacunaHistorial {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id_perfil")
    private int idPerfil;

    @ColumnInfo(name = "nombre_vacuna")
    private String nombreVacuna;

    @ColumnInfo(name = "dosis_numero")
    private int dosisNumero;

    @ColumnInfo(name = "total_dosis")
    private int totalDosis;

    @ColumnInfo(name = "fecha_aplicacion")
    private String fechaAplicacion; // null = pendiente

    @ColumnInfo(name = "proxima_dosis")
    private String proximaDosis;

    @ColumnInfo(name = "observaciones")
    private String observaciones;

    // Constructor vacío para Room
    public VacunaHistorial() {}

    // Constructor que usamos nosotros
    @Ignore
    public VacunaHistorial(int idPerfil, String nombreVacuna,
                           int dosisNumero, int totalDosis,
                           String proximaDosis, String observaciones) {
        this.idPerfil = idPerfil;
        this.nombreVacuna = nombreVacuna;
        this.dosisNumero = dosisNumero;
        this.totalDosis = totalDosis;
        this.fechaAplicacion = null; // pendiente por defecto
        this.proximaDosis = proximaDosis;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }
    public String getNombreVacuna() { return nombreVacuna; }
    public void setNombreVacuna(String n) { this.nombreVacuna = n; }
    public int getDosisNumero() { return dosisNumero; }
    public void setDosisNumero(int d) { this.dosisNumero = d; }
    public int getTotalDosis() { return totalDosis; }
    public void setTotalDosis(int t) { this.totalDosis = t; }
    public String getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(String f) { this.fechaAplicacion = f; }
    public String getProximaDosis() { return proximaDosis; }
    public void setProximaDosis(String p) { this.proximaDosis = p; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String o) { this.observaciones = o; }
}