package com.example.narratives.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narratives.R;
import com.example.narratives.informacion.InfoMiPerfil;
import com.example.narratives.peticiones.clubes.Message;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MESSAGE_SENT = 1;
    private static final int MESSAGE_RECEIVED = 2;

    private ArrayList<Message> messages;
    private Context context;

    public ChatAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getUserId() == InfoMiPerfil.getId()) {
            return MESSAGE_SENT;
        } else {
            return MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case MESSAGE_SENT:
                view = inflater.inflate(R.layout.item_own_msg, parent, false);
                return new SentMessageHolder(view);
            case MESSAGE_RECEIVED:
                view = inflater.inflate(R.layout.item_others_msg, parent, false);
                return new ReceivedMessageHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        switch (holder.getItemViewType()) {
            case MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView date;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.own_textMessageBody);
            date = itemView.findViewById(R.id.own_textDate);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime localDate = LocalDateTime.parse(message.getDate(), formatter);
            ZonedDateTime zonedDate = localDate.atZone(ZoneOffset.UTC);
            zonedDate.withZoneSameInstant(ZoneId.systemDefault());
            date.setText(zonedDate.toString());
        }
    }

    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView messageText;
        TextView date;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.others_textUsername);
            messageText = itemView.findViewById(R.id.others_textMessageBody);
            date = itemView.findViewById(R.id.others_textDate);
        }

        void bind(Message message) {
            username.setText(message.getUsername());
            messageText.setText(message.getMessage());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime localDate = LocalDateTime.parse(message.getDate(), formatter);
            ZonedDateTime zonedDate = localDate.atZone(ZoneOffset.UTC);
            zonedDate.withZoneSameInstant(ZoneId.systemDefault());
            date.setText(zonedDate.toString());
        }
    }
}

