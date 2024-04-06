package com.example.narratives.informacion;

import com.example.narratives.peticiones.Audiolibro;

import java.util.ArrayList;

public class InfoAudiolibros {
    private static ArrayList<Audiolibro> todosLosAudiolibros;
    private static String[] generos = {"Todos", "Romance", "Terror", "Infantil", "Misterio"};

    public static ArrayList<Audiolibro> getTodosLosAudiolibros() {
        return todosLosAudiolibros;
    }

    public static void setTodosLosAudiolibros(ArrayList<Audiolibro> todosLosAudiolibros) {
        InfoAudiolibros.todosLosAudiolibros = todosLosAudiolibros;
    }

    public static String[] getGeneros() {
        return generos;
    }

    public static void setGeneros(String[] generos) {
        InfoAudiolibros.generos = generos;
    }
}
