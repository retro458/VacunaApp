package com.example.pinchaapp.dto;

public class CertificadoDto {

    private int idCertificado;
    private int idMiembro;
    private String nombreMiembro;
    private String codigoQr;
    private String urlQr;
    private String fechaGeneracion;
    private String fechaExpiracion;

    public CertificadoDto(int idMiembro) {
        this.idMiembro = idMiembro;
    }

    public int getIdCertificado() { return idCertificado; }
    public void setIdCertificado(int idCertificado) { this.idCertificado = idCertificado; }

    public int getIdMiembro() { return idMiembro; }
    public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }

    public String getNombreMiembro() { return nombreMiembro; }
    public void setNombreMiembro(String nombreMiembro) { this.nombreMiembro = nombreMiembro; }

    public String getCodigoQr() { return codigoQr; }
    public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }

    public String getUrlQr() { return urlQr; }
    public void setUrlQr(String urlQr) { this.urlQr = urlQr; }

    public String getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(String fechaGeneracion){ this.fechaGeneracion = fechaGeneracion; }

    public String getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(String fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
}