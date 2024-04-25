package com.example.narratives.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.MainActivity;
import com.example.narratives.biblioteca.BibliotecaGridAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.especifico.Genero;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibroItem;
import com.example.narratives.peticiones.audiolibros.todos.AudiolibrosResult;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentBiblioteca extends Fragment {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    GridView gridView;
    BibliotecaGridAdapter bibliotecaGridAdapter;
    EditText buscador;
    AutoCompleteTextView filtros;
    ArrayAdapter<String> adapterFiltros;
    String generoLibrosMostrados;

    static AudiolibroEspecificoResponse audiolibroActual;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_biblioteca, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        retrofit = ApiClient.getRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();
        gridView = (GridView) getView().findViewById(R.id.gridViewBibliotecaGeneral);
        buscador = (EditText) getView().findViewById(R.id.editTextBuscadorGeneralBiblioteca);
        filtros = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextViewFiltrosBiblioteca);
        adapterFiltros = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, InfoAudiolibros.getGeneros());
        filtros.setText("Todos");
        generoLibrosMostrados = "todos";


        filtros.setAdapter(adapterFiltros);

        //obtenerAudiolibrosEjemplo();
        obtenerTodosLosAudiolibros();
        bibliotecaGridAdapter = new BibliotecaGridAdapter(getContext(), InfoAudiolibros.getTodosLosAudiolibros());

        gridView.setAdapter(bibliotecaGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                peticionAudiolibrosId(position);
            }
        });

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (FragmentBiblioteca.this).bibliotecaGridAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    private void obtenerTodosLosAudiolibros(){
        Call<AudiolibrosResult> llamada = retrofitInterface.ejecutarAudiolibros(ApiClient.getUserCookie());
        llamada.enqueue(new Callback<AudiolibrosResult>() {
            @Override
            public void onResponse(Call<AudiolibrosResult> call, Response<AudiolibrosResult> response) {
                int codigo = response.code();

                if (codigo == 200){
                    ArrayList<AudiolibroItem> audiolibrosResult = response.body().getAudiolibros();

                    if(audiolibrosResult == null){
                        Toast.makeText(getContext(), "Resultado de audiolibros nulo", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(getContext(), "Audiolibros recibidos OK", Toast.LENGTH_LONG).show();
                        InfoAudiolibros.setTodosLosAudiolibros(audiolibrosResult);
                    }

                } else if (codigo == 500){
                    Toast.makeText(getContext(), "Error del servidor",
                            Toast.LENGTH_LONG).show();

                } else if (codigo == 404){
                    Toast.makeText(getContext(), "Error 404 /audiolibros", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String error = jObjError.getString("error");
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Algo ha fallado obteniendo el error " + String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AudiolibrosResult> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor (obteniendo todos los libros)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void obtenerAudiolibrosEjemplo(){
        String[] portadas = {
                "https://narrativesarchivos.blob.core.windows.net/imagenes/ElCoco.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/ElHombreDelTrajeNegro.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/ElUmbralDeLaNoche.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/HarryPotter1.jpeg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/LaOdisea.png",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/OrgulloYPrejuicio.jpg",
                "https://narrativesarchivos.blob.core.windows.net/imagenes/Popsy.jpg"
        };

        String[] titulos = {
                "El coco",
                "El hombre de traje negro",
                "El umbral de la noche",
                "Harry Potter y la piedra filosofal",
                "La odisea",
                "Orgullo y prejuicio",
                "Popsy"
        };

        ArrayList<AudiolibroItem> audiolibros = new ArrayList<>();
        for(int i = 0; i < titulos.length; i++){
            AudiolibroItem a = new AudiolibroItem(i, titulos[i], i, "descripcion", portadas[i], "genero", 5);
            audiolibros.add(a);
        }

        InfoAudiolibros.setTodosLosAudiolibros(audiolibros);
    }

    private void mostrarPopupInfoLibro(){
        esconderTeclado();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInfoLibro = inflater.inflate(R.layout.popup_info_libro, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;


        ImageView imageViewPortada = viewInfoLibro.findViewById(R.id.imageViewPortadaInfoLibro);
        Glide
                .with(getContext())
                .load(audiolibroActual.getAudiolibro().getImg())
                .centerCrop()
                .placeholder(R.drawable.icono_imagen_estandar_foreground)
                .into(imageViewPortada);

        TextView textViewTitulo = viewInfoLibro.findViewById(R.id.textViewTituloInfoLibro);
        textViewTitulo.setText(audiolibroActual.getAudiolibro().getTitulo());

        TextView textViewDescripcion = viewInfoLibro.findViewById(R.id.textViewDescripcionInfoLibro);
        textViewDescripcion.setText(audiolibroActual.getAudiolibro().getDescripcion());

        TextView textViewAutor = viewInfoLibro.findViewById(R.id.textViewNombreAutorInfoLibro);
        textViewAutor.setText(audiolibroActual.getAutor().getNombre());

        TextView textViewGeneros = viewInfoLibro.findViewById(R.id.textViewGeneroInfoLibro);
        textViewGeneros.setText(getFormattedGenres(audiolibroActual.getGeneros()));

        PopupWindow popupWindow = new PopupWindow(viewInfoLibro,width,height, true);
        popupWindow.setAnimationStyle(0);

        FrameLayout layout = getActivity().findViewById(R.id.main_layout);
        layout.post(new Runnable(){
            @Override
            public void run(){
                popupWindow.showAtLocation(layout, Gravity.BOTTOM,0,0);
            }
        });


        FloatingActionButton botonCerrar = (FloatingActionButton) viewInfoLibro.findViewById(R.id.botonVolverDesdeInfoLibro);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        MaterialButton escucharAudiolibro = (MaterialButton) viewInfoLibro.findViewById(R.id.botonEscucharAudiolibroInfoLibro);
        escucharAudiolibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.fragmentoEscuchandoAbierto.inicializarLibro(audiolibroActual);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Dirígete al REPRODUCTOR...");
                builder.setMessage("El libro estará disponible en cuanto termine la carga.");
                builder.setIcon(R.drawable.icono_auriculares_pequeno);

                builder.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        popupWindow.dismiss();;
                    }
                });



                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void esconderTeclado() {
        if(getActivity().getCurrentFocus() != null){
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void peticionAudiolibrosId(int position){
        AudiolibroItem audiolibro = (AudiolibroItem) bibliotecaGridAdapter.getItem(position);

        Call<AudiolibroEspecificoResponse> llamada = retrofitInterface.ejecutarAudiolibrosId(ApiClient.getUserCookie(), audiolibro.getId());
        llamada.enqueue(new Callback<AudiolibroEspecificoResponse>() {
            @Override
            public void onResponse(Call<AudiolibroEspecificoResponse> call, Response<AudiolibroEspecificoResponse> response) {
                int codigo = response.code();

                if (response.code() == 200) {
                    audiolibroActual = response.body();
                    mostrarPopupInfoLibro();

                } else if(codigo == 409) {
                    Toast.makeText(getContext(), "No hay ningún audiolibro con ese ID", Toast.LENGTH_LONG).show();

                } else if(codigo == 500) {
                    Toast.makeText(getContext(), "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Error desconocido: " + String.valueOf(codigo), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AudiolibroEspecificoResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
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

}