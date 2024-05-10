package com.example.narratives.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionSet;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.databinding.ActivityMainBinding;
import com.example.narratives.peticiones.LoginRequest;
import com.example.narratives.peticiones.LoginResult;
import com.example.narratives.peticiones.ReseniasRequest;
import com.example.narratives.resenias.ListAdapter;
import com.example.narratives.resenias.Resenia;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReseniaActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<Resenia> arrayListReseñas;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());


        retrofitInterface = ApiClient.getRetrofitInterface();

        setContentView(R.layout.pantalla_resenias);
        String[] nombres = {"Juan Pérez", "María Gutiérrez", "Carlos Martínez", "Ana López"};
        float[] valoraciones = {4.3F,5.0F,3.8F, 4.1F};
        String[] descripciones = {
                "Una obra maestra de la literatura latinoamericana que narra la historia de la familia Buendía en Macondo.",
                "Un cuento mágico sobre la amistad y la importancia de ver el mundo con los ojos del corazón.",
                "Una distopía que explora temas como el control gubernamental, la vigilancia y la libertad individual.",
                "Una clásica novela romántica que sigue las vidas y amores de las hermanas Bennet en la Inglaterra del siglo XIX."
        };
        arrayListReseñas = new ArrayList<>();

        for(int i=0; i<nombres.length;i++){
            Resenia reseña = new Resenia(nombres[i], descripciones[i], valoraciones[i]);
            arrayListReseñas.add(reseña);
        }

        ListView lv = findViewById(R.id.listViewListaReseñas);
        ListAdapter listAdapter = new ListAdapter(ReseniaActivity.this,arrayListReseñas);
        lv.setAdapter(listAdapter);
       // lv.setClickable(true);
       /* lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }
    ///RECOPILAR DATOS DE LA BASE DE DATOS------------------------------------------
    //-----------------------------------

    public void ponerDatos(){
        ListView lv = findViewById(R.id.listViewListaReseñas);

        ReseniasRequest request = new ReseniasRequest();
        //request.setUsername(usuarioEditText.getText().toString());
        //request.setPassword(passwordEditText.getText().toString());

        ListAdapter listAdapter = new ListAdapter(ReseniaActivity.this,arrayListReseñas);
        lv.setAdapter(listAdapter);

       /* Call<LoginResult> llamada = retrofitInterface.ejecutarInicioSesion(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.code() == 200) {
                    if(response.isSuccessful()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReseniaActivity.this, R.style.ExitoAlertDialogStyle);
                        builder.setMessage("Iniciando sesión...");
                        builder.show();
                        String cookie = response.headers().get("Set-Cookie");
                        ApiClient.setUserCookie(cookie);
                        new Handler().postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        abrirMenuMain();
                                    }
                                }
                                , 500);
                    }else{
                        Toast.makeText(LoginActivity.this, "Código correcto, pero sesión no exitosa", Toast.LENGTH_LONG).show();
                    }


                } else if (response.code() == 404){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.ErrorAlertDialogStyle);
                    builder.setTitle("ERROR");
                    builder.setMessage("Usuario no encontrado");
                    builder.show();

                } else if (response.code() == 401){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.ErrorAlertDialogStyle);
                    builder.setTitle("ERROR");
                    builder.setMessage("Contraseña incorrecta");
                    builder.show();

                } else if (response.code() == 500){
                    Toast.makeText(LoginActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(LoginActivity.this, "Código error " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Algo ha fallado",
                        Toast.LENGTH_LONG).show();
            }
        });*/
    }
}
