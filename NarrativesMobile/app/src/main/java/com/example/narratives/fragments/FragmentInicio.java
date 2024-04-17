package com.example.narratives.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.Audiolibro;
import com.example.narratives.menuprincipal.adaptador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentInicio extends Fragment {
    RecyclerView rv, rvTerror, rvFantasia, rvMitologia, rvNovela, rvPoesia;
    adaptador adaptador;
    private ArrayList<Audiolibro> audiolibros;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        obtenerAudiolibrosEjemplo();
        rv = getView().findViewById(R.id.RecyclerViewSeguirEscuchando);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adaptador = new adaptador(getContext(), audiolibros);
        rv.setAdapter(adaptador);


        rvTerror = getView().findViewById(R.id.RecyclerViewTerror);
        rvTerror.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvTerror.setAdapter(adaptador);

        rvFantasia = getView().findViewById(R.id.RecyclerFantasia);
        rvFantasia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFantasia.setAdapter(adaptador);

        rvMitologia = getView().findViewById(R.id.RecyclerMitologia);
        rvMitologia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvMitologia.setAdapter(adaptador);

        rvPoesia = getView().findViewById(R.id.RecyclerPoesia);
        rvPoesia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvPoesia.setAdapter(adaptador);

        rvNovela = getView().findViewById(R.id.RecyclerNovela);
        rvNovela.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvNovela.setAdapter(adaptador);

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
            Audiolibro a = new Audiolibro(i, titulos[i], i, "descripcion", portadas[i]);
            audiolibros.add(a);
        }

        InfoAudiolibros.setTodosLosAudiolibros(audiolibros);
    }



}



