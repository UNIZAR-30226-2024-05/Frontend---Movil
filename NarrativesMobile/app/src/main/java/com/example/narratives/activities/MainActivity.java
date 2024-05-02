package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.databinding.ActivityMainBinding;
import com.example.narratives.fragments.FragmentAmigos;
import com.example.narratives.fragments.FragmentBiblioteca;
import com.example.narratives.fragments.FragmentClubs;
import com.example.narratives.fragments.FragmentEscuchando;
import com.example.narratives.fragments.FragmentInicio;
import com.example.narratives.informacion.InfoMiPerfil;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.users.perfiles.MiPerfilResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;


    static public int fragmentoActual = 0;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public static FloatingActionButton fabEscuchando;

    private static InfoMiPerfil miPerfil;

    private static ImageView imageViewFotoPerfil;

    static public FragmentInicio fragmentoInicioAbierto;
    static public FragmentBiblioteca fragmentoBibliotecaAbierto;
    static public FragmentEscuchando fragmentoEscuchandoAbierto;
    static public FragmentAmigos fragmentoAmigosAbierto;
    static public FragmentClubs fragmentoClubsAbierto;


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
                    reemplazarFragmento(fragmentoInicioAbierto, getSupportFragmentManager().beginTransaction());
                    fragmentoActual = 0;
                    break;

                case R.id.biblioteca:
                    reemplazarFragmento(fragmentoBibliotecaAbierto, getSupportFragmentManager().beginTransaction());
                    fragmentoActual = 1;
                    break;

                case R.id.amigos:
                    reemplazarFragmento(fragmentoAmigosAbierto, getSupportFragmentManager().beginTransaction());
                    fragmentoActual = 3;
                    break;

                case R.id.clubs:
                    reemplazarFragmento(fragmentoClubsAbierto, getSupportFragmentManager().beginTransaction());
                    fragmentoActual = 4;
                    break;

            }

            return true;
        });

        fabEscuchando = (FloatingActionButton) findViewById(R.id.botonEscuchando);
        findViewById(R.id.botonEscuchando).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reemplazarFragmento(fragmentoEscuchandoAbierto, getSupportFragmentManager().beginTransaction());
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
        fragmentoEscuchandoAbierto.pararPorCierreSesion();

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarUsersLogout(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {
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
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No se ha conectado con el servidor (cerrando sesión)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirMiPerfil(){
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewMiPerfil=inflater.inflate(R.layout.popup_mi_perfil, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;


        TextView textViewCambiarContrasena = viewMiPerfil.findViewById(R.id.textViewCambiarContrasenaMiPerfil) ;
        SpannableString content = new SpannableString( "Cambiar contraseña" ) ;
        content.setSpan( new UnderlineSpan() , 0 , content.length() , 0 ) ;
        textViewCambiarContrasena.setText(content);

        imageViewFotoPerfil = viewMiPerfil.findViewById(R.id.imageViewMiPerfil);
        imageViewFotoPerfil.setImageResource(MainActivity.getMiPerfil().getImgResource());
        imageViewFotoPerfil.setClickable(true);

        TextView textViewUsuarioMiPerfil = viewMiPerfil.findViewById(R.id.textViewRealNombreUsuarioMiPerfil);
        textViewUsuarioMiPerfil.setText(getMiPerfil().getUsername());

        TextView textViewMailMiPerfil = viewMiPerfil.findViewById(R.id.textViewRealCorreoElectronicoMiPerfil);
        textViewMailMiPerfil.setText(getMiPerfil().getMail());

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

        textViewCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCambioContrasena();
            }
        });

        Button buttonMisColecciones = viewMiPerfil.findViewById(R.id.botonMisColeccionesMiPerfil);
        buttonMisColecciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMisColecciones();
            }
        });
    }

    public void reemplazarFragmento(Fragment fragmento, FragmentTransaction fragmentTransaction){
        esconderTeclado();
        switch(fragmentoActual){
            case 0:
                fragmentTransaction.hide(fragmentoInicioAbierto);
                break;

            case 1:
                fragmentTransaction.hide(fragmentoBibliotecaAbierto);
                break;

            case 2:
                fragmentTransaction.hide(fragmentoEscuchandoAbierto);

                fabEscuchando.setImageTintList(ColorStateList.valueOf((0xFF) << 24 | (0x66) << 16 | (0x66) << 8 | (0x66)));
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
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }


    private void abrirCambioContrasena() {
        Intent intent = new Intent(this, CambioContrasenaActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void abrirCambioFotoPerfil() {
        Intent intent = new Intent(this, CambioFotoPerfilActivity.class);
        intent.putExtra("foto_perfil_actual", miPerfil.getImgResource());
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void abrirMisColecciones() {
        Intent intent = new Intent(this, ColeccionesActivity.class);
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
        miPerfil = new InfoMiPerfil();
        peticionUsersProfile();
    }


    public static InfoMiPerfil getMiPerfil() {
        return miPerfil;
    }

    public static void actualizarFotoPerfil(){
        imageViewFotoPerfil.setImageResource(miPerfil.getImgResource());
    }



    public void peticionUsersProfile(){
        Call<MiPerfilResponse> llamada = retrofitInterface.ejecutarUsersProfile(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<MiPerfilResponse>() {
            @Override
            public void onResponse(Call<MiPerfilResponse> call, Response<MiPerfilResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoMiPerfil infoMiPerfil = getMiPerfil();

                    infoMiPerfil.setUsername(response.body().getUsername());
                    infoMiPerfil.setMail(response.body().getMail());
                    infoMiPerfil.setImg(response.body().getImg());

                } else if(codigo == 500) {
                    Toast.makeText(MainActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error desconocido (UsersProfile): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MiPerfilResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void esconderTeclado() {
        if(this.getCurrentFocus() != null){
            InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}