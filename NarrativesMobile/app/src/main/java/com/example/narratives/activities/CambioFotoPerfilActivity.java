package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;

import retrofit2.Retrofit;

public class CambioFotoPerfilActivity extends AppCompatActivity {

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;


    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.cambio_foto_perfil);
        super.onCreate(savedInstanceState);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();



    }

    private void cambiarFoto() {
        /* TODO: Rellenar cuando backend lo implemente
        CambioContrasenaRequest request = new CambioContrasenaRequest();
        request.setOldPassword(editTextContrasenaAntigua.getText().toString());
        request.setNewPassword(editTextContrasenaNueva.getText().toString());

        Call<StandardMessageRequest> llamada = retrofitInterface.ejecutarCambioContrasena(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<StandardMessageRequest>() {
            @Override
            public void onResponse(Call<StandardMessageRequest> call, Response<StandardMessageRequest> response) {


                if(response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CambioFotoPerfilActivity.this, R.style.ExitoAlertDialogStyle);
                    builder.setTitle("ÉXITO");
                    builder.setMessage("Contraseña cambiada correctamente");

                    builder.show();
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    abrirMenuMain();
                                }
                            }

                            , 1000);

                }  else if (response.code() == 401){
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("Incorrect password")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(CambioFotoPerfilActivity.this, R.style.ErrorAlertDialogStyle);
                            builder.setTitle("ERROR");
                            builder.setMessage("La contraseña es incorrecta");
                            builder.show();
                        } else {
                            Toast.makeText(CambioFotoPerfilActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(CambioFotoPerfilActivity.this, "Algo ha fallado obteniendo el error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CambioFotoPerfilActivity.this, "Código de error no reconocido",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StandardMessageRequest> call, Throwable t) {
                Toast.makeText(CambioFotoPerfilActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
        */
    }

    public void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}
