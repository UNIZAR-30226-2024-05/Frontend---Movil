package com.example.narratives.peticiones.clubes;

public class ClubRequest {
    String nombre;
    String descripcion;
    Integer audiolibroID;

    public ClubRequest(String clubName) {
        this.nombre = clubName;
    }
    public ClubRequest(String clubName, String clubDesc, Integer audiolibro_id) {
        this.nombre = clubName;
        this.descripcion = clubDesc;
        this.audiolibroID = audiolibro_id;
    }

    public void setClubName(String clubName) {
        this.nombre = clubName;
    }
    public void setClubDesc(String clubDesc) {
        this.descripcion = clubDesc;
    }
    public void setAudiolibroId(Integer audiolibro_id) {
        this.audiolibroID = audiolibro_id;
    }
}
