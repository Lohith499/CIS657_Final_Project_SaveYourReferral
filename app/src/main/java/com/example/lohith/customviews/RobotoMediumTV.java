package com.example.lohith.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.smartapps.saveyourreferrals.MyApplication;

@SuppressWarnings("nls")
public class RobotoMediumTV extends TextView {

	public RobotoMediumTV(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RobotoMediumTV(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RobotoMediumTV(Context context) {
		super(context);
		init();
	}

	private void init() {
		if (MyApplication.isonGraphicalLayout)
			return;
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Roboto-Medium.ttf");
		setTypeface(tf);

	}

}
