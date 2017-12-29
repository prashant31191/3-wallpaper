package com.azwallpaper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fmsirvent.ParallaxEverywhere.PEWImageView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.model.GsonResponseWallpaperList;
import com.model.JsonImageModel;
import com.squareup.picasso.Transformation;
import com.utils.Blur;
import com.utils.RealmBackupRestore;
import com.utils.TouchImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**

 */
public class LocalWallpaperActivity extends AppCompatActivity {
    private static final String TAG = LocalWallpaperActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private GridLayoutManager mLayoutManager;
    private static final int COLUMN_NUM = 2;
    private GalleryAdapter mAdapter;

    RelativeLayout rlImage;
    TouchImageView ivFullScreen;
    ImageView ivClose;
    TextView tvSetWallpaper;
    Bitmap bitmap = null;

    String id = "1";
    String strTitle = "Wallpaper";


    Realm realm;

    /**
     * Sets the Action Bar for new Android versions.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup() {
        try {
            ((LocalWallpaperActivity) this).getSupportActionBar().setTitle(strTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "----------onCreate----------");

            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_dashboard);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            rlImage = (RelativeLayout) findViewById(R.id.rlImage);
            ivFullScreen = (TouchImageView) findViewById(R.id.ivFullScreen);
            ivClose = (ImageView) findViewById(R.id.ivClose);
            tvSetWallpaper = (TextView) findViewById(R.id.tvSetWallpaper);
            tvSetWallpaper.setTypeface(App.getFont_Regular());

            tvSetWallpaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bitmap != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setWallpaper();
                            }
                        }, 500);

                    } else {
                        Toast.makeText(getApplicationContext(), "Please wait Image is Loading...!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.GONE);
                    rlImage.setVisibility(View.GONE);


                }
            });


            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setHasFixedSize(true);

            if (getIntent() != null && getIntent().getExtras() != null) {
                if (getIntent().getExtras().getString("id") != null) {
                    id = getIntent().getExtras().getString("id");
                }

                if (getIntent().getExtras().getString("title") != null) {
                    strTitle = getIntent().getExtras().getString("title");
                    actionBarSetup();
                }

            }

            if (id != null) {






           /* // Clear the realm from last time
            Realm.deleteRealm(realmConfiguration);
*/
            /*RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                    .encryptionKey(App.getEncryptRawKey())
                    .build();
*/
                //realm = Realm.getInstance(realmConfiguration);
                realm = Realm.getInstance(App.getRealmConfiguration());


