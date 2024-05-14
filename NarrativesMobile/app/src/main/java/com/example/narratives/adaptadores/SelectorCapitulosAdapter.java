package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.narratives.peticiones.audiolibros.especifico.Capitulo;

import java.util.ArrayList;
import java.util.List;

public class SelectorCapitulosAdapter extends BaseAdapter {

    private List<Capitulo> marcapaginasList;
    private List<Capitulo> tempMarcapaginasList;
    private Context context;


    public SelectorCapitulosAdapter(Context context, List<Capitulo> marcapaginasList) {
        this.context = context;
        this.marcapaginasList = marcapaginasList;
        this.tempMarcapaginasList = new ArrayList<>(marcapaginasList);
    }

    @Override
    public int getCount() {
        return marcapaginasList.size();
    }

    @Override
    public Object getItem(int position) {
        return marcapaginasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(marcapaginasList.get(position).getNombre());

        return view;
    }

}
