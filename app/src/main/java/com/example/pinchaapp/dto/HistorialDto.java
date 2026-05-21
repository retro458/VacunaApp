package com.example.pinchaapp.dto;

public class HistorialDto {

    private int    idHistorial;
    private int    idMiembro;
    private String nombreMiembro;
    private int    idVacuna;
    private String nombreVacuna;
    private int    idCentro;
    private String nombreCentro;
    private String fechaAplicacion;
    private int    dosisNumero;
    private String lote;
    private String nombreMedico;
    private String observaciones;
    private String proximaDosis;

    public int    getIdHistorial()                  { return idHistorial; }
    public void   setIdHistorial(int idHistorial)   { this.idHistorial = idHistorial; }

    public int    getIdMiembro()                    { return idMiembro; }
    public void   setIdMiembro(int idMiembro)       { this.idMiembro = idMiembro; }

    public String getNombreMiembro()                        { return nombreMiembro; }
    public void   setNombreMiembro(String nombreMiembro)    { this.nombreMiembro = nombreMiembro; }

    public int    getIdVacuna()                     { return idVacuna; }
    public void   setIdVacuna(int idVacuna)         { this.idVacuna = idVacuna; }

    public String getNombreVacuna()                       { return nombreVacuna; }
    public void   setNombreVacuna(String nombreVacuna)    { this.nombreVacuna = nombreVacuna; }

    public int    getIdCentro()                     { return idCentro; }
    public void   setIdCentro(int idCentro)         { this.idCentro = idCentro; }

    public String getNombreCentro()                       { return nombreCentro; }
    public void   setNombreCentro(String nombreCentro)    { this.nombreCentro = nombreCentro; }

    public String getFechaAplicacion()                          { return fechaAplicacion; }
    public void   setFechaAplicacion(String fechaAplicacion)    { this.fechaAplicacion = fechaAplicacion; }

    public int    getDosisNumero()                  { return dosisNumero; }
    public void   setDosisNumero(int dosisNumero)   { this.dosisNumero = dosisNumero; }

    public String getLote()                   { return lote; }
    public void   setLote(String lote)        { this.lote = lote; }

    public String getNombreMedico()                       { return nombreMedico; }
    public void   setNombreMedico(String nombreMedico)    { this.nombreMedico = nombreMedico; }

    public String getObservaciones()                        { return observaciones; }
    public void   setObservaciones(String observaciones)    { this.observaciones = observaciones; }

    public String getProximaDosis()                       { return proximaDosis; }
    public void   setProximaDosis(String proximaDosis)    { this.proximaDosis = proximaDosis; }
}