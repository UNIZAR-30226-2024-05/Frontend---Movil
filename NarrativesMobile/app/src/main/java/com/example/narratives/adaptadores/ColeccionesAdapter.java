package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.narratives.R;
import com.example.narratives.peticiones.colecciones.ColeccionesItem;

import java.util.List;

public class ColeccionesAdapter extends ArrayAdapter<ColeccionesItem> {
    private Context context;
    private int resourceLayout;
    private List<ColeccionesItem> coleccionesList;

    private OnMenuItemClickListener listener;

    public ColeccionesAdapter(@NonNull Context context, int resourceLayout, @NonNull List<ColeccionesItem> coleccionesList) {
        super(context, 0, coleccionesList);
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.coleccionesList = coleccionesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceLayout, null);
        }

        ColeccionesItem coleccion = coleccionesList.get(position);

        TextView textViewNombreColeccion = view.findViewById(R.id.textViewNobreColeccion);
        textViewNombreColeccion.setText(coleccion.getTitulo());

        ImageView imageViewTresPuntosColeccion = view.findViewById(R.id.imageViewTresPuntosColeccion);
        imageViewTresPuntosColeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_lista_coleccion_audiolibros, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_eliminar:
                                if (listener != null) {
                                    listener.onMenuEliminarColeccionClick(position);
                                }
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        return view;
    }

    public interface OnMenuItemClickListener {
        void onMenuEliminarColeccionClick(int position);
    }

    public void setOnMenuEliminarColeccionClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }
}
