package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives.peticiones.colecciones.AudiolibrosColeccionItem;
import com.example.narratives.peticiones.colecciones.ColeccionesItem;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class AudiolibrosColeccionAdapter extends ArrayAdapter<AudiolibrosColeccionItem>  {
    private Context context;
    private int resourceLayout;
    private List<AudiolibrosColeccionItem> audiolibrosList;

    public AudiolibrosColeccionAdapter(@NonNull Context context, int resourceLayout, @NonNull List<AudiolibrosColeccionItem> audiolibrosList) {
        super(context, 0, audiolibrosList);
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.audiolibrosList = audiolibrosList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceLayout, null);
        }

        AudiolibrosColeccionItem audiolibro = audiolibrosList.get(position);

        TextView textViewNombreAudiolibroColeccion = view.findViewById(R.id.textViewNombreAudiolibroColeccion);
        textViewNombreAudiolibroColeccion.setText(audiolibro.getTitulo());

        TextView textViewAutorAudiolibroColeccion = view.findViewById(R.id.textViewAutorAudiolibroColeccion);
        textViewAutorAudiolibroColeccion.setText(audiolibro.getAutor());

        ShapeableImageView imageViewImgAudiolibroColeccion = view.findViewById(R.id.imageViewImgAudiolibroColeccion);
        Glide
                .with(getContext())
                .load(audiolibro.getImg())
                .centerCrop()
                .placeholder(R.drawable.icono_imagen_estandar_foreground)
                .into(imageViewImgAudiolibroColeccion);

        return view;
    }
}
