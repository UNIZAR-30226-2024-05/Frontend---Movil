package com.example.narratives.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives.adaptadores.AudiolibrosColeccionAdapter;
import com.example.narratives.adaptadores.ColeccionesAdapter;
import com.example.narratives.adaptadores.ResenasAdapter;
import com.example.narratives.informacion.InfoMiPerfil;
import com.example.narratives.peticiones.audiolibros.especifico.GenericReview;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ResenasActivity extends AppCompatActivity {
    ArrayList<GenericReview> resenasPublicasList = InfoLibroActivity.audiolibroActual.getPublicReviews();
    ArrayList<GenericReview> resenasAmigosList = InfoLibroActivity.audiolibroActual.getFriendsReviews();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resenas);

        TextView textViewTituloLibroResenas = findViewById(R.id.textViewTituloLibroResenas);
        textViewTituloLibroResenas.setText(InfoLibroActivity.audiolibroActual.getAudiolibro().getTitulo());

        TextView textViewNingunaResena = findViewById(R.id.textViewNingunaResena);
        if (resenasPublicasList == null || resenasPublicasList.isEmpty()) {
            textViewNingunaResena.setVisibility(View.VISIBLE);
        } else {
            textViewNingunaResena.setVisibility(View.GONE);
        }

        ResenasAdapter resenasAdapter = new ResenasAdapter(this, R.layout.item_lista_resenas, resenasPublicasList);

        ListView listViewListaResenas = findViewById(R.id.listViewListaResenas);
        listViewListaResenas.setAdapter(resenasAdapter);

        Switch switchVisibilidad = findViewById(R.id.switchVisibilidad);
        switchVisibilidad.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                final ResenasAdapter adapterAmigos = new ResenasAdapter(ResenasActivity.this, R.layout.item_lista_resenas, resenasAmigosList);
                listViewListaResenas.setAdapter(adapterAmigos);

                if (resenasAmigosList == null || resenasAmigosList.isEmpty()) {
                    textViewNingunaResena.setVisibility(View.VISIBLE);
                } else {
                    textViewNingunaResena.setVisibility(View.GONE);
                }
            } else {
                final ResenasAdapter adapterPublicas = new ResenasAdapter(ResenasActivity.this, R.layout.item_lista_resenas, resenasPublicasList);
                listViewListaResenas.setAdapter(adapterPublicas);

                if (resenasPublicasList == null || resenasPublicasList.isEmpty()) {
                    textViewNingunaResena.setVisibility(View.VISIBLE);
                } else {
                    textViewNingunaResena.setVisibility(View.GONE);
                }
            }
        });

        FloatingActionButton botonAnadir = findViewById(R.id.fabNuevaResena);
    }
}
