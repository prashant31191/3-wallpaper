package com.azwallpaper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fmsirvent.ParallaxEverywhere.PEWImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.model.JsonDashboardModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.utils.Blur;
import com.utils.RealmBackupRestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private static final int COLUMN_NUM = 1;
    private GalleryAdapter mAdapter;
    ArrayList<JsonDashboardModel> arrayListJsonDashboardModel = new ArrayList<>();

    Realm realm;

    FloatingActionButton fab;
    FloatingActionButton fabCreateBackup;
    FloatingActionButton fabRestoreBackup;

    private static final String TAG = DashboardActivity.class.getSimpleName();

    //for the video ads
    private RewardedVideoAd mRewardedVideoAd;
    boolean isClickEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_dashboard);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);

            fab = (FloatingActionButton) findViewById(R.id.fab);
            fabCreateBackup = (FloatingActionButton) findViewById(R.id.fabCreateBackup);
            fabCreateBackup.setVisibility(View.GONE);

            fabRestoreBackup = (FloatingActionButton) findViewById(R.id.fabRestoreBackup);

            fabRestoreBackup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (App.checkDbFileIsExist() == true) {
                        RealmBackupRestore realmBackupRestore = new RealmBackupRestore(DashboardActivity.this, realm);
                        realmBackupRestore.restore();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 3000);


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);

                        builder.setTitle("New wallpaper available here approx size = 2.3 MB");
                        builder.setMessage("Are you sure restore backup?");

                        builder.setPositiveButton("DOWNLOAD", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog

                                App.downloadPhoto2();

                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }




/*
                    Snackbar.make(view, "Are you sure restore backup ?", Snackbar.LENGTH_LONG)
                            .setAction("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (App.checkDbFileIsExist() == true) {
                                        RealmBackupRestore realmBackupRestore = new RealmBackupRestore(DashboardActivity.this, realm);
                                        realmBackupRestore.restore();

                                        Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        App.downloadPhoto2();
                                    }

                                }
                            }).show();

                    */
                }
            });

            fabCreateBackup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Snackbar.make(view, "Are you sure create backup ?", Snackbar.LENGTH_LONG)
                            .setAction("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RealmBackupRestore realmBackupRestore = new RealmBackupRestore(DashboardActivity.this, realm);
                                    realmBackupRestore.backup();

                                }
                            }).show();
                }
            });


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DashboardActivity.this, GalleryActivity.class);
                    startActivity(intent);

/*
                    RealmBackupRestore realmBackupRestore = new RealmBackupRestore(DashboardActivity.this, realm);
                    realmBackupRestore.backup();
*/


                    //realmBackupRestore.restore();


                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


            //RealmConfiguration realmConfiguration =

/*
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                    .encryptionKey(App.getEncryptRawKey())
                    .build();
*/


            // Clear the realm from last time
            // Realm.deleteRealm(realmConfiguration);
            //realm = Realm.getInstance(realmConfiguration);
            realm = Realm.getInstance(App.getRealmConfiguration());

           /*
           RealmBackupRestore realmBackupRestore = new RealmBackupRestore(DashboardActivity.this, realm);
            //realmBackupRestore.backup();
            realmBackupRestore.restore();
            realm = Realm.getInstance(realmConfiguration);
            */


