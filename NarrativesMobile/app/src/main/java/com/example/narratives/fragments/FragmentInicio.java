package com.example.narratives.fragments;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.menuprincipal.MenuPrincipalGridAdapter;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.Audiolibro;
import com.example.narratives.peticiones.AudiolibrosResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Retrofit;


public class FragmentInicio extends Fragment {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    RecyclerView RecyclerView;
    MenuPrincipalGridAdapter MenuPrincipalGridAdapter;
    String generoLibrosMostrados;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        retrofit = ApiClient.getRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        obtenerAudiolibrosEjemplo();
        //obtenerTodosLosAudiolibros();
        MenuPrincipalGridAdapter = new MenuPrincipalGridAdapter(getContext(), InfoAudiolibros.getTodosLosAudiolibros());

        GridView gridView = (GridView) getView().findViewById(R.id.gridViewMenuPrincipal);
        gridView.setAdapter(MenuPrincipalGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mostrarPopupInfoLibro(position);
            }
        });

    }

    private void mostrarPopupInfoLibro(int position){
        esconderTeclado();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInfoLibro = inflater.inflate(R.layout.popup_info_libro, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        //PRUEBA, habrá que conseguir el libro según el género en 'generoLibrosMostrados'
        Audiolibro audiolibro = (Audiolibro) MenuPrincipalGridAdapter.getItem(position);

        ImageView imageViewPortada = viewInfoLibro.findViewById(R.id.imageViewPortadaInfoLibro);
        Glide
                .with(getContext())
                .load(audiolibro.getImg())
                .centerCrop()
                .placeholder(R.drawable.icono_imagen_estandar_foreground)
                .into(imageViewPortada);

        TextView textViewTitulo = viewInfoLibro.findViewById(R.id.textViewTituloInfoLibro);
        textViewTitulo.setText(audiolibro.getTitulo());


        PopupWindow popupWindow=new PopupWindow(viewInfoLibro,width,height, true);
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
    }

    private void esconderTeclado() {
        if(getActivity().getCurrentFocus() != null){
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

        ArrayList<Audiolibro> audiolibros = new ArrayList<>();
        for(int i = 0; i < titulos.length; i++){
            Audiolibro a = new Audiolibro(i, titulos[i], i, "descripcion", portadas[i]);
            audiolibros.add(a);
        }

        InfoAudiolibros.setTodosLosAudiolibros(audiolibros);
    }
}