package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.informacion.InfoMiPerfil;
import com.example.narratives.peticiones.home.HomeResult;
import com.example.narratives.peticiones.users.login.LoginRequest;
import com.example.narratives.peticiones.users.login.LoginResult;
import com.example.narratives.sockets.SocketManager;

import org.json.JSONObject;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private RetrofitInterface retrofitInterface;
    private Socket socket;
    private String cookie;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.inicio_sesion);
        super.onCreate(savedInstanceState);

        retrofitInterface = ApiClient.getRetrofitInterface();

        Button botonIniciarSesion = (Button) findViewById(R.id.botonConfirmarLogin);
        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usuarioEditText = (EditText) findViewById(R.id.editTextUsuarioLogin);
                EditText passwordEditText = (EditText) findViewById(R.id.editTextContraseñaLogin);

                LoginRequest request = new LoginRequest();
                request.setUsername(usuarioEditText.getText().toString());
                request.setPassword(passwordEditText.getText().toString());

                peticionLogin(request);
            }
        });

        findViewById(R.id.botonIrARegistroDesdeLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMenuRegistro();
            }
        });

        findViewById(R.id.botonVolverAlInicioLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirHomeSinRegistro();
            }
        });

    }

    private void peticionLogin(LoginRequest request) {
        Call<LoginResult> llamada = retrofitInterface.ejecutarUsersLogin(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(@NonNull Call<LoginResult> call, @NonNull Response<LoginResult> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.ExitoAlertDialogStyle);
                        builder.setMessage("Iniciando sesión...");

                        alertDialog = builder.create();
                        alertDialog.show();

                        cookie = response.headers().get("Set-Cookie");

                        InfoMiPerfil.setId(response.body().getId());
                        ApiClient.setUserCookie(cookie);

                        socket = SocketManager.getInstance();
                        socket.connect();

                        guardarSesion(cookie, response.body().getId());

                        new Handler().postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        peticionHome();
                                    }
                                }, 1000);
                    } else {
                        Toast.makeText(LoginActivity.this, "Código correcto, pero sesión no exitosa",
                                        Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 404) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.ErrorAlertDialogStyle);
                    builder.setTitle("ERROR");
                    builder.setMessage("Usuario no encontrado");
                    builder.show();
                } else if (response.code() == 401){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.ErrorAlertDialogStyle);
                    builder.setTitle("ERROR");
                    builder.setMessage("Contraseña incorrecta");
                    builder.show();
                } else if (response.code() == 500){
                    Toast.makeText(LoginActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Código error " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResult> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Algo ha fallado",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void peticionHome() {
        Call<HomeResult> llamada = retrofitInterface.ejecutarHome(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<HomeResult>() {
            @Override
            public void onResponse(@NonNull Call<HomeResult> call, @NonNull Response<HomeResult> response) {
                int codigo = response.code();

                if (codigo == 200) {
                    InfoAudiolibros.setAudiolibrosSeguirEscuchando(response.body().getSeguir_escuchando(), response.body().getUltimo());
                    InfoAudiolibros.setUltimoLibro(response.body().getUltimo());

                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    abrirMenuMain();
                                }
                            }, 1000);

                } else if (codigo == 500) {
                    Toast.makeText(LoginActivity.this, "Error del servidor",
                            Toast.LENGTH_LONG).show();
                } else if (codigo == 404) {
                    Toast.makeText(LoginActivity.this, "Error 404 /audiolibros",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Error desconocido (audiolibros): "
                                + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeResult> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "No se ha conectado con el servidor (home)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int club_id = getIntent().getIntExtra("club_id", -1);
        if (club_id > 0) {
            intent.putExtra("club_id", club_id);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        finish();
    }

    public void abrirHomeSinRegistro() {
        Intent intent = new Intent(this, HomeSinRegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    public void abrirMenuRegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void guardarSesion(String cookie, int user_id) {
        SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cookie", cookie);
        editor.putInt("user_id", user_id);
        editor.apply();
    }
}