                if (realm != null) {

                    //insertValues();
                    getWallpaperValues();

                } else {
                    App.showLog("=====realm===null==");
                }
            } else {
                progressBar.setVisibility(View.GONE);
            }

            setDisplayBanner();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    AdView mAdView;
    RelativeLayout rlAds;
    private void setDisplayBanner()
    {


        //String deviceid = tm.getDeviceId();

        rlAds = (RelativeLayout) findViewById(R.id.rlAds);
        rlAds.setVisibility(View.VISIBLE);
        mAdView = new AdView(LocalWallpaperActivity.this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(App.ADS_ID_BANNER2);

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        rlAds.addView(mAdView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("35588250B43518CCDA7DE425504EE232")
                .build();
        //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        //.addTestDevice(deviceid).build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
                rlAds.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertValues() {
        try {
            // for the insert value
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ArrayList<JsonImageModel> arrayListJsonImageModel = new ArrayList<>();
                    if (id.equalsIgnoreCase("13")) {
                        arrayListJsonImageModel = App.getJsonFromGson("popular.json", id, realm);
                        // App.gettingRecordFromJson("popular.json");
                        //arrayListJsonImageModel = App.gettingRecordFromJson("z12cate_.json");
                        //arrayListJsonImageModel = App.gettingRecordFromJson("popular.json");
                        Collections.reverse(arrayListJsonImageModel);
                    } else if (id.equalsIgnoreCase("12")) {
                        arrayListJsonImageModel = App.getJsonFromGson("z" + id + "cate_.json", id, realm);
                        Collections.reverse(arrayListJsonImageModel);
                    } else {
                        arrayListJsonImageModel = App.getJsonFromGson("z" + id + "cate_.json", id, realm);
                        //arrayListJsonImageModel = App.gettingRecordFromJson("z" + id + "cate_.json");
                    }
                    if (arrayListJsonImageModel != null) {
                        App.showLog("====arrayListJsonImageModel===" + arrayListJsonImageModel.size());
                        mLayoutManager = new GridLayoutManager(LocalWallpaperActivity.this, COLUMN_NUM);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new GalleryAdapter(LocalWallpaperActivity.this, arrayListJsonImageModel);
                        mRecyclerView.setAdapter(mAdapter);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }, 1500);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void getWallpaperValues() {
        try {
            // for the insert value
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ArrayList<JsonImageModel> arrayListJsonImageModel = new ArrayList<>();
                    if (id.equalsIgnoreCase("13")) {
                        arrayListJsonImageModel = getDataWallpaper("popular.json", id, realm);
                        // App.gettingRecordFromJson("popular.json");
                        //arrayListJsonImageModel = App.gettingRecordFromJson("z12cate_.json");
                        //arrayListJsonImageModel = App.gettingRecordFromJson("popular.json");
                        Collections.reverse(arrayListJsonImageModel);
                    } else if (id.equalsIgnoreCase("12")) {
                        arrayListJsonImageModel = getDataWallpaper("z" + id + "cate_.json", id, realm);
                        Collections.reverse(arrayListJsonImageModel);
                    } else {
                        arrayListJsonImageModel = getDataWallpaper("z" + id + "cate_.json", id, realm);
                        //arrayListJsonImageModel = App.gettingRecordFromJson("z" + id + "cate_.json");
                    }
                    if (arrayListJsonImageModel != null) {
                        App.showLog("====arrayListJsonImageModel===" + arrayListJsonImageModel.size());
                        mLayoutManager = new GridLayoutManager(LocalWallpaperActivity.this, COLUMN_NUM);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new GalleryAdapter(LocalWallpaperActivity.this, arrayListJsonImageModel);
                        mRecyclerView.setAdapter(mAdapter);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }

                    if(arrayListJsonImageModel==null || arrayListJsonImageModel.size() <= 1)
                    {
                        if (App.checkDbFileIsExist() == true && realm !=null) {
                            RealmBackupRestore realmBackupRestore = new RealmBackupRestore(LocalWallpaperActivity.this, realm);
                            realmBackupRestore.restore();

                        }
                        else
                        {
                            App.downloadPhoto2();
                        }
                    }
                }
            }, 1500);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public ArrayList<JsonImageModel> getDataWallpaper(String strFileName,String id,Realm realm) {
        List<JsonImageModel> list = new ArrayList<>();
        try {

            App.showLog("========getDataWallpaper=====");
            ArrayList<GsonResponseWallpaperList> arrGsonResponseWallpaperList = new ArrayList<>();

            RealmResults<GsonResponseWallpaperList> arrDLocationModel = realm.where(GsonResponseWallpaperList.class)
                    .beginGroup()
                    .equalTo("filename", strFileName)
                    /*.or()
                    .contains("name", "Jo")*/
                    .endGroup()

                    .findAll();

            App.sLog("===arrDLocationModel==" + arrDLocationModel);
            List<GsonResponseWallpaperList> gsonResponseWallpaperList = arrDLocationModel;
            arrGsonResponseWallpaperList = new ArrayList<GsonResponseWallpaperList>(gsonResponseWallpaperList);

            for (int k = 0; k < arrGsonResponseWallpaperList.size(); k++) {
                App.sLog(k + "===arrGsonResponseWallpaperList=name=" + arrGsonResponseWallpaperList.get(k).filename);

                if( arrGsonResponseWallpaperList.get(k).arrayListJsonImageModel !=null && arrGsonResponseWallpaperList.get(k).arrayListJsonImageModel.size() > 0)
                {
                    App.sLog(k + "===arrGsonResponseWallpaperList=size=" + arrGsonResponseWallpaperList.get(k).arrayListJsonImageModel.size());
                    list = arrGsonResponseWallpaperList.get(k).arrayListJsonImageModel;


                    break;
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new ArrayList<JsonImageModel>(list);
    }

    private void setWallpaper() {
        String filename = "wallpaper.png";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();


            Uri uri = Uri.fromFile(dest);
            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("mimeType", "image/*");

            progressBar.setVisibility(View.GONE);
            this.startActivity(Intent.createChooser(intent, "Set as :"));

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

        private Context mContext;   // get resource of this component
        private List<JsonImageModel> mList;

        public GalleryAdapter(Context mContext, List<JsonImageModel> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public PEWImageView mImageView;
            public TextView tvTitle;
            public CardView cvCard;


            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (PEWImageView) itemView.findViewById(R.id.gallery_item);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                cvCard = (CardView) itemView.findViewById(R.id.cvCard);

                tvTitle.setTypeface(App.getFont_Regular());
            }
        }

        @Override
        public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_wallpaper,
                    parent, false);
            GalleryAdapter.ViewHolder vh = new GalleryAdapter.ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, final int position) {
//        Contact item = mList.get(position);
            final JsonImageModel item = mList.get(position);
            holder.tvTitle.setText(position + " ♥ " + item.title);
            holder.cvCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //App.expand2(rlImage);
                    rlImage.setVisibility(View.VISIBLE);//
                    progressBar.setVisibility(View.VISIBLE);

                    App.showLog("==img==" + mList.get(position).thumbnail_url);

                    Glide.with(LocalWallpaperActivity.this).load(mList.get(position).image_path).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            try {
                                bitmap = resource;
                                ivFullScreen.setImageBitmap(resource);
                                progressBar.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Glide.with(LocalWallpaperActivity.this).load(mList.get(position).image_path).placeholder(R.color.light_gray).into(ivFullScreen);

                }
            });

            Glide.with(LocalWallpaperActivity.this)
                    .load(item.thumbnail_url)
                    .thumbnail(0.5f)
                    .placeholder(R.color.light_gray)
                    .into(holder.mImageView);



         /*   Picasso.with(mContext)
                    .load(item.thumbnail_url)
                    .resize(10, 10)
                    //.fit()
                    //.centerCrop()

                    .error(R.mipmap.ic_launcher)
                    .transform(blurTransformation)
                    .into(holder.mImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(mContext)
                                    .load(item.thumbnail_url) // image url goes here

                                    .into(holder.mImageView);
                        }

                        @Override
                        public void onError() {
                        }
                    });*/

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        //    public void addAll(List<Contact> newList) {
        public void addAll(List<JsonImageModel> newList) {
            mList.addAll(newList);
        }

        public void add(JsonImageModel item) {
            mList.add(item);
        }

        public void clear() {
            mList.clear();
        }


        Transformation blurTransformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap blurred = Blur.fastblur(mContext, source, 10);
                source.recycle();
                return blurred;
            }

            @Override
            public String key() {
                return "blur()";
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    @Override
    public void onBackPressed() {
        if (ivClose != null && rlImage != null && rlImage.getVisibility() == View.VISIBLE) {
            ivClose.performClick();
        } else {
            super.onBackPressed();
        }
    }
}
