package com.example.narratives.informacion;

import static java.lang.Math.max;

import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InfoAudiolibros {

    public static AudiolibroEspecificoResponse audiolibroActual;

    public static AudiolibroEspecificoResponse getAudiolibroActual() {
        if(audiolibroActual == null){
            return new AudiolibroEspecificoResponse();
        } else {
            return audiolibroActual;
        }
    }

    public static void setAudiolibroActual(AudiolibroEspecificoResponse audiolibroActual) {
        InfoAudiolibros.audiolibroActual = audiolibroActual;
    }
    private static ArrayList<AudiolibroItem> todosLosAudiolibros;
    private static ArrayList<AudiolibroItem> todosLosAudiolibrosEjemplo;

    private static String[] generos = {"Todos", "Misterio", "Fantasía", "Romance", "Terror", "Ciencia ficción", "Histórico", "Infantil", "Mitología", "Humor", "Autoayuda", "Poesía", "Aventuras"};

    public static ArrayList<AudiolibroItem> getTodosLosAudiolibros() {
        return todosLosAudiolibros;
    }

    public static void setTodosLosAudiolibros(ArrayList<AudiolibroItem> todosLosAudiolibros) {
        if(todosLosAudiolibros != null){
            Collections.sort(todosLosAudiolibros, new Comparator<AudiolibroItem>() {
                @Override
                public int compare(AudiolibroItem a1, AudiolibroItem a2) {
                    return a1.getTitulo().compareToIgnoreCase(a2.getTitulo());
                }
            });
        }

        InfoAudiolibros.todosLosAudiolibros = todosLosAudiolibros;
    }

    public static String[] getGeneros() {
        return generos;
    }

    public static void setGeneros(String[] generos) {
        InfoAudiolibros.generos = generos;
    }

    public static ArrayList<AudiolibroItem> getTodosLosAudiolibrosEjemplo() {
        return todosLosAudiolibrosEjemplo;
    }

    public static void setTodosLosAudiolibrosEjemplo(ArrayList<AudiolibroItem> todosLosAudiolibrosEjemplo) {
        InfoAudiolibros.todosLosAudiolibrosEjemplo = todosLosAudiolibrosEjemplo;
    }

    public static ArrayList<AudiolibroItem> getAudiolibrosPorGenero(String genero){
        if(genero.equals("Todos") || genero.equals("todos")){
            return todosLosAudiolibros;
        }

        ArrayList<AudiolibroItem> audiolibrosPorGenero = new ArrayList<>();

        for(AudiolibroItem audiolibro : todosLosAudiolibros){
            if(audiolibro.getGenero().equals(genero)){
                audiolibrosPorGenero.add(audiolibro);
            }
        }

        return  audiolibrosPorGenero;
    }

    public static ArrayList<AudiolibroItem> getAudiolibrosPorAutor(String autor){
        ArrayList<AudiolibroItem> audiolibrosPorAutor = new ArrayList<>();

        for(AudiolibroItem audiolibro : todosLosAudiolibros){
            if(audiolibro.getAutor().equals(autor)){
                audiolibrosPorAutor.add(audiolibro);
            }
        }
        return  audiolibrosPorAutor;
    }

    public static String[] getGenerosSeleccionados(){
        String[] generosResultado = {"", "", "", "", ""};
        String[] todosLosGeneros = generos.clone();

        // Ponemos 'Todos' al final para no cogerlo
        String ultimoTemp = todosLosGeneros[todosLosGeneros.length - 1];
        todosLosGeneros[todosLosGeneros.length - 1] = todosLosGeneros[0];
        todosLosGeneros[0] = ultimoTemp;

        for(int i = 0; i < 5; i++){
            // Cogemos uno aleatorio sin contar los del final, que son los ya elegidos
            int indice = (int)(System.currentTimeMillis() % (todosLosGeneros.length - 2 - i));
            String seleccionado = todosLosGeneros[indice];

            // Intercambiamos el elegido por el final del string, para no repetir
            String temp = todosLosGeneros[todosLosGeneros.length - 2 - i];
            todosLosGeneros[todosLosGeneros.length - 2 - i] = todosLosGeneros[indice];
            todosLosGeneros[indice] = temp;

            // Metemos el elegido en el array resultado
            generosResultado[i] = seleccionado;
        }

        return generosResultado;
    }

    public static ArrayList<AudiolibroItem> getAudiolibrosRecomendados(int size){
        ArrayList<AudiolibroItem> audiolibrosRecomendados = new ArrayList<>();
        ArrayList<AudiolibroItem> todosOrdenados = (ArrayList<AudiolibroItem>) todosLosAudiolibros.clone();

        // Ordenamos por puntuación
        Collections.sort(todosOrdenados, new Comparator<AudiolibroItem>() {
            @Override
            public int compare(AudiolibroItem a1, AudiolibroItem a2) {
                int res = 0;
                if(a1.getPuntuacion() < a2.getPuntuacion()){
                    res = 1;
                } else if (a1.getPuntuacion() > a2.getPuntuacion()){
                    res = -1;
                }
                return res;
            }
        });

        int maxLibros = max(size,todosOrdenados.size());
        for(int i = 0; i < maxLibros; i++){
            audiolibrosRecomendados.add(todosOrdenados.get(i));
        }

        return  audiolibrosRecomendados;
    }

    public static void setTodoANull(){
        setTodosLosAudiolibros(null);
        setTodosLosAudiolibrosEjemplo(null);
    }


    //TODO: cuando se actualice, cambiar el return
    public static ArrayList<AudiolibroItem> getAudiolibrosSeguirEscuchando(){
        return new ArrayList<>();
    }




}
