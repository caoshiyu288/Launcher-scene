
package com.hct.scene.launcher2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.hct.scene.launcher.R;

public class ClockMinute extends View {
    private Context mContext;

    private Drawable mBackground;

    private Drawable mMinute;

    private Time mTime;

    private int bgWidth;

    private int bgHeight;

    private final Handler mHandler = new Handler();

    private static final int STYLEONE = 0;

    private static final int STYLETWO = 1;
    
    private Map<String, Drawable> mClockImage = new HashMap<String, Drawable>();
    

    public ClockMinute(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        init();
    }

    public ClockMinute(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        init();
    }
    
    private void setupImage(Context context){
        mClockImage.clear();

        TypedArray imageType = context.getResources().obtainTypedArray(R.array.house1clocktype);
        TypedArray imageArray = context.getResources().obtainTypedArray(R.array.house1clock);

        for(int i=0;i<imageType.length();i++){
            String type = imageType.getString(i);
            Drawable image = imageArray.getDrawable(i);
            if(type != null){
                mClockImage.put(type, image);
            }   
        }

        imageType.recycle();
        imageArray.recycle();
    }
    
    private void init() {
        setupImage(mContext);
        mBackground = mClockImage.get("clock_fresh");
        bgWidth = mBackground.getIntrinsicWidth();
        bgHeight = mBackground.getIntrinsicHeight();
        mMinute = mClockImage.get("minute_fresh");
        mTime = new Time();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        setMeasuredDimension(bgWidth, bgHeight);
    }

    void drawClockPointer(Canvas canvas) {
        int timeMinute = mTime.minute;
        canvas.save();
        // draw minute hand
        canvas.rotate(timeMinute / 60.0f * 360.0f, bgWidth / 2, bgHeight / 2);
        final Drawable MinuteHand = mMinute;
        int mw = MinuteHand.getIntrinsicWidth();
        int mh = MinuteHand.getIntrinsicHeight();
        MinuteHand.setBounds(bgWidth / 2 - mw / 2, bgHeight / 2 - mh / 2, bgWidth / 2 + mw / 2,
                bgHeight / 2 + mh / 2);
        MinuteHand.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        mTime.setToNow();
        drawClockPointer(canvas);
    }

    public void changeStyle(int style) {
        if(style == STYLEONE){
            mMinute = mClockImage.get("minute_fresh");
        }else if(style == STYLETWO){
            mMinute = mClockImage.get("minute_princess");
        }
        invalidate();
    }
}
