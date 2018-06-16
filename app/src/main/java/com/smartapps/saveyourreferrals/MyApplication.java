package com.smartapps.saveyourreferrals;

/**
 * @author Lohith and Brian
 */
import android.app.Application;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;

import com.smartapps.saveyourreferrals.dao.DaoMaster;
import com.smartapps.saveyourreferrals.dao.DaoMaster.DevOpenHelper;
import com.smartapps.saveyourreferrals.dao.DaoSession;

public class MyApplication extends Application {

	private static MyApplication mInstance;
	private static ProgressDialog pDialog;
	public static boolean isonGraphicalLayout = true;
	private DaoMaster daoMaster;
	private String sessionCookie;
	private DaoSession daoSession;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isonGraphicalLayout = false;
		mInstance = this;
		configDB();
	}

	private void configDB() {
		try {
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "55miles",
					null);

			SQLiteDatabase database = helper.getWritableDatabase();
			daoMaster = new DaoMaster(database);
		} catch (Exception exception) {
		}

	}

	public DaoMaster getDaoMaster() {
		if (daoMaster == null) {
			configDB();
		}
		return daoMaster;
	}

	public DaoSession getDaoSession() {
		if (daoSession == null) {
			daoSession = getDaoMaster().newSession();
		}
		return daoSession;
	}

	public void setDaoMaster(DaoMaster daoMaster) {
		this.daoMaster = daoMaster;
	}

	public static MyApplication getInstance() {
		return mInstance;
	}

	public void setmInstance(MyApplication mInstance) {
		this.mInstance = mInstance;
	}

}
