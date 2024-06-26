package com.example.narratives.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.clubes.BuscarClubActivity;
import com.example.narratives.activities.clubes.CrearClubActivity;
import com.example.narratives.activities.clubes.InfoClubActivity;
import com.example.narratives.adaptadores.ClubAdapter;
import com.example.narratives.informacion.InfoClubes;
import com.example.narratives.peticiones.clubes.Club;
import com.example.narratives.peticiones.clubes.ClubesResult;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentClubs extends Fragment {
    public final static int CHAT_CLUB = 0;
    public final static int CREAR_CLUB = 1;
    public final static int BUSCAR_CLUB = 2;
    public final static int INFO_CLUB = 3;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    RecyclerView mClubes;
    ClubAdapter mAdapter;
    FloatingActionButton addClub;
    ExtendedFloatingActionButton newClub;
    ExtendedFloatingActionButton joinClub;
    private boolean fabs_visible;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clubs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (InfoClubes.getTodosLosClubes() == null) {
            // Petición de clubes
            retrofit = ApiClient.getRetrofit();
            retrofitInterface = ApiClient.getRetrofitInterface();
            obtenerMyClubes(view);
        } else {
            setupRecyclerView(view);
        }

        addClub = view.findViewById(R.id.fab_addClub);
        newClub = view.findViewById(R.id.fab_newClub);
        joinClub = view.findViewById(R.id.fab_joinClub);
        fabs_visible = false;
        newClub.setVisibility(View.GONE);
        joinClub.setVisibility(View.GONE);

        addClub.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fabs_visible) {
                            fabs_visible = false;
                            newClub.setVisibility(View.GONE);
                            joinClub.setVisibility(View.GONE);
                        } else {
                            fabs_visible = true;
                            newClub.setVisibility(View.VISIBLE);
                            joinClub.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        newClub.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fabs_visible = false;
                        newClub.setVisibility(View.GONE);
                        joinClub.setVisibility(View.GONE);
                        startCrearClubActivity();
                    }
                }
        );

        joinClub.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fabs_visible = false;
                        newClub.setVisibility(View.GONE);
                        joinClub.setVisibility(View.GONE);
                        startBuscarClubActivity();
                    }
                }
        );


    }

    private void setupRecyclerView(View view) {
        mClubes = view.findViewById(R.id.listViewMyClubes);
        mClubes.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ClubAdapter(getContext(), this, R.layout.item_club, InfoClubes.getTodosLosClubes());
        mClubes.setAdapter(mAdapter);
    }


    private void obtenerMyClubes(View view){
        Call<ClubesResult> llamada = retrofitInterface.ejecutarMyClubes(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<ClubesResult>() {
            @Override
            public void onResponse(Call<ClubesResult> call, Response<ClubesResult> response) {
                int codigo = response.code();

                if (codigo == 200){
                    ArrayList<Club> clubesResult = response.body().getClubes();

                    if(clubesResult == null){
                        //Texto no hay clubes
                    } else {
                        InfoClubes.setTodosLosClubes(clubesResult);
                        setupRecyclerView(view);
                    }

                } else if (codigo == 500){
                    Toast.makeText(getActivity(), "Error del servidor",
                            Toast.LENGTH_LONG).show();

                } else if (codigo == 404){
                    Toast.makeText(getActivity(), "Error 404 /audiolibros", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error desconocido (audiolibros): " + String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ClubesResult> call, Throwable t) {
                Toast.makeText(getActivity(), "No se ha conectado con el servidor (audiolibros)",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(getActivity(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHAT_CLUB) {
                if (data.getBooleanExtra("update", true)) {
                    mAdapter.notifyDataSetChanged();
                }
            } else if (requestCode == CREAR_CLUB) {
                mAdapter.notifyDataSetChanged();
            } else if (requestCode == BUSCAR_CLUB) {
                if (data.getBooleanExtra("update", true)) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /*private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int requestCode = result.getData().getIntExtra("requestCode", -1);
                if (requestCode == CHAT_CLUB) {
                    if (result.getData().getBooleanExtra("update", true)){
                        mAdapter.notifyDataSetChanged();
                    }
                } else if (requestCode == CREAR_CLUB) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mAdapter.notifyDataSetChanged();
                    }
                } else if (requestCode == BUSCAR_CLUB) {
                    if (result.getData().getBooleanExtra("update", true)){
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });*/
    private void startCrearClubActivity() {
        Context context = requireContext();
        Intent intent = new Intent(context, CrearClubActivity.class);
        startActivityForResult(intent, FragmentClubs.CREAR_CLUB);
    }

    private void startBuscarClubActivity() {
        Context context = requireContext();
        Intent intent = new Intent(context, BuscarClubActivity.class);
        startActivityForResult(intent, FragmentClubs.BUSCAR_CLUB);
    }
}