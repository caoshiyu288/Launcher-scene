
package com.hct.scene.launcher2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import com.hct.scene.launcher.R;

public class ClockHead extends FrameLayout {
    private ClockMinute mMin;

    private ClockHour mHour;

    private RotateAnimation mMinAni;

    private RotateAnimation mHourAni;

    private static final int STYLEONE = 0;

    private static final int STYLETWO = 1;

    private Map<String, Drawable> mClockImage = new HashMap<String, Drawable>();

    private final Handler mHandler = new Handler();

    private boolean mAttached;

    private Time mTime;

    private Context mContext;

    public ClockHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public ClockHead(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void setupImage(Context context) {
        mClockImage.clear();
        mContext = context;
        TypedArray imageType = context.getResources().obtainTypedArray(R.array.house1clocktype);
        TypedArray imageArray = context.getResources().obtainTypedArray(R.array.house1clock);

        for (int i = 0; i < imageType.length(); i++) {
            String type = imageType.getString(i);
            Drawable image = imageArray.getDrawable(i);
            if (type != null) {
                mClockImage.put(type, image);
            }
        }

        imageType.recycle();
        imageArray.recycle();
    }

    private void init(Context context) {
        setupImage(context);

        setBackgroundDrawable(mClockImage.get("clock_fresh"));

        mMin = new ClockMinute(getContext());
        LayoutParams minLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        addView(mMin, minLayoutParams);

        mHour = new ClockHour(getContext());
        LayoutParams hourLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        addView(mHour, hourLayoutParams);

        mMinAni = new RotateAnimation(0, 360, Animation.RELATIVE_TO_PARENT, 0.5f,
                Animation.RELATIVE_TO_PARENT, 0.5f);
        mHourAni = new RotateAnimation(0, -360, Animation.RELATIVE_TO_PARENT, 0.5f,
                Animation.RELATIVE_TO_PARENT, 0.5f);
        mMinAni.setDuration(1500);
        mHourAni.setDuration(1500);
        
        mTime = new Time();
    }

    public void startAnimation() {
        mMin.startAnimation(mMinAni);
        mHour.startAnimation(mHourAni);
    }

    public void stopAnimation() {
        mMin.clearAnimation();
        mHour.clearAnimation();
    }

    public void changeStyle(int style) {
        if (style == STYLEONE) {
            setBackgroundDrawable(mClockImage.get("clock_fresh"));
        } else if (style == STYLETWO) {
            setBackgroundDrawable(mClockImage.get("clock_princess"));
        }

        mMin.changeStyle(style);
        mHour.changeStyle(style);
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTime.setToNow();
            mMin.invalidate();
            mHour.invalidate();

            int time = mTime.hour;
            if (time == 0) {
                Intent calendarintent = new Intent();
                calendarintent.setAction("hct.calendarview.update.action");
                mContext.sendBroadcast(calendarintent);// CalendarView will
                                                       // receiver
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            mContext.registerReceiver(mIntentReceiver, filter, null, mHandler);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        if (mAttached) {
            mContext.unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }
}
