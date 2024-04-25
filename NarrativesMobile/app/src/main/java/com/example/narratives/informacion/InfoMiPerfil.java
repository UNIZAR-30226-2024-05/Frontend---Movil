package com.example.narratives.informacion;

import com.example.narratives.R;

public class InfoMiPerfil {

    private String username;
    private String mail;

    private String img = "0";

    public int getImgResource(){
        if(img == null){
            return R.drawable.icono_imagen_estandar_foreground;
        }

        if(img.equals("0")){
            return R.drawable.pfp_perro;

        } else if(img.equals("2")){
            return R.drawable.pfp_rana;

        } else if(img.equals("4")){
            return R.drawable.pfp_pollo;

        } else if(img.equals("6")){
            return R.drawable.pfp_buho;

        } else if(img.equals("8")){
            return R.drawable.pfp_doraemon;

        } else if(img.equals("1")){
            return R.drawable.pfp_gato;

        } else if(img.equals("3")){
            return R.drawable.pfp_leon;

        } else if(img.equals("5")){
            return R.drawable.pfp_vaca;

        } else if(img.equals("7")){
            return R.drawable.pfp_perezoso;

        } else if(img.equals("9")){
            return R.drawable.pfp_pikachu;

        }

        return R.drawable.icono_imagen_estandar_foreground;
    }

    public int getImgImageView(){
        if(img == null){
            return -1;
        }

        if(img.equals("0")){
            return R.id.imageViewFotoPerfilPerro;

        } else if(img.equals("2")){
            return R.id.imageViewFotoPerfilRana;

        } else if(img.equals("4")){
            return R.id.imageViewFotoPerfilPollo;

        } else if(img.equals("6")){
            return R.id.imageViewFotoPerfilBuho;

        } else if(img.equals("8")){
            return R.id.imageViewFotoPerfilDoraemon;

        } else if(img.equals("1")){
            return R.id.imageViewFotoPerfilGato;

        } else if(img.equals("3")){
            return R.id.imageViewFotoPerfilLeon;

        } else if(img.equals("5")){
            return R.id.imageViewFotoPerfilVaca;

        } else if(img.equals("7")){
            return R.id.imageViewFotoPerfilPerezoso;

        } else if(img.equals("9")){
            return R.id.imageViewFotoPerfilPikachu;

        }

        return -1;
    }

    public static String getImgString(int id){

        if(id == R.id.imageViewFotoPerfilPerro){
            return "0";

        } else if(id == R.id.imageViewFotoPerfilRana){
            return "2";

        } else if(id == R.id.imageViewFotoPerfilPollo){
            return "4";

        } else if(id == R.id.imageViewFotoPerfilBuho){
            return "6";

        } else if(id == R.id.imageViewFotoPerfilDoraemon){
            return "8";

        } else if(id == R.id.imageViewFotoPerfilGato){
            return "1";

        } else if(id == R.id.imageViewFotoPerfilLeon){
            return "3";

        } else if(id == R.id.imageViewFotoPerfilVaca){
            return "5";

        } else if(id == R.id.imageViewFotoPerfilPerezoso){
            return "7";

        } else if(id == R.id.imageViewFotoPerfilPikachu) {
            return "9";
        }

        return "error";
    }



    public String getImg() {
        return img;
    }

    public void setImg(String _img){
        this.img = _img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
