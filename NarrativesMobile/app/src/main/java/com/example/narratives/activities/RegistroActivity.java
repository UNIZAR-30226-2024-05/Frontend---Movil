package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.narratives.regislogin.LoginResult;
import com.example.narratives.regislogin.RetrofitInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroActivity extends AppCompatActivity{

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    //TODO: Conseguir IP del servidor
    private String URL_BASE = "";
    private EditText editTextUsuario;
    private EditText editTextCorreo;
    private EditText editTextContraseñaRegistro;
    private EditText editTextContraseñaRegistroConfirmar;

        protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.registrarse);
            super.onCreate(savedInstanceState);

            editTextUsuario = findViewById(R.id.editTextUsuarioRegistro);
            editTextCorreo = findViewById(R.id.editTextCorreoRegistro);
            editTextContraseñaRegistro = findViewById(R.id.editTextPasswordRegistro);
            editTextContraseñaRegistroConfirmar = findViewById(R.id.editTextPasswordConfirmarRegistro);

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);

            findViewById(R.id.botonConfirmarRegistro).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validarCampos()) {
                        comprobarDatosRegistro(botonConfirmarRegistro);
                    }
                }
            });

            findViewById(R.id.botonIrInicioSesionDesdeRegistro).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirMenuLogin();
                }
            });

            findViewById(R.id.botonVolverAlInicioRegistro).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirMenuHomeSinRegistro();
                }
            });
        }

    private boolean validarCampos() {
        String usuario = editTextUsuario.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String password = editTextContraseñaRegistro.getText().toString().trim();
        String confirmarContraseña = editTextContraseñaRegistroConfirmar.getText().toString().trim();

        // Verificar si algún campo está vacío
        if (usuario.isEmpty() || correo.isEmpty() || password.isEmpty() || confirmarContraseña.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar el formato del correo
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Introduzca un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar si las contraseñas coinciden
        if (!password.equals(confirmarContraseña) ) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar si la password tiene un formato correcto
        if (password.length() > 20) {
            Toast.makeText(this, "La contraseña no puede tener más de 20 caracteres", Toast.LENGTH_SHORT).show();
            return false;

        }

        if (!comprobarCaracteresPassword(password)){
            Toast.makeText(this, "La contraseña solo puede tener carácteres alfanuméricos o guión bajo (_)", Toast.LENGTH_SHORT).show();
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
        startActivity(intent);
    }

    public void abrirMenuHomeSinRegistro() {
        Intent intent = new Intent(this, HomeSinRegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void abrirMenuLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void comprobarDatosRegistro(Button boton){

        EditText usuarioEditText = (EditText) findViewById(R.id.editTextUsuarioRegistro);
        EditText passwordEditText = (EditText) findViewById(R.id.editTextPasswordRegistro);

        EditText correoEditText = (EditText) findViewById(R.id.editTextCorreoRegistro);


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> datos = new HashMap<>();


                datos.put("username", usuarioEditText.getText().toString());
                datos.put("mail", correoEditText.getText().toString());
                datos.put("password", passwordEditText.getText().toString());

                Call<Void> llamada = retrofitInterface.ejecutarRegistro(datos);
                llamada.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                            builder.setMessage("Cuenta creada con éxito");
                            builder.show();
                            abrirMenuMain();

                        } else {
                            Toast.makeText(RegistroActivity.this, response.errorBody().toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(RegistroActivity.this, "No se ha conectado con el servidor",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


}


