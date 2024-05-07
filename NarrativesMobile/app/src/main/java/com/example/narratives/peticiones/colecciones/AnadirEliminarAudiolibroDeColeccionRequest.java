package com.example.narratives.peticiones.colecciones;

public class AnadirEliminarAudiolibroDeColeccionRequest {
    private int audiolibroId;
    private int coleccionId;

    public int getAudiolibroId() {
        return audiolibroId;
    }

    public void setAudiolibroId(int audiolibroId) {
        this.audiolibroId = audiolibroId;
    }

    public int getColeccionId() {
        return coleccionId;
    }

    public void setColeccionId(int coleccionId) {
        this.coleccionId = coleccionId;
    }
}
