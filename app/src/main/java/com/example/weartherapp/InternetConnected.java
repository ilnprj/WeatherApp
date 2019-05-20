package com.example.weartherapp;

import java.net.InetAddress;

public class InternetConnected {
    public static Boolean isInternetConnected(){
        boolean status = false;
        try{
            InetAddress address = InetAddress.getByName("google.com");

            if(address!=null)
            {
                status = true;
            }
        }catch (Exception e) // Catch the exception
        {
            e.printStackTrace();
        }
        return status;
    }
}
