package com.example.pinchaapp.dto;

import java.util.ArrayList;
import java.util.List;

public class VacunaDto {

    // ============================================================
    // REQUEST DTOs
    // ============================================================

    public static class CrearVacunaDto {
        private String nombre;
        private String tipo; // humano | animal | ambos
        private String fabricante;
        private String descripcion;
        private String imagenUrl;

        // Constructor vacío requerido por serializadores
        public CrearVacunaDto() {}

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getFabricante() { return fabricante; }
        public void setFabricante(String fabricante) { this.fabricante = fabricante; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public String getImagenUrl() { return imagenUrl; }
        public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    }

    public static class ActualizarVacunaDto {
        private String nombre;
        private String tipo;
        private String fabricante;
        private String descripcion;
        private String imagenUrl;

        public ActualizarVacunaDto() {}

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getFabricante() { return fabricante; }
        public void setFabricante(String fabricante) { this.fabricante = fabricante; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public String getImagenUrl() { return imagenUrl; }
        public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    }

    public static class CrearEsquemaDto {
        private int idVacuna;
        private int numeroDosis;
        private Integer intervaloDias;   // Integer acepta null (int? en C#)
        private Integer edadMinimaDias;  // Integer acepta null (int? en C#)
        private String descripcion;

        public CrearEsquemaDto() {}

        public int getIdVacuna() { return idVacuna; }
        public void setIdVacuna(int idVacuna) { this.idVacuna = idVacuna; }
        public int getNumeroDosis() { return numeroDosis; }
        public void setNumeroDosis(int numeroDosis) { this.numeroDosis = numeroDosis; }
        public Integer getIntervaloDias() { return intervaloDias; }
        public void setIntervaloDias(Integer intervaloDias) { this.intervaloDias = intervaloDias; }
        public Integer getEdadMinimaDias() { return edadMinimaDias; }
        public void setEdadMinimaDias(Integer edadMinimaDias) { this.edadMinimaDias = edadMinimaDias; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }

    // ============================================================
    // RESPONSE DTOs
    // ============================================================

    public static class VacunaResponseDto {
        private int id;
        private String nombre;
        private String tipo;
        private String fabricante;
        private String descripcion;
        private String imagenUrl;
        private boolean activo;

        public VacunaResponseDto() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getFabricante() { return fabricante; }
        public void setFabricante(String fabricante) { this.fabricante = fabricante; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public String getImagenUrl() { return imagenUrl; }
        public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
        public boolean isActivo() { return activo; }
        public void setActivo(boolean activo) { this.activo = activo; }

        // Sobrescribir toString para que el AutoCompleteTextView (Material Spinner) muestre directo el nombre
        @Override
        public String toString() {
            return nombre;
        }
    }

    public static class EsquemaVacunaDto {
        private int id;
        private int idVacuna;
        private int numeroDosis;
        private Integer intervaloDias;   // Nullable mapeado a la perfección
        private Integer edadMinimaDias;  // Nullable mapeado a la perfección
        private String descripcion;

        public EsquemaVacunaDto() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getIdVacuna() { return idVacuna; }
        public void setIdVacuna(int idVacuna) { this.idVacuna = idVacuna; }
        public int getNumeroDosis() { return numeroDosis; }
        public void setNumeroDosis(int numeroDosis) { this.numeroDosis = numeroDosis; }
        public Integer getIntervaloDias() { return intervaloDias; }
        public void setIntervaloDias(Integer intervaloDias) { this.intervaloDias = intervaloDias; }
        public Integer getEdadMinimaDias() { return edadMinimaDias; }
        public void setEdadMinimaDias(Integer edadMinimaDias) { this.edadMinimaDias = edadMinimaDias; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }

    public static class VacunaConEsquemaDto {
        private VacunaResponseDto vacuna = new VacunaResponseDto();
        private List<EsquemaVacunaDto> esquemas = new ArrayList<>();

        public VacunaConEsquemaDto() {}

        public VacunaResponseDto getVacuna() { return vacuna; }
        public void setVacuna(VacunaResponseDto vacuna) { this.vacuna = vacuna; }
        public List<EsquemaVacunaDto> getEsquemas() { return esquemas; }
        public void setEsquemas(List<EsquemaVacunaDto> esquemas) { this.esquemas = esquemas; }
    }
}