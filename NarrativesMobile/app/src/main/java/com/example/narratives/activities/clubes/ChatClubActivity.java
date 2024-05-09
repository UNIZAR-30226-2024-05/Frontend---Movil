package com.example.narratives.activities.clubes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.narratives.R;
import com.example.narratives._backend.ApiClient;
import com.example.narratives._backend.RetrofitInterface;
import com.example.narratives.adaptadores.ChatAdapter;
import com.example.narratives.fragments.FragmentClubs;
import com.example.narratives.informacion.InfoClubes;
import com.example.narratives.peticiones.clubes.Club;
import com.example.narratives.peticiones.clubes.ClubResult;
import com.example.narratives.peticiones.clubes.Message;
import com.example.narratives.sockets.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatClubActivity extends AppCompatActivity implements SocketManager.MessageListener {
    private int club_id;
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    TextView clubName;
    ImageView clubImg;
    EditText inputText;
    Button send;
    ImageButton back;
    RecyclerView msgsView;
    ChatAdapter mAdapter;
    private Socket mSocket = SocketManager.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new TransitionSet());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_club);

        SocketManager.addMessageListener(this);

        retrofit = ApiClient.getLoginRetrofit();
        retrofitInterface = ApiClient.getRetrofitInterface();

        clubName = findViewById(R.id.chatClub_name);
        clubImg = findViewById(R.id.chatClub_img);
        inputText = findViewById(R.id.chatClub_text);
        send = findViewById(R.id.chatClub_send);
        back = findViewById(R.id.chatClub_back);
        msgsView = findViewById(R.id.chatClub_msgs);

        cargarDatos();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelActivity();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputText.getText().toString().trim().isEmpty()) {
                    sendMessage();
                }
            }
        });
    }

    @Override
    public void onMessageReceived() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void cargarDatos() {
        club_id = getIntent().getIntExtra("club_id", -1);
        Call<ClubResult> llamada = retrofitInterface.ejecutarInfoClub(ApiClient.getUserCookie(), club_id);
        llamada.enqueue(new Callback<ClubResult>() {
            @Override
            public void onResponse(Call<ClubResult> call, Response<ClubResult> response) {
                if (response.code() == 200) {
                    Club club = InfoClubes.getClubById(response.body().getClub().getId());
                    if (club.getMessages() == null) {
                        if (response.body().getClub().getMessages() == null) {
                            club.setMessages(new ArrayList<>());
                        } else {
                            club.setMessages(response.body().getClub().getMessages());
                        }
                    }
                    clubName.setText(club.getName());
                    if (club.getAudiolibro() != null) {
                        Glide
                                .with(ChatClubActivity.this)
                                .load(club.getAudiolibro().getImg())
                                .centerCrop()
                                .placeholder(R.drawable.icono_libro)
                                .into(clubImg);
                    }
                    msgsView.setLayoutManager(new LinearLayoutManager(ChatClubActivity.this));
                    mAdapter = new ChatAdapter(ChatClubActivity.this, club.getMessages());
                    msgsView.setAdapter(mAdapter);
                } else if (response.code() == 500) {
                    Toast.makeText(ChatClubActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ChatClubActivity.this, "Código error " + String.valueOf(response.code()),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ClubResult> call, Throwable t) {
                // Maneja la falla de la solicitud aquí
                Toast.makeText(ChatClubActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendMessage() {
        JSONObject messageData = new JSONObject();
        try {
            messageData.put("club_id", club_id);
            messageData.put("msg", inputText.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketManager.IOSendMessage("message", messageData);
    }

    private void cancelActivity() {
        SocketManager.removeMessageListener();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("requestCode", FragmentClubs.CREAR_CLUB);
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancelActivity();
        super.onBackPressed();
    }
}
