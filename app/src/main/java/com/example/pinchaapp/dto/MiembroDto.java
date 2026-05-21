package com.example.pinchaapp.dto;

public class MiembroDto {

    private int    idMiembro;
    private String nombre;
    private String tipo;           // humano o mascota
    private String fechaNacimiento;
    private String genero;
    private String especie;        // solo mascotas
    private String raza;           // solo mascotas

    public int    getIdMiembro()                { return idMiembro; }
    public void   setIdMiembro(int idMiembro)   { this.idMiembro = idMiembro; }

    public String getNombre()                   { return nombre; }
    public void   setNombre(String nombre)      { this.nombre = nombre; }

    public String getTipo()                     { return tipo; }
    public void   setTipo(String tipo)          { this.tipo = tipo; }

    public String getFechaNacimiento()                        { return fechaNacimiento; }
    public void   setFechaNacimiento(String fechaNacimiento)  { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero()                   { return genero; }
    public void   setGenero(String genero)      { this.genero = genero; }

    public String getEspecie()                  { return especie; }
    public void   setEspecie(String especie)    { this.especie = especie; }

    public String getRaza()                     { return raza; }
    public void   setRaza(String raza)          { this.raza = raza; }
}