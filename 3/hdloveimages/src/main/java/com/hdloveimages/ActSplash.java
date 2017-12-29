package com.hdloveimages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hdloveimages.animation.ActAnimationViews;

public class ActSplash extends Activity
{
String TAG  = "ActSplash";
int TIME = 3000;
	private FirebaseAnalytics mFirebaseAnalytics;
	private FirebaseAuth mFirebaseAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		try {

			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

			setContentView(R.layout.activity_splash);



			// Initialize FirebaseAuth
			mFirebaseAuth = FirebaseAuth.getInstance();
			// Obtain the FirebaseAnalytics instance.
			mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


			setSendDataAnalytics();
			setSendCrashData();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					setExpireApp();
				}
			},TIME);



		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}


	}

	@SuppressLint("SimpleDateFormat")
	private void setExpireApp()
	{



		int year;
		int month;
		int day;

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		//month=month+1;
		String chDtC=year+"-"+(month+1)+"-"+day;

		if(month<10)
		{
			if(day<10)
			{
				chDtC=year+"-0"+(month+1)+"-0"+day;
			}
			else
			{
				chDtC=year+"-0"+(month+1)+"-"+day;
			}
		}
		else
		{
			if(day<10)
			{
				chDtC=year+"-"+(month+1)+"-0"+day;
			}

		}


		DateFormat formatter_dest ; 
		Date date_dest = null ; 
		formatter_dest = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date_dest = formatter_dest.parse(chDtC);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		DateFormat formatter_source ; 
		Date date_source = null;

		formatter_source= new SimpleDateFormat("yyyy-MM-dd");

		try
		{
			//End Date
			date_source = formatter_source.parse("2020-07-01");

		} 

		catch (ParseException e1)

		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(!date_source.after(date_dest))
		{
			Toast.makeText(getApplicationContext(), "YouPlay application has been expired. \nPlease contact application developer.", Toast.LENGTH_SHORT).show();
			String e="com.hdloveimages";
			finish();
			Intent intent = new Intent(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:" + e));
			startActivity(intent);
		}
		else
		{

		    new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					finish();
					//Intent intent = new  Intent(ActSplash.this,ActNotification .class);
					Intent intent = new  Intent(ActSplash.this,ActAnimationViews.class);
					startActivity(intent);
				}
			},1000);
		}

	}




	// Get Device token - Function
	private void getDeviceToken() {
		try {

			String refreshedToken = FirebaseInstanceId.getInstance().getToken();
			Log.d(TAG, "Refreshed token: " + refreshedToken);
			// [START subscribe_topics]
			//FirebaseMessaging.getInstance().subscribeToTopic("news");
			Log.d(TAG, "Subscribed to news topic");
			// [END subscribe_topics]
			Log.d(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());

			if (refreshedToken != null && refreshedToken.length() > 5) {
				//App.sharePrefrences.setPref(PreferencesKeys.strDeviceId, refreshedToken);

				TIME = 2000;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	private void setSendDataAnalytics() {
		try {
			Log.d(TAG, "---FirebaseAnalytics.Event.SELECT_CONTENT------");
			Bundle bundle = new Bundle();
			bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "111");
			bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Prince");
			bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
			mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
			Log.d(TAG, "---FirebaseAnalytics.Event.SHARE------");
			Bundle bundle2 = new Bundle();
			bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "prince article");
			bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, "p786");
			mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);
		} catch (Exception e) {
			Log.d(TAG, "---setSendDataAnalytics-Error send analytics--");
			e.printStackTrace();
		}
	}

	private void setSendCrashData() {
		Log.d(TAG, "---setSendCrashData--");
		//FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
		//FirebaseCrash.report(new Exception("My first Android non-fatal error"));
		//FirebaseCrash.log("Activity created");

		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id_a311");
		bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name_prince");
		bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
		mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
	}


}
