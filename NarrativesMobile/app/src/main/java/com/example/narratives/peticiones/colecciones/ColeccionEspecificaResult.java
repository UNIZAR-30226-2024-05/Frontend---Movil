package com.example.narratives.peticiones.colecciones;

import java.util.ArrayList;

public class ColeccionEspecificaResult {
    private String message;
    private ColeccionesItem coleccion;
    private String propietarioUsername;
    private boolean guardada;
    private ArrayList<AudiolibrosColeccionItem> audiolibros;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ColeccionesItem getColeccion() {
        return coleccion;
    }

    public void setColeccion(ColeccionesItem coleccion) {
        this.coleccion = coleccion;
    }

    public String getPropietarioUsername() {
        return propietarioUsername;
    }

    public void setPropietarioUsername(String propietarioUsername) {
        this.propietarioUsername = propietarioUsername;
    }

    public boolean isGuardada() {
        return guardada;
    }

    public void setGuardada(boolean guardada) {
        this.guardada = guardada;
    }

    public ArrayList<AudiolibrosColeccionItem> getAudiolibros() {
        return audiolibros;
    }

    public void setAudiolibros(ArrayList<AudiolibrosColeccionItem> audiolibros) {
        this.audiolibros = audiolibros;
    }
}
