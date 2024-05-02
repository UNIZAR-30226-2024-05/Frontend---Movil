package com.example.narratives.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.narratives.adaptadores.ColeccionesAdapter;
import com.example.narratives.adaptadores.AudiolibrosColeccionAdapter;
import com.example.narratives.informacion.InfoColecciones;
import com.example.narratives.informacion.InfoMiPerfil;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.colecciones.GestionColeccionRequest;
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

public class ColeccionesActivity extends AppCompatActivity {
    private ColeccionesAdapter coleccionesAdapter;
    private ArrayList<ColeccionesItem> coleccionesList;
    static ColeccionEspecificaResult coleccionActual;
    private TextView textViewGuardadaSiNo;
    private Button buttonGuardarQuitarColeccion;
    private PopupWindow popupWindow;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.colecciones);

        retrofitInterface = ApiClient.getRetrofitInterface();

        EditText editTextBuscadorColecciones = findViewById(R.id.editTextBuscadorColecciones);
        ListView listViewListaColecciones = findViewById(R.id.listViewListaColecciones);

        coleccionesList = InfoColecciones.getTodasLasColecciones();
        if (coleccionesList == null || coleccionesList.isEmpty()) {
            coleccionesList = new ArrayList<>();
            obtenerColecciones();
        }

        coleccionesAdapter = new ColeccionesAdapter(this, android.R.layout.simple_list_item_1, coleccionesList);
        listViewListaColecciones.setAdapter(coleccionesAdapter);

        listViewListaColecciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obtenerInfoColeccion(coleccionesList.get(position).getId());
            }
        });

        editTextBuscadorColecciones.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                coleccionesAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        FloatingActionButton botonVolver = findViewById(R.id.botonVolverDesdeListaColecciones);
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton botonAnadir = findViewById(R.id.botonAnadirNuevaColeccion);
        botonAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarPopUpNuevaColeccion();
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
                        Toast.makeText(ColeccionesActivity.this, "Resultado de colecciones nulo", Toast.LENGTH_LONG).show();
                    } else {
                        InfoColecciones.setTodasLasColecciones(coleccionesResult);
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
            public void onResponse(@NonNull Call<ColeccionEspecificaResult> call, @NonNull Response<ColeccionEspecificaResult> response) {
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

    private void mostrarPopUpNuevaColeccion() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewNuevaColeccion = inflater.inflate(R.layout.popup_anadir_coleccion, null);

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
            AlertDialog.Builder builder = new AlertDialog.Builder(ColeccionesActivity.this, R.style.ErrorAlertDialogStyle);
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
                    coleccionesList.clear();
                    obtenerColecciones();
                    Toast.makeText(ColeccionesActivity.this, "Colección añadida", Toast.LENGTH_LONG).show();
                } else if (response.code() == 400) {
                    Toast.makeText(ColeccionesActivity.this, "Título existente", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(ColeccionesActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ColeccionesActivity.this, "Error desconocido " + response.code(), Toast.LENGTH_LONG).show();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ColeccionesActivity.this);
                    builder.setTitle("¿Está seguro de eliminar esta colección?");
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
            }
        });

        TextView textViewSinAudiolibrosEnColeccion = viewInfoColeccion.findViewById(R.id.textViewSinAudiolibrosEnColeccion);

        ArrayList<AudiolibrosColeccionItem> audiolibrosColeccionList = coleccionActual.getAudiolibros();
        if (!audiolibrosColeccionList.isEmpty()) {
            textViewSinAudiolibrosEnColeccion.setVisibility(View.GONE);
            AudiolibrosColeccionAdapter audiolibrosColeccionAdapter = new AudiolibrosColeccionAdapter(this, R.layout.item_audiolibros_coleccion, audiolibrosColeccionList);
            ListView listViewListaAudiolibrosColeccion = viewInfoColeccion.findViewById(R.id.listViewListaAudiolibrosColeccion);
            listViewListaAudiolibrosColeccion.setAdapter(audiolibrosColeccionAdapter);
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
        GestionColeccionRequest request = new GestionColeccionRequest();
        request.setCollectionId(coleccionActual.getColeccion().getId());

        Call<GenericMessageResult> call = retrofitInterface.ejecutarColeccionesFriend(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                if (response.isSuccessful()) {
                    coleccionesList.clear();
                    obtenerColecciones();
                    coleccionActual.setGuardada(true);
                    textViewGuardadaSiNo.setText("SI");
                    buttonGuardarQuitarColeccion.setText("QUITAR");
                    Toast.makeText(ColeccionesActivity.this, "Colección añadida", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(ColeccionesActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ColeccionesActivity.this, "Error desconocido " + response.code(), Toast.LENGTH_LONG).show();
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
        GestionColeccionRequest request = new GestionColeccionRequest();
        request.setCollectionId(coleccionActual.getColeccion().getId());

        Call<GenericMessageResult> call = retrofitInterface.ejecutarColeccionesRemove(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                if (response.isSuccessful()) {
                    coleccionesList.clear();
                    obtenerColecciones();
                    if (coleccionActual.getPropietarioUsername().equals(InfoMiPerfil.getUsername())) {
                        popupWindow.dismiss();
                    } else {
                        coleccionActual.setGuardada(false);
                        textViewGuardadaSiNo.setText("NO");
                        buttonGuardarQuitarColeccion.setText("GUARDAR");
                    }
                    Toast.makeText(ColeccionesActivity.this, "Colección eliminada", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(ColeccionesActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ColeccionesActivity.this, "Error desconocido " + response.code(), Toast.LENGTH_LONG).show();
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
}
