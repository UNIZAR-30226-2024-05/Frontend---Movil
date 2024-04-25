package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionSet;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives.peticiones.users.register.RegisterRequest;
import com.example.narratives.peticiones.users.register.RegisterResult;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistroActivity extends AppCompatActivity{

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private EditText editTextUsuario;
    private EditText editTextCorreo;
    private EditText editTextContraseñaRegistro;
    private EditText editTextContraseñaRegistroConfirmar;

        protected void onCreate(Bundle savedInstanceState) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setExitTransition(new TransitionSet());

            setContentView(R.layout.registro);
            super.onCreate(savedInstanceState);

            editTextUsuario = findViewById(R.id.editTextUsuarioRegistro);
            editTextCorreo = findViewById(R.id.editTextCorreoRegistro);
            editTextContraseñaRegistro = findViewById(R.id.editTextPasswordRegistro);
            editTextContraseñaRegistroConfirmar = findViewById(R.id.editTextPasswordConfirmarRegistro);

            retrofit = ApiClient.getRetrofit();

            retrofitInterface = ApiClient.getRetrofitInterface();


            Button botonRegistro = (Button) findViewById(R.id.botonConfirmarRegistro);
            botonRegistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validarCampos()) {
                        comprobarDatosRegistro(botonRegistro);
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

        if (password.length() < 8) {
            Toast.makeText(RegistroActivity.this, "La contraseña no puede tener menos de 8 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        int comprobacionCaracteres = comprobarCaracteresPassword(password);
        if (comprobacionCaracteres == 1){
            Toast.makeText(RegistroActivity.this, "La contraseña solo puede tener carácteres alfanuméricos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (comprobacionCaracteres == 2){
            Toast.makeText(RegistroActivity.this, "La contraseña debe contener los caracteres requeridos*", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }


    private int comprobarCaracteresPassword(String password) {
        boolean numero = false;
        boolean mayus = false;
        boolean minus = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (!(Character.isLetterOrDigit(c))) {
                return 1;
            }

            if (!numero && Character.isDigit(c)) numero = true;
            if (!minus && Character.isLowerCase(c)) minus = true;
            if (!mayus && Character.isUpperCase(c)) mayus = true;

        }
        if(numero && mayus && minus){
            return 0;
        } else {
            return 2;
        }
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

        RegisterRequest request = new RegisterRequest();
        request.setUsername(editTextUsuario.getText().toString());
        request.setMail(editTextCorreo.getText().toString());
        request.setPassword(editTextContraseñaRegistro.getText().toString());

        Call<RegisterResult> llamada = retrofitInterface.ejecutarUsersRegister(request);
        llamada.enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {


                if(response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this, R.style.ExitoAlertDialogStyle);
                    builder.setTitle("ÉXITO");
                    builder.setMessage("La cuenta ha sido creada correctamente");
                    builder.show();
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    abrirMenuLogin();
                                }
                            }

                    , 1000);

                }  else if (response.code() == 409){
                    /*Toast.makeText(RegistroActivity.this, "Usuario o correo ya existentes",
                            Toast.LENGTH_LONG).show();
                    */
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");

                        if(error.equals("Existing username")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                            builder.setTitle("ERROR");
                            builder.setMessage("Ya existe una cuenta con este nombre");
                            builder.show();
                        } else if(error.equals("Existing email")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                            builder.setTitle("ERROR");
                            builder.setMessage("Ya existe una cuenta con este correo");
                            builder.show();
                        } else {
                            Toast.makeText(RegistroActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                        } catch (Exception e) {
                        Toast.makeText(RegistroActivity.this, "Algo ha fallado obteniendo el error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegistroActivity.this, "Código de error no reconocido: " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                Toast.makeText(RegistroActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
    }




}


