package com.example.narratives.activities.clubes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionSet;
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
import com.example.narratives.fragments.FragmentClubs;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.informacion.InfoClubes;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;
import com.example.narratives.peticiones.clubes.Club;
import com.example.narratives.peticiones.clubes.ClubRequest;
import com.example.narratives.peticiones.clubes.ClubResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CrearClubActivity extends AppCompatActivity {
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    EditText editTextClubName;
    EditText editTextClubDesc;
    private EditText etSearch;
    private Spinner spinner;
    private AudiolibroAdapter mAdapter;
    FloatingActionButton fabBack;
    Button buttCreate;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_club);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        editTextClubName = findViewById(R.id.editTextNombreClub);
        editTextClubDesc = findViewById(R.id.editTextDescClub);
        fabBack = findViewById(R.id.botonVolverDesdeCrearClub);
        buttCreate = findViewById(R.id.botonConfirmarCrearClub);
        etSearch = findViewById(R.id.et_search);
        spinner = findViewById(R.id.spinner);

        ArrayList<AudiolibroItem> audiolibros = new ArrayList<>();
        AudiolibroItem ninguno = new AudiolibroItem(0, getString(R.string.ninguno));
        audiolibros.add(ninguno);
        audiolibros.addAll(InfoAudiolibros.getTodosLosAudiolibros());

        mAdapter = new AudiolibroAdapter(CrearClubActivity.this, audiolibros);
        spinner.setAdapter(mAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (CrearClubActivity.this).mAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelActivity();
            }
        });

        buttCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarYEnviarDatos();
            }
        });
    }
    private void cancelActivity() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("requestCode", FragmentClubs.CREAR_CLUB);
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    private void comprobarYEnviarDatos() {
        String clubName = editTextClubName.getText().toString().trim();
        String clubDesc = editTextClubDesc.getText().toString().trim();
        AudiolibroItem audiolibro = (AudiolibroItem) spinner.getSelectedItem();

        if (clubName.isEmpty()) {
            Toast.makeText(CrearClubActivity.this, R.string.missing_club_name, Toast.LENGTH_LONG);
        } else {
            ClubRequest request = new ClubRequest(clubName, null, null);
            if (!clubDesc.isEmpty()) {
                request.setClubDesc(clubDesc);
            }
            if (audiolibro.getId() > 0) {
                request.setAudiolibroId(audiolibro.getId());
            }

            Call<ClubResult> llamada = retrofitInterface.ejecutarCreateClub(ApiClient.getUserCookie(), request);
            llamada.enqueue(new Callback<ClubResult>() {
                @Override
                public void onResponse(Call<ClubResult> call, Response<ClubResult> response) {
                    if (response.code() == 200) {
                        Club club = response.body().getClub();
                        club.setIsAdmin(true);
                        InfoClubes.addClub(club);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("requestCode", FragmentClubs.CREAR_CLUB);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();

                    } else if (response.code() == 500) {
                        Toast.makeText(CrearClubActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(CrearClubActivity.this, "Código error " + String.valueOf(response.code()),
                                Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ClubResult> call, Throwable t) {
                    // Maneja la falla de la solicitud aquí
                    Toast.makeText(CrearClubActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        cancelActivity();
        super.onBackPressed();
    }
}
