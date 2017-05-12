package com.example.asier.vibbay03.Beans;

/**
 * Created by asier on 06/05/2017.
 */

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Puja {

    private double precio;

    public Puja(){
    }
    public Puja(double pprecio){
        this.precio = pprecio;
    }
    public double getPrecio(){
        return this.precio;
    }


    /////////////////
    private String idArt;
    private String idUsuario;
    public void setIdArt(String pidArt){
        this.idArt = pidArt;
    }
    public void setIdUsuario(String pidUsuario){
        this.idUsuario = pidUsuario;
    }
    public String getIdArt(){
        return this.idArt;
    }
    public String getIdUsuario(){
        return this.idUsuario;
    }
    /////////////////


    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("precio", precio);
        return result;
    }
}
