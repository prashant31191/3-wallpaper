package com.hdloveimages;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.hdloveimages.reminder.ActReminder;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Prashant on 21-12-2017.
 */

public class ActNotification extends Activity
{
    Button btnPushNotification;
    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnPushNotification = (Button)findViewById(R.id.btnPushNotification);
        btnPushNotification.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(ActNotification.this, ActCustomeNotification.class);
                startActivity(intent);
                return false;
            }
        });
        btnPushNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //message will contain the Push Message
                String message = "Message detail here..";
                //imageUri will contain URL of the image to be displayed with Notification
                String imageUri = "https://raw.githubusercontent.com/prashant31191/tcpip-android/master/TcpipAndroid/chat/1images.jpg";
                String imageUri2 = "https://raw.githubusercontent.com/prashant31191/tcpip-android/master/TcpipAndroid/chat/2images.jpg";
                String imageUri3 = "https://raw.githubusercontent.com/prashant31191/tcpip-android/master/TcpipAndroid/chat/3images.jpg";
                //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
                //If the key AnotherActivity has  value as False then when the user taps on notification, in the app ActReminder will be opened.
                String TrueOrFlase = "AnotherActivity";

                //To get a Bitmap image from the URL received
                bitmap = getBitmapfromUrl(imageUri);

                sendNotification(message, bitmap, TrueOrFlase);

                Intent intent = new Intent(ActNotification.this, ActReminder.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendNotification(String messageBody, Bitmap image, String TrueOrFalse) {
        Intent intent = new Intent(this, ActNotification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("AnotherActivity", TrueOrFalse);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_favorite_border_black_18dp)
                .setContentTitle(messageBody)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image))/*Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }




}
