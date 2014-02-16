package com.hct.scene.launcher2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;

public class HouseCellLayout extends CellLayout{
	private Rect mHightLightRect = null;
	static Paint mPaint = new Paint();
	static{
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2.5f);
		PathEffect effect = new DashPathEffect(new float[] { 3, 7, 3, 7},1);
//		mPaint.setAntiAlias(true);
		mPaint.setPathEffect(effect);
		
	}
	public HouseCellLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HouseCellLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HouseCellLayout(Context context) {
		super(context);
	}

	@Override
	protected void setCountXY() {
		mCountX = LauncherModel.getHouseCellCountX();
		mCountY = LauncherModel.getHouseCellCountY();
	}
	
	protected void updateAllCellsAnimation(boolean on) {
		final int childCount = getChildrenChildCount();
		for (int i = 0; i < childCount; i++) {
			Furniture furniture =(Furniture)mChildren.getChildAt(i);
			if (furniture.isAnimationObj()) {
				if (on) {
//					furniture.setCurrentPage(mCurrentPage);
					furniture.onAnimation(getMeasuredWidth());
				} else {
					furniture.stopAnimation(getMeasuredWidth());
//					furniture.setLastPage(page);
				}
			}
		}

	}
	
	protected void setZoomMode(boolean flag){
		final int childCount = getChildrenChildCount();
		for (int i = 0; i < childCount; i++) {
			Furniture furniture =(Furniture)mChildren.getChildAt(i);
			furniture.setZoomMode(flag);
		}
	}
	protected void setHighLightRect(Rect r){
		mHightLightRect = r;
		invalidate();
	}
	protected Rect getHightLightRect(){
		return mHightLightRect;
	}
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
		if(mHightLightRect != null){
			drawHightLight(canvas);
		}
	}
	protected void drawHightLight(Canvas canvas){
//		Paint p = new Paint();
//		p.setColor(Color.RED);
//		p.setStyle(Style.STROKE);
//		p.setStrokeWidth(3.5f);
		canvas.drawRect(mHightLightRect, mPaint);
		
	}
}
