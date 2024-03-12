package com.example.narratives.activities;

import static com.example.narratives.regislogin.RetrofitInterface.URL_BASE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.narratives.fragments.FragmentAmigos;
import com.example.narratives.fragments.FragmentBiblioteca;
import com.example.narratives.fragments.FragmentClubs;
import com.example.narratives.fragments.FragmentEscuchando;
import com.example.narratives.fragments.FragmentInicio;
import com.example.narratives.R;
import com.example.narratives.databinding.ActivityMainBinding;
import com.example.narratives.regislogin.LoginResult;
import com.example.narratives.regislogin.RetrofitInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;


    int fragmentoActual = 0;
    FragmentManager fragManager = getSupportFragmentManager();


    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;


    private Fragment fragmentoInicioAbierto;
    private Fragment fragmentoBibliotecaAbierto;
    private Fragment fragmentoEscuchandoAbierto;
    private Fragment fragmentoAmigosAbierto;
    private Fragment fragmentoClubsAbierto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        /*
        FloatingActionButton botonIniciarSesion = (FloatingActionButton) findViewById(R.id.cerrarsesión);
        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });
        */

        fragmentoInicioAbierto = new FragmentInicio();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragmentoInicioAbierto).commit();
        fragmentoBibliotecaAbierto = new FragmentBiblioteca();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragmentoBibliotecaAbierto).commit();
        fragmentoEscuchandoAbierto = new FragmentEscuchando();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragmentoEscuchandoAbierto).commit();
        fragmentoAmigosAbierto = new FragmentAmigos();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragmentoAmigosAbierto).commit();
        fragmentoClubsAbierto = new FragmentClubs();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragmentoClubsAbierto).commit();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reemplazarFragmentoInicial();
        binding.bottomNavigatorView.getMenu().getItem(2).setEnabled(false);
        binding.bottomNavigatorView.setBackground(null);
        binding.bottomNavigatorView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {
                case R.id.inicio:
                    reemplazarFragmento(fragmentoInicioAbierto);
                    fragmentoActual = 0;
                    break;

                case R.id.biblioteca:
                    reemplazarFragmento(fragmentoBibliotecaAbierto);
                    fragmentoActual = 1;
                    break;

                case R.id.amigos:
                    reemplazarFragmento(fragmentoAmigosAbierto);
                    fragmentoActual = 3;
                    break;

                case R.id.clubs:
                    reemplazarFragmento(fragmentoClubsAbierto);
                    fragmentoActual = 4;
                    break;

            }

            return true;
        });

        FloatingActionButton fabEscuchando = (FloatingActionButton) findViewById(R.id.botonEscuchando);
        findViewById(R.id.botonEscuchando).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reemplazarFragmento(fragmentoEscuchandoAbierto);
                binding.bottomNavigatorView.getMenu().getItem(fragmentoActual).setCheckable(false);
                fabEscuchando.setImageTintList(ColorStateList.valueOf((0xff) << 24 | (0x01) << 16 | (0x87) << 8 | (0x86)));
                fragmentoActual = 2;
            }
        });
    }

    private void cerrarSesion() {

        Call<Void> llamada = retrofitInterface.ejecutarSalirSesion();
        llamada.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this, "Sesión cerrada correctamente",
                            Toast.LENGTH_LONG).show();

                    abrirMenuHomeSinRegistro();

                } else if (response.code() == 409){
                    Toast.makeText(MainActivity.this, "No había sesión iniciada",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void reemplazarFragmento(Fragment fragmento){


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.botonEscuchando);
        fab.setImageTintList(ColorStateList.valueOf((0xFF) << 24 | (0x66) << 16 | (0x66) << 8 | (0x66)));
        binding.bottomNavigatorView.getMenu().getItem(fragmentoActual).setCheckable(true);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch(fragmentoActual){
            case 0:
                fragmentTransaction.hide(fragmentoInicioAbierto);
                break;

            case 1:
                fragmentTransaction.hide(fragmentoBibliotecaAbierto);
                break;

            case 2:
                fragmentTransaction.hide(fragmentoEscuchandoAbierto);
                break;

            case 3:
                fragmentTransaction.hide(fragmentoAmigosAbierto);
                break;

            case 4:
                fragmentTransaction.hide(fragmentoClubsAbierto);
                break;
        }
        fragmentTransaction.show(fragmento);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragmento);
        fragmentTransaction.commit();
    }

    public void abrirMenuHomeSinRegistro() {
        Intent intent = new Intent(this, HomeSinRegistroActivity.class);
        startActivity(intent);
    }

    private void reemplazarFragmentoInicial(){
        fragmentoActual = 0;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragmentoInicioAbierto);
        fragmentTransaction.hide(fragmentoBibliotecaAbierto);
        fragmentTransaction.hide(fragmentoEscuchandoAbierto);
        fragmentTransaction.hide(fragmentoAmigosAbierto);
        fragmentTransaction.hide(fragmentoClubsAbierto);
        fragmentTransaction.commit();
    }
}