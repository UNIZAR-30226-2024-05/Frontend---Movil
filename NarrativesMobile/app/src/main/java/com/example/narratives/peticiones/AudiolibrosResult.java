package com.example.narratives.peticiones;

import java.util.ArrayList;

public class AudiolibrosResult {
    String message;
    ArrayList<Audiolibro> audiolibros;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Audiolibro> getAudiolibros() {
        return audiolibros;
    }

    public void setAudiolibros(ArrayList<Audiolibro> audiolibros) {
        this.audiolibros = audiolibros;
    }
}
