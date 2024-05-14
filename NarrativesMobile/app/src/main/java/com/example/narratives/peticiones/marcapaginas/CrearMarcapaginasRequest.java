package com.example.narratives.peticiones.marcapaginas;

public class CrearMarcapaginasRequest {

    private int marcapaginasID;
    private String titulo;
    private int capitulo;

    private String tiempo;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getCapitulo() {
        return capitulo;
    }

    public void setCapitulo(int capitulo) {
        this.capitulo = capitulo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public int getMarcapaginasID() {
        return marcapaginasID;
    }

    public void setMarcapaginasID(int marcapaginasID) {
        this.marcapaginasID = marcapaginasID;
    }


}
