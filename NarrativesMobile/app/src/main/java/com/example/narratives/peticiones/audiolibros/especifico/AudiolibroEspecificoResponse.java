package com.example.narratives.peticiones.audiolibros.especifico;

import java.util.ArrayList;

public class AudiolibroEspecificoResponse {
    private AudiolibroSimple audiolibro;
    private AutorSimple autor;
    private ArrayList<Genero> generos;
    private ArrayList<Capitulo> capitulos;
    private ArrayList<GenericReview> public_reviews;
    private ArrayList<GenericReview> friends_reviews;

    private float puntuacion;
    private OwnReview own_review;
    private UltimoMomento ultimo_momento;
    private ArrayList<Marcapaginas> mp_personalizados;
    private ArrayList<Coleccion> colecciones;

    public AudiolibroSimple getAudiolibro() {
        return audiolibro;
    }

    public void setAudiolibro(AudiolibroSimple audiolibro) {
        this.audiolibro = audiolibro;
    }

    public AutorSimple getAutor() {
        return autor;
    }

    public void setAutor(AutorSimple autor) {
        this.autor = autor;
    }

    public ArrayList<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(ArrayList<Genero> generos) {
        this.generos = generos;
    }

    public ArrayList<Capitulo> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(ArrayList<Capitulo> capitulos) {
        this.capitulos = capitulos;
    }

    public ArrayList<GenericReview> getPublicReviews() {
        return public_reviews;
    }

    public void setPublicReviews(ArrayList<GenericReview> public_reviews) {
        this.public_reviews = public_reviews;
    }

    public ArrayList<GenericReview> getFriendsReviews() {
        return friends_reviews;
    }

    public void setFriendsReviews(ArrayList<GenericReview> friends_reviews) {
        this.friends_reviews = friends_reviews;
    }

    public OwnReview getOwnReview() {
        return own_review;
    }

    public void setOwnReview(OwnReview own_review) {
        this.own_review = own_review;
    }

    public UltimoMomento getUltimoMomento() {
        return ultimo_momento;
    }

    public void setUltimoMomento(UltimoMomento ultimo_momento) {
        this.ultimo_momento = ultimo_momento;
    }

    public ArrayList<Marcapaginas> getMarcapaginas() {
        return mp_personalizados;
    }

    public void setMarcapaginas(ArrayList<Marcapaginas> mp_personalizados) {
        this.mp_personalizados = mp_personalizados;
    }

    public ArrayList<Coleccion> getColecciones() {
        return colecciones;
    }

    public void setColecciones(ArrayList<Coleccion> colecciones) {
        this.colecciones = colecciones;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }
    public String getNombreCap(int num) {
        for (Capitulo capitulo : capitulos) {
            if(capitulo.getId() == num){
                return capitulo.getNombre();
            }
        }
        //Por si recorre todo y no lo encuenta
        return "Capítulo " + String.valueOf(num);
    }
}
