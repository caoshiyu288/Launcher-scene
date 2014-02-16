package com.hct.scene.launcher2;

import com.hct.scene.launcher.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ThumnailFrame extends FrameLayout{
	private TextView mThumnail;
	private ImageView mInd;
	private boolean mFocus;
	public ThumnailFrame(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ThumnailFrame(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ThumnailFrame(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mThumnail =(TextView) findViewById(R.id.thumnail);
		mInd = (ImageView) findViewById(R.id.ind);
	}
	
	public void setContent(String text, Drawable icon){
		mThumnail.setText(text);
		mThumnail.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
	}
	public void updateInd(boolean on){
		if(on){
			if(mInd.getDrawable() == null)
				mInd.setImageDrawable(getResources().getDrawable(R.drawable.style_using));
		}else{
			mInd.setImageDrawable(null);
		}
	}
	public void setFocus(boolean flag){
		mFocus = flag;
	} 
	public boolean isFocus(){
		return mFocus;
	}
}
