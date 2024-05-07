package com.example.narratives.peticiones.users.perfiles;

import com.example.narratives.peticiones.users.amigos.UltimaActividad;

public class UserResponse {
    String id;
    String username;
    int img;
    //ArrayList<Coleccion> colecciones...
    String estado;
    UltimaActividad ultimo;



    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public UltimaActividad getUltimo() {
        return ultimo;
    }

    public void setUltimo(UltimaActividad ultimo) {
        this.ultimo = ultimo;
    }

}
