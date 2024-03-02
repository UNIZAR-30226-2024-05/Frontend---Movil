package com.example.narratives.regislogin;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
    public String getUsuario() {
        return usuario;
    }

    public String getCorreo() {
        return correo;
    }

    @SerializedName("username")
    private String usuario;

    @SerializedName("mail")
    private String correo;
}
