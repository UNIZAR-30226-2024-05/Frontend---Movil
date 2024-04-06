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
    private ArrayList<Audiolibro> todosLosAudiolibros;
    private ArrayList<Audiolibro> tempTodosLosAudiolibros;

    private Context context;

    private LayoutInflater layoutInflater;

    public BibliotecaGridAdapter(Context context, ArrayList<Audiolibro> todosLosAudiolibros) {
        this.context = context;
        this.todosLosAudiolibros = todosLosAudiolibros;
        this.tempTodosLosAudiolibros = todosLosAudiolibros;
    }


    @Override   
    public int getCount() {
        return todosLosAudiolibros.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
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
            view = layoutInflater.inflate(R.layout.biblioteca_libro_portada, null);
        }

        ImageView imageView = view.findViewById(R.id.imageViewLibroBiblioteca);
        TextView textView = view.findViewById(R.id.textViewLibroBiblioteca);

        textView.setText(todosLosAudiolibros.get(i).getTitulo());
        Glide
                .with(context)
                .load(todosLosAudiolibros.get(i).getImg())
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

                for (int i = 0; i < tempTodosLosAudiolibros.size(); i++) {
                    if (tempTodosLosAudiolibros.get(i).getTitulo().toUpperCase().contains(charSequence)) {
                        filtros.add(tempTodosLosAudiolibros.get(i));
                    }
                }

                filterResults.count = filtros.size();
                filterResults.values = filtros;

            } else {
                filterResults.count = tempTodosLosAudiolibros.size();
                filterResults.values = tempTodosLosAudiolibros;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            todosLosAudiolibros = (ArrayList<Audiolibro>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
