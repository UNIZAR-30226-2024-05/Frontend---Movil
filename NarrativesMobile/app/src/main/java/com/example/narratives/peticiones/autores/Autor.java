package com.example.narratives.peticiones.autores;

public class Autor {
    int id;
    String nombre;
    String ciudadnacimiento;

    String informacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCiudadnacimiento() {
        return ciudadnacimiento;
    }

    public void setCiudadnacimiento(String ciudadnacimiento) {
        this.ciudadnacimiento = ciudadnacimiento;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }
}
