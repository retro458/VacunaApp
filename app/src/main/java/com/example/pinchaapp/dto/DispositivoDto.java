package com.example.pinchaapp.dto;

public class DispositivoDto {

    private int idDispositivo;
    private String tokenPush;
    private String plataforma; // android, ios
    private boolean activo;

    public DispositivoDto(String tokenPush, String plataforma) {
        this.tokenPush  = tokenPush;
        this.plataforma = plataforma;
    }

    public int getIdDispositivo()  { return idDispositivo; }
    public void setIdDispositivo(int idDispositivo) { this.idDispositivo = idDispositivo; }

    public String getTokenPush() { return tokenPush; }
    public void setTokenPush(String tokenPush) { this.tokenPush = tokenPush; }

    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}