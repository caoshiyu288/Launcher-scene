
package com.hct.scene.launcher2;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.CallLog;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import com.hct.scene.launcher.R;

public class CallNum extends View {
    private Drawable mBg;

    private int mMissedCallCount;

    private Context mContext;

    private boolean mCallLogInitialized = false;

    private final Handler mHandler = new Handler();

    private newCalllogContentObserver mNewCalllogContentObserver;

    private static final int STYLEONE = 0;

    private static final int STYLETWO = 1;

    private int mRed = 82;

    private int mGree = 129;

    private int mBlue = 82;
    
    private Map<String, Drawable> mCallMsgImage = new HashMap<String, Drawable>();
    
    public CallNum(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public CallNum(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }
    
    private void setupImage(Context context){
        mCallMsgImage.clear();

        TypedArray imageType = context.getResources().obtainTypedArray(R.array.house1callmsgtype);
        TypedArray imageArray = context.getResources().obtainTypedArray(R.array.house1callmsg);

        for(int i=0;i<imageType.length();i++){
            String type = imageType.getString(i);
            Drawable image = imageArray.getDrawable(i);
            if(type != null){
                mCallMsgImage.put(type, image);
            }   
        }

        imageType.recycle();
        imageArray.recycle();
    }
    
    private void init(Context context) {
        setupImage(context);
        //mBg = context.getResources().getDrawable(mBgId);
        mBg = mCallMsgImage.get("left_fresh");
        mContext = context;
        mNewCalllogContentObserver = new newCalllogContentObserver(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        String missCall = null;

        Paint paint = new Paint();
        paint.setARGB(255, mRed, mGree, mBlue);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        if (mMissedCallCount > 0) {
            mBg.setBounds(0, 0, mBg.getIntrinsicWidth(), mBg.getIntrinsicHeight());
            mBg.draw(canvas);
            // setBackgroundResource(R.drawable.left_wind);
            if (mMissedCallCount == 1) {
                missCall = mContext.getResources().getString(R.string.str_missed_call);
            } else {
                missCall = mContext.getResources().getString(R.string.str_missed_calls);
            }
            paint.setTextSize(24f);
            if(mMissedCallCount >= 10){
                canvas.drawText(String.valueOf(mMissedCallCount), 20.0f, 35.0f, paint);
            } else {
                canvas.drawText(String.valueOf(mMissedCallCount), 30.0f, 35.0f, paint);
            }
            paint.setTextSize(14f);
            canvas.drawText(missCall, 50.0f, 35.0f, paint);
        }
    }

    public void changeStyle(int style) {
        if (style == STYLEONE) {
            mRed = 82;
            mGree = 129;
            mBlue = 82;
            mBg = mCallMsgImage.get("left_fresh");
        } else if (style == STYLETWO) {
            mRed = 236;
            mGree = 132;
            mBlue = 185;
            mBg = mCallMsgImage.get("left_princess");
        }
        
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        setMeasuredDimension(mBg.getIntrinsicWidth(), mBg.getIntrinsicHeight());
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        startMissedCallListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        unRegisterMissedCallListener();
    }

    private void findNewMissedCallCount() {
        Cursor csr = null;
        try {
            csr = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                    "type = 3 and new = 1", null, null);
            mMissedCallCount = csr.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            csr.close();
        }

    }

    public class newCalllogContentObserver extends ContentObserver {
        public newCalllogContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {

            findNewMissedCallCount();
            postInvalidate();
        }
    }

    private Runnable mInitCallLog = new Runnable() {
        @Override
        public void run() {
            startMissedCallListener();
        }
    };

    private void startMissedCallListener() {
        if (!mCallLogInitialized
                && mContext.getContentResolver().acquireContentProviderClient(
                        CallLog.Calls.CONTENT_URI) != null) {
            mContext.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true,
                    mNewCalllogContentObserver);

            mCallLogInitialized = true;
        }

        if (mCallLogInitialized) {
            findNewMissedCallCount();
        } else {
            mHandler.postDelayed(mInitCallLog, 1000);
        }
    }

    private void unRegisterMissedCallListener() {
        mContext.getContentResolver().unregisterContentObserver(mNewCalllogContentObserver);
    }

    public int getMissCallNum() {
        return mMissedCallCount;
    }
}
