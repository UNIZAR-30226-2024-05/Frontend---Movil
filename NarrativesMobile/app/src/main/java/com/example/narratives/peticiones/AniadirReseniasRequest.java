package com.example.narratives.peticiones;

public class AniadirReseniasRequest {

    int id_audiolibro;
    String comentario;
    String puntuacion;
    char visibilidad;


    public int getId_audiolibro() {
        return id_audiolibro;
    }

    public void setId_audiolibro(int id_audiolibro) {
        this.id_audiolibro = id_audiolibro;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }

    public char getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(char visibilidad) {
        this.visibilidad = visibilidad;
    }
}
