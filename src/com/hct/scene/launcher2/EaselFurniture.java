package com.hct.scene.launcher2;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.hct.scene.launcher.R;

public class EaselFurniture extends Furniture{
	private View mEaselView;
	private ArrayList<Drawable> mBgArray = new ArrayList<Drawable>();
	
	public EaselFurniture(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public EaselFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EaselFurniture(Context context) {
		super(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.easel, this);
        mEaselView = findViewById(R.id.easel);
        setupStyles();
        mEaselView.setOnClickListener(this);
	}

	@Override
	protected Rect getHighLightFrame() {
		Rect r = new Rect();
		mEaselView.getHitRect(r);
		return r;
	}

	@Override
	public void changeStyle(int style) {
        mEaselView.setOnLongClickListener(mLongClickListener);
		if(style < mBgArray.size()){
			mEaselView.setBackgroundDrawable(mBgArray.get(style));
		}
	}

	private void setupStyles(){
		mBgArray.clear();
		Resources res = getResources();

		TypedArray styles = res.obtainTypedArray(R.array.easel_bg);		

		for(int i = 0; i < styles.length(); i++){
			Drawable bg = styles.getDrawable(i);
			mBgArray.add(i, bg);
		}

		styles.recycle();
	}

	@Override
	public void onClick(View v) {
		if(mZoomMode){
			mSingleClickListener.onClick(v);
		} else {
			WeatherData.enterWeatherApp(getContext());
		}
	}
}
