package com.hct.scene.launcher2;

import java.util.Observable;
import java.util.Observer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import android.os.Build;

import com.hct.scene.launcher.R;

public class WeatherBoard extends LinearLayout implements Observer {
	private final String DEGREE_CELSIUS = "℃";

	private TextView mCity;
	private TextView mWeather;
	private TextView mTemperature;
	private TextView mCurTemperature;
	private ImageButton mRefreshButton;
//	private WeatherProviderService mWPS;
	private Context mContext;
	public WeatherBoard(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WeatherBoard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public WeatherBoard(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		WeatherData.getInstance().addObserver(this);
		mCity = (TextView) findViewById(R.id.weather_city);
		mWeather = (TextView) findViewById(R.id.weather);
		mTemperature = (TextView) findViewById(R.id.temperature);
		mCurTemperature = (TextView) findViewById(R.id.current_temperature);
		mRefreshButton = (ImageButton) findViewById(R.id.weather_refresh);
		mRefreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AnimationDrawable ad = (AnimationDrawable) mRefreshButton.getDrawable();
				if (ad != null) {
					ad.stop();
					ad.setOneShot(true);
					ad.start();
					updateWeatherData();
				}
			}
		});
	}

	private void displayWeatherBoard(String city, String weather, int low, int high, int d) {
		mCity.setText(city);
		mWeather.setText(weather);
		mTemperature.setText(low + DEGREE_CELSIUS + " ~ " + high + DEGREE_CELSIUS);
		mCurTemperature.setText(d + DEGREE_CELSIUS);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof WeatherData) {
			WeatherData weatherData = (WeatherData) arg0;
			displayWeatherBoard(weatherData.getCity(), weatherData.getWeatherDescription(), weatherData.getLowTemperature(),
					weatherData.getHighTemperature(), weatherData.getTemperature());
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
//		getContext().bindService(new Intent(getContext(), WeatherProviderService.class), mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
//		getContext().unbindService(mConnection);
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
//		if (visibility == VISIBLE && mWPS != null) {
//			mWPS.updateWeatherData();
//		}
		if(visibility == VISIBLE){
			updateWeatherData();
		}
	}

	/** Defines callbacks for service binding, passed to bindService() */
	/*
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			mWPS = binder.getService();
			mWPS.updateWeatherData();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mWPS = null;
		}
	};*/
	
	public void updateWeatherData() {
		Cursor c = null;
		final WeatherData weatherData = WeatherData.getInstance();
		try {
			c = mContext.getContentResolver().query(WeatherData.CONTENT_URI, null, null, null, null);
			if (c != null && c.getCount() > 0) {
				final String relcity = WeatherData.KEY_RELCITY;
				final String description = WeatherData.KEY_DESCRIPTION;
				final String temperature = WeatherData.KEY_TEMPERATURE;
				final String temperature_now = WeatherData.KEY_TEMPERATURE_NOW;
				
				c.moveToFirst();

				if (c.getString(c.getColumnIndex(relcity)) == null || c.getString(c.getColumnIndex(description)) == null
						|| c.getString(c.getColumnIndex(temperature)) == null
						|| c.getString(c.getColumnIndex(temperature_now)) == null) {
					c.close();
					return;
				}

				weatherData.setCity(c.getString(c.getColumnIndex(relcity)));
				parseAndSetTemperatrue(c.getString(c.getColumnIndex(temperature)), c.getString(c.getColumnIndex(temperature_now)));

				try {
					String description1 = c.getString(c.getColumnIndex(description));
					weatherData.setWeather(parseWeatherType(description1), description1);
				} catch (Exception e) {
					e.printStackTrace();
				}

				weatherData.dataChanged();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
	}
	private void parseAndSetTemperatrue(String t, String d) {
		int low = 0;
		int high = 0;
		int current = Integer.parseInt(d);
		final WeatherData weatherData = WeatherData.getInstance();
		if (t.matches(WeatherData.TEMERATURE_PATTERN)) {
			int start = 0;
			int end = t.indexOf("℃", start);
			String h = t.substring(start, end);
			start = t.indexOf("~") + 1;
			end = t.indexOf("℃", start);
			String l = t.substring(start, end);
			low = Integer.parseInt(l);
			high = Integer.parseInt(h);
		}

		weatherData.setTemperature(low, high, current);
	}
	
	private String parseWeatherType(String description) {
		String weatherType = null;
		if (description.contains("转")) {
			int i = description.indexOf("转");
			description = description.substring(0, i);
		}

		if (description.contains("晴")) {
			weatherType = WeatherData.WEATHER_FINE;
		} else if (description.contains("雨")) {
			weatherType = WeatherData.WEATHER_RAINY;
		} else if (description.contains("雪")) {
			weatherType = WeatherData.WEATHER_SNOWY;
		} else {
			weatherType = WeatherData.WEATHER_CLOUDY;
		}

		return weatherType;
	}
}
