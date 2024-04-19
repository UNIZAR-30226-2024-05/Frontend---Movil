package com.example.narratives.activities;

import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.Window;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives.databinding.ActivityMainBinding;
import com.example.narratives.resenias.ListAdapter;
import com.example.narratives.resenias.Resenia;

import java.util.ArrayList;

public class ReseniaActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.pantalla_resenias);
        String[] nombres = {"Juan Pérez", "María Gutiérrez", "Carlos Martínez", "Ana López"};
        float[] valoraciones = {4.3F,5.0F,3.8F, 4.1F};
        String[] descripciones = {
                "Una obra maestra de la literatura latinoamericana que narra la historia de la familia Buendía en Macondo.",
                "Un cuento mágico sobre la amistad y la importancia de ver el mundo con los ojos del corazón.",
                "Una distopía que explora temas como el control gubernamental, la vigilancia y la libertad individual.",
                "Una clásica novela romántica que sigue las vidas y amores de las hermanas Bennet en la Inglaterra del siglo XIX."
        };
        ArrayList<Resenia> arrayListReseñas = new ArrayList<>();

        for(int i=0; i<nombres.length;i++){
            Resenia reseña = new Resenia(nombres[i], descripciones[i], valoraciones[i]);
            arrayListReseñas.add(reseña);
        }

        ListView lv = findViewById(R.id.listViewListaReseñas);
        ListAdapter listAdapter = new ListAdapter(ReseniaActivity.this,arrayListReseñas);
        lv.setAdapter(listAdapter);
       // lv.setClickable(true);
       /* lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }
    ///RECOPILAR DATOS DE LA BASE DE DATOS------------------------------------------
    //-----------------------------------


}
