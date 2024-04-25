package com.example.narratives._backend;

import com.example.narratives.peticiones.AudiolibrosResult;
import com.example.narratives.peticiones.CambioContrasenaRequest;
import com.example.narratives.peticiones.CambioFotoPerfilRequest;
import com.example.narratives.peticiones.LoginRequest;
import com.example.narratives.peticiones.LoginResult;
import com.example.narratives.peticiones.MiPerfilResponse;
import com.example.narratives.peticiones.RegisterRequest;
import com.example.narratives.peticiones.RegisterResult;
import com.example.narratives.peticiones.StandardMessageResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitInterface {
    //String URL_BASE = "http://20.199.84.234:8000";

    //String URL_BASE = "https://narratives-backend.azurewebsites.net";
    String URL_BASE = "https://server.narratives.es";
    @POST("/users/login")
    Call<LoginResult> ejecutarUsersLogin(@Header("Cookie") String userCookie, @Body LoginRequest request);

    @POST("/users/register")
    Call<RegisterResult> ejecutarUsersRegister(@Body RegisterRequest request);

    @POST("/users/logout")
    Call<StandardMessageResult> ejecutarUsersLogout(@Header("Cookie") String userCookie);

    @POST("/users/change_pass")
    Call<StandardMessageResult> ejecutarUsersChange_pass(@Header("Cookie") String userCookie, @Body CambioContrasenaRequest request);

    @POST("/users/change_img")
    Call<StandardMessageResult> ejecutarUsersChange_img(@Header("Cookie") String userCookie, @Body CambioFotoPerfilRequest request);

    @GET("/users/profile")
    Call<MiPerfilResponse> ejecutarUsersProfile(@Header("Cookie") String userCookie);

    @GET("/audiolibros")
    Call<AudiolibrosResult> ejecutarAudiolibros(@Header("Cookie") String userCookie);
}
