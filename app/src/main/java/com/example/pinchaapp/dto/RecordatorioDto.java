package com.example.pinchaapp.dto;

public class RecordatorioDto {

    private int    idRecordatorio;
    private int    idMiembro;
    private String nombreMiembro;
    private int    idVacuna;
    private String nombreVacuna;
    private int    dosisNumero;
    private String fechaProgramada; // "yyyy-MM-dd"
    private String estado;          // "pendiente" | "completado" | "pospuesto"
    private String descripcion;

    public int    getIdRecordatorio()                       { return idRecordatorio; }
    public void   setIdRecordatorio(int idRecordatorio)     { this.idRecordatorio = idRecordatorio; }

    public int    getIdMiembro()                            { return idMiembro; }
    public void   setIdMiembro(int idMiembro)               { this.idMiembro = idMiembro; }

    public String getNombreMiembro()                        { return nombreMiembro; }
    public void   setNombreMiembro(String nombreMiembro)    { this.nombreMiembro = nombreMiembro; }

    public int    getIdVacuna()                             { return idVacuna; }
    public void   setIdVacuna(int idVacuna)                 { this.idVacuna = idVacuna; }

    public String getNombreVacuna()                         { return nombreVacuna; }
    public void   setNombreVacuna(String nombreVacuna)      { this.nombreVacuna = nombreVacuna; }

    public int    getDosisNumero()                          { return dosisNumero; }
    public void   setDosisNumero(int dosisNumero)           { this.dosisNumero = dosisNumero; }

    public String getFechaProgramada()                          { return fechaProgramada; }
    public void   setFechaProgramada(String fechaProgramada)    { this.fechaProgramada = fechaProgramada; }

    public String getEstado()                   { return estado; }
    public void   setEstado(String estado)      { this.estado = estado; }

    public String getDescripcion()                      { return descripcion; }
    public void   setDescripcion(String descripcion)    { this.descripcion = descripcion; }
}