package com.smartapps.saveyourreferrals;

/**
 * @author Lohith and Brian
 */
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lohith.customviews.TextDrawable;
import com.facebook.share.model.ShareLinkContent;

import com.facebook.share.widget.ShareButton;
import com.smartapps.saveyourreferrals.dao.AppInfo;
import com.smartapps.saveyourreferrals.dao.AppInfoDao.Properties;

import java.util.ArrayList;
import java.util.List;

import saveyourreferrals.lohith.com.saveyourreferrals.R;

public class RreferalAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public class StoryBean {

	}

	private List<AppInfo> mAppInfoList;
	private boolean misLatestStories;
	private Animation anim1;
	private Animation anim2;
	private Context mContext;
	private Boolean isOnlyFavourites;
	private List<AppInfo> mFavAppInfoList = new ArrayList<AppInfo>();

	public RreferalAdapter(List<StoryBean> response, boolean isLatestStories,
                           Context mContext, Boolean isOnlyFavourites) {
		this.mContext = mContext;
		this.isOnlyFavourites = isOnlyFavourites;

		if (isOnlyFavourites) {
			mAppInfoList = ((HomeActivity) mContext).getFavAppInfoList();
		} else {
			mAppInfoList = ((HomeActivity) mContext).getAppInfoList();
		}

		misLatestStories = isLatestStories;

	}

	public void setList(List<AppInfo> list) {
		mAppInfoList = list;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mAppInfoList.size() ;
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder,
			final int mPosition) {

		final int position = mPosition ;
		if (getItemViewType(mPosition) != 0) {
			final AddViewHolder vhadd = (AddViewHolder) viewHolder;


			return;
		}
		final AppInfo appInfo = mAppInfoList.get(position);
		final ViewHolder vh = (ViewHolder) viewHolder;
		vh.appNameTextview.setText(appInfo.getApp_name());
		vh.refDescTextView.setText(appInfo.getReferral_text());
		vh.deleteIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					MyApplication.getInstance().getDaoSession().delete(appInfo);
					mAppInfoList.remove(appInfo);
					notifyDataSetChanged();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		if (appInfo.getIs_favourite().equals("1")) {
			vh.favIcon.setImageResource(R.drawable.like);

		} else {
			vh.favIcon.setImageResource(R.drawable.unlike);
		}
		vh.shareIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String shareBody = "App Name:" + appInfo.getApp_name()+";Referral Description:"+appInfo.getReferral_text();
					Intent intent = shareExludingApp(mContext,
							mContext.getPackageName(), "" + shareBody);
					mContext.startActivity(intent);
					//
					// Intent sharingIntent = new Intent(
					// android.content.Intent.ACTION_SEND);
					// sharingIntent.setType("text/plain");
					// sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					// shareBody);
					// mContext.startActivity(Intent.createChooser(sharingIntent,
					// "Share"));
				} catch (Exception e) {
					showToast(mContext,
							"No applications found to share the message");
				}

			}
		});

		// Sharing the content to facebook
		ShareLinkContent content = new ShareLinkContent.Builder()
				// Setting the title that will be shared
				.setContentTitle("Planning a trip to Dubai?")
				// Setting the description that will be shared
				.setQuote("App Name:" + appInfo.getApp_name()+";Referral Description:"+appInfo.getReferral_text())
				// Setting the URL that will be shared
				//.setContentUrl(Uri.parse("https://justa128.github.io/dubai-tour-guide/landingpage/"))
				// Setting the image that will be shared
				.setContentUrl(Uri.parse("https://lh3.googleusercontent.com/ZN9n_7gXTF5nU5OmXpm-GMi7DrqKAA_y1IsILzndgslSqiJvK8Qx9npaPqH8bdRn8CPRa7pURbRyGtDeSnriiO10ZgJTSSDeUMSU9ekJ3rn76XskmbClUv1Xu2R8BNk32CUPGW1wTMz0cBBTHgwtlUPI3pwOgpGh9OAFFo5RuOz5bmgtKH8V8dN8XPkEoMo4rkjtQaT3ROlyhPUpHIDctRQpGKnVF1seQteR8MKfBWUlImH2r8RUu8NH0p-pCf72EQLPzYaj5p39qtYz6PZWdRYkLUiAvEWu8kAbcMzvu7PrbbkDCJkG83k0WRzA5EKEv7LSdjWt8IK7ohtojXX49gwX87HMXEyJaPtL_STSzbEHWSuVw9gxs66V8QBlMatjhCxZzwP6PFnY5GcqHvLBCxJ1-LUdj2585bzDsyH0mGXq7RywUBEIdkvhnMR8XE4Dv5tsLoeVOxh0pgEkrRTk4yyOF0VeaFFsLv3v0ZyTSLVBsTELEPEeyY8hJyGOtT-3HoJ7jsJwjbSfX3BRd0yRgPmhe-OtreXvcJp047IH_lX3oX4C6byusj4TDJQfSUa40_m1pibYjPqPwqqSO0XFCT57LSrk2QiWNh9TzaYt=s118-no"))
				.build();
		vh.shareButton.setShareContent(content);

		vh.favIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (appInfo.getIs_favourite().equals("0")) {
					appInfo.setIs_favourite("1");
					MyApplication.getInstance().getDaoSession()
							.insertOrReplace(appInfo);
					vh.favIcon.setImageResource(R.drawable.like);

				} else {
					appInfo.setIs_favourite("0");
					MyApplication.getInstance().getDaoSession()
							.insertOrReplace(appInfo);
					vh.favIcon.setImageResource(R.drawable.unlike);
				}

			}
		});

		try {
			String firstletter = "";
			if (appInfo.getApp_name() != null
					&& appInfo.getApp_name().length() > 0)
				firstletter = appInfo.getApp_name().charAt(0) + "";
			if (appInfo.getBase64_applogo().equals("")) {
				TextDrawable drawable1 = TextDrawable
						.builder()
						.beginConfig()
						.textColor(Color.WHITE)
						.useFont(Typeface.DEFAULT_BOLD)
						.fontSize(convertDpToPixel(22))
						.endConfig()
						.buildRound(
								"" + firstletter,
								mContext.getResources().getColor(
										R.color.content_text_color));
				vh.appLogo.setImageDrawable(drawable1);
				return;
			}
			byte[] decodedString = Base64.decode(appInfo.getBase64_applogo(),
					Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);
			vh.appLogo.setImageBitmap(decodedByte);



			// decodedByte.recycle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			vh.appLogo.setImageResource(R.drawable.ic_launcher);
		}

	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		if (arg1 == 0) {
			View view = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.list_row_story, parent, false);

			ViewHolder vh = new ViewHolder(view);

			return vh;
		} else {
			View view = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.list_row_add, parent, false);

			AddViewHolder vh = new AddViewHolder(view);

			return vh;
		}
	}

	public class AddViewHolder extends RecyclerView.ViewHolder {


		public AddViewHolder(View view) {
			super(view);

		}
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		public TextView appNameTextview;
		public TextView refDescTextView;
		public ImageView appLogo;
		public ImageView shareIV;
		public ImageView favIcon;
		public ImageView deleteIcon;
		public ShareButton shareButton;

		public ViewHolder(View view) {
			super(view);
			appNameTextview = (TextView) view.findViewById(R.id.app_name);
			refDescTextView = (TextView) view.findViewById(R.id.ref_desc);

			appLogo = (ImageView) view.findViewById(R.id.app_logo);
			favIcon = (ImageView) view.findViewById(R.id.favourite);
			deleteIcon = (ImageView) view.findViewById(R.id.delete);
			shareIV = (ImageView) view.findViewById(R.id.share);
			shareButton = (ShareButton) view.findViewById(R.id.fb_share_button);
		}

	}

	private void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public List<AppInfo> getFavAppInfoList() {
		mFavAppInfoList.clear();
		if (mAppInfoList == null)
			mAppInfoList = MyApplication.getInstance().getDaoSession()
					.getAppInfoDao().queryBuilder()
					.where(Properties.Is_favourite.eq("1")).list();
		for (int i = 0; i < mAppInfoList.size(); i++) {
			if (mAppInfoList.get(i).getIs_favourite().equals("1")) {
				mFavAppInfoList.add(mAppInfoList.get(i));
			}

		}
		return mFavAppInfoList;
	}

	public static int convertDpToPixel(float dp) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) Math.round(px);
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return  0;
	}

	public static Intent shareExludingApp(Context ctx,
                                          String packageNameToExclude, String text) {
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		List<ResolveInfo> resInfo = ctx.getPackageManager()
				.queryIntentActivities(createShareIntent(text), 0);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo info : resInfo) {
				Intent targetedShare = createShareIntent(text);

				if (!info.activityInfo.packageName
						.equalsIgnoreCase(packageNameToExclude)) {
					targetedShare.setPackage(info.activityInfo.packageName);
					targetedShareIntents.add(targetedShare);
				}
			}

			Intent chooserIntent = Intent.createChooser(
					targetedShareIntents.remove(0), "Select app to share");
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
					targetedShareIntents.toArray(new Parcelable[] {}));
			return chooserIntent;
		}
		return null;
	}

	private static Intent createShareIntent(String text) {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		if (text != null) {
			share.putExtra(Intent.EXTRA_TEXT, text);
		}
		return share;
	}
}
