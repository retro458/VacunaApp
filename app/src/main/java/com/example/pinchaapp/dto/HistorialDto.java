package com.example.pinchaapp.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class HistorialDto {

    // ── REQUEST (lo que ya tenés) ────────────────────────────────────
    @SerializedName("idMiembro")
    private int idMiembro;

    @SerializedName("idVacuna")
    private int idVacuna;

    @SerializedName("idCentro")
    private int idCentro;

    @SerializedName("fechaAplicacion")
    private String fechaAplicacion;

    @SerializedName("dosisNumero")
    private int dosisNumero;

    @SerializedName("lote")
    private String lote;

    @SerializedName("nombreMedico")
    private String nombreMedico;

    @SerializedName("observaciones")
    private String observaciones;

    public HistorialDto(int idMiembro, int idVacuna, int idCentro,
                        String fechaAplicacion, int dosisNumero,
                        String lote, String nombreMedico, String observaciones) {
        this.idMiembro       = idMiembro;
        this.idVacuna        = idVacuna;
        this.idCentro        = idCentro;
        this.fechaAplicacion = fechaAplicacion;
        this.dosisNumero     = dosisNumero;
        this.lote            = lote;
        this.nombreMedico    = nombreMedico;
        this.observaciones   = observaciones;
    }

    // Getters
    public int getIdMiembro() { return idMiembro; }
    public int getIdVacuna() { return idVacuna; }
    public int getIdCentro() { return idCentro; }
    public String getFechaAplicacion() { return fechaAplicacion; }
    public int getDosisNumero() { return dosisNumero; }
    public String getLote() { return lote; }
    public String getNombreMedico() { return nombreMedico; }
    public String getObservaciones() { return observaciones; }

    // ── RESPONSE (lo que le faltaba) ─────────────────────────────────
    public static class HistorialResponseDto {
        @SerializedName("id")
        private int id;

        @SerializedName("fechaAplicacion")
        private String fechaAplicacion;

        @SerializedName("proximaDosis")
        private String proximaDosis;

        @SerializedName("dosisNumero")
        private int dosisNumero;

        @SerializedName("lote")
        private String lote;

        @SerializedName("nombreMedico")
        private String nombreMedico;

        @SerializedName("observaciones")
        private String observaciones;

        @SerializedName("vacuna")
        private String vacuna;

        @SerializedName("fabricante")
        private String fabricante;

        @SerializedName("tipoVacuna")
        private String tipoVacuna;

        @SerializedName("centro")
        private String centro;

        @SerializedName("centroDireccion")
        private String centroDireccion;

        @SerializedName("recordatorioEstado")
        private String recordatorioEstado;

        @SerializedName("fechaRecordatorio")
        private String fechaRecordatorio;

        public HistorialResponseDto() {}

        public int getId() { return id; }
        public String getFechaAplicacion() { return fechaAplicacion; }
        public String getProximaDosis() { return proximaDosis; }
        public int getDosisNumero() { return dosisNumero; }
        public String getLote() { return lote; }
        public String getNombreMedico() { return nombreMedico; }
        public String getObservaciones() { return observaciones; }
        public String getVacuna() { return vacuna; }
        public String getFabricante() { return fabricante; }
        public String getTipoVacuna() { return tipoVacuna; }
        public String getCentro() { return centro; }
        public String getCentroDireccion() { return centroDireccion; }
        public String getRecordatorioEstado() { return recordatorioEstado; }
        public String getFechaRecordatorio() { return fechaRecordatorio; }
    }

    // ── PRÓXIMAS DOSIS ────────────────────────────────────────────────
    public static class ProximaDosisResponseDto {
        @SerializedName("miembro")
        private String miembro;

        @SerializedName("tipoMiembro")
        private String tipoMiembro;

        @SerializedName("vacuna")
        private String vacuna;

        @SerializedName("dosisAplicada")
        private int dosisAplicada;

        @SerializedName("proximaDosis")
        private String proximaDosis;

        @SerializedName("diasRestantes")
        private Integer diasRestantes;

        @SerializedName("recordatorio")
        private String recordatorio;

        public ProximaDosisResponseDto() {}

        public String getMiembro() { return miembro; }
        public String getTipoMiembro() { return tipoMiembro; }
        public String getVacuna() { return vacuna; }
        public int getDosisAplicada() { return dosisAplicada; }
        public String getProximaDosis() { return proximaDosis; }
        public Integer getDiasRestantes() { return diasRestantes; }
        public String getRecordatorio() { return recordatorio; }
    }
}