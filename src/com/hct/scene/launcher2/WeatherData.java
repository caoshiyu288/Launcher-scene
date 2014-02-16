package com.hct.scene.launcher2;

import java.util.Observable;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

public class WeatherData extends Observable {
	private static WeatherData sInstance = null;
	private String mCity;
	private String mWeatherType;
	private String mWeatherDescription;
	private int mDegrees;
	private int mLowTemperature;
	private int mHighTemperature;
	
	public static final String WEATHER_FINE = "fine";
	public static final String WEATHER_CLOUDY = "cloudy";
	public static final String WEATHER_RAINY = "rainy";
	public static final String WEATHER_SNOWY = "snowy";
	
	protected static final Uri CONTENT_URI = Uri.parse("content://com.metek.zqWeather.DataContentProvider/weathers");
	protected static final String KEY_RELCITY = "relCity";
	protected static final String KEY_DESCRIPTION = "weather1";//"weatherNow";
	protected static final String KEY_TEMPERATURE = "temp1";
	protected static final String KEY_TEMPERATURE_NOW = "tempNow";
	protected static final String TEMERATURE_PATTERN = "\\-?\\d+℃~\\-?\\d+℃";
	private WeatherData() {
		super();
	}

	public static WeatherData getInstance(){
    	if(sInstance == null){
    		sInstance = new WeatherData();
    	}
    	return sInstance;
    }
	
	public void dataChanged(){
		setChanged();
		notifyObservers();
	}
	
	public void setWeather(String w, String description) throws Exception{
		if(w.equals(WEATHER_FINE)){
			mWeatherType = w;
		}else if(w.equals(WEATHER_CLOUDY)){
			mWeatherType = w;
		}else if(w.equals(WEATHER_RAINY)){
			mWeatherType = w;
		}else if(w.equals(WEATHER_SNOWY)){
			mWeatherType = w;
		}else{
			throw new Exception("unsupported weather type");
		}
		
		mWeatherDescription = description;
	}
	
	public void setCity(String city){
		mCity = city;
	}
	
	public void setTemperature(int low, int high, int d){
		mDegrees = d;
		mLowTemperature = low;
		mHighTemperature = high;
	}
	
	public String getCity() {
		return mCity;
	}
	public String getWeatherType() {
		return mWeatherType;
	}
	public String getWeatherDescription() {
		return mWeatherDescription;
	}
	public int getTemperature() {
		return mDegrees;
	}
	public int getLowTemperature() {
		return mLowTemperature;
	}
	public int getHighTemperature() {
		return mHighTemperature;
	}
	public static void enterWeatherApp(Context context) {
		try {
			context.getPackageManager().getApplicationInfo("com.metek.customzqWeather", 0);
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.setClassName("com.metek.customzqWeather", "com.metek.zqWeather.activity.WelcomeActivity");
			context.startActivity(intent);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
}
