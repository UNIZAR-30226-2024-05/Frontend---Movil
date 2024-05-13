package com.example.narratives.sockets;

import com.example.narratives._backend.ApiClient;
import com.example.narratives.informacion.InfoClubes;
import com.example.narratives.peticiones.clubes.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager {
    private static final String SERVER_URL = "https://server.narratives.es";
    private static Socket mSocket;
    private static String cookie;
    private static MessageListener messageListeners;

    private SocketManager() {
        // Constructor privado para evitar la creación de instancias adicionales
    }

    public static Socket getInstance() {
        if (mSocket == null) {
            createSocket();
        }
        return mSocket;
    }

    private static void createSocket() {
        try {
            IO.Options opts = new IO.Options();
            Map<String, List<String>> headers = new HashMap<>();
            setSession(ApiClient.getUserCookie());
            if (cookie != null) {
                List<String> cookies = new ArrayList<>();
                cookies.add(cookie);
                headers.put("cookie", cookies);
                opts.extraHeaders = headers;
            }
            mSocket = IO.socket(SERVER_URL, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void setSession(String cookie) {
        String[] cookieParts = cookie.split(";");
        SocketManager.cookie = cookieParts[0].trim();
        if (mSocket != null) {
            updateSocketHeaders();
        }
    }

    private static void updateSocketHeaders() {
        IO.Options opts = new IO.Options();// mSocket.opts();
        Map<String, List<String>> headers = new HashMap<>();
        if (cookie != null) {
            List<String> cookies = new ArrayList<>();
            cookies.add(cookie);
            headers.put("cookie", cookies);
            opts.extraHeaders = headers;
            try {
                mSocket = IO.socket(SERVER_URL, opts);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addMessageListener(MessageListener listener) {
        messageListeners = listener;
    }

    public static void removeMessageListener() {
        messageListeners = null;
    }

    /*public static void onMessageReceived() {
        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args.length > 0 && args[0] instanceof JSONObject) {
                    JSONObject json = (JSONObject) args[0];
                    try {
                        Message msg = new Message();
                        if (InfoClubes.getClubById(json.getInt("club_id")).getMessages() != null) {
                            msg.setId(json.getInt("id"));
                            msg.setUserId(json.getInt("user_id"));
                            msg.setUsername(json.getString("username"));
                            msg.setMessage(json.getString("mensaje"));
                            msg.setDate(json.getString("fecha"));
                            InfoClubes.getClubById(json.getInt("club_id")).addMessage(msg);
                            messageListeners.onMessageReceived();
                        }
                        // Luego puedes procesar los datos recibidos como desees
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }*/

    public static void IOSendMessage(String event, JSONObject message) {
        mSocket.emit(event, message);
    }

    public interface MessageListener {
        void onMessageReceived();
    }
}
