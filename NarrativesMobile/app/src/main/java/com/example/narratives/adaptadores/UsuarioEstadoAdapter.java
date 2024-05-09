package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.narratives.R;
import com.example.narratives.informacion.InfoAmigos;
import com.example.narratives.peticiones.amistad.lista.UsuarioEstado;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class UsuarioEstadoAdapter extends ArrayAdapter<UsuarioEstado> implements Filterable {


    private List<UsuarioEstado> userList;
    private List<UsuarioEstado> tempUserList;

    private Context context;
    private int resourceLayout;

    private UsuarioEstadoFilter userFilter;

    public UsuarioEstadoAdapter(@NonNull Context _context, int _resource, @NonNull List <UsuarioEstado> _objects) {
        super(_context, _resource, _objects);
        this.userList = _objects;
        this.tempUserList = _objects;
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

        UsuarioEstado usuario = userList.get(position);

        TextView nombre = view.findViewById(R.id.textViewNombreAnadirUsuario);
        nombre.setText(usuario.getUsername());

        ShapeableImageView foto = view.findViewById(R.id.imageViewFotoAnadirAmigo);
        foto.setImageResource(InfoAmigos.getImageResourceFromImgCode(usuario.getImg()));

        MaterialButton boton = (MaterialButton) view.findViewById(R.id.botonAnadirAmigo);

        if(usuario.getEstado() == 1){
            cambiarAEnviarSolicitud(boton);
        } else if(usuario.getEstado() == 2){
            cambiarACancelarSolicitud(boton);
        } else if(usuario.getEstado() == 3){
            cambiarASolicitudRecibida(boton);
        }

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(userFilter == null){
            userFilter = new UsuarioEstadoFilter();
        }
        return userFilter;
    }

    @Nullable
    @Override
    public UsuarioEstado getItem(int position) {
        return userList.get(position);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    class UsuarioEstadoFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.length() > 0) {
                charSequence = charSequence.toString().toUpperCase();
                ArrayList<UsuarioEstado> filtros = new ArrayList<>();

                for (int i = 0; i < tempUserList.size(); i++) {
                    if (tempUserList.get(i).getUsername().toUpperCase().contains(charSequence)) {
                        filtros.add(tempUserList.get(i));
                    }
                }

                filterResults.count = filtros.size();
                filterResults.values = filtros;

            } else {
                filterResults.count = tempUserList.size();
                filterResults.values = tempUserList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            userList = (ArrayList<UsuarioEstado>)filterResults.values;
            notifyDataSetChanged();
        }
    }

    private void cambiarACancelarSolicitud(MaterialButton boton){
        boton.setBackgroundResource(R.color.white);
        boton.setStrokeWidth(2);
        boton.setStrokeColorResource(R.color.teal_300);
        boton.setText("Cancelar solicitud");
        boton.setIconTintResource(R.color.teal_300);
    }

    private void cambiarAEnviarSolicitud(MaterialButton boton){
        boton.setBackgroundResource(R.color.teal_300);
        boton.setStrokeWidth(0);
        boton.setStrokeColorResource(R.color.white);
        boton.setText("Enviar solicitud");
        //boton.setTextColor(R.color.white);
        boton.setIconTintResource(R.color.white);
    }

    private void cambiarASolicitudRecibida(MaterialButton boton){
        boton.setBackgroundResource(R.color.white);
        boton.setStrokeWidth(0);
        boton.setElevation((float) 0.01);
        boton.setStrokeColorResource(R.color.white);
        boton.setText("Solicitud recibida");
        boton.setIconTintResource(R.color.gris_claro);
        boton.setClickable(false);
    }
}
