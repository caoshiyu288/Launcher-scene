package com.hct.scene.launcher2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import java.util.Timer; 
import java.util.TimerTask; 

import com.hct.scene.launcher.R;

import android.provider.Settings;
import android.os.Handler; 
import android.os.Message; 
import android.os.PowerManager;
import android.os.RemoteException;

//import android.os.IPowerManager;
import android.os.ServiceManager;

public class WallLampFurniture extends Furniture{
    private boolean m_bScreenLight = false;
    private int m_ScreenLight = 0;	
    private ImageView img_screenlight;
    private int BrightnessStatus = 0;
    private static final int MINIMUM_BACKLIGHT = 30;//android.os.Power.BRIGHTNESS_DIM + 10;
    private static final int MAXIMUM_BACKLIGHT = 255;//android.os.Power.BRIGHTNESS_ON;
    private static final int DEFAULT_BACKLIGHT = 102;//(int) (android.os.Power.BRIGHTNESS_ON * 0.4f);

    private Context mContext; 
    private Bitmap  bitmap;
    private BitmapDrawable mDrawable;
    private int mLayoutWidth = 0;
    private int mLayoutHeight = 0;
    private int mStyle = 0;
    private static BrightnessObserver sSettingsObserver;

    private static final String TAG = "liuyang-bbkui";
    
	public WallLampFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public WallLampFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public WallLampFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
        mContext = context;
        initViews(context);
        m_ScreenLight = getBrightness(mContext);
        Log.e("BBK-UI", "m_ScreenLight="+m_ScreenLight);
        BrightnessStatus = getBrightnessStatus(mContext);
        checkObserver(context);
	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
    	Rect rect = new Rect();
    	img_screenlight.getHitRect(rect);
    	
