package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives.peticiones.CambioContrasenaRequest;
import com.example.narratives.peticiones.StandardMessageRequest;
import com.example.narratives.regislogin.ApiClient;
import com.example.narratives.regislogin.RetrofitInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CambioContrasenaActivity extends AppCompatActivity {

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    private EditText editTextContrasenaAntigua;
    private EditText editTextContrasenaNueva;
    private EditText editTextVerificarContrasenaNueva;


    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.cambio_contrasena);
        super.onCreate(savedInstanceState);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        editTextContrasenaAntigua = findViewById(R.id.editTextContraseñaAntigua);
        editTextContrasenaNueva = findViewById(R.id.editTextContrasenaNueva);
        editTextVerificarContrasenaNueva = findViewById(R.id.editTextVerificarContrasenaNueva);

        Button botonConfirmar = (Button) findViewById(R.id.botonConfirmarCambioContrasena);
        botonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comprobarDatosCambioContrasena()){
                    cambiarContrasena();
                }
            }
        });

        FloatingActionButton botonAtras = (FloatingActionButton) findViewById(R.id.botonVolverDesdeCambioContrasena);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMenuMain();
            }
        });



    }

    private void cambiarContrasena() {
        CambioContrasenaRequest request = new CambioContrasenaRequest();
        request.setOldPassword(editTextContrasenaAntigua.getText().toString());
        request.setNewPassword(editTextContrasenaNueva.getText().toString());

        Call<StandardMessageRequest> llamada = retrofitInterface.ejecutarCambioContrasena(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<StandardMessageRequest>() {
            @Override
            public void onResponse(Call<StandardMessageRequest> call, Response<StandardMessageRequest> response) {


                if(response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CambioContrasenaActivity.this, R.style.ExitoAlertDialogStyle);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(CambioContrasenaActivity.this, R.style.ErrorAlertDialogStyle);
                            builder.setTitle("ERROR");
                            builder.setMessage("La contraseña es incorrecta");
                            builder.show();
                        } else {
                            Toast.makeText(CambioContrasenaActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(CambioContrasenaActivity.this, "Algo ha fallado obteniendo el error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CambioContrasenaActivity.this, "Código de error no reconocido",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StandardMessageRequest> call, Throwable t) {
                Toast.makeText(CambioContrasenaActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean comprobarDatosCambioContrasena() {
        String antigua = editTextContrasenaAntigua.getText().toString().trim();
        String nueva = editTextContrasenaNueva.getText().toString().trim();
        String verificarNueva = editTextVerificarContrasenaNueva.getText().toString().trim();

        // Verificar si algún campo está vacío
        if (antigua.isEmpty() || nueva.isEmpty() || verificarNueva.isEmpty()) {
            Toast.makeText(CambioContrasenaActivity.this, "Se deben completar todos los campos", Toast.LENGTH_LONG).show();
            return false;
        }

        // Verificar si la contraseña nueva y la de verificación coinciden
        if (!nueva.equals(verificarNueva) ) {
            Toast.makeText(CambioContrasenaActivity.this, "La contraseña nueva debe coincidir con la de verificación", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar si la contraseña antigua y nueva NO coinciden
        if (antigua.equals(nueva) ) {
            Toast.makeText(CambioContrasenaActivity.this, "La contraseña nueva no puede ser igual que la antigua", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar si la password tiene un formato correcto
        if (nueva.length() > 20) {
            Toast.makeText(CambioContrasenaActivity.this, "La contraseña no puede tener más de 20 caracteres", Toast.LENGTH_SHORT).show();
            return false;

        }

        if (!comprobarCaracteresPassword(nueva)){
            Toast.makeText(CambioContrasenaActivity.this, "La contraseña solo puede tener carácteres alfanuméricos o guión bajo (_)", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean comprobarCaracteresPassword(String password) {
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (!(Character.isLetterOrDigit(c) || c == '_')) {
                return false;
            }
        }
        return true;
    }


    public void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}
