package com.example.pinchaapp.dto;

public class GoogleAuthDto {

    private String idToken;

    public GoogleAuthDto(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken()             { return idToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }
}