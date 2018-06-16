package com.smartapps.saveyourreferrals;

/**
 * @author Lohith and Brian
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartapps.saveyourreferrals.dao.AppInfo;
import com.smartapps.saveyourreferrals.dao.AppInfoDao.Properties;

import java.util.List;

import saveyourreferrals.lohith.com.saveyourreferrals.R;

public class AddManualReferralActivity extends Activity {
	EditText _appName;
	EditText _referralDesc;
	TextView _submitReferral;
	private ProgressDialog progressDialog;
	private List<AppInfo> temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_referral);
		_appName = (EditText) findViewById(R.id.input_email);
		_referralDesc = (EditText) findViewById(R.id.input_password);
		_submitReferral = (TextView) findViewById(R.id.btn_Submit);
		_submitReferral.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveReferralManually();
			}
		});
		findViewById(R.id.nav_icon).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

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

	public void saveReferralManually() {

		if (!validate()) {
			return;
		}
		String email = _appName.getText().toString();
		String password = _referralDesc.getText().toString();
		saveDetails(email, System.currentTimeMillis() + "", "", password, "0",
				System.currentTimeMillis() + "");
	}

	public boolean validate() {
		boolean valid = true;
		String appName = _appName.getText().toString();
		String refDescription = _referralDesc.getText().toString();

		if (appName.isEmpty()) {
			showToast(AddManualReferralActivity.this, "Enter valid App name");
			valid = false;
		} else {
			_appName.setError(null);
		}

		if (refDescription.isEmpty()) {
			showToast(AddManualReferralActivity.this, "Enter valid Description");
			valid = false;
		} else {
			_referralDesc.setError(null);
		}

		return valid;
	}

	private void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}



	public void saveDetails(String appname, String pakckagename,
                            String base64image, String referraltext, String isfavouite,
                            String time) {
		AppInfo appInfo = new AppInfo();
		appInfo.setApp_name(appname + "");
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
		appInfo.setTime(time + "");

		MyApplication.getInstance().getDaoSession().insertOrReplace(appInfo);
		finish();

	}

}
