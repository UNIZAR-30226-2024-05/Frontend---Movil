package com.example.narratives.peticiones.clubes;

import java.io.Serializable;
import java.util.ArrayList;

public class Club implements Serializable {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean isadmin;
    private boolean ismember;
    private Audiolibro audiolibro;
    private ArrayList<UserMember> members;
    private ArrayList<Message> messages;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.nombre;
    }
    public String getDescription() { return this.descripcion; }
    public boolean isAdmin() { return this.isadmin; }
    public void setIsAdmin(boolean isAdmin) {
        this.isadmin = isAdmin;
    }
    public boolean isMember() { return this.ismember; }
    public Audiolibro getAudiolibro() { return this.audiolibro; }
    public ArrayList<UserMember> getMembers() { return this.members; }
    public ArrayList<Message> getMessages() {
        return this.messages;
    }
    public void setMessages(ArrayList<Message> msgs) {
        this.messages = msgs;
    }
    public void addMessage(Message msg) {
        this.messages.add(msg);
    }

    public class Audiolibro implements Serializable {
        private int id;
        private String titulo;
        private String img;
        public int getId() { return this.id; }
        public String getTitulo() { return this.titulo; }
        public String getImg() { return this.img; }
    }

    public class UserMember implements Serializable {
        private int id;
        private String username;
        private int img;
        private boolean isAdmin;

        public int getId() {
            return this.id;
        }

        public String getUsername() {
            return this.username;
        }

        public int getImg() {
            return this.img;
        }
        public boolean isAdmin() {
            return this.isAdmin;
        }
    }
}
