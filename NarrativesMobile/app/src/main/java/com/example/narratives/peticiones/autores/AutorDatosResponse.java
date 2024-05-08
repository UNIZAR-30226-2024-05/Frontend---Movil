package com.example.narratives.peticiones.autores;

import com.example.narratives.peticiones.audiolibros.especifico.AutorSimple;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;

public class AutorDatosResponse {

    Autor autor;
    private String generoMasEscrito;
    private double NotaMedia;
    ArrayList<AudiolibroItem> libros;

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getgeneroMasEscrito() {
        return generoMasEscrito;
    }

    public void setgeneroMasEscrito(String generoMasEscrito) {
        this.generoMasEscrito = generoMasEscrito;
    }

    public double getNotaMedia() {
        return NotaMedia;
    }

    public void setNotaMedia(double NotaMedia) {
        this.NotaMedia = NotaMedia;
    }

    public ArrayList<AudiolibroItem> getAudiolibros() {
        return libros;
    }

    public void setAAudiolibros(ArrayList<AudiolibroItem> audiolibros) {
        this.libros = audiolibros;
    }
}
