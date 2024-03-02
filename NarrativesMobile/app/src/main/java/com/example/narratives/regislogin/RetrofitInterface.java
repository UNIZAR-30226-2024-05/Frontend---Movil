package com.example.narratives.regislogin;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/iniciarsesion")
    Call<LoginResult> ejecutarInicioSesion(@Body HashMap<String, String> datos);

    @POST("/registrar")
    Call<LoginResult> ejecutarRegistro(@Body HashMap<String, String> datos);

}
