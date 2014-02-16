package com.hct.scene.launcher2;

import com.hct.scene.launcher.R;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ShowcaseCutFurniture extends Furniture{
	private ImageView mContent;
	public ShowcaseCutFurniture(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initContent(context);
	}

	public ShowcaseCutFurniture(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ShowcaseCutFurniture(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}
	private void initContent(Context context){
		ImageView view = new ImageView(context);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		view.setOnClickListener(this);
		addView(view, lp);
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
			mContent.setImageResource(R.drawable.showcase_cut_fresh);
		}else if(style == 1){
			mContent.setImageResource(R.drawable.showcase_cut_princess);
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
