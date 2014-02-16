package com.hct.scene.launcher2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class WindowFurniture extends Furniture implements Observer{
	private View mWindowView;
	private View mWindowCurtain;
	private ArrayList<Drawable> mCurtainArray = new ArrayList<Drawable>();
	private String mWeather;
	private ImageView mScene;
	private Map<String, Drawable> mSceneBackgrounds = new HashMap<String, Drawable>();
	private Map<String, Drawable> mSceneAnimations = new HashMap<String, Drawable>();

	public WindowFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public WindowFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public WindowFurniture(Context context) {
		super(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.weather_window, this);
        mScene = (ImageView)findViewById(R.id.weather_scene);
        mWindowCurtain = findViewById(R.id.window_and_curtain);
        setupStyles();
        setupSceneBackgrounds();
		setupSceneAnimations();
		WeatherData weatherData = WeatherData.getInstance();
		weatherData.addObserver(this);
		mWeather = weatherData.getWeatherType();
		mWindowView = findViewById(R.id.weather_window);
		mWindowView.setOnClickListener(this);
	}

	@Override
	protected Rect getHighLightFrame() {
		Rect r = new Rect();
		mWindowView.getHitRect(r);
		return r;
	}

	@Override
	public boolean isAnimationObj() {
		return true;
	}

	@Override
	public void onAnimation(int width) {
		super.onAnimation(width);
		displayWeatherScene();
	}

	@Override
	public void stopAnimation(int width) {
		super.stopAnimation(width);
		AnimationDrawable ad = (AnimationDrawable)mScene.getDrawable();
		if(ad != null){
			ad.stop();
		}
	}

	@Override
	public void changeStyle(int style) {
		mWindowView.setOnLongClickListener(mLongClickListener);
		if(style < mCurtainArray.size()){
			mWindowCurtain.setBackgroundDrawable(mCurtainArray.get(style));
		}
	}

	private void setupStyles(){
		mCurtainArray.clear();

		Resources res = getResources();
		TypedArray styles = res.obtainTypedArray(R.array.window_bg);		

		for(int i = 0; i < styles.length(); i++){
			Drawable bg = styles.getDrawable(i);
			mCurtainArray.add(i, bg);
		}

		styles.recycle();
	}

	private void setupSceneBackgrounds(){
		mSceneBackgrounds.clear();
		Resources res = getResources();

		TypedArray types = res.obtainTypedArray(R.array.all_weathers);
		TypedArray backgrounds = res.obtainTypedArray(R.array.scene_backgrounds);

		for(int i = 0; i < types.length(); i++){
			String type = types.getString(i);
			Drawable bg = backgrounds.getDrawable(i);
			if(type != null){
				mSceneBackgrounds.put(type, bg);
			}
		}

		types.recycle();
		backgrounds.recycle();
	}
	
	private void setupSceneAnimations(){
		mSceneAnimations.clear();
		Resources res = getResources();

		TypedArray types = res.obtainTypedArray(R.array.all_weathers);
		TypedArray animations = res.obtainTypedArray(R.array.scene_animations);

		for(int i = 0; i < types.length(); i++){
			String type = types.getString(i);
			Drawable anim = animations.getDrawable(i);
			if(type != null){
				mSceneAnimations.put(type, anim);
			}
		}

		types.recycle();
		animations.recycle();
	}
	
	private void displayWeatherScene(){
		//get scene animation in hashmap then display
		Log.d("WindowFurniture","displayWeatherScene: "+mWeather+", "+mSceneAnimations.get(mWeather));
		mScene.setBackgroundDrawable(mSceneBackgrounds.get(mWeather));
		mScene.setImageDrawable(mSceneAnimations.get(mWeather));
		AnimationDrawable ad = (AnimationDrawable)mScene.getDrawable();
		if(ad != null){
			ad.stop();
			ad.start();
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg0 instanceof WeatherData){
			WeatherData weatherData = (WeatherData)arg0;
			mWeather = weatherData.getWeatherType();
			displayWeatherScene();
		}
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
