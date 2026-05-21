package com.example.pinchaapp.dto;

public class ActualizarEstadoDto {

    // Valores válidos: "pendiente" | "completado" | "pospuesto"
    private String estado;

    public ActualizarEstadoDto(String estado) {
        this.estado = estado;
    }

    public String getEstado()                { return estado; }
    public void   setEstado(String estado)   { this.estado = estado; }
}