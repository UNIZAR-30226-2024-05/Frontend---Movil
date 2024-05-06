package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.adaptadores.MenuInicioAdapter;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibrosResult;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeSinRegistroActivity extends AppCompatActivity {


    RecyclerView rvRecomendados, rvGenero1, rvGenero2, rvGenero3, rvGenero4, rvGenero5;
    MenuInicioAdapter adapterRecomendados, adapter1, adapter2, adapter3, adapter4, adapter5;
    RetrofitInterface retrofitInterface;

    CoordinatorLayout coordinatorLayout;
    ConstraintLayout cargandoNarrativesLayout;

    LinearLayout botonesLayout;
    ImageView logo;

    TextView textViewGenero1, textViewGenero2, textViewGenero3, textViewGenero4, textViewGenero5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.home_sin_registro);
        super.onCreate(savedInstanceState);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        cargandoNarrativesLayout = findViewById(R.id.constraintLayoutCargandoNarratives);
        retrofitInterface = ApiClient.getRetrofitInterface();
        logo = findViewById(R.id.imageViewLogoCargandoNarratives);
        botonesLayout = findViewById(R.id.linearLayoutBotones);

        rvRecomendados = findViewById(R.id.recyclerViewRecomendados);

        textViewGenero1 = findViewById(R.id.textViewGenero1);
        rvGenero1 = findViewById(R.id.recyclerViewGenero1);

        textViewGenero2 = findViewById(R.id.textViewGenero2);
        rvGenero2 = findViewById(R.id.recyclerViewGenero2);

        textViewGenero3 = findViewById(R.id.textViewGenero3);
        rvGenero3 = findViewById(R.id.recyclerViewGenero3);

        textViewGenero4 = findViewById(R.id.textViewGenero4);
        rvGenero4 = findViewById(R.id.recyclerViewGenero4);

        textViewGenero5 = findViewById(R.id.textViewGenero5);
        rvGenero5 = findViewById(R.id.recyclerViewGenero5);

        if(InfoAudiolibros.getTodosLosAudiolibrosEjemplo() == null){
            obtenerAudiolibrosEjemplo();
        }

        if(InfoAudiolibros.getTodosLosAudiolibros() == null){
            obtenerTodosLosAudiolibros();
        } else {
            mostrarMenuInicio(false);
        }

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

        ArrayList<AudiolibroItem> audiolibros = new ArrayList<>();
        for(int i = 0; i < titulos.length; i++){
            AudiolibroItem a = new AudiolibroItem(i, titulos[i], "autor", "descripcion", portadas[i], "genero", 5);
            audiolibros.add(a);
        }

        InfoAudiolibros.setTodosLosAudiolibrosEjemplo(audiolibros);
    }

    private void obtenerTodosLosAudiolibros(){
        Call<AudiolibrosResult> llamada = retrofitInterface.ejecutarAudiolibros(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<AudiolibrosResult>() {
            @Override
            public void onResponse(Call<AudiolibrosResult> call, Response<AudiolibrosResult> response) {
                int codigo = response.code();

                if (codigo == 200){
                    ArrayList<AudiolibroItem> audiolibrosResult = response.body().getAudiolibros();

                    if(audiolibrosResult == null){
                        Toast.makeText(HomeSinRegistroActivity.this, "Resultado de audiolibros nulo", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(getContext(), "TodosAudiolibros OK, size = " + String.valueOf(audiolibrosResult.size()), Toast.LENGTH_LONG).show();
                        InfoAudiolibros.setTodosLosAudiolibros(audiolibrosResult);
                        mostrarMenuInicio(true);
                    }

                } else if (codigo == 500){
                    Toast.makeText(HomeSinRegistroActivity.this, "Error del servidor",
                            Toast.LENGTH_LONG).show();

                } else if (codigo == 404){
                    Toast.makeText(HomeSinRegistroActivity.this, "Error 404 /audiolibros", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(HomeSinRegistroActivity.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(HomeSinRegistroActivity.this, "Error desconocido (audiolibros): " + String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AudiolibrosResult> call, Throwable t) {
                Toast.makeText(HomeSinRegistroActivity.this, "No se ha conectado con el servidor (audiolibros)",
                        Toast.LENGTH_LONG).show();

                //Toast.makeText(HomeSinRegistroActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarMenuInicio(boolean wait){
        cargarCarruselesConGeneros();

        if(wait) {
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            esconderCargandoNarratives();
                        }
                    }
                    , 350);
        } else {
            cargandoNarrativesLayout.setAlpha((float) 0);
        }
    }

    private void esconderCargandoNarratives() {

        Animation fadeOutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        fadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cargandoNarrativesLayout.setAlpha((float) 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        cargandoNarrativesLayout.startAnimation(fadeOutAnim);
    }

    private void cargarCarruselesConGeneros() {

        rvRecomendados.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvGenero1.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvGenero2.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvGenero3.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvGenero4.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvGenero5.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this, LinearLayoutManager.HORIZONTAL, false));

        if(InfoAudiolibros.getTodosLosAudiolibros() != null){
            String[] generos = InfoAudiolibros.getGenerosSeleccionados();

            adapterRecomendados = new MenuInicioAdapter(HomeSinRegistroActivity.this, InfoAudiolibros.getAudiolibrosRecomendados(10));
            rvRecomendados.setAdapter(adapterRecomendados);

            adapter1 = new MenuInicioAdapter(HomeSinRegistroActivity.this, InfoAudiolibros.getAudiolibrosPorGenero(generos[0]));
            rvGenero1.setAdapter(adapter1);
            textViewGenero1.setText(generos[0]);

            adapter2 = new MenuInicioAdapter(HomeSinRegistroActivity.this, InfoAudiolibros.getAudiolibrosPorGenero(generos[1]));
            rvGenero2.setAdapter(adapter2);
            textViewGenero2.setText(generos[1]);

            adapter3 = new MenuInicioAdapter(HomeSinRegistroActivity.this, InfoAudiolibros.getAudiolibrosPorGenero(generos[2]));
            rvGenero3.setAdapter(adapter3);
            textViewGenero3.setText(generos[2]);

            adapter4 = new MenuInicioAdapter(HomeSinRegistroActivity.this, InfoAudiolibros.getAudiolibrosPorGenero(generos[3]));
            rvGenero4.setAdapter(adapter4);
            textViewGenero4.setText(generos[3]);

            adapter5 = new MenuInicioAdapter(HomeSinRegistroActivity.this, InfoAudiolibros.getAudiolibrosPorGenero(generos[4]));
            rvGenero5.setAdapter(adapter5);
            textViewGenero5.setText(generos[4]);

        } else {
            adapterRecomendados = new MenuInicioAdapter(HomeSinRegistroActivity.this, InfoAudiolibros.getTodosLosAudiolibrosEjemplo());
            rvGenero1.setAdapter(adapterRecomendados);
            rvGenero2.setAdapter(adapterRecomendados);
            rvGenero3.setAdapter(adapterRecomendados);
            rvGenero4.setAdapter(adapterRecomendados);
            rvGenero5.setAdapter(adapterRecomendados);
        }
    }


    public void abrirMenuRegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void abrirMenuLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}


