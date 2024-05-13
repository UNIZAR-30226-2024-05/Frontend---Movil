package com.example.narratives.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.AnadirAmigoActivity;
import com.example.narratives.activities.InfoAmigoActivity;
import com.example.narratives.activities.SolicitudesEnviadasActivity;
import com.example.narratives.activities.SolicitudesHistorialActivity;
import com.example.narratives.activities.SolicitudesRecibidasActivity;
import com.example.narratives.adaptadores.AmigosAdapter;
import com.example.narratives.informacion.InfoAmigos;
import com.example.narratives.peticiones.amistad.amigos.AmigoSimple;
import com.example.narratives.peticiones.amistad.amigos.AmigosResponse;
import com.example.narratives.peticiones.amistad.lista.AmistadListaResponse;
import com.example.narratives.peticiones.amistad.lista.UsuarioEstado;
import com.example.narratives.peticiones.users.perfiles.UserResponse;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentAmigos extends Fragment {

    public static boolean actualizarLista;
    static ListView listaAmigos;
    static AmigosAdapter amigosAdapter;

    static EditText buscador;

    static ArrayList<AmigoSimple> amigos;

    RetrofitInterface retrofitInterface;

    FloatingActionButton fabAcciones;
    ExtendedFloatingActionButton fabAnadirAmigo;
    ExtendedFloatingActionButton fabEnviadas;
    ExtendedFloatingActionButton fabRecibidas;
    ExtendedFloatingActionButton fabHistorial;

    boolean fabsVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amigos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        actualizarLista = false;
        retrofitInterface = ApiClient.getRetrofitInterface();
        listaAmigos = (ListView) getView().findViewById(R.id.listViewListaAmigos);
        buscador = (EditText) getView().findViewById(R.id.editTextBuscadorListaAmigos);
        fabAcciones = (FloatingActionButton) getView().findViewById(R.id.fabAcciones);
        fabAnadirAmigo = (ExtendedFloatingActionButton) getView().findViewById(R.id.fabAnadirAmigo);
        fabEnviadas = (ExtendedFloatingActionButton) getView().findViewById(R.id.fabSolicitudesEnviadas);
        fabRecibidas = (ExtendedFloatingActionButton) getView().findViewById(R.id.fabSolicitudesRecibidas);
        fabHistorial = (ExtendedFloatingActionButton) getView().findViewById(R.id.fabHistorialPeticiones);

        esconderFabs();

        listaAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                peticionUsersId(position);
            }
        });

        fabAcciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabsVisible){
                    esconderFabs();
                } else {
                    mostrarFabs();
                }
            }
        });

        fabAnadirAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividad(new Intent(getContext(), AnadirAmigoActivity.class));
            }
        });

        fabEnviadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividad(new Intent(getContext(), SolicitudesEnviadasActivity.class));
            }
        });

        fabRecibidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividad(new Intent(getContext(), SolicitudesRecibidasActivity.class));
            }
        });

        fabHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividad(new Intent(getContext(), SolicitudesHistorialActivity.class));
            }
        });


        peticionAmigos();
        peticionAmistadLista();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(actualizarLista) {
            peticionAmigos();
            peticionAmistadLista();
            actualizarLista = false;
        } // MainActivity.fragmentoAmigosAbierto.actualizarLista = true; para activarlo

    }



    private void peticionUsersId(int position) {
        AmigoSimple amigo = (AmigoSimple) amigosAdapter.getItem(position);

        Call<UserResponse> llamada = retrofitInterface.ejecutarUsersId(ApiClient.getUserCookie(), amigo.getId());
        llamada.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoAmigos.setAmigoActual(response.body());
                    abrirActividad(new Intent(getContext(), InfoAmigoActivity.class));

                } else if(codigo == 409) {
                    Toast.makeText(getContext(), "No hay ningún usuario con ese ID", Toast.LENGTH_LONG).show();

                } else if(codigo == 500) {
                    Toast.makeText(getContext(), "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Error desconocido (usersId): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor (usersId)", Toast.LENGTH_LONG).show();
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

                    //TODO: quitar ejemplo
                    if(response.body().getAmigos() == null){
                        Toast.makeText(getContext(), "Amigos null", Toast.LENGTH_LONG).show();

                        ArrayList<AmigoSimple> amigosEjemplo = new ArrayList<AmigoSimple>();
                        amigosEjemplo.add(new AmigoSimple(1, "Buri", 6));
                        amigosEjemplo.add(new AmigoSimple(2, "Jaume", 7));
                        amigosEjemplo.add(new AmigoSimple(3, "Manu", 5));
                        amigosEjemplo.add(new AmigoSimple(4, "Marti", 1));

                        InfoAmigos.setAmigos(amigosEjemplo);
                    } else {
                        InfoAmigos.setAmigos(response.body().getAmigos());
                    }

                    cargarAdaptador();

                } else if (codigo == 500){
                    Toast.makeText(getContext(), "Error del servidor",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error desconocido (amigos): " + String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AmigosResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor (amigos)",
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
                        Toast.makeText(getContext(), "UsuariosEstado null", Toast.LENGTH_LONG).show();
                        InfoAmigos.setUsuariosEstado(new ArrayList<UsuarioEstado>());
                    }

                } else if(codigo == 500) {
                    Toast.makeText(getContext(), "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Error desconocido (usersId): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AmistadListaResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor (usersId)", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void cargarAdaptador() {
        amigos = InfoAmigos.getAmigos();
        amigosAdapter = new AmigosAdapter(getContext(), R.layout.item_lista_amigos, amigos);
        listaAmigos.setAdapter(amigosAdapter);

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                amigosAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void mostrarFabs() {
        fabAnadirAmigo.setVisibility(View.VISIBLE);
        fabEnviadas.setVisibility(View.VISIBLE);
        fabRecibidas.setVisibility(View.VISIBLE);
        fabHistorial.setVisibility(View.VISIBLE);
        fabsVisible = true;
    }

    private void esconderFabs() {
        fabAnadirAmigo.setVisibility(View.GONE);
        fabEnviadas.setVisibility(View.GONE);
        fabRecibidas.setVisibility(View.GONE);
        fabHistorial.setVisibility(View.GONE);
        fabsVisible = false;
    }

    private void abrirActividad(Intent intent) {
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }

}