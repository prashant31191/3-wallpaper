package com.azwallpaper;

import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;

import android.support.multidex.MultiDex;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.model.GsonResponseWallpaperList;
import com.model.JsonDashboardModel;
import com.model.JsonImageModel;
import com.utils.SharePrefrences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;


import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by prashant.patel on 7/20/2017.
 */

public class App extends Application {

    String TAG = "====App==";

    // app folder name
    public static String APP_FOLDERNAME = "azwallpaper";
    public static String ADS_ID_BANNER = "ca-app-pub-4346653435295459/3341327605";
    public static String ADS_ID_BANNER2 = "ca-app-pub-4346653435295459/7033160603";
    public static String ADS_ID_RVID = "ca-app-pub-4346653435295459/1206118856";
    public static final String APP_ID = "ca-app-pub-4346653435295459~7028580736";

    //for the realm database encryption and decryption key
    public static String RealmEncryptionKey = "f263575e7b00a977a8e915feb9bfb2f992b2b8f11eaaaaaaa46523132131689465413132132165469487987987643545465464abbbbbccdddffff111222333";
    public static RealmConfiguration realmConfiguration;

    /*
    // Sending side
    public byte[] bytes64Key = App.RealmEncryptionKey.getBytes("UTF-8");

    String base64 = Base64.encodeToString(bytes64Key, Base64.DEFAULT);
    // Receiving side
    byte[] dataReceive = Base64.decode(base64, Base64.DEFAULT);
    String text = new String(dataReceive, "UTF-8");*/

    // share pref name
    public static String PREF_NAME = "azwallpaper_app";

    // class for the share pref keys and valyes get set
    public static SharePrefrences sharePrefrences;


    // for the app context
    static Context mContext;

    // for the set app fontface or type face
    static Typeface tf_Regular, tf_Bold;

