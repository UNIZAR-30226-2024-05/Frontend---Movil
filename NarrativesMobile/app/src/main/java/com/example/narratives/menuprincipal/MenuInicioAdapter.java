package com.example.narratives.menuprincipal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;

public class MenuInicioAdapter extends RecyclerView.Adapter<MenuInicioAdapter.AdaptadorViewHolder> {

    Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<AudiolibroItem> audiolibros;

    public MenuInicioAdapter(Context context, ArrayList<AudiolibroItem> audiolibros) {
        this.context = context;
        this.audiolibros = audiolibros;
    }

    @NonNull
    @Override
    public MenuInicioAdapter.AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_principal,null,false);
        return new MenuInicioAdapter.AdaptadorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuInicioAdapter.AdaptadorViewHolder holder, int position) {
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
        }
    }
}
