package com.example.narratives.menuprincipal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives.fragments.FragmentInicio;
import com.example.narratives.peticiones.Audiolibro;

import java.util.ArrayList;

public class adaptador extends RecyclerView.Adapter<adaptador.AdaptadorViewHolder> {

    Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Audiolibro> audiolibros;


    public adaptador(Context context, ArrayList<Audiolibro> audiolibros) {
        this.context = context;
        this.audiolibros = audiolibros;
    }

    @NonNull
    @Override
    public adaptador.AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_principal,null,false);
        return new adaptador.AdaptadorViewHolder(v);
    }

    public Object getItem(int i) {
        return audiolibros.get(i);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptador.AdaptadorViewHolder holder, int position) {
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        holder.tvTitulo.setText(audiolibros.get(position).getTitulo());


        Glide
                .with(context)
                .load(audiolibros.get(position).getImg())
                .centerCrop()
                .placeholder(R.drawable.icono_imagen_estandar_foreground)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return audiolibros.size();
    }

    public class AdaptadorViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitulo;
        ImageView imageView;
        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imageViewLibro);
            tvTitulo = itemView.findViewById(R.id.textViewNombreLibro);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener la posición del elemento clickeado
                    int position = getAdapterPosition();

                    // Verificar que la posición sea válida
                    if (position != RecyclerView.NO_POSITION) {
                        // Obtener el audiolibro en la posición clickeada
                        Audiolibro audiolibro = audiolibros.get(position);
                        // Mostrar un Toast con el título del audiolibro
                        Toast.makeText(itemView.getContext(), "Audiolibro seleccionado: " + audiolibro.getTitulo(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
