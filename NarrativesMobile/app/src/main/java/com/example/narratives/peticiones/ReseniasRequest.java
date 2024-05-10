package com.example.narratives.peticiones;

public class ReseniasRequest {
    String nombre, descripcion;
    float valoracion;

    char visibilidad;

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public void setNombre(String nombre){
        this.nombre=nombre;
    }

    public void setValoracion(float valoracion){
        this.valoracion=valoracion;
    }

    public void setVisibilidad(char visibilidad){
        this.visibilidad=visibilidad;
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
