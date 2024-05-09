package com.example.narratives.peticiones.colecciones;

import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;

public class ColeccionesResult {
    private String message;
    private ArrayList<ColeccionesItem> collections;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ColeccionesItem> getCollections() {
        return collections;
    }

    public void setCollections(ArrayList<ColeccionesItem> collections) {
        this.collections = collections;
    }
}
