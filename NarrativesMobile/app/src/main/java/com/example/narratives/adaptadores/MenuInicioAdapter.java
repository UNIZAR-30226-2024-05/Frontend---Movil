package com.example.narratives.adaptadores;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
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
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.InfoLibroActivity;
import com.example.narratives.activities.MainActivity;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuInicioAdapter extends RecyclerView.Adapter<MenuInicioAdapter.AdaptadorViewHolder> {
    private final Context context;
    private LayoutInflater layoutInflater;
    private final ArrayList<AudiolibroItem> audiolibros;

    RetrofitInterface retrofitInterface;

    public MenuInicioAdapter(Context context, ArrayList<AudiolibroItem> audiolibros) {
        this.context = context;
        this.audiolibros = audiolibros;
    }

    @NonNull
    @Override
    public MenuInicioAdapter.AdaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_principal,null,false);
        return new AdaptadorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuInicioAdapter.AdaptadorViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        retrofitInterface = ApiClient.getRetrofitInterface();

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


        int finalPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peticionAudiolibrosId(finalPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (audiolibros == null) {
            return 0;
        }
        return audiolibros.size();
    }

    public static class AdaptadorViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvTitulo;
        private final ImageView imageView;
        public AdaptadorViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewLibro);
            tvTitulo = itemView.findViewById(R.id.textViewNombreLibro);
        }
    }

    private void peticionAudiolibrosId(int position) {
        AudiolibroItem audiolibro = (AudiolibroItem) audiolibros.get(position);

        Call<AudiolibroEspecificoResponse> llamada = retrofitInterface.ejecutarAudiolibrosId(ApiClient.getUserCookie(), audiolibro.getId());
        llamada.enqueue(new Callback<AudiolibroEspecificoResponse>() {
            @Override
            public void onResponse(@NonNull Call<AudiolibroEspecificoResponse> call, @NonNull Response<AudiolibroEspecificoResponse> response) {
                int codigo = response.code();

                if (codigo == 200) {
                    InfoAudiolibros.setAudiolibroActual(response.body());
                    abrirInfoLibro();
                } else if (codigo == 409) {
                    Toast.makeText(MainActivity.fragmentoInicioAbierto.getContext(), "No hay ningún audiolibro con ese ID (menuInicioAdaper)", Toast.LENGTH_LONG).show();
                } else if (codigo == 500) {
                    Toast.makeText(MainActivity.fragmentoInicioAbierto.getContext(), "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.fragmentoInicioAbierto.getContext(), "Error desconocido (AudiolibrosId): " + codigo, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AudiolibroEspecificoResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.fragmentoInicioAbierto.getContext(), "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirInfoLibro() {
        Intent intent = new Intent(MainActivity.fragmentoInicioAbierto.getContext(), InfoLibroActivity.class);
        startActivity(MainActivity.fragmentoInicioAbierto.getContext(), intent,
                                ActivityOptions.makeSceneTransitionAnimation(MainActivity.fragmentoInicioAbierto.getActivity()).toBundle());
    }
}
