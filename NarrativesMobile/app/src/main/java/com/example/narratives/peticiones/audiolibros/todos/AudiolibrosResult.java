package com.example.narratives.peticiones.audiolibros.todos;

import java.util.ArrayList;

public class AudiolibrosResult {
    String message;
    ArrayList<AudiolibroItem> audiolibros;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<AudiolibroItem> getAudiolibros() {
        return audiolibros;
    }

    public void setAudiolibros(ArrayList<AudiolibroItem> audiolibros) {
        this.audiolibros = audiolibros;
    }
}
