package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.especifico.Genero;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;
import com.example.narratives.peticiones.autores.AutorDatosResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoLibroActivity extends AppCompatActivity {

    public static AudiolibroEspecificoResponse audiolibroActual;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        retrofit = ApiClient.getRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.activity_info_libro);
        super.onCreate(savedInstanceState);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;




        ImageView imageViewPortada = findViewById(R.id.imageViewPortadaInfoLibro);

        Glide
                .with(this)
                .load(audiolibroActual.getAudiolibro().getImg())
                .centerCrop()
                .placeholder(R.drawable.icono_imagen_estandar_foreground)
                .into(imageViewPortada);

        TextView textViewTitulo = findViewById(R.id.textViewTituloInfoLibro);
        textViewTitulo.setText(audiolibroActual.getAudiolibro().getTitulo());

        TextView textViewDescripcion = findViewById(R.id.textViewDescripcionInfoLibro);
        textViewDescripcion.setText(audiolibroActual.getAudiolibro().getDescripcion());

        TextView textViewAutor = findViewById(R.id.textViewNombreAutorInfoLibro);
        textViewAutor.setText(audiolibroActual.getAutor().getNombre());
        textViewAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            //Ya lo cambiare a petición pero para probar que va a la pagina
            public void onClick(View view) {
                //abrirPaginaAutor();
                peticionAudiolibrosId();
            }
        });

        TextView textViewGeneros = findViewById(R.id.textViewGeneroInfoLibro);
        textViewGeneros.setText(getFormattedGenres(audiolibroActual.getGeneros()));


        FloatingActionButton botonCerrar = (FloatingActionButton) findViewById(R.id.botonVolverDesdeInfoLibro);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMenuMain();
            }
        });

        MaterialButton escucharAudiolibro = (MaterialButton) findViewById(R.id.botonEscucharAudiolibroInfoLibro);
        escucharAudiolibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarYAbrirReproductor();
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoLibroActivity.this);
                //builder.setTitle("Dirígete al REPRODUCTOR...");
                builder.setMessage("El libro estará disponible en cuanto termine la carga.");
                builder.setIcon(R.drawable.icono_auriculares_pequeno);

                builder.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        abrirMenuMain();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                */
            }
        });
    }

    private void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void cargarYAbrirReproductor(){
        MainActivity.fragmentoEscuchandoAbierto.inicializarLibro(audiolibroActual);
        MainActivity.abrirEscuchando = true;
        abrirMenuMain();
    }

    public void abrirPaginaAutor() {
        Intent intent = new Intent(this, InfoAutorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private String getFormattedGenres(ArrayList<Genero> generos){
        String result = "";

        for(int i = 0; i < generos.size(); i++){
            Genero genero = generos.get(i);

            result += genero.getNombre();

            if(i != (generos.size() - 1)){
                result += ", ";
            }
        }

        return result;
    }

    private void peticionAudiolibrosId(){

        Call<AutorDatosResponse> llamada = retrofitInterface.ejecutarAutoresId(ApiClient.getUserCookie(), audiolibroActual.getAutor().getId());
        llamada.enqueue(new Callback<AutorDatosResponse>() {
            @Override
            public void onResponse(Call<AutorDatosResponse> call, Response<AutorDatosResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoAutorActivity.autorActual = response.body();
                    abrirPaginaAutor();

                } else if(codigo == 409) {
                    Toast.makeText(InfoLibroActivity.this, "No hay ningún audiolibro con ese ID", Toast.LENGTH_LONG).show();

                } else if(codigo == 500) {
                    Toast.makeText(InfoLibroActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(InfoLibroActivity.this, "Error desconocido (AudiolibrosId): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AutorDatosResponse> call, Throwable t) {
                Toast.makeText(InfoLibroActivity.this, "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }
}
