package com.hct.scene.launcher2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.hct.scene.launcher.R;

public class ChatFurniture extends Furniture{
    private Context mContext; 
    private ImageView img_qq;
    
	public ChatFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ChatFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ChatFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
   	    mContext = context;
   	    initViews(context);
	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
    	Rect rect = new Rect();
    	img_qq.getHitRect(rect);
    	
		return rect;
	}

	@Override
	public void changeStyle(int style){
		super.changeStyle(style);
        setParams(mContext, style);
	}
	
	@Override
	public boolean isAnimationObj(){
		return true;
	}
	
	@Override
	public void onAnimation(int width){
	
		super.onAnimation(width);
		
		Animation am = new TranslateAnimation (0, 0, 0,-12);
		am.setDuration(120);
		am.setRepeatCount(3);
		am.setRepeatMode(Animation.REVERSE);
		setAnimation(am);
		img_qq.startAnimation(am);
	}

    private void initViews(Context context) {
    	img_qq = new ImageView(context);
    	addView(img_qq);
        img_qq.setOnClickListener(viewClickListener);
    }
    
    private void setParams(Context context, int style){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) img_qq.getLayoutParams();
        if (style == 0){
        	img_qq.setImageResource(R.drawable.chat_fresh);
        } else if (style == 1) {
        	img_qq.setImageResource(R.drawable.chat_princess);
        }
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        img_qq.setLayoutParams(params);
        
        img_qq.setOnLongClickListener(mLongClickListener);
    }

	private OnClickListener viewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mZoomMode) {
				mSingleClickListener.onClick(v);
			} else {
				 Intent intent = new Intent();
				 intent.setClassName("com.tencent.mobileqq",
				 "com.tencent.mobileqq.activity.SplashActivity");
				// mContext.startActivity(intent);
				 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		        try {
		            getContext().startActivity(intent);
		        } catch (ActivityNotFoundException e) {
		            Toast.makeText(getContext(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
		        } catch (SecurityException e) {
		            Toast.makeText(getContext(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
		        }
			}
		}
	};
}
