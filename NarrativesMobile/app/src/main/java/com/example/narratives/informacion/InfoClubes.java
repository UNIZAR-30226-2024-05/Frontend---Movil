package com.example.narratives.informacion;

import android.util.Log;

import java.util.ArrayList;
import com.example.narratives.peticiones.clubes.Club;

public class InfoClubes {
    private static ArrayList<Club> todosLosClubes;
    public static ArrayList<Club> getTodosLosClubes() {
        if (todosLosClubes == null) {
            Log.d("LISTA1", "null");
        } else if (todosLosClubes.isEmpty()) {
            Log.d("LISTA1", "empty");
        } else {
            Log.d("LISTA1", todosLosClubes.get(0).getName());
        }
        return todosLosClubes;
    }

    public static void setTodosLosClubes(ArrayList<Club> _todosLosClubes) {
        if (_todosLosClubes == null) {
            Log.d("LISTA2", "null");
        } else if (_todosLosClubes.isEmpty()) {
            Log.d("LISTA2", "empty");
        } else {
            Log.d("LISTA2", _todosLosClubes.get(0).getName());
        }
        todosLosClubes = _todosLosClubes;
        if (todosLosClubes == null) {
            Log.d("LISTA3", "null");
        } else if (todosLosClubes.isEmpty()) {
            Log.d("LISTA3", "empty");
        } else {
            Log.d("LISTA3", todosLosClubes.get(0).getName());
        }
    }

    public static Club getClubById(int id) {
        for (Club club : todosLosClubes) {
            if (club.getId() == id) {
                return club;
            }
        }
        return null;
    }

    public static void addClub(Club club) {
        InfoClubes.todosLosClubes.add(club);
    }

    public static void removeClub(Club club) {
        InfoClubes.todosLosClubes.remove(club);
    }
    public static void removeClubById(int club_id) {
        removeClub(getClubById(club_id));
    }
}