
package com.hct.scene.launcher2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.hct.scene.launcher.R;

public class CalendarView extends View {

    private Drawable mBg;

    private final Handler mHandler = new Handler();

    private Calendar mCalendar;

    private Context mContext;

    private static final int STYLEONE = 0;

    private static final int STYLETWO = 1;

    private int mRed = 82;

    private int mGree = 129;

    private int mBlue = 82;

    private Map<String, Drawable> mDesktopCalendarImage = new HashMap<String, Drawable>();

    private boolean mAttached;

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            invalidate();
        }
    };

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public CalendarView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void setupImage(Context context) {
        mDesktopCalendarImage.clear();

        TypedArray imageType = context.getResources().obtainTypedArray(R.array.house1calendartype);
        TypedArray imageArray = context.getResources().obtainTypedArray(R.array.house1calendar);

        for (int i = 0; i < imageType.length(); i++) {
            String type = imageType.getString(i);
            Drawable image = imageArray.getDrawable(i);
            if (type != null) {
                mDesktopCalendarImage.put(type, image);
            }
        }

        imageType.recycle();
        imageArray.recycle();
    }

    private void init(Context context) {
        mContext = context;
        setupImage(context);
        mCalendar = Calendar.getInstance();
        mBg = mDesktopCalendarImage.get("calendar_fresh");
        setBackgroundDrawable(mDesktopCalendarImage.get("calendar_fresh"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        String strYear = String.valueOf(mCalendar.get(Calendar.YEAR));

        String able = mContext.getResources().getConfiguration().locale.getCountry();
        String strMonth = null;
        if ("US".equals(able)) {
            strMonth = getMonth(mCalendar.get(Calendar.MONTH));
        } else {
            String month_format = getContext().getString(R.string.month_format);
            SimpleDateFormat sMonthFormat = new SimpleDateFormat(month_format);
            strMonth = sMonthFormat.format(new java.util.Date());
        }

        String day_format = getContext().getString(R.string.day_format);
        SimpleDateFormat sDayFormat = new SimpleDateFormat(day_format);
        String strDay = sDayFormat.format(new java.util.Date());

        paint.setARGB(255, mRed, mGree, mBlue);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setTextSize(24f);
        canvas.drawText(strYear, 15.0f, 40.0f, paint);
        canvas.drawText(strMonth, 15.0f, 65.0f, paint);
        canvas.save();

        paint.setTextSize(30f);
        paint.setStrokeWidth(1.0f);
        canvas.drawText(strDay, 70.0f, 55.0f, paint);
    }

    public void changeStyle(int style) {
        if (style == STYLEONE) {
            mRed = 82;
            mGree = 129;
            mBlue = 82;
            setBackgroundDrawable(mDesktopCalendarImage.get("calendar_fresh"));
        } else if (style == STYLETWO) {
            mRed = 236;
            mGree = 132;
            mBlue = 185;
            setBackgroundDrawable(mDesktopCalendarImage.get("calendar_princess"));
        }
        invalidate();
    }

    private String getMonth(int month) {
        String enMonth = "null";
        switch (month) {
            case 0:
                enMonth = "Jan";
                break;
            case 1:
                enMonth = "Feb";
                break;
            case 2:
                enMonth = "Mar";
                break;
            case 3:
                enMonth = "Apr";
                break;
            case 4:
                enMonth = "May";
                break;
            case 5:
                enMonth = "Jun";
                break;
            case 6:
                enMonth = "Jul";
                break;
            case 7:
                enMonth = "Aug";
                break;
            case 8:
                enMonth = "Sep";
                break;
            case 9:
                enMonth = "Oct";
                break;
            case 10:
                enMonth = "Nov";
                break;
            case 11:
                enMonth = "Dec";
                break;
        }

        return enMonth;
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction("hct.calendarview.update.action");
            mContext.registerReceiver(mIntentReceiver, filter,null,mHandler);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub

        if (mAttached) {
            mContext.unregisterReceiver(mIntentReceiver);
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int bgWidth = mBg.getIntrinsicWidth();
        int bgHeight = mBg.getIntrinsicHeight();
        setMeasuredDimension(bgWidth, bgHeight);
    }

}
