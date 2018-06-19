package com.smartapps.saveyourreferrals;

/**
 * @author Lohith and Brian
 */
import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.IntentService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.lohith.customviews.SlidingTabLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smartapps.saveyourreferrals.dao.AppInfo;
import com.smartapps.saveyourreferrals.dao.AppInfoDao.Properties;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Adding Firebase


import saveyourreferrals.lohith.com.saveyourreferrals.R;

public class HomeActivity extends FragmentActivity implements OnClickListener {
	private SlidingTabLayout mSlidingTabLayout;
	private List<AppInfo> mAppInfoList;
	private List<AppInfo> mFavAppInfoList = new ArrayList<AppInfo>();
	private List<AppInfo> temp;
	public static List<AppInfo> allHistory = new ArrayList<AppInfo>();;
	private TabsPagerAdapter adapter;
	private boolean orderbyname = true;;
	public DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private View navmenuIcon;


	private FirebaseFirestore mFirestore;


	private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;
	private Boolean phone_permission_granted;
	DatabaseReference topRef;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_content);
		intializeNavigationDrawer();
		View navmenuIcon = findViewById(R.id.nav_icon);
		navmenuIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				performNavMenuAction();

			}
		});
		phone_permission_granted = false;


		if (ContextCompat.checkSelfPermission(HomeActivity.this,
				Manifest.permission.CALL_PHONE)
				!= PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(HomeActivity.this,
					new String[]{Manifest.permission.CALL_PHONE},
					MY_PERMISSIONS_REQUEST_CALL_PHONE);
		} else {
			phone_permission_granted = true;
		}


		mFirestore = FirebaseFirestore.getInstance();



		adapter = new TabsPagerAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(
				R.color.content_text_color));
		mSlidingTabLayout.setViewPager(pager);
		pager.addOnPageChangeListener(adapter.onPageChangeListener);

		topRef = FirebaseDatabase.getInstance().getReference("history");


		try {
			checktheIntents(getIntent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		findViewById(R.id.send_feedback).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						openFeedbackScreen();

					}
				});

		findViewById(R.id.app_info).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this, HowToUseActivity.class);
				startActivity(intent);

			}
		});

	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		try {
			checktheIntents(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void intializeNavigationDrawer() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_launcher, R.string.app_name,
				R.drawable.ic_launcher) {
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	private void openFeedbackScreen() {
		Intent Email = new Intent(Intent.ACTION_SEND);
		Email.setType("text/email");
		Email.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "Lohith.gre@gmail.com" });
		PackageManager manager = getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String versionName = info.versionName;
		String Model = android.os.Build.MODEL; // Model
		// String Product = android.os.Build.PRODUCT;
		Email.putExtra(Intent.EXTRA_SUBJECT,
				"Save your Referrals Android App Feedback" + " [ "
						+ "App Version " + versionName + ","
						+ Build.VERSION.RELEASE + "," + Model + " ]");
		// Email.putExtra(Intent.EXTRA_SUBJECT,
		// "Success Stories Feedback");
		Email.putExtra(Intent.EXTRA_TEXT, "Hi ...," + "");
		startActivity(Intent.createChooser(Email, "Send Feedback:"));
	}

	private void checktheIntents(Intent myintent) {
		Intent intent = myintent;
		String action = intent.getAction();
		String type = intent.getType();
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			} else if (type.startsWith("image/")) {
				handleSendText(intent); // Handle single image being sent
			}
		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			if (type.startsWith("image/")) {
				handleSendMultipleImages(intent); // Handle multiple images
													// being sent
			}
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				AppOpsManager appOps = (AppOpsManager) this
						.getSystemService(Context.APP_OPS_SERVICE);
				int mode = appOps.checkOpNoThrow("android:get_usage_stats",
						android.os.Process.myUid(), this.getPackageName());
				boolean granted = mode == AppOpsManager.MODE_ALLOWED;
				if (!granted) {
					showAlertDailog();
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	void handleSendText(Intent intent) {
		if (!intent.hasExtra(Intent.EXTRA_TEXT)) {
			return;
		}
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (TextUtils.isEmpty(sharedText)) {
			return;
		}
		// TextView textView = (TextView) findViewById(R.id.tv);
		// textView.setText(sharedText + "");
		String topPackageName = "";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			try {
				AppOpsManager appOps = (AppOpsManager) this
						.getSystemService(Context.APP_OPS_SERVICE);
				int mode = appOps.checkOpNoThrow("android:get_usage_stats",
						android.os.Process.myUid(), this.getPackageName());
				boolean granted = mode == AppOpsManager.MODE_ALLOWED;
				if (!granted) {
					Toast.makeText(
							HomeActivity.this,
							"Please accept the permission to Save the Referal Code",
							Toast.LENGTH_SHORT).show();
					showAlertDailog();
					return;
				}
				UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
				long time = System.currentTimeMillis();
				// We get usage stats for the last 10 seconds
				List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 20, time);
				// Sort the stats by the last time used
				if (stats != null) {
					// SortedMap<Long, UsageStats> mySortedMap = new
					// TreeMap<Long,
					// UsageStats>();
					// for (UsageStats usageStats : stats) {
					// if (!usageStats.getPackageName()
					// .equalsIgnoreCase("android"))
					// mySortedMap.put(usageStats.getLastTimeUsed(),
					// usageStats);
					// }
					// if (mySortedMap != null && !mySortedMap.isEmpty()) {
					long lastUsedAppTime = 0;
					for (UsageStats usageStats : stats) {

						if (usageStats.getLastTimeUsed() > lastUsedAppTime) {
							if (usageStats.getPackageName().contains("process")
									|| usageStats.getPackageName().contains(
											"launcher")
									|| usageStats.getPackageName().equals(
											"android")
									|| usageStats.getPackageName().equals(
											getPackageName())) {
							} else {
								topPackageName = usageStats.getPackageName();
								lastUsedAppTime = usageStats.getLastTimeUsed();
							}
						}
					}

					// textView.setText(topPackageName + "");
					// final ImageView iv = (ImageView) findViewById(R.id.iv);
					Drawable icon;
					try {
						icon = getPackageManager().getApplicationIcon(
								"" + topPackageName);
						saveDetails(getAppNameFromPackage(topPackageName),
								topPackageName, encodeIcon(icon), sharedText,
								"0", "" + System.currentTimeMillis());
						// iv.setImageDrawable(icon);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				saveDetails(getAppNameFromPackage(""),
						System.currentTimeMillis() + "", "", sharedText, "0",
						"" + System.currentTimeMillis());
			}
		} else {
			try {
				ActivityManager am = (ActivityManager) this
						.getSystemService(ACTIVITY_SERVICE);
				List<ActivityManager.RecentTaskInfo> recentTasks = am
						.getRecentTasks(1, ActivityManager.RECENT_WITH_EXCLUDED);
				for (int i = 0; i < 1; i++) {
					final ActivityManager.RecentTaskInfo recentInfo = recentTasks
							.get(i);

					Intent recentintent = new Intent(recentInfo.baseIntent);
					if (recentInfo.origActivity != null) {
						recentintent.setComponent(recentInfo.origActivity);
					}

					final PackageManager pm = getPackageManager();
					final ResolveInfo resolveInfo = pm.resolveActivity(
							recentintent, 0);
					final ActivityInfo info = resolveInfo.activityInfo;
					final String title = info.loadLabel(pm).toString();

					Log.d("hello", "  " + title + " " + info.packageName);
					topPackageName = info.packageName;
					// final ImageView iv = (ImageView) findViewById(R.id.iv);
					// iv.setImageDrawable(info.loadIcon(pm));

					saveDetails(getAppNameFromPackage(topPackageName),
							topPackageName, encodeIcon(info.loadIcon(pm)),
							sharedText, "0", "" + System.currentTimeMillis());
				}
			} catch (Exception e) {
				saveDetails(getAppNameFromPackage(""),
						System.currentTimeMillis() + "", "", sharedText, "0",
						"" + System.currentTimeMillis());
			}
		}
		if (sharedText != null) {
			// Update UI to reflect text being shared
		}
	}

	void handleSendImage(Intent intent) {
		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			// Update UI to reflect image being shared
		}
	}

	void handleSendMultipleImages(Intent intent) {
		ArrayList<Uri> imageUris = intent
				.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		if (imageUris != null) {
			// Update UI to reflect multiple images being shared
		}
	}

	public String getAppNameFromPackage(String pakcagenmae) {
		try {
			final PackageManager pm = getApplicationContext()
					.getPackageManager();
			ApplicationInfo ai;
			try {
				ai = pm.getApplicationInfo(pakcagenmae, 0);
			} catch (final NameNotFoundException e) {
				ai = null;
			}
			final String applicationName = (String) (ai != null ? pm
					.getApplicationLabel(ai) : "(unknown)");
			return applicationName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "(unknown)";
		}
	}


	@Override
	public void onResume(){
		super.onResume();
		allHistory.clear();
		topRef = FirebaseDatabase.getInstance().getReference("history");
		topRef.addChildEventListener (chEvListener);
//topRef.addValueEventListener(valEvListener);
	}
	@Override
	public void onPause(){
		super.onPause();
		topRef.removeEventListener(chEvListener);
	}

	private ChildEventListener chEvListener = new ChildEventListener() {
		@Override
		public void onChildAdded(DataSnapshot dataSnapshot, String s) {
			AppInfo entry = (AppInfo)
					dataSnapshot.getValue(AppInfo.class);
			entry.setTest1(dataSnapshot.getKey());
			allHistory.add(entry);
		}
		@Override
		public void onChildChanged(DataSnapshot dataSnapshot, String s) {
		}
		@Override
		public void onChildRemoved(DataSnapshot dataSnapshot) {
			AppInfo entry = (AppInfo)
					dataSnapshot.getValue(AppInfo.class);
			List<AppInfo> newHistory = new ArrayList<AppInfo>();
			for (AppInfo t : allHistory) {
				if (!t.getTest1().equals(dataSnapshot.getKey())) {
					newHistory.add(t);
				}
			}
			allHistory = newHistory;
		}
		@Override

		public void onChildMoved(DataSnapshot dataSnapshot, String s) {
		}
		@Override
		public void onCancelled(DatabaseError databaseError) {
		}
	};

	public void saveDetails(String appname, String pakckagename,
                            String base64image, String referraltext, String isfavouite,
                            String time) {
		AppInfo appInfo = new AppInfo();
		appInfo.setApp_name(appname + "");
		pakckagename = pakckagename+""+System.currentTimeMillis();
		appInfo.setPackage_name_name(pakckagename + "");
		appInfo.setBase64_applogo(base64image + "");
		appInfo.setReferral_text(referraltext + "");



		temp = MyApplication.getInstance().getDaoSession().getAppInfoDao()
				.queryBuilder()
				.where(Properties.Package_name_name.eq(pakckagename + ""))
				.list();
		if (temp.size() > 0) {
			appInfo.setIs_favourite(temp.get(0).getIs_favourite() + "");
		} else {
			appInfo.setIs_favourite(isfavouite + "");
		}

		String stime = ""+Calendar.getInstance().getTime().toString();
		appInfo.setTime(Calendar.getInstance().getTime().toString() + "");



		MyApplication.getInstance().getDaoSession().insertOrReplace(appInfo);

		//Adding Firebase
		Map<String,String> appRef = new HashMap<>();
		appRef.put("AppName",appname);
		appRef.put("Packagename",pakckagename);
		appRef.put("RefrralText",referraltext);
		appRef.put("TimeStamp",""+stime);

		//**********************************************

		if (phone_permission_granted) {
			appRef.put("DeviceIMEI",getDeviceIMEI());
		} else {
			appRef.put("DeviceIMEI","Not able to Fetch ID");
		}
		//**************************************

		topRef.push().setValue(appRef);
		mFirestore.collection("Referrals").add(appRef).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
			@Override
			public void onSuccess(DocumentReference documentReference) {
					Toast.makeText(HomeActivity.this, "Referral Hasbeen added to FireStore", Toast.LENGTH_SHORT).show();
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					String error = e.getMessage();
					Toast.makeText(HomeActivity.this, "Error : "+error,Toast.LENGTH_SHORT).show();
			}
		});


		refreshTheData();

	}

	private void refreshTheData() {
		if (adapter == null)
			adapter = new TabsPagerAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(
				R.color.content_text_color));
		mSlidingTabLayout.setViewPager(pager);
		// pager.setOnPageChangeListener(adapter.onPageChangeListener);

	}


	public String getDeviceIMEI() {
		String deviceUniqueIdentifier = null;
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	try {
		if (null != tm) {
			deviceUniqueIdentifier = tm.getDeviceId();
		}
		if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
			deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
		}
		return deviceUniqueIdentifier;
	}
	catch (SecurityException e) {
		 e.printStackTrace();
			}
			return "Not able to get deviceID";
	}




	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay!
					phone_permission_granted = true;


				} else {
					phone_permission_granted=false;
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}
		}
	}





	public String encodeIcon(Drawable icon) {
		try {
			Drawable ic = icon;

			if (ic != null) {

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				// bitmap.compress(CompressFormat.PNG, 0, outputStream);

				BitmapDrawable bitDw = ((BitmapDrawable) ic);
				Bitmap bitmap = bitDw.getBitmap();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] bitmapByte = stream.toByteArray();

				String base64String = Base64.encodeToString(bitmapByte,
						Base64.DEFAULT);
				System.out.println("..length of image..." + bitmapByte.length);
				return base64String;
			} else {
				return "";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "";
		}

	}

	public List<AppInfo> getAppInfoList() {
		if (orderbyname)
			mAppInfoList = MyApplication.getInstance().getDaoSession()
					.getAppInfoDao().queryBuilder()
					.orderAsc(Properties.App_name).list();
		else
			mAppInfoList = MyApplication.getInstance().getDaoSession()
					.getAppInfoDao().queryBuilder().orderDesc(Properties.Time)
					.list();
		return mAppInfoList;
	}

	public void setOrderByName(boolean orderby) {
		this.orderbyname = orderby;
	}

	public List<AppInfo> getFavAppInfoList() {
		// mFavAppInfoList.clear();
		// if (mAppInfoList == null)
		mAppInfoList = MyApplication.getInstance().getDaoSession()
				.getAppInfoDao().queryBuilder()
				.where(Properties.Is_favourite.eq("1")).list();
		// for (int i = 0; i < mAppInfoList.size(); i++) {
		// if (mAppInfoList.get(i).getIs_favourite().equals("1")) {
		// mFavAppInfoList.add(mAppInfoList.get(i));
		// }
		//
		// }
		return mAppInfoList;
	}

	public void showAlertDailog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				HomeActivity.this);

		// set title
		alertDialogBuilder.setTitle("Permission Required");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"Please enable the permission to start using this app")
				.setCancelable(false)
				.setPositiveButton("Open Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								AppOpsManager appOps = (AppOpsManager) HomeActivity.this
										.getSystemService(Context.APP_OPS_SERVICE);
								int mode = appOps.checkOpNoThrow(
										"android:get_usage_stats",
										android.os.Process.myUid(),
										HomeActivity.this.getPackageName());
								boolean granted = mode == AppOpsManager.MODE_ALLOWED;
								if (!granted) {
									startActivity(new Intent(
											Settings.ACTION_USAGE_ACCESS_SETTINGS));
								}
								HomeActivity.this.finish();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing

								dialog.cancel();
								HomeActivity.this.finish();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	public void performNavMenuAction() {

		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawers();
		} else {
			mDrawerLayout.openDrawer(Gravity.START);
		}
	}
}
