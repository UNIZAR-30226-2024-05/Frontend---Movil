package com.example.narratives.resenias;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.narratives.peticiones.Audiolibro;

public class ListAdapter extends ArrayAdapter<Resenia> {
    public ListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
