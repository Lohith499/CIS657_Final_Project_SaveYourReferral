package com.example.lohith.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.smartapps.saveyourreferrals.MyApplication;

@SuppressWarnings("nls")
public class RobotoBoldTV extends TextView {

	public RobotoBoldTV(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RobotoBoldTV(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RobotoBoldTV(Context context) {
		super(context);
		init();
	}

	private void init() {
		if (MyApplication.isonGraphicalLayout)
			return;
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Roboto-Bold.ttf");
		setTypeface(tf);

	}

}
