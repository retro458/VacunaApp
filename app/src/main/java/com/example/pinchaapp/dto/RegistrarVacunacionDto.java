package com.example.pinchaapp.dto;

public class RegistrarVacunacionDto {

    private int    idMiembro;
    private int    idVacuna;
    private int    idCentro;
    private String fechaAplicacion; // "yyyy-MM-dd"
    private int    dosisNumero;
    private String lote;
    private String nombreMedico;
    private String observaciones;

    public RegistrarVacunacionDto(int idMiembro, int idVacuna, int idCentro,
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

    public int    getIdMiembro()                    { return idMiembro; }
    public void   setIdMiembro(int idMiembro)       { this.idMiembro = idMiembro; }

    public int    getIdVacuna()                     { return idVacuna; }
    public void   setIdVacuna(int idVacuna)         { this.idVacuna = idVacuna; }

    public int    getIdCentro()                     { return idCentro; }
    public void   setIdCentro(int idCentro)         { this.idCentro = idCentro; }

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
}