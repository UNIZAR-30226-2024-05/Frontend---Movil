package com.example.narratives.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;

public class RegistroActivity extends AppCompatActivity{

    private EditText editTextNombre;
    private EditText editTextUsuario;
    private EditText editTextCorreo;
    private EditText editTextContraseñaRegistro;
    private EditText editTextContraseñaRegistroConfirmar;

        protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.registrarse);
            super.onCreate(savedInstanceState);

            editTextNombre = findViewById(R.id.editTextNombre);
            editTextUsuario = findViewById(R.id.editTextUsuario);
            editTextCorreo = findViewById(R.id.editTextCorreo);
            editTextContraseñaRegistro = findViewById(R.id.editTextContraseñaRegistro);
            editTextContraseñaRegistroConfirmar = findViewById(R.id.editTextContraseñaRegistroConfirmar);

            findViewById(R.id.BotonConfirmar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validarCampos()) {
                        // Aquí puedes realizar acciones adicionales si todos los campos son válidos
                    }
                }
            });
        }
    private boolean validarCampos() {
        String nombre = editTextNombre.getText().toString().trim();
        String usuario = editTextUsuario.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String contraseña = editTextContraseñaRegistro.getText().toString().trim();
        String confirmarContraseña = editTextContraseñaRegistroConfirmar.getText().toString().trim();

        // Verificar si algún campo está vacío
        if (nombre.isEmpty() || usuario.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            Toast.makeText(this, "complete todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar el formato del correo
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "introduzca un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar si el correo es de Gmail o Hotmail
        if (!correo.contains("@gmail.com") && !correo.contains("@hotmail.com")) {
            Toast.makeText(this, "introduzca un correo de Gmail o Hotmail", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificar si las contraseñas coinciden
        if (!contraseña.equals(confirmarContraseña)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

