package com.example.narratives.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.menuprincipal.MenuInicioAdapter;

public class FragmentInicio extends Fragment {
    RecyclerView rvSeguirEscuchando, rvGenero1, rvGenero2, rvGenero3, rvGenero4, rvGenero5;
    MenuInicioAdapter menuInicioAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        rvSeguirEscuchando = getView().findViewById(R.id.recyclerViewSeguirEscuchando);
        rvSeguirEscuchando.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        if(InfoAudiolibros.getTodosLosAudiolibros() != null){
            menuInicioAdapter = new MenuInicioAdapter(getContext(), InfoAudiolibros.getTodosLosAudiolibros());
        } else {
            menuInicioAdapter = new MenuInicioAdapter(getContext(), InfoAudiolibros.getTodosLosAudiolibrosEjemplo());
        }

        rvSeguirEscuchando.setAdapter(menuInicioAdapter);

        rvGenero1 = getView().findViewById(R.id.recyclerViewGenero1);
        rvGenero1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero1.setAdapter(menuInicioAdapter);

        rvGenero2 = getView().findViewById(R.id.recyclerViewGenero2);
        rvGenero2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero2.setAdapter(menuInicioAdapter);

        rvGenero3 = getView().findViewById(R.id.recyclerViewGenero3);
        rvGenero3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero3.setAdapter(menuInicioAdapter);

        rvGenero5 = getView().findViewById(R.id.recyclerViewGenero4);
        rvGenero5.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero5.setAdapter(menuInicioAdapter);

        rvGenero4 = getView().findViewById(R.id.recyclerViewGenero5);
        rvGenero4.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero4.setAdapter(menuInicioAdapter);
    }
}


