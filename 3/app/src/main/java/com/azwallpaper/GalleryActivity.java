package com.azwallpaper;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.api.UrlManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


/**

 */
public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = GalleryActivity.class.getSimpleName();

    //for the video ads
    private RewardedVideoAd mRewardedVideoAd;
    boolean isClickEnable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "----------onCreate----------");

            super.onCreate(savedInstanceState);
            //requestWindowFeature(Window.FEATURE_NO_TITLE);

            //this.getSupportActionBar().hide();
            //this.getSupportActionBar().show();
            setContentView(R.layout.activity_gallery);
            // test to fix searchview
            //handleIntent(getIntent());

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            setupAds();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

            setDisplayBanner();
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

    AdView mAdView;
    RelativeLayout rlAds;
    private void setDisplayBanner()
    {


        //String deviceid = tm.getDeviceId();

        rlAds = (RelativeLayout) findViewById(R.id.rlAds);
        rlAds.setVisibility(View.VISIBLE);
        mAdView = new AdView(GalleryActivity.this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(App.ADS_ID_BANNER);

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
    protected void onNewIntent(Intent intent) {

        Log.d(TAG, "----------onNewIntent----------");
        setIntent(intent);
        handleIntent(intent);
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

    private void handleIntent(Intent intent) {
        Log.d(TAG, "----------handleIntent----------");


        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "---handleIntent----testsearchview---Received a new search query: " + query);

//            PreferenceManager.getDefaultSharedPreferences(this)
//                    .edit()
//                    .putString(FlickrFetchr.PREF_SEARCH_QUERY, query)
//                    .commit();

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(UrlManager.PREF_SEARCH_QUERY, query)
                    .commit();

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.gallery_fragment);

            if(fragment!= null) {
                ( (GalleryFragment) fragment).refresh();
            }
        }
    }
}
