package com.smartapps.saveyourreferrals;

/**
 * @author Lohith and Brian
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import saveyourreferrals.lohith.com.saveyourreferrals.R;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							startActivity(new Intent(getApplicationContext(),
									HomeActivity.class));
							finish();
						}
					});

				}
			}
		};
		splashTread.start();

		// CountDownTimer Counter1 = new CountDownTimer(2000, 1000) {
		// public void onTick(long millisUntilFinished) {
		// }
		//
		// public void onFinish() {
		// finish();
		// Intent home_Intent = new Intent();
		// home_Intent.setClass(SplashActivity.this, MainActivity.class);
		// startActivity(home_Intent);
		// }
		//
		// };
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
