package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.fragments.FragmentAmigos;
import com.example.narratives.informacion.InfoAmigos;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.GenericOtherIdRequest;
import com.example.narratives.peticiones.users.perfiles.UserResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InfoAmigoActivity extends AppCompatActivity {

    private UserResponse amigoActual;
    FloatingActionButton fabAtras;

    MaterialButton botonSolicitud;
    TextView estado;
    TextView nombre;

    TextView tituloUltimo;
    ShapeableImageView portada;
    ShapeableImageView fotoPerfil;

    RetrofitInterface retrofitInterface;

    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.activity_info_amigo);
        super.onCreate(savedInstanceState);

        retrofitInterface = ApiClient.getRetrofitInterface();
        amigoActual = InfoAmigos.getAmigoActual();

        nombre = (TextView) findViewById(R.id.textViewNombreInfoAmigo);
        nombre.setText(amigoActual.getUsername());

        fotoPerfil = (ShapeableImageView) findViewById(R.id.imageViewFotoInfoAmigo);
        fotoPerfil.setImageResource(InfoAmigos.getImageResourceFromImgCode(amigoActual.getImg()));

        tituloUltimo = (TextView) findViewById(R.id.textViewUltimaActividadInfoAmigo);
        portada = (ShapeableImageView) findViewById(R.id.imageViewPortadaInfoAmigo);

        if(amigoActual.getUltimo() != null){
            Glide
                    .with(this)
                    .load(amigoActual.getUltimo().getImg())
                    .centerCrop()
                    .placeholder(R.drawable.icono_imagen_estandar_foreground)
                    .into(portada);
        }

        estado = (TextView) findViewById(R.id.textViewEstadoInfoAmigo);
        estado.setText(InfoAmigos.getEstadoStringFromId(amigoActual.getEstado()));

        fabAtras = (FloatingActionButton) findViewById(R.id.botonVolverDesdeInfoAmigo);
        fabAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMain();
            }
        });

        botonSolicitud = (MaterialButton) findViewById(R.id.botonSolicitudInfoAmigo);
        botonSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(botonSolicitud.getText().equals("Eliminar amigo")){
                    peticionAmistadRemove(amigoActual.getId());
                } else if(botonSolicitud.getText().equals("Enviar solicitud")){
                    peticionAmistadSend(amigoActual.getId());
                } else if(botonSolicitud.getText().equals("Solicitud recibida")) {
                    abrirMensajeGestionarRecibida(amigoActual);
                } else if(botonSolicitud.getText().equals("Solicitud enviada")) {
                    peticionAmistadCancel(amigoActual.getId());
                }
            }
        });

        if(amigoActual.getEstado() == 1){
            cambiarEstadoAEliminado();
        } else if (amigoActual.getEstado() == 2){
            cambiarEstadoAEnviado();
        } else if (amigoActual.getEstado() == 3){
            cambiarEstadoARecibido();
        } // else: es amigo

        MaterialButton botonVerColeccionesInfoAmigo = findViewById(R.id.botonVerColeccionesInfoAmigo);
        botonVerColeccionesInfoAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarColeccionesAmigo();
            }
        });
    }

    private void abrirMensajeGestionarRecibida(UserResponse usuario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡" + usuario.getUsername() + " quiere ser tu amigo!");
        builder.setMessage("¿Quieres aceptar la solicitud de amistad de " + usuario.getUsername() + "?");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                peticionAmistadAccept(usuario.getId());
            }
        });

        builder.setNegativeButton("Rechazar    |", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                peticionAmistadReject(usuario.getId());
            }
        });

        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(alertDialog != null && alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }


    private void cambiarEstadoAEliminado(){
        portada.setVisibility(View.GONE);
        tituloUltimo.setVisibility(View.GONE);
        botonSolicitud.setText("Enviar solicitud");
        botonSolicitud.setTextColor(getResources().getColor(R.color.white));
        botonSolicitud.setIconResource(R.drawable.icono_enviar);
        botonSolicitud.setIconTintResource(R.color.teal_casi_blanco);
        estado.setText("No sois amigos");
    }

    private void cambiarEstadoAEnviado(){
        portada.setVisibility(View.GONE);
        tituloUltimo.setVisibility(View.GONE);
        botonSolicitud.setText("Solicitud enviada");
        botonSolicitud.setTextColor(getResources().getColor(R.color.teal_claro));
        estado.setText("Solicitud enviada");
    }


    private void cambiarEstadoARecibido() {
        portada.setVisibility(View.GONE);
        tituloUltimo.setVisibility(View.GONE);
        botonSolicitud.setText("Solicitud recibida");
        botonSolicitud.setIconResource(R.drawable.icono_recibir);
        botonSolicitud.setIconTintResource(R.color.teal_casi_blanco);
    }

    private void peticionAmistadRemove(int id){
        GenericOtherIdRequest request = new GenericOtherIdRequest(id);

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarAmistadRemove(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {

                if(response.code() == 200) {
                    cambiarEstadoAEliminado();
                    FragmentAmigos.actualizarLista = true;

                }  else if (response.code() == 409){

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("Doesn't exist friendship")){
                            Toast.makeText(InfoAmigoActivity.this, "No puedes eliminar si no sois amigos", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(InfoAmigoActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(InfoAmigoActivity.this, "Algo ha fallado obteniendo el error (amigosCancel)", Toast.LENGTH_LONG).show();
                    }
                }  else if (response.code() == 500){
                    Toast.makeText(InfoAmigoActivity.this, "Error del server (amigosCancel)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(InfoAmigoActivity.this, "Código de error (amigosCancel): " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                Toast.makeText(InfoAmigoActivity.this, "No se ha conectado con el servidor (amigosCancel)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void peticionAmistadSend(int id){
        GenericOtherIdRequest request = new GenericOtherIdRequest(id);

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarAmistadSend(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {

                if(response.code() == 200) {
                    cambiarEstadoAEnviado();
                    FragmentAmigos.actualizarLista = true;

                }  else if (response.code() == 409){

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("No sent request")){
                            Toast.makeText(InfoAmigoActivity.this, "No puedes cancelar una petición no enviada", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(InfoAmigoActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(InfoAmigoActivity.this, "Algo ha fallado obteniendo el error (amigosCancel)", Toast.LENGTH_LONG).show();
                    }
                }  else if (response.code() == 500){
                    Toast.makeText(InfoAmigoActivity.this, "Error del server (amigosCancel)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(InfoAmigoActivity.this, "Código de error (amigosCancel): " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                Toast.makeText(InfoAmigoActivity.this, "No se ha conectado con el servidor (amigosCancel)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void peticionAmistadAccept(int id){
        GenericOtherIdRequest request = new GenericOtherIdRequest(id);

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarAmistadAccept(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {

                if(response.code() == 200) {
                    FragmentAmigos.actualizarLista = true;
                    alertDialog.dismiss();
                    abrirMain();

                }  else if (response.code() == 409){

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("No received request")){
                            Toast.makeText(InfoAmigoActivity.this, "No hay solicitud que aceptar", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(InfoAmigoActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(InfoAmigoActivity.this, "Algo ha fallado obteniendo el error (amigosCancel)", Toast.LENGTH_LONG).show();
                    }
                }  else if (response.code() == 500){
                    Toast.makeText(InfoAmigoActivity.this, "Error del server (amigosCancel)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(InfoAmigoActivity.this, "Código de error (amigosCancel): " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                Toast.makeText(InfoAmigoActivity.this, "No se ha conectado con el servidor (amigosCancel)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void peticionAmistadReject(int id){
        GenericOtherIdRequest request = new GenericOtherIdRequest(id);

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarAmistadReject(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {

                if(response.code() == 200) {
                    FragmentAmigos.actualizarLista = true;
                    alertDialog.dismiss();
                    abrirMain();

                }  else if (response.code() == 409){

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("No received request")){
                            Toast.makeText(InfoAmigoActivity.this, "No hay solicitud que rechazar", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(InfoAmigoActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(InfoAmigoActivity.this, "Algo ha fallado obteniendo el error (amigosCancel)", Toast.LENGTH_LONG).show();
                    }
                }  else if (response.code() == 500){
                    Toast.makeText(InfoAmigoActivity.this, "Error del server (amigosCancel)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(InfoAmigoActivity.this, "Código de error (amigosCancel): " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                Toast.makeText(InfoAmigoActivity.this, "No se ha conectado con el servidor (amigosCancel)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void peticionAmistadCancel(int id){
        GenericOtherIdRequest request = new GenericOtherIdRequest(id);

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarAmistadCancel(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {

                if(response.code() == 200) {
                    cambiarEstadoAEliminado();
                    FragmentAmigos.actualizarLista = true;

                }  else if (response.code() == 409){

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("No sent request")){
                            Toast.makeText(InfoAmigoActivity.this, "No puedes cancelar una petición no enviada", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(InfoAmigoActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(InfoAmigoActivity.this, "Algo ha fallado obteniendo el error (amigosCancel)", Toast.LENGTH_LONG).show();
                    }
                }  else if (response.code() == 500){
                    Toast.makeText(InfoAmigoActivity.this, "Error del server (amigosCancel)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(InfoAmigoActivity.this, "Código de error (amigosCancel): " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                Toast.makeText(InfoAmigoActivity.this, "No se ha conectado con el servidor (amigosCancel)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private void mostrarColeccionesAmigo() {
        ColeccionesActivity.username = amigoActual.getUsername();
        ColeccionesActivity.coleccionesList.clear();
        ColeccionesActivity.coleccionesList = amigoActual.getColecciones();

        Intent intent = new Intent(this, ColeccionesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
