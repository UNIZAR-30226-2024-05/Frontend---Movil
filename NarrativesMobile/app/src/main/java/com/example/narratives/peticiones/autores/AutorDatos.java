package com.example.narratives.peticiones.autores;

import com.example.narratives.peticiones.audiolibros.especifico.Capitulo;
import com.example.narratives.peticiones.audiolibros.especifico.GenericReview;
import com.example.narratives.peticiones.audiolibros.especifico.OwnReview;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;

public class AutorDatos {
    private String nombre;
    private String informacion;
    private String Ciudad;
    private String Genero;
    private double media;
    private int id;
    ArrayList<AudiolibroItem> libros;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String Nombre) {
        this.nombre = Nombre;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String Ciudad) {
        this.informacion = Ciudad;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String Genero) {
        this.Genero = Genero;
    }
    
    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public double getMedia() {
        return media;
    }

    public void setMedia(double Media) {
        this.media = Media;
    }

    public ArrayList<AudiolibroItem> getAudiolibros() {
        return libros;
    }

    public void setAAudiolibros(ArrayList<AudiolibroItem> audiolibros) {
        this.libros = audiolibros;
    }
}
