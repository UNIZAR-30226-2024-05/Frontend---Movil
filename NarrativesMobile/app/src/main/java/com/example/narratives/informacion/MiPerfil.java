package com.example.narratives.informacion;

import com.example.narratives.R;

public class MiPerfil {

    private String username;
    private String mail;

    private static String photo;

    public static int getPhotoResource(){
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
}
