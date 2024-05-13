package com.example.narratives.peticiones;

public class AniadirReseniasResult {
    int id;
    String comentario;
    int puntuacion;
    char visibilidad;
    String fecha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public char getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(char visibilidad) {
        this.visibilidad = visibilidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
