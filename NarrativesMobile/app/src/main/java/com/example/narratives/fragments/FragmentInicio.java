package com.example.narratives.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.biblioteca.BibliotecaGridAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.menuprincipal.MenuPrincipalGridAdapter;

import java.util.ArrayList;

import retrofit2.Retrofit;


public class FragmentInicio extends Fragment {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    GridView gridView;
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
        gridView = (GridView) getView().findViewById(R.id.gridViewBibliotecaGeneral);

        obtenerAudiolibrosEjemplo();
        //obtenerTodosLosAudiolibros();
        MenuPrincipalGridAdapter = new MenuPrincipalGridAdapter(getContext(), InfoAudiolibros.getTodosLosAudiolibros());

        gridView.setAdapter(MenuPrincipalGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mostrarPopupInfoLibro(position);
            }
        });

    public void setMejoresValorados(){

    }

    public void setRomance() {
        ImageView ivRomance1, ivRomance2, ivRomance3, ivRomance4;
        TextView tvRomance1, tvRomance2, tvRomance3, tvRomance4;
        ArrayList<ImageView> imagenesRomance;


        View rootView = getView();

        if (rootView != null) {
            ivRomance1 = rootView.findViewById(R.id.imageViewRomance1);
            ivRomance2 = rootView.findViewById(R.id.imageViewRomance2);
            ivRomance3 = rootView.findViewById(R.id.imageViewRomance3);
            ivRomance4 = rootView.findViewById(R.id.imageViewRomance4);

            tvRomance1 = rootView.findViewById(R.id.textViewRomance1);
            tvRomance2 = rootView.findViewById(R.id.textViewRomance2);
            tvRomance3 = rootView.findViewById(R.id.textViewRomance3);
            tvRomance4 = rootView.findViewById(R.id.textViewRomance4);

            //CAMBIAR LAS IMAGENES LUEGO POR LA CONSULTA QUE SE REALICE
            ivRomance1.setImageResource(R.drawable.icono_imagen_estandar_foreground);
            ivRomance2.setImageResource(R.drawable.icono_imagen_estandar_foreground);
            ivRomance3.setImageResource(R.drawable.icono_imagen_estandar_foreground);
            ivRomance4.setImageResource(R.drawable.icono_imagen_estandar_foreground);

            tvRomance1.setText("9.0");
            tvRomance2.setText("9.0");
            tvRomance3.setText("9.0");
            tvRomance4.setText("9.0");
        }
    }

    public void setCienciaFiccion(){

    }
}