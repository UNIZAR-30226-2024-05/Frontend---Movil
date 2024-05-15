package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.narratives.R;
import com.example.narratives.peticiones.audiolibros.especifico.GenericReview;

import java.util.List;

public class ResenasAdapter extends ArrayAdapter<GenericReview> {
    private Context context;
    private int resourceLayout;
    private List<GenericReview> resenasList;

    public ResenasAdapter(@NonNull Context context, int resourceLayout, @NonNull List<GenericReview> resenasList) {
        super(context, 0, resenasList);
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.resenasList = resenasList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceLayout, null);
        }

        TextView textViewUsernameResenas = view.findViewById(R.id.textViewUsernameResenas);
        textViewUsernameResenas.setText(resenasList.get(position).getUsername());

        CardView cardView = view.findViewById(R.id.cardView);
        if (resenasList.get(position).getComentario() == null | resenasList.get(position).getComentario().isEmpty()) {
            cardView.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.VISIBLE);
            TextView textViewComentarioResena = cardView.findViewById(R.id.textViewComentarioResena);
            textViewComentarioResena.setText(resenasList.get(position).getComentario());
        }

        RatingBar ratingBarListaResenas = view.findViewById(R.id.ratingBarListaResenas);
        ratingBarListaResenas.setRating((float) resenasList.get(position).getPuntuacion());
        ratingBarListaResenas.setIsIndicator(true);

        return view;
    }
}
