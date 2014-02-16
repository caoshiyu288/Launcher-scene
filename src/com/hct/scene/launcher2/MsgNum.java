
package com.hct.scene.launcher2;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import com.hct.scene.launcher.R;

public class MsgNum extends View {
    private Context mContext;

    private Drawable mBackground;

    private int mMsgNum;

    private final Handler mHandler = new Handler();

    private int mNewSmsCount;

    private int mNewMmsCount;

    private boolean mMsgInitialized = false;

    private newMsgContentObserver mNewMsgContentObserver;

    private static final int STYLEONE = 0;

    private static final int STYLETWO = 1;

    private int mRed = 82;

    private int mGree = 129;

    private int mBlue = 82;

    private Map<String, Drawable> mCallMsgImage = new HashMap<String, Drawable>();
    
    public MsgNum(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public MsgNum(Context context) {
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
        //mBackground = context.getResources().getDrawable(mBgId);
        mBackground = mCallMsgImage.get("right_fresh");
        mContext = context;
        mNewMsgContentObserver = new newMsgContentObserver(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int bgWidth = mBackground.getIntrinsicWidth();
        int bgHeight = mBackground.getIntrinsicHeight();
        setMeasuredDimension(bgWidth, bgHeight);
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        startMsgListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        unRegisterMsgListener();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        mMsgNum = mNewSmsCount + mNewMmsCount;
        String unreadmsg = null;

        Paint paint = new Paint();
        paint.setARGB(255, mRed, mGree, mBlue);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        if (mMsgNum > 0) {
            mBackground.setBounds(0, 0, mBackground.getIntrinsicWidth(),
                    mBackground.getIntrinsicHeight());
            mBackground.draw(canvas);
            // setBackgroundResource(R.drawable.right_wind);
            if (mMsgNum == 1) {
                unreadmsg = mContext.getResources().getString(R.string.str_unread_msg);
            } else {
                unreadmsg = mContext.getResources().getString(R.string.str_unread_msgs);
            }
            paint.setTextSize(24f);
            if(mMsgNum >= 10){
                canvas.drawText(String.valueOf(mMsgNum), 15.0f, 35.0f, paint); 
            }else{
                canvas.drawText(String.valueOf(mMsgNum), 20.0f, 35.0f, paint);
            }
            paint.setTextSize(14f);
            canvas.drawText(unreadmsg, 45.0f, 35.0f, paint);
        }
    }

    public void changeStyle(int style) {
        if (style == STYLEONE) {
            mRed = 82;
            mGree = 129;
            mBlue = 82;
            mBackground = mCallMsgImage.get("right_fresh");
        } else if (style == STYLETWO) {
            mRed = 236;
            mGree = 132;
            mBlue = 185;
            mBackground = mCallMsgImage.get("right_princess");
        }
        //mBackground = getContext().getResources().getDrawable(mBgId);
        invalidate();
    }

    private void findNewSmsCount() {
        Cursor csr = null;
        try {
            csr = mContext.getContentResolver().query(Uri.parse("content://sms"), null,
                    "type = 1 and read = 0", null, null);
            mNewSmsCount = csr.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            csr.close();
        }

    }

    private void findNewMmsCount() {
        Cursor csr = null;
        try {
            csr = mContext.getContentResolver().query(Uri.parse("content://mms/inbox"), null,
                    "read = 0 and ct_t IS NOT NULL", null, null);
            mNewMmsCount = csr.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            csr.close();
        }
    }

    public class newMsgContentObserver extends ContentObserver {
        public newMsgContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            findNewMmsCount();
            findNewSmsCount();
            postInvalidate();
        }
    }

    private Runnable mInitMsg = new Runnable() {
        @Override
        public void run() {
            startMsgListener();
        }
    };

    private void startMsgListener() {
        if (!mMsgInitialized
                && mContext.getContentResolver().acquireContentProviderClient(
                        Uri.parse("content://mms-sms/")) != null) {
            mContext.getContentResolver().registerContentObserver(Uri.parse("content://mms-sms/"),
                    true, mNewMsgContentObserver);
            mMsgInitialized = true;
        }

        if (mMsgInitialized) {
            findNewMmsCount();
            findNewSmsCount();
        } else {
            mHandler.postDelayed(mInitMsg, 1000);
        }
    }

    private void unRegisterMsgListener() {
        mContext.getContentResolver().unregisterContentObserver(mNewMsgContentObserver);
    }

    public int getUnreadMsg() {
        return mNewSmsCount + mNewMmsCount;
    }
}
