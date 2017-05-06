package com.example.asier.vibbay03.Beans;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Articulo {

    private String titulo;
    private int estado;
    private String imagen;
    private double precio;

    /////////////////
    private String userId;
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    ///////////////////////////
    public Articulo() {
    }

    public Articulo(String titulo, int estado, String imagen, double precio) {
        this.titulo = titulo;
        this.estado = estado;
        this.imagen = imagen;
        this.precio = precio;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int isEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("titulo", titulo);
        result.put("estado", estado);
        result.put("imagen", imagen);
        result.put("precio", precio);

        return result;
    }
}
