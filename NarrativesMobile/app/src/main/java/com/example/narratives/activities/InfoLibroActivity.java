package com.example.narratives.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.CapitulosAdapter;
import com.example.narratives.adaptadores.MarcapaginasAdapter;
import com.example.narratives.fragments.FragmentEscuchando;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.especifico.Capitulo;
import com.example.narratives.peticiones.audiolibros.especifico.Coleccion;
import com.example.narratives.peticiones.audiolibros.especifico.Genero;
import com.example.narratives.peticiones.audiolibros.especifico.Marcapaginas;
import com.example.narratives.peticiones.colecciones.AnadirEliminarAudiolibroDeColeccionRequest;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;
import com.example.narratives.peticiones.autores.AutorDatosResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoLibroActivity extends AppCompatActivity {

    public static AudiolibroEspecificoResponse audiolibroActual;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    MaterialButton escucharAudiolibro;
    MaterialButton comprarAudiolibro;

    private PopupWindow popupWindow;

    ArrayList<Marcapaginas> marcapaginas;
    ListView listaMarcapaginas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        retrofit = ApiClient.getRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.activity_info_libro);
        super.onCreate(savedInstanceState);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        marcapaginas = audiolibroActual.getMarcapaginas();

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
        textViewAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            //Ya lo cambiare a petición pero para probar que va a la pagina
            public void onClick(View view) {
                //abrirPaginaAutor();
                peticionAutorId();
            }
        });

        TextView textViewGeneros = findViewById(R.id.textViewGeneroInfoLibro);
        textViewGeneros.setText(getFormattedGenres(audiolibroActual.getGeneros()));


        FloatingActionButton botonCerrar = (FloatingActionButton) findViewById(R.id.botonVolverDesdeInfoLibro);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton botonMarcapaginas = (FloatingActionButton) findViewById(R.id.botonMarcapaginas);
        botonMarcapaginas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPopupMarcapaginas();
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

        comprarAudiolibro = (MaterialButton) findViewById(R.id.botonComprarEnAmazonInfoLibro);
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

        double media = audiolibroActual.getAudiolibro().getPuntuacion();
        int numEstrellasLlenas = (int) media;
        int nivelEstrella = (int) (media * 10) % 10; // Nivel de la estrella llena (0-10)

        for (int i = 0; i < 5; i++) {
            ImageView imageView = obtenerImageViewEstrellaLlena(i);
            imageView.setVisibility(View.GONE);
        }
        // Mostrar las estrellas llenas necesarias
        for (int i = 0; i < numEstrellasLlenas; i++) {
            ImageView imageView = obtenerImageViewEstrellaLlena(i);
            if (imageView != null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.getDrawable().setLevel(1000);
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

    public void abrirPaginaAutor() {
        Intent intent = new Intent(this, InfoAutorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
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

    private void peticionAutorId(){

        Call<AutorDatosResponse> llamada = retrofitInterface.ejecutarAutoresId(ApiClient.getUserCookie(), audiolibroActual.getAutor().getId());
        llamada.enqueue(new Callback<AutorDatosResponse>() {
            @Override
            public void onResponse(Call<AutorDatosResponse> call, Response<AutorDatosResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    InfoAutorActivity.autorActual = response.body();
                    abrirPaginaAutor();

                } else if(codigo == 404) {
                    Toast.makeText(InfoLibroActivity.this, "No hay ningún autor con ese ID", Toast.LENGTH_LONG).show();

                } else if(codigo == 500) {
                    Toast.makeText(InfoLibroActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(InfoLibroActivity.this, "Error desconocido (AutoresId): " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AutorDatosResponse> call, Throwable t) {
                Toast.makeText(InfoLibroActivity.this, "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirPopupMarcapaginas() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewMarcapaginas = inflater.inflate(R.layout.popup_marcapaginas, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        listaMarcapaginas = (ListView) viewMarcapaginas.findViewById(R.id.listViewListaMarcapaginas);
        MarcapaginasAdapter m = new MarcapaginasAdapter(this, R.layout.item_marcapaginas, marcapaginas,audiolibroActual);
        listaMarcapaginas.setAdapter(m);

        PopupWindow popupWindow = new PopupWindow(viewMarcapaginas, width, height, true);
        popupWindow.setAnimationStyle(0);

        ConstraintLayout layout = this.findViewById(R.id.info_libro_layout);
        layout.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
            }
        });

        listaMarcapaginas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                popupWindow.dismiss();
                //Buscar forma de pausar antes de reproducir ya que si se estaba reproduciendo algo, carga el nuevo libro pero salta al siguiente cap
                //MainActivity.fragmentoEscuchandoAbierto.pararPorCierreSesion();
                audiolibroActual.getUltimoMomento().setCapitulo(marcapaginas.get(position).getCapitulo());
                audiolibroActual.getUltimoMomento().setFecha(marcapaginas.get(position).getFecha());
                cargarYAbrirReproductor();
            }
        });
        listaMarcapaginas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                abrirEditarMarcapaginas(marcapaginas.get(position).getId(),marcapaginas.get(position).getCapitulo(),marcapaginas.get(position).getTitulo(),marcapaginas.get(position).getFecha());
                return true; // Devuelve true para indicar que el evento de clic largo ha sido manejado
            }
        });

        // Encuentra el botón en tu layout
        FloatingActionButton botonCerrar = (FloatingActionButton) viewMarcapaginas.findViewById(R.id.botonCerrarMarcapaginas);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }

    private ImageView obtenerImageViewEstrellaLlena(int posicion) {
        switch (posicion) {
            case 0:
                return findViewById(R.id.imageViewEstrella1ValoracionInfoLibroLlena);
            case 1:
                return findViewById(R.id.imageViewEstrella2ValoracionInfoLibroLlena);
            case 2:
                return findViewById(R.id.imageViewEstrella3ValoracionInfoLibroLlena);
            case 3:
                return findViewById(R.id.imageViewEstrella4ValoracionInfoLibroLLena);
            case 4:
                return findViewById(R.id.imageViewEstrella5ValoracionInfoLibroLlena);
            default:
                return null;
        }
    }
    public void abrirEditarMarcapaginas(int id, int capituloActual, String nombre, String fecha) {
        Intent intent = new Intent(this, EditMarcapaginasActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("listaCapitulos", audiolibroActual.getCapitulos());
        intent.putExtra("IdMarcapaginas", id);
        intent.putExtra("capituloActual", capituloActual);
        intent.putExtra("nombreMarcapaginas", nombre);
        intent.putExtra("timestamp", fecha);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
