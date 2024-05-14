package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.BibliotecaTituloGridAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.narratives.R;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;
import com.example.narratives.peticiones.autores.AutorDatosResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoAutorActivity extends AppCompatActivity {

    public static AutorDatosResponse autorActual;
    GridView gridView;
    BibliotecaTituloGridAdapter bibliotecaGridAdapter;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        retrofit = ApiClient.getRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.activity_info_autor);
        super.onCreate(savedInstanceState);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        gridView = (GridView) findViewById(R.id.gridViewBibliotecaAutor);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                peticionAudiolibrosId(position, id);
            }
        });




        FloatingActionButton botonCerrar = (FloatingActionButton) findViewById(R.id.botonVolverDesdeInfoLibro);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Volver al audiolibro
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializarAdaptadorBiblioteca(InfoAudiolibros.getAudiolibrosPorAutor(autorActual.getAutor().getNombre()));
        TextView textViewNombre = findViewById(R.id.textViewNombreAutor);
        textViewNombre.setText(autorActual.getAutor().getNombre());

        TextView textViewCiudad= findViewById(R.id.texCiudadNacimiento);
        textViewCiudad.setText(autorActual.getAutor().getCiudadnacimiento());

        TextView textViewGenero = findViewById(R.id.textViewGeneroMasEscrito);
        textViewGenero.setText(autorActual.getgeneroMasEscrito());

        TextView textViewInformacion = findViewById(R.id.textViewDescripcionInfoLibro);
        textViewInformacion.setText(autorActual.getAutor().getInformacion());

        double media = autorActual.getNotaMedia();
        // Determinar cuántas estrellas llenas se muestran
        int numEstrellasLlenas = (int) media; // Parte entera de la calificación
        int nivelEstrella = (int) (media * 10) % 10; // Nivel de la estrella llena (0-10)

        // Ocultar todas las estrellas llenas
        for (int i = 0; i < 5; i++) {
            ImageView imageView = obtenerImageViewEstrellaLlena(i);
            imageView.setVisibility(View.GONE);
        }

        // Mostrar las estrellas llenas necesarias
        for (int i = 0; i < numEstrellasLlenas; i++) {
            ImageView imageView = obtenerImageViewEstrellaLlena(i);
            if (imageView != null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.getDrawable().setLevel(1000); // Estrella llena al máximo
            }
        }
        // Mostrar una fracción de la última estrella llena
        if (nivelEstrella > 0 && numEstrellasLlenas < 5) {
            ImageView imageView = obtenerImageViewEstrellaLlena(numEstrellasLlenas);
            VectorDrawableCompat drawable = VectorDrawableCompat.create(getResources(), R.drawable.icono_estrella_llena, null);
            imageView.setVisibility(View.VISIBLE);

            if (drawable != null) {
                int w = drawable.getIntrinsicWidth();
                int h = drawable.getIntrinsicHeight();

                Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

                drawable.draw(canvas);

                double anchuraCorte = 12.5 + nivelEstrella*2.5;

                Bitmap bitmapRecortado = Bitmap.createBitmap(bitmap, 0, 0, (int) anchuraCorte, h);

                imageView.setImageBitmap(bitmapRecortado);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();

                //Re ajustar estrella porque se mueve tras recorte
                //int offset = (10-nivelEstrella)*2; // Hacer case. Para .5 es 10 y para .7 es 6
                int offset = 15-nivelEstrella;
                int offsetPx = (int) (offset * getResources().getDisplayMetrics().density);
                layoutParams.leftMargin -= offsetPx;
                imageView.setLayoutParams(layoutParams);
            }
        }
    }

    private void inicializarAdaptadorBiblioteca(ArrayList<AudiolibroItem> libros){
        bibliotecaGridAdapter = new BibliotecaTituloGridAdapter(this, libros);

        gridView.setAdapter(bibliotecaGridAdapter);

    }

    private void peticionAudiolibrosId(int position, long idGrid){
        AudiolibroItem audiolibro = (AudiolibroItem) bibliotecaGridAdapter.getItem(position);

        Call<AudiolibroEspecificoResponse> llamada = retrofitInterface.ejecutarAudiolibrosId(ApiClient.getUserCookie(), audiolibro.getId());
        llamada.enqueue(new Callback<AudiolibroEspecificoResponse>() {
            @Override
            public void onResponse(Call<AudiolibroEspecificoResponse> call, Response<AudiolibroEspecificoResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoLibroActivity.audiolibroActual = response.body();
                    abrirInfoLibro();

                } else if(codigo == 409) {
                    Toast.makeText(InfoAutorActivity.this, "No hay ningún audiolibro con ese ID", Toast.LENGTH_LONG).show();

                } else if(codigo == 500) {
                    Toast.makeText(InfoAutorActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(InfoAutorActivity.this, "Error desconocido (AudiolibrosId): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AudiolibroEspecificoResponse> call, Throwable t) {
                Toast.makeText(InfoAutorActivity.this, "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirInfoLibro() {
        Intent intent = new Intent(InfoAutorActivity.this, InfoLibroActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private ImageView obtenerImageViewEstrellaLlena(int posicion) {
        switch (posicion) {
            case 0:
                return findViewById(R.id.imageViewEstrella1ValoracionInfoAutorLlena);
            case 1:
                return findViewById(R.id.imageViewEstrella2ValoracionInfoAutorLlena);
            case 2:
                return findViewById(R.id.imageViewEstrella3ValoracionInfoAutorLlena);
            case 3:
                return findViewById(R.id.imageViewEstrella4ValoracionInfoAutorLLena);
            case 4:
                return findViewById(R.id.imageViewEstrella5ValoracionInfoAutorLlena);
            default:
                return null;
        }
    }
}



