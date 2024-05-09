package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.UsuarioEstadoAdapter;
import com.example.narratives.informacion.InfoAmigos;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.GenericOtherIdRequest;
import com.example.narratives.peticiones.amistad.lista.UsuarioEstado;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnadirAmigoActivity extends AppCompatActivity {
    UsuarioEstadoAdapter usuariosAdapter;
    List<UsuarioEstado> listaNoAmigos;
    EditText buscador;
    ListView usuarios;

    RetrofitInterface retrofitInterface;
    FloatingActionButton volverAMain;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.amigos_anadir);
        super.onCreate(savedInstanceState);

        retrofitInterface = ApiClient.getRetrofitInterface();
        usuarios = (ListView) findViewById(R.id.listViewListaAnadirAmigo);
        buscador = (EditText) findViewById(R.id.editTextBuscadorListaAnadirAmigos);

        volverAMain = (FloatingActionButton) findViewById(R.id.botonVolverDesdeAnadirAmigo);
        volverAMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMain();
            }
        });

        usuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                UsuarioEstado usuarioPedido = listaNoAmigos.get(position);

                MaterialButton boton = (MaterialButton) view.findViewById(R.id.botonAnadirAmigo);
                if(boton.getText().equals("Enviar solicitud")){
                    Toast.makeText(AnadirAmigoActivity.this, "Enviar solicitud", Toast.LENGTH_LONG).show();
                    peticionAmistadSend(usuarioPedido.getId(), boton);
                } else if(boton.getText().equals("Cancelar solicitud")){
                    Toast.makeText(AnadirAmigoActivity.this, "Cancelar solicitud", Toast.LENGTH_LONG).show();
                    peticionAmistadCancel(usuarioPedido.getId(), boton);
                }
            }
        });

        cargarAdaptador();
    }




    private void cargarAdaptador() {
        listaNoAmigos = InfoAmigos.getUsuariosEstadoNoAmigos();
        if(listaNoAmigos == null){ new ArrayList<UsuarioEstado>();}
        usuariosAdapter = new UsuarioEstadoAdapter(this,R.layout.item_lista_anadir_amigos , listaNoAmigos);
        usuarios.setAdapter(usuariosAdapter);

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (AnadirAmigoActivity.this).usuariosAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    private void abrirMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }


    private void cambiarEntreEnviarYCancelarSolicitud(MaterialButton boton){

        if(boton.getText().equals("Enviar solicitud")){ // hay que pasar a cancelar solicitud
            boton.setBackgroundResource(R.color.teal_300);
            boton.setStrokeWidth(0);
            boton.setStrokeColorResource(R.color.white);
            boton.setText("Enviar solicitud");
            boton.setTextColor(getResources().getColor(R.color.white));

        } else {    // hay que pasar a enviar solicitud
            boton.setBackgroundResource(R.color.white);
            boton.setStrokeWidth(2);
            boton.setStrokeColorResource(R.color.teal_300);
            boton.setText("Cancelar solicitud");
            boton.setTextColor(getResources().getColor(R.color.teal_300));
        }
    }

    private void cambiarACancelarSolicitud(MaterialButton boton){
        boton.setElevation(1);
        boton.setBackgroundResource(R.color.teal_300);
        boton.setStrokeWidth(0);
        boton.setStrokeColorResource(R.color.white);
        boton.setText("Enviar solicitud");
        boton.setTextColor(getResources().getColor(R.color.white));
        boton.setClickable(false);
    }

    private void cambiarAEnviarSolicitud(MaterialButton boton){
        boton.setElevation(1);
        boton.setBackgroundResource(R.color.white);
        boton.setStrokeWidth(2);
        boton.setStrokeColorResource(R.color.teal_300);
        boton.setText("Cancelar solicitud");
        boton.setTextColor(getResources().getColor(R.color.teal_300));
        boton.setClickable(false);
    }

    private void cambiarASolicitudRecibida(MaterialButton boton){
        boton.setElevation(1);
        boton.setBackgroundResource(R.color.white);
        boton.setStrokeWidth(0);
        boton.setElevation((float) 0.01);
        boton.setStrokeColorResource(R.color.white);
        boton.setText("Solicitud recibida");
        boton.setTextColor(getResources().getColor(R.color.gris_claro));
        boton.setClickable(false);
    }

    private void peticionAmistadSend(int id, MaterialButton boton){
        GenericOtherIdRequest request = new GenericOtherIdRequest(id);

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarAmistadSend(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {

                if(response.code() == 200) {
                    Toast.makeText(AnadirAmigoActivity.this, "Solicitud enviada", Toast.LENGTH_LONG).show();
                    cambiarACancelarSolicitud(boton);

                }  else if (response.code() == 409){

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("Can't send itself")){
                            Toast.makeText(AnadirAmigoActivity.this, "No puedes enviarte una solicitud a tí mismo", Toast.LENGTH_LONG).show();

                        } else if(error.equals("Already friends")) {
                            Toast.makeText(AnadirAmigoActivity.this, "Ya sois amigos", Toast.LENGTH_LONG).show();

                        } else if(error.equals("Already sent request")) {
                            Toast.makeText(AnadirAmigoActivity.this, "Ya has enviado una solicitud a este usuario", Toast.LENGTH_LONG).show();

                        } else if(error.equals("Already friends")) {
                            Toast.makeText(AnadirAmigoActivity.this, "Tienes una solicitud recibida de este usuario", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(AnadirAmigoActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(AnadirAmigoActivity.this, "Algo ha fallado obteniendo el error (amigosSend)", Toast.LENGTH_LONG).show();
                    }
                }  else if (response.code() == 500){
                    Toast.makeText(AnadirAmigoActivity.this, "Error del server (amigosSend)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(AnadirAmigoActivity.this, "Código de error (amigosSend): " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                Toast.makeText(AnadirAmigoActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void peticionAmistadCancel(int id, MaterialButton boton){
        GenericOtherIdRequest request = new GenericOtherIdRequest(id);

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarAmistadCancel(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {

                if(response.code() == 200) {
                    Toast.makeText(AnadirAmigoActivity.this, "Solicitud cancelada", Toast.LENGTH_LONG).show();
                    cambiarAEnviarSolicitud(boton);

                }  else if (response.code() == 409){

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("No sent request")){
                            Toast.makeText(AnadirAmigoActivity.this, "No puedes cancelar una petición no enviada", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(AnadirAmigoActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(AnadirAmigoActivity.this, "Algo ha fallado obteniendo el error (amigosCancel)", Toast.LENGTH_LONG).show();
                    }
                }  else if (response.code() == 500){
                    Toast.makeText(AnadirAmigoActivity.this, "Error del server (amigosCancel)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(AnadirAmigoActivity.this, "Código de error (amigosCancel): " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                Toast.makeText(AnadirAmigoActivity.this, "No se ha conectado con el servidor (amigosCancel)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}