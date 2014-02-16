
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

public class DesktopCallAndMsg extends FrameLayout {
    private CallNum mCallNumView;

    private MsgNum mMsgNumView;
    
    private static final int STYLEONE = 0;

    private static final int STYLETWO = 1;

    private ImageView mImageView;

    private ImageView mCallView;
    
    private ImageView mMsgView;
    
    private ImageView mForcerView;
    
    private Map<String, Drawable> mCallMsgImage = new HashMap<String, Drawable>();
    
    public DesktopCallAndMsg(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public DesktopCallAndMsg(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public DesktopCallAndMsg(Context context) {
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
        
        mImageView = new ImageView(context);
        //mImageView.setImageResource(R.drawable.house1_callmsg_r_callandmsg);
        mImageView.setImageDrawable(mCallMsgImage.get("callmsg_fresh"));
        addView(mImageView);

        mForcerView = new ImageView(context);
        //mForcerView.setImageResource(R.drawable.house1_callmsg_r_forcer);
        mForcerView.setImageDrawable(mCallMsgImage.get("forcer_fresh"));
        LayoutParams forcerParams = new LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
        forcerParams.leftMargin = 48;
        forcerParams.topMargin = 150;
        addView(mForcerView,forcerParams);
        
        mCallNumView = new CallNum(context);
        LayoutParams callNumParams = new LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
        addView(mCallNumView, callNumParams);
        
        mCallView = new ImageView(context);
        //mCallView.setImageResource(R.drawable.house1_callmsg_r_call);
        mCallView.setImageDrawable(mCallMsgImage.get("call_fresh"));
        LayoutParams callParams = new LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
        callParams.leftMargin = 95;
        callParams.topMargin = 60;
        addView(mCallView,callParams);
        
        mMsgView = new ImageView(context);
        //mMsgView.setImageResource(R.drawable.house1_callmsg_r_msg);
        mMsgView.setImageDrawable(mCallMsgImage.get("msg_fresh"));
        LayoutParams MsgParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.RIGHT);
        MsgParams.topMargin = 150;
        MsgParams.rightMargin = 150;
        addView(mMsgView,MsgParams);

        mMsgNumView = new MsgNum(context);
        LayoutParams MsgNumParams = new LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.RIGHT);
        MsgNumParams.topMargin = 100;
        MsgNumParams.rightMargin = 15;
        addView(mMsgNumView, MsgNumParams);
    }

    public void changeStyle(int style) {
        if (style == STYLEONE) {
            //mForcerView.setImageResource(R.drawable.house1_callmsg_r_forcer);
            //mCallView.setImageResource(R.drawable.house1_callmsg_r_call);
            //mMsgView.setImageResource(R.drawable.house1_callmsg_r_msg);
            mForcerView.setImageDrawable(mCallMsgImage.get("forcer_fresh"));
            mCallView.setImageDrawable(mCallMsgImage.get("call_fresh"));
            mMsgView.setImageDrawable(mCallMsgImage.get("msg_fresh"));
        } else if (style == STYLETWO) {
           // mForcerView.setImageResource(R.drawable.house1_callmsg_p_forcer);
            //mCallView.setImageResource(R.drawable.house1_callmsg_p_call);
            //mMsgView.setImageResource(R.drawable.house1_callmsg_p_msg);
            mForcerView.setImageDrawable(mCallMsgImage.get("forcer_princess"));
            mCallView.setImageDrawable(mCallMsgImage.get("call_princess"));
            mMsgView.setImageDrawable(mCallMsgImage.get("msg_princess"));
        }
        mCallNumView.changeStyle(style);
        mMsgNumView.changeStyle(style);
    }

    public void setClickListener(View.OnClickListener listener) {
        mCallNumView.setOnClickListener(listener);
        mCallView.setOnClickListener(listener);
        mMsgView.setOnClickListener(listener);
        mMsgNumView.setOnClickListener(listener);
        mForcerView.setOnClickListener(listener);
    }

    public void setLongClickListener(View.OnLongClickListener listener) {
        mCallNumView.setOnLongClickListener(listener);
        mCallView.setOnLongClickListener(listener);
        mMsgView.setOnLongClickListener(listener);
        mMsgNumView.setOnLongClickListener(listener);
        mForcerView.setOnLongClickListener(listener);
    }
    
    public ImageView getCallView() {
        return mCallView;
    }

    public ImageView getMsgView() {
        return mMsgView;
    }  

    public CallNum getCallNumView() {
        return mCallNumView;
    }

    public MsgNum getMsgNumView() {
        return mMsgNumView;
    }
}
