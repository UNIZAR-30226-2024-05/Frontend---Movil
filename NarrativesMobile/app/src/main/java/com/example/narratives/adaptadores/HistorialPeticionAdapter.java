package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.informacion.InfoPeticiones;
import com.example.narratives.peticiones.amistad.peticiones.HistorialPeticionConTipo;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistorialPeticionAdapter extends ArrayAdapter<HistorialPeticionConTipo> {


    private List<HistorialPeticionConTipo> petitionList;
    private List<HistorialPeticionConTipo> tempPetitionList;

    private Context context;
    private int resourceLayout;

    RetrofitInterface retrofitInterface = ApiClient.getRetrofitInterface();

    public HistorialPeticionAdapter(@NonNull Context _context, int _resource, @NonNull List <HistorialPeticionConTipo> _objects) {
        super(_context, _resource, _objects);
        Collections.sort(_objects, new Comparator<HistorialPeticionConTipo>() {
            @Override
            public int compare(HistorialPeticionConTipo a1, HistorialPeticionConTipo a2) {
                return a1.getFecha().comparar(a2.getFecha());
            }
        });
        this.petitionList = _objects;
        this.tempPetitionList = _objects;
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

        HistorialPeticionConTipo peticion = petitionList.get(position);

        TextView accion = view.findViewById(R.id.textViewAccionHistorial);
        accion.setText(formatearAccionSegun(peticion));


        TextView fecha = view.findViewById(R.id.textViewFechaHistorial);
        fecha.setText(peticion.getFecha().getFechaFormateada());

        ShapeableImageView foto = view.findViewById(R.id.imageViewIconoHistorial);
        foto.setImageResource(InfoPeticiones.getImageResourceFromCode(peticion.getTipo()));

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private String formatearAccionSegun(HistorialPeticionConTipo peticion) {
        if(peticion.getTipo() == 0){
            return "Solicitud ENVIADA a " + peticion.getUsername() + ".";
        } else if(peticion.getTipo() == 1){
            return "Solicitud RECIBIDA de " + peticion.getUsername() + ".";
        } else if(peticion.getTipo() == 2){
            return "Solicitud ACEPTADA por " + peticion.getUsername() + ".";
        } else if(peticion.getTipo() == 3){
            return "Solicitud RECHAZADA por " + peticion.getUsername() + ".";
        } else {
            return "Código desconocido";
        }
    }

    @Nullable
    @Override
    public HistorialPeticionConTipo getItem(int position) {
        return petitionList.get(position);
    }

    @Override
    public int getCount() {
        return petitionList.size();
    }

}
