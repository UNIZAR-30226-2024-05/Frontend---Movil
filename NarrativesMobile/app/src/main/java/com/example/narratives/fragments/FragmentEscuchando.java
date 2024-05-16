package com.example.narratives.fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.CrearMarcapaginasActivity;
import com.example.narratives.activities.MainActivity;
import com.example.narratives.adaptadores.CapitulosAdapter;
import com.example.narratives.informacion.InfoAudiolibros;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.audiolibros.especifico.AudiolibroEspecificoResponse;
import com.example.narratives.peticiones.audiolibros.especifico.Capitulo;
import com.example.narratives.peticiones.audiolibros.especifico.UltimoMomento;
import com.example.narratives.peticiones.marcapaginas.ListeningRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEscuchando extends Fragment {
    private MediaPlayer mediaPlayer;
    private FloatingActionButton fabPlay;
    private FloatingActionButton fabPause;
    private FloatingActionButton fabAvanzar;
    private FloatingActionButton fabRetrasar;
    private FloatingActionButton fabSiguienteCap;
    private FloatingActionButton fabAnteriorCap;
    private MaterialButton fabMarcapaginas;
    private MaterialButton selectorCapitulos;
    private TextView numerosIzquierda;
    private TextView numerosDerecha;
    private ImageView portada;
    private TextView titulo_libro;
    private TextView titulo_cap;
    private TextView num_cap;
    private static ConstraintLayout reproduceUnAudiolibro;
    private static ConstraintLayout cargandoAudiolibro;
    private ConstraintLayout reproductor;
    private ImageView iconoCargando;
    private SeekBar seekBar;
    private Handler handler;
    private UpdateSeekBar updateSeekBar;
    private UpdateUltimoMomento updateUltimoMomento;
    private ArrayList<Capitulo> capitulos;
    private UltimoMomento ultimoMomento;
    private Animation animacionCargando;
    private RetrofitInterface retrofitInterface;
    private boolean primerAudio = true;
    private boolean primerLibro = true;
    private boolean libroReiniciado = false;
    private boolean libroEnUltimoMomento = true;
    private int capituloActual = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_escuchando, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        inicializarElementosReproductor();

        if(InfoAudiolibros.getUltimoLibro() == null || InfoAudiolibros.getUltimoLibro().getId_audiolibro() < 0){
            mostrarReproduceUnAudiolibro();
            esconderReproductor();
            esconderCargandoAudiolibro();
            fabPlay.setClickable(false);
            fabPause.setClickable(false);
        } else {
            peticionAudiolibrosId(InfoAudiolibros.getUltimoLibro().getId_audiolibro());
        }




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
        fabMarcapaginas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCrearMarcapaginas();
            }
        });

        selectorCapitulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPopupCapitulos();
            }
        });
    }

    private void inicializarElementosReproductor() {
        numerosIzquierda = getView().findViewById(R.id.textViewSeekBarIzquierdaEscuchando);
        numerosDerecha= getView().findViewById(R.id.textViewSeekBarDerechaEscuchando);

        portada = getView().findViewById(R.id.shapeableImageViewPortadaLibroEscuchando);
        titulo_libro = getView().findViewById(R.id.textViewTituloLibroEscuchando);
        titulo_cap = getView().findViewById(R.id.textViewTituloCapituloEscuchando);
        num_cap = getView().findViewById(R.id.textViewNumeroCapituloEscuchando);

        fabPlay = getView().findViewById(R.id.botonPlayEscuchando);
        fabPause = getView().findViewById(R.id.botonPauseEscuchando);
        fabAvanzar =  getView().findViewById(R.id.botonAvanzarDiezEscuchando);
        fabRetrasar = getView().findViewById(R.id.botonRetrasarDiezEscuchando);
        fabSiguienteCap = getView().findViewById(R.id.botonSiguienteCapituloEscuchando);
        fabAnteriorCap = getView().findViewById(R.id.botonAnteriorCapituloEscuchando);
        fabMarcapaginas = getView().findViewById(R.id.botonCrearMarcapaginas);
        selectorCapitulos = getView().findViewById(R.id.botonSelectorDeCapitulos);

        fabPause = getView().findViewById(R.id.botonPauseEscuchando);
        fabPlay = getView().findViewById(R.id.botonPlayEscuchando);
        fabPause.setEnabled(false);

        reproductor = getView().findViewById(R.id.constraintLayoutReproductor);
        reproduceUnAudiolibro = getView().findViewById(R.id.constraintLayoutReproduceUnAudiolibro);
        cargandoAudiolibro = getView().findViewById(R.id.constraintLayoutCargandoAudiolibro);

        iconoCargando = getView().findViewById(R.id.imageViewCargandoAudiolibro);
        iconoCargando.animate().rotation(-720f).setDuration(3000);

        animacionCargando = AnimationUtils.loadAnimation(getContext(), R.anim.rotation_animation);
        animacionCargando.setInterpolator(new LinearInterpolator());
        animacionCargando.setDuration(1400);

        handler = new Handler();
        retrofitInterface = ApiClient.getRetrofitInterface();
        updateUltimoMomento = new UpdateUltimoMomento();
    }

    private void abrirPopupCapitulos() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewCapitulos = inflater.inflate(R.layout.popup_capitulos, null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height= ViewGroup.LayoutParams.MATCH_PARENT;

        ListView listaCapitulos = viewCapitulos.findViewById(R.id.listViewListaCapitulos);
        CapitulosAdapter capitulosAdapter = new CapitulosAdapter(getContext(), R.layout.item_capitulo, capitulos);
        listaCapitulos.setAdapter(capitulosAdapter);

        PopupWindow popupWindow = new PopupWindow(viewCapitulos,width,height, true);
        popupWindow.setAnimationStyle(0);

        FrameLayout layout = getActivity().findViewById(R.id.main_layout);
        layout.post(new Runnable(){
            @Override
            public void run(){
                popupWindow.showAtLocation(layout, Gravity.BOTTOM,0,0);
            }
        });

        listaCapitulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                popupWindow.dismiss();
                libroReiniciado = true;
                prepararAudio(position);
            }
        });

        FloatingActionButton botonCerrar = viewCapitulos.findViewById(R.id.botonCerrarCapitulos);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    private void mostrarReproductor() {
        reproductor.setVisibility(View.VISIBLE);

        fabPause.setClickable(true);
        fabPlay.setClickable(true);
        fabRetrasar.setClickable(true);
        fabAvanzar.setClickable(true);
        fabAnteriorCap.setClickable(true);
        fabSiguienteCap.setClickable(true);
    }

    private void esconderReproductor() {
        reproductor.setVisibility(View.INVISIBLE);

        fabPause.setClickable(false);
        fabPlay.setClickable(false);
        fabRetrasar.setClickable(false);
        fabAvanzar.setClickable(false);
        fabAnteriorCap.setClickable(false);
        fabSiguienteCap.setClickable(false);
    }

    private void mostrarReproduceUnAudiolibro() {
        reproduceUnAudiolibro.setVisibility(View.VISIBLE);
    }

    private void esconderReproduceUnAudiolibro() {
        reproduceUnAudiolibro.setVisibility(View.INVISIBLE);
    }

    private void mostrarCargandoAudiolibro() {
        cargandoAudiolibro.setVisibility(View.VISIBLE);
        iconoCargando.startAnimation(animacionCargando);
    }

    private void esconderCargandoAudiolibro() {
        cargandoAudiolibro.setVisibility(View.INVISIBLE);
    }

    public void actualizarDuracionAudio() {
        int duracion = mediaPlayer.getDuration();

        numerosDerecha.setText(getBarFormattedTime(duracion));
    }

    public void actualizarAudioReproducido() {
        int posicionActual = mediaPlayer.getCurrentPosition();

        numerosIzquierda.setText(getBarFormattedTime(posicionActual));
    }

    public void inicializarLibro(AudiolibroEspecificoResponse audiolibro) {
        if (updateUltimoMomento != null) {
            handler.removeCallbacks(updateUltimoMomento);
        }

        capitulos = audiolibro.getCapitulos();
        primerAudio = true;

        ultimoMomento = null;
        ultimoMomento = audiolibro.getUltimoMomento();

        if(ultimoMomento == null ) {
            capituloActual = 0;
            libroEnUltimoMomento = true;
        } else if (getIndiceCapituloFromId(ultimoMomento.getCapitulo()) < 0){
            Toast.makeText(getContext(), "Cap con id \"" + ultimoMomento.getCapitulo()
                                + "\" no existe en este audiolibro", Toast.LENGTH_LONG).show();
            capituloActual = 0;

        } else {
            capituloActual = getIndiceCapituloFromId(ultimoMomento.getCapitulo());
            libroEnUltimoMomento = false;
        }

        titulo_libro.setText(audiolibro.getAudiolibro().getTitulo());
        Glide
                .with(getContext())
                .load(audiolibro.getAudiolibro().getImg())
                .centerCrop()
                .placeholder(R.drawable.icono_imagen_estandar_foreground)
                .into(portada);

        if (primerLibro) {
            esconderReproduceUnAudiolibro();
        }

        prepararAudio(capituloActual);
    }

    public void prepararAudio(int capitulo) {
        mostrarCargandoAudiolibro();
        esconderReproductor();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            fabPause.performClick();
        }

        if (!primerLibro) {
            mediaPlayer.reset();
        }

        if (capitulo < 0) {
            capituloActual = 0;
        } else if(capitulo >= capitulos.size()){
            Toast.makeText(getContext(), "FIN DEL LIBRO | Reiniciando...", Toast.LENGTH_LONG).show();
            libroReiniciado = true;
            capituloActual = 0;
        } else {
            capituloActual = capitulo;
        }
   
        titulo_cap.setText(capitulos.get(capituloActual).getNombre());
        num_cap.setText(getCapituloWithNumberString(capitulos.get(capituloActual).getNumero()) + ":");

        fabPlay.setClickable(false);
        fabPause.setClickable(false);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        try {
            mediaPlayer.setDataSource(capitulos.get(capituloActual).getAudio());
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    fabPlay.setClickable(true);
                    fabPause.setClickable(true);

                    actualizarDuracionAudio();
                    setActualizacionSeekBar();

                    if (primerAudio) {
                        primerAudio = false;

                        if(libroEnUltimoMomento){
                            peticionActualizarUltimoMomento();
                            esconderCargandoAudiolibro();
                            mostrarReproductor();
                        }

                    } else {    // no es el primer audio, ya habia otro capítulo antes
                        reanudarMusica();
                    }

                    if (libroReiniciado) {
                        libroReiniciado = false;
                        mediaPlayer.seekTo(0);
                        ponerBarraACeroManualmente();
                        pararMusica();
                    }

                    if (primerLibro) {
                        primerLibro = false;
                    }
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    int cp = mp.getCurrentPosition();
                    mp.seekTo(0);

                    if (cp < mediaPlayer.getDuration() - 1000) {
                        Toast.makeText(getContext(), "ERROR: no hagas saltos de audio tan grandes", Toast.LENGTH_LONG).show();
                        pararMusica();
                    } else {
                        prepararAudio(capituloActual+1);
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error preparando audio", Toast.LENGTH_LONG).show();
        }
    }

    private void ponerBarraACeroManualmente() {
        numerosIzquierda.setText(getBarFormattedTime(0));
        seekBar.setProgress(0);
        seekBar.setSecondaryProgress(0);
    }

    public class UpdateUltimoMomento implements Runnable {
        @Override
        public void run(){
            if (libroEnUltimoMomento) {
                peticionActualizarUltimoMomento();
            }

            handler.postDelayed(this, 10000);
        }
    }

    public void reanudarMusica() {
        fabPause.setEnabled(true);
        fabPause.setVisibility(View.VISIBLE);
        fabPause.setClickable(true);

        fabPlay.setEnabled(false);
        fabPlay.setVisibility(View.INVISIBLE);
        fabPlay.setClickable(false);

        mediaPlayer.start();

        handler.post(updateSeekBar);
        handler.post(updateUltimoMomento);
    }

    public void pararMusica() {
        peticionActualizarUltimoMomento();

        fabPlay.setEnabled(true);
        fabPlay.setVisibility(View.VISIBLE);
        fabPlay.setClickable(true);

        fabPause.setEnabled(false);
        fabPause.setVisibility(View.INVISIBLE);
        fabPause.setClickable(false);

        mediaPlayer.pause();

        handler.removeCallbacks(updateSeekBar);
        handler.removeCallbacks(updateUltimoMomento);
    }

    public void pararPorCierreSesion(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
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
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(MediaPlayer mp, int percent)
            {
                double ratio = percent / 100.0;
                int bufferingLevel = (int)(mp.getDuration() * ratio);
                seekBar.setSecondaryProgress(bufferingLevel);

                if (!libroEnUltimoMomento) {
                    int momentoTarget = getPetitionDeformattedTime(ultimoMomento.getFecha());

                    mp.seekTo(bufferingLevel - 5000);

                    if ((bufferingLevel - 5000) > momentoTarget) {
                        mp.seekTo(momentoTarget);
                        peticionActualizarUltimoMomento();
                        esconderCargandoAudiolibro();
                        mostrarReproductor();
                        libroEnUltimoMomento = true;
                    }
                } else if (cargandoAudiolibro.getVisibility() == View.VISIBLE) {
                    esconderCargandoAudiolibro();
                    mostrarReproductor();
                }
            }
        });

        if (updateSeekBar != null) {
            handler.removeCallbacks(updateSeekBar);
        }

        updateSeekBar = new UpdateSeekBar();
        handler.post(updateSeekBar);
    }

    public String getCapituloWithNumberString(int num) {
        return "Capítulo " + num;
    }

    private void peticionActualizarUltimoMomento() {
        ListeningRequest request = new ListeningRequest();
        request.setCapitulo(capitulos.get(capituloActual).getId());
        request.setTiempo(getPetitionFormattedTime(mediaPlayer.getCurrentPosition()));

        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarMarcapaginasListening(ApiClient.getUserCookie(), request);

        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(@NonNull Call<GenericMessageResult> call, @NonNull Response<GenericMessageResult> response) {
                if (response.code() == 500){
                    Toast.makeText(getContext(), "Error del servidor (audiolibros/listening)", Toast.LENGTH_LONG).show();
                } else if(response.code() != 200) {
                    Toast.makeText(getContext(), "Error desconocido (audiolibros/listening): " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericMessageResult> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Algo ha fallado (audiolibros/listening)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getBarFormattedTime(int duration) {
        if(duration >= 6000000){
            return String.format("%01d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(duration))),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            );
        } else if(duration < 6000000 && duration >= 600000){
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

    private String getPetitionFormattedTime(int duration) {
            return String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            );
    }

    private int getPetitionDeformattedTime(String time) {
        String[] strArray  = time.split(":");

        // strArray tendrá 3 valores: hora, minuto y segundo;
        int duration = 0;

        duration += Integer.valueOf(strArray[0]) * 60 * 60 * 1000;  // hora
        duration += Integer.valueOf(strArray[1]) * 60 * 1000;       // min
        duration += Integer.valueOf(strArray[2]) * 1000;            // seg

        return duration;
    }

    private int getIndiceCapituloFromId(int id) {
        for(int i = 0; i < capitulos.size(); i++){
            if(capitulos.get(i).getId() == id){
                return i;
            }
        }

        return -1; //ningún capítulo coincide con el id
    }

    public void abrirCrearMarcapaginas() {
        Intent intent = new Intent(getContext(), CrearMarcapaginasActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        String tiempo = getPetitionFormattedTime(mediaPlayer.getCurrentPosition());
        intent.putExtra("ReproductorSegundos", tiempo.substring(6, 8));
        intent.putExtra("ReproductorMinutos", tiempo.substring(3, 5));
        intent.putExtra("ReproductorHoras", tiempo.substring(0, 2));
        intent.putExtra("listaCapitulos", capitulos);
        intent.putExtra("capituloActual", capituloActual);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }


    private void peticionAudiolibrosId(int id) {
        Call<AudiolibroEspecificoResponse> llamada = retrofitInterface.ejecutarAudiolibrosId(ApiClient.getUserCookie(), id);
        llamada.enqueue(new Callback<AudiolibroEspecificoResponse>() {
            @Override
            public void onResponse(@NonNull Call<AudiolibroEspecificoResponse> call, @NonNull Response<AudiolibroEspecificoResponse> response) {
                int codigo = response.code();

                if (codigo == 200) {
                    MainActivity.fragmentoEscuchandoAbierto.inicializarLibro(response.body());

                } else if (codigo == 409) {
                    //Toast.makeText(MainActivity.fragmentoInicioAbierto.getContext(), "No hay ningún audiolibro con ese ID (escuchando)", Toast.LENGTH_LONG).show();
                } else if (codigo == 500) {
                    Toast.makeText(MainActivity.fragmentoInicioAbierto.getContext(), "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.fragmentoInicioAbierto.getContext(), "Error desconocido (AudiolibrosId): " + codigo, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AudiolibroEspecificoResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.fragmentoInicioAbierto.getContext(), "No se ha conectado con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }
}
