package com.example.pinchaapp.dto;

import com.google.gson.annotations.SerializedName;

public class AlergiasDto {

    // Para GET /api/alergias (catálogo) — devuelve "id"
    public static class AlergiaDto {
        @SerializedName("id")
        private int id;

        @SerializedName("nombre")
        private String nombre;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    // Para GET /api/alergias/miembro/{id} — devuelve "idAlergia"
    public static class AlergiaMiembroDto {
        @SerializedName("idMiembroAlergia")
        private int idMiembroAlergia;

        @SerializedName("idAlergia")
        private int idAlergia;

        @SerializedName("nombre")
        private String nombre;

        public int getIdAlergia() { return idAlergia; }
        public String getNombre() { return nombre; }
    }

    public static class CrearAlergiaDto {
        private String nombre;
        public CrearAlergiaDto(String nombre) { this.nombre = nombre; }
        public String getNombre() { return nombre; }
    }
}