package com.example.narratives.peticiones.home;

import java.util.ArrayList;

public class HomeResult {
    String message;
    LibroEscuchado ultimo;
    ArrayList<LibroEscuchado> seguir_escuchando;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LibroEscuchado getUltimo() {
        return ultimo;
    }

    public void setUltimo(LibroEscuchado ultimo) {
        this.ultimo = ultimo;
    }

    public ArrayList<LibroEscuchado> getSeguir_escuchando() {
        return seguir_escuchando;
    }

    public void setSeguir_escuchando(ArrayList<LibroEscuchado> seguir_escuchando) {
        this.seguir_escuchando = seguir_escuchando;
    }
}
