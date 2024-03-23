package com.example.narratives.regislogin;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginResult {
    private String message;

    private User user;

    public String getMessage() {
        return message;
    }

    public String getUsername(){
        return user.getUsername();
    }

    public String getRole(){
        return user.getRole();
    }
}

