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
import com.example.narratives.informacion.MiPerfil;
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

                    if(viejo == null){
                        Toast.makeText(CambioFotoPerfilActivity.this, "Viejo nulo", Toast.LENGTH_LONG).show();
                    } else {
                        viejo.setPadding(0,0,0,0);
                    }

                    if(nuevo == null){
                        Toast.makeText(CambioFotoPerfilActivity.this, "Nuevo nulo", Toast.LENGTH_LONG).show();
                    } else {
                        nuevo.setPadding(10, 10, 10, 10);
                    }

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
        initialImageViewId = MainActivity.getMiPerfil().getPhotoImageView();
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
            String nuevaFoto = MiPerfil.getPhotoString(actualImageViewId);
            Toast.makeText(CambioFotoPerfilActivity.this, "Foto cambiada a: " + nuevaFoto, Toast.LENGTH_LONG).show();
            MainActivity.getMiPerfil().setPhoto(nuevaFoto);
            MainActivity.actualizarFotoPerfil();
            abrirMenuMain();

            /* TODO: Rellenar cuando backend lo implemente
            CambioContrasenaRequest request = new CambioContrasenaRequest();
            request.setOldPassword(editTextContrasenaAntigua.getText().toString());
            request.setNewPassword(editTextContrasenaNueva.getText().toString());

            Call<StandardMessageRequest> llamada = retrofitInterface.ejecutarCambioContrasena(ApiClient.getUserCookie(), request);
            llamada.enqueue(new Callback<StandardMessageRequest>() {
                @Override
                public void onResponse(Call<StandardMessageRequest> call, Response<StandardMessageRequest> response) {


                    if(response.code() == 200) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CambioFotoPerfilActivity.this, R.style.ExitoAlertDialogStyle);
                        builder.setTitle("ÉXITO");
                        builder.setMessage("Contraseña cambiada correctamente");

                        builder.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        abrirMenuMain();
                                    }
                                }

                                , 1000);

                    }  else if (response.code() == 401){
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String error = jObjError.getString("error");

                            if(error.equals("Incorrect password")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(CambioFotoPerfilActivity.this, R.style.ErrorAlertDialogStyle);
                                builder.setTitle("ERROR");
                                builder.setMessage("La contraseña es incorrecta");
                                builder.show();
                            } else {
                                Toast.makeText(CambioFotoPerfilActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(CambioFotoPerfilActivity.this, "Algo ha fallado obteniendo el error", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CambioFotoPerfilActivity.this, "Código de error no reconocido",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<StandardMessageRequest> call, Throwable t) {
                    Toast.makeText(CambioFotoPerfilActivity.this, "No se ha conectado con el servidor",
                            Toast.LENGTH_LONG).show();
                }
            });
            */
        }

    }

    public void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}
