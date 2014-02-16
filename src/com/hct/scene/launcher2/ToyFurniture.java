package com.hct.scene.launcher2;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class ToyFurniture extends Furniture {
	private ImageView mImageToy;
	private int mImageId = R.drawable.scene_wind_toy;

	public ToyFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		params.topMargin = 30;
		mImageToy = new ImageView(context);
		mImageToy.setImageResource(mImageId);
		addView(mImageToy, params);

		mImageToy.setOnClickListener(this);
	}

	public ToyFurniture(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ToyFurniture(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
		Rect rect = mTempRect;

		rect.left = mImageToy.getLeft();
		rect.top = mImageToy.getTop();
		rect.right = mImageToy.getRight() - 1;
		rect.bottom = mImageToy.getBottom() - 1;

		return rect;
	}

	@Override
	public void changeStyle(int style) {
		// TODO Auto-generated method stub
		if (0 == style) {
			mImageId = R.drawable.scene_wind_toy;
		} else if (1 == style) {
			mImageId = R.drawable.scene_princess_toy;
		}

		mImageToy.setImageResource(mImageId);
		mImageToy.setOnLongClickListener(mLongClickListener);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mZoomMode) {
			mSingleClickListener.onClick(v);
		}
	}
}
