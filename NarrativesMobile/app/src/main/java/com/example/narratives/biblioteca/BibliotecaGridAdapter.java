package com.example.narratives.biblioteca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives.peticiones.Audiolibro;

import java.util.ArrayList;

public class BibliotecaGridAdapter extends BaseAdapter implements Filterable {

    private LibroFilter libroFilter;
    private ArrayList<Audiolibro> audiolibros;
    private ArrayList<Audiolibro> tempAudiolibros;

    private Context context;

    private LayoutInflater layoutInflater;

    public BibliotecaGridAdapter(Context context, ArrayList<Audiolibro> audiolibros) {
        this.context = context;
        this.audiolibros = audiolibros;
        this.tempAudiolibros = audiolibros;
    }


    @Override   
    public int getCount() {
        return audiolibros.size();
    }

    @Override
    public Object getItem(int i) {
        return audiolibros.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null){
            view = layoutInflater.inflate(R.layout.item_biblioteca_libro_portada, null);
        }

        ImageView imageView = view.findViewById(R.id.imageViewLibroBiblioteca);
        TextView textView = view.findViewById(R.id.textViewLibroBiblioteca);

        textView.setText(audiolibros.get(i).getTitulo());


        Glide
                .with(context)
                .load(audiolibros.get(i).getImg())
                .centerCrop()
                .placeholder(R.drawable.icono_imagen_estandar_foreground)
                .into(imageView);


        return view;
    }

    @Override
    public Filter getFilter() {
        if(libroFilter == null){
            libroFilter = new LibroFilter();
        }
        return libroFilter;
    }

    class LibroFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if (charSequence != null && charSequence.length() > 0) {
                charSequence = charSequence.toString().toUpperCase();
                ArrayList<Audiolibro> filtros = new ArrayList<>();

                for (int i = 0; i < tempAudiolibros.size(); i++) {
                    if (tempAudiolibros.get(i).getTitulo().toUpperCase().contains(charSequence)) {
                        filtros.add(tempAudiolibros.get(i));
                    }
                }

                filterResults.count = filtros.size();
                filterResults.values = filtros;

            } else {
                filterResults.count = tempAudiolibros.size();
                filterResults.values = tempAudiolibros;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            audiolibros = (ArrayList<Audiolibro>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}