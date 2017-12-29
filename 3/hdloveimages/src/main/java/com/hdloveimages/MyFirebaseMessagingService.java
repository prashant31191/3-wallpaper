package com.hdloveimages;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Prashant on 21-12-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;

    /**
     * new application link -
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //
        Log.d(TAG, "From: " + remoteMessage.getFrom());



        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        setCustomViewNotification();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //The message which i send will have keys named [message, image, AnotherActivity] and corresponding values.
        //You can change as per the requirement.
// --  {image=https://raw.githubusercontent.com/prashant31191/tcpip-android/master/TcpipAndroid/chat/3images.jpg, message=tet}
        //message will contain the Push Message
        String message = remoteMessage.getData().get("message");
        //imageUri will contain URL of the image to be displayed with Notification
        String imageUri = remoteMessage.getData().get("image");
        //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
        //If the key AnotherActivity has  value as False then when the user taps on notification, in the app ActReminder will be opened.
        String TrueOrFlase = remoteMessage.getData().get("AnotherActivity");


        showNotification("this is test","this is image url");

        //To get a Bitmap image from the URL received
        bitmap = getBitmapfromUrl(imageUri);
        showNotificationOld(imageUri,"Title....",message,R.drawable.ic_favorite_border_black_18dp);
        sendNotification2(message, bitmap, TrueOrFlase);




        //To get a Bitmap image from the URL received
        //bitmap = getBitmapfromUrl(imageUri);
        new generatePictureStyleNotification2222(this.getApplicationContext(), "App name title", message, "11:54", imageUri).execute();


    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendNotification(String messageBody, Bitmap image, String TrueOrFalse) {
        Intent intent = new Intent(this, ActNotification.class);

        intent.putExtra("AnotherActivity", TrueOrFalse);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(image)/*Notification icon image*/
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


    public class generatePictureStyleNotification2222 extends AsyncTask<String, Void, Bitmap> {

        private Context mContext1;
        private String title1, message1, time1, imageUrl1;

        public generatePictureStyleNotification2222(Context context, String title, String message, String time, String imageUrl) {
            super();
            mContext1 = context;
            title1 = title;
            message1 = message;
            time1 = time;
            imageUrl1 = imageUrl;

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream input;
            try {
                URL url = new URL(imageUrl1);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                // File dir = new File(App.strFolderNamePath);
                try {

                   /* int count;

                    String fileName = imageUrl1.substring(imageUrl1.lastIndexOf('/') + 1);



                    if (dir.mkdirs()) {

                    }


                    OutputStream output = new FileOutputStream(App.strFolderNamePath + File.separator + fileName);
                    System.out.println("==save image path is ==>>" + App.strFolderNamePath + File.separator + fileName);

                    byte data[] = new byte[1024];
                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;

                        output.write(data, 0, count);
                    }
                    // flushing output
                    output.flush();
                    // closing streams
                    output.close();
                    input.close();*/


                } catch (Exception e) {
                    e.printStackTrace();
                }


                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            try {


                if (result == null) {
                    result = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                }

                result = BitmapFactory.decodeResource(getResources(),R.drawable.noti_img_bg);


                Intent intent = new Intent(MyFirebaseMessagingService.this, ActNotification.class);
                //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                int notifitionIdTime = (int) System.currentTimeMillis();

                PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, notifitionIdTime  /*Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                        //   .setSmallIcon(R.drawable.noti_icon1)
                        .setContentTitle(title1)
                        // .setContentText(this.message)
                        .setSubText(message1)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.ic_favorite_border_black_18dp);
                    notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext1.getResources(), R.mipmap.ic_launcher));
                } else {
                    notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
                }


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notifitionIdTime  /*ID of notification*/, notificationBuilder.build());


                //sendNotification(message1, result, title1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void showNotification(String message,String attachment) {
        try {

            Intent intent = new Intent(MyFirebaseMessagingService.this, ActNotification.class);
            //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // asynRead1Badge(badgetype);
            int notifitionIdTime = (int) System.currentTimeMillis();
            PendingIntent pendingIntent;
            pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), notifitionIdTime ,
                    intent, PendingIntent.FLAG_ONE_SHOT);

            Bitmap imgBitmap  = BitmapFactory.decodeResource(getResources(),R.drawable.noti_img_bg);


            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.getApplicationContext());
            try {

                Log.d("-0000-","====test1===");


                if (attachment != null && attachment.length() > 0) { // WIth Large Image - Rich Notification

                    Log.d("-attachment-","====attachment==="+attachment);

                    notificationBuilder //= new NotificationCompat.Builder(this)
                            //.setSmallIcon(R.drawable.ic_launcher)
                            //.setSmallIcon(R.mipmap.ic_launcher)
                            .setSmallIcon(R.drawable.ic_favorite_border_black_18dp)
                            .setContentTitle(getResources().getString(R.string.app_name))
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
                                    .setBigContentTitle(getResources().getString(R.string.app_name))
                                    .setSummaryText(message)
                            );
                } else {

                    Log.d("-0000-","====test2===");
                    notificationBuilder //= new NotificationCompat.Builder(this)
                            //.setSmallIcon(R.drawable.ic_launcher)
                            //.setSmallIcon(R.mipmap.ic_launcher)
                            .setSmallIcon(R.drawable.ic_favorite_border_black_18dp)
                            .setContentTitle(getResources().getString(R.string.app_name))
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
            NotificationManager notificationManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notifitionIdTime , notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapfromUrlMy(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bm = BitmapFactory.decodeStream(input);
            //Bitmap bitmap = cropToSquare(bm);
            Bitmap bitmap = Bitmap.createBitmap(bm, 0, 50, 720, 512);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }




















    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendNotification2(String messageBody, Bitmap image, String TrueOrFalse) {
        Intent intent = new Intent(this, ActNotification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("AnotherActivity", TrueOrFalse);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(image)/*Notification icon image*/
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


    public void showNotificationOld(final String img_url, final String title, final String message, final int smallIcon) {
        try {

            // To push notification from background thread
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try{


            final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(smallIcon)
                    .setContentIntent(
                            PendingIntent.getActivity(
                                    MyFirebaseMessagingService.this,
                                    0,
                                    new Intent(MyFirebaseMessagingService.this, ActNotification.class),
                                    PendingIntent.FLAG_UPDATE_CURRENT)
                    )
                    //here comes to load image by Picasso
                    //it should be inside try block
                    .setLargeIcon(Picasso.with(MyFirebaseMessagingService.this).load(img_url).get())
                    //BigPicture Style
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            //This one is same as large icon but it wont show when its expanded that's why we again setting
                            .bigLargeIcon(Picasso.with(MyFirebaseMessagingService.this).load(img_url).get())
                            //This is Big Banner image
                            .bigPicture(Picasso.with(MyFirebaseMessagingService.this).load(img_url).get())
                            //When Notification expanded title and content text
                            .setBigContentTitle(title)
                            .setSummaryText(message)
                    );

            //extra you can add actions

            manager.notify(1029, builder.build());

                  //  Picasso.with(context).load("URL").into(contentView, iconId, notificationId, notification);
                //    Picasso.with(context).load("BANNER_IMAGE_URL").into(bigContentView, bigIconId, notificationId, notification);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * Custom View Notification
     *
     * @return Notification
     * @see CreateNotification
     */
    private Notification setCustomViewNotification() {

        Bitmap imgBitmap  = BitmapFactory.decodeResource(getResources(),R.drawable.noti_img_bg);

        // Creates an explicit intent for an ActNotification to receive.
        Intent resultIntent = new Intent(this, ActNotification.class);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ActNotification.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create remote view and set bigContentView.
        RemoteViews expandedView = new RemoteViews(this.getPackageName(), R.layout.notification_custom_remote);
        expandedView.setTextViewText(R.id.text_view, "Neat logo!");
        expandedView.setImageViewBitmap(R.id.iv_photo,imgBitmap);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle("Custom View").build();

        notification.bigContentView = expandedView;

        return notification;
    }



}
