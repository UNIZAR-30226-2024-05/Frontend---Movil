package com.example.narratives.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.narratives.R;
import com.example.narratives.peticiones.colecciones.ColeccionesItem;

import java.util.ArrayList;
import java.util.List;

public class ColeccionesAdapter extends ArrayAdapter<ColeccionesItem> implements Filterable {
    private final Context context;
    private final int resourceLayout;
    private List<ColeccionesItem> coleccionesList;
    private final List<ColeccionesItem> tmpColeccionesList;
    private OnMenuItemClickListener listener;
    private boolean coleccionAmigo;
    private ColeccionesFilter filter;

    public ColeccionesAdapter(@NonNull Context context, int resourceLayout, @NonNull List<ColeccionesItem> coleccionesList) {
        super(context, 0, coleccionesList);
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.coleccionesList = coleccionesList;
        this.tmpColeccionesList = coleccionesList;
    }

    @Override
    public int getCount() {
        return coleccionesList.size();
    }

    @Override
    public ColeccionesItem getItem(int i) {
        return coleccionesList.get(i);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceLayout, null);
        }

        TextView textViewNombreColeccion = view.findViewById(R.id.textViewNobreColeccion);
        textViewNombreColeccion.setText(coleccionesList.get(position).getTitulo());

        ImageView imageViewTresPuntosColeccion = view.findViewById(R.id.imageViewTresPuntosColeccion);

        /*if (coleccionAmigo) {
            imageViewTresPuntosColeccion.setVisibility(View.GONE);
        }*/

        imageViewTresPuntosColeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_lista_coleccion_audiolibros, popupMenu.getMenu());

                Menu menu = popupMenu.getMenu();
                MenuItem itemEliminar = menu.findItem(R.id.menu_eliminar);

                if (coleccionAmigo || coleccionesList.get(position).getTitulo().equals("Escuchar mas tarde")
                    || coleccionesList.get(position).getTitulo().equals("Favoritos")) {
                    itemEliminar.setVisible(false);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_eliminar) {
                            if (listener != null) {
                                listener.onMenuEliminarColeccionClick(position);
                            }
                            return true;
                        } else if (item.getItemId() == R.id.menu_compartir) {
                            // Crea un Intent para compartir el enlace
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            String mensaje = "Mira esta colección: " +  getItem(position).getTitulo()
                                                + ".\n\n" + "https://www.narratives.es/coleccion?id="
                                                + String.valueOf(getItem(position).getId());
                            shareIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
                            // Inicia el Intent
                            context.startActivity(Intent.createChooser(shareIntent, "Compartir colección"));
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        return view;
    }

    public boolean isColeccionAmigo() {
        return coleccionAmigo;
    }

    public void setColeccionAmigo(boolean coleccionAmigo) {
        this.coleccionAmigo = coleccionAmigo;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new ColeccionesFilter();
        }
        return filter;
    }

    public interface OnMenuItemClickListener {
        void onMenuEliminarColeccionClick(int position);
    }

    public void setOnMenuEliminarColeccionClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    public class ColeccionesFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<ColeccionesItem> filtros = new ArrayList<>();

                for (int i = 0; i < tmpColeccionesList.size(); i++) {
                    if (tmpColeccionesList.get(i).getTitulo().toUpperCase().contains(constraint)) {
                        filtros.add(tmpColeccionesList.get(i));
                    }
                }

                filterResults.count = filtros.size();
                filterResults.values = filtros;

            } else {
                filterResults.count = tmpColeccionesList.size();
                filterResults.values = tmpColeccionesList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            coleccionesList = (List<ColeccionesItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
