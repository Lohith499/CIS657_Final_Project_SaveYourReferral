package com.smartapps.saveyourreferrals;

/**
 * @author Lohith and Brian
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	private AllAppsFragments fragment1;
	private FavouriteAppsFragments fragment2;

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			fragment1 = new AllAppsFragments();
			return fragment1;
		}
		fragment2 = new FavouriteAppsFragments();
		return fragment2;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (position == 0)
			return "All Apps";
		else {
			return "Favourites";
		}
	}

	OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 0) {
				fragment1.refreshTheData();
			} else {
				fragment2.refreshTheData();
			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};
}