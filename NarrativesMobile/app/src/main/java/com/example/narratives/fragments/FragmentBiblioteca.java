package com.example.narratives.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.info.InfoLibroActivity;
import com.example.narratives.adaptadores.BibliotecaAutorGridAdapter;
import com.example.narratives.adaptadores.BibliotecaTagsGridAdapter;
import com.example.narratives.adaptadores.BibliotecaTituloGridAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentBiblioteca extends Fragment {
    private RetrofitInterface retrofitInterface;
    private GridView gridView;
    private BaseAdapter adaptadorActual;
    private Switch switchOrdenarPor;
    private String generoLibrosMostrados;
    private String buscarPorActual;
    private ArrayList<String> categoriasBuscarPor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_biblioteca, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Retrofit retrofit = ApiClient.getRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();
        gridView = (GridView) getView().findViewById(R.id.gridViewBibliotecaGeneral);
        EditText buscador = (EditText) getView().findViewById(R.id.editTextBuscadorGeneralBiblioteca);
        AutoCompleteTextView filtros = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextViewFiltrosBiblioteca);
        AutoCompleteTextView buscarPor = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextViewBuscarPorBiblioteca);
        switchOrdenarPor = (Switch) getView().findViewById(R.id.switchCriterioOrdenLibros);

        ArrayAdapter<String> adapterFiltros = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, InfoAudiolibros.getGeneros());
        filtros.setText("Todos");
        generoLibrosMostrados = "Todos";
        filtros.setAdapter(adapterFiltros);

        categoriasBuscarPor = new ArrayList<String>();
        categoriasBuscarPor.add("Título");
        categoriasBuscarPor.add("Autor");
        categoriasBuscarPor.add("Tags");
        ArrayAdapter<String> adapterBuscarPor = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoriasBuscarPor);
        buscarPor.setText("Título");
        buscarPorActual = "Título";
        buscarPor.setAdapter(adapterBuscarPor);

        if (InfoAudiolibros.getTodosLosAudiolibros() != null) {
            inicializarAdaptadorBiblioteca();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                peticionAudiolibrosId(position);
            }
        });

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((FragmentBiblioteca.this).adaptadorActual != null) {
                    if (buscarPorActual.equals("Autor")) {
                        BibliotecaAutorGridAdapter tempAdapter = (BibliotecaAutorGridAdapter) (FragmentBiblioteca.this).adaptadorActual;
                        tempAdapter.getFilter().filter(charSequence);
                    } else if(buscarPorActual.equals("Tags")) {
                        BibliotecaTagsGridAdapter tempAdapter = (BibliotecaTagsGridAdapter) (FragmentBiblioteca.this).adaptadorActual;
                        tempAdapter.getFilter().filter(charSequence);
                    } else { // buscarPorActual.equals("Título")
                        BibliotecaTituloGridAdapter tempAdapter = (BibliotecaTituloGridAdapter) (FragmentBiblioteca.this).adaptadorActual;
                        tempAdapter.getFilter().filter(charSequence);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        filtros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                generoLibrosMostrados = InfoAudiolibros.getGeneros()[position];
                inicializarAdaptadorBiblioteca();
            }
        });

        buscarPor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                buscarPorActual = categoriasBuscarPor.get(position);
                inicializarAdaptadorBiblioteca();
            }
        });

        switchOrdenarPor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicializarAdaptadorBiblioteca();
            }
        });
    }

    private void peticionAudiolibrosId(int position) {
        AudiolibroItem audiolibro = (AudiolibroItem) adaptadorActual.getItem(position);

        Call<AudiolibroEspecificoResponse> llamada = retrofitInterface.ejecutarAudiolibrosId(ApiClient.getUserCookie(), audiolibro.getId());
        llamada.enqueue(new Callback<AudiolibroEspecificoResponse>() {
            @Override
            public void onResponse(@NonNull Call<AudiolibroEspecificoResponse> call, @NonNull Response<AudiolibroEspecificoResponse> response) {
                int codigo = response.code();

                if (codigo == 200) {
                    InfoAudiolibros.setAudiolibroActual(response.body());
                    abrirInfoLibro();
                } else if (codigo == 409) {
                    Toast.makeText(getContext(), "No hay ningún audiolibro con ese ID (bilbio)", Toast.LENGTH_LONG).show();
                } else if (codigo == 500) {
                    Toast.makeText(getContext(), "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Error desconocido (AudiolibrosId): " + codigo, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AudiolibroEspecificoResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void inicializarAdaptadorBiblioteca() {
        ArrayList<AudiolibroItem> libros = InfoAudiolibros.getAudiolibrosPorGenero(generoLibrosMostrados);

        if (switchOrdenarPor.isChecked()) {
            Collections.sort(libros, new Comparator<AudiolibroItem>() {
                @Override
                public int compare(AudiolibroItem a1, AudiolibroItem a2) {
                    int res = 0;
                    if (a1.getPuntuacion() < a2.getPuntuacion()) {
                        res = 1;
                    } else if (a1.getPuntuacion() > a2.getPuntuacion()){
                        res = -1;
                    }
                    return res;
                }
            });
        } else {
            Collections.sort(libros, new Comparator<AudiolibroItem>() {
                @Override
                public int compare(AudiolibroItem a1, AudiolibroItem a2) {
                    return a1.getTitulo().compareToIgnoreCase(a2.getTitulo());
                }
            });
        }

        if (buscarPorActual.equals("Autor")) {
            adaptadorActual = new BibliotecaAutorGridAdapter(getContext(), libros);
        } else if (buscarPorActual.equals("Tags")) {
            adaptadorActual = new BibliotecaTagsGridAdapter(getContext(), libros);
        } else { // buscarPorActual.equals("Título")
            adaptadorActual = new BibliotecaTituloGridAdapter(getContext(), libros);
        }

        gridView.setAdapter(adaptadorActual);
    }

    private void abrirInfoLibro() {
        Intent intent = new Intent(getContext(), InfoLibroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }
}
