package com.example.narratives.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.AmigosAdapter;
import com.example.narratives.informacion.InfoAmigos;
import com.example.narratives.peticiones.users.amigos.AmigoSimple;
import com.example.narratives.peticiones.users.amigos.AmigosResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentAmigos extends Fragment {

    ListView listaAmigos;
    AmigosAdapter amigosAdapter;

    EditText buscador;

    ArrayList<AmigoSimple> amigos;

    RetrofitInterface retrofitInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amigos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        retrofitInterface = ApiClient.getRetrofitInterface();
        listaAmigos = (ListView) getView().findViewById(R.id.listViewListaAmigos);
        buscador = (EditText) getView().findViewById(R.id.editTextBuscadorListaAmigos);

        peticionAmigos();
    }

    private void peticionAmigos() {
        Call<AmigosResponse> llamada = retrofitInterface.ejecutarAmistadAmigos(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<AmigosResponse>() {
            @Override
            public void onResponse(Call<AmigosResponse> call, Response<AmigosResponse> response) {
                int codigo = response.code();

                if (codigo == 200){
                    InfoAmigos.setAmigos(response.body().getAmigos());

                    //TODO: quitar ejemplo
                    if(InfoAmigos.getAmigos() == null || InfoAmigos.getAmigos().size() == 0){
                        ArrayList<AmigoSimple> amigosEjemplo = new ArrayList<AmigoSimple>();
                        amigosEjemplo.add(new AmigoSimple(1, "Buri", 6));
                        amigosEjemplo.add(new AmigoSimple(2, "Jaume", 7));
                        amigosEjemplo.add(new AmigoSimple(3, "Manu", 5));
                        amigosEjemplo.add(new AmigoSimple(4, "Marti", 1));

                        InfoAmigos.setAmigos(amigosEjemplo);
                    }
                    amigos = InfoAmigos.getAmigos();
                    cargarAdaptador();

                } else if (codigo == 500){
                    Toast.makeText(getContext(), "Error del servidor",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error desconocido (amigos): " + String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AmigosResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor (amigos)",
                        Toast.LENGTH_LONG).show();

                //Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void cargarAdaptador() {
        amigosAdapter = new AmigosAdapter(getContext(),R.layout.item_lista_amigos,amigos);
        listaAmigos.setAdapter(amigosAdapter);

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (FragmentAmigos.this).amigosAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}