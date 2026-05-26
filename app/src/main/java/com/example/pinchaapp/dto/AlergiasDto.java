package com.example.pinchaapp.dto;

public class AlergiasDto {

    // ============================================================
    // REQUEST DTOs
    // ============================================================

    public static class CrearAlergiaDto {
        private String nombre;

        public CrearAlergiaDto() {}

        public CrearAlergiaDto(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    public static class AsignarAlergiaDto {
        private int idAlergia;

        public AsignarAlergiaDto() {}

        public AsignarAlergiaDto(int idAlergia) {
            this.idAlergia = idAlergia;
        }

        public int getIdAlergia() { return idAlergia; }
        public void setIdAlergia(int idAlergia) { this.idAlergia = idAlergia; }
    }

    // ============================================================
    // RESPONSE DTOs
    // ============================================================

    public static class AlergiaDto {
        private int id;
        private String nombre;

        public AlergiaDto() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    public static class MiembroAlergiaDto {
        private int idMiembroAlergia;
        private int idAlergia;
        private String nombre;

        public MiembroAlergiaDto() {}

        public int getIdMiembroAlergia() { return idMiembroAlergia; }
        public void setIdMiembroAlergia(int idMiembroAlergia) { this.idMiembroAlergia = idMiembroAlergia; }

        public int getIdAlergia() { return idAlergia; }
        public void setIdAlergia(int idAlergia) { this.idAlergia = idAlergia; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
}