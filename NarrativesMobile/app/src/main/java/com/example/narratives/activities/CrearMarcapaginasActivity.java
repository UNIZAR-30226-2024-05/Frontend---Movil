package com.example.narratives.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.AudiolibroAdapter;
import com.example.narratives.adaptadores.SelectorCapitulosAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.audiolibros.especifico.Capitulo;
import com.example.narratives.peticiones.marcapaginas.CrearMarcapaginasRequest;
import com.example.narratives.peticiones.GenericMessageResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CrearMarcapaginasActivity extends AppCompatActivity {
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    EditText editTextMarcapaginasName;
    EditText editTextMarcapaginasTimeHour;
    EditText editTextMarcapaginasTimeMinute;
    EditText editTextMarcapaginasTimeSecond;
    private EditText etSearch;
    private Spinner spinner;
    private SelectorCapitulosAdapter mAdapter;
    private String Hour;
    private String Minute;
    private String Second;
    FloatingActionButton fabBack;
    Button buttCreate;
    ArrayList<Capitulo> listaCapitulos;
    int capituloActual;


    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());
        listaCapitulos = (ArrayList<Capitulo>) getIntent().getSerializableExtra("listaCapitulos");
        capituloActual = getIntent().getIntExtra("capituloActual", 1);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_marcapaginas);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        editTextMarcapaginasName = findViewById(R.id.editTextNombreMarcapaginas);

        editTextMarcapaginasTimeSecond = findViewById(R.id.editTextSecond);
        editTextMarcapaginasTimeSecond.setText(getIntent().getStringExtra("ReproductorSegundos"));
        editTextMarcapaginasTimeMinute  = findViewById(R.id.editTextMinute);
        editTextMarcapaginasTimeMinute.setText(getIntent().getStringExtra("ReproductorMinutos"));
        editTextMarcapaginasTimeHour = findViewById(R.id.editTextHour);
        editTextMarcapaginasTimeHour.setText(getIntent().getStringExtra("ReproductorHoras"));



        fabBack = findViewById(R.id.botonVolverDesdeCrearMarcapaginas);
        buttCreate = findViewById(R.id.botonConfirmarCrearMarcapaginas);
        spinner = findViewById(R.id.spinner);

        mAdapter = new SelectorCapitulosAdapter(CrearMarcapaginasActivity.this, listaCapitulos);
        spinner.setAdapter(mAdapter);
        spinner.setSelection(capituloActual);

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    peticionCrearMarcapaginas();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void peticionCrearMarcapaginas() throws IOException {
        String marcapaginasName = editTextMarcapaginasName.getText().toString().trim();
        Second = editTextMarcapaginasTimeSecond.getText().toString().trim();
        if(Second.equals("")||Second.isEmpty()){
            Second= "00";
        } else if(Integer.parseInt(Second) > 0 && Integer.parseInt(Second) <10){
            Second = "0" + Integer.parseInt(Second);
        }

        Minute = editTextMarcapaginasTimeMinute.getText().toString().trim();
        if(Minute.equals("")||Minute.isEmpty()){
            Minute= "00";
        } else if(Integer.parseInt(Minute) > 0 && Integer.parseInt(Minute) <10){
            Minute = "0" + Integer.parseInt(Minute);
        }

        Hour = editTextMarcapaginasTimeHour.getText().toString().trim();
        if(Hour.equals("")||Hour.isEmpty()){
            Hour= "00";
        } else if(Integer.parseInt(Hour) > 0 && Integer.parseInt(Hour) <10){
            Hour = "0" + Integer.parseInt(Hour);
        }

        Capitulo select = (Capitulo) spinner.getSelectedItem();

        //Habra que hablar si merece la pena, soluciona que pongan duraciones invalidas pero alarga bastante el proceso
        MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(select.getAudio());
            mediaPlayer.prepare();
            int duracion = (mediaPlayer.getDuration())/1000;
            mediaPlayer.release();

        Log.d("tiempo marcapaginas", "peticionCrearMarcapaginas(SS(MM/HH): " +Second +"/"+Minute+"/"+Hour);
        if (marcapaginasName.isEmpty()) {
            Toast.makeText(CrearMarcapaginasActivity.this, "El nombre del marcapaginas no puede estar vacio", Toast.LENGTH_LONG).show();;
        }else if(Integer.parseInt(Hour)*3600 + Integer.parseInt(Minute)*60 + Integer.parseInt(Second) >= duracion){
            Toast.makeText(CrearMarcapaginasActivity.this, "El timestamp del marcapaginas no puede ser mayor a la duración del capitulo", Toast.LENGTH_LONG).show();;
        } else if(Integer.parseInt(Second) < 0 || Integer.parseInt(Second) >59){
            Toast.makeText(CrearMarcapaginasActivity.this, "El valor de segundos debe estar entre 0 y 59", Toast.LENGTH_LONG).show();;
        } else if(Integer.parseInt(Minute) < 0 || Integer.parseInt(Minute) >59){
            Toast.makeText(CrearMarcapaginasActivity.this, "El valor de minuto debe estar entre 0 y 59", Toast.LENGTH_LONG).show();;
        } else if(Integer.parseInt(Hour) < 0 || Integer.parseInt(Hour) >23){
            Toast.makeText(CrearMarcapaginasActivity.this, "El valor de hora debe estar entre 0 y 23", Toast.LENGTH_LONG).show();;
        } else {
            String marcapaginasTime = Hour+ ":" + Minute + ":" +Second;
            Log.d("Comprobacion", "peticionCrearMarcapaginas: " + marcapaginasTime);
            CrearMarcapaginasRequest request = new CrearMarcapaginasRequest();
            request.setTitulo(marcapaginasName);
            if (!marcapaginasTime.isEmpty()) {
                request.setTiempo(marcapaginasTime);
            }
            request.setCapitulo((select.getId()));
            //request.setCapitulo(listaCapitulos.get(capituloActual).getId()); //esto si fuera solo el cap reproduciendose
                Log.d("Prueba", "peticionCrearMarcapaginas: " + request.getCapitulo() + " y " + request.getTiempo());

            Call<GenericMessageResult> llamada = retrofitInterface.ejecutarCreateMarcapaginas(ApiClient.getUserCookie(), request);
            llamada.enqueue(new Callback<GenericMessageResult>() {
                @Override
                public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {
                    if (response.code() == 200) {
                       // Marcapaginas marcapaginas = response.body().getMarcapaginas();
                        finish();

                    } else if (response.code() == 500) {
                        Toast.makeText(CrearMarcapaginasActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(CrearMarcapaginasActivity.this, "Código error " + String.valueOf(response.code()),
                                Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                    // Maneja la falla de la solicitud aquí
                    Toast.makeText(CrearMarcapaginasActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
