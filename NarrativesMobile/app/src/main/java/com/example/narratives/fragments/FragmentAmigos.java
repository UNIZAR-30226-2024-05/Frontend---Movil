package com.example.narratives.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.narratives.R;
import com.example.narratives.adaptadores.UserAdapter;
import com.example.narratives.peticiones.users.login.LoginUser;

import java.util.ArrayList;


public class FragmentAmigos extends Fragment {

    ListView listaAmigos;
    UserAdapter userAdapter;

    EditText buscador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amigos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        listaAmigos = (ListView) getView().findViewById(R.id.listViewListaAmigos);
        buscador = (EditText) getView().findViewById(R.id.editTextBuscadorListaAmigos);

        String[] nombres = {"Manu", "Ismael", "Pablo", "Alicia", "Luis", "Lilai", "Curro", "Dani", "paula", "pepe", "María", "victor", "Jaume", "David", "Alba", "cristina"};

        ArrayList<LoginUser> usuarios = new ArrayList<LoginUser>();
        for (int i = 0; i < nombres.length; i++){
            String role;
            if(i % 2 == 0) {
                role = "normal";
            } else {
                role = "especial";
            }
            usuarios.add(new LoginUser(nombres[i], role));
        }

        userAdapter = new UserAdapter(getContext(),R.layout.item_usuario_prueba,usuarios);
        listaAmigos.setAdapter(userAdapter);

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (FragmentAmigos.this).userAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}