    public App() throws UnsupportedEncodingException {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            MultiDex.install(this);

            mContext = getApplicationContext();
            sharePrefrences = new SharePrefrences(App.this);

            Realm.init(this);
            Fabric.with(this, new Crashlytics());
            realmConfiguration = getRealmConfiguration();

            getFont_Regular();
            getFont_Bold();
            createAppFolder();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void sLog(String strTag,String strMsg)
    {
        Log.d("---1--"+strTag,"--2--"+strMsg);
    }
    public static void sLog(String strMsg)
    {
        Log.d("---1--","--2--"+strMsg);
    }





    public static boolean isInternetAvail(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void hideSoftKeyboardMy(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


   /* public static void myStartActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    public static void myFinishActivityRefresh(Activity activity, Intent intent) {
        activity.finish();
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public static void myFinishActivity(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
*/

    public static void expand(final View v) {
        //v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); //WRAP_CONTENT
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        //? WindowManager.LayoutParams.WRAP_CONTENT //WRAP_CONTENT
                        ? WindowManager.LayoutParams.MATCH_PARENT //WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void expandWRAP_CONTENT(final Button button) {
        //button.setVisibility(View.VISIBLE);
        // Prepare the View for the animation
        button.setVisibility(View.VISIBLE);
      /*  button.setAlpha(0.0f);

// Start the animation
        button.animate()
                .translationY(button.getHeight())
                .alpha(1.0f);*/
    }
    public static void expandWRAP_CONTENT(final View v) {
        //v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); //WRAP_CONTENT
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        //? WindowManager.LayoutParams.WRAP_CONTENT //WRAP_CONTENT
                        ? WindowManager.LayoutParams.MATCH_PARENT //WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }



    public static String doubleHtmlConvert(String html)
    {
        return  String.valueOf(Html.fromHtml(String.valueOf(Html.fromHtml(html))));
    }



    public static String singleHtmlConvert(String html)
    {
        return  String.valueOf(Html.fromHtml(html));
    }

/*

    public static RequestBody createPartFromString(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    private static RequestBody createPartFromFile(File file) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), file);
    }
*/



    public static String getCurrentDateTime() {
        String current_date = "";
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        current_date = postFormater.format(c.getTime());

        return current_date;
    }


    public static void showToastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }


    public static void showToastShort(Context context, String strMessage) {
        Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
    }


    public static void showSnackBar(View view, String strMessage) {
        Snackbar snackbar = Snackbar.make(view, strMessage, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }


    public static void showSnackBarLong(View view, String strMessage) {
        Snackbar.make(view, strMessage, Snackbar.LENGTH_LONG).show();
    }


    public static void showLog(String strMessage) {
        Log.v("==App==", "--strMessage--" + strMessage);
    }public static void showLog(String strMessage1,String strMessage) {
        Log.v("==App tag=="+strMessage1, "--strMessage--" + strMessage);
    }


    public static void showLogApi(String strMessage) {
        //Log.v("==App==", "--strMessage--" + strMessage);
        System.out.println("--API-MESSAGE--" + strMessage);

        //  appendLogApi("c_api", strMessage);
    }

    public static void showLogApi(String strOP, String strMessage) {
        //Log.v("==App==", "--strMessage--" + strMessage);
        System.out.println("--API-strOP--" + strOP);
        System.out.println("--API-MESSAGE--" + strMessage);

        // appendLogApi(strOP + "_c_api", strMessage);
    }
/*

    public static void showLogApiRespose(String op, Response response) {
        //Log.w("=op==>" + op, "response==>");
        String strResponse = new Gson().toJson(response.body());
        Log.i("=op==>" + op, "response==>" + strResponse);
        // appendLogApi(op + "_r_api", strResponse);
    }
*/


    public static void showLogResponce(String strTag, String strResponse) {
        Log.i("==App==strTag==" + strTag, "--strResponse--" + strResponse);
        //appendLogApi(strTag + "_r_api", strResponse);
    }

    public static void appendLogApi(String fileName, String text) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMM_dd_HHmmss");
            String currentDateandTime = sdf.format(new Date());

            String sdCardPath = sdCardPath = Environment.getExternalStorageDirectory().toString();

            File logFile = new File(sdCardPath, "/" + App.APP_FOLDERNAME + "/AppLog2/" + fileName + "_" + currentDateandTime + "_lg.txt");
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    showLog(e.getMessage()+"======");
                }
            }

            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (Exception e) {

            showLog( e.getMessage()+"======");
        }
    }

    public static Typeface getFont_Regular() {
        tf_Regular = Typeface.createFromAsset(mContext.getAssets(), "font/roboto_regular.ttf");
        return tf_Regular;
    }


    public static Typeface getFont_Bold() {
        //tf_Bold = Typeface.createFromAsset(mContext.getAssets(), "font/pacifico.ttf");
        tf_Bold = Typeface.createFromAsset(mContext.getAssets(), "font/pacifico.ttf");
        return tf_Bold;
    }

    private void createAppFolder() {
        try {
            String sdCardPath = Environment.getExternalStorageDirectory().toString();
            //File file2 = new File(sdCardPath + "/" + App.APP_FOLDERNAME + "");
            File file2 = new File(sdCardPath + "/" + App.APP_FOLDERNAME + "/AppLog2");
            if (!file2.exists()) {
                if (!file2.mkdirs()) {
                    System.out.println("==Create Directory " + App.APP_FOLDERNAME + "====");
                } else {
                    System.out.println("==No--1Create Directory " + App.APP_FOLDERNAME + "====");
                }
            } else {
                System.out.println("== already created---No--2Create Directory " + App.APP_FOLDERNAME + "====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getAppFolderName()
    {
        return Environment.getExternalStorageDirectory().toString() +  "/" + App.APP_FOLDERNAME ;
    }


    public static  int getMatColor(String typeColor)
    {
        int returnColor = Color.BLACK;
        int arrayId = mContext.getResources().getIdentifier("mdcolor_" + typeColor, "array", mContext.getPackageName());

        if (arrayId != 0)
        {
            TypedArray colors = mContext.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }


    public static ArrayList<JsonImageModel> gettingRecordFromJson(String strFilename)
    {
        ArrayList<JsonImageModel> arrayListDLocationModel = new ArrayList<>();

        try{

            //Get Data From Text Resource File Contains Json Data.

            //InputStream inputStream = mContext.getResources().openRawResource(intRawJsonFile);
            InputStreamReader inputStream = new InputStreamReader(mContext.getAssets().open(strFilename));


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int ctr;
            try {
                ctr = inputStream.read();
                while (ctr != -1) {
                    byteArrayOutputStream.write(ctr);
                    ctr = inputStream.read();
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
           // Log.v("Text Data", byteArrayOutputStream.toString());
            try {

                // Parse the data into jsonobject to get original data in form of json.
                JSONObject jObject = new JSONObject(byteArrayOutputStream.toString());

                JSONArray jArray = jObject.getJSONArray("data");
                String  title="";
                String  image_path="";
                String preview ="";
                String thumbnail_url ="";


                for (int i = 0; i < jArray.length(); i++) {
                    title = jArray.getJSONObject(i).getString("title");
                    image_path = jArray.getJSONObject(i).getString("image_path");
                    preview = jArray.getJSONObject(i).getString("preview");
                    thumbnail_url = jArray.getJSONObject(i).getString("thumbnail_url");

                    JsonImageModel jsonImageModel = new JsonImageModel();
                    jsonImageModel.title = title;
                    jsonImageModel.image_path = image_path;
                    jsonImageModel.preview = preview;
                    jsonImageModel.thumbnail_url = thumbnail_url;
                    arrayListDLocationModel.add(jsonImageModel);

                 //   Log.i("=====","==title=="+title);
                }
                return arrayListDLocationModel;
            } catch (Exception e) {
                e.printStackTrace();
                return arrayListDLocationModel;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return arrayListDLocationModel;
        }
    }
    public static ArrayList<JsonDashboardModel> gettingRecordFromJsonDashboard(int intRawJsonFile)
    {
        ArrayList<JsonDashboardModel> arrayListDLocationModel = new ArrayList<>();

        try{

            //Get Data From Text Resource File Contains Json Data.

            InputStream inputStream = mContext.getResources().openRawResource(intRawJsonFile);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int ctr;
            try {
                ctr = inputStream.read();
                while (ctr != -1) {
                    byteArrayOutputStream.write(ctr);
                    ctr = inputStream.read();
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.v("Text Data", byteArrayOutputStream.toString());
            try {

                // Parse the data into jsonobject to get original data in form of json.
                JSONObject jObject = new JSONObject(byteArrayOutputStream.toString());

                JSONArray jArray = jObject.getJSONArray("dashboard");
                String id ="";
                String  name="";
                String  keyword="";
                String image ="";


                for (int i = 0; i < jArray.length(); i++) {
                    id = jArray.getJSONObject(i).getString("id");
                    name = jArray.getJSONObject(i).getString("name");
                    keyword = jArray.getJSONObject(i).getString("keyword");
                    image = jArray.getJSONObject(i).getString("image");

                    JsonDashboardModel jsonImageModel = new JsonDashboardModel();
                    jsonImageModel.id = id;
                    jsonImageModel.name = name;
                    jsonImageModel.keyword = keyword;
                    jsonImageModel.image = image;
                    arrayListDLocationModel.add(jsonImageModel);

                    Log.i("=====","==name=="+name);
                }
                return arrayListDLocationModel;
            } catch (Exception e) {
                e.printStackTrace();
                return arrayListDLocationModel;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return arrayListDLocationModel;
        }
    }


    public static ArrayList<JsonImageModel>  getJsonFromGson(String strFilename, String id, Realm realm)
    {
        ArrayList<JsonImageModel> arrayList = new ArrayList<JsonImageModel>();

        String json = null;
        try {
            InputStream inputStream = mContext.getAssets().open(strFilename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            Gson gson =new Gson();
            GsonResponseWallpaperList gsonResponseWallpaperList = gson.fromJson(json, GsonResponseWallpaperList.class);
            gsonResponseWallpaperList.filename = strFilename;

            if(gsonResponseWallpaperList  !=null && gsonResponseWallpaperList .arrayListJsonImageModel !=null)
            {
                insertWallpaper(realm,gsonResponseWallpaperList );

                //return null;

                if(gsonResponseWallpaperList.arrayListJsonImageModel !=null) {
                    List<JsonImageModel> list = gsonResponseWallpaperList.arrayListJsonImageModel;
                    arrayList = new ArrayList<JsonImageModel>(list);

                    return arrayList;
                }
                else
                {
                    return arrayList;
                }

            }
            else {
                return arrayList;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return arrayList;
        }

    }

    public static void insertWallpaper(Realm realm, GsonResponseWallpaperList gsonResponseWallpaperList) {
        try {
            App.showLog("========insertWallpaper=====");


            realm.beginTransaction();
            GsonResponseWallpaperList realmDJsonDashboardModel = realm.copyToRealm(gsonResponseWallpaperList);
            realm.commitTransaction();

            getDataWallpaper(realm);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                realm.commitTransaction();
            }
            catch (Exception e2)
            {e2.printStackTrace();}
        }

    }

    public static void getDataWallpaper(Realm realm) {
        try {
            App.showLog("========getDataWallpaper=====");
            ArrayList<GsonResponseWallpaperList> arrGsonResponseWallpaperList = new ArrayList<>();

            RealmResults<GsonResponseWallpaperList> arrDLocationModel = realm.where(GsonResponseWallpaperList.class).findAll();
            App.sLog("===arrDLocationModel==" + arrDLocationModel);
            List<GsonResponseWallpaperList> gsonResponseWallpaperList = arrDLocationModel;
            arrGsonResponseWallpaperList = new ArrayList<GsonResponseWallpaperList>(gsonResponseWallpaperList);

            for (int k = 0; k < arrGsonResponseWallpaperList.size(); k++) {
                App.sLog(k + "===arrGsonResponseWallpaperList=name=" + arrGsonResponseWallpaperList.get(k).filename);

                App.sLog(k + "===arrGsonResponseWallpaperList=size=" + arrGsonResponseWallpaperList.get(k).arrayListJsonImageModel.size());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
// for the encrypt Encrypt
    public static byte[] getEncryptRawKey() {

        try {
            /*byte[] bytes64Key = App.RealmEncryptionKey.getBytes("UTF-8");
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(bytes64Key);
            kgen.init(128, sr);
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();*/

            byte[] key = new BigInteger(App.RealmEncryptionKey,16).toByteArray();
            return key;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public static RealmConfiguration getRealmConfiguration()
    {
        if(realmConfiguration !=null)
        {
            return realmConfiguration;
        }
        else
        {
/*

            realmConfiguration = new RealmConfiguration.Builder()
                    .encryptionKey(App.getEncryptRawKey())
                    .build();
*/


            realmConfiguration = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .encryptionKey(App.getEncryptRawKey())
                    .build();


            return realmConfiguration;
        }
    }

    //file dowload from url and set database
    public static void downloadPhoto2() {
        try {
            App.showLog("=======downloadPhoto2=====");
             DownloadManager mDownloadManager;

            mDownloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);

            String folderName = "files";

            // String url = "https://raw.githubusercontent.com/prashant31191/BitTiger-MiniFlickr/master/app/apk_keystore/files/download.realm";
            // no encrypt - String url = "https://raw.githubusercontent.com/prashant31191/BitTiger-MiniFlickr/master/app/apk_keystore/files/sm-download.realm";
            String url = "https://raw.githubusercontent.com/prashant31191/BitTiger-MiniFlickr/master/app/apk_keystore/files/11download.realm";
            URL urlObj = new URL(url);
            String urlPath = urlObj.getPath();
            String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
// fileName is now "somefilename.xy"

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle("Download...");
            request.setDescription("Please wait...!");
            //request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,File.separator + folderName + File.separator + fileName);
            request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, File.separator + "download.realm");
            //request.setDestinationInExternalFilesDir(mContext, folderName, "default.realm");
            //request.setDestinationInExternalFilesDir(mContext, "", "default.realm");
            mDownloadManager.enqueue(request);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static boolean checkDbFileIsExist() {
        try {
App.showLog("=======checkDbFileIsExist=====");


            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)  + File.separator + "download.realm");
            if (file.exists()) {
                //Do something
                return true;
            } else {
                // Do something else.
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFile()
    {
        App.showLog("=======deleteFile=====");

        try {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator +  "download.realm");
            if (file.exists()) {
                //Do something
                file.delete();

                return true;
            } else {
                // Do something else.
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean renameFile(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }


}
