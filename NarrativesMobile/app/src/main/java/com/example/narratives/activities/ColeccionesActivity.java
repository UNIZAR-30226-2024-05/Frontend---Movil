package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.AudiolibrosColeccionAdapter;
import com.example.narratives.adaptadores.ColeccionesAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.informacion.InfoMiPerfil;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.colecciones.AnadirEliminarAudiolibroDeColeccionRequest;
import com.example.narratives.peticiones.colecciones.AnadirEliminarColeccionRequest;
import com.example.narratives.peticiones.colecciones.AudiolibrosColeccionItem;
import com.example.narratives.peticiones.colecciones.ColeccionEspecificaResult;
import com.example.narratives.peticiones.colecciones.ColeccionesItem;
import com.example.narratives.peticiones.colecciones.ColeccionesRequest;
import com.example.narratives.peticiones.colecciones.ColeccionesResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ColeccionesActivity extends AppCompatActivity implements ColeccionesAdapter.OnMenuItemClickListener,
                    AudiolibrosColeccionAdapter.OnMenuItemClickListener {
    public static String username;
    public static ArrayList<ColeccionesItem> coleccionesList = new ArrayList<>();
    public static ColeccionEspecificaResult coleccionActual;
    private ColeccionesAdapter coleccionesAdapter;
    private AudiolibrosColeccionAdapter audiolibrosColeccionAdapter;
    private TextView textViewGuardadaSiNo;
    private Button buttonGuardarQuitarColeccion;
    private PopupWindow popupWindow;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colecciones);

        retrofitInterface = ApiClient.getRetrofitInterface();

        TextView textViewTituloColecciones = findViewById(R.id.textViewTituloColecciones);
        FloatingActionButton botonAnadir = findViewById(R.id.botonAnadirNuevaColeccion);

        coleccionesAdapter = new ColeccionesAdapter(this, R.layout.item_lista_colecciones, coleccionesList);
        coleccionesAdapter.setOnMenuEliminarColeccionClickListener(this);

        if (!username.equals(InfoMiPerfil.getUsername())) {      // Amigo
            textViewTituloColecciones.setText("Colecciones de " + username);
            botonAnadir.setVisibility(View.GONE);
            coleccionesAdapter.setColeccionAmigo(true);
        } else {    // Yo
            coleccionesAdapter.setColeccionAmigo(false);
            obtenerColecciones();
            botonAnadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarPopupNuevaColeccion();
                }
            });
        }

        EditText editTextBuscadorColecciones = findViewById(R.id.editTextBuscadorColecciones);
        editTextBuscadorColecciones.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                coleccionesAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ListView listViewListaColecciones = findViewById(R.id.listViewListaColecciones);
        listViewListaColecciones.setAdapter(coleccionesAdapter);
        listViewListaColecciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obtenerInfoColeccion(coleccionesList.get(position).getId());
            }
        });

        FloatingActionButton botonVolver = findViewById(R.id.botonVolverDesdeListaColecciones);
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void obtenerColecciones() {
        Call<ColeccionesResult> call = retrofitInterface.ejecutarColecciones(ApiClient.getUserCookie());
        call.enqueue(new Callback<ColeccionesResult>() {
            @Override
            public void onResponse(@NonNull Call<ColeccionesResult> call, @NonNull Response<ColeccionesResult> response) {
                if (response.isSuccessful()) {
                    ArrayList<ColeccionesItem> coleccionesResult = response.body().getCollections();
                    if (coleccionesResult.isEmpty()) {
                        Toast.makeText(ColeccionesActivity.this, "Resultado de colecciones nulo",
                                        Toast.LENGTH_LONG).show();
                    } else {
                        coleccionesList.clear();
                        coleccionesList.addAll(coleccionesResult);
                        coleccionesAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ColeccionesResult> call, @NonNull Throwable t) {
                Toast.makeText(ColeccionesActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(ColeccionesActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void obtenerInfoColeccion(int coleccion) {
        Call<ColeccionEspecificaResult> call = retrofitInterface.ejecutarColeccionesId(ApiClient.getUserCookie(), coleccion);
        call.enqueue(new Callback<ColeccionEspecificaResult>() {
            @Override
            public void onResponse(@NonNull Call<ColeccionEspecificaResult> call,
                                   @NonNull Response<ColeccionEspecificaResult> response) {
                if (response.isSuccessful()) {
                    coleccionActual = response.body();
                    mostrarPopUpInfoColeccion();
                } else if (response.code() == 409) {
                    Toast.makeText(ColeccionesActivity.this, "No se ha encontrado la colección en la BD",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ColeccionEspecificaResult> call, @NonNull Throwable t) {
                Toast.makeText(ColeccionesActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(ColeccionesActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        coleccionesAdapter.notifyDataSetChanged();
    }

    private void mostrarPopupNuevaColeccion() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewNuevaColeccion = inflater.inflate(R.layout.popup_nueva_coleccion, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        popupWindow = new PopupWindow(viewNuevaColeccion, width, height, true);
        popupWindow.setAnimationStyle(1);

        FrameLayout layout = findViewById(R.id.main_layout);
        layout.post(new Runnable(){
            @Override
            public void run(){
                popupWindow.showAtLocation(layout, Gravity.BOTTOM,0,0);
            }
        });

        FloatingActionButton botonCerrar = viewNuevaColeccion.findViewById(R.id.botonCerrarNuevaColeccion);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        EditText editTextTituloColeccion = viewNuevaColeccion.findViewById(R.id.editTextUsuarioRegistro);

        Button botonGuardarNuevaColeccion = viewNuevaColeccion.findViewById(R.id.botonConfirmarNuevaColeccion);
        botonGuardarNuevaColeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tituloNuevaColeccion = editTextTituloColeccion.getText().toString().trim();
                if (tituloOk(tituloNuevaColeccion)) {
                    anadirColeccion(tituloNuevaColeccion);
                    popupWindow.dismiss();
                }
            }
        });
    }

    private boolean tituloOk(String titulo) {
        if (titulo.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ColeccionesActivity.this,
                                                                    R.style.ErrorAlertDialogStyle);
            builder.setTitle("ERROR");
            builder.setMessage("El título no puede estar vacío");
            builder.show();
            return false;
        }
        return true;
    }

    private void anadirColeccion(String titulo) {
        ColeccionesRequest request = new ColeccionesRequest();
        request.setTitle(titulo);

        Call<GenericMessageResult> call = retrofitInterface.ejecutarColeccionesCreate(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                if (response.isSuccessful()) {
                    obtenerColecciones();
                    Toast.makeText(ColeccionesActivity.this, "Colección añadida", Toast.LENGTH_LONG).show();
                } else if (response.code() == 400) {
                    Toast.makeText(ColeccionesActivity.this, "Título existente", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(ColeccionesActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ColeccionesActivity.this, "Error desconocido " + response.code(),
                                    Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericMessageResult> call, @NonNull Throwable t) {
                Toast.makeText(ColeccionesActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(ColeccionesActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarPopUpInfoColeccion() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewInfoColeccion = inflater.inflate(R.layout.popup_info_coleccion, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        popupWindow = new PopupWindow(viewInfoColeccion, width, height, true);
        popupWindow.setAnimationStyle(1);

        FrameLayout layout = findViewById(R.id.main_layout);
        layout.post(new Runnable(){
            @Override
            public void run(){
                popupWindow.showAtLocation(layout, Gravity.BOTTOM,0,0);
            }
        });

        TextView textViewTituloColeccion = viewInfoColeccion.findViewById(R.id.textViewTituloColeccion);
        textViewTituloColeccion.setText(coleccionActual.getColeccion().getTitulo());

        TextView textViewNombrePropietarioColeccion = viewInfoColeccion.findViewById(R.id.textViewNombrePropietarioColeccion);
        textViewNombrePropietarioColeccion.setText(coleccionActual.getPropietarioUsername());

        textViewGuardadaSiNo = viewInfoColeccion.findViewById(R.id.textViewGuardadaSiNo);
        buttonGuardarQuitarColeccion = viewInfoColeccion.findViewById(R.id.buttonGuardarQuitarColeccion);

        if (coleccionActual.getColeccion().getTitulo().equals("Escuchar mas tarde")
                || coleccionActual.getColeccion().getTitulo().equals("Favoritos")) {
            TextView textViewTextoGuardada = viewInfoColeccion.findViewById(R.id.textViewTextoGuardada);
            textViewTextoGuardada.setVisibility(View.GONE);
            textViewGuardadaSiNo.setVisibility(View.GONE);
            buttonGuardarQuitarColeccion.setVisibility(View.GONE);
        } else {
            if (coleccionActual.isGuardada()) {
                textViewGuardadaSiNo.setText("SI");
                buttonGuardarQuitarColeccion.setText("QUITAR");
            } else {
                textViewGuardadaSiNo.setText("NO");
                buttonGuardarQuitarColeccion.setText("GUARDAR");
            }

            buttonGuardarQuitarColeccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!coleccionActual.isGuardada()) {
                        guardarColeccion();
                    } else {
                        mostrarConfirmacionEliminarColeccion();
                    }
                }
            });
        }

        TextView textViewSinAudiolibrosEnColeccion = viewInfoColeccion.findViewById(R.id.textViewSinAudiolibrosEnColeccion);

        ArrayList<AudiolibrosColeccionItem> audiolibrosColeccionList = coleccionActual.getAudiolibros();
        if (!audiolibrosColeccionList.isEmpty()) {
            textViewSinAudiolibrosEnColeccion.setVisibility(View.GONE);

            audiolibrosColeccionAdapter = new AudiolibrosColeccionAdapter(this,
                                                    R.layout.item_audiolibros_coleccion, audiolibrosColeccionList);
            audiolibrosColeccionAdapter.setOnMenuEliminarAudiolibroClickListener(this);
            audiolibrosColeccionAdapter.setColeccionAmigo(!username.equals(InfoMiPerfil.getUsername()));

            ListView listViewListaAudiolibrosColeccion = viewInfoColeccion.findViewById(R.id.listViewListaAudiolibrosColeccion);
            listViewListaAudiolibrosColeccion.setAdapter(audiolibrosColeccionAdapter);

            listViewListaAudiolibrosColeccion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mostrarInfoLibro(audiolibrosColeccionList.get(position).getId());
                }
            });
        } else {
            textViewSinAudiolibrosEnColeccion.setVisibility(View.VISIBLE);
        }

        FloatingActionButton botonVolverDesdeInfoColeccion = viewInfoColeccion.findViewById(R.id.botonVolverDesdeInfoColeccion);
        botonVolverDesdeInfoColeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    private void guardarColeccion() {
        AnadirEliminarColeccionRequest request = new AnadirEliminarColeccionRequest();
        request.setCollectionId(coleccionActual.getColeccion().getId());

        Call<GenericMessageResult> call = retrofitInterface.ejecutarColeccionesFriend(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                if (response.isSuccessful()) {
                    if (!username.equals(InfoMiPerfil.getUsername())) {
                        obtenerColecciones();
                    }
                    coleccionActual.setGuardada(true);
                    textViewGuardadaSiNo.setText("SI");
                    buttonGuardarQuitarColeccion.setText("QUITAR");
                    Toast.makeText(ColeccionesActivity.this, "Colección añadida", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(ColeccionesActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ColeccionesActivity.this, "Error desconocido " + response.code(),
                                    Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericMessageResult> call, @NonNull Throwable t) {
                Toast.makeText(ColeccionesActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(ColeccionesActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void quitarColeccion() {
        AnadirEliminarColeccionRequest request = new AnadirEliminarColeccionRequest();
        request.setCollectionId(coleccionActual.getColeccion().getId());

        Call<GenericMessageResult> call = retrofitInterface.ejecutarColeccionesRemove(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                if (response.isSuccessful()) {
                    obtenerColecciones();
                    if (coleccionActual.getPropietarioUsername().equals(InfoMiPerfil.getUsername())) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    } else {
                        coleccionActual.setGuardada(false);
                        textViewGuardadaSiNo.setText("NO");
                        buttonGuardarQuitarColeccion.setText("GUARDAR");
                    }
                    Toast.makeText(ColeccionesActivity.this, "Colección eliminada", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(ColeccionesActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ColeccionesActivity.this, "Error desconocido " + response.code(),
                                    Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericMessageResult> call, @NonNull Throwable t) {
                Toast.makeText(ColeccionesActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(ColeccionesActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void quitarAudiolibro(AudiolibrosColeccionItem audiolibro) {
        AnadirEliminarAudiolibroDeColeccionRequest request = new AnadirEliminarAudiolibroDeColeccionRequest();
        request.setAudiolibroId(audiolibro.getId());
        request.setColeccionId(coleccionActual.getColeccion().getId());

        Call<GenericMessageResult> call = retrofitInterface.ejecutarColeccionesEliminarAudiolibro(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                if (response.isSuccessful()) {
                    coleccionActual.getAudiolibros().remove(audiolibro);
                    audiolibrosColeccionAdapter.notifyDataSetChanged();
                    Toast.makeText(ColeccionesActivity.this, "Audiolibro eliminado", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(ColeccionesActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ColeccionesActivity.this, "Error desconocido " + response.code(),
                                    Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericMessageResult> call, @NonNull Throwable t) {
                Toast.makeText(ColeccionesActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(ColeccionesActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void obtenerInfoLibro(int audiolibroId, Callback<AudiolibroEspecificoResponse> callback) {
        Call<AudiolibroEspecificoResponse> llamada = retrofitInterface.ejecutarAudiolibrosId(ApiClient.getUserCookie(), audiolibroId);
        llamada.enqueue(callback);
    }

    @Override
    public void onMenuEliminarColeccionClick(int position) {
        coleccionActual = new ColeccionEspecificaResult();
        coleccionActual.setColeccion(coleccionesList.get(position));
        coleccionActual.setPropietarioUsername(InfoMiPerfil.getUsername());
        mostrarConfirmacionEliminarColeccion();
    }

    @Override
    public void onMenuEliminarAudiolibroClick(int position) {
        mostrarConfirmacionEliminarAudiolibro(position);
    }

    @Override
    public void onReproducirAudiolibroClick(int position) {
        inicializarLibro(position);
    }

    private void inicializarLibro(int position) {
        obtenerInfoLibro(coleccionActual.getAudiolibros().get(position).getId(), new Callback<AudiolibroEspecificoResponse>() {
            @Override
            public void onResponse(Call<AudiolibroEspecificoResponse> call, Response<AudiolibroEspecificoResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoAudiolibros.audiolibroActual = response.body();
                    MainActivity.fragmentoEscuchandoAbierto.inicializarLibro(InfoLibroActivity.audiolibroActual);
                    MainActivity.abrirEscuchando = true;
                    abrirMenuMain();
                } else if(codigo == 409) {
                    Toast.makeText(ColeccionesActivity.this, "No hay ningún audiolibro con ese ID",
                                    Toast.LENGTH_LONG).show();
                } else if(codigo == 500) {
                    Toast.makeText(ColeccionesActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ColeccionesActivity.this, "Error desconocido (AudiolibrosId): " + codigo,
                                    Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AudiolibroEspecificoResponse> call, Throwable t) {
                Toast.makeText(ColeccionesActivity.this, "No se ha conectado con el servidor",
                                Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirMenuMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private void mostrarConfirmacionEliminarColeccion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ColeccionesActivity.this);
        builder.setTitle("¿Está seguro de eliminar la colección " + coleccionActual.getColeccion().getTitulo() + "?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quitarColeccion();
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

    private void mostrarConfirmacionEliminarAudiolibro(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ColeccionesActivity.this);
        builder.setTitle("¿Está seguro de quitar el audiolibro "
                        + coleccionActual.getAudiolibros().get(position).getTitulo() + "?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quitarAudiolibro(coleccionActual.getAudiolibros().get(position));
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

    private void mostrarInfoLibro(int audiolibroId) {
        obtenerInfoLibro(audiolibroId, new Callback<AudiolibroEspecificoResponse>() {
            @Override
            public void onResponse(Call<AudiolibroEspecificoResponse> call, Response<AudiolibroEspecificoResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoLibroActivity.audiolibroActual = response.body();
                    Intent intent = new Intent(ColeccionesActivity.this, InfoLibroActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ColeccionesActivity.this).toBundle());
                } else if(codigo == 409) {
                    Toast.makeText(ColeccionesActivity.this, "No hay ningún audiolibro con ese ID",
                                    Toast.LENGTH_LONG).show();
                } else if(codigo == 500) {
                    Toast.makeText(ColeccionesActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ColeccionesActivity.this, "Error desconocido (AudiolibrosId): " + codigo,
                                    Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AudiolibroEspecificoResponse> call, Throwable t) {
                Toast.makeText(ColeccionesActivity.this, "No se ha conectado con el servidor",
                                Toast.LENGTH_LONG).show();
            }
        });
    }
}
