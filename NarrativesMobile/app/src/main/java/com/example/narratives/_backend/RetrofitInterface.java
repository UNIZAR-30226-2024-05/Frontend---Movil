package com.example.narratives._backend;

import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibrosResult;
import com.example.narratives.peticiones.autores.AutorDatosResponse;
import com.example.narratives.peticiones.marcapaginas.ListeningRequest;
import com.example.narratives.peticiones.users.cambio_datos.CambioContrasenaRequest;
import com.example.narratives.peticiones.users.cambio_datos.CambioFotoPerfilRequest;
import com.example.narratives.peticiones.users.login.LoginRequest;
import com.example.narratives.peticiones.users.login.LoginResult;
import com.example.narratives.peticiones.users.perfiles.MiPerfilResponse;
import com.example.narratives.peticiones.users.register.RegisterRequest;
import com.example.narratives.peticiones.users.register.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {
    //String URL_BASE = "http://20.199.84.234:8000";

    //String URL_BASE = "https://narratives-backend.azurewebsites.net";
    String URL_BASE = "https://server.narratives.es";
    @POST("/users/login")
    Call<LoginResult> ejecutarUsersLogin(@Header("Cookie") String userCookie, @Body LoginRequest request);

    @POST("/users/register")
    Call<RegisterResult> ejecutarUsersRegister(@Body RegisterRequest request);

    @POST("/users/logout")
    Call<GenericMessageResult> ejecutarUsersLogout(@Header("Cookie") String userCookie);

    @POST("/users/change_pass")
    Call<GenericMessageResult> ejecutarUsersChange_pass(@Header("Cookie") String userCookie, @Body CambioContrasenaRequest request);

    @POST("/users/change_img")
    Call<GenericMessageResult> ejecutarUsersChange_img(@Header("Cookie") String userCookie, @Body CambioFotoPerfilRequest request);

    @GET("/users/profile")
    Call<MiPerfilResponse> ejecutarUsersProfile(@Header("Cookie") String userCookie);



    @GET("/audiolibros")
    Call<AudiolibrosResult> ejecutarAudiolibros(@Header("Cookie") String userCookie);

    @GET("/audiolibros/{id}")
    Call<AudiolibroEspecificoResponse> ejecutarAudiolibrosId(@Header("Cookie") String userCookie, @Path("id") int id);

    @GET("/autores/data/{id}")
    Call<AutorDatosResponse> ejecutarAutoresId(@Header("Cookie") String userCookie, @Path("id") int id);


    @POST("/marcapaginas/listening")
    Call<GenericMessageResult> ejecutarMarcapaginasListening(@Header("Cookie") String userCookie, @Body ListeningRequest request);
}
