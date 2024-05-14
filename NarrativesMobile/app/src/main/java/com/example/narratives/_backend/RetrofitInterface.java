package com.example.narratives._backend;

import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibrosResult;
import com.example.narratives.peticiones.colecciones.AnadirEliminarAudiolibroDeColeccionRequest;
import com.example.narratives.peticiones.colecciones.AnadirEliminarColeccionRequest;
import com.example.narratives.peticiones.colecciones.ColeccionEspecificaResult;
import com.example.narratives.peticiones.colecciones.ColeccionesRequest;
import com.example.narratives.peticiones.colecciones.ColeccionesResult;
import com.example.narratives.peticiones.clubes.ClubRequest;
import com.example.narratives.peticiones.clubes.ClubesResult;
import com.example.narratives.peticiones.clubes.ClubResult;
import com.example.narratives.peticiones.autores.AutorDatosResponse;
import com.example.narratives.peticiones.marcapaginas.CrearMarcapaginasRequest;
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
    //String URL_BASE = "https://52.143.153.248";
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

    @GET("/colecciones")
    Call<ColeccionesResult> ejecutarColecciones(@Header("Cookie") String userCookie);

    @GET("/colecciones/{coleccionId}")
    Call<ColeccionEspecificaResult> ejecutarColeccionesId(@Header("Cookie") String userCookie, @Path("coleccionId") int coleccionId);
    
    @GET("/autores/data/{id}")
    Call<AutorDatosResponse> ejecutarAutoresId(@Header("Cookie") String userCookie, @Path("id") int id);

    @POST("/colecciones/create")
    Call<GenericMessageResult> ejecutarColeccionesCreate(@Header("Cookie") String userCookie, @Body ColeccionesRequest request);

    @POST("/colecciones/remove")
    Call<GenericMessageResult> ejecutarColeccionesRemove(@Header("Cookie") String userCookie, @Body AnadirEliminarColeccionRequest request);

    @POST("/colecciones/friend")
    Call<GenericMessageResult> ejecutarColeccionesFriend(@Header("Cookie") String userCookie, @Body AnadirEliminarColeccionRequest request);

    @POST("/colecciones/anadirAudiolibro")
    Call<GenericMessageResult> ejecutarColeccionesAnadirAudiolibro(@Header("Cookie") String userCookie, @Body AnadirEliminarAudiolibroDeColeccionRequest request);

    @POST("/colecciones/eliminarAudiolibro")
    Call<GenericMessageResult> ejecutarColeccionesEliminarAudiolibro(@Header("Cookie") String userCookie, @Body AnadirEliminarAudiolibroDeColeccionRequest request);

    @POST("/marcapaginas/listening")
    Call<GenericMessageResult> ejecutarMarcapaginasListening(@Header("Cookie") String userCookie, @Body ListeningRequest request);

    @GET("/club/lista")
    Call<ClubesResult> ejecutarMyClubes(@Header("Cookie") String userCookie);

    @POST("/club/create")
    Call<ClubResult> ejecutarCreateClub(@Header("Cookie") String userCookie, @Body ClubRequest request);

    @GET("/club/all")
    Call<ClubesResult> ejecutarBuscarClubes(@Header("Cookie") String userCookie);
    
    @GET("/club/datos/{id}")
    Call<ClubResult> ejecutarInfoClub(@Header("Cookie") String userCookie, @Path("id") int id);
    @POST("/marcapaginas/create")
    Call<GenericMessageResult> ejecutarCreateMarcapaginas(@Header("Cookie") String userCookie, @Body CrearMarcapaginasRequest request);

    @POST("/marcapaginas/update")
    Call<GenericMessageResult> ejecutarUpdateMarcapaginas(@Header("Cookie") String userCookie, @Body CrearMarcapaginasRequest request);

    @POST("/marcapaginas/delete")
    Call<GenericMessageResult> ejecutarDeleteMarcapaginas(@Header("Cookie") String userCookie, @Body CrearMarcapaginasRequest request);
}
