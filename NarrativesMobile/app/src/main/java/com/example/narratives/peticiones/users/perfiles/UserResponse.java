package com.example.narratives.peticiones.users.perfiles;

import com.example.narratives.peticiones.amistad.amigos.ColeccionDeAmigo;
import com.example.narratives.peticiones.amistad.amigos.UltimaActividad;

import java.util.ArrayList;

public class UserResponse {
    int id;
    String username;
    int img;
    ArrayList<ColeccionDeAmigo> colecciones;
    int estado;
    UltimaActividad ultimo;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public UltimaActividad getUltimo() {
        return ultimo;
    }

    public void setUltimo(UltimaActividad ultimo) {
        this.ultimo = ultimo;
    }

}
