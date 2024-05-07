package com.example.narratives.sockets;

import android.util.Log;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketManager {
    private static final String SERVER_URL = "https://server.narratives.es";
    private static Socket mSocket;
    private static String cookie;

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
        //Log.d("COOKIE_ALL", cookie);
        String[] cookieParts = cookie.split(";");
        //Log.d("COOKIE", cookieParts[0].trim());
        SocketManager.cookie = cookieParts[0].trim();
        //SocketManager.cookie = "he cambiado";
        Log.d("COOKIE_DEF", SocketManager.cookie);
        // Si la instancia del socket ya está creada, actualizamos sus cabeceras
        /*if (mSocket != null) {
            updateSocketHeaders();
        }*/
    }
/*
    private static void updateSocketHeaders() {
        IO.Options opts = mSocket.opts();
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
    }*/

    public void IOSendMessage(String event, JSONObject message) {
        mSocket.emit(event, message);
    }
}
