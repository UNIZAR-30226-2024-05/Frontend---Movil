package com.example.narratives.menuprincipal;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives.peticiones.Audiolibro;

import java.time.Instant;
import java.util.ArrayList;

public class MenuPrincipalGridAdapter extends BaseAdapter {

    private ArrayList<Audiolibro> audiolibros;
    private ArrayList<Audiolibro> tempAudiolibros;

    private Context context;

    private LayoutInflater layoutInflater;
    public MenuPrincipalGridAdapter(Context context, ArrayList<Audiolibro> audiolibros) {
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
            view = layoutInflater.inflate(R.layout.item_menu_principal, null);
        }

        ImageView imageView = view.findViewById(R.id.imageViewLibro);
        TextView textViewNombre = view.findViewById(R.id.textViewNombreLibro);
        TextView textViewValoracion = view.findViewById(R.id.textViewValoracion);

        //textViewNombre.setText(audiolibros.get(i).getTitulo());

        //textViewValoracion.setText(audiolibros.get(i).getId());//CAMBIAR ESTO POR VALORACION


        Glide.with(context).load(audiolibros.get(i).getImg()).centerCrop().placeholder(R.drawable.icono_imagen_estandar_foreground).into(imageView);


        return view;
    }
}
