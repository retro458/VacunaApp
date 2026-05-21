package com.example.pinchaapp.dto;

public class AlergiaDto {

    private int idAlergia;
    private String nombre;
    private String descripcion;

    public int getIdAlergia() { return idAlergia; }
    public void setIdAlergia(int idAlergia) { this.idAlergia = idAlergia; }

    public String getNombre() { return nombre; }
    public void   setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void   setDescripcion(String descripcion) { this.descripcion = descripcion; }
}