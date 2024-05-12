package com.example.narratives.activities.clubes;

import android.os.Bundle;
import android.transition.TransitionSet;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.UserMemberAdapter;
import com.example.narratives.peticiones.clubes.Club;
import com.example.narratives.peticiones.clubes.ClubResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoClubActivity extends AppCompatActivity {
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    ImageView img;
    TextView name;
    TextView desc;
    TextView audiolibro;
    ListView members;
    UserMemberAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_club);

        // Registrar la vista para el menú contextual
        registerForContextMenu(findViewById(R.id.infoClub_contextMenu));

        img = findViewById(R.id.infoClub_img);
        name = findViewById(R.id.infoClub_name);
        desc = findViewById(R.id.infoClub_desc);
        audiolibro = findViewById(R.id.infoClub_audiolibro);
        members = findViewById(R.id.infoClub_members);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        cargarDatos();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu_op1:
                // Manejar la opción 1
                return true;
            case R.id.context_menu_op2:
                // Manejar la opción 2
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void cargarDatos() {
        Call<ClubResult> llamada = retrofitInterface.ejecutarInfoClub(ApiClient.getUserCookie(), getIntent().getIntExtra("club_id", -1));
        llamada.enqueue(new Callback<ClubResult>() {
            @Override
            public void onResponse(Call<ClubResult> call, Response<ClubResult> response) {
                if (response.code() == 200) {
                    Club club = response.body().getClub();
                    name.setText(club.getName());
                    desc.setText(club.getDescription());
                    if (club.getAudiolibro() != null) {
                        audiolibro.setText(club.getAudiolibro().getTitulo());
                        Glide
                                .with(InfoClubActivity.this)
                                .load(club.getAudiolibro().getImg())
                                .centerCrop()
                                .placeholder(R.drawable.icono_libro)
                                .into(img);
                    }
                    mAdapter = new UserMemberAdapter(InfoClubActivity.this,R.layout.item_lista_amigos, club.getMembers());
                    members.setAdapter(mAdapter);
                } else if (response.code() == 500) {
                    Toast.makeText(InfoClubActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(InfoClubActivity.this, "Código error " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ClubResult> call, Throwable t) {
                // Maneja la falla de la solicitud aquí
                Toast.makeText(InfoClubActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
