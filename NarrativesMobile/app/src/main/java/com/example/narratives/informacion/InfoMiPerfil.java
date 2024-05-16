package com.example.narratives.informacion;

import com.example.narratives.R;

public class InfoMiPerfil {
    private static int user_id;
    private static String username;
    private String mail;
    private String img = "0";

    public int getImgResource() {
        if (img == null) {
            return R.drawable.icono_imagen_estandar_foreground;
        }

        switch (img) {
            case "0":
                return R.drawable.pfp_perro;
            case "2":
                return R.drawable.pfp_rana;
            case "4":
                return R.drawable.pfp_pollo;
            case "6":
                return R.drawable.pfp_buho;
            case "8":
                return R.drawable.pfp_doraemon;
            case "1":
                return R.drawable.pfp_gato;
            case "3":
                return R.drawable.pfp_leon;
            case "5":
                return R.drawable.pfp_vaca;
            case "7":
                return R.drawable.pfp_perezoso;
            case "9":
                return R.drawable.pfp_pikachu;
        }

        return R.drawable.icono_imagen_estandar_foreground;
    }

    public int getImgImageView() {
        if (img == null) {
            return -1;
        }

        switch (img) {
            case "0":
                return R.id.imageViewFotoPerfilPerro;
            case "2":
                return R.id.imageViewFotoPerfilRana;
            case "4":
                return R.id.imageViewFotoPerfilPollo;
            case "6":
                return R.id.imageViewFotoPerfilBuho;
            case "8":
                return R.id.imageViewFotoPerfilDoraemon;
            case "1":
                return R.id.imageViewFotoPerfilGato;
            case "3":
                return R.id.imageViewFotoPerfilLeon;
            case "5":
                return R.id.imageViewFotoPerfilVaca;
            case "7":
                return R.id.imageViewFotoPerfilPerezoso;
            case "9":
                return R.id.imageViewFotoPerfilPikachu;
        }

        return -1;
    }

    public static String getImgString(int id) {
        if (id == R.id.imageViewFotoPerfilPerro) {
            return "0";
        } else if (id == R.id.imageViewFotoPerfilRana) {
            return "2";
        } else if (id == R.id.imageViewFotoPerfilPollo) {
            return "4";
        } else if (id == R.id.imageViewFotoPerfilBuho) {
            return "6";
        } else if (id == R.id.imageViewFotoPerfilDoraemon) {
            return "8";
        } else if (id == R.id.imageViewFotoPerfilGato) {
            return "1";
        } else if (id == R.id.imageViewFotoPerfilLeon) {
            return "3";
        } else if (id == R.id.imageViewFotoPerfilVaca) {
            return "5";
        } else if (id == R.id.imageViewFotoPerfilPerezoso) {
            return "7";
        } else if (id == R.id.imageViewFotoPerfilPikachu) {
            return "9";
        }

        return "error";
    }

    public static void setId(int _user_id) {
        user_id = _user_id;
    }

    public static int getId() {
        return user_id;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String _img){
        this.img = _img;
    }

    public static String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String _mail) {
        this.mail = _mail;
    }

    public void setAllInfoNull(){
        this.setId(-1);
        this.setUsername(null);
        this.setImg(null);
        this.setMail(null);
    }
}
