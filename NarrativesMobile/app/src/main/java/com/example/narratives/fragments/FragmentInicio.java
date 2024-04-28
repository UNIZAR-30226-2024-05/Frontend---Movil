package com.example.narratives.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.adaptadores.MenuInicioAdapter;

public class FragmentInicio extends Fragment {
    RecyclerView rvSeguirEscuchando, rvGenero1, rvGenero2, rvGenero3, rvGenero4, rvGenero5;
    MenuInicioAdapter adapterRecomendados, adapter1, adapter2, adapter3, adapter4, adapter5;

    TextView textViewGenero1, textViewGenero2, textViewGenero3, textViewGenero4, textViewGenero5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        rvSeguirEscuchando = getView().findViewById(R.id.recyclerViewSeguirEscuchando);

        textViewGenero1 = getView().findViewById(R.id.textViewGenero1);
        rvGenero1 = getView().findViewById(R.id.recyclerViewGenero1);

        textViewGenero2 = getView().findViewById(R.id.textViewGenero2);
        rvGenero2 = getView().findViewById(R.id.recyclerViewGenero2);

        textViewGenero3 = getView().findViewById(R.id.textViewGenero3);
        rvGenero3 = getView().findViewById(R.id.recyclerViewGenero3);

        textViewGenero4 = getView().findViewById(R.id.textViewGenero4);
        rvGenero4 = getView().findViewById(R.id.recyclerViewGenero4);

        textViewGenero5 = getView().findViewById(R.id.textViewGenero5);
        rvGenero5 = getView().findViewById(R.id.recyclerViewGenero5);

        cargarCarruselesConGeneros();
    }

    private void cargarCarruselesConGeneros() {


        rvSeguirEscuchando.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rvGenero1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero4.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero5.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        if(InfoAudiolibros.getTodosLosAudiolibros() != null){
            String[] generos = InfoAudiolibros.getGenerosSeleccionados();

            adapterRecomendados = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosSeguirEscuchando());
            rvSeguirEscuchando.setAdapter(adapterRecomendados);

            adapter1 = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosPorGenero(generos[0]));
            rvGenero1.setAdapter(adapter1);
            textViewGenero1.setText(generos[0]);

            adapter2 = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosPorGenero(generos[1]));
            rvGenero2.setAdapter(adapter2);
            textViewGenero2.setText(generos[1]);

            adapter3 = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosPorGenero(generos[2]));
            rvGenero3.setAdapter(adapter3);
            textViewGenero3.setText(generos[2]);

            adapter4 = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosPorGenero(generos[3]));
            rvGenero4.setAdapter(adapter4);
            textViewGenero4.setText(generos[3]);

            adapter5 = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosPorGenero(generos[4]));
            rvGenero5.setAdapter(adapter5);
            textViewGenero5.setText(generos[4]);

        } else {
            adapterRecomendados = new MenuInicioAdapter(getContext(), InfoAudiolibros.getTodosLosAudiolibrosEjemplo());
            rvGenero1.setAdapter(adapterRecomendados);
            rvGenero2.setAdapter(adapterRecomendados);
            rvGenero3.setAdapter(adapterRecomendados);
            rvGenero4.setAdapter(adapterRecomendados);
            rvGenero5.setAdapter(adapterRecomendados);
        }
    }
}


