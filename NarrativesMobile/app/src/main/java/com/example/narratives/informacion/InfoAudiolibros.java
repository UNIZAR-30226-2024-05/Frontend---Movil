package com.example.narratives.informacion;

import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;

public class InfoAudiolibros {
    private static ArrayList<AudiolibroItem> todosLosAudiolibros;
    private static String[] generos = {"Todos", "Romance", "Terror", "Infantil", "Misterio"};

    public static ArrayList<AudiolibroItem> getTodosLosAudiolibros() {
        return todosLosAudiolibros;
    }

    public static void setTodosLosAudiolibros(ArrayList<AudiolibroItem> todosLosAudiolibros) {
        InfoAudiolibros.todosLosAudiolibros = todosLosAudiolibros;
    }

    public static String[] getGeneros() {
        return generos;
    }

    public static void setGeneros(String[] generos) {
        InfoAudiolibros.generos = generos;
    }
}
