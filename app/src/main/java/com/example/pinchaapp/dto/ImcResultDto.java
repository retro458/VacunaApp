package com.example.pinchaapp.dto;

public class ImcResultDto {

    private int    idImc;
    private int    idMiembro;
    private double peso;
    private double altura;
    private double resultado;
    private String clasificacion;
    private String fecha;

    public int getIdImc() { return idImc; }
    public void setIdImc(int idImc) { this.idImc = idImc; }

    public int getIdMiembro() { return idMiembro; }
    public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }

    public double getPeso() { return peso; }
    public void setPeso(double peso){ this.peso = peso; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public double getResultado() { return resultado; }
    public void setResultado(double resultado) { this.resultado = resultado; }

    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }

    public String getFecha() { return fecha; }
    public void   setFecha(String fecha) { this.fecha = fecha; }
}