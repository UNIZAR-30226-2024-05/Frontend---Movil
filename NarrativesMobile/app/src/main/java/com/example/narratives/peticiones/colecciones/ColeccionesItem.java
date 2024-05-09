package com.example.narratives.peticiones.colecciones;

public class ColeccionesItem {
    private int id;
    private String titulo;
    private int propietario;

    public ColeccionesItem(int id, String titulo, int propietario) {
        this.id = id;
        this.titulo = titulo;
        this.propietario = propietario;
    }

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

    public int getPropietario() {
        return propietario;
    }

    public void setPropietario(int propietario) {
        this.propietario = propietario;
    }
}
