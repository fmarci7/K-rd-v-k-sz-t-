package com.example.projekt2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class ErtesitesKezelo {
    private static final String CHANNEL_ID = "notification_channel";
    private final int NOTIFICATION_ID = 0;

    private Context mContext;
    private NotificationManager mManager;
    public ErtesitesKezelo(Context context) {
        this.mContext=context;
        this.mManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }else{
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Ertesites",NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.RED);
            channel.setDescription("Ertesitesek az appbol");
            this.mManager.createNotificationChannel(channel);
        }
    }

    public void send(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext,CHANNEL_ID)
                .setContentTitle("KerdoivKeszitoApp")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_background);
        this.mManager.notify(NOTIFICATION_ID,builder.build());
    }
}
