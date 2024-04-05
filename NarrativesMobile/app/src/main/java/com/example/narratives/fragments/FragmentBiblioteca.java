package com.example.narratives.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.Audiolibro;
import com.example.narratives.peticiones.AudiolibrosResult;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentBiblioteca extends Fragment {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_biblioteca, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        retrofit = ApiClient.getRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();




    }


    private void obtenerTodosLosAudiolibros(){
        Call<AudiolibrosResult> llamada = retrofitInterface.ejecutarObtencionGeneralAudiolibros(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<AudiolibrosResult>() {
            @Override
            public void onResponse(Call<AudiolibrosResult> call, Response<AudiolibrosResult> response) {
                int codigo = response.code();

                if (response.code() == 200){
                    ArrayList<Audiolibro> audiolibrosResult = response.body().getAudiolibros();

                    if(audiolibrosResult == null){
                        Toast.makeText(getContext(), "Resultado de audiolibros nulo",
                                Toast.LENGTH_LONG).show();
                    } else {
                        InfoAudiolibros.setTodosLosAudiolibros(audiolibrosResult);
                    }

                } else if (response.code() == 500){
                    Toast.makeText(getContext(), "Error del servidor",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Algo ha fallado obteniendo el error", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AudiolibrosResult> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}