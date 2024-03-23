package com.example.narratives.regislogin;

import com.example.narratives.peticiones.LoginRequest;
import com.example.narratives.peticiones.LoginResult;
import com.example.narratives.peticiones.RegisterRequest;
import com.example.narratives.peticiones.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitInterface {
    String URL_BASE = "http://20.199.84.234:8000";
    @POST("/users/login")
    Call<LoginResult> ejecutarInicioSesion(@Header("Cookie") String userCookie, @Body LoginRequest request);

    @POST("/users/register")
    Call<RegisterResult> ejecutarRegistro(@Body RegisterRequest request);

    @POST("/users/logout")
    Call<Void> ejecutarSalirSesion(@Header("Cookie") String userCookie);

}
