package com.example.narratives.fragments;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.InfoLibroActivity;
import com.example.narratives.activities.MainActivity;
import com.example.narratives.adaptadores.BibliotecaGridAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.especifico.Genero;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentBiblioteca extends Fragment {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    GridView gridView;
    BibliotecaGridAdapter bibliotecaGridAdapter;
    EditText buscador;
    AutoCompleteTextView filtros;
    ArrayAdapter<String> adapterFiltros;
    String generoLibrosMostrados;
    ArrayList<AudiolibroItem> audiolibros;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_biblioteca, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        retrofit = ApiClient.getRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();
        gridView = (GridView) getView().findViewById(R.id.gridViewBibliotecaGeneral);
        buscador = (EditText) getView().findViewById(R.id.editTextBuscadorGeneralBiblioteca);
        filtros = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextViewFiltrosBiblioteca);
        adapterFiltros = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, InfoAudiolibros.getGeneros());
        filtros.setText("Todos");
        generoLibrosMostrados = "todos";

        filtros.setAdapter(adapterFiltros);

        if(InfoAudiolibros.getTodosLosAudiolibros() != null){
            bibliotecaGridAdapter = new BibliotecaGridAdapter(getContext(), InfoAudiolibros.getTodosLosAudiolibros());
        } else {
            bibliotecaGridAdapter = new BibliotecaGridAdapter(getContext(), InfoAudiolibros.getTodosLosAudiolibrosEjemplo());
        }

        gridView.setAdapter(bibliotecaGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                peticionAudiolibrosId(position, id);
            }
        });

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (FragmentBiblioteca.this).bibliotecaGridAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void peticionAudiolibrosId(int position, long idGrid){
        AudiolibroItem audiolibro = (AudiolibroItem) bibliotecaGridAdapter.getItem(position);

        Call<AudiolibroEspecificoResponse> llamada = retrofitInterface.ejecutarAudiolibrosId(ApiClient.getUserCookie(), audiolibro.getId());
        llamada.enqueue(new Callback<AudiolibroEspecificoResponse>() {
            @Override
            public void onResponse(Call<AudiolibroEspecificoResponse> call, Response<AudiolibroEspecificoResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoLibroActivity.audiolibroActual = response.body();
                    abrirInfoLibro();

                } else if(codigo == 409) {
                    Toast.makeText(getContext(), "No hay ningún audiolibro con ese ID", Toast.LENGTH_LONG).show();

                } else if(codigo == 500) {
                    Toast.makeText(getContext(), "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Error desconocido (AudiolibrosId): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AudiolibroEspecificoResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirInfoLibro() {
        Intent intent = new Intent(getContext(), InfoLibroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }

    private void esconderTeclado() {
        if(getActivity().getCurrentFocus() != null){
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}