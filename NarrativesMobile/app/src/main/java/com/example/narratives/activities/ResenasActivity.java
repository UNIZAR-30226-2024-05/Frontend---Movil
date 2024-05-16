package com.example.narratives.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.ResenasAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.informacion.InfoMiPerfil;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.especifico.GenericReview;
import com.example.narratives.peticiones.audiolibros.especifico.OwnReview;
import com.example.narratives.peticiones.resenas.ResenaDeleteRequest;
import com.example.narratives.peticiones.resenas.ResenaEditRequest;
import com.example.narratives.peticiones.resenas.ResenaPostRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResenasActivity extends AppCompatActivity {
    private final ArrayList<GenericReview> resenasPublicasList = InfoLibroActivity.audiolibroActual.getPublicReviews();
    private final ArrayList<GenericReview> resenasAmigosList = InfoLibroActivity.audiolibroActual.getFriendsReviews();
    private PopupWindow popupWindow;
    private RadioGroup radioGroupMiResena;
    private RatingBar ratingBarMiResena;
    private EditText editTextComentarioMiResena;
    private TextView textViewNingunaResena;
    private Button buttonMiResena;
    private FloatingActionButton fabNuevaResena;
    private final GenericReview review = new GenericReview();
    private ResenasAdapter resenasAdapter;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resenas);

        retrofitInterface = ApiClient.getRetrofitInterface();

        TextView textViewTituloLibroResenas = findViewById(R.id.textViewTituloLibroResenas);
        textViewTituloLibroResenas.setText(InfoLibroActivity.audiolibroActual.getAudiolibro().getTitulo());

        textViewNingunaResena = findViewById(R.id.textViewNingunaResena);
        if (resenasPublicasList == null || resenasPublicasList.isEmpty()) {
            textViewNingunaResena.setVisibility(View.VISIBLE);
        } else {
            textViewNingunaResena.setVisibility(View.GONE);
        }

        resenasAdapter = new ResenasAdapter(this, R.layout.item_lista_resenas, resenasPublicasList);

        ListView listViewListaResenas = findViewById(R.id.listViewListaResenas);
        listViewListaResenas.setAdapter(resenasAdapter);

        Switch switchVisibilidad = findViewById(R.id.switchVisibilidad);
        switchVisibilidad.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                final ResenasAdapter adapterAmigos = new ResenasAdapter(ResenasActivity.this,
                                                    R.layout.item_lista_resenas, resenasAmigosList);
                listViewListaResenas.setAdapter(adapterAmigos);

                if (resenasAmigosList.isEmpty()) {
                    textViewNingunaResena.setVisibility(View.VISIBLE);
                } else {
                    textViewNingunaResena.setVisibility(View.GONE);
                }
            } else {
                final ResenasAdapter adapterPublicas = new ResenasAdapter(ResenasActivity.this,
                                                    R.layout.item_lista_resenas, resenasPublicasList);
                listViewListaResenas.setAdapter(adapterPublicas);

                if (resenasPublicasList.isEmpty()) {
                    textViewNingunaResena.setVisibility(View.VISIBLE);
                } else {
                    textViewNingunaResena.setVisibility(View.GONE);
                }
            }
        });

        fabNuevaResena = findViewById(R.id.fabNuevaResena);
        buttonMiResena = findViewById(R.id.buttonMiResena);

        if (InfoLibroActivity.audiolibroActual.getOwnReview().getId() != 0) {
            fabNuevaResena.setVisibility(View.GONE);
            buttonMiResena.setVisibility(View.VISIBLE);
        }

        fabNuevaResena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupNuevaResena();
            }
        });

        buttonMiResena.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mostrarPopupMiResena();
            }
        });

        FloatingActionButton botonVolverDesdeResenas = findViewById(R.id.botonVolverDesdeResenas);
        botonVolverDesdeResenas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mostrarPopupNuevaResena() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewMiResena = inflater.inflate(R.layout.popup_mi_resena, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        popupWindow = new PopupWindow(viewMiResena, width, height, true);
        popupWindow.setAnimationStyle(1);

        getWindow().getDecorView().post(new Runnable(){
            @Override
            public void run(){
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
            }
        });

        radioGroupMiResena = viewMiResena.findViewById(R.id.radioGroupMiResena);
        ratingBarMiResena = viewMiResena.findViewById(R.id.ratingBarMiResena);
        editTextComentarioMiResena = viewMiResena.findViewById(R.id.editTextComentarioMiResena);

        FloatingActionButton botonCerrar = viewMiResena.findViewById(R.id.botonCerrarMiResena);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        Button botonConfirmarMiResena = viewMiResena.findViewById(R.id.botonConfirmarMiResena);
        if (InfoLibroActivity.audiolibroActual.getOwnReview().getId() == 0) {
            botonConfirmarMiResena.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (camposResenaOk()) {
                        int visibilidad = 0;
                        switch (radioGroupMiResena.getCheckedRadioButtonId()) {
                            case R.id.radioButtonOptionAmigos:
                                visibilidad = 1;
                                break;
                            case R.id.radioButtonOptionPrivada:
                                visibilidad = 2;
                                break;
                            default:
                                break;
                        }
                        publicarResena(editTextComentarioMiResena.getText().toString(),
                                                (int) ratingBarMiResena.getRating(), visibilidad);
                    } else {
                        Toast.makeText(ResenasActivity.this, "Campos vacíos", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            botonConfirmarMiResena.setText("GUARDAR");
            botonConfirmarMiResena.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (camposResenaOk()) {
                        int visibilidad = 0;
                        switch (radioGroupMiResena.getCheckedRadioButtonId()) {
                            case R.id.radioButtonOptionAmigos:
                                visibilidad = 1;
                                break;
                            case R.id.radioButtonOptionPrivada:
                                visibilidad = 2;
                                break;
                            default:
                                break;
                        }
                        editarResena(editTextComentarioMiResena.getText().toString(),
                                                    ratingBarMiResena.getRating(), visibilidad);
                    } else {
                        Toast.makeText(ResenasActivity.this, "Campos vacíos", Toast.LENGTH_LONG).show();
                    }
                }
            });

            Button botonEliminarMiResena = viewMiResena.findViewById(R.id.botonEliminarMiResena);
            botonEliminarMiResena.setVisibility(View.VISIBLE);
            botonEliminarMiResena.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarConfirmacionEliminaResena();
                }
            });
        }
    }

    private void mostrarPopupMiResena() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewMiResena = inflater.inflate(R.layout.popup_mi_resena, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        popupWindow = new PopupWindow(viewMiResena, width, height, true);
        popupWindow.setAnimationStyle(1);

        getWindow().getDecorView().post(new Runnable(){
            @Override
            public void run(){
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
            }
        });

        radioGroupMiResena = viewMiResena.findViewById(R.id.radioGroupMiResena);
        ratingBarMiResena = viewMiResena.findViewById(R.id.ratingBarMiResena);
        editTextComentarioMiResena = viewMiResena.findViewById(R.id.editTextComentarioMiResena);
        rellenarCamposMiResena();

        FloatingActionButton botonCerrar = viewMiResena.findViewById(R.id.botonCerrarMiResena);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        Button botonConfirmarMiResena = viewMiResena.findViewById(R.id.botonConfirmarMiResena);
        if (InfoLibroActivity.audiolibroActual.getOwnReview().getId() == 0) {
            botonConfirmarMiResena.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (camposResenaOk()) {
                        int visibilidad = 0;
                        switch (radioGroupMiResena.getCheckedRadioButtonId()) {
                            case R.id.radioButtonOptionAmigos:
                                visibilidad = 1;
                                break;
                            case R.id.radioButtonOptionPrivada:
                                visibilidad = 2;
                                break;
                            default:
                                break;
                        }
                        publicarResena(editTextComentarioMiResena.getText().toString(),
                                                (int) ratingBarMiResena.getRating(), visibilidad);
                    } else {
                        Toast.makeText(ResenasActivity.this, "Campos vacíos", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            botonConfirmarMiResena.setText("GUARDAR");
            botonConfirmarMiResena.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (camposResenaOk()) {
                        int visibilidad = 0;
                        switch (radioGroupMiResena.getCheckedRadioButtonId()) {
                            case R.id.radioButtonOptionAmigos:
                                visibilidad = 1;
                                break;
                            case R.id.radioButtonOptionPrivada:
                                visibilidad = 2;
                                break;
                            default:
                                break;
                        }
                        editarResena(editTextComentarioMiResena.getText().toString(),
                                                    ratingBarMiResena.getRating(), visibilidad);
                    } else {
                        Toast.makeText(ResenasActivity.this, "Campos vacíos", Toast.LENGTH_LONG).show();
                    }
                }
            });

            Button botonEliminarMiResena = viewMiResena.findViewById(R.id.botonEliminarMiResena);
            botonEliminarMiResena.setVisibility(View.VISIBLE);
            botonEliminarMiResena.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarConfirmacionEliminaResena();
                }
            });
        }
    }

    private void rellenarCamposMiResena() {
        ratingBarMiResena.setRating(InfoLibroActivity.audiolibroActual.getOwnReview().getPuntuacion());
        editTextComentarioMiResena.setText(InfoLibroActivity.audiolibroActual.getOwnReview().getComentario());

        switch (InfoLibroActivity.audiolibroActual.getOwnReview().getVisibilidad()) {
            case 0:
                radioGroupMiResena.check(R.id.radioButtonOptionPublica);
                break;
            case 1:
                radioGroupMiResena.check(R.id.radioButtonOptionAmigos);
                break;
            default: // 2
                radioGroupMiResena.check(R.id.radioButtonOptionPrivada);
                break;
        }
    }

    private boolean camposResenaOk() {
        return ratingBarMiResena.getRating() != 0 && radioGroupMiResena.getCheckedRadioButtonId() != -1;
    }

    private void publicarResena(String comentario, int puntuacion, int visibilidad) {
        ResenaPostRequest request = new ResenaPostRequest();
        request.setId_audiolibro(InfoLibroActivity.audiolibroActual.getAudiolibro().getId());
        request.setComentario(comentario);
        request.setPuntuacion(puntuacion);
        request.setVisibilidad(visibilidad);

        Call<OwnReview> call = retrofitInterface.ejecutarReviewPostReview(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<OwnReview>() {
            @Override
            public void onResponse(@NonNull Call<OwnReview> call, @NonNull Response<OwnReview> response) {
                if (response.isSuccessful()) {
                    actualizarPuntuacionAudiolibro();
                    InfoLibroActivity.audiolibroActual.setOwnReview(response.body());
                    actualizarListas(visibilidad);
                    fabNuevaResena.setVisibility(View.GONE);
                    buttonMiResena.setVisibility(View.VISIBLE);
                    popupWindow.dismiss();
                    Toast.makeText(ResenasActivity.this, "Reseña publicada", Toast.LENGTH_LONG).show();
                } else if (response.code() == 409) {
                    Toast.makeText(ResenasActivity.this, "Ya has hecho una reseña", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(ResenasActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResenasActivity.this, "Error desconocido " + response.code(),
                                    Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OwnReview> call, @NonNull Throwable t) {
                Toast.makeText(ResenasActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(ResenasActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void editarResena(String comentario, float puntuacion, int visibilidad) {
        ResenaEditRequest request = new ResenaEditRequest();
        request.setId_review(InfoLibroActivity.audiolibroActual.getOwnReview().getId());
        request.setComentario(comentario);
        request.setPuntuacion(puntuacion);
        request.setVisibilidad(visibilidad);

        Call<OwnReview> call = retrofitInterface.ejecutarReviewEditReview(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<OwnReview>() {
            @Override
            public void onResponse(@NonNull Call<OwnReview> call, @NonNull Response<OwnReview> response) {
                if (response.isSuccessful()) {
                    actualizarPuntuacionAudiolibro();
                    InfoLibroActivity.audiolibroActual.setOwnReview(response.body());
                    actualizarListas(visibilidad);
                    popupWindow.dismiss();
                    Toast.makeText(ResenasActivity.this, "Reseña editada", Toast.LENGTH_LONG).show();
                } else if (response.code() == 403) {
                    Toast.makeText(ResenasActivity.this, "La reseña no es tuya", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(ResenasActivity.this, "La reseña no existe", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(ResenasActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResenasActivity.this, "Error desconocido " + response.code(),
                                    Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OwnReview> call, @NonNull Throwable t) {
                Toast.makeText(ResenasActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(ResenasActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarConfirmacionEliminaResena() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResenasActivity.this);
        builder.setTitle("¿Está seguro de eliminar su reseña?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarResena();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void eliminarResena() {
        ResenaDeleteRequest request = new ResenaDeleteRequest();
        request.setId_review(InfoLibroActivity.audiolibroActual.getOwnReview().getId());

        Call<GenericMessageResult> call = retrofitInterface.ejecutarReviewDeleteReview(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                if (response.isSuccessful()) {
                    actualizarPuntuacionAudiolibro();
                    actualizarListas(2);
                    InfoLibroActivity.audiolibroActual.setOwnReview(null);
                    fabNuevaResena.setVisibility(View.VISIBLE);
                    buttonMiResena.setVisibility(View.GONE);
                    popupWindow.dismiss();
                    Toast.makeText(ResenasActivity.this, "Reseña eliminada", Toast.LENGTH_LONG).show();
                } else if (response.code() == 403) {
                    Toast.makeText(ResenasActivity.this, "La reseña no es tuya", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(ResenasActivity.this, "La reseña no existe", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(ResenasActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResenasActivity.this, "Error desconocido " + response.code(),
                                    Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericMessageResult> call, @NonNull Throwable t) {
                Toast.makeText(ResenasActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(ResenasActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void actualizarPuntuacionAudiolibro(){
        Call<AudiolibroEspecificoResponse> llamada = retrofitInterface.ejecutarAudiolibrosId(ApiClient.getUserCookie(),
                                                    InfoLibroActivity.audiolibroActual.getAudiolibro().getId());
        llamada.enqueue(new Callback<AudiolibroEspecificoResponse>() {
            @Override
            public void onResponse(@NonNull Call<AudiolibroEspecificoResponse> call, @NonNull Response<AudiolibroEspecificoResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoAudiolibros.setAudiolibroActual(response.body());
                    InfoLibroActivity.audiolibroActual = response.body();
                } else if(codigo == 409) {
                    Toast.makeText(ResenasActivity.this, "No hay ningún audiolibro con ese ID (reseñas)",
                                    Toast.LENGTH_LONG).show();
                } else if(codigo == 500) {
                    Toast.makeText(ResenasActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResenasActivity.this, "Error desconocido (AudiolibrosId): "
                                    + codigo, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AudiolibroEspecificoResponse> call, @NonNull Throwable t) {
                Toast.makeText(ResenasActivity.this, "No se ha conectado con el servidor",
                                Toast.LENGTH_LONG).show();
            }
        });
    }

    private void actualizarListas(int visibilidad) {
        eliminarResenaAnterior();

        review.setId(InfoLibroActivity.audiolibroActual.getOwnReview().getId());
        review.setUsername(InfoMiPerfil.getUsername());
        review.setUser_id(InfoMiPerfil.getId());
        review.setPuntuacion(InfoLibroActivity.audiolibroActual.getOwnReview().getPuntuacion());
        review.setComentario(InfoLibroActivity.audiolibroActual.getOwnReview().getComentario());
        review.setFecha(InfoLibroActivity.audiolibroActual.getOwnReview().getFecha());

        if (visibilidad == 0) {
            resenasPublicasList.add(review);
        } else {
            InfoLibroActivity.audiolibroActual.getPublicReviews().remove(review);
            resenasPublicasList.remove(review);
        }

        if (resenasPublicasList.isEmpty()) {
            textViewNingunaResena.setVisibility(View.VISIBLE);
        } else {
            textViewNingunaResena.setVisibility(View.GONE);
        }

        resenasAdapter.notifyDataSetChanged();
    }

    private void eliminarResenaAnterior() {
        for (GenericReview resena: resenasPublicasList) {
            if (resena.getId() == InfoLibroActivity.audiolibroActual.getOwnReview().getId()) {
                resenasPublicasList.remove(resena);
                return;
            }
        }
    }
}
