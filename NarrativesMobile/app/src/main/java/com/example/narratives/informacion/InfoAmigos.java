package com.example.narratives.informacion;

import com.example.narratives.R;
import com.example.narratives.peticiones.users.amigos.AmigoSimple;

import java.util.ArrayList;

public class InfoAmigos {
    private static ArrayList<AmigoSimple> amigos;

    public static ArrayList<AmigoSimple> getAmigos() {
        return amigos;
    }

    public static void setAmigos(ArrayList<AmigoSimple> _amigos) {
        amigos = _amigos;
    }


    public static int getImageResourceFromImgCode(int img){

        if(img == 0){
            return R.drawable.pfp_perro;

        } else if(img == 2){
            return R.drawable.pfp_rana;

        } else if(img == 4){
            return R.drawable.pfp_pollo;

        } else if(img == 6){
            return R.drawable.pfp_buho;

        } else if(img == 8){
            return R.drawable.pfp_doraemon;

        } else if(img == 1){
            return R.drawable.pfp_gato;

        } else if(img == 3){
            return R.drawable.pfp_leon;

        } else if(img == 5){
            return R.drawable.pfp_vaca;

        } else if(img == 7){
            return R.drawable.pfp_perezoso;

        } else if(img == 9){
            return R.drawable.pfp_pikachu;

        }

        return -1;
    }

}
