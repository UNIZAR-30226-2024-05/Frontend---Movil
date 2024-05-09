package com.example.narratives.peticiones.amistad.lista;

import java.util.ArrayList;

public class AmistadListaResponse {

    ArrayList<UsuarioEstado> amigos;

    public ArrayList<UsuarioEstado> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<UsuarioEstado> amigos) {
        this.amigos = amigos;
    }
}
