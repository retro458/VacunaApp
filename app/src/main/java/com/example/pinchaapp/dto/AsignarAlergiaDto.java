package com.example.pinchaapp.dto;

import com.google.gson.annotations.SerializedName;

public class AsignarAlergiaDto {

    @SerializedName("idAlergia")
    private int idAlergia;

    public AsignarAlergiaDto(int idAlergia) {
        this.idAlergia = idAlergia;
    }

    public int getIdAlergia() { return idAlergia; }
    public void setIdAlergia(int idAlergia) { this.idAlergia = idAlergia; }
}