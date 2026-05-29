package com.example.pinchaapp.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ImcDto {

    // ============================================================
    // REQUEST DTOs
    // ============================================================
    public static class RegistrarImcDto {
        private int idMiembro;
        private Double peso;
        private Double altura;
        private String fecha;  // ← String, no Date

        public RegistrarImcDto() {}

        public int getIdMiembro() { return idMiembro; }
        public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
        public Double getPeso() { return peso; }
        public void setPeso(Double peso) { this.peso = peso; }
        public Double getAltura() { return altura; }
        public void setAltura(Double altura) { this.altura = altura; }
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
    }

    public static class RegistrarCarnetDto {
        private int idMiembro;
        private String nombreClinica;
        private Date fecha;
        private String imagen; // URL del storage

        public RegistrarCarnetDto() {}

        // Getters y Setters
        public int getIdMiembro() { return idMiembro; }
        public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
        public String getNombreClinica() { return nombreClinica; }
        public void setNombreClinica(String nombreClinica) { this.nombreClinica = nombreClinica; }
        public Date getFecha() { return fecha; }
        public void setFecha(Date fecha) { this.fecha = fecha; }
        public String getImagen() { return imagen; }
        public void setImagen(String imagen) { this.imagen = imagen; }
    }

    // ============================================================
    // RESPONSE DTOs
    // ============================================================
    public static class ImcResponseDto {
        @SerializedName("idImc")
        private int id;

        @SerializedName("peso")
        private Double peso;

        @SerializedName("altura")
        private Double altura;

        @SerializedName("resultado")
        private Double resultado;

        @SerializedName("clasificacion")
        private String clasificacion;

        @SerializedName("fecha")
        private String fecha;

        public int getId() { return id; }
        public Double getPeso() { return peso; }
        public Double getAltura() { return altura; }
        public Double getResultado() { return resultado; }
        public String getClasificacion() { return clasificacion; }
        public String getFecha() { return fecha; }
    }

    public static class CarnetEscaneadoResponseDto {
        private int id;
        private int idMiembro;
        private String nombreClinica;
        private Date fecha;
        private String imagen;

        public CarnetEscaneadoResponseDto() {}

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getIdMiembro() { return idMiembro; }
        public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
        public String getNombreClinica() { return nombreClinica; }
        public void setNombreClinica(String nombreClinica) { this.nombreClinica = nombreClinica; }
        public Date getFecha() { return fecha; }
        public void setFecha(Date fecha) { this.fecha = fecha; }
        public String getImagen() { return imagen; }
        public void setImagen(String imagen) { this.imagen = imagen; }
    }
}