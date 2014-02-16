package com.hct.scene.launcher2;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hct.scene.launcher.R;

public class HouseStyleTransfer extends LinearLayout implements View.OnClickListener{
	private TextView mTitle;
	private LinearLayout mContent;
	private LayoutInflater mInflater;
	private View mFocusView;
	private Launcher mLauncher;
	public HouseStyleTransfer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mInflater = LayoutInflater.from(context);
	}

	public HouseStyleTransfer(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public HouseStyleTransfer(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTitle = (TextView) findViewById(R.id.title);
		mContent = (LinearLayout) findViewById(R.id.content);
		intContent();
		
	}

	private void intContent(){
		if(mContent != null){
			for (int i = 0; i < Furniture.STYLE_AMOUNT; i++) {
				ThumnailFrame thumnail= (ThumnailFrame) mInflater.inflate(R.layout.style_thumnail, null);
				thumnail.setOnClickListener(this);
				mContent.addView(thumnail);
//				ThumnailFrame thumnailPrincess = (ThumnailFrame) mInflater.inflate(R.layout.style_thumnail, null);
//				thumnailPrincess.setOnClickListener(this);
//				mContent.addView(thumnailPrincess);
			}
		}
	}
	public void setup(Launcher launcher){
		mLauncher = launcher;
	}
	@Override
	public void onClick(View v) {
		if(v instanceof ThumnailFrame){
			final int count = mContent.getChildCount();
			if(count != Furniture.STYLE_AMOUNT) return;
			for (int i = 0; i < count; i++) {
				ThumnailFrame thumnail = (ThumnailFrame) mContent.getChildAt(i);
				if(thumnail == v){
					if(!thumnail.isFocus()){
						thumnail.updateInd(true);
						if(mFocusView instanceof Furniture){
							((Furniture)mFocusView).changeStyle(i);
							((Furniture)mFocusView).updateDatabase(i);
							mLauncher.getHouseWorkspace().
							changeAllSameFurnitureStyle((Furniture)mFocusView, i);
						}else if(mFocusView instanceof HouseWorkspace){
							((HouseWorkspace)mFocusView).setWall(i);
							mLauncher.saveHouseWall(i);
						}
						thumnail.setFocus(true);
					}
				}else{
					if(thumnail.isFocus()){
						thumnail.updateInd(false);
						thumnail.setFocus(false);
					}
				}
			}
		}
	}
	public void updateStyle(String title, String arrayName, int style){
    	final String packageName = getContext().getPackageName();
    	final int arrayId = getResources().getIdentifier(arrayName, "array", packageName);
    	if(arrayId != 0)
    		updateStyle(title, arrayId, style);
	}
	public void updateStyle(String title, int arrayId, int style){
		if(mTitle != null){
			mTitle.setText(title);
		}

		TypedArray styleArray = getResources().obtainTypedArray(R.array.furniture_style);
		TypedArray thumnailArray = getResources().obtainTypedArray(arrayId);

		Log.d("furniture", "thumnailArray.length()----"+thumnailArray.length());
		for (int i = 0; i < thumnailArray.length(); i++) {
			ThumnailFrame frame = (ThumnailFrame) mContent.getChildAt(i);
			frame.setContent(styleArray.getString(i), thumnailArray.getDrawable(i));
			if(style == i){
				if(!frame.isFocus()){
					frame.updateInd(true);
					frame.setFocus(true);
				}
			}else{
				if(frame.isFocus()){
					frame.updateInd(false);
					frame.setFocus(false);
				}
			}
		}

		styleArray.recycle();
		thumnailArray.recycle();
	}
	public void setFocusView(View focusView){
		mFocusView = focusView;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		LauncherLog.d("HouseStyle","onInterceptTouchEvent--ev--"+ev);
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//intercept the event while in zoom mode
		LauncherLog.d("HouseStyle","onTouchEvent--ev--"+event);
		if(mLauncher.getHouseWorkspace().isZoom()){
			return true;
		}
		return super.onTouchEvent(event);
	}
	
}
