package com.example.narratives.peticiones;

public class User {
    private String username;
    private String role;

    public User(String _username, String _role){
        this.username = _username;
        this.role = _role;
    }
    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
