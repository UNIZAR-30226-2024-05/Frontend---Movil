package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.narratives.R;
import com.example.narratives.activities.MainActivity;
import com.example.narratives.peticiones.audiolibros.especifico.Capitulo;

import java.util.List;

public class CapitulosAdapter  extends ArrayAdapter<Capitulo> implements Filterable {
    private List<Capitulo> capitulos;

    private List<Capitulo> tempCapitulos;

    private Context context;

    private int resourceLayout;
    private LayoutInflater layoutInflater;

    public CapitulosAdapter(@NonNull Context _context, int _resource, @NonNull List<Capitulo> _objects) {
        super(_context, _resource, _objects);
        this.capitulos = _objects;
        this.tempCapitulos = _objects;
        this.context = _context;
        this.resourceLayout = _resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceLayout, null);
        }

        Capitulo cap = capitulos.get(position);

        TextView numCapitulo = view.findViewById(R.id.textViewNumCapitulo);
        numCapitulo.setText(MainActivity.fragmentoEscuchandoAbierto.getCapituloWithNumberString(position+1));

        TextView nombre = view.findViewById(R.id.textViewNombreCapitulo);
        nombre.setText(cap.getNombre());

        return view;
    }

    @Nullable
    @Override
    public Capitulo getItem(int position) {
        return capitulos.get(position);
    }

    @Override
    public int getCount() {
        return capitulos.size();
    }





}
