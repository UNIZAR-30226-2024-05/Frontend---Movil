package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.especifico.Genero;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class InfoLibroActivity extends AppCompatActivity {

    public AudiolibroEspecificoResponse audiolibroActual;

    MaterialButton escucharAudiolibro;
    MaterialButton comprarAudiolibro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.activity_info_libro);
        super.onCreate(savedInstanceState);

        audiolibroActual = InfoAudiolibros.getAudiolibroActual();

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

        TextView textViewGeneros = findViewById(R.id.textViewGeneroInfoLibro);
        textViewGeneros.setText(getFormattedGenres(audiolibroActual.getGeneros()));


        FloatingActionButton botonCerrar = (FloatingActionButton) findViewById(R.id.botonVolverDesdeInfoUsuario);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMenuMain();
            }
        });

        escucharAudiolibro = (MaterialButton) findViewById(R.id.botonEscucharAudiolibroInfoLibro);
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

        comprarAudiolibro = (MaterialButton) findViewById(R.id.botonVerResenasInfoUsuario);
        comprarAudiolibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAmazonConLink();
            }
        });


    }

    private void abrirAmazonConLink() {
        String urlBusquedaConTitulo = "https://www.amazon.es/s?k=libro+" +
                                        audiolibroActual.getAudiolibro().getTitulo().replace(" ","+")
                                        + "+" + audiolibroActual.getAutor().getNombre().replace(" ","+");

        Uri uri = Uri.parse(urlBusquedaConTitulo); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void cargarYAbrirReproductor(){
        MainActivity.fragmentoEscuchandoAbierto.inicializarLibro(audiolibroActual);
        MainActivity.abrirEscuchando = true;
        abrirMenuMain();
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

    private void abrirMenuMain() {
        InfoAudiolibros.setAudiolibroActual(null);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }
}
