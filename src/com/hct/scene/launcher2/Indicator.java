package com.hct.scene.launcher2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hct.scene.launcher.R;

// create by hct lijunyi, 20130129

public class Indicator extends LinearLayout {
	private Context mContext;
	List<TextView> mIndicator;

	private final int PAGE_NULL = 0;
	private int mNumberMax = PAGE_NULL;

	private final int mWidth = 20;
	private final int mHeight = 20;

	private final int mImageNormal = R.drawable.indicator_normal;
	private final int mImageSelect = R.drawable.indicator_select;

	public Indicator(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		this(context, attrs, 0);
	}

	public Indicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

		initView(context);
	}

	private void initView(Context context) {
		mContext = context;

		setGravity(Gravity.CENTER);
		mIndicator = new ArrayList<TextView>();
	}

	private TextView createDot() {
		TextView dot = new TextView(mContext);
		dot.setBackgroundResource(mImageNormal);
		dot.setGravity(Gravity.CENTER);

		return dot;
	}

	/* base from 1 -> size */
	public void changeState(int index, int max) {
		if (index > max) {
			return;
		}

		if (max > mNumberMax) {
			if (PAGE_NULL == mNumberMax) {
				for (int i = 0; i < max; i++) {
					mIndicator.add(createDot());

					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mWidth, mHeight);
					addView(mIndicator.get(i), params);
				}
			} else {
				int step = max - mNumberMax;

				for (int i = 0; i < step; i++) {
					mIndicator.add(createDot());

					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mWidth, mHeight);
					addView(mIndicator.get(mNumberMax + i), params);
				}
			}
		} else if (max < mNumberMax) {
			int step = mNumberMax - max;

			for (int i = 0; i < step; i++) {
				removeView(mIndicator.remove(mNumberMax - i - 1));
			}
		}

		resetState(max);

		mIndicator.get(index - 1).setBackgroundResource(mImageSelect);
	}

	private void resetState(int max) {
		mNumberMax = max;

		for (int i = 0; i < mNumberMax; i++) {
			mIndicator.get(i).setBackgroundResource(mImageNormal);
		}
	}
}
