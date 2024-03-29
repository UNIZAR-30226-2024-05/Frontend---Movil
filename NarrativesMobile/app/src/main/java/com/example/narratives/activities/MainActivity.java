package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.narratives.R;
import com.example.narratives.databinding.ActivityMainBinding;
import com.example.narratives.fragments.FragmentAmigos;
import com.example.narratives.fragments.FragmentBiblioteca;
import com.example.narratives.fragments.FragmentClubs;
import com.example.narratives.fragments.FragmentEscuchando;
import com.example.narratives.fragments.FragmentInicio;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.informacion.MiPerfil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;


    int fragmentoActual = 0;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private static MiPerfil miPerfil;

    private static ImageView imageViewFotoPerfil;

    private FragmentInicio fragmentoInicioAbierto;
    private FragmentBiblioteca fragmentoBibliotecaAbierto;
    private FragmentEscuchando fragmentoEscuchandoAbierto;
    private FragmentAmigos fragmentoAmigosAbierto;
    private FragmentClubs fragmentoClubsAbierto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        retrofit = ApiClient.getRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        obtenerDatosMiPerfil();

        fragmentoInicioAbierto = new FragmentInicio();
        getSupportFragmentManager().beginTransaction().add(R.id.main_layout, fragmentoInicioAbierto).commit();
        fragmentoBibliotecaAbierto = new FragmentBiblioteca();
        getSupportFragmentManager().beginTransaction().add(R.id.main_layout, fragmentoBibliotecaAbierto).commit();
        fragmentoEscuchandoAbierto = new FragmentEscuchando();
        getSupportFragmentManager().beginTransaction().add(R.id.main_layout, fragmentoEscuchandoAbierto).commit();
        fragmentoAmigosAbierto = new FragmentAmigos();
        getSupportFragmentManager().beginTransaction().add(R.id.main_layout, fragmentoAmigosAbierto).commit();
        fragmentoClubsAbierto = new FragmentClubs();
        getSupportFragmentManager().beginTransaction().add(R.id.main_layout, fragmentoClubsAbierto).commit();


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
                binding.bottomNavigatorView.getMenu().getItem(2).setChecked(true);
                fabEscuchando.setImageTintList(ColorStateList.valueOf((0xff) << 24 | (0x01) << 16 | (0x87) << 8 | (0x86)));
                fragmentoActual = 2;
            }
        });



        FloatingActionButton botonCerrarSesion = (FloatingActionButton) findViewById(R.id.botonCerrarSesion);
        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAlertaCerrarSesion();
            }
        });

        FloatingActionButton botonMiPerfil = (FloatingActionButton) findViewById(R.id.botonMiPerfil);
        botonMiPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMiPerfil();
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void abrirAlertaCerrarSesion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Estás seguro de que quieres cerrar la sesión?");

        builder.setPositiveButton("Cerrar sesión", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cerrarSesion();
            }
        });

        builder.setNegativeButton("Cancelar    |", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cerrarSesion() {
        fragmentoEscuchandoAbierto.pararMusica();
        fragmentoEscuchandoAbierto.reiniciarMusica();

        Call<Void> llamada = retrofitInterface.ejecutarSalirSesion(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Cerrando sesión...");
                    builder.show();
                    ApiClient.setUserCookie(null);
                    new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                abrirMenuHomeSinRegistro();
                            }
                        }
                        , 500);

                } else if (response.code() == 401){
                    Toast.makeText(MainActivity.this, "No hay sesión iniciada",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Algo ha fallado obteniendo el error", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirMiPerfil(){
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewMiPerfil=inflater.inflate(R.layout.mi_perfil, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;


        TextView textViewCambiarContrasena = viewMiPerfil.findViewById(R.id.textViewCambiarContrasenaMiPerfil) ;
        SpannableString content = new SpannableString( "Cambiar contraseña" ) ;
        content.setSpan( new UnderlineSpan() , 0 , content.length() , 0 ) ;
        textViewCambiarContrasena.setText(content);

        imageViewFotoPerfil = viewMiPerfil.findViewById(R.id.imageViewMiPerfil);
        imageViewFotoPerfil.setImageResource(MainActivity.getMiPerfil().getPhotoResource());
        imageViewFotoPerfil.setClickable(true);

        imageViewFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCambioFotoPerfil();
            }
        });


        PopupWindow popupWindow=new PopupWindow(viewMiPerfil,width,height, true);
        popupWindow.setAnimationStyle(1);

        FrameLayout layout = findViewById(R.id.main_layout);
        layout.post(new Runnable(){
            @Override
            public void run(){
                popupWindow.showAtLocation(layout, Gravity.BOTTOM,0,0);
            }
        });


        FloatingActionButton botonCerrar = (FloatingActionButton) viewMiPerfil.findViewById(R.id.botonCerrarMiPerfil);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        textViewCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCambioContrasena();
            }
        });
    }

    private void reemplazarFragmento(Fragment fragmento){
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
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.botonEscuchando);
                fab.setImageTintList(ColorStateList.valueOf((0xFF) << 24 | (0x66) << 16 | (0x66) << 8 | (0x66)));
                break;

            case 3:
                fragmentTransaction.hide(fragmentoAmigosAbierto);
                break;

            case 4:
                fragmentTransaction.hide(fragmentoClubsAbierto);
                break;
        }
        fragmentTransaction.show(fragmento);
        fragmentTransaction.commit();
    }

    public void abrirMenuHomeSinRegistro() {
        Intent intent = new Intent(this, HomeSinRegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }


    private void abrirCambioContrasena() {
        Intent intent = new Intent(this, CambioContrasenaActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void abrirCambioFotoPerfil() {
        Intent intent = new Intent(this, CambioFotoPerfilActivity.class);
        intent.putExtra("foto_perfil_actual", miPerfil.getPhotoResource());
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
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

    private void obtenerDatosMiPerfil(){
        miPerfil = new MiPerfil();
    }


    public static MiPerfil getMiPerfil() {
        return miPerfil;
    }


    public static void actualizarFotoPerfil(){
        imageViewFotoPerfil.setImageResource(miPerfil.getPhotoResource());
    }
}