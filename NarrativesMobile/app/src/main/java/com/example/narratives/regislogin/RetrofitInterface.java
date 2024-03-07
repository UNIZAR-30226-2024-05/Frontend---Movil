package com.example.narratives.regislogin;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/users/login")
    Call<LoginResult> ejecutarInicioSesion(@Body HashMap<String, String> datos);

    @POST("/users/register")
    Call<Void> ejecutarRegistro(@Body HashMap<String, String> datos);

}
