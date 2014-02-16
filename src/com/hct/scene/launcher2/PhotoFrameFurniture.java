package com.hct.scene.launcher2;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

public class PhotoFrameFurniture extends Furniture{
	private PhotoFrameScene mPhotoFrameScene;
	public static int photoFrameId = 999;
	public PhotoFrameFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initPhotoCase();
	}

	public PhotoFrameFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initPhotoCase();
	}

	public PhotoFrameFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initPhotoCase();
	}
	private void initPhotoCase(){
		mPhotoFrameScene = new PhotoFrameScene(getContext());
		LayoutParams photoSceneParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		addView(mPhotoFrameScene, photoSceneParam);
		mPhotoFrameScene.setOnClickListener(this);
	}
	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
		Rect rect = new Rect();
		rect.left = mPhotoFrameScene.getLeft();
		rect.top = mPhotoFrameScene.getTop();
		rect.right = mPhotoFrameScene.getRight();
		rect.bottom = mPhotoFrameScene.getBottom();
		return rect;
	}

	@Override
	public void changeStyle(int style) {
		// TODO Auto-generated method stub
		super.changeStyle(style);
		mPhotoFrameScene.changeStyle(style);
		mPhotoFrameScene.setOnLongClickListener(mLongClickListener);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mZoomMode){
			mSingleClickListener.onClick(v);
		}else{
			try {
				Intent intent = new Intent();
				intent.setAction("com.hct.scene.PHOTOFRAMECONFIGURE");
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, photoFrameId);
				intent.putExtra("photoframe", 1);
				intent.putExtra("ext_photo_width", mPhotoFrameScene.getWidth());
				intent.putExtra("ext_photo_height", mPhotoFrameScene.getHeight());
				getContext().startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
