package com.example.pinchaapp.dto;

public class VacunaDto {

    private int id;
    private String nombreVacuna;
    private int dosisNumero;
    private int totalDosis;
    private String fechaAplicacion;
    private String proximaDosis;
    private String observaciones;
    private boolean aplicada;

    public VacunaDto(int id, String nombreVacuna, int dosisNumero, int totalDosis,
                     String fechaAplicacion, String proximaDosis,
                     String observaciones, boolean aplicada) {
        this.id = id;
        this.nombreVacuna = nombreVacuna;
        this.dosisNumero = dosisNumero;
        this.totalDosis = totalDosis;
        this.fechaAplicacion = fechaAplicacion;
        this.proximaDosis = proximaDosis;
        this.observaciones = observaciones;
        this.aplicada = aplicada;
    }

    public int getId() { return id; }
    public String getNombreVacuna() { return nombreVacuna; }
    public int getDosisNumero() { return dosisNumero; }
    public int getTotalDosis() { return totalDosis; }
    public String getFechaAplicacion() { return fechaAplicacion; }
    public String getProximaDosis() { return proximaDosis; }
    public String getObservaciones() { return observaciones; }
    public boolean isAplicada() { return aplicada; }
    public void setAplicada(boolean aplicada) { this.aplicada = aplicada; }
    public void setFechaAplicacion(String f) { this.fechaAplicacion = f; }
}