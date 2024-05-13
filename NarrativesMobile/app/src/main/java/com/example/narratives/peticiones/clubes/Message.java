package com.example.narratives.peticiones.clubes;

import java.io.Serializable;

public class Message  implements Serializable {
    private int id;
    private int user_id;
    private String username;
    private String mensaje;
    private String fecha;
    public Message(){}
    public void setId(int _id) {
        this.id = _id;
    }
    public int getId() { return this.id; }
    public void setUserId(int _user_id) {
        this.user_id = _user_id;
    }
    public int getUserId() { return this.user_id; }
    public void setUsername(String _username) {
        this.username = _username;
    }
    public String getUsername() { return this.username; }
    public void setMessage(String _mensaje) {
        this.mensaje = _mensaje;
    }
    public String getMessage() { return this.mensaje; }
    public void setDate(String _fecha) {
        this.fecha = _fecha;
    }
    public String getDate() { return this.fecha; }
}
