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
import com.example.narratives.peticiones.Audiolibro;

import java.util.ArrayList;

public class adaptador extends RecyclerView.Adapter<adaptador.AdaptadorViewHolder>  {
    private final RecyclerViewInterface rvi;
    Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Audiolibro> audiolibros;


    public adaptador(RecyclerViewInterface rvi, Context context, ArrayList<Audiolibro> audiolibros) {
        this.rvi = rvi;
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
                public void onClick(View view) {
                    if(rvi != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            rvi.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

}
