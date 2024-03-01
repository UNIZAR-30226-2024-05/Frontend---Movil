package com.example.narratives;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inicio_sesion);
        super.onCreate(savedInstanceState);

        TextView invitadoTextView = findViewById(R.id.invitadoTextView);
        invitadoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirHomeSinRegistro();
            }
        });
        Button botonIniciarSesion = (Button) findViewById(R.id.botonIniciarSesion);
        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMenuInicio();
            }

        });
    }

    public void abrirMenuInicio() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void abrirHomeSinRegistro() {
        Intent intent = new Intent(this, HomeSinRegistro.class);
        startActivity(intent);
    }

}