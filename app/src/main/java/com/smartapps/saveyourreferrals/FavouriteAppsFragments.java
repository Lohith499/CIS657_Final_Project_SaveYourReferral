package com.smartapps.saveyourreferrals;

/**
 * @author Lohith and Brian
 */
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartapps.saveyourreferrals.dao.AppInfo;

import java.util.ArrayList;

import saveyourreferrals.lohith.com.saveyourreferrals.R;

public class FavouriteAppsFragments extends Fragment {
	private RecyclerView recyclerView;
	private LinearLayoutManager mLayoutManager;
	private RreferalAdapter rreferalAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fav_main_layout_container,
				container, false);
		adjustTheRecyclerViewAccordingly(view);
		return view;
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

	private void adjustTheRecyclerViewAccordingly(View view) {
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		int orientation;
		if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			orientation = LinearLayoutManager.VERTICAL;
		} else {
			orientation = LinearLayoutManager.HORIZONTAL;
		}
		mLayoutManager = new LinearLayoutManager(getActivity(), orientation,
				false);
		recyclerView.setLayoutManager(mLayoutManager);
		refreshTheData();
	}

	public void saveDetails(String appname, String pakckagename,
                            String base64image, String referraltext, String isfavouite,
                            String time) {
		AppInfo appInfo = new AppInfo();
		appInfo.setApp_name(appname + "");
		appInfo.setPackage_name_name(pakckagename + "");
		appInfo.setBase64_applogo(base64image + "");
		appInfo.setReferral_text(referraltext + "");
		appInfo.setIs_favourite(isfavouite + "");
		appInfo.setTime(time + "");
		MyApplication.getInstance().getDaoSession().insertOrReplace(appInfo);

		// refreshTheData();

	}

	public void refreshTheData() {
		if (recyclerView != null) {
			if (rreferalAdapter == null) {
				rreferalAdapter = new RreferalAdapter(null, true,
						getActivity(), true);
				recyclerView.setAdapter(rreferalAdapter);
			} else {
				rreferalAdapter.setList(((HomeActivity) getActivity())
						.getFavAppInfoList());
				rreferalAdapter.notifyDataSetChanged();
			}

		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshTheData();
	}

}
