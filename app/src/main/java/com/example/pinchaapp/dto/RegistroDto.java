package com.example.pinchaapp.dto;

public class RegistroDto {

    private String nombre;
    private String correo;
    private String password;
    private String telefono;

    public RegistroDto(String nombre, String correo, String password, String telefono) {
        this.nombre   = nombre;
        this.correo   = correo;
        this.password = password;
        this.telefono = telefono;
    }

    public String getNombre()            { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo()            { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPassword()              { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono()              { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }


}
