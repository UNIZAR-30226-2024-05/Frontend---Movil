package com.example.narratives.activities.main;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.clubes.InfoClubActivity;
import com.example.narratives.activities.colecciones.ColeccionesActivity;
import com.example.narratives.activities.pre_session.HomeSinRegistroActivity;
import com.example.narratives.activities.pre_session.LoginActivity;
import com.example.narratives.databinding.ActivityMainBinding;
import com.example.narratives.fragments.FragmentAmigos;
import com.example.narratives.fragments.FragmentBiblioteca;
import com.example.narratives.fragments.FragmentClubs;
import com.example.narratives.fragments.FragmentEscuchando;
import com.example.narratives.fragments.FragmentInicio;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.informacion.InfoClubes;
import com.example.narratives.informacion.InfoMiPerfil;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.users.perfiles.MiPerfilResponse;
import com.example.narratives.sockets.SocketManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    public static int fragmentoActual = 0;
    public static FragmentInicio fragmentoInicioAbierto;
    public static FragmentBiblioteca fragmentoBibliotecaAbierto;
    public static FragmentEscuchando fragmentoEscuchandoAbierto;
    public static FragmentAmigos fragmentoAmigosAbierto;
    public static FragmentClubs fragmentoClubsAbierto;
    public static FloatingActionButton fabEscuchando;
    public static boolean abrirEscuchando;
    private static ImageView imageViewFotoPerfil;
    private static InfoMiPerfil infoMiPerfil;
    private RetrofitInterface retrofitInterface;
    private AlertDialog alertDialog;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        abrirEscuchando = false;
        retrofitInterface = ApiClient.getRetrofitInterface();

        obtenerDatosMiPerfil();

        fragmentoInicioAbierto = new FragmentInicio();
        fragmentManager.beginTransaction().add(R.id.main_layout, fragmentoInicioAbierto).commit();
        fragmentoBibliotecaAbierto = new FragmentBiblioteca();
        fragmentManager.beginTransaction().add(R.id.main_layout, fragmentoBibliotecaAbierto).commit();
        fragmentoEscuchandoAbierto = new FragmentEscuchando();
        fragmentManager.beginTransaction().add(R.id.main_layout, fragmentoEscuchandoAbierto).commit();
        fragmentoAmigosAbierto = new FragmentAmigos();
        fragmentManager.beginTransaction().add(R.id.main_layout, fragmentoAmigosAbierto).commit();
        fragmentoClubsAbierto = new FragmentClubs();
        fragmentManager.beginTransaction().add(R.id.main_layout, fragmentoClubsAbierto).commit();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reemplazarFragmentoInicial();
        binding.bottomNavigatorView.getMenu().getItem(2).setEnabled(false);
        binding.bottomNavigatorView.setBackground(null);
        binding.bottomNavigatorView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {
                case R.id.inicio:
                    esconderTeclado();
                    reemplazarFragmento(fragmentoInicioAbierto, fragmentManager.beginTransaction());
                    fragmentoActual = 0;
                    break;

                case R.id.biblioteca:
                    esconderTeclado();
                    reemplazarFragmento(fragmentoBibliotecaAbierto, fragmentManager.beginTransaction());
                    fragmentoActual = 1;
                    break;

                case R.id.amigos:
                    esconderTeclado();
                    reemplazarFragmento(fragmentoAmigosAbierto, fragmentManager.beginTransaction());
                    fragmentoActual = 3;
                    break;

                case R.id.clubs:
                    esconderTeclado();
                    reemplazarFragmento(fragmentoClubsAbierto, fragmentManager.beginTransaction());
                    fragmentoActual = 4;
                    break;
            }

            return true;
        });

        fabEscuchando = findViewById(R.id.botonEscuchando);
        fabEscuchando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esconderTeclado();
                reemplazarFragmento(fragmentoEscuchandoAbierto, fragmentManager.beginTransaction());
                binding.bottomNavigatorView.getMenu().getItem(2).setChecked(true);
                fabEscuchando.setImageTintList(ColorStateList.valueOf((0xff) << 24 | (0x01) << 16 | (0x87) << 8 | (0x86)));
                fragmentoActual = 2;
            }
        });

        FloatingActionButton botonCerrarSesion = findViewById(R.id.botonCerrarSesion);
        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAlertaCerrarSesion();
            }
        });

        FloatingActionButton botonMiPerfil = findViewById(R.id.botonMiPerfil);
        botonMiPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMiPerfil();
            }
        });

        gestionarDeepLinks();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (abrirEscuchando) {
            fabEscuchando.performClick();
        }
        abrirEscuchando = false;
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
                if(alertDialog != null && alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void cerrarSesion() {
        fragmentoEscuchandoAbierto.pararPorCierreSesion();

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarUsersLogout(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                int codigo = response.code();

                if (codigo == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Cerrando sesión...");
                    alertDialog = builder.create();
                    alertDialog.show();
                    cierreSesionLocal(false);

                } else if (codigo == 401){
                    cierreSesionLocal(true);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Algo ha fallado obteniendo el error",
                                        Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericMessageResult> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "No se ha conectado con el servidor (cerrando sesión)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirMiPerfil() {
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewMiPerfil=inflater.inflate(R.layout.popup_mi_perfil, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        TextView textViewCambiarContrasena = viewMiPerfil.findViewById(R.id.textViewCambiarContrasenaMiPerfil) ;
        SpannableString content = new SpannableString("Cambiar contraseña" ) ;
        content.setSpan(new UnderlineSpan() , 0 , content.length() , 0 ) ;
        textViewCambiarContrasena.setText(content);

        imageViewFotoPerfil = viewMiPerfil.findViewById(R.id.imageViewMiPerfil);
        imageViewFotoPerfil.setImageResource(MainActivity.getInfoMiPerfil().getImgResource());
        imageViewFotoPerfil.setClickable(true);

        TextView textViewUsuarioMiPerfil = viewMiPerfil.findViewById(R.id.textViewRealNombreUsuarioMiPerfil);
        textViewUsuarioMiPerfil.setText(InfoMiPerfil.getUsername());

        TextView textViewMailMiPerfil = viewMiPerfil.findViewById(R.id.textViewRealCorreoElectronicoMiPerfil);
        textViewMailMiPerfil.setText(getInfoMiPerfil().getMail());

        imageViewFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCambioFotoPerfil();
            }
        });

        PopupWindow popupWindow = new PopupWindow(viewMiPerfil,width,height, true);
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

        Button buttonMisColecciones = viewMiPerfil.findViewById(R.id.botonMisColeccionesMiPerfil);
        buttonMisColecciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                ColeccionesActivity.username = InfoMiPerfil.getUsername();
                abrirMisColecciones();
            }
        });
    }

    public static void reemplazarFragmento(Fragment fragmento, FragmentTransaction fragmentTransaction){
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

    private void abrirMisColecciones() {
        Intent intent = new Intent(this, ColeccionesActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void reemplazarFragmentoInicial() {
        fragmentoActual = 0;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragmentoInicioAbierto);
        fragmentTransaction.hide(fragmentoBibliotecaAbierto);
        fragmentTransaction.hide(fragmentoEscuchandoAbierto);
        fragmentTransaction.hide(fragmentoAmigosAbierto);
        fragmentTransaction.hide(fragmentoClubsAbierto);
        fragmentTransaction.commit();
    }

    private void obtenerDatosMiPerfil() {
        infoMiPerfil = new InfoMiPerfil();
        peticionUsersProfile();
    }

    public static InfoMiPerfil getInfoMiPerfil() {
        return infoMiPerfil;
    }

    public static void actualizarFotoPerfil() {
        imageViewFotoPerfil.setImageResource(infoMiPerfil.getImgResource());
    }

    public void peticionUsersProfile() {
        Call<MiPerfilResponse> llamada = retrofitInterface.ejecutarUsersProfile(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<MiPerfilResponse>() {
            @Override
            public void onResponse(@NonNull Call<MiPerfilResponse> call, @NonNull Response<MiPerfilResponse> response) {
                int codigo = response.code();

                if (codigo == 200) {
                    InfoMiPerfil infoMiPerfil = getInfoMiPerfil();
                    infoMiPerfil.setUsername(response.body().getUsername());
                    infoMiPerfil.setMail(response.body().getMail());
                    infoMiPerfil.setImg(response.body().getImg());
                } else if(codigo == 401) {
                    cierreSesionLocal(true);
                }else if(codigo == 500) {
                    Toast.makeText(MainActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error desconocido (UsersProfile): "
                                    + codigo, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MiPerfilResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "No se ha conectado con el servidor",
                                Toast.LENGTH_LONG).show();
            }
        });
    }

    private void vaciarInformacionSesionActual(){
        ApiClient.setUserCookie(null);
        SocketManager.deleteInstance();
        infoMiPerfil = null;
        InfoMiPerfil.setId(-1);
        InfoAudiolibros.setTodosLosAudiolibros(null);
        InfoClubes.setTodosLosClubes(null);
        // toda la informacion de AMIGOS a null
    }

    private void esconderTeclado() {
        if(this.getCurrentFocus() != null){
            InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void abrirMenuHomeSinRegistro() {
        Intent intent = new Intent(this, HomeSinRegistroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        finish();
    }

    private void abrirCambioContrasena() {
        Intent intent = new Intent(this, CambioContrasenaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void abrirCambioFotoPerfil() {
        Intent intent = new Intent(this, CambioFotoPerfilActivity.class);
        intent.putExtra("foto_perfil_actual", infoMiPerfil.getImgResource());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void abrirMenuLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void cierreSesionLocal(boolean forced) {
        ApiClient.setUserCookie(null);

        SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        vaciarInformacionSesionActual();

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (forced) {
                            abrirMenuLogin();
                        } else {
                            abrirMenuHomeSinRegistro();
                        }
                    }
                }, 1000);
    }

    private void gestionarDeepLinks() {
        Intent intentData = getIntent();
        int club_id = intentData.getIntExtra("club_id", -1);
        if (club_id > 0) {
            Intent intent = new Intent(this, InfoClubActivity.class);
            intent.putExtra("club_id", club_id);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        int coleccion_id = intentData.getIntExtra("coleccion_id", -1);
        if (coleccion_id > 0) {
            Intent intent = new Intent(this, ColeccionesActivity.class);
            intent.putExtra("coleccion_id", coleccion_id);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }
}
