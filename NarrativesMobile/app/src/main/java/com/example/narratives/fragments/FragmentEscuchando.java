package com.example.narratives.fragments;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

    TextView numerosIzquierda;
    TextView numerosDerecha;


    SeekBar seekBar;

    Handler handler;

    UpdateSeekBar updateSeekBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_escuchando, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        numerosIzquierda = getView().findViewById(R.id.textViewSeekBarIzquierdaEscuchando);
        numerosDerecha= getView().findViewById(R.id.textViewSeekBarDerechaEscuchando);

        seekBar = getView().findViewById(R.id.seekbarEscuchando);

        fabPlay = (FloatingActionButton) getView().findViewById(R.id.botonPlayEscuchando);
        fabPause = (FloatingActionButton) getView().findViewById(R.id.botonPauseEscuchando);
        fabAvanzar = (FloatingActionButton) getView().findViewById(R.id.botonAvanzarDiezEscuchando);
        fabRetrasar = (FloatingActionButton) getView().findViewById(R.id.botonRetrasarDiezEscuchando);

        fabPause = (FloatingActionButton) view.findViewById(R.id.botonPauseEscuchando);
        fabPlay = (FloatingActionButton) getView().findViewById(R.id.botonPlayEscuchando);
        fabPause.setEnabled(false);

        //fabPlay.setEnabled(false);
        //mediaPlayer = MediaPlayer.create(getContext(), R.raw.zowi);
        //mediaPlayer = MediaPlayer.create(getContext(), R.raw.exclusive);

        fabPlay.setClickable(false);
        fabPause.setClickable(false);
        prepararAudio("https://narrativesarchivos.blob.core.windows.net/audios/LaOdisea_1.mp3", false   );


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


    }

    private void inicializarMediaPlayer() {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO: Habrá que crear un método para que se reproduzca el siguiente capítulo
                int cp = mp.getCurrentPosition();
                mp.seekTo(0);
                mediaPlayer.stop();


                if(cp < mediaPlayer.getDuration() - 1000){
                    Toast.makeText(getContext(), "ERROR: no hagas saltos de audio tan grandes", Toast.LENGTH_LONG).show();
                    pararMusica();
                    reiniciarMusica();
                } else {

                    prepararAudio("https://narrativesarchivos.blob.core.windows.net/audios/LaOdisea_2.mp3", true);
                }
            }
        });
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


    public void prepararAudio(String url, boolean reproducir_automaticamente){
        fabPlay.setClickable(false);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        inicializarAudio(url, reproducir_automaticamente);
    }

    private void inicializarAudio(String url, boolean reproducir_automaticamente) {
        try{
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    fabPlay.setClickable(true);
                    actualizarDuracionAudio();
                    setConfiguracionSeekBar();
                    if(reproducir_automaticamente){
                        reanudarMusica();
                    } else {
                        pararMusica();
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
        //prepararAudio();
    }

    public class UpdateSeekBar implements Runnable {
        @Override
        public void run(){
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            actualizarAudioReproducido();
            handler.postDelayed(this, 100);
        }
    }

    private void setConfiguracionSeekBar() {
        handler = new Handler();
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

        /*
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(MediaPlayer mp, int percent)
            {
                double ratio = percent / 100.0;
                int bufferingLevel = (int)(mp.getDuration() * ratio);
                seekBar.setSecondaryProgress(bufferingLevel);
                if(esperandoBuffer){
                    if(bufferingLevel > checkpoint){
                        esperandoBuffer = false;
                        mp.seekTo(checkpoint);
                        bufferCarga1min = 1;
                        bufferCarga5min = 1;
                    } else if(bufferingLevel > 300000*bufferCarga5min + 60000*bufferCarga1min){
                        mp.seekTo(300000*bufferCarga5min + 60000*bufferCarga1min);
                        bufferCarga5min += 1;
                    } else if(bufferingLevel > 300000*(bufferCarga5min-1) + 60000*bufferCarga1min) {
                        mp.seekTo(300000*(bufferCarga5min-1) + 60000*bufferCarga1min);
                        bufferCarga1min += 1;
                    }
                }

            }

        });
        */
        updateSeekBar = new UpdateSeekBar();
        handler.post(updateSeekBar);
    }
}