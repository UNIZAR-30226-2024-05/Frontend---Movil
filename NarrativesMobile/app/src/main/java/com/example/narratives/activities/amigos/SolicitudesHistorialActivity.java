package com.example.narratives.activities.amigos;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.main.MainActivity;
import com.example.narratives.adaptadores.HistorialPeticionAdapter;
import com.example.narratives.informacion.InfoPeticiones;
import com.example.narratives.peticiones.amistad.solicitudes.AmistadPeticionesResponse;
import com.example.narratives.peticiones.amistad.solicitudes.HistorialPeticionConTipo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitudesHistorialActivity extends AppCompatActivity {

    HistorialPeticionAdapter peticionesAdapter;
    ArrayList<HistorialPeticionConTipo> listaPeticiones;
    ListView peticiones;

    RetrofitInterface retrofitInterface;
    FloatingActionButton volverAMain;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.amigos_historial);
        super.onCreate(savedInstanceState);

        peticiones = (ListView) findViewById(R.id.listViewListaHistorial);
        retrofitInterface = ApiClient.getRetrofitInterface();

        volverAMain = (FloatingActionButton) findViewById(R.id.botonVolverDesdeHistorialAmigos);
        volverAMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMain();
            }
        });

        peticionAmistadPeticiones();
    }


    private void cargarAdaptador() {
        listaPeticiones = InfoPeticiones.getPeticionesOrdenadas();
        if(listaPeticiones == null){ new ArrayList<HistorialPeticionConTipo>();}

        peticionesAdapter = new HistorialPeticionAdapter(this,R.layout.item_lista_historial_peticiones, listaPeticiones);
        peticiones.setAdapter(peticionesAdapter);
    }

    private void peticionAmistadPeticiones() {
        Call<AmistadPeticionesResponse> llamada = retrofitInterface.ejecutarAmistadPeticiones(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<AmistadPeticionesResponse>() {
            @Override
            public void onResponse(Call<AmistadPeticionesResponse> call, Response<AmistadPeticionesResponse> response) {

                if(response.code() == 200) {
                    InfoPeticiones.setPeticiones(response.body());
                    cargarAdaptador();

                }  else if (response.code() == 500){
                    Toast.makeText(SolicitudesHistorialActivity.this, "Error del server (historial)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(SolicitudesHistorialActivity.this, "Código de error (amigosSend): " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AmistadPeticionesResponse> call, Throwable t) {
                Toast.makeText(SolicitudesHistorialActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();
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