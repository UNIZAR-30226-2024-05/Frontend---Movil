package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.UsuarioEstadoRecibidasAdapter;
import com.example.narratives.informacion.InfoAmigos;
import com.example.narratives.peticiones.amistad.lista.UsuarioEstado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SolicitudesRecibidasActivity extends AppCompatActivity {
    UsuarioEstadoRecibidasAdapter usuariosAdapter;
    List<UsuarioEstado> listaRecibidos;
    EditText buscador;
    ListView usuarios;
    RetrofitInterface retrofitInterface;
    FloatingActionButton volverAMain;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.amigos_soli_recibidas);
        super.onCreate(savedInstanceState);

        retrofitInterface = ApiClient.getRetrofitInterface();
        usuarios = (ListView) findViewById(R.id.listViewListaSoliRecibidas);
        buscador = (EditText) findViewById(R.id.editTextBuscadorListaSoliRecibidas);

        volverAMain = (FloatingActionButton) findViewById(R.id.botonVolverDesdeSoliRecibidas);
        volverAMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMain();
            }
        });

        cargarAdaptador();
    }


    private void cargarAdaptador() {
        listaRecibidos = InfoAmigos.getUsuariosEstadoRecibidos();
        if(listaRecibidos == null){ new ArrayList<UsuarioEstado>();}

        usuariosAdapter = new UsuarioEstadoRecibidasAdapter(this,R.layout.item_lista_soli_recibidas, listaRecibidos);
        usuarios.setAdapter(usuariosAdapter);

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (SolicitudesRecibidasActivity.this).usuariosAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void abrirMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }
}