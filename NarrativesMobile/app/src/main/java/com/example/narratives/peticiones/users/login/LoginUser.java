package com.example.narratives.peticiones.users.login;

public class LoginUser {
    private int user_id;
    private String username;
    private String role;

    public LoginUser(String _username, String _role){
        this.username = _username;
        this.role = _role;
    }

    public int getId() {
        return this.user_id;
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
