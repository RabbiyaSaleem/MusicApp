package com.uog.fyp.e.musicapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class Foreground extends Service {
    public static final String MY_TAG="MYTAG";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";




    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

          String input= intent.getStringExtra("inputExtra");
        createNotificationChannel();
        /*ShowNotification();*/
      Intent notificationIntent=new Intent(this, PlayerActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(123,notification);
        stopForeground(true);
        stopSelf();
        return START_NOT_STICKY;

       /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(MY_TAG, "run:starting download");
                int i = 0;
                while (i < 10) {
                    Log.d(MY_TAG, "run:Progress is:" + (i + 1));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                Log.d(MY_TAG, "run:download completed");

                stopForeground(true);
                stopSelf();
            }
        });
        thread.start();
        return START_STICKY;*/

    }
     /* private void ShowNotification(){
          NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"ChannelID");

          builder.setSmallIcon(R.mipmap.ic_launcher)
                  .setContentText("Service Notification")
                  .setContentTitle("Title");
          Notification notification=builder.build();
          startForeground(123,notification);
      }*/


    @Override
    public IBinder onBind(Intent intent) {
       // TODO: Return the Communication Channel to the Service.
        throw new UnsupportedOperationException("Not yet Completed");
        /*return null;*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

    }
}
