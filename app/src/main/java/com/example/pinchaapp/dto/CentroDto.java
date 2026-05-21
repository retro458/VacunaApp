package com.example.pinchaapp.dto;

import java.util.List;

public class CentroDto {

    private int idCentro;
    private String nombre;
    private String direccion;
    private String telefono;
    private double latitud;
    private double longitud;
    private String horario;
    private double distanciaKm; // solo en busqueda por cercania
    private List<VacunaDto> vacunasDisponibles; // solo en /detalle

    public int getIdCentro() { return idCentro; }
    public void setIdCentro(int idCentro)  { this.idCentro = idCentro; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion()  { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono)  { this.telefono = telefono; }

    public double getLatitud()  { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm)  { this.distanciaKm = distanciaKm; }

    public List<VacunaDto> getVacunasDisponibles()  { return vacunasDisponibles; }
    public void setVacunasDisponibles(List<VacunaDto> vacunasDisponibles){ this.vacunasDisponibles = vacunasDisponibles; }
}