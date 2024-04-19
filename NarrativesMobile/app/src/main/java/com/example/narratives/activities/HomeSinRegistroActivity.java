package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.menuprincipal.RecyclerViewInterface;
import com.example.narratives.menuprincipal.adaptador;
import com.example.narratives.peticiones.Audiolibro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeSinRegistroActivity extends AppCompatActivity implements RecyclerViewInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.homesinregistro);
        super.onCreate(savedInstanceState);

        obtenerAudiolibrosEjemplo();
        rv = findViewById(R.id.RecyclerViewRecomendados);
        rv.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        adaptador = new adaptador(this, HomeSinRegistroActivity.this, audiolibros);
        rv.setAdapter(adaptador);

        rvTerror = findViewById(R.id.RecyclerViewTerror1);
        rvTerror.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvTerror.setAdapter(adaptador);

        rvFantasia = findViewById(R.id.RecyclerFantasia1);
        rvFantasia.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvFantasia.setAdapter(adaptador);

        rvMitologia = findViewById(R.id.RecyclerMitologia1);
        rvMitologia.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvMitologia.setAdapter(adaptador);

        rvPoesia = findViewById(R.id.RecyclerPoesia1);
        rvPoesia.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvPoesia.setAdapter(adaptador);

        rvNovela = findViewById(R.id.RecyclerNovela1);
        rvNovela.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvNovela.setAdapter(adaptador);

        findViewById(R.id.botonIrLoginDesdeInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMenuLogin();
            }
        });

        findViewById(R.id.botonIrRegistroDesdeInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMenuRegistro();
            }
        });

    }

    public void abrirMenuRegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void abrirMenuLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
    RecyclerView rv, rvTerror, rvFantasia, rvMitologia, rvNovela, rvPoesia;
    com.example.narratives.menuprincipal.adaptador adaptador;
    private ArrayList<Audiolibro> audiolibros;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
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

        audiolibros = new ArrayList<>();
        for(int i = 0; i < titulos.length; i++){
            Audiolibro a = new Audiolibro(i, titulos[i], i, "descripcion", portadas[i]);
            audiolibros.add(a);
        }

        InfoAudiolibros.setTodosLosAudiolibros(audiolibros);
    }

    @Override
    public void onItemClick(int pos) {
        Toast.makeText(getApplicationContext(), "Se abre el libro", Toast.LENGTH_SHORT).show();
        View popupView = getLayoutInflater().inflate(R.layout.popup_info_libro, null);

        // Crea una instancia de PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Muestra el popup en el centro de la pantalla
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        }

    private void mostrarPopupInfoLibro(int position){
        esconderTeclado();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInfoLibro = inflater.inflate(R.layout.popup_info_libro, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        //PRUEBA, habrá que conseguir el libro según el género en 'generoLibrosMostrados'
       // Audiolibro audiolibro = (Audiolibro) bibliotecaGridAdapter.getItem(position);

        ImageView imageViewPortada = viewInfoLibro.findViewById(R.id.imageViewPortadaInfoLibro);
        Glide
                .with(getContext())
                .load(audiolibro.getImg())
                .centerCrop()
                .placeholder(R.drawable.icono_imagen_estandar_foreground)
                .into(imageViewPortada);

        TextView textViewTitulo = viewInfoLibro.findViewById(R.id.textViewTituloInfoLibro);
        textViewTitulo.setText(audiolibro.getTitulo());


        PopupWindow popupWindow=new PopupWindow(viewInfoLibro,width,height, true);
        popupWindow.setAnimationStyle(0);

        FrameLayout layout = findViewById(R.id.main_layout);
        layout.post(new Runnable(){
            @Override
            public void run(){
                popupWindow.showAtLocation(layout, Gravity.BOTTOM,0,0);
            }
        });


        FloatingActionButton botonCerrar = (FloatingActionButton) viewInfoLibro.findViewById(R.id.botonVolverDesdeInfoLibro);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    private void esconderTeclado() {
        if(getCurrentFocus() != null){
            InputMethodManager inputManager = (InputMethodManager).getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}


