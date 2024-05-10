package com.example.narratives.resenias;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.activities.ReseniaActivity;
import com.example.narratives.peticiones.ReseniasRequest;
import com.example.narratives.peticiones.StandardMessageResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AniadirResenia extends AppCompatActivity {
    EditText descripcion;
    RatingBar aniadir;

    Spinner spinner;

    private RetrofitInterface retrofitInterface;
    char opcionSeleccionada;
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        setContentView(R.layout.aniadir_resenia);
        super.onCreate(savedInstanceState);
        retrofitInterface = ApiClient.getRetrofitInterface();

        Button botonAniadirResenia = (Button) findViewById(R.id.botonAniadirResenia);
        descripcion = findViewById(R.id.editTextDescripcionresenia);
       // descripcion = (EditText) findViewById(R.id.editTextDescripcionresenia);
        aniadir = (RatingBar) findViewById(R.id.ratingBarAniadir);

        spinner = findViewById(R.id.spinner);

        // Define las opciones del Spinner
        String[] opciones = {"Publica", "Privada", "Solo Amigos"};

        // Crea un ArrayAdapter usando el array de opciones y un layout simple
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, opciones);

        // Especifica el layout a usar cuando se despliega el Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplica el adapter al Spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtiene el elemento seleccionado
                String op= (String) parent.getItemAtPosition(position);
                opcionSeleccionada = convertirAChar(op);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acción a realizar cuando no se selecciona ningún elemento
            }
        });
        botonAniadirResenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enviarDatosResenia();
            }
        });
    }

    public void enviarDatosResenia(){
        ReseniasRequest request = new ReseniasRequest();
        //request.setNombre(editTextText.getText().toString());
        request.setDescripcion(descripcion.getText().toString());
        request.setValoracion(aniadir.getRating());
        request.setVisibilidad(opcionSeleccionada);



        Call<StandardMessageResult> llamada = retrofitInterface.ejecutarAniadirResenia(request);
        llamada.enqueue(new Callback<StandardMessageResult>() {
            @Override
            public void onResponse(Call<StandardMessageResult> call, Response<StandardMessageResult> response) {

                if(response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AniadirResenia.this, R.style.ExitoAlertDialogStyle);
                    builder.setTitle("ÉXITO");
                    builder.setMessage("La reseña ha sido añadida correctamente");
                    builder.show();
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    abrirReseñas();
                                }
                            }

                            , 1000);

                }  else if (response.code() == 409){
                    Toast.makeText(AniadirResenia.this, "Ya hay una reseña publicada",
                            Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AniadirResenia.this);
                    builder.setTitle("ERROR");
                    builder.setMessage("Ya existe una reseña publicada");
                    builder.show();

                }
                else if (response.code() == 500){
                    Toast.makeText(AniadirResenia.this, "Error del servidor",
                            Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AniadirResenia.this);
                    builder.setTitle("ERROR");
                    builder.setMessage("Error del servidor");
                    builder.show();

                }else {
                    Toast.makeText(AniadirResenia.this, "Código de error no reconocido: " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StandardMessageResult> call, Throwable throwable) {
                Toast.makeText(AniadirResenia.this, "No se ha conectado con el servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private char convertirAChar(String item){
        if(item.equals("Publica")){
            return 0;
        }
        if(item.equals("Solo Amigos")){
            return 1;
        }
        else{
            return 2;
        }

    }

    public void abrirReseñas() {
        Intent intent = new Intent(this, ReseniaActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }
}
