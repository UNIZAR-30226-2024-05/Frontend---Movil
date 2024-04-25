package com.example.narratives.peticiones.audiolibros.especifico;

import java.util.ArrayList;

public class AudiolibroEspecificoResponse {
    AudiolibroSimple audiolibro;
    AutorSimple autor;
    ArrayList<Genero> generos;
    ArrayList<Capitulo> capitulos;
    ArrayList<GenericReview> public_reviews;
    ArrayList<GenericReview> friends_reviews;

    OwnReview own_review;
    UltimoMomento ultimo_momento;
    ArrayList<Marcapaginas> mp_personalizados;
    ArrayList<Coleccion> colecciones;

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
}
