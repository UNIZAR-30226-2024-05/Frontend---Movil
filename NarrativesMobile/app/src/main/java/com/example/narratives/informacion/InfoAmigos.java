package com.example.narratives.informacion;

import com.example.narratives.R;
import com.example.narratives.peticiones.amistad.amigos.AmigoSimple;
import com.example.narratives.peticiones.amistad.lista.UsuarioEstado;
import com.example.narratives.peticiones.users.perfiles.UserResponse;

import java.util.ArrayList;

public class InfoAmigos {

    private static UserResponse amigoActual;

    private static ArrayList<AmigoSimple> amigos;

    private static ArrayList<UsuarioEstado> usuariosEstado;


    public static ArrayList<AmigoSimple> getAmigos() {
        return amigos;
    }

    public static void setAmigos(ArrayList<AmigoSimple> _amigos) {
        amigos = _amigos;
    }

    public static ArrayList<UsuarioEstado> getUsuariosEstado() {
        return usuariosEstado;
    }

    public static void setUsuariosEstado(ArrayList<UsuarioEstado> usuariosEstado) {
        InfoAmigos.usuariosEstado = usuariosEstado;
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

    public static String getEstadoStringFromId(int estado){
        if(estado == 0){
            return "¡Sois amigos!";
        } else if(estado == 1){
            return "No sois amigos";
        } else if(estado == 2){
            return "Petición de amistad enviada";
        } else if(estado == 3){
            return "¡Petición de amistad recibida!";
        }

        return "Estado indeterminado";
    }

    public static ArrayList<UsuarioEstado> getUsuariosEstadoNoAmigos(){
        ArrayList<UsuarioEstado> noAmigos = new ArrayList<UsuarioEstado>();

        for(UsuarioEstado usuario : usuariosEstado){
            if(usuario.getEstado() != 0){
                noAmigos.add(usuario);
            }
        }

        return noAmigos;
    }



    public static ArrayList<UsuarioEstado> getUsuariosEstadoAmigos(){
        ArrayList<UsuarioEstado> enviados = new ArrayList<UsuarioEstado>();

        for(UsuarioEstado usuario : usuariosEstado){
            if(usuario.getEstado() == 0){
                enviados.add(usuario);
            }
        }

        return enviados;
    }

    public static ArrayList<UsuarioEstado> getUsuariosEstadoEnviados(){
        ArrayList<UsuarioEstado> enviados = new ArrayList<UsuarioEstado>();

        for(UsuarioEstado usuario : usuariosEstado){
            if(usuario.getEstado() == 2){
                enviados.add(usuario);
            }
        }

        return enviados;
    }

    public static ArrayList<UsuarioEstado> getUsuariosEstadoRecibidos(){
        ArrayList<UsuarioEstado> recibidos = new ArrayList<UsuarioEstado>();

        for(UsuarioEstado usuario : usuariosEstado){
            if(usuario.getEstado() == 3){
                recibidos.add(usuario);
            }
        }

        return recibidos;
    }

    public static UserResponse getAmigoActual() {
        return amigoActual;
    }

    public static void setAmigoActual(UserResponse amigoActual) {
        InfoAmigos.amigoActual = amigoActual;
    }
}
