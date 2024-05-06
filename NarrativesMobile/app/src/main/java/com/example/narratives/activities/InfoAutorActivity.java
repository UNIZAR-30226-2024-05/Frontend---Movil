package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.BibliotecaGridAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives.adaptadores.BibliotecaGridAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;
import com.example.narratives.peticiones.autores.AutorDatos;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoAutorActivity extends AppCompatActivity {

    public static AutorDatos autorActual;
    GridView gridView;
    BibliotecaGridAdapter bibliotecaGridAdapter;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        retrofit = ApiClient.getRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.activity_info_autor);
        super.onCreate(savedInstanceState);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        gridView = (GridView) findViewById(R.id.gridViewBibliotecaAutor);

        inicializarAdaptadorBiblioteca(InfoAudiolibros.getAudiolibrosPorAutor(autorActual.getNombre()));



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                peticionAudiolibrosId(position, id);
            }
        });


        TextView textViewNombre = findViewById(R.id.textViewNombreAutor);
        textViewNombre.setText(autorActual.getNombre());

        TextView textViewCiudad= findViewById(R.id.texCiudadNacimiento);
        textViewCiudad.setText(autorActual.getCiudad());

        TextView textViewGenero = findViewById(R.id.textViewGeneroMasEscrito);
        textViewGenero.setText(autorActual.getGenero());

        /* Buscar como pasar el entero para que pinte las estrellas. Sino pondre el entero sin mas
        TextView textViewPuntuacion = findViewById(R.id.linearLayoutValoracionMedia);
        textViewPuntuacion.setText(autorActual.getAutor().getPuntuacionMedia()); */


        FloatingActionButton botonCerrar = (FloatingActionButton) findViewById(R.id.botonVolverDesdeInfoLibro);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Volver al audiolibro
            }
        });

    }
    private void inicializarAdaptadorBiblioteca(ArrayList<AudiolibroItem> libros){
        bibliotecaGridAdapter = new BibliotecaGridAdapter(this, libros);

        gridView.setAdapter(bibliotecaGridAdapter);

    }

    private void peticionAudiolibrosId(int position, long idGrid){
        AudiolibroItem audiolibro = (AudiolibroItem) bibliotecaGridAdapter.getItem(position);

        Call<AudiolibroEspecificoResponse> llamada = retrofitInterface.ejecutarAudiolibrosId(ApiClient.getUserCookie(), audiolibro.getId());
        llamada.enqueue(new Callback<AudiolibroEspecificoResponse>() {
            @Override
            public void onResponse(Call<AudiolibroEspecificoResponse> call, Response<AudiolibroEspecificoResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoLibroActivity.audiolibroActual = response.body();
                    abrirInfoLibro();

                } else if(codigo == 409) {
                    Toast.makeText(InfoAutorActivity.this, "No hay ningún audiolibro con ese ID", Toast.LENGTH_LONG).show();

                } else if(codigo == 500) {
                    Toast.makeText(InfoAutorActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(InfoAutorActivity.this, "Error desconocido (AudiolibrosId): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AudiolibroEspecificoResponse> call, Throwable t) {
                Toast.makeText(InfoAutorActivity.this, "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirInfoLibro() {
        Intent intent = new Intent(InfoAutorActivity.this, InfoLibroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}



