package com.hct.scene.launcher2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.hct.scene.launcher.R;

public class BookcaseFurniture extends Furniture{
	private static String TAG = "BookcaseFurniture";
	private BookScene mBookScene;
	public BookcaseFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initBookCase();
	}

	public BookcaseFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initBookCase();
	}

	public BookcaseFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initBookCase();
	}
	private void initBookCase(){
		mBookScene = new BookScene(getContext());
		LayoutParams bookSceneParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT|Gravity.TOP);
		bookSceneParam.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.bookscene_top_margin);
		addView(mBookScene, bookSceneParam);
		mBookScene.setOnClickListener(this);
	}
	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
		Rect rect = new Rect();
		rect.left = mBookScene.getLeft();
		rect.top = mBookScene.getTop();
		rect.right = mBookScene.getRight()-1;
		rect.bottom = mBookScene.getBottom();
		return rect;
	}

	@Override
	public void changeStyle(int style) {
		// TODO Auto-generated method stub
		super.changeStyle(style);
		mBookScene.changeStyle(style);
		mBookScene.setOnLongClickListener(mLongClickListener);
	}
	private boolean checkPackageName(String packageName) {  
		if (packageName == null || "".equals(packageName))	
			return false;  
		try {  
			android.content.pm.ApplicationInfo info = getContext().getPackageManager().getApplicationInfo(	
					packageName, PackageManager.GET_UNINSTALLED_PACKAGES);	
			return true;  
		} catch (NameNotFoundException e) {  
			return false;  
		}  
	} 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mZoomMode){
			mSingleClickListener.onClick(v);
		}else{
			String[] bookReading = getResources().getStringArray(R.array.book_reading);
			String packageName = null;
			String className = null;
			for(int i = 0; i < bookReading.length ; i=i+2){
				
				Log.d(TAG, "bookReading[i] = " + bookReading[i] + " |bookReading[i+1] = " + bookReading[i+1]);
				if(checkPackageName(bookReading[i])){
					packageName = bookReading[i];
					className = bookReading[i+1];
					break;
				}
			}
			Log.d(TAG, "packageName = " + packageName + " |className = " + className);
			if(packageName != null && className != null){
				try{
				       Intent intent = new Intent();
					   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
				       intent.setClassName(packageName, className);
				       getContext().startActivity(intent);
				}catch(ActivityNotFoundException e){
					Toast.makeText(getContext(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
				}catch(SecurityException e){
					Toast.makeText(getContext(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
