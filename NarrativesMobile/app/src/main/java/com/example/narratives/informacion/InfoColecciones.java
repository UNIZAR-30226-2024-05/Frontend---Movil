package com.example.narratives.informacion;

import com.example.narratives.peticiones.colecciones.AudiolibrosColeccionItem;
import com.example.narratives.peticiones.colecciones.ColeccionesItem;

import java.util.ArrayList;

public class InfoColecciones {
    private static ArrayList<ColeccionesItem> todasLasColecciones;
    private static ArrayList<AudiolibrosColeccionItem> todosLosAudiolibros;

    private static String propietarioUsername;

    private static boolean guardada;

    public static ArrayList<ColeccionesItem> getTodasLasColecciones() {
        return todasLasColecciones;
    }

    public static void setTodasLasColecciones(ArrayList<ColeccionesItem> todasLasColecciones) {
        InfoColecciones.todasLasColecciones = todasLasColecciones;
    }

    public static ArrayList<AudiolibrosColeccionItem> getTodosLosAudiolibros() {
        return todosLosAudiolibros;
    }

    public static void setTodosLosAudiolibros(ArrayList<AudiolibrosColeccionItem> todosLosAudiolibros) {
        InfoColecciones.todosLosAudiolibros = todosLosAudiolibros;
    }

    public static String getPropietarioUsername() {
        return propietarioUsername;
    }

    public static void setPropietarioUsername(String propietarioUsername) {
        InfoColecciones.propietarioUsername = propietarioUsername;
    }

    public static boolean isGuardada() {
        return guardada;
    }

    public static void setGuardada(boolean guardada) {
        InfoColecciones.guardada = guardada;
    }
}
