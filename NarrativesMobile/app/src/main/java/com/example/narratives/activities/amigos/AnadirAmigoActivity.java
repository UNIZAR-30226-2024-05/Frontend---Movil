package com.example.narratives.activities.amigos;

import android.annotation.SuppressLint;
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
import com.example.narratives.activities.info.InfoAmigoActivity;
import com.example.narratives.activities.main.MainActivity;
import com.example.narratives.adaptadores.UsuarioEstadoAnadirAdapter;
import com.example.narratives.informacion.InfoAmigos;
import com.example.narratives.peticiones.amistad.amigos.AmigosResponse;
import com.example.narratives.peticiones.amistad.lista.AmistadListaResponse;
import com.example.narratives.peticiones.amistad.lista.UsuarioEstado;
import com.example.narratives.peticiones.users.perfiles.UserResponse;
import com.example.narratives.sockets.SocketManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnadirAmigoActivity extends AppCompatActivity {
    UsuarioEstadoAnadirAdapter usuariosAdapter;
    List<UsuarioEstado> listaNoAmigos;
    EditText buscador;
    ListView usuarios;

    RetrofitInterface retrofitInterface;
    FloatingActionButton volverAMain;
    private Socket mSocket = SocketManager.getInstance();


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
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                peticionUsersId(pos);
            }
        });


        mSocket.on("peticionReceived", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args.length > 0 && args[0] instanceof JSONObject) {

                    AnadirAmigoActivity.this.runOnUiThread(new Runnable() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void run() {
                            peticionAmigos();
                            peticionAmistadLista();

                        }
                    });
                }
            }
        });

        mSocket.on("peticionAccepted", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args.length > 0 && args[0] instanceof JSONObject) {

                    AnadirAmigoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            peticionAmigos();
                            peticionAmistadLista();
                        }
                    });
                }
            }
        });

        mSocket.on("peticionRejected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args.length > 0 && args[0] instanceof JSONObject) {

                    AnadirAmigoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            peticionAmigos();
                            peticionAmistadLista();
                        }
                    });
                }
            }
        });

        mSocket.on("peticionCancelled", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args.length > 0 && args[0] instanceof JSONObject) {

                    AnadirAmigoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            peticionAmigos();
                            peticionAmistadLista();
                        }
                    });
                }
            }
        });

        mSocket.on("friendshipRemoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args.length > 0 && args[0] instanceof JSONObject) {

                    AnadirAmigoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            peticionAmigos();
                            peticionAmistadLista();
                        }
                    });
                }
            }
        });

        cargarAdaptador();
    }


    private void cargarAdaptador() {
        listaNoAmigos = InfoAmigos.getUsuariosEstadoNoAmigos();
        if(listaNoAmigos == null){ new ArrayList<UsuarioEstado>();}
        usuariosAdapter = new UsuarioEstadoAnadirAdapter(this,R.layout.item_lista_anadir_amigos , listaNoAmigos);
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


    private void peticionUsersId(int position) {
        UsuarioEstado usuario = (UsuarioEstado) usuariosAdapter.getItem(position);

        Call<UserResponse> llamada = retrofitInterface.ejecutarUsersId(ApiClient.getUserCookie(), usuario.getId());
        llamada.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoAmigos.setAmigoActual(response.body());
                    abrirActividad(new Intent(AnadirAmigoActivity.this, InfoAmigoActivity.class));

                } else if(codigo == 409) {
                    Toast.makeText(AnadirAmigoActivity.this, "No hay ningún usuario con ese ID", Toast.LENGTH_LONG).show();

                } else if(codigo == 500) {
                    Toast.makeText(AnadirAmigoActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(AnadirAmigoActivity.this, "Error desconocido (usersId): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(AnadirAmigoActivity.this, "No se ha conectado con el servidor (usersId)", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void peticionAmigos() {
        Call<AmigosResponse> llamada = retrofitInterface.ejecutarAmistadAmigos(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<AmigosResponse>() {
            @Override
            public void onResponse(Call<AmigosResponse> call, Response<AmigosResponse> response) {
                int codigo = response.code();

                if (codigo == 200){

                    if(response.body().getAmigos() == null){
                        Toast.makeText(AnadirAmigoActivity.this, "Amigos null", Toast.LENGTH_LONG).show();


                        InfoAmigos.setAmigos(new ArrayList<>());
                    } else {
                        InfoAmigos.setAmigos(response.body().getAmigos());
                    }


                } else if (codigo == 500){
                    Toast.makeText(AnadirAmigoActivity.this, "Error del servidor",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(AnadirAmigoActivity.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(AnadirAmigoActivity.this, "Error desconocido (amigos): " + String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AmigosResponse> call, Throwable t) {
                Toast.makeText(AnadirAmigoActivity.this, "No se ha conectado con el servidor (amigos)",
                        Toast.LENGTH_LONG).show();

                //Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(AnadirAmigoActivity.this, "UsuariosEstado null", Toast.LENGTH_LONG).show();
                        InfoAmigos.setUsuariosEstado(new ArrayList<UsuarioEstado>());
                    }
                    cargarAdaptador();


                } else if(codigo == 500) {
                    Toast.makeText(AnadirAmigoActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(AnadirAmigoActivity.this, "Error desconocido (usersId): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AmistadListaResponse> call, Throwable t) {
                Toast.makeText(AnadirAmigoActivity.this, "No se ha conectado con el servidor (usersId)", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void abrirMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private void abrirActividad(Intent intent) {
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}

