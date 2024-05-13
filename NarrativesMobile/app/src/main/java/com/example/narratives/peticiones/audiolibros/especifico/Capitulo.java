package com.example.narratives.peticiones.audiolibros.especifico;
import android.os.Parcel;
import android.os.Parcelable;

public class Capitulo implements Parcelable {
    int id;
    int numero;
    String nombre;
    String audio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    protected Capitulo(Parcel in) {
        id = in.readInt();
        numero = in.readInt();
        nombre = in.readString();
        audio = in.readString();
    }

    public static final Creator<Capitulo> CREATOR = new Creator<Capitulo>() {
        @Override
        public Capitulo createFromParcel(Parcel in) {
            return new Capitulo(in);
        }

        @Override
        public Capitulo[] newArray(int size) {
            return new Capitulo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(numero);
        dest.writeString(nombre);
        dest.writeString(audio);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
