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
import com.example.narratives.peticiones.amistad.lista.AmistadListaResponse;
import com.example.narratives.peticiones.amistad.lista.UsuarioEstado;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitudesEnviadasActivity extends AppCompatActivity {
    UsuarioEstadoAdapter usuariosAdapter;
    List<UsuarioEstado> listaEnviados;
    EditText buscador;
    ListView usuarios;

    RetrofitInterface retrofitInterface;
    FloatingActionButton volverAMain;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.amigos_soli_enviadas);
        super.onCreate(savedInstanceState);

        retrofitInterface = ApiClient.getRetrofitInterface();
        usuarios = (ListView) findViewById(R.id.listViewListaSoliEnviadas);
        buscador = (EditText) findViewById(R.id.editTextBuscadorListaSoliEnviadas);

        volverAMain = (FloatingActionButton) findViewById(R.id.botonVolverDesdeSoliEnviadas);
        volverAMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMain();
            }
        });

        usuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                UsuarioEstado usuarioPedido = listaEnviados.get(position);

                MaterialButton boton = (MaterialButton) view.findViewById(R.id.botonCancelarSoli);
                peticionAmistadCancel(usuarioPedido.getId(), boton);
            }
        });

        cargarAdaptador();
    }





    private void cargarAdaptador() {
        listaEnviados = InfoAmigos.getUsuariosEstadoEnviados();
        if(listaEnviados == null){ new ArrayList<UsuarioEstado>();}

        usuariosAdapter = new UsuarioEstadoAdapter(this,R.layout.item_lista_soli_enviadas, listaEnviados);
        usuarios.setAdapter(usuariosAdapter);

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (SolicitudesEnviadasActivity.this).usuariosAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                    Toast.makeText(SolicitudesEnviadasActivity.this, "Solicitud cancelada", Toast.LENGTH_LONG).show();
                    peticionAmistadLista();

                }  else if (response.code() == 409){

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("No sent request")){
                            Toast.makeText(SolicitudesEnviadasActivity.this, "No puedes cancelar una petición no enviada", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(SolicitudesEnviadasActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SolicitudesEnviadasActivity.this, "Algo ha fallado obteniendo el error (amigosCancel)", Toast.LENGTH_LONG).show();
                    }
                }  else if (response.code() == 500){
                    Toast.makeText(SolicitudesEnviadasActivity.this, "Error del server (amigosCancel)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(SolicitudesEnviadasActivity.this, "Código de error (amigosCancel): " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                Toast.makeText(SolicitudesEnviadasActivity.this, "No se ha conectado con el servidor (amigosCancel)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void peticionAmistadLista() {

        Call<AmistadListaResponse> llamada = retrofitInterface.ejecutarAmistadLista(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<AmistadListaResponse>() {
            @Override
            public void onResponse(Call<AmistadListaResponse> call, Response<AmistadListaResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    if(response.body().getUsers() != null){
                        InfoAmigos.setUsuariosEstado(response.body().getUsers());
                    } else {
                        InfoAmigos.setUsuariosEstado(new ArrayList<UsuarioEstado>());
                    }

                    cargarAdaptador();

                } else if(codigo == 500) {
                    Toast.makeText(SolicitudesEnviadasActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(SolicitudesEnviadasActivity.this, "Error desconocido (usersId): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AmistadListaResponse> call, Throwable t) {
                Toast.makeText(SolicitudesEnviadasActivity.this, "No se ha conectado con el servidor (usersId)", Toast.LENGTH_LONG).show();
            }
        });
    }




    private void abrirMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }
}