package com.hct.scene.launcher2;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class SofaFurniture extends Furniture{
	
	private Context mContext; 
//	private BitmapDrawable mDrawable;
	private ImageView img_sofa;
//	private int mLayoutWidth = 0;
//	private int mLayoutHeight = 0;

	public SofaFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SofaFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SofaFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		initViews(context);
	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
    	Rect rect = new Rect();
    	img_sofa.getHitRect(rect);
    	
		return rect;
	}

	@Override
	public void changeStyle(int style){
        setParams(mContext, style);
		super.changeStyle(style);
	}
	
	@Override
	public boolean isAnimationObj(){
		return false;
	}
	
	@Override
	public void onAnimation(int width){
	
		super.onAnimation(width);
	}

    private void initViews(Context context) {
    	img_sofa = new ImageView(context);
    	addView(img_sofa);
    	FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) img_sofa.getLayoutParams();
    	params.topMargin = (int) getResources().getDimension(R.dimen.sofa_view_fresh_top_margin);//68;
    	img_sofa.setLayoutParams(params);
        img_sofa.setOnClickListener(viewClickListener);
    }
    
    private void setParams(Context context, int style){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) img_sofa.getLayoutParams();
        if (style == 0){
        	img_sofa.setImageResource(R.drawable.sofa_fresh);
        } else if (style == 1) {
        	img_sofa.setImageResource(R.drawable.sofa_princess);
        }
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
//        if (style == 0){
//    		params.topMargin = (int) getResources().getDimension(R.dimen.sofa_view_fresh_top_margin);//68;
//        } else if (style == 1) {
//    		params.topMargin = (int) getResources().getDimension(R.dimen.sofa_view_princess_top_margin);//68;
//        }
        img_sofa.setLayoutParams(params);
        
        img_sofa.setOnLongClickListener(mLongClickListener);
    }

	private OnClickListener viewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mZoomMode) {
				mSingleClickListener.onClick(v);
			} else {
				
			}
		}
	};

}
