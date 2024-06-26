package com.example.narratives.activities.pre_session;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.main.MainActivity;
import com.example.narratives.adaptadores.MenuInicioAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.informacion.InfoMiPerfil;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibrosResult;
import com.example.narratives.peticiones.home.HomeResult;
import com.example.narratives.sockets.SocketManager;

import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeSinRegistroActivity extends AppCompatActivity {
    private boolean haySesion;
    private int club_id = -1;
    private int coleccion_id = -1;
    private RecyclerView rvRecomendados, rvGenero1, rvGenero2, rvGenero3, rvGenero4, rvGenero5;
    private RetrofitInterface retrofitInterface;
    private ConstraintLayout cargandoNarrativesLayout;
    private TextView textViewGenero1, textViewGenero2, textViewGenero3, textViewGenero4, textViewGenero5;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.home_sin_registro);
        super.onCreate(savedInstanceState);

        handleDeepLink(getIntent());

        retrofitInterface = ApiClient.getRetrofitInterface();

        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("cookie")) {
            //Hay sesión
            haySesion = true;
            InfoMiPerfil.setId(sharedPreferences.getInt("user_id", -1));
            ApiClient.setUserCookie(sharedPreferences.getString("cookie", null));
            Socket mSocket = SocketManager.getInstance();
            mSocket.connect();
            if (InfoAudiolibros.getTodosLosAudiolibros() == null) {
                obtenerTodosLosAudiolibros();
            } else {
                peticionHome();
            }
        } else {
            haySesion = false;
            cargandoNarrativesLayout = findViewById(R.id.constraintLayoutCargandoNarratives);
            rvRecomendados = findViewById(R.id.recyclerViewRecomendados);

            textViewGenero1 = findViewById(R.id.textViewGenero1);
            rvGenero1 = findViewById(R.id.recyclerViewGenero1);

            textViewGenero2 = findViewById(R.id.textViewGenero2);
            rvGenero2 = findViewById(R.id.recyclerViewGenero2);

            textViewGenero3 = findViewById(R.id.textViewGenero3);
            rvGenero3 = findViewById(R.id.recyclerViewGenero3);

            textViewGenero4 = findViewById(R.id.textViewGenero4);
            rvGenero4 = findViewById(R.id.recyclerViewGenero4);

            textViewGenero5 = findViewById(R.id.textViewGenero5);
            rvGenero5 = findViewById(R.id.recyclerViewGenero5);

            if (InfoAudiolibros.getTodosLosAudiolibros() == null) {
                obtenerTodosLosAudiolibros();
            } else {
                mostrarMenuInicio(false);
            }

            findViewById(R.id.botonIrLoginDesdeInicio).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirMenuLogin();
                }
            });

            findViewById(R.id.botonIrRegistroDesdeInicio).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirMenuRegistro();
                }
            });
        }
    }

    public void obtenerTodosLosAudiolibros() {
        Call<AudiolibrosResult> llamada = retrofitInterface.ejecutarAudiolibros(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<AudiolibrosResult>() {
            @Override
            public void onResponse(@NonNull Call<AudiolibrosResult> call, @NonNull Response<AudiolibrosResult> response) {
                int codigo = response.code();

                if (codigo == 200) {
                    ArrayList<AudiolibroItem> audiolibrosResult = response.body().getAudiolibros();


                    if (audiolibrosResult == null) {
                        Toast.makeText(HomeSinRegistroActivity.this, "Resultado de audiolibros nulo",
                                Toast.LENGTH_LONG).show();
                    } else {
                        InfoAudiolibros.setTodosLosAudiolibros(audiolibrosResult);
                        if (!haySesion) {
                            if (club_id > 0 || coleccion_id > 0) {
                                abrirMenuLogin();
                            } else {
                                mostrarMenuInicio(true);
                            }
                        } else {
                            peticionHome();
                        }
                    }

                } else if (codigo == 500) {
                    Toast.makeText(HomeSinRegistroActivity.this, "Error del servidor",
                            Toast.LENGTH_LONG).show();
                } else if (codigo == 404) {
                    Toast.makeText(HomeSinRegistroActivity.this, "Error 404 /audiolibros",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(HomeSinRegistroActivity.this, error, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(HomeSinRegistroActivity.this, "Error desconocido (audiolibros): "
                                + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AudiolibrosResult> call, @NonNull Throwable t) {
                Toast.makeText(HomeSinRegistroActivity.this, "No se ha conectado con el servidor (todosAudiolibros)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void peticionHome() {
        Call<HomeResult> llamada = retrofitInterface.ejecutarHome(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<HomeResult>() {
            @Override
            public void onResponse(@NonNull Call<HomeResult> call, @NonNull Response<HomeResult> response) {
                int codigo = response.code();

                if (codigo == 200) {
                    InfoAudiolibros.setUltimoLibro(response.body().getUltimo());
                    InfoAudiolibros.setAudiolibrosSeguirEscuchando(response.body().getSeguir_escuchando(), response.body().getUltimo());

                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    abrirMenuMain(sharedPreferences);
                                }
                            }, 1000);
                } else if (codigo == 401) {
                    Toast.makeText(HomeSinRegistroActivity.this, "La sesión ha cadudado, vuevlva a iniciar sesión.",
                            Toast.LENGTH_LONG).show();
                    hayFalsaSesion();
                } else if (codigo == 500) {
                    Toast.makeText(HomeSinRegistroActivity.this, "Error del servidor",
                            Toast.LENGTH_LONG).show();
                } else if (codigo == 404) {
                    Toast.makeText(HomeSinRegistroActivity.this, "Error 404 /audiolibros",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(HomeSinRegistroActivity.this, error, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(HomeSinRegistroActivity.this, "Error desconocido (audiolibros): "
                                + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeResult> call, @NonNull Throwable t) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeSinRegistroActivity.this, R.style.ErrorAlertDialogStyle);
                builder.setMessage(t.getMessage());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    private void mostrarMenuInicio(boolean wait) {
        cargarCarruselesConGeneros();

        if (wait) {
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            esconderCargandoNarratives();
                        }
                    }, 350);
        } else {
            cargandoNarrativesLayout.setAlpha((float) 0);
        }
    }

    private void esconderCargandoNarratives() {
        Animation fadeOutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        fadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cargandoNarrativesLayout.setAlpha((float) 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        cargandoNarrativesLayout.startAnimation(fadeOutAnim);
    }

    private void cargarCarruselesConGeneros() {
        rvRecomendados.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        rvGenero1.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        rvGenero2.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        rvGenero3.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        rvGenero4.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        rvGenero5.setLayoutManager(new LinearLayoutManager(HomeSinRegistroActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

        MenuInicioAdapter adapterRecomendados;
        if (InfoAudiolibros.getTodosLosAudiolibros() != null) {
            String[] generos = InfoAudiolibros.getGenerosSeleccionados();

            adapterRecomendados = new MenuInicioAdapter(HomeSinRegistroActivity.this,
                    InfoAudiolibros.getAudiolibrosRecomendados(10));
            rvRecomendados.setAdapter(adapterRecomendados);

            MenuInicioAdapter adapter1 = new MenuInicioAdapter(HomeSinRegistroActivity.this,
                    InfoAudiolibros.getAudiolibrosPorGenero(generos[0]));
            rvGenero1.setAdapter(adapter1);
            textViewGenero1.setText(generos[0]);

            MenuInicioAdapter adapter2 = new MenuInicioAdapter(HomeSinRegistroActivity.this,
                    InfoAudiolibros.getAudiolibrosPorGenero(generos[1]));
            rvGenero2.setAdapter(adapter2);
            textViewGenero2.setText(generos[1]);

            MenuInicioAdapter adapter3 = new MenuInicioAdapter(HomeSinRegistroActivity.this,
                    InfoAudiolibros.getAudiolibrosPorGenero(generos[2]));
            rvGenero3.setAdapter(adapter3);
            textViewGenero3.setText(generos[2]);

            MenuInicioAdapter adapter4 = new MenuInicioAdapter(HomeSinRegistroActivity.this,
                    InfoAudiolibros.getAudiolibrosPorGenero(generos[3]));
            rvGenero4.setAdapter(adapter4);
            textViewGenero4.setText(generos[3]);

            MenuInicioAdapter adapter5 = new MenuInicioAdapter(HomeSinRegistroActivity.this,
                    InfoAudiolibros.getAudiolibrosPorGenero(generos[4]));
            rvGenero5.setAdapter(adapter5);
            textViewGenero5.setText(generos[4]);

        }
    }

    public void abrirMenuMain(SharedPreferences sharedPreferences) {
        Intent intent = new Intent(this, MainActivity.class);
        if (club_id > 0) {
            intent.putExtra("club_id", club_id);
        } else if (coleccion_id > 0) {
            intent.putExtra("coleccion_id", coleccion_id);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    public void abrirMenuRegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void abrirMenuLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        if (club_id > 0) {
            intent.putExtra("club_id", club_id);
        } else if (coleccion_id > 0) {
            intent.putExtra("coleccion_id", coleccion_id);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void hayFalsaSesion() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("cookie");
        editor.remove("user_id");
        editor.apply();
        abrirMenuLogin();
    }

    private void handleDeepLink(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            String path = data.getPath();
            // Verificar si el path coincide con tu deep link esperado
            if ("/club".equals(path)) {
                // Obtener el parámetro id del deep link
                String id = data.getQueryParameter("id");
                club_id = Integer.parseInt(id);
            } else if ("/coleccion".equals(path)) {
                String id = data.getQueryParameter("id");
                coleccion_id = Integer.parseInt(id);
            }
        }
    }
}
