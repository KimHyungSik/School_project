package com.example.destination_alarm;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Socket_ extends Thread {

    String Ip;
    int port;
    String data;

    private DataOutputStream dos;
    private Socket socket;

    Socket_(String Ip, int port, String data){
        this.Ip = Ip;
        this.port = port;
        this.data = data;
    }

    public void run(){
        try {
            socket = new Socket(Ip, port);

            dos = new DataOutputStream(socket.getOutputStream());

            Log.d("ss","그래 이건 된다");
            dos.writeUTF(data);
            dos.flush();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
