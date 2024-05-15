package com.example.narratives.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.MenuInicioAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibrosResult;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInicio extends Fragment {
    private RetrofitInterface retrofitInterface;
    private RecyclerView rvSeguirEscuchando, rvGenero1, rvGenero2, rvGenero3, rvGenero4, rvGenero5;

    TextView textViewGenero1, textViewGenero2, textViewGenero3, textViewGenero4, textViewGenero5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        inicializarObjetos();

        if (InfoAudiolibros.getTodosLosAudiolibros() == null || InfoAudiolibros.getTodosLosAudiolibros().isEmpty()) {
            obtenerTodosLosAudiolibros();
        } else {
            cargarCarruselesConGeneros();
        }
    }

    private void inicializarObjetos() {
        retrofitInterface = ApiClient.getRetrofitInterface();

        rvSeguirEscuchando = getView().findViewById(R.id.recyclerViewSeguirEscuchando);

        textViewGenero1 = getView().findViewById(R.id.textViewGenero1);
        rvGenero1 = getView().findViewById(R.id.recyclerViewGenero1);

        textViewGenero2 = getView().findViewById(R.id.textViewGenero2);
        rvGenero2 = getView().findViewById(R.id.recyclerViewGenero2);

        textViewGenero3 = getView().findViewById(R.id.textViewGenero3);
        rvGenero3 = getView().findViewById(R.id.recyclerViewGenero3);

        textViewGenero4 = getView().findViewById(R.id.textViewGenero4);
        rvGenero4 = getView().findViewById(R.id.recyclerViewGenero4);

        textViewGenero5 = getView().findViewById(R.id.textViewGenero5);
        rvGenero5 = getView().findViewById(R.id.recyclerViewGenero5);
    }


    public void obtenerTodosLosAudiolibros() {
        Call<AudiolibrosResult> llamada = retrofitInterface.ejecutarAudiolibros(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<AudiolibrosResult>() {
            @Override
            public void onResponse(@NonNull Call<AudiolibrosResult> call, @NonNull Response<AudiolibrosResult> response) {
                int codigo = response.code();

                if (codigo == 200) {
                    ArrayList<AudiolibroItem> audiolibrosResult = response.body().getAudiolibros();

                    if (audiolibrosResult == null) {
                        Toast.makeText(getContext(), "Resultado de audiolibros nulo", Toast.LENGTH_LONG).show();
                    } else {
                        InfoAudiolibros.setTodosLosAudiolibros(audiolibrosResult);
                        cargarCarruselesConGeneros();
                    }
                } else if (codigo == 500){
                    Toast.makeText(getContext(), "Error del servidor",
                            Toast.LENGTH_LONG).show();
                } else if (codigo == 404){
                    Toast.makeText(getContext(), "Error 404 /audiolibros", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error desconocido (audiolibros): " + String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AudiolibrosResult> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor (audiolibros)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarCarruselesConGeneros() {
        rvSeguirEscuchando.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rvGenero1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero4.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenero5.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        MenuInicioAdapter adapterRecomendados;
        if (InfoAudiolibros.getTodosLosAudiolibros() != null) {
            String[] generos = InfoAudiolibros.getGenerosSeleccionados();

            adapterRecomendados = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosSeguirEscuchando());
            rvSeguirEscuchando.setAdapter(adapterRecomendados);

            MenuInicioAdapter adapter1 = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosPorGenero(generos[0]));
            rvGenero1.setAdapter(adapter1);
            textViewGenero1.setText(generos[0]);

            MenuInicioAdapter adapter2 = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosPorGenero(generos[1]));
            rvGenero2.setAdapter(adapter2);
            textViewGenero2.setText(generos[1]);

            MenuInicioAdapter adapter3 = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosPorGenero(generos[2]));
            rvGenero3.setAdapter(adapter3);
            textViewGenero3.setText(generos[2]);

            MenuInicioAdapter adapter4 = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosPorGenero(generos[3]));
            rvGenero4.setAdapter(adapter4);
            textViewGenero4.setText(generos[3]);

            MenuInicioAdapter adapter5 = new MenuInicioAdapter(getContext(), InfoAudiolibros.getAudiolibrosPorGenero(generos[4]));
            rvGenero5.setAdapter(adapter5);
            textViewGenero5.setText(generos[4]);

        } else {
            adapterRecomendados = new MenuInicioAdapter(getContext(), InfoAudiolibros.getTodosLosAudiolibrosEjemplo());
            rvGenero1.setAdapter(adapterRecomendados);
            rvGenero2.setAdapter(adapterRecomendados);
            rvGenero3.setAdapter(adapterRecomendados);
            rvGenero4.setAdapter(adapterRecomendados);
            rvGenero5.setAdapter(adapterRecomendados);
        }
    }
}
