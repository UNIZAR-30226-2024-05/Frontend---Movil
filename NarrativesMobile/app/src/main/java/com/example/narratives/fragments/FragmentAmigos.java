package com.example.narratives.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.narratives.R;
import com.example.narratives.amigos.UserAdapter;
import com.example.narratives.peticiones.User;

import java.util.ArrayList;


public class FragmentAmigos extends Fragment {

    ListView listaAmigos;
    UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amigos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        listaAmigos = (ListView) getView().findViewById(R.id.listViewListaAmigos);

        ArrayList<User> usuarios = new ArrayList<User>();
        for (int i = 1; i <= 30; i++){
            String role;
            if(i % 2 == 0) {
                role = "normal";
            } else {
                role = "especial";
            }
            usuarios.add(new User("Usuario "+String.valueOf(i), role));
        }

        userAdapter = new UserAdapter(getContext(),R.layout.usuario_prueba,usuarios);
        listaAmigos.setAdapter(userAdapter);
    }
}