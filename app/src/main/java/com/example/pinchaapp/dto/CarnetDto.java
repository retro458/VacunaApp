package com.example.pinchaapp.dto;

public class CarnetDto {

    private int idCarnet;
    private int idMiembro;
    private String urlImagen;
    private String descripcion;
    private String fechaRegistro;

    public CarnetDto(int idMiembro, String urlImagen, String descripcion, String fechaRegistro) {
        this.idMiembro    = idMiembro;
        this.urlImagen    = urlImagen;
        this.descripcion  = descripcion;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdCarnet() { return idCarnet; }
    public void setIdCarnet(int idCarnet) { this.idCarnet = idCarnet; }

    public int getIdMiembro() { return idMiembro; }
    public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}