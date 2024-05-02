package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.narratives.peticiones.colecciones.ColeccionesItem;

import java.util.List;

public class ColeccionesAdapter extends ArrayAdapter<ColeccionesItem> {
    private Context context;
    private int resourceLayout;
    private List<ColeccionesItem> coleccionesList;

    public ColeccionesAdapter(@NonNull Context context, int resourceLayout, @NonNull List<ColeccionesItem> coleccionesList) {
        super(context, 0, coleccionesList);
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.coleccionesList = coleccionesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceLayout, null);
        }

        ColeccionesItem coleccion = coleccionesList.get(position);

        TextView textViewNombreColeccion = view.findViewById(android.R.id.text1);
        textViewNombreColeccion.setText(coleccion.getTitulo());

        return view;
    }
}

