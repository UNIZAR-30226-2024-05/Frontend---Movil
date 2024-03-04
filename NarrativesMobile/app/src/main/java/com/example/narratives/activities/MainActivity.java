package com.example.narratives.activities;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    int fragmentoActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reemplazarFragmento(new FragmentInicio());
        binding.bottomNavigatorView.getMenu().getItem(2).setEnabled(false);
        binding.bottomNavigatorView.setBackground(null);
        binding.bottomNavigatorView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {
                case R.id.inicio:
                    reemplazarFragmento(new FragmentInicio());
                    fragmentoActual = 0;
                    break;

                case R.id.biblioteca:
                    reemplazarFragmento(new FragmentBiblioteca());
                    fragmentoActual = 1;
                    break;

                case R.id.amigos:
                    reemplazarFragmento(new FragmentAmigos());
                    fragmentoActual = 3;
                    break;

                case R.id.clubs:
                    reemplazarFragmento(new FragmentClubs());
                    fragmentoActual = 4;
                    break;

            }

            return true;
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.botonEscuchando);
        findViewById(R.id.botonEscuchando).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reemplazarFragmento(new FragmentEscuchando());
                binding.bottomNavigatorView.getMenu().getItem(fragmentoActual).setCheckable(false);
                fab.setImageTintList(ColorStateList.valueOf((0xff) << 24 | (0x01) << 16 | (0x87) << 8 | (0x86)));
            }
        });
    }


    private void reemplazarFragmento(Fragment fragmento){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.botonEscuchando);
        fab.setImageTintList(ColorStateList.valueOf((0xFF) << 24 | (0x66) << 16 | (0x66) << 8 | (0x66)));
        binding.bottomNavigatorView.getMenu().getItem(fragmentoActual).setCheckable(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragmento);
        fragmentTransaction.commit();
    }
}