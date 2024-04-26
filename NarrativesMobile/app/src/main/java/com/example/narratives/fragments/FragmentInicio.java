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
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;

public class FragmentInicio extends Fragment {
    RecyclerView rv, rvTerror, rvFantasia, rvMitologia, rvNovela, rvPoesia;
    MenuInicioAdapter MenuInicioAdapter;
    private ArrayList<AudiolibroItem> audiolibros;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        audiolibros = InfoAudiolibros.getTodosLosAudiolibrosEjemplo();

        rv = getView().findViewById(R.id.RecyclerViewSeguirEscuchando);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        MenuInicioAdapter = new MenuInicioAdapter(getContext(), audiolibros);
        rv.setAdapter(MenuInicioAdapter);

        rvTerror = getView().findViewById(R.id.RecyclerViewTerror);
        rvTerror.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvTerror.setAdapter(MenuInicioAdapter);

        rvFantasia = getView().findViewById(R.id.RecyclerFantasia);
        rvFantasia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFantasia.setAdapter(MenuInicioAdapter);

        rvMitologia = getView().findViewById(R.id.RecyclerMitologia);
        rvMitologia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvMitologia.setAdapter(MenuInicioAdapter);

        rvPoesia = getView().findViewById(R.id.RecyclerPoesia);
        rvPoesia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvPoesia.setAdapter(MenuInicioAdapter);

        rvNovela = getView().findViewById(R.id.RecyclerNovela);
        rvNovela.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvNovela.setAdapter(MenuInicioAdapter);

    }
}


