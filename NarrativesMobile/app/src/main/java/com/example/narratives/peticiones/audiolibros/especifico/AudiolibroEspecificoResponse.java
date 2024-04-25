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
}
