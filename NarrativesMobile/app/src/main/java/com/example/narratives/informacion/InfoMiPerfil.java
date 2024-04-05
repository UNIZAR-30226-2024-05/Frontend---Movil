package com.example.narratives.informacion;

import com.example.narratives.R;

public class InfoMiPerfil {

    private String username;
    private String mail;

    private String photo = "perro";

    public int getPhotoResource(){
        if(photo == null){
            return R.drawable.icono_imagen_estandar_foreground;
        }

        if(photo.equals("perro")){
            return R.drawable.pfp_perro;

        } else if(photo.equals("rana")){
            return R.drawable.pfp_rana;

        } else if(photo.equals("pollo")){
            return R.drawable.pfp_pollo;

        } else if(photo.equals("buho")){
            return R.drawable.pfp_buho;

        } else if(photo.equals("doraemon")){
            return R.drawable.pfp_doraemon;

        } else if(photo.equals("gato")){
            return R.drawable.pfp_gato;

        } else if(photo.equals("leon")){
            return R.drawable.pfp_leon;

        } else if(photo.equals("vaca")){
            return R.drawable.pfp_vaca;

        } else if(photo.equals("perezoso")){
            return R.drawable.pfp_perezoso;

        } else if(photo.equals("pikachu")){
            return R.drawable.pfp_pikachu;

        }

        return R.drawable.icono_imagen_estandar_foreground;
    }

    public int getPhotoImageView(){
        if(photo == null){
            return -1;
        }

        if(photo.equals("perro")){
            return R.id.imageViewFotoPerfilPerro;

        } else if(photo.equals("rana")){
            return R.id.imageViewFotoPerfilRana;

        } else if(photo.equals("pollo")){
            return R.id.imageViewFotoPerfilPollo;

        } else if(photo.equals("buho")){
            return R.id.imageViewFotoPerfilBuho;

        } else if(photo.equals("doraemon")){
            return R.id.imageViewFotoPerfilDoraemon;

        } else if(photo.equals("gato")){
            return R.id.imageViewFotoPerfilGato;

        } else if(photo.equals("leon")){
            return R.id.imageViewFotoPerfilLeon;

        } else if(photo.equals("vaca")){
            return R.id.imageViewFotoPerfilVaca;

        } else if(photo.equals("perezoso")){
            return R.id.imageViewFotoPerfilPerezoso;

        } else if(photo.equals("pikachu")){
            return R.id.imageViewFotoPerfilPikachu;

        }

        return -1;
    }

    public static String getPhotoString(int id){

        if(id == R.id.imageViewFotoPerfilPerro){
            return "perro";

        } else if(id == R.id.imageViewFotoPerfilRana){
            return "rana";

        } else if(id == R.id.imageViewFotoPerfilPollo){
            return "pollo";

        } else if(id == R.id.imageViewFotoPerfilBuho){
            return "buho";

        } else if(id == R.id.imageViewFotoPerfilDoraemon){
            return "doraemon";

        } else if(id == R.id.imageViewFotoPerfilGato){
            return "gato";

        } else if(id == R.id.imageViewFotoPerfilLeon){
            return "leon";

        } else if(id == R.id.imageViewFotoPerfilVaca){
            return "vaca";

        } else if(id == R.id.imageViewFotoPerfilPerezoso){
            return "perezoso";

        } else if(id == R.id.imageViewFotoPerfilPikachu) {
            return "pikachu";
        }

        return "error";
    }

    public void setPhoto(String _photo){
        photo = _photo;
    }
}
