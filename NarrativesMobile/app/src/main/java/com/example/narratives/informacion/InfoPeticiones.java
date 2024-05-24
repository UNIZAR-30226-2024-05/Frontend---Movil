package com.example.narratives.informacion;

import com.example.narratives.R;
import com.example.narratives.peticiones.amistad.solicitudes.AmistadPeticionesResponse;
import com.example.narratives.peticiones.amistad.solicitudes.HistorialPeticionConTipo;
import com.example.narratives.peticiones.amistad.solicitudes.HistorialPeticionGenerica;

import java.util.ArrayList;

public class InfoPeticiones {

    public static ArrayList<HistorialPeticionGenerica> enviadas;
    public static ArrayList<HistorialPeticionGenerica> recibidas;
    public static ArrayList<HistorialPeticionGenerica> aceptadas;
    public static ArrayList<HistorialPeticionGenerica> rechazadas;
    public static ArrayList<HistorialPeticionConTipo> peticionesOrdenadas;

    public static void setPeticiones(AmistadPeticionesResponse peticiones){
        peticionesOrdenadas = new ArrayList<HistorialPeticionConTipo>();

        if(peticiones == null){
            enviadas = new ArrayList<HistorialPeticionGenerica>();
            recibidas = new ArrayList<HistorialPeticionGenerica>();
            aceptadas = new ArrayList<HistorialPeticionGenerica>();
            rechazadas = new ArrayList<HistorialPeticionGenerica>();

        } else {
            if(peticiones.getEnviadas() != null){
                enviadas = peticiones.getEnviadas();
                for (HistorialPeticionGenerica p : peticiones.getEnviadas()) {
                    peticionesOrdenadas.add(new HistorialPeticionConTipo(p, 0));
                }
            } else {
                enviadas = new ArrayList<HistorialPeticionGenerica>();
            }

            if (peticiones.getRecibidas() != null){
                recibidas = peticiones.getRecibidas();
                for (HistorialPeticionGenerica p : peticiones.getRecibidas()) {
                    peticionesOrdenadas.add(new HistorialPeticionConTipo(p, 1));
                }
            } else {
                recibidas = new ArrayList<HistorialPeticionGenerica>();
            }

            if(peticiones.getAceptadas() != null){
                aceptadas = peticiones.getAceptadas();
                for (HistorialPeticionGenerica p : peticiones.getAceptadas()) {
                    peticionesOrdenadas.add(new HistorialPeticionConTipo(p, 2));
                }
            } else {
                aceptadas = new ArrayList<HistorialPeticionGenerica>();
            }

            if(peticiones.getRechazadas() != null){
                rechazadas = peticiones.getRechazadas();
                for (HistorialPeticionGenerica p : peticiones.getRechazadas()) {
                    peticionesOrdenadas.add(new HistorialPeticionConTipo(p, 3));
                }
            } else {
                rechazadas = new ArrayList<HistorialPeticionGenerica>();
            }

/*
            Collections.sort(peticionesOrdenadas, new Comparator<HistorialPeticionConTipo>() {
                @Override
                public int compare(HistorialPeticionConTipo a1, HistorialPeticionConTipo a2) {
                    return a1.getFecha().comparar(a2.getFecha());
                }
            });*/

        }
    }


    public static int getImageResourceFromCode(int i){
        if(i == 0){
            return R.drawable.icono_enviar;
        } else if(i == 1){
            return R.drawable.icono_recibir;
        } else if(i == 2){
            return R.drawable.icono_aceptar_soli;
        } else if(i == 3){
            return R.drawable.icono_cruz_cerrar;
        } else {
            return R.drawable.icono_imagen_estandar_foreground;
        }
    }


    public static ArrayList<HistorialPeticionGenerica> getEnviadas() {
        return enviadas;
    }

    public static void setEnviadas(ArrayList<HistorialPeticionGenerica> enviadas) {
        InfoPeticiones.enviadas = enviadas;
    }

    public static ArrayList<HistorialPeticionGenerica> getRecibidas() {
        return recibidas;
    }

    public static void setRecibidas(ArrayList<HistorialPeticionGenerica> recibidas) {
        InfoPeticiones.recibidas = recibidas;
    }

    public static ArrayList<HistorialPeticionGenerica> getAceptadas() {
        return aceptadas;
    }

    public static void setAceptadas(ArrayList<HistorialPeticionGenerica> aceptadas) {
        InfoPeticiones.aceptadas = aceptadas;
    }

    public static ArrayList<HistorialPeticionGenerica> getRechazadas() {
        return rechazadas;
    }

    public static void setRechazadas(ArrayList<HistorialPeticionGenerica> rechazadas) {
        InfoPeticiones.rechazadas = rechazadas;
    }

    public static ArrayList<HistorialPeticionConTipo> getPeticionesOrdenadas() {
        return peticionesOrdenadas;
    }

    public static void setPeticionesOrdenadas(ArrayList<HistorialPeticionConTipo> peticionesOrdenadas) {
        InfoPeticiones.peticionesOrdenadas = peticionesOrdenadas;
    }
}
