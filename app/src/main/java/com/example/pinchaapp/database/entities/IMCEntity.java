package com.example.pinchaapp.database.entities;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "imc")
public class IMCEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idPerfil;

    private double peso;

    private double altura;

    private double imc;

    private String categoria;

    private String fecha;

    public IMCEntity() {}

    @Ignore
    public IMCEntity(
            int idPerfil,
            double peso,
            double altura,
            double imc,
            String categoria,
            String fecha
    ) {
        this.idPerfil = idPerfil;
        this.peso = peso;
        this.altura = altura;
        this.imc = imc;
        this.categoria = categoria;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}