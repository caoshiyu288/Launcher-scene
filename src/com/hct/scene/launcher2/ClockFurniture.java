
package com.hct.scene.launcher2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.hct.scene.launcher.R;

public class ClockFurniture extends Furniture {

    private DesktopClock mDesktopClock;

    public ClockFurniture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ClockFurniture(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClockFurniture(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void init(Context context) {
        mDesktopClock = new DesktopClock(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
        layoutParams.topMargin = context.getResources().getDimensionPixelSize(
                R.dimen.desktop_clock_top_margin);
        addView(mDesktopClock, layoutParams);
        // mDesktopClock.setOnClickListener(this);
        mDesktopClock.setClickListener(this);
    }

    @Override
    protected Rect getHighLightFrame() {
        // TODO Auto-generated method stub
        // Rect rect = mDesktopClock.getHighLightFrame();
        Rect rect = new Rect();
        mDesktopClock.getHitRect(rect);
        return rect;
    }

    @Override
    public void changeStyle(int style) {
        // TODO Auto-generated method stub
        mDesktopClock.changeStyle(style);
        mDesktopClock.setLongClickListener(mLongClickListener);
    }

    @Override
    public boolean isAnimationObj() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onAnimation(int width) {
        // TODO Auto-generated method stub
        mDesktopClock.startAnimation();
    }

    @Override
    public void stopAnimation(int width) {
        // TODO Auto-generated method stub
        mDesktopClock.stopAnimation();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mZoomMode) {
            mSingleClickListener.onClick(v);
        } else {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setClassName("com.android.deskclock", "com.android.deskclock.DeskClock");
            getContext().startActivity(intent);
        }

    }

}
