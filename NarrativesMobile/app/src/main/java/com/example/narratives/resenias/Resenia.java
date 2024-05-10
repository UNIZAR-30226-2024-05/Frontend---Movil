package com.example.narratives.resenias;

public class Resenia {

    String nombre, descripcion;
    float valoracion;

    public Resenia(String nombre, String descripcion, float valoracion){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valoracion = valoracion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public void setNombre(String nombre){
        this.nombre=nombre;
    }

    public void setValoracion(float valoracion){
        this.valoracion=valoracion;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public String getNombre(){
        return nombre;
    }

    public float getValoracion(){
        return valoracion;
    }
}