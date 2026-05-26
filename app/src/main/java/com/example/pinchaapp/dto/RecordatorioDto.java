package com.example.pinchaapp.dto;

import java.util.Date;

public class RecordatorioDto {

    // ============================================================
    // REQUEST DTOs
    // ============================================================
    public static class ActualizarRecordatorioDto {
        private String estado; // pendiente | completado | pospuesto
        public ActualizarRecordatorioDto() {}
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }

    public static class CrearRecordatorioCampaniaDto {
        private int idCampania;
        private Date fechaRecordatorio;
        private String mensaje;

        public CrearRecordatorioCampaniaDto() {}

        // Getters y Setters
        public int getIdCampania() { return idCampania; }
        public void setIdCampania(int idCampania) { this.idCampania = idCampania; }
        public Date getFechaRecordatorio() { return fechaRecordatorio; }
        public void setFechaRecordatorio(Date fechaRecordatorio) { this.fechaRecordatorio = fechaRecordatorio; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }

    // ============================================================
    // RESPONSE DTOs
    // ============================================================
    public static class RecordatorioResponseDto {
        private int id;
        private int idHistorial;
        private Date fechaRecordatorio;
        private String tipo;
        private String mensaje;
        private String estado;
        private String vacuna;
        private String miembro;
        private Integer diasRestantes;

        public RecordatorioResponseDto() {}

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getIdHistorial() { return idHistorial; }
        public void setIdHistorial(int idHistorial) { this.idHistorial = idHistorial; }
        public Date getFechaRecordatorio() { return fechaRecordatorio; }
        public void setFechaRecordatorio(Date fechaRecordatorio) { this.fechaRecordatorio = fechaRecordatorio; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        public String getVacuna() { return vacuna; }
        public void setVacuna(String vacuna) { this.vacuna = vacuna; }
        public String getMiembro() { return miembro; }
        public void setMiembro(String miembro) { this.miembro = miembro; }
        public Integer getDiasRestantes() { return diasRestantes; }
        public void setDiasRestantes(Integer diasRestantes) { this.diasRestantes = diasRestantes; }
    }

    public static class RecordatorioCampaniaResponseDto {
        private int id;
        private String campania;
        private String vacuna;
        private String lugar;
        private Date fechaCampania;
        private Date fechaRecordatorio;
        private Integer diasRestantes;

        public RecordatorioCampaniaResponseDto() {}

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getCampania() { return campania; }
        public void setCampania(String campania) { this.campania = campania; }
        public String getVacuna() { return vacuna; }
        public void setVacuna(String vacuna) { this.vacuna = vacuna; }
        public String getLugar() { return lugar; }
        public void setLugar(String lugar) { this.lugar = lugar; }
        public Date getFechaCampania() { return fechaCampania; }
        public void setFechaCampania(Date fechaCampania) { this.fechaCampania = fechaCampania; }
        public Date getFechaRecordatorio() { return fechaRecordatorio; }
        public void setFechaRecordatorio(Date fechaRecordatorio) { this.fechaRecordatorio = fechaRecordatorio; }
        public Integer getDiasRestantes() { return diasRestantes; }
        public void setDiasRestantes(Integer diasRestantes) { this.diasRestantes = diasRestantes; }
    }
}