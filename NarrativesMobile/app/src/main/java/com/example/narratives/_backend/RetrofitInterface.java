package com.example.narratives._backend;

import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.GenericOtherIdRequest;
import com.example.narratives.peticiones.amistad.amigos.AmigosResponse;
import com.example.narratives.peticiones.amistad.lista.AmistadListaResponse;
import com.example.narratives.peticiones.amistad.peticiones.AmistadPeticionesResponse;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.especifico.OwnReview;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibrosResult;
import com.example.narratives.peticiones.clubes.ClubRequest;
import com.example.narratives.peticiones.clubes.ClubResult;
import com.example.narratives.peticiones.clubes.ClubesResult;
import com.example.narratives.peticiones.colecciones.AnadirEliminarAudiolibroDeColeccionRequest;
import com.example.narratives.peticiones.colecciones.AnadirEliminarColeccionRequest;
import com.example.narratives.peticiones.colecciones.ColeccionEspecificaResult;
import com.example.narratives.peticiones.colecciones.ColeccionesRequest;
import com.example.narratives.peticiones.colecciones.ColeccionesResult;
import com.example.narratives.peticiones.autores.AutorDatosResponse;
import com.example.narratives.peticiones.marcapaginas.ListeningRequest;
import com.example.narratives.peticiones.resenas.ResenaDeleteRequest;
import com.example.narratives.peticiones.resenas.ResenaEditRequest;
import com.example.narratives.peticiones.resenas.ResenaPostRequest;
import com.example.narratives.peticiones.users.cambio_datos.CambioContrasenaRequest;
import com.example.narratives.peticiones.users.cambio_datos.CambioFotoPerfilRequest;
import com.example.narratives.peticiones.users.login.LoginRequest;
import com.example.narratives.peticiones.users.login.LoginResult;
import com.example.narratives.peticiones.users.perfiles.MiPerfilResponse;
import com.example.narratives.peticiones.users.perfiles.UserResponse;
import com.example.narratives.peticiones.users.register.RegisterRequest;
import com.example.narratives.peticiones.users.register.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {
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

    @GET("/users/{id}")
    Call<UserResponse> ejecutarUsersId(@Header("Cookie") String userCookie, @Path("id") int id);

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


    @POST("/amistad/send")
    Call<GenericMessageResult> ejecutarAmistadSend(@Header("Cookie") String userCookie, @Body GenericOtherIdRequest other_id);

    @POST("/amistad/accept")
    Call<GenericMessageResult> ejecutarAmistadAccept(@Header("Cookie") String userCookie, @Body GenericOtherIdRequest other_id);

    @POST("/amistad/reject")
    Call<GenericMessageResult> ejecutarAmistadReject(@Header("Cookie") String userCookie, @Body GenericOtherIdRequest other_id);

    @POST("/amistad/cancel")
    Call<GenericMessageResult> ejecutarAmistadCancel(@Header("Cookie") String userCookie, @Body GenericOtherIdRequest other_id);

    @POST("/amistad/remove")
    Call<GenericMessageResult> ejecutarAmistadRemove(@Header("Cookie") String userCookie, @Body GenericOtherIdRequest other_id);

    @GET("/amistad/amigos")
    Call<AmigosResponse> ejecutarAmistadAmigos(@Header("Cookie") String userCookie);

    @GET("/amistad/lista")
    Call<AmistadListaResponse> ejecutarAmistadLista(@Header("Cookie") String userCookie);


    @POST("/marcapaginas/listening")
    Call<GenericMessageResult> ejecutarMarcapaginasListening(@Header("Cookie") String userCookie, @Body ListeningRequest request);

    @GET("/amistad/peticiones")
    Call<AmistadPeticionesResponse> ejecutarAmistadPeticiones(@Header("Cookie") String userCookie);


    @GET("/club/lista")
    Call<ClubesResult> ejecutarMyClubes(@Header("Cookie") String userCookie);

    @POST("/club/create")
    Call<ClubResult> ejecutarCreateClub(@Header("Cookie") String userCookie, @Body ClubRequest request);

    @GET("/club/all")
    Call<ClubesResult> ejecutarBuscarClubes(@Header("Cookie") String userCookie);
    
    @GET("/club/datos/{id}")
    Call<ClubResult> ejecutarInfoClub(@Header("Cookie") String userCookie, @Path("id") int id);
    @POST("/club/join")
    Call<GenericMessageResult> ejecutarJoinClub(@Header("Cookie") String userCookie, @Body ClubRequest request);
    @POST("/club/left")
    Call<GenericMessageResult> ejecutarLeaveClub(@Header("Cookie") String userCookie, @Body ClubRequest request);
    @POST("/club/delete")
    Call<GenericMessageResult> ejecutarDeleteClub(@Header("Cookie") String userCookie, @Body ClubRequest request);

    @POST("/review/post_review")
    Call<OwnReview> ejecutarReviewPostReview(@Header("Cookie") String userCookie, @Body ResenaPostRequest request);
    @POST("/review/edit_review")
    Call<OwnReview> ejecutarReviewEditReview(@Header("Cookie") String userCookie, @Body ResenaEditRequest request);
    @POST("/review/delete_review")
    Call<GenericMessageResult> ejecutarReviewDeleteReview(@Header("Cookie") String userCookie, @Body ResenaDeleteRequest request);
}
