package com.example.narratives.activities;

import android.app.Activity;
import android.content.Intent;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CrearMarcapaginasActivity extends AppCompatActivity {
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    EditText editTextMarcapaginasName;
    EditText editTextMarcapaginasTime;
    private EditText etSearch;
    private Spinner spinner;
    private SelectorCapitulosAdapter mAdapter;
    FloatingActionButton fabBack;
    Button buttCreate;
    ArrayList<Capitulo> listaCapitulos;
    int capituloActual;


    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());
        listaCapitulos = (ArrayList<Capitulo>) getIntent().getSerializableExtra("listaCapitulos");
        capituloActual = getIntent().getIntExtra("capituloActual", 1);

        if (listaCapitulos != null) {
            // Haz lo que necesites con la lista de capítulos recibida
        } else {
            // Maneja el caso en el que la lista de capítulos no se haya recibido correctamente
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_marcapaginas);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        editTextMarcapaginasName = findViewById(R.id.editTextNombreMarcapaginas);
        editTextMarcapaginasTime = findViewById(R.id.editTextDescMarcapaginas);
        fabBack = findViewById(R.id.botonVolverDesdeCrearMarcapaginas);
        buttCreate = findViewById(R.id.botonConfirmarCrearMarcapaginas);
        spinner = findViewById(R.id.spinner);

        mAdapter = new SelectorCapitulosAdapter(CrearMarcapaginasActivity.this, listaCapitulos);
        spinner.setAdapter(mAdapter);


        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peticionCrearMarcapaginas();
            }
        });
    }

    private void peticionCrearMarcapaginas() {
        String marcapaginasName = editTextMarcapaginasName.getText().toString().trim();
        String marcapaginasTime = editTextMarcapaginasTime.getText().toString().trim();
        //AudiolibroItem audiolibro = (AudiolibroItem) spinner.getSelectedItem();

        if (marcapaginasName.isEmpty()) {
            Toast.makeText(CrearMarcapaginasActivity.this, "El nombre del marcapaginas no puede estar vacio", Toast.LENGTH_LONG);
        } else {
            CrearMarcapaginasRequest request = new CrearMarcapaginasRequest();
            request.setTitulo(
                    marcapaginasName
            );
            if (!marcapaginasTime.isEmpty()) {
                request.setTiempo(marcapaginasTime);
            }

            request.setCapitulo(listaCapitulos.get(capituloActual).getId());
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
