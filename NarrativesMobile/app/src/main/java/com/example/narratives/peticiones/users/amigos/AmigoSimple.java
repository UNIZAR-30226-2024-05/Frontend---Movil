package com.example.narratives.peticiones.users.amigos;

public class AmigoSimple {
    int id;
    String username;
    int img;

    public AmigoSimple(int id, String username, int img) {
        this.id = id;
        this.username = username;
        this.img = img;
    }

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
}
