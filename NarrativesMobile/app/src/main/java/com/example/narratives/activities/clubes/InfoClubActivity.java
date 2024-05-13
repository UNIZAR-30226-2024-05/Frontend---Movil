package com.example.narratives.activities.clubes;

import android.app.Activity;
import android.content.Intent;
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

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.UserMemberAdapter;
import com.example.narratives.fragments.FragmentClubs;
import com.example.narratives.informacion.InfoClubes;
import com.example.narratives.peticiones.GenericMessageResult;
import com.example.narratives.peticiones.clubes.Club;
import com.example.narratives.peticiones.clubes.ClubRequest;
import com.example.narratives.peticiones.clubes.ClubResult;
import com.example.narratives.sockets.SocketManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoClubActivity extends AppCompatActivity {
    private Club club;
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
        //registerForContextMenu(findViewById(R.id.infoClub_contextMenu));

        img = findViewById(R.id.infoClub_img);
        name = findViewById(R.id.infoClub_name);
        desc = findViewById(R.id.infoClub_desc);
        audiolibro = findViewById(R.id.infoClub_audiolibro);
        members = findViewById(R.id.infoClub_members);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        cargarDatos(false);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                closeActivity();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
        if(club.isAdmin()){
            menu.findItem(R.id.context_menu_eliminar).setVisible(true);
        } else if (club.isMember()) {
            menu.findItem(R.id.context_menu_abandonar).setVisible(true);
        } else {
            menu.findItem(R.id.context_menu_unirse).setVisible(true);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu_compartir:
                //compartirClub();
                return true;
            case R.id.context_menu_unirse:
                unirseClub();
                invalidateOptionsMenu();
                return true;
            case R.id.context_menu_abandonar:
                abandonarClub();
                invalidateOptionsMenu();
                return true;
            case R.id.context_menu_eliminar:
                eliminarClub();
                invalidateOptionsMenu();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FragmentClubs.CHAT_CLUB) {
            if (resultCode == Activity.RESULT_OK) {
                Boolean update = data.getBooleanExtra("update", false);
                if (update) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("requestCode", FragmentClubs.CHAT_CLUB);
                    resultIntent.putExtra("update", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        }
    }

    /*private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int requestCode = result.getData().getIntExtra("requestCode", -1);
                if (requestCode == FragmentClubs.CHAT_CLUB) {
                    Boolean update = getIntent().getBooleanExtra("update", false);
                    if (update) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("requestCode", FragmentClubs.INFO_CLUB);
                        resultIntent.putExtra("update", true);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                }
            });*/

    private void cargarDatos(boolean guardar) {
        Call<ClubResult> llamada = retrofitInterface.ejecutarInfoClub(ApiClient.getUserCookie(), getIntent().getIntExtra("club_id", -1));
        llamada.enqueue(new Callback<ClubResult>() {
            @Override
            public void onResponse(Call<ClubResult> call, Response<ClubResult> response) {
                if (response.code() == 200) {
                    club = response.body().getClub();
                    if (guardar) {
                        InfoClubes.addClub(club);
                    }
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
                    mAdapter = new UserMemberAdapter(InfoClubActivity.this,R.layout.item_usuario_prueba, club.getMembers());
                    members.setAdapter(mAdapter);
                    registerForContextMenu(findViewById(R.id.infoClub_contextMenu));
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

    private void unirseClub() {
        ClubRequest request = new ClubRequest(club.getId());
        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarJoinClub(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {
                if (response.code() == 200) {
                    Toast.makeText(InfoClubActivity.this, "Te uniste al club correctamente", Toast.LENGTH_LONG).show();
                    InfoClubes.addClub(club);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("update", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else if (response.code() == 500) {
                    Toast.makeText(InfoClubActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InfoClubActivity.this, "Código error " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                // Maneja la falla de la solicitud aquí
                Toast.makeText(InfoClubActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abandonarClub() {
        ClubRequest request = new ClubRequest(club.getId());
        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarLeaveClub(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {
                if (response.code() == 200) {
                    Toast.makeText(InfoClubActivity.this, "Abandonaste el club correctamente", Toast.LENGTH_LONG).show();
                    InfoClubes.removeClubById(club.getId());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("update", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else if (response.code() == 500) {
                    Toast.makeText(InfoClubActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InfoClubActivity.this, "Código error " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                // Maneja la falla de la solicitud aquí
                Toast.makeText(InfoClubActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void eliminarClub() {
        ClubRequest request = new ClubRequest(club.getId());
        Call<GenericMessageResult> llamada = retrofitInterface.ejecutarDeleteClub(ApiClient.getUserCookie(), request);
        llamada.enqueue(new Callback<GenericMessageResult>() {
            @Override
            public void onResponse(Call<GenericMessageResult> call, Response<GenericMessageResult> response) {
                if (response.code() == 200) {
                    Toast.makeText(InfoClubActivity.this, "El club se eliminó correctamente", Toast.LENGTH_LONG).show();
                    InfoClubes.removeClubById(club.getId());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("requestCode", FragmentClubs.INFO_CLUB);
                    resultIntent.putExtra("update", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else if (response.code() == 500) {
                    Toast.makeText(InfoClubActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InfoClubActivity.this, "Código error " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<GenericMessageResult> call, Throwable t) {
                // Maneja la falla de la solicitud aquí
                Toast.makeText(InfoClubActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void closeActivity() {
        SocketManager.removeMessageListener();
        Boolean update = getIntent().getBooleanExtra("update", false);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("update", update);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
