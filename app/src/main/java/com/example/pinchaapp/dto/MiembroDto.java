package com.example.pinchaapp.dto;

import java.util.Date;

public class MiembroDto {

    // ============================================================
    // REQUEST DTOs
    // ============================================================
    public static class CrearMiembroDto {
        private String nombre;
        private String tipo; // persona | mascota
        private String fechaNacimiento;
        private String genero;
        private String especie; // solo mascotas
        private String numeroDocumento;
        private String fotoUrl;

        public CrearMiembroDto() {}

        // Getters y Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
        public String getGenero() { return genero; }
        public void setGenero(String genero) { this.genero = genero; }
        public String getEspecie() { return especie; }
        public void setEspecie(String especie) { this.especie = especie; }
        public String getNumeroDocumento() { return numeroDocumento; }
        public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
        public String getFotoUrl() { return fotoUrl; }
        public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    }

    public static class ActualizarMiembroDto {
        private String nombre;
        private String fechaNacimiento;
        private String genero;
        private String especie;
        private String numeroDocumento;
        private String fotoUrl;

        public ActualizarMiembroDto() {}

        // Getters y Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
        public String getGenero() { return genero; }
        public void setGenero(String genero) { this.genero = genero; }
        public String getEspecie() { return especie; }
        public void setEspecie(String especie) { this.especie = especie; }
        public String getNumeroDocumento() { return numeroDocumento; }
        public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
        public String getFotoUrl() { return fotoUrl; }
        public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    }

    // ============================================================
    // RESPONSE DTOs
    // ============================================================
    public static class MiembroResponseDto {
        private int id;
        private String nombre;
        private String tipo;
        private String fechaNacimiento;
        private String genero;
        private String especie;
        private String numeroDocumento;
        private String fotoUrl;
        private Integer edad;

        public MiembroResponseDto() {}

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
        public String getGenero() { return genero; }
        public void setGenero(String genero) { this.genero = genero; }
        public String getEspecie() { return especie; }
        public void setEspecie(String especie) { this.especie = especie; }
        public String getNumeroDocumento() { return numeroDocumento; }
        public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
        public String getFotoUrl() { return fotoUrl; }
        public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
        public Integer getEdad() { return edad; }
        public void setEdad(Integer edad) { this.edad = edad; }
    }

    public static class CrearAlergiaDto {
        private String nombre;
        public CrearAlergiaDto() {}
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    public static class AsignarAlergiaDto {
        private int idAlergia;
        public AsignarAlergiaDto() {}
        public int getIdAlergia() { return idAlergia; }
        public void setIdAlergia(int idAlergia) { this.idAlergia = idAlergia; }
    }

    public static class AlergiaResponseDto {
        private int id;
        private String nombre;
        public AlergiaResponseDto() {}
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    public static class MiembroAlergiaResponseDto {
        private int idMiembroAlergia;
        private int idAlergia;
        private String nombre;

        public MiembroAlergiaResponseDto() {}

        // Getters y Setters
        public int getIdMiembroAlergia() { return idMiembroAlergia; }
        public void setIdMiembroAlergia(int idMiembroAlergia) { this.idMiembroAlergia = idMiembroAlergia; }
        public int getIdAlergia() { return idAlergia; }
        public void setIdAlergia(int idAlergia) { this.idAlergia = idAlergia; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
}