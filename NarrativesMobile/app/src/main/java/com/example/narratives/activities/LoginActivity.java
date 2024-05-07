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

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.sockets.SocketManager;
import com.example.narratives.peticiones.users.login.LoginRequest;
import com.example.narratives.peticiones.users.login.LoginResult;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    Socket socket;
    String cookie;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.inicio_sesion);
        super.onCreate(savedInstanceState);

        retrofit = ApiClient.getLoginRetrofit();
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
        findViewById(R.id.botonDirecto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginRequest request = new LoginRequest(); // hace un login automático con una cuenta de prueba
                request.setUsername("directo");
                request.setPassword("Directo1");

                peticionLogin(request);
            }
        });
    }

    private void peticionLogin(LoginRequest request) {

        Call<LoginResult> llamada = retrofitInterface.ejecutarUsersLogin(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.code() == 200) {
                    if(response.isSuccessful()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.ExitoAlertDialogStyle);
                        builder.setMessage("Iniciando sesión...");

                        alertDialog = builder.create();
                        alertDialog.show();

                        cookie = response.headers().get("Set-Cookie");
                        ApiClient.setUserCookie(cookie);
                        SocketManager.setSession(cookie);
                        socket = SocketManager.getInstance();
                        socket.connect();
                        guardarSesion(cookie, response.body().getId());
                        new Handler().postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        abrirMenuMain();
                                    }
                                }
                                , 1000);
                    }else{
                        Toast.makeText(LoginActivity.this, "Código correcto, pero sesión no exitosa", Toast.LENGTH_LONG).show();
                    }


                } else if (response.code() == 404){
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
                    Toast.makeText(LoginActivity.this, "Código error " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Algo ha fallado",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//
        intent.putExtra("COOKIE", cookie);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        finish();
    }

    public void abrirHomeSinRegistro() {



        Intent intent = new Intent(this, HomeSinRegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
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