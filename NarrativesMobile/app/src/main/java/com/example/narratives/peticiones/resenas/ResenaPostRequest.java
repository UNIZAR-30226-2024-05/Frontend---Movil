package com.example.narratives.peticiones.resenas;

public class ResenaPostRequest {
    private int id_audiolibro;
    private String comentario;
    private float puntuacion;
    private int visibilidad;

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

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(int visibilidad) {
        this.visibilidad = visibilidad;
    }
}
