package com.example.narratives.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;

public class RegistroActivity extends AppCompatActivity{

    private EditText editTextUsuario;
    private EditText editTextCorreo;
    private EditText editTextContraseñaRegistro;
    private EditText editTextContraseñaRegistroConfirmar;

        protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.registrarse);
            super.onCreate(savedInstanceState);

            editTextUsuario = findViewById(R.id.editTextUsuarioRegistro);
            editTextCorreo = findViewById(R.id.editTextCorreo);
            editTextContraseñaRegistro = findViewById(R.id.editTextPasswordRegistro);
            editTextContraseñaRegistroConfirmar = findViewById(R.id.editTextPasswordConfirmarRegistro);

            findViewById(R.id.botonConfirmarRegistro).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validarCampos()) {
                        abrirMenuMain();
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
        startActivity(intent);
    }

    public void abrirMenuLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }}