// for the insert # !@#!@#
//  # !@#!@#

            if (realm != null && arrayListJsonDashboardModel != null) {
                //  # !@#!@#  insertDashboard();
                getDataDashboard();
            } else {
                App.showLog("===no insert database dashboard==");
            }

            setupAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isInserted = false;

    private void insertDashboard() {
        try {
            App.showLog("========insertDashboard=====");
            arrayListJsonDashboardModel = App.gettingRecordFromJsonDashboard(R.raw.dashboard);

            if (arrayListJsonDashboardModel != null) {

                isInserted = true;
                realm.beginTransaction();
                Collection<JsonDashboardModel> realmDJsonDashboardModel = realm.copyToRealm(arrayListJsonDashboardModel);
                realm.commitTransaction();

                getDataDashboard();

            } else {
                App.showLog("===arrayListJsonDashboardModel ==null==no insert database dashboard==");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataDashboard() {
        try {
            App.showLog("========getDataDashboard=====");

            RealmResults<JsonDashboardModel> arrDLocationModel = realm.where(JsonDashboardModel.class).findAll();
            App.sLog("===arrDLocationModel==" + arrDLocationModel);
            List<JsonDashboardModel> arraDLocationModel = arrDLocationModel;
            arrayListJsonDashboardModel = new ArrayList<JsonDashboardModel>(arraDLocationModel);

            if (arrayListJsonDashboardModel != null) {
                App.showLog("====arrayListJsonDashboardModel===" + arrayListJsonDashboardModel.size());


                mLayoutManager = new GridLayoutManager(DashboardActivity.this, COLUMN_NUM);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new GalleryAdapter(DashboardActivity.this, arrayListJsonDashboardModel);
                mRecyclerView.setAdapter(mAdapter);
            }

            if (arrayListJsonDashboardModel.size() > 0) {

            } else {
                if (isInserted == false) {
                    insertDashboard();
                }
            }

            /*
            for (int k = 0; k < arrayListJsonDashboardModel.size(); k++) {
                App.sLog(k + "===arrayListJsonDashboardModel==" + arrayListJsonDashboardModel.get(k).name);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(DashboardActivity.this, GalleryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sync) {
            if (fabRestoreBackup != null)
                fabRestoreBackup.performClick();
        } else if (id == R.id.nav_more_app) {
            //
            String url = "https://www.amazon.com/s/ref=bl_sr_mobile-apps/131-3886681-0807921?_encoding=UTF8&field-brandtextbin=wewer&node=2350149011";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_send) {
            shareApp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void shareApp()
    {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Awesome applications");
            String sAux = "\nLet me recommend you this application please download and review.\n\n";
            sAux = sAux + "https://www.amazon.com/s/ref=bl_sr_mobile-apps/131-3886681-0807921?_encoding=UTF8&field-brandtextbin=wewer&node=2350149011 \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Share application"));
        } catch(Exception e) {
            //e.toString();
        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }


    public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

        private Context mContext;   // get resource of this component
        private List<JsonDashboardModel> mList;

        public GalleryAdapter(Context mContext, List<JsonDashboardModel> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public PEWImageView mImageView;
            public TextView tvTitle;


            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (PEWImageView) itemView.findViewById(R.id.gallery_item);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

                tvTitle.setTypeface(App.getFont_Bold());

            }
        }

        @Override
        public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dashboard,
                    parent, false);
            GalleryAdapter.ViewHolder vh = new GalleryAdapter.ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, final int position) {
//        Contact item = mList.get(position);
            final JsonDashboardModel item = mList.get(position);
            holder.tvTitle.setText(item.name);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mList.get(position).id != null && mList.get(position).id.equalsIgnoreCase("14")) {
                        Intent intent = new Intent(DashboardActivity.this, GalleryActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(DashboardActivity.this, LocalWallpaperActivity.class);
                        intent.putExtra("id", mList.get(position).id);
                        intent.putExtra("title", mList.get(position).name);
                        startActivity(intent);
                    }


                }
            });

            Picasso.with(mContext)
                    .load(item.image)
                    .resize(10, 10)
                    //.fit()
                    //.centerCrop()

                    .error(R.mipmap.ic_launcher)
                    .transform(blurTransformation)
                    .into(holder.mImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(mContext)
                                    .load(item.image) // image url goes here

                                    .into(holder.mImageView);
                        }

                        @Override
                        public void onError() {
                        }
                    });

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        //    public void addAll(List<Contact> newList) {
        public void addAll(List<JsonDashboardModel> newList) {
            mList.addAll(newList);
        }

        public void add(JsonDashboardModel item) {
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



    private void setupAds()
    {
        try{
            // Initialize the Mobile Ads SDK.
            MobileAds.initialize(this, App.APP_ID);

            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    Log.i(TAG, "====onRewardedVideoAdLoaded====");
                    if (isClickEnable == false) {
                        isClickEnable = true;
                        showRewardedVideo();
                    }

                    loadRewardedVideoAd();


                }

                @Override
                public void onRewardedVideoAdOpened() {
                    Log.i(TAG, "====onRewardedVideoAdOpened====");
                }

                @Override
                public void onRewardedVideoStarted() {
                    Log.i(TAG, "====onRewardedVideoStarted====");
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    Log.i(TAG, "====onRewardedVideoAdClosed====");
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    Log.i(TAG, "====onRewarded====");
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                    Log.i(TAG, "====onRewardedVideoAdLeftApplication====");
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                    Log.i(TAG, "====onRewardedVideoAdFailedToLoad====");

                }
            });

            loadRewardedVideoAd();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




    private void loadRewardedVideoAd() {
        if (mRewardedVideoAd !=null && !mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd(App.ADS_ID_RVID, new AdRequest.Builder().build());
        }
    }

    private void showRewardedVideo() {
        Log.i(TAG, "====showRewardedVideo====");

        if (mRewardedVideoAd !=null && mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    @Override
    protected void onResume() {

        if (App.checkDbFileIsExist() == true) {
            if (realm != null && fabRestoreBackup != null) {
                fabRestoreBackup.performClick();
            }
        }

        super.onResume();
    }
}
