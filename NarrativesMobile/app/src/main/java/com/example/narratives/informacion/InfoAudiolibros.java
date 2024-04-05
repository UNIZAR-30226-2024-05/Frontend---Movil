package com.example.narratives.informacion;

import com.example.narratives.peticiones.Audiolibro;

import java.util.ArrayList;

public class InfoAudiolibros {
    private static ArrayList<Audiolibro> todosLosAudiolibros;

    public static ArrayList<Audiolibro> getTodosLosAudiolibros() {
        return todosLosAudiolibros;
    }

    public static void setTodosLosAudiolibros(ArrayList<Audiolibro> todosLosAudiolibros) {
        InfoAudiolibros.todosLosAudiolibros = todosLosAudiolibros;
    }
}
