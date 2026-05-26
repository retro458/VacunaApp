package com.example.pinchaapp.dto;

import java.util.Date;

public class HistorialDto {

    // ============================================================
    // REQUEST DTOs
    // ============================================================
    public static class RegistrarVacunacionDto {
        private int idMiembro;
        private int idVacuna;
        private Integer idCentro;
        private Date fechaAplicacion;
        private int dosisNumero;
        private String lote;
        private String nombreMedico;
        private String observaciones;

        public RegistrarVacunacionDto() {}

        // Getters y Setters
        public int getIdMiembro() { return idMiembro; }
        public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
        public int getIdVacuna() { return idVacuna; }
        public void setIdVacuna(int idVacuna) { this.idVacuna = idVacuna; }
        public Integer getIdCentro() { return idCentro; }
        public void setIdCentro(Integer idCentro) { this.idCentro = idCentro; }
        public Date getFechaAplicacion() { return fechaAplicacion; }
        public void setFechaAplicacion(Date fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
        public int getDosisNumero() { return dosisNumero; }
        public void setDosisNumero(int dosisNumero) { this.dosisNumero = dosisNumero; }
        public String getLote() { return lote; }
        public void setLote(String lote) { this.lote = lote; }
        public String getNombreMedico() { return nombreMedico; }
        public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    }

    // ============================================================
    // RESPONSE DTOs
    // ============================================================
    public static class HistorialResponseDto {
        private int id;
        private Date fechaAplicacion;
        private Date proximaDosis;
        private int dosisNumero;
        private String lote;
        private String nombreMedico;
        private String observaciones;
        private String vacuna;
        private String fabricante;
        private String tipoVacuna;
        private String centro;
        private String centroDireccion;
        private String recordatorioEstado;
        private Date fechaRecordatorio;

        public HistorialResponseDto() {}

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public Date getFechaAplicacion() { return fechaAplicacion; }
        public void setFechaAplicacion(Date fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
        public Date getProximaDosis() { return proximaDosis; }
        public void setProximaDosis(Date proximaDosis) { this.proximaDosis = proximaDosis; }
        public int getDosisNumero() { return dosisNumero; }
        public void setDosisNumero(int dosisNumero) { this.dosisNumero = dosisNumero; }
        public String getLote() { return lote; }
        public void setLote(String lote) { this.lote = lote; }
        public String getNombreMedico() { return nombreMedico; }
        public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
        public String getVacuna() { return vacuna; }
        public void setVacuna(String vacuna) { this.vacuna = vacuna; }
        public String getFabricante() { return fabricante; }
        public void setFabricante(String fabricante) { this.fabricante = fabricante; }
        public String getTipoVacuna() { return tipoVacuna; }
        public void setTipoVacuna(String tipoVacuna) { this.tipoVacuna = tipoVacuna; }
        public String getCentro() { return centro; }
        public void setCentro(String centro) { this.centro = centro; }
        public String getCentroDireccion() { return centroDireccion; }
        public void setCentroDireccion(String centroDireccion) { this.centroDireccion = centroDireccion; }
        public String getRecordatorioEstado() { return recordatorioEstado; }
        public void setRecordatorioEstado(String recordatorioEstado) { this.recordatorioEstado = recordatorioEstado; }
        public Date getFechaRecordatorio() { return fechaRecordatorio; }
        public void setFechaRecordatorio(Date fechaRecordatorio) { this.fechaRecordatorio = fechaRecordatorio; }
    }

    public static class ProximaDosisResponseDto {
        private String miembro;
        private String tipoMiembro;
        private String vacuna;
        private int dosisAplicada;
        private Date proximaDosis;
        private Integer diasRestantes;
        private String recordatorio;

        public ProximaDosisResponseDto() {}

        // Getters y Setters
        public String getMiembro() { return miembro; }
        public void setMiembro(String miembro) { this.miembro = miembro; }
        public String getTipoMiembro() { return tipoMiembro; }
        public void setTipoMiembro(String tipoMiembro) { this.tipoMiembro = tipoMiembro; }
        public String getVacuna() { return vacuna; }
        public void setVacuna(String vacuna) { this.vacuna = vacuna; }
        public int getDosisAplicada() { return dosisAplicada; }
        public void setDosisAplicada(int dosisAplicada) { this.dosisAplicada = dosisAplicada; }
        public Date getProximaDosis() { return proximaDosis; }
        public void setProximaDosis(Date proximaDosis) { this.proximaDosis = proximaDosis; }
        public Integer getDiasRestantes() { return diasRestantes; }
        public void setDiasRestantes(Integer diasRestantes) { this.diasRestantes = diasRestantes; }
        public String getRecordatorio() { return recordatorio; }
        public void setRecordatorio(String recordatorio) { this.recordatorio = recordatorio; }
    }
}