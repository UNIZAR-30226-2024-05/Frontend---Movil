package com.example.narratives.peticiones.audiolibros.todos;

public class AudiolibroItem {
    int id;
    String titulo;
    int autor_id;
    String autor;
    String descripcion;
    String img;

    String genero;

    float puntuacion;

    public AudiolibroItem(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public AudiolibroItem(int id, String titulo, int autor_id, String autor, String descripcion, String img, String genero, float puntuacion){
        this.id = id;
        this.titulo = titulo;
        this.autor_id = autor_id;
        this.autor = autor;
        this.descripcion = descripcion;
        this.img = img;
        this.genero = genero;
        this.puntuacion = puntuacion;
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

    public int getAutor_id() {
        return autor_id;
    }

    public void setAutor_id(int autor_id) {
        this.autor_id = autor_id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }
}
