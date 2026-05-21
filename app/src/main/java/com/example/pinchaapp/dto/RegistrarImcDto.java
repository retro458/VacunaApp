package com.example.pinchaapp.dto;

public class RegistrarImcDto {

    private int idMiembro;
    private double peso; // en kg
    private double altura; // en metros
    private String fecha; // "yyyy-MM-dd"

    public RegistrarImcDto(int idMiembro, double peso, double altura, String fecha) {
        this.idMiembro = idMiembro;
        this.peso = peso;
        this.altura = altura;
        this.fecha = fecha;
    }

    public int getIdMiembro() { return idMiembro; }
    public void   setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}