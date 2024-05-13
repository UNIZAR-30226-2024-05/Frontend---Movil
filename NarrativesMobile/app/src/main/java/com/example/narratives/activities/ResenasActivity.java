package com.example.narratives.activities;

import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives.adaptadores.ResenasAdapter;
import com.example.narratives.peticiones.audiolibros.especifico.GenericReview;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ResenasActivity extends AppCompatActivity {
    ArrayList<GenericReview> resenasPublicasList = InfoLibroActivity.audiolibroActual.getPublicReviews();
    ArrayList<GenericReview> resenasAmigosList = InfoLibroActivity.audiolibroActual.getFriendsReviews();
    private PopupWindow popupWindow;
    private boolean fabs_visible;

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

        FloatingActionButton fabMenuResenas = findViewById(R.id.fabMenuResenas);
        ExtendedFloatingActionButton fabNuevaResena = findViewById(R.id.fabNuevaResena);
        ExtendedFloatingActionButton fabMiResena = findViewById(R.id.fabMiResena);

        fabMenuResenas.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fabs_visible) {
                        fabs_visible = false;
                        fabNuevaResena.setVisibility(View.GONE);
                        fabMiResena.setVisibility(View.GONE);
                    } else {
                        fabs_visible = true;
                        fabNuevaResena.setVisibility(View.VISIBLE);
                        fabMiResena.setVisibility(View.VISIBLE);
                    }
                }
            }
        );

        fabNuevaResena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupNuevaResena();
            }
        });
    }

    private void mostrarPopupNuevaResena() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewMiResena = inflater.inflate(R.layout.popup_mi_resena, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        popupWindow = new PopupWindow(viewMiResena, width, height, true);
        popupWindow.setAnimationStyle(1);

        FrameLayout layout = findViewById(R.id.main_layout);
        layout.post(new Runnable(){
            @Override
            public void run(){
                popupWindow.showAtLocation(layout, Gravity.BOTTOM,0,0);
            }
        });

        FloatingActionButton botonCerrar = viewMiResena.findViewById(R.id.botonCerrarMiResena);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        Button botonConfirmarMiResena = viewMiResena.findViewById(R.id.botonConfirmarMiResena);
        botonConfirmarMiResena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //publicarResena();
                popupWindow.dismiss();
            }
        });
    }
}
