package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives.informacion.InfoAmigos;
import com.example.narratives.peticiones.users.perfiles.UserResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;


public class InfoAmigoActivity extends AppCompatActivity {

    private UserResponse amigoActual;

    FloatingActionButton fabAtras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.activity_info_amigo);
        super.onCreate(savedInstanceState);

        amigoActual = InfoAmigos.getAmigoActual();

        TextView titulo = (TextView) findViewById(R.id.textViewNombreInfoAmigo);
        titulo.setText(amigoActual.getUsername());

        ShapeableImageView fotoPerfil = (ShapeableImageView) findViewById(R.id.imageViewFotoInfoAmigo);
        fotoPerfil.setImageResource(InfoAmigos.getImageResourceFromImgCode(amigoActual.getImg()));

        if(amigoActual.getUltimo() != null){
            ShapeableImageView portada = (ShapeableImageView) findViewById(R.id.imageViewPortadaInfoAmigo);
            Glide
                    .with(this)
                    .load(amigoActual.getUltimo().getImg())
                    .centerCrop()
                    .placeholder(R.drawable.icono_imagen_estandar_foreground)
                    .into(portada);
        }

        TextView estado = (TextView) findViewById(R.id.textViewEstadoInfoAmigo);
        estado.setText(InfoAmigos.getEstadoStringFromId(amigoActual.getEstado()));

        fabAtras = (FloatingActionButton) findViewById(R.id.botonVolverDesdeInfoAmigo);
        fabAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMain();
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
