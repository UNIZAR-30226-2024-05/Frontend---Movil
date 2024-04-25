package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.informacion.InfoMiPerfil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Retrofit;

public class CambioFotoPerfilActivity extends AppCompatActivity {

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    private int actualImageViewId;
    private int initialImageViewId;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.cambio_foto_perfil);
        super.onCreate(savedInstanceState);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        inicializarFotoActual();

        Button botonConfirmar = (Button) findViewById(R.id.botonConfirmarCambioFotoPerfil);
        botonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarFoto();
            }
        });

        FloatingActionButton botonAtras = (FloatingActionButton) findViewById(R.id.botonVolverDesdeCambioFotoPerfil);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMenuMain();
            }
        });



        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newImageViewId = view.getId();

                ImageView viejo = (ImageView) findViewById(actualImageViewId);
                ImageView nuevo = (ImageView) findViewById(newImageViewId);

                if(viejo != nuevo){

                    if(viejo != null) viejo.setPadding(0,0,0,0);

                    if(nuevo != null) nuevo.setPadding(10, 10, 10, 10);

                    actualImageViewId = newImageViewId;
                }
            }
        };

        ImageView gato = (ImageView) findViewById(R.id.imageViewFotoPerfilGato);
        gato.setOnClickListener(onClickListener);
        ImageView perro = (ImageView) findViewById(R.id.imageViewFotoPerfilPerro);
        perro.setOnClickListener(onClickListener);
        ImageView rana = (ImageView) findViewById(R.id.imageViewFotoPerfilRana);
        rana.setOnClickListener(onClickListener);
        ImageView leon = (ImageView) findViewById(R.id.imageViewFotoPerfilLeon);
        leon.setOnClickListener(onClickListener);
        ImageView pollo = (ImageView) findViewById(R.id.imageViewFotoPerfilPollo);
        pollo.setOnClickListener(onClickListener);
        ImageView vaca = (ImageView) findViewById(R.id.imageViewFotoPerfilVaca);
        vaca.setOnClickListener(onClickListener);
        ImageView buho = (ImageView) findViewById(R.id.imageViewFotoPerfilBuho);
        buho.setOnClickListener(onClickListener);
        ImageView perezoso = (ImageView) findViewById(R.id.imageViewFotoPerfilPerezoso);
        perezoso.setOnClickListener(onClickListener);
        ImageView doraemon = (ImageView) findViewById(R.id.imageViewFotoPerfilDoraemon);
        doraemon.setOnClickListener(onClickListener);
        ImageView pikachu = (ImageView) findViewById(R.id.imageViewFotoPerfilPikachu);
        pikachu.setOnClickListener(onClickListener);

    }

    private void inicializarFotoActual(){
        initialImageViewId = MainActivity.getMiPerfil().getImgImageView();
        actualImageViewId = initialImageViewId;

        ImageView inicial = (ImageView) findViewById(actualImageViewId);

        if(inicial == null){
            Toast.makeText(CambioFotoPerfilActivity.this, Integer.toString(actualImageViewId), Toast.LENGTH_LONG).show();
        } else {
            inicial.setPadding(10, 10, 10, 10);
        }
    }

    private void cambiarFoto() {
        if(initialImageViewId == actualImageViewId){
            Toast.makeText(CambioFotoPerfilActivity.this, "Elige una foto distinta a la actual", Toast.LENGTH_LONG).show();
        } else {
            String nuevaFoto = InfoMiPerfil.getImgString(actualImageViewId);
            MainActivity.getMiPerfil().setImg(nuevaFoto);
            MainActivity.actualizarFotoPerfil();

            MainActivity.getPeticiones().peticionUsersChange_img(this, nuevaFoto);
        }
    }

    public void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}