		return rect;
	}

	
	@Override
	public void changeStyle(int style){
		mStyle = style;
		setParams(mContext, style);
		super.changeStyle(style);
	}
	
	@Override
	public boolean isAnimationObj(){
		return false;
	}
	
	@Override
	public void onAnimation(int width){
	
		super.onAnimation(width);
	}

    @Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		
        if (sSettingsObserver != null) {
            sSettingsObserver.stopObserving();
            sSettingsObserver = null;
        }
	}

    private void initViews(Context context) {
    	img_screenlight = new ImageView(context);
    	addView(img_screenlight);
        
        img_screenlight.setOnClickListener(viewClickListener);
    }
    
	private void setParams(Context context, int style){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) img_screenlight.getLayoutParams();
        if (style == 0){
            if (BrightnessStatus == 0){
            	img_screenlight.setImageResource(R.drawable.light_fresh_0);
            } else if (BrightnessStatus == 1){
            	img_screenlight.setImageResource(R.drawable.light_fresh_1);
            } else if (BrightnessStatus == 2){
            	img_screenlight.setImageResource(R.drawable.light_fresh_2);
            } else if (BrightnessStatus == 3){
            	img_screenlight.setImageResource(R.drawable.light_fresh_3);
            }
        } else if (style == 1) {
            if (BrightnessStatus == 0){
            	img_screenlight.setImageResource(R.drawable.light_princess_0);
            } else if (BrightnessStatus == 1){
            	img_screenlight.setImageResource(R.drawable.light_princess_1);
            } else if (BrightnessStatus == 2){
            	img_screenlight.setImageResource(R.drawable.light_princess_2);
            } else if (BrightnessStatus == 3){
            	img_screenlight.setImageResource(R.drawable.light_princess_3);
            }
        }
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER; //| Gravity.CENTER_HORIZONTAL;
        img_screenlight.setLayoutParams(params);
        
        img_screenlight.setOnLongClickListener(mLongClickListener);
    }

    private void changebackgroud(int status){
        if (mStyle == 0){
            if (status == 0) {
            	img_screenlight.setImageResource(R.drawable.light_fresh_0);
            } else if (status == 1){
            	img_screenlight.setImageResource(R.drawable.light_fresh_1);
            } else if (status == 2){
            	img_screenlight.setImageResource(R.drawable.light_fresh_2);
            } else if (status == 3){
            	img_screenlight.setImageResource(R.drawable.light_fresh_3);
            }
        } else if (mStyle == 1) {
            if (status == 0){
            	img_screenlight.setImageResource(R.drawable.light_princess_0);
            } else if (status == 1){
            	img_screenlight.setImageResource(R.drawable.light_princess_1);
            } else if (status == 2){
            	img_screenlight.setImageResource(R.drawable.light_princess_2);
            } else if (status == 3){
            	img_screenlight.setImageResource(R.drawable.light_princess_3);
            }
        }
    }
    
	private OnClickListener viewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mZoomMode){
				mSingleClickListener.onClick(v);
			}else{
				if (3 == BrightnessStatus) {
					BrightnessStatus = 0;
					changebackgroud(0);
					setBrightnessMode(mContext,
							Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
					setBrightness(mContext, MINIMUM_BACKLIGHT);
				} else if (2 == BrightnessStatus) {
					BrightnessStatus = 3;
					changebackgroud(3);
					setBrightnessMode(mContext,
							Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
					setBrightness(mContext, MAXIMUM_BACKLIGHT);
				} else if (1 == BrightnessStatus) {
					BrightnessStatus = 2;
					changebackgroud(2);
					setBrightnessMode(mContext,
							Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
					setBrightness(mContext, DEFAULT_BACKLIGHT);
				} else if (0 == BrightnessStatus) {
					BrightnessStatus = 1;
					changebackgroud(1);
					setBrightnessMode(mContext,
							Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
					setBrightness(mContext, MINIMUM_BACKLIGHT);
				}
			}
		}
	};
	
    private void setBrightness(Context context, int gbrightness){

        try {
//            IPowerManager power = IPowerManager.Stub.asInterface(
//                    ServiceManager.getService("power"));
        	PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (true) {
                ContentResolver cr = context.getContentResolver();
                int brightness = Settings.System.getInt(cr,Settings.System.SCREEN_BRIGHTNESS);
                int brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;

//                boolean automaticAvailable = context.getResources().getBoolean(
//                        com.android.internal.R.bool.config_automatic_brightness_available);
                SensorManager mgr = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
                if (mgr != null && mgr.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
                    brightnessMode = Settings.System.getInt(cr,Settings.System.SCREEN_BRIGHTNESS_MODE);
                }

                Log.d(TAG, "setBrightnesse-brightness="+brightness);
                Log.d(TAG, "setBrightnesse-gbrightness="+gbrightness);
                brightness = gbrightness;

                if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL) {
                    Log.i(TAG, "setBrightness--set--brightness"+brightness);
//                    power.setBacklightBrightness(brightness);
                    pm.setBacklightBrightness(brightness);
                    Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS, brightness);
                }
                Log.d(TAG, "setBrightness-end-brightnessMode ="+Settings.System.getInt(cr,
                        Settings.System.SCREEN_BRIGHTNESS_MODE)+" brightness = "+Settings.System.getInt(cr,
                                Settings.System.SCREEN_BRIGHTNESS));
            }
        } catch (Exception e) {
            Log.d(TAG, "toggleBrightness1: " + e);
        } 
     }
    
    private int getBrightness(Context context) {
        try {
//            IPowerManager power = IPowerManager.Stub.asInterface(
//                    ServiceManager.getService("power"));
            if (true) {
                int brightness = Settings.System.getInt(context.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS);
                Log.v(TAG, "getBrightness() , brightness =" + brightness);
                return brightness;
            }
        } catch (Exception e) {
        }
        return 0;
    }

    private int getBrightnessMode(Context context) {
		
        int brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
        try {
//            IPowerManager power = IPowerManager.Stub.asInterface(
//            ServiceManager.getService("power"));
            if (true) {
                ContentResolver cr = context.getContentResolver();
                //boolean automaticAvailable = context.getResources().getBoolean(
                //    com.android.internal.R.bool.config_automatic_brightness_available);
                SensorManager mgr = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
                if (mgr != null && mgr.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
                    brightnessMode = Settings.System.getInt(cr,Settings.System.SCREEN_BRIGHTNESS_MODE);
                }
                Log.d(TAG, "getBrightnessMode="+brightnessMode);
            } else {
                // Make sure we set the brightness if automatic mode isn't available
                brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
            }

        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "getBrightnessMode : " + e);
        }

        return brightnessMode;
    }

    private void setBrightnessMode(Context context, int mode) {
//        IPowerManager power = IPowerManager.Stub.asInterface(ServiceManager.getService("power"));
        if (true) {
            ContentResolver cr = context.getContentResolver();
            int brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
            boolean automaticAvailable = true;//context.getResources().getBoolean(
              //  com.android.internal.R.bool.config_automatic_brightness_available);
			if (automaticAvailable){
                brightnessMode = mode;
			}
			
            SensorManager mgr = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
            if (mgr != null && mgr.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
                // Set screen brightness mode (automatic or manual)
                Settings.System.putInt(context.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        brightnessMode);
                Log.d(TAG, "setBrightnessMode-successa"+brightnessMode);
            } else {
                // Make sure we set the brightness if automatic mode isn't available
                //brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
                Log.d(TAG, "setBrightnessMode-mgr == null");
            }
        } 
    }
	
    private int getBrightnessStatus(Context context){
    	int status = 0;
    	int brightness = getBrightness(context);
		int brightnessMode = getBrightnessMode(context);
        if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
            status = 0;
        } else if (brightness <= MINIMUM_BACKLIGHT){
            status = 1;
        } else if (brightness <= DEFAULT_BACKLIGHT){
            status = 2;
        } else {
            status = 3;
        }
		Log.v(TAG, "getBrightnessStatus="+status);
        return status;
    }

	/** Observer to watch for changes to the BRIGHTNESS setting */
	private class BrightnessObserver extends ContentObserver {

		private Context mInContext;

		BrightnessObserver(Handler handler, Context context) {
			super(handler);
			mInContext = context;
		}

		void startObserving() {
			ContentResolver resolver = mInContext.getContentResolver();
			// Listen to brightness and brightness mode
			resolver.registerContentObserver(Settings.System
					.getUriFor(Settings.System.SCREEN_BRIGHTNESS), false, this);
			resolver.registerContentObserver(Settings.System
					.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE), false, this);
		}

		void stopObserving() {
			mInContext.getContentResolver().unregisterContentObserver(this);
		}

		@Override
		public void onChange(boolean selfChange) {
			Log.d(TAG, "BrightnessObserver , onChange()");
			BrightnessStatus = getBrightnessStatus(mInContext);
			changebackgroud(BrightnessStatus);
		}
	}

    private void checkObserver(Context context) {
        if (sSettingsObserver == null) {
            sSettingsObserver = new BrightnessObserver(new Handler(),
                    context.getApplicationContext());
            sSettingsObserver.startObserving();
        }
    }
	
    
    
}
