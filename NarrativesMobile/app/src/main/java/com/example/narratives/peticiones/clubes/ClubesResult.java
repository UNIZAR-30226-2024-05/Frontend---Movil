package com.example.narratives.peticiones.clubes;

import java.util.ArrayList;

public class ClubesResult {
    String message;
    ArrayList<Club> listaClubes;

    public String getMessage() {
        return message;
    }

    public ArrayList<Club> getClubes() {
        return this.listaClubes;
    }
}
