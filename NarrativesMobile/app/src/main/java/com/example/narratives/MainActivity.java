package com.example.narratives;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.narratives.databinding.ActivityMainBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reemplazarFragmento(new FragmentInicio());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigatorView.setBackground(null);
        binding.bottomNavigatorView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {
                case R.id.inicio:
                    reemplazarFragmento(new FragmentInicio());
                    break;

                case R.id.biblioteca:
                    reemplazarFragmento(new FragmentBiblioteca());
                    break;

                case R.id.amigos:
                    reemplazarFragmento(new FragmentAmigos());
                    break;

                case R.id.clubs:
                    reemplazarFragmento(new FragmentClubs());
                    break;

                case R.id.escuchando:
                    reemplazarFragmento(new FragmentEscuchando());
                    break;

            }

            return true;
        });
    }


    private void reemplazarFragmento(Fragment fragmento){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragmento);
        fragmentTransaction.commit();

    }

    public void abrirMenuInicio() {
        Intent intent = new Intent(this, FragmentInicio.class);
        startActivity(intent);
        startActivityFromFragment(new FragmentInicio(), intent, 0);
    }

}