package com.hct.scene.launcher2;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.hct.scene.launcher2.Launcher.AddAppsToHouse;
public abstract class Furniture extends FrameLayout implements View.OnClickListener, SelfAnimation{
	
	private Rect mFrame = new Rect(); 
	public final static int STYLE_AMOUNT = 2;
	protected ArrayList<Drawable> mTempDrawables = new ArrayList<Drawable>(STYLE_AMOUNT);
	protected int mLastPage;
	protected int mCurrentPage;
	protected boolean mZoomMode;
	protected View.OnLongClickListener mLongClickListener;
	protected View.OnClickListener mSingleClickListener;
	protected AddAppsToHouse mAddApps;
	protected boolean mHightFlag;
	protected final Rect mTempRect = new Rect();
	public Furniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public Furniture(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public Furniture(Context context) {
		this(context, null, 0);
	}
    public void applyFromInfo(FurnitureInfo info) {
        changeStyle(info.style);
        setTag(info);
    }
    public void changeStyle(int style){
    //	setBackground(style);
    }
    private void setBackground(int style){
    	ArrayList<Drawable> bgs = loadArray(this.getClass().getSimpleName());
    	if(bgs.size()>style){
    		setBackgroundDrawable(bgs.get(style));
    	}
    }
    protected void updateDatabase(int style){
    	FurnitureInfo info = (FurnitureInfo) getTag();
    	info.style = style;
    	LauncherModel.updateItemInDatabase(getContext(), info);
    }
    protected ArrayList<Drawable> loadArray(String arrayName){
    	final ArrayList<Drawable> drawables = new ArrayList<Drawable>();
    	drawables.clear();
    	final String packageName = getContext().getPackageName();//getResources().getResourcePackageName(R.array.furniture_style);
//    	final String simpleClassName = this.getClass().getSimpleName();
    	Log.d("furniture", "FurnitureStruct-packageName=="+packageName+"," +
    			"simpleName=="+arrayName);
    	final int furnitureArrayId = getResources().getIdentifier(arrayName, "array", packageName);
    	if(furnitureArrayId != 0){
    		String[] res = getResources().getStringArray(furnitureArrayId);
//    		if(res.length < 2){
//    			throw new NotFoundException("furniture class bg is not enough!!!");
//    		} 
    		for (int i = 0; i < res.length; i++) {
    			try {
    				Drawable d = getResources().getDrawable(getResources().getIdentifier(res[i], "drawable", packageName));
    				drawables.add(d);
    			} catch (NotFoundException e) {
    				e.printStackTrace();
    				drawables.add(null);
    			}
    		}
    	}
    	return drawables;
    }
    /*
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d("furniture", "onMease--"+this);
//		Drawable bg = getBackground();
//		if(bg != null){
//			setMeasuredDimension(bg.getIntrinsicWidth(), bg.getIntrinsicHeight());
//		}
		setMeasuredDimension(mSpanX * , bg.getIntrinsicHeight());
	}
	*/
    
    
    /**
     *   set a dotted frame for the focused view
     * */
//	protected void setHighLightFrame(Rect frame){
//		mFrame = frame;
//		invalidate();
//	}
	protected abstract Rect getHighLightFrame();
	
	public void setHightFlag(boolean flag){
		mHightFlag = flag;
		invalidate();
	}
	public boolean getHightFlag(){
		return mHightFlag;
	}
	@Override
	public void onClick(View v) {
		Log.d("furniture", "furniture--onClick--v--"+v+"this=="+this);
//		mFrame = getHighLightFrame();
//		invalidate();
	}
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
//		if(mZoomMode){
//			drawHightLightFrame(canvas);
//		}
//		drawSelfArea(canvas);
		if(mHightFlag){
//			drawHightLightFrame(canvas);
		}
	}
    
	private void drawHightLightFrame(Canvas canvas){
		//draw the rect
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(3.5f);
		Rect frame = getHighLightFrame();
		if(frame != null)
			canvas.drawRect(frame, p);
	}
	protected void drawSelfArea(Canvas canvas){
		//draw the rect
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(3.5f);
		Rect r = mFrame;
		if(r != null){
			r.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
			canvas.drawRect(r, p);
		}
	}

	protected void updateOffset(int offset){
		
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.d("furniture", "furniture--dispatchTouchEvent----"+ev+","+this);
		return super.dispatchTouchEvent(ev);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.d("furniture", "furniture--onInterceptTouchEvent----"+ev+","+this);
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("furniture", "furniture--onTouchEvent----"+event+","+this);
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	
	public void setLongClickListener(View.OnLongClickListener listener){
		mLongClickListener = listener;
	}
	public void setSingleClickListener(View.OnClickListener listener){
		mSingleClickListener = listener;
	}
	public void setAddAppsRunnable(AddAppsToHouse runnable){
		mAddApps = runnable;
	}
	public void setZoomMode(boolean flag){
		mZoomMode = flag;
	}
	@Override
	public void setLastPage(int page) {
		mLastPage = page;
	}
	
	@Override
	public void setCurrentPage(int page) {
		mCurrentPage = page;
		
	}
	public boolean isAnimationObj(){
		return false;
	}
	@Override
	public void onAnimation(int width) {
		
	}
	@Override
	public void stopAnimation(int width) {
		
	}
	@Override
	public void callbackAnimation() {
		
	}
	
}
interface SelfAnimation{
	public boolean isAnimationObj();
	public void onAnimation(int width);
	public void stopAnimation(int width);
	public void callbackAnimation();
	public void setLastPage(int page);
	public void setCurrentPage(int page);
}