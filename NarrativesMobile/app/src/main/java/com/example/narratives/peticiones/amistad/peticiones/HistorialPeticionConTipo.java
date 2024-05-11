package com.example.narratives.peticiones.amistad.peticiones;

import com.example.narratives.activities.FechaParser;

public class HistorialPeticionConTipo {
    int user_id;
    String username;
    int tipo; //0-enviada(de user) 1-recibida(a user) 2-aceptada(por el otro) 3-rechazada(por el otro)

    FechaParser fecha;


    public HistorialPeticionConTipo(HistorialPeticionGenerica _peticion, int _tipo){
        user_id = _peticion.getUser_id();
        username = _peticion.getUsername();
        tipo = _tipo;
        fecha = new FechaParser(_peticion.getFecha());
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public FechaParser getFecha() {
        return fecha;
    }

    public void setFecha(FechaParser fecha) {
        this.fecha = fecha;
    }
}
