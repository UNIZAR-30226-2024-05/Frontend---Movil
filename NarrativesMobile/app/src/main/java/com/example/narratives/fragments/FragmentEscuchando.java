package com.example.narratives.fragments;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.narratives.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.TimeUnit;


public class FragmentEscuchando extends Fragment {

    MediaPlayer mediaPlayer;

    FloatingActionButton fabPlay;
    FloatingActionButton fabPause;

    FloatingActionButton fabAvanzar;

    FloatingActionButton fabRetrasar;
    FloatingActionButton fabSiguienteCap;
    FloatingActionButton fabAnteriorCap;

    TextView numerosIzquierda;
    TextView numerosDerecha;

    static ConstraintLayout reproduceUnAudiolibro;
    static ConstraintLayout cargandoAudiolibro;

    ConstraintLayout reproductor;

    ImageView iconoCargando;

    SeekBar seekBar;

    Handler handler;

    UpdateSeekBar updateSeekBar;

    String[] capitulos = {"https://narrativesarchivos.blob.core.windows.net/audios/LaOdisea_1.mp3", "https://narrativesarchivos.blob.core.windows.net/audios/LaOdisea_2.mp3", "https://narrativesarchivos.blob.core.windows.net/audios/LaOdisea_3.mp3"};
    boolean primerAudio = true;
    boolean irAMinutoConcreto = false;

    boolean reproductorCargado = false;

    boolean libroReiniciado = false;
    int capituloActual = 0;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_escuchando, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        inicializarElementosReproductor();

        //mostrarReproduceUnAudiolibro();
        esconderReproduceUnAudiolibro();
        esconderReproductor();
        esconderCargandoAudiolibro();



        //prepararAudio(capituloActual);
        //esconderReproduceUnAudiolibro();

