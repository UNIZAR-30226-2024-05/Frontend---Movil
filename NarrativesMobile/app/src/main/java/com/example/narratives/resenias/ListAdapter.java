package com.example.narratives.resenias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.narratives.R;
import com.example.narratives.peticiones.Audiolibro;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Resenia> {

    public ListAdapter(Context context, ArrayList<Resenia> reseniArrayList){
        super(context, R.layout.item_lista_resenias, reseniArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Resenia resenia = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_resenias, parent, false);

        }

        TextView nombrePersona = convertView.findViewById(R.id.textViewNombrePersona);
        TextView textoReseña = convertView.findViewById(R.id.textViewDescripcionReseña);
        RatingBar valoracion = convertView.findViewById(R.id.ratingBar);

        float num = (float) resenia.valoracion;

        nombrePersona.setText(resenia.nombre);
        textoReseña.setText(resenia.descripcion);
        valoracion.setRating(num);

        return super.getView(position, convertView, parent);
    }
}
