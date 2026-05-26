package com.example.pinchaapp.dto;

import java.util.List;

public class PerfilMiembroDto {

    private int idMiembro;
    private String nombre;
    private String tipo;
    private String fechaNacimiento;
    private String genero;
    private String especie;
    private String raza;
    private double peso;
    private double altura;
    private List<AlergiasDto> alergias;
    private List<HistorialDto> historial;

    public int getIdMiembro() { return idMiembro; }
    public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }

    public String getNombre() { return nombre; }
    public void   setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void   setTipo(String tipo) { this.tipo = tipo; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void   setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public List<AlergiasDto>  getAlergias() { return alergias; }
    public void setAlergias(List<AlergiasDto> alergias){ this.alergias = alergias; }

    public List<HistorialDto> getHistorial() { return historial; }
    public void setHistorial(List<HistorialDto> historial){ this.historial = historial; }
}