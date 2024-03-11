package com.example.narratives.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.narratives.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FragmentEscuchando extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_escuchando, container, false);
        FloatingActionButton fabPause = (FloatingActionButton) view.findViewById(R.id.botonPauseEscuchando);
        fabPause.setEnabled(false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        FloatingActionButton fabPlay = (FloatingActionButton) getView().findViewById(R.id.botonPlayEscuchando);
        FloatingActionButton fabPause = (FloatingActionButton) getView().findViewById(R.id.botonPauseEscuchando);

        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabPause.setEnabled(true);
                fabPlay.setEnabled(false);
            }
        });

        fabPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabPlay.setEnabled(true);
                fabPause.setEnabled(false);
            }
        });

    }

}