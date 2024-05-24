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
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.especifico.Marcapaginas;

import java.util.List;

public class MarcapaginasAdapter extends ArrayAdapter<Marcapaginas> implements Filterable {
    private List<Marcapaginas> marcapaginas;

    private List<Marcapaginas> tempCapitulos;
    private AudiolibroEspecificoResponse audiolibroActual;

    private Context context;

    private int resourceLayout;
    private LayoutInflater layoutInflater;

    public MarcapaginasAdapter(@NonNull Context _context, int _resource, @NonNull List<Marcapaginas> _objects, AudiolibroEspecificoResponse audiolibro) {
        super(_context, _resource, _objects);
        this.marcapaginas = _objects;
        this.tempCapitulos = _objects;
        this.context = _context;
        this.resourceLayout = _resource;
        this.audiolibroActual =audiolibro;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceLayout, null);
        }

        Marcapaginas cap = marcapaginas.get(position);

        TextView numCapitulo = view.findViewById(R.id.textView_marcapaginasItemCap);
        numCapitulo.setText(audiolibroActual.getNombreCap(cap.getCapitulo()));

        TextView titulo = view.findViewById(R.id.textView_marcapaginasItemTitle);
        titulo.setText(cap.getTitulo());

        TextView time = view.findViewById(R.id.textView_marcapaginasItemTime);
        time.setText(cap.getFecha());

        return view;
    }

    @Nullable
    @Override
    public Marcapaginas getItem(int position) {
        return marcapaginas.get(position);
    }

    @Override
    public int getCount() {
        if(marcapaginas==null){return 0;}
        return marcapaginas.size();
    }





}
