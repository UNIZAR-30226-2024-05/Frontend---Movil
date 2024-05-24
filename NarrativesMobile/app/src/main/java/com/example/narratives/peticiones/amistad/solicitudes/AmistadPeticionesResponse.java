package com.example.narratives.peticiones.amistad.solicitudes;

import java.util.ArrayList;

public class AmistadPeticionesResponse {

    ArrayList<HistorialPeticionGenerica> enviadas;
    ArrayList<HistorialPeticionGenerica> recibidas;
    ArrayList<HistorialPeticionGenerica> aceptadas;
    ArrayList<HistorialPeticionGenerica> rechazadas;

    public ArrayList<HistorialPeticionGenerica> getEnviadas() {
        return enviadas;
    }

    public void setEnviadas(ArrayList<HistorialPeticionGenerica> enviadas) {
        this.enviadas = enviadas;
    }

    public ArrayList<HistorialPeticionGenerica> getRecibidas() {
        return recibidas;
    }

    public void setRecibidas(ArrayList<HistorialPeticionGenerica> recibidas) {
        this.recibidas = recibidas;
    }

    public ArrayList<HistorialPeticionGenerica> getAceptadas() {
        return aceptadas;
    }

    public void setAceptadas(ArrayList<HistorialPeticionGenerica> aceptadas) {
        this.aceptadas = aceptadas;
    }

    public ArrayList<HistorialPeticionGenerica> getRechazadas() {
        return rechazadas;
    }

    public void setRechazadas(ArrayList<HistorialPeticionGenerica> rechazadas) {
        this.rechazadas = rechazadas;
    }
}
