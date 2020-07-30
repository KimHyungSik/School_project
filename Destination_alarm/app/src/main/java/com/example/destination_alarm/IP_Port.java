package com.example.destination_alarm;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class IP_Port {
    private static String IP = "192.168.0.5";
    private static int Port = 8800;

    IP_Port(){
    }

    public int getPort(){
        return Port;
    }
    public String getIp(){
        return IP;
    }

}

