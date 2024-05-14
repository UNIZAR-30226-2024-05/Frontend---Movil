package com.example.narratives.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
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
import com.example.narratives.adaptadores.SelectorCapitulosAdapter;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.audiolibros.especifico.Capitulo;
import com.example.narratives.peticiones.marcapaginas.CrearMarcapaginasRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditMarcapaginasActivity extends AppCompatActivity {
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
    Button buttUpdate;
    Button buttDelete;
    ArrayList<Capitulo> listaCapitulos;
    private int capituloActual;

    private int id;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());
        listaCapitulos = (ArrayList<Capitulo>) getIntent().getSerializableExtra("listaCapitulos");
        capituloActual = getIntent().getIntExtra("capituloActual", 1);
        id =getIntent().getIntExtra("IdMarcapaginas", 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_marcapaginas);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        editTextMarcapaginasName = findViewById(R.id.editTextNombreMarcapaginas);
        editTextMarcapaginasName.setText(getIntent().getStringExtra("nombreMarcapaginas"));

        editTextMarcapaginasTimeSecond = findViewById(R.id.editTextSecond);
        editTextMarcapaginasTimeSecond.setText(getIntent().getStringExtra("timestamp").substring(6,8));
        editTextMarcapaginasTimeMinute  = findViewById(R.id.editTextMinute);
        editTextMarcapaginasTimeMinute.setText(getIntent().getStringExtra("timestamp").substring(3,5));
        editTextMarcapaginasTimeHour = findViewById(R.id.editTextHour);
        editTextMarcapaginasTimeHour.setText(getIntent().getStringExtra("timestamp").substring(0,2));


        fabBack = findViewById(R.id.botonVolverDesdeCrearMarcapaginas);
        buttUpdate = findViewById(R.id.botonConfirmarModificarMarcapaginas);
        buttDelete = findViewById(R.id.botonConfirmarEliminarMarcapaginas);
        spinner = findViewById(R.id.spinner);

        mAdapter = new SelectorCapitulosAdapter(EditMarcapaginasActivity.this, listaCapitulos);
        spinner.setAdapter(mAdapter);
        int posi = capituloActual-listaCapitulos.get(0).getId();
        spinner.setSelection(posi);


        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    peticionModificarMarcapaginas();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peticionBorrarMarcapaginas();
            }
        });
    }

    private void peticionModificarMarcapaginas() throws IOException {
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
            Toast.makeText(EditMarcapaginasActivity.this, "El nombre del marcapaginas no puede estar vacio", Toast.LENGTH_LONG).show();;
        }else if(Integer.parseInt(Hour)*3600 + Integer.parseInt(Minute)*60 + Integer.parseInt(Second) >= duracion){
            Toast.makeText(EditMarcapaginasActivity.this, "El timestamp del marcapaginas no puede ser mayor a la duración del capitulo", Toast.LENGTH_LONG).show();;
        } else if(Integer.parseInt(Second) < 0 || Integer.parseInt(Second) >59){
            Toast.makeText(EditMarcapaginasActivity.this, "El valor de segundos debe estar entre 0 y 59", Toast.LENGTH_LONG).show();;
        } else if(Integer.parseInt(Minute) < 0 || Integer.parseInt(Minute) >59){
            Toast.makeText(EditMarcapaginasActivity.this, "El valor de minuto debe estar entre 0 y 59", Toast.LENGTH_LONG).show();;
        } else if(Integer.parseInt(Hour) < 0 || Integer.parseInt(Hour) >23){
            Toast.makeText(EditMarcapaginasActivity.this, "El valor de hora debe estar entre 0 y 23", Toast.LENGTH_LONG).show();;
        } else {
            String marcapaginasTime = Hour+ ":" + Minute + ":" +Second;
            Log.d("Comprobacion", "peticionCrearMarcapaginas: " + marcapaginasTime);
            CrearMarcapaginasRequest request = new CrearMarcapaginasRequest();

            request.setTitulo(marcapaginasName);
            request.setTiempo(marcapaginasTime);
            request.setCapitulo((select.getId()));
            request.setMarcapaginasID(id);
            //request.setCapitulo(listaCapitulos.get(capituloActual).getId()); //esto si fuera solo el cap reproduciendose
                Log.d("Prueba", "peticionCrearMarcapaginas: " + request.getCapitulo() + " y " + request.getTiempo() + " y " + request.getMarcapaginasID());

            Call<GenericMessageResult> llamada = retrofitInterface.ejecutarUpdateMarcapaginas(ApiClient.getUserCookie(), request);
            llamada.enqueue(new Callback<GenericMessageResult>() {
                @Override
                public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {
                    if (response.code() == 200) {
                        Toast.makeText(EditMarcapaginasActivity.this, "Marcapaginas editado. Salga del audiolibro para aplicar los cambios", Toast.LENGTH_LONG).show();
                        finish();

                    } else if (response.code() == 404) {
                        Toast.makeText(EditMarcapaginasActivity.this, "Estas intentando borrar un marcapaginas ya borrado", Toast.LENGTH_LONG).show();

                    }else if (response.code() == 500) {
                        Toast.makeText(EditMarcapaginasActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(EditMarcapaginasActivity.this, "Código error " + String.valueOf(response.code()),
                                Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                    // Maneja la falla de la solicitud aquí
                    Toast.makeText(EditMarcapaginasActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void peticionBorrarMarcapaginas() {

            Log.d("Comprobacion", "peticionCrearMarcapaginas: " + id);
            CrearMarcapaginasRequest request = new CrearMarcapaginasRequest();

            request.setMarcapaginasID(id);
            Log.d("Prueba", "peticionBorrarMarcapaginas: " + request.getMarcapaginasID());

            Call<GenericMessageResult> llamada = retrofitInterface.ejecutarDeleteMarcapaginas(ApiClient.getUserCookie(), request);
            llamada.enqueue(new Callback<GenericMessageResult>() {
                @Override
                public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {
                    if (response.code() == 200) {
                        Toast.makeText(EditMarcapaginasActivity.this, "Marcapaginas borrado. Salga del audiolibro para aplicar los cambios", Toast.LENGTH_LONG).show();
                        finish();

                    } else if (response.code() == 404) {
                        Toast.makeText(EditMarcapaginasActivity.this, "Estas intentando borrar un marcapaginas ya borrado", Toast.LENGTH_LONG).show();

                    }else if (response.code() == 500) {
                        Toast.makeText(EditMarcapaginasActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(EditMarcapaginasActivity.this, "Código error " + String.valueOf(response.code()),
                                Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                    // Maneja la falla de la solicitud aquí
                    Toast.makeText(EditMarcapaginasActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
