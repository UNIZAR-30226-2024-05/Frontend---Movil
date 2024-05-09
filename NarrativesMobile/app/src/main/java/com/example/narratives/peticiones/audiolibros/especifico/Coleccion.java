package com.example.narratives.peticiones.audiolibros.especifico;

public class Coleccion {
    int id;
    String titulo;

    boolean pertenece;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setTitulo(boolean pertenece) {
        this.pertenece = pertenece;
    }

    public boolean getPertenece() {
        return pertenece;
    }

    public void setPertenece(boolean pertenece) {
        this.pertenece = pertenece;
    }
}
