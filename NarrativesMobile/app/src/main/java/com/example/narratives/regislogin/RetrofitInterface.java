package com.example.narratives.regislogin;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    String URL_BASE = "http://20.199.84.234:8000";
    @POST("/users/login")
    Call<LoginResult> ejecutarInicioSesion(@Body HashMap<String, String> datos);

    @POST("/users/register")
    Call<RegisterResult> ejecutarRegistro(@Body HashMap<String, String> datos);

    @POST("/users/logout")
    Call<Void> ejecutarSalirSesion();

}
