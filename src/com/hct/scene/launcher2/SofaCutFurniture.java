package com.hct.scene.launcher2;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class SofaCutFurniture extends Furniture{
	private ImageView mContent;
	public SofaCutFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initContent(context);
	}

	public SofaCutFurniture(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SofaCutFurniture(Context context) {
		this(context, null, 0);
	}

	private void initContent(Context context){
		ImageView view = new ImageView(context);
		view.setOnClickListener(this);
		addView(view);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
    	params.topMargin = (int) getResources().getDimension(R.dimen.sofa_view_fresh_top_margin);//68;
//        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        view.setLayoutParams(params);
		mContent = view;
	}
	@Override
	protected Rect getHighLightFrame() {
		if(mContent != null){
			Rect r = mTempRect;
			mContent.getHitRect(r);
			r.left = -2;
			return r;
		}
		return null;
	}

	@Override
	public void changeStyle(int style) {
		super.changeStyle(style);
		if(style == 0){
			mContent.setImageResource(R.drawable.sofa_cut_fresh);
		}else if(style == 1){
			mContent.setImageResource(R.drawable.sofa_cut_princess);
//			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mContent.getLayoutParams();
//			params.topMargin = (int) getResources().getDimension(R.dimen.sofa_view_princess_top_margin);
//			mContent.setLayoutParams(params);
		}
	}

	@Override
	public void onClick(View v) {
		if(mZoomMode){
			mSingleClickListener.onClick(v);
		}
	}

	@Override
	public void setLongClickListener(OnLongClickListener listener) {
		super.setLongClickListener(listener);
		if(mContent != null){
			mContent.setOnLongClickListener(listener);
		}
	}
	
	
}
