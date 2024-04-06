package com.example.narratives.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.biblioteca.BibliotecaGridAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.Audiolibro;
import com.example.narratives.peticiones.AudiolibrosResult;

import org.json.JSONObject;

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

        obtenerAudiolibrosEjemplo();
        //obtenerTodosLosAudiolibros();
        bibliotecaGridAdapter = new BibliotecaGridAdapter(getContext(), InfoAudiolibros.getTodosLosAudiolibros());
        gridView.setAdapter(bibliotecaGridAdapter);

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


    private void obtenerTodosLosAudiolibros(){
        Call<AudiolibrosResult> llamada = retrofitInterface.ejecutarObtencionGeneralAudiolibros(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<AudiolibrosResult>() {
            @Override
            public void onResponse(Call<AudiolibrosResult> call, Response<AudiolibrosResult> response) {
                int codigo = response.code();

                if (response.code() == 200){
                    ArrayList<Audiolibro> audiolibrosResult = response.body().getAudiolibros();

                    if(audiolibrosResult == null){
                        Toast.makeText(getContext(), "Resultado de audiolibros nulo", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Audiolibros recibidos OK", Toast.LENGTH_LONG).show();
                        InfoAudiolibros.setTodosLosAudiolibros(audiolibrosResult);
                    }

                } else if (response.code() == 500){
                    Toast.makeText(getContext(), "Error del servidor",
                            Toast.LENGTH_LONG).show();

                } else if (response.code() == 404){
                    //Toast.makeText(getContext(), "Error 404 /audiolibros", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Algo ha fallado obteniendo el error " + String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AudiolibrosResult> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void obtenerAudiolibrosEjemplo(){
        String[] portadas = {
                "https://narrativesarchivos.blob.core.windows.net/imagenes/ElCoco.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/ElHombreDelTrajeNegro.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/ElUmbralDeLaNoche.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/HarryPotter1.jpeg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/LaOdisea.png",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/OrgulloYPrejuicio.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/Popsy.jpg"
        };

        String[] titulos = {
                "El coco",
                "El hombre de traje negro",
                "El umbral de la noche",
                "Harry Potter y la piedra filosofal",
                "La odisea",
                "Orgullo y prejuicio",
                "Popsy"
        };

        ArrayList<Audiolibro> audiolibros = new ArrayList<>();
        for(int i = 0; i < titulos.length; i++){
            Audiolibro a = new Audiolibro(i, titulos[i], i, "descripcion", portadas[i]);
            audiolibros.add(a);
        }

        InfoAudiolibros.setTodosLosAudiolibros(audiolibros);

    }

}