
package com.hct.scene.launcher2;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class DesktopCalendar extends FrameLayout {
    private CalendarView mCalendarView;

    private static final int STYLEONE = 0;

    private static final int STYLETWO = 1;

    private ImageView mImageView;
    
    private Map<String, Drawable> mDesktopCalendarImage = new HashMap<String, Drawable>();
    
    public DesktopCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public DesktopCalendar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void setupImage(Context context){
        mDesktopCalendarImage.clear();

        TypedArray imageType = context.getResources().obtainTypedArray(R.array.house1calendartype);
        TypedArray imageArray = context.getResources().obtainTypedArray(R.array.house1calendar);

        for(int i=0;i<imageType.length();i++){
            String type = imageType.getString(i);
            Drawable image = imageArray.getDrawable(i);
            if(type != null){
                mDesktopCalendarImage.put(type, image);
            }   
        }

        imageType.recycle();
        imageArray.recycle();
    }
    
    private void init(Context context) {
        setupImage(context);
        
        mImageView = new ImageView(context);
        mImageView.setImageDrawable(mDesktopCalendarImage.get("calendarbg_fresh"));
        addView(mImageView);

        mCalendarView = new CalendarView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL);
        layoutParams.leftMargin = 20;
        addView(mCalendarView, layoutParams);

    }

    public void changeStyle(int style) {
        if (style == STYLEONE) {
            mImageView.setImageDrawable(mDesktopCalendarImage.get("calendarbg_fresh"));
        } else if (style == STYLETWO) {
            mImageView.setImageDrawable(mDesktopCalendarImage.get("calendarbg_princess"));
        }
        mCalendarView.changeStyle(style);
    }

    public void setClickListener(View.OnClickListener listener) {
        mCalendarView.setOnClickListener(listener);
        mImageView.setOnClickListener(listener);
    }

    public void setLongClockListener(View.OnLongClickListener listener) {
        mImageView.setOnLongClickListener(listener);
        mCalendarView.setOnLongClickListener(listener);
    }
    
    public View getCalendar(){
        return mCalendarView;
    }
}
