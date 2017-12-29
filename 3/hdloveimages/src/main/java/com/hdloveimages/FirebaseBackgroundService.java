package com.hdloveimages;

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
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by prashant.patel on 12/22/2017.
 */

public class FirebaseBackgroundService extends WakefulBroadcastReceiver {

    private static final String TAG = "FirebaseService";
    Context mContext;
    String image = "test";
    String message = "test";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "I'm in!!!");
        mContext = context;

        try {
            if (android.os.Build.VERSION.SDK_INT > 9)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            if (intent.getExtras() != null) {


                if( intent.getExtras().get("image")!=null)
                {
                    image = intent.getExtras().get("image").toString();
                }

                if(intent.getExtras().get("message")!=null)
                {
                    message = intent.getExtras().get("message").toString();
                }


                showNotification(message, image);



                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        Object value = bundle.get(key);
                        Log.d(TAG, String.format("%s %s (%s)", key,
                                value.toString(), value.getClass().getName()));
                    }
                }


                for (String key : intent.getExtras().keySet()) {
                    String value = intent.getExtras().get(key).toString();
                    Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
                    if (key.equalsIgnoreCase("gcm.notification.body") && value != null) {
                        //  Bundle bundle = new Bundle();
                        Intent backgroundIntent = new Intent(context, ActNotification.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        bundle.putString("image_data", value + "");
                        backgroundIntent.putExtras(bundle);
                        context.startActivity(backgroundIntent);
                        //context.startService(backgroundIntent);
                    }
                }
            }

        }
        catch (Exception e )
        {
            e.printStackTrace();
        }

    }



    private void showNotification(String message,String attachment) {
        try {

            Intent intent = new Intent(mContext, ActNotification.class);
            //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // asynRead1Badge(badgetype);
            int notifitionIdTime = (int) System.currentTimeMillis();
            PendingIntent pendingIntent;
            pendingIntent = PendingIntent.getActivity(mContext, notifitionIdTime ,
                    intent, PendingIntent.FLAG_ONE_SHOT);


            Bitmap imgBitmap  = getBitmapfromUrl222(attachment);


            if( imgBitmap ==null)
            {
                imgBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.noti_img_bg);
            }




            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
            try {

                Log.d("-0000-","====test1===");
                if (attachment != null && attachment.length() > 0) { // WIth Large Image - Rich Notification
                    notificationBuilder //= new NotificationCompat.Builder(this)
                            //.setSmallIcon(R.drawable.ic_launcher)
                            //.setSmallIcon(R.mipmap.ic_launcher)
                            .setSmallIcon(R.drawable.ic_favorite_border_black_18dp)
                            .setContentTitle(mContext.getResources().getString(R.string.app_name))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    //This one is same as large icon but it wont show when its expanded that's why we again setting
                                    //.bigLargeIcon(Picasso.with(this).load(attachment).get())
                                    //This is Big Banner image
                                    .bigPicture(imgBitmap)
                                    // .bigPicture(Picasso.with(this).load(attachment).get())
                                    // .bigPicture(finalBitmap)
                                    //When Notification expanded title and content text
                                    .setBigContentTitle(mContext.getResources().getString(R.string.app_name))
                                    .setSummaryText(message)
                            );
                } else {

                    Log.d("-0000-","====test2===");
                    notificationBuilder //= new NotificationCompat.Builder(this)
                            //.setSmallIcon(R.drawable.ic_launcher)
                            //.setSmallIcon(R.mipmap.ic_launcher)
                            .setSmallIcon(R.drawable.ic_favorite_border_black_18dp)
                            .setContentTitle(mContext.getResources().getString(R.string.app_name))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


 /*if (notificationManager == null)
 notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);*/
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notifitionIdTime , notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapfromUrl222(String imageUrl) {
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