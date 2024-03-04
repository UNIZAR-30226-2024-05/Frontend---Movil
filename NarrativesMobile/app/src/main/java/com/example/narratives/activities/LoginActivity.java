package com.example.narratives.activities;

import static android.app.ProgressDialog.show;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives.regislogin.LoginResult;
import com.example.narratives.regislogin.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    //TODO: Conseguir IP del servidor
    private String URL_BASE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inicio_sesion);
        super.onCreate(savedInstanceState);
/*
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Button botonIniciarSesion = (Button) findViewById(R.id.botonConfirmarLogin);
        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarDatosLogin(botonIniciarSesion);
            }
        });
*/

        // PROVISIONAL PARA HACER PRUEBAS

        Button botonConfirmar = (Button) this.findViewById(R.id.botonConfirmarLogin);
        botonConfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                abrirMenuMain();
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

    private void comprobarDatosLogin(Button boton) {
        EditText usuarioEditText = (EditText) findViewById(R.id.editTextUsuarioLogin);
        EditText passwordEditText = (EditText) findViewById(R.id.editTextContraseñaLogin);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> datos = new HashMap<>();


                datos.put("username", usuarioEditText.getText().toString());
                datos.put("password", passwordEditText.getText().toString());

                Call<LoginResult> llamada = retrofitInterface.ejecutarInicioSesion(datos);
                llamada.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.code() == 200) {

                            LoginResult resultado = response.body();

                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle(resultado.getUsuario());
                            builder.setTitle(resultado.getUsuario());
                            builder.setMessage(resultado.getCorreo());

                            builder.show();
                            abrirMenuMain();

                        } else {
                            Toast.makeText(LoginActivity.this, response.errorBody().toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    public void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void abrirHomeSinRegistro() {
        Intent intent = new Intent(this, HomeSinRegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void abrirMenuRegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}