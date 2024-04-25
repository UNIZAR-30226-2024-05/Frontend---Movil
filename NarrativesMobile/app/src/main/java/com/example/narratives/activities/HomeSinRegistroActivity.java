package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.menuprincipal.adaptador;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;

public class HomeSinRegistroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.homesinregistro);
        super.onCreate(savedInstanceState);

        obtenerAudiolibrosEjemplo();
        rv = findViewById(R.id.RecyclerViewRecomendados);
        rv.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        adaptador = new adaptador(HomeSinRegistroActivity.this, audiolibros);
        rv.setAdapter(adaptador);

        rvTerror = findViewById(R.id.RecyclerViewTerror1);
        rvTerror.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvTerror.setAdapter(adaptador);

        rvFantasia = findViewById(R.id.RecyclerFantasia1);
        rvFantasia.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvFantasia.setAdapter(adaptador);

        rvMitologia = findViewById(R.id.RecyclerMitologia1);
        rvMitologia.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvMitologia.setAdapter(adaptador);

        rvPoesia = findViewById(R.id.RecyclerPoesia1);
        rvPoesia.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvPoesia.setAdapter(adaptador);

        rvNovela = findViewById(R.id.RecyclerNovela1);
        rvNovela.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvNovela.setAdapter(adaptador);

        findViewById(R.id.botonIrLoginDesdeInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMenuLogin();
            }
        });

        findViewById(R.id.botonIrRegistroDesdeInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMenuRegistro();
            }
        });

    }

    public void abrirMenuRegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void abrirMenuLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
    RecyclerView rv, rvTerror, rvFantasia, rvMitologia, rvNovela, rvPoesia;
    com.example.narratives.menuprincipal.adaptador adaptador;
    private ArrayList<AudiolibroItem> audiolibros;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }


    private void obtenerAudiolibrosEjemplo(){
        String[] portadas = {
                "https://narrativesarchivos.blob.core.windows.net/imagenes/ElCoco.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/ElHombreDelTrajeNegro.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/ElUmbralDeLaNoche.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/HarryPotter1.jpeg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/LaOdisea.png",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/OrgulloYPrejuicio.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/Popsy.jpg"
        };

        String[] titulos = {
                "El coco",
                "El hombre de traje negro",
                "El umbral de la noche",
                "Harry Potter y la piedra filosofal",
                "La odisea",
                "Orgullo y prejuicio",
                "Popsy"
        };

        audiolibros = new ArrayList<>();
        for(int i = 0; i < titulos.length; i++){
            AudiolibroItem a = new AudiolibroItem(i, titulos[i], i, "descripcion", portadas[i]);
            audiolibros.add(a);
        }

        InfoAudiolibros.setTodosLosAudiolibros(audiolibros);
    }
}


