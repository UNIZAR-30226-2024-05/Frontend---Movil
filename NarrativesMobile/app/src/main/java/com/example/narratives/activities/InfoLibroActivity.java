package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.especifico.Coleccion;
import com.example.narratives.peticiones.audiolibros.especifico.Genero;
import com.example.narratives.peticiones.colecciones.AnadirEliminarAudiolibroDeColeccionRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoLibroActivity extends AppCompatActivity {

    public static AudiolibroEspecificoResponse audiolibroActual;

    MaterialButton escucharAudiolibro;
    MaterialButton comprarAudiolibro;

    private PopupWindow popupWindow;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.activity_info_libro);
        super.onCreate(savedInstanceState);

        audiolibroActual = InfoAudiolibros.getAudiolibroActual();

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        retrofitInterface = ApiClient.getRetrofitInterface();

        ImageView imageViewPortada = findViewById(R.id.imageViewPortadaInfoLibro);

        Glide
                .with(this)
                .load(audiolibroActual.getAudiolibro().getImg())
                .centerCrop()
                .placeholder(R.drawable.icono_imagen_estandar_foreground)
                .into(imageViewPortada);

        TextView textViewTitulo = findViewById(R.id.textViewTituloInfoLibro);
        textViewTitulo.setText(audiolibroActual.getAudiolibro().getTitulo());

        TextView textViewDescripcion = findViewById(R.id.textViewDescripcionInfoLibro);
        textViewDescripcion.setText(audiolibroActual.getAudiolibro().getDescripcion());

        TextView textViewAutor = findViewById(R.id.textViewNombreAutorInfoLibro);
        textViewAutor.setText(audiolibroActual.getAutor().getNombre());

        TextView textViewGeneros = findViewById(R.id.textViewGeneroInfoLibro);
        textViewGeneros.setText(getFormattedGenres(audiolibroActual.getGeneros()));


        FloatingActionButton botonCerrar = (FloatingActionButton) findViewById(R.id.botonVolverDesdeInfoUsuario);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        escucharAudiolibro = findViewById(R.id.botonEscucharAudiolibroInfoLibro);
        escucharAudiolibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarYAbrirReproductor();
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoLibroActivity.this);
                //builder.setTitle("Dirígete al REPRODUCTOR...");
                builder.setMessage("El libro estará disponible en cuanto termine la carga.");
                builder.setIcon(R.drawable.icono_auriculares_pequeno);

                builder.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        abrirMenuMain();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                */
            }
        });

        comprarAudiolibro = (MaterialButton) findViewById(R.id.botonVerResenasInfoUsuario);
        comprarAudiolibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAmazonConLink();
            }
        });

        MaterialButton botonAnadirAColeccion = findViewById(R.id.botonAnadirAColeccionInfoLibro);
        botonAnadirAColeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarListaColecciones();
            }
        });
    }

    private void abrirAmazonConLink() {
        String urlBusquedaConTitulo = "https://www.amazon.es/s?k=libro+" +
                                        audiolibroActual.getAudiolibro().getTitulo().replace(" ","+")
                                        + "+" + audiolibroActual.getAutor().getNombre().replace(" ","+");

        Uri uri = Uri.parse(urlBusquedaConTitulo); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void cargarYAbrirReproductor(){
        MainActivity.fragmentoEscuchandoAbierto.inicializarLibro(audiolibroActual);
        MainActivity.abrirEscuchando = true;
        abrirMenuMain();
    }

    private String getFormattedGenres(ArrayList<Genero> generos){
        String result = "";

        for(int i = 0; i < generos.size(); i++){
            Genero genero = generos.get(i);

            result += genero.getNombre();

            if(i != (generos.size() - 1)){
                result += ", ";
            }
        }

        return result;
    }

    private void abrirMenuMain() {
        InfoAudiolibros.setAudiolibroActual(null);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private void mostrarListaColecciones() {
        ArrayList<Coleccion> coleccionesList = audiolibroActual.getColecciones();
        ArrayList<Coleccion> coleccionesSeleccionadasInicialmente = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Colecciones");

        final String[] titulosColecciones = new String[coleccionesList.size()];
        final boolean[] checkedItems = new boolean[coleccionesList.size()];

        for (int i = 0; i < coleccionesList.size(); i++) {
            titulosColecciones[i] = coleccionesList.get(i).getTitulo();
            checkedItems[i] = coleccionesList.get(i).getPertenece();
            if (checkedItems[i]) {
                coleccionesSeleccionadasInicialmente.add(coleccionesList.get(i));
            }
        }

        builder.setMultiChoiceItems(titulosColecciones, checkedItems, (dialog, which, isChecked) -> {
            checkedItems[which] = isChecked;
        });

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            for (int i = 0; i < coleccionesList.size(); i++) {
                Coleccion coleccion = coleccionesList.get(i);
                if (checkedItems[i]) {
                    if (!coleccionesSeleccionadasInicialmente.contains(coleccion)) {
                        anadirAudiolibroAColeccion(coleccion.getId(), audiolibroActual.getAudiolibro().getId());
                    }
                } else {
                    if (coleccionesSeleccionadasInicialmente.contains(coleccion)) {
                        eliminarAudiolibroDeColeccion(coleccion.getId(), audiolibroActual.getAudiolibro().getId());
                    }
                }
                coleccionesList.get(i).setPertenece(checkedItems[i]);
            }
            Toast.makeText(InfoLibroActivity.this, "Cambios realizados", Toast.LENGTH_LONG).show();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> { });

        builder.create().show();
    }

    private void anadirAudiolibroAColeccion(int coleccionId, int audiolibroId) {
        AnadirEliminarAudiolibroDeColeccionRequest request = new AnadirEliminarAudiolibroDeColeccionRequest();
        request.setAudiolibroId(audiolibroId);
        request.setColeccionId(coleccionId);

        Call<GenericMessageResult> call = retrofitInterface.ejecutarColeccionesAnadirAudiolibro(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                if (response.code() == 500) {
                    Toast.makeText(InfoLibroActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else if (response.code() != 200) {
                    Toast.makeText(InfoLibroActivity.this, "Error desconocido " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericMessageResult> call, @NonNull Throwable t) {
                Toast.makeText(InfoLibroActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(InfoLibroActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void eliminarAudiolibroDeColeccion(int coleccionId, int audiolibroId) {
        AnadirEliminarAudiolibroDeColeccionRequest request = new AnadirEliminarAudiolibroDeColeccionRequest();
        request.setAudiolibroId(audiolibroId);
        request.setColeccionId(coleccionId);

        Call<GenericMessageResult> call = retrofitInterface.ejecutarColeccionesEliminarAudiolibro(ApiClient.getUserCookie(), request);
        call.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                if (response.code() == 500) {
                    Toast.makeText(InfoLibroActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else if (response.code() != 200) {
                    Toast.makeText(InfoLibroActivity.this, "Error desconocido " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericMessageResult> call, @NonNull Throwable t) {
                Toast.makeText(InfoLibroActivity.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(InfoLibroActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
