package com.example.narratives._backend;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.example.narratives.R;
import com.example.narratives.activities.MainActivity;
import com.example.narratives.informacion.InfoMiPerfil;
import com.example.narratives.peticiones.CambioFotoPerfilRequest;
import com.example.narratives.peticiones.MiPerfilResponse;
import com.example.narratives.peticiones.StandardMessageResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Peticiones {

    public void peticionUsersProfile(Context context){
        RetrofitInterface retrofitInterface = ApiClient.getRetrofitInterface();
        Call<MiPerfilResponse> llamada = retrofitInterface.ejecutarUsersProfile(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<MiPerfilResponse>() {
            @Override
            public void onResponse(Call<MiPerfilResponse> call, Response<MiPerfilResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoMiPerfil infoMiPerfil = MainActivity.getMiPerfil();

                    infoMiPerfil.setUsername(response.body().getUsername());
                    infoMiPerfil.setMail(response.body().getMail());
                    infoMiPerfil.setImg(response.body().getImg());
                } else if(codigo == 500) {
                    Toast.makeText(context, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Error desconocido: " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MiPerfilResponse> call, Throwable t) {
                Toast.makeText(context, "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void peticionUsersChange_img(Context context, String newImg){
        RetrofitInterface retrofitInterface = ApiClient.getRetrofitInterface();
        CambioFotoPerfilRequest request = new CambioFotoPerfilRequest();
        request.setNewImg(newImg);
        Call<StandardMessageResult> llamada = retrofitInterface.ejecutarUsersChange_img(ApiClient.getUserCookie(), request);

        llamada.enqueue(new Callback<StandardMessageResult>() {
            @Override
            public void onResponse(Call<StandardMessageResult> call, Response<StandardMessageResult> response) {
                int codigo = response.code();

                if(codigo == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.TealAlertDialogStyle);
                    builder.setMessage("Foto actualizada");
                    builder.show();
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(context, intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
                                }
                            }
                            , 500);



                } else if(codigo == 500) {
                    Toast.makeText(context, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Error desconocido: " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<StandardMessageResult> call, Throwable t) {
                Toast.makeText(context, "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }


}
