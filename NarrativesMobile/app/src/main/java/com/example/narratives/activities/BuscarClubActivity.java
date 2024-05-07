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
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.AudiolibroAdapter;
import com.example.narratives.adaptadores.ClubAdapter;
import com.example.narratives.fragments.FragmentClubs;
import com.example.narratives.informacion.InfoClubes;
import com.example.narratives.peticiones.clubes.Club;
import com.example.narratives.peticiones.clubes.ClubResult;
import com.example.narratives.peticiones.clubes.ClubesResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BuscarClubActivity extends AppCompatActivity {
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    EditText searcher;
    RecyclerView recycler;
    FloatingActionButton fabBack;
    private ClubAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_club);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        searcher = findViewById(R.id.editText_BuscarClub);
        recycler = findViewById(R.id.recycler_BuscarClub);
        fabBack = findViewById(R.id.botonVolverDesdeBuscarClub);

        getClubes();

        searcher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (BuscarClubActivity.this).mAdapter.getFilter().filter(charSequence);
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
    }

    private void cancelActivity() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("requestCode", FragmentClubs.CREAR_CLUB);
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    private void getClubes() {
        Call<ClubesResult> llamada = retrofitInterface.ejecutarBuscarClubes(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<ClubesResult>() {
            @Override
            public void onResponse(Call<ClubesResult> call, Response<ClubesResult> response) {
                if (response.code() == 200) {
                    ArrayList<Club> clubList = response.body().getClubes();
                    recycler.setLayoutManager(new LinearLayoutManager(BuscarClubActivity.this));
                    mAdapter = new ClubAdapter(BuscarClubActivity.this, R.layout.item_club, clubList);
                    recycler.setAdapter(mAdapter);
                } else if (response.code() == 500) {
                    Toast.makeText(BuscarClubActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(BuscarClubActivity.this, "Código error " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ClubesResult> call, Throwable t) {
                // Maneja la falla de la solicitud aquí
                Toast.makeText(BuscarClubActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        cancelActivity();
        super.onBackPressed();
    }
}
