package com.example.narratives.peticiones.users.login;

public class LoginResult {
    private String message;

    private LoginUser user;

    public String getMessage() {
        return message;
    }

    public String getUsername(){
        return user.getUsername();
    }
    public int getId() { return user.getId(); }

    public String getRole(){
        return user.getRole();
    }
}

