package com.hdloveimages.reminder;

/**
 * Created by prashant.patel on 12/22/2017.
 */


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hdloveimages.ActNotification;
import com.hdloveimages.MyFirebaseMessagingService;
import com.hdloveimages.R;

/**
 * Created by Jaison on 17/06/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    Context mContext ;
    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        mContext = context;
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        String strLink = "asdasd";
        showNotification("this is message noti",strLink);

        //Trigger the notification
        NotificationScheduler.showNotification(context, ActReminder.class,
                "You have 5 unwatched videos", "Watch them now?");

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

            Bitmap imgBitmap  = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.noti_img_bg);


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

}