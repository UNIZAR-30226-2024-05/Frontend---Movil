package com.example.narratives.informacion;

import com.example.narratives.peticiones.amistad.peticiones.AmistadPeticionesResponse;
import com.example.narratives.peticiones.amistad.peticiones.HistorialPeticionConTipo;
import com.example.narratives.peticiones.amistad.peticiones.HistorialPeticionGenerica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InfoPeticiones {

    ArrayList<HistorialPeticionConTipo> peticionesOrdenadas;

    public void setPeticionesOrdenadas(AmistadPeticionesResponse peticiones){
        for(HistorialPeticionGenerica p : peticiones.getEnviadas()){
            peticionesOrdenadas.add(new HistorialPeticionConTipo(p,0));
        }

        for(HistorialPeticionGenerica p : peticiones.getRecibidas()){
            peticionesOrdenadas.add(new HistorialPeticionConTipo(p,1));
        }

        for(HistorialPeticionGenerica p : peticiones.getAceptadas()){
            peticionesOrdenadas.add(new HistorialPeticionConTipo(p,2));
        }

        for(HistorialPeticionGenerica p : peticiones.getRechazadas()){
            peticionesOrdenadas.add(new HistorialPeticionConTipo(p,3));
        }

        Collections.sort(peticionesOrdenadas, new Comparator<HistorialPeticionConTipo>() {
            @Override
            public int compare(HistorialPeticionConTipo a1, HistorialPeticionConTipo a2) {
                return 1;
            }
        });
    }
}
