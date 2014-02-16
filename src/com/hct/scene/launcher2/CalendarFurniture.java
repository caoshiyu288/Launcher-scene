
package com.hct.scene.launcher2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

public class CalendarFurniture extends Furniture {

    private DesktopCalendar mDesktopCalendar;

    public CalendarFurniture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public CalendarFurniture(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public CalendarFurniture(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        mDesktopCalendar = new DesktopCalendar(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
        // layoutParams.leftMargin = 40;
        layoutParams.topMargin = 20;
        addView(mDesktopCalendar, layoutParams);
        mDesktopCalendar.setClickListener(this);
    }

    @Override
    protected Rect getHighLightFrame() {
        // TODO Auto-generated method stub
        Rect rect = new Rect();
        mDesktopCalendar.getHitRect(rect);
        return rect;
    }

    @Override
    public void changeStyle(int style) {
        // TODO Auto-generated method stub
        mDesktopCalendar.changeStyle(style);
        mDesktopCalendar.setLongClockListener(mLongClickListener);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mZoomMode) {
            mSingleClickListener.onClick(v);
        } else if(v == mDesktopCalendar.getCalendar()){
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setClassName("com.android.calendar", "com.android.calendar.AllInOneActivity");
            getContext().startActivity(intent);
        }
    }
}
