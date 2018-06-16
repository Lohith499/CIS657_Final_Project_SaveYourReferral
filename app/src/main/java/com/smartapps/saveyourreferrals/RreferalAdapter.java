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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
	private int ADD_INTERVAL = 4;

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
		return mAppInfoList.size() + mAppInfoList.size() / ADD_INTERVAL;
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder,
			final int mPosition) {

		final int position = mPosition - ((mPosition + 1) / (ADD_INTERVAL + 1));
		if (getItemViewType(mPosition) != 0) {
			final AddViewHolder vhadd = (AddViewHolder) viewHolder;

			AdRequest adRequest = new AdRequest.Builder()
			// .addTestDevice(
			// "32AD717F3EDA76A9F0C839FF9588E39C")
					.build();
			vhadd.mAdView.loadAd(adRequest);
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
					String shareBody = "" + appInfo.getReferral_text();
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
		AdView mAdView;

		public AddViewHolder(View view) {
			super(view);
			mAdView = (AdView) view.findViewById(R.id.adView);
		}
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		public TextView appNameTextview;
		public TextView refDescTextView;
		public ImageView appLogo;
		public ImageView shareIV;
		public ImageView favIcon;
		public ImageView deleteIcon;

		public ViewHolder(View view) {
			super(view);
			appNameTextview = (TextView) view.findViewById(R.id.app_name);
			refDescTextView = (TextView) view.findViewById(R.id.ref_desc);

			appLogo = (ImageView) view.findViewById(R.id.app_logo);
			favIcon = (ImageView) view.findViewById(R.id.favourite);
			deleteIcon = (ImageView) view.findViewById(R.id.delete);
			shareIV = (ImageView) view.findViewById(R.id.share);

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
		if ((position + 1) % (ADD_INTERVAL + 1) == 0) {
			return 0;
		} else {
			return 0;
		}
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
