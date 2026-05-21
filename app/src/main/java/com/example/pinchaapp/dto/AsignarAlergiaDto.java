package com.example.pinchaapp.dto;

public class AsignarAlergiaDto {

    private int idAlergia;

    public AsignarAlergiaDto(int idAlergia) {
        this.idAlergia = idAlergia;
    }

    public int getIdAlergia() { return idAlergia; }
    public void setIdAlergia(int idAlergia) { this.idAlergia = idAlergia; }
}