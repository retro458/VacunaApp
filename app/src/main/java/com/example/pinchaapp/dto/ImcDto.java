package com.example.pinchaapp.dto;

import java.util.Date;

public class ImcDto {

    // ============================================================
    // REQUEST DTOs
    // ============================================================
    public static class RegistrarImcDto {
        private int idMiembro;
        private Double peso;
        private Double altura;
        private Date fecha;

        public RegistrarImcDto() {}

        // Getters y Setters
        public int getIdMiembro() { return idMiembro; }
        public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
        public Double getPeso() { return peso; }
        public void setPeso(Double peso) { this.peso = peso; }
        public Double getAltura() { return altura; }
        public void setAltura(Double altura) { this.altura = altura; }
        public Date getFecha() { return fecha; }
        public void setFecha(Date fecha) { this.fecha = fecha; }
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
        private int id;
        private int idMiembro;
        private Double peso;
        private Double altura;
        private Double resultado;
        private String clasificacion;
        private Date fecha;

        public ImcResponseDto() {}

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getIdMiembro() { return idMiembro; }
        public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
        public Double getPeso() { return peso; }
        public void setPeso(Double peso) { this.peso = peso; }
        public Double getAltura() { return altura; }
        public void setAltura(Double altura) { this.altura = altura; }
        public Double getResultado() { return resultado; }
        public void setResultado(Double resultado) { this.resultado = resultado; }
        public String getClasificacion() { return clasificacion; }
        public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }
        public Date getFecha() { return fecha; }
        public void setFecha(Date fecha) { this.fecha = fecha; }
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