        //mostrarCargandoAudiolibro();

        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reanudarMusica();
            }
        });

        fabPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pararMusica();
            }
        });

        fabAvanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
            }
        });

        fabRetrasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
            }
        });


        fabSiguienteCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(0);
                prepararAudio(capituloActual + 1);
            }
        });

        fabAnteriorCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(0);
                prepararAudio(capituloActual - 1);
            }
        });
    }



    private void inicializarElementosReproductor() {
        numerosIzquierda = getView().findViewById(R.id.textViewSeekBarIzquierdaEscuchando);
        numerosDerecha= getView().findViewById(R.id.textViewSeekBarDerechaEscuchando);


        fabPlay = (FloatingActionButton) getView().findViewById(R.id.botonPlayEscuchando);
        fabPause = (FloatingActionButton) getView().findViewById(R.id.botonPauseEscuchando);
        fabAvanzar = (FloatingActionButton) getView().findViewById(R.id.botonAvanzarDiezEscuchando);
        fabRetrasar = (FloatingActionButton) getView().findViewById(R.id.botonRetrasarDiezEscuchando);
        fabSiguienteCap = (FloatingActionButton) getView().findViewById(R.id.botonSiguienteCapituloEscuchando);
        fabAnteriorCap = (FloatingActionButton) getView().findViewById(R.id.botonAnteriorCapituloEscuchando);

        fabPause = (FloatingActionButton) getView().findViewById(R.id.botonPauseEscuchando);
        fabPlay = (FloatingActionButton) getView().findViewById(R.id.botonPlayEscuchando);
        fabPause.setEnabled(false);

        reproductor = getView().findViewById(R.id.constraintLayoutReproductor);
        reproduceUnAudiolibro = getView().findViewById(R.id.constraintLayoutReproduceUnAudiolibro);
        cargandoAudiolibro = getView().findViewById(R.id.constraintLayoutCargandoAudiolibro);

        iconoCargando = (ImageView) getView().findViewById(R.id.imageViewCargandoAudiolibro);

        handler = new Handler();
    }


    private void mostrarReproductor(){
        reproductor.setEnabled(true);
        fabPause.setEnabled(true);
        fabPlay.setEnabled(true);
        fabRetrasar.setEnabled(true);
        fabAvanzar.setEnabled(true);
        fabAnteriorCap.setEnabled(true);
        fabSiguienteCap.setEnabled(true);
    }

    private void esconderReproductor() {
        if(reproductor.isEnabled()) {
            reproductor.setEnabled(false);
        }

        fabPause.setEnabled(false);
        fabPlay.setEnabled(false);
        fabRetrasar.setEnabled(false);
        fabAvanzar.setEnabled(false);
        fabAnteriorCap.setEnabled(false);
        fabSiguienteCap.setEnabled(false);
    }

    private void mostrarReproduceUnAudiolibro(){
        reproduceUnAudiolibro.setEnabled(true);
    }

    private void esconderReproduceUnAudiolibro() {
        if(reproduceUnAudiolibro.isEnabled()) {
            reproduceUnAudiolibro.setEnabled(false);
        }
    }

    private void mostrarCargandoAudiolibro(){
        cargandoAudiolibro.setEnabled(true);
        iconoCargando.animate().rotation(-720f).setDuration(3000).start();
    }

    private void esconderCargandoAudiolibro() {
        if(cargandoAudiolibro.isEnabled()) {
            cargandoAudiolibro.setEnabled(false);
        }
    }


    public static void reproducirNuevoLibro(int idAudiolibro){
        // TODO: petición de audiolibro concreto, guardar capitulos, si es el primero hacer petición del minuto concreto

    }




    public void actualizarDuracionAudio(){
        int duracion = mediaPlayer.getDuration();

        numerosDerecha.setText(getFormattedTime(duracion));
    }

    public void actualizarAudioReproducido(){
        int posicionActual = mediaPlayer.getCurrentPosition();

        numerosIzquierda.setText(getFormattedTime(posicionActual));
    }

    private String getFormattedTime(int duration){

        if(duration >= 600000){
            return String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            );
        } else {
            return String.format("%01d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))

            );
        }
    }

    /*
    public void prepararPrimerAudio(){
        fabPlay.setClickable(false);
        fabPause.setClickable(false);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );


        try{
            mediaPlayer.setDataSource(audios[captiuloActual]);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    fabPlay.setClickable(true);
                    fabPause.setClickable(true);
                    actualizarDuracionAudio();
                    setActualizacionSeekBar();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    int cp = mp.getCurrentPosition();
                    mp.seekTo(0);


                    if(cp < mediaPlayer.getDuration() - 1000){
                        Toast.makeText(getContext(), "ERROR: no hagas saltos de audio tan grandes", Toast.LENGTH_LONG).show();
                        pararMusica();
                    } else {
                        prepararAudio();
                    }
                }
            });
        } catch (Exception e){
            Toast.makeText(getContext(), "Error preparando audio", Toast.LENGTH_LONG).show();
        }
    }
    */

    public void prepararAudio(int capitulo){

        if(!primerAudio){
            mediaPlayer.reset();
        }

        if(capitulo < 0){
            capituloActual = 0;
        } else if(capitulo >= capitulos.length){
            Toast.makeText(getContext(), "FIN DEL LIBRO", Toast.LENGTH_LONG).show();
            libroReiniciado = true;
            capituloActual = 0;
        } else {
            capituloActual = capitulo;
        }

        Toast.makeText(getContext(), "Capitulo " + String.valueOf(capituloActual+1), Toast.LENGTH_LONG).show();


        fabPlay.setClickable(false);
        fabPause.setClickable(false);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        try{
            mediaPlayer.setDataSource(capitulos[capituloActual]);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    fabPlay.setClickable(true);
                    fabPause.setClickable(true);

                    actualizarDuracionAudio();
                    setActualizacionSeekBar();
                    if(!primerAudio){
                        reanudarMusica();
                    }

                    if(libroReiniciado){
                        libroReiniciado = false;
                        pararMusica();
                    }

                    if(primerAudio){
                        primerAudio = false;
                    }
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO: Habrá que crear un método para que se reproduzca el siguiente capítulo
                    int cp = mp.getCurrentPosition();
                    mp.seekTo(0);


                    if(cp < mediaPlayer.getDuration() - 1000){
                        Toast.makeText(getContext(), "ERROR: no hagas saltos de audio tan grandes", Toast.LENGTH_LONG).show();
                        pararMusica();
                    } else {
                        prepararAudio(capituloActual +1);
                    }
                }
            });

        } catch (Exception e){
            Toast.makeText(getContext(), "Error preparando audio", Toast.LENGTH_LONG).show();
        }
    }



    public void reanudarMusica(){
        fabPause.setEnabled(true);
        fabPlay.setEnabled(false);
        mediaPlayer.start();
    }

    public void pararMusica(){
        fabPlay.setEnabled(true);
        fabPause.setEnabled(false);
        mediaPlayer.pause();
    }

    public void reiniciarMusica(){
        mediaPlayer.reset();
    }

    public class UpdateSeekBar implements Runnable {
        @Override
        public void run(){
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            actualizarAudioReproducido();
            handler.postDelayed(this, 100);
        }
    }

    private void setActualizacionSeekBar() {
        seekBar = getView().findViewById(R.id.seekbarEscuchando);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(MediaPlayer mp, int percent)
            {
                double ratio = percent / 100.0;
                int bufferingLevel = (int)(mp.getDuration() * ratio);
                seekBar.setSecondaryProgress(bufferingLevel);
            }

        });

        updateSeekBar = new UpdateSeekBar();
        handler.post(updateSeekBar);
    }

}