package com.example.narratives.peticiones.clubes;

import java.io.Serializable;
import java.util.ArrayList;

public class Club implements Serializable {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean isAdmin;
    private boolean isMember;
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
    public boolean isAdmin() { return this.isAdmin; }
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public boolean isMember() { return this.isMember; }
    public Audiolibro getAudiolibro() { return this.audiolibro; }
    public ArrayList<UserMember> getMembers() { return this.members; }
    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    public class Audiolibro {
        private int id;
        private String titulo;
        private String img;
        public int getId() { return this.id; }
        public String getTitulo() { return this.titulo; }
        public String getImg() { return this.img; }
    }

    public class UserMember {
        private int id;
        private String username;
        private char img;
        private boolean isAdmin;

        public int getId() {
            return this.id;
        }

        public String getUsername() {
            return this.username;
        }

        public char getImg() {
            return this.img;
        }
        public boolean isAdmin() {
            return this.isAdmin;
        }
    }

    public class Message {
        private int id;
        private int user_id;
        private String username;
        private String mensaje;
        private String fecha;
        public int getId() { return this.id; }
        public int getUserId() { return this.user_id; }
        public String getUsername() { return this.username; }
        public String getMessage() { return this.mensaje; }
        public String getDate() { return this.fecha; }
    }
}
