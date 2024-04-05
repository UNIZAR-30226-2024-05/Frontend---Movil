package com.example.narratives.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.narratives.R;

import java.util.ArrayList;


public class FragmentInicio extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }


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