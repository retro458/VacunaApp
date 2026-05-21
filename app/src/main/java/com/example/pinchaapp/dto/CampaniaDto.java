package com.example.pinchaapp.dto;

public class CampaniaDto {

    private int    idCampania;
    private String nombre;
    private String descripcion;
    private String fechaInicio;   // "yyyy-MM-dd"
    private String fechaFin;      // "yyyy-MM-dd"
    private String estado;
    private int    idCentro;
    private String nombreCentro;
    private String vacuna;

    public int    getIdCampania()                   { return idCampania; }
    public void   setIdCampania(int idCampania)     { this.idCampania = idCampania; }

    public String getNombre()                       { return nombre; }
    public void   setNombre(String nombre)          { this.nombre = nombre; }

    public String getDescripcion()                      { return descripcion; }
    public void   setDescripcion(String descripcion)    { this.descripcion = descripcion; }

    public String getFechaInicio()                      { return fechaInicio; }
    public void   setFechaInicio(String fechaInicio)    { this.fechaInicio = fechaInicio; }

    public String getFechaFin()                   { return fechaFin; }
    public void   setFechaFin(String fechaFin)    { this.fechaFin = fechaFin; }

    public String getEstado()                   { return estado; }
    public void   setEstado(String estado)      { this.estado = estado; }

    public int    getIdCentro()                 { return idCentro; }
    public void   setIdCentro(int idCentro)     { this.idCentro = idCentro; }

    public String getNombreCentro()                       { return nombreCentro; }
    public void   setNombreCentro(String nombreCentro)    { this.nombreCentro = nombreCentro; }

    public String getVacuna()                   { return vacuna; }
    public void   setVacuna(String vacuna)      { this.vacuna = vacuna; }
}