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

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives.peticiones.colecciones.AudiolibrosColeccionItem;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class AudiolibrosColeccionAdapter extends ArrayAdapter<AudiolibrosColeccionItem>  {
    private Context context;
    private int resourceLayout;
    private List<AudiolibrosColeccionItem> audiolibrosList;
    private AudiolibrosColeccionAdapter.OnMenuItemClickListener listener;

    public AudiolibrosColeccionAdapter(@NonNull Context context, int resourceLayout, @NonNull List<AudiolibrosColeccionItem> audiolibrosList) {
        super(context, 0, audiolibrosList);
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.audiolibrosList = audiolibrosList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceLayout, null);
        }

        AudiolibrosColeccionItem audiolibro = audiolibrosList.get(position);

        TextView textViewNombreAudiolibroColeccion = view.findViewById(R.id.textViewNombreAudiolibroColeccion);
        textViewNombreAudiolibroColeccion.setText(audiolibro.getTitulo());

        TextView textViewAutorAudiolibroColeccion = view.findViewById(R.id.textViewAutorAudiolibroColeccion);
        textViewAutorAudiolibroColeccion.setText(audiolibro.getNombre_autor());

        ShapeableImageView imageViewImgAudiolibroColeccion = view.findViewById(R.id.imageViewImgAudiolibroColeccion);
        Glide
                .with(getContext())
                .load(audiolibro.getImg())
                .centerCrop()
                .placeholder(R.drawable.icono_imagen_estandar_foreground)
                .into(imageViewImgAudiolibroColeccion);

        ImageView imageViewEscucharAudiolibroColeccion = view.findViewById(R.id.imageViewEscucharAudiolibroColeccion);
        imageViewEscucharAudiolibroColeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReproducirAudiolibroClick(position);
                }
            }
        });

        ImageView imageViewTresPuntosColeccion = view.findViewById(R.id.imageViewTresPuntosAudiolibroColeccion);
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
                                    listener.onMenuEliminarAudiolibroClick(position);
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
        void onMenuEliminarAudiolibroClick(int position);
        void onReproducirAudiolibroClick(int position);
    }

    public void setOnMenuEliminarAudiolibroClickListener(AudiolibrosColeccionAdapter.OnMenuItemClickListener listener) {
        this.listener = listener;
    }
}
