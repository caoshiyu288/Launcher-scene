package com.hct.scene.launcher2;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.hct.scene.launcher.R;

public class GlobeFurniture extends Furniture{
    private Context mContext; 
    private ImageView img_browser;
    private ImageView img_desk;
    private final int deskImageId = 1;
    private final int browserImageId = 2;
//    private static final String TAG = "liuyang-bbkui";
    
	public GlobeFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public GlobeFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GlobeFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
        mContext = context;
        initViews(context);
	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
    	Rect r = mTempRect;
    	img_desk.getHitRect(r);
    	r.left += 2;
		return r;
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
        img_desk = new ImageView(context);
       	img_desk.setId(deskImageId);
        addView(img_desk);
        
        img_browser = new ImageView(context);
        img_browser.setId(browserImageId);
        addView(img_browser);
        
		img_desk.setOnClickListener(viewClickListener);
		img_browser.setOnClickListener(viewClickListener);
    }
    
    private void setParams(Context context, int style){
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) img_desk.getLayoutParams();
        if (style == 0){
        	img_desk.setImageResource(R.drawable.browser_fresh_bg);
        	params1.topMargin = (int) getResources().getDimension(R.dimen.browserbg_view_fresh_top_margin);
        } else if (style == 1) {
        	img_desk.setImageResource(R.drawable.browser_princess_bg);
        	params1.topMargin = (int) getResources().getDimension(R.dimen.browserbg_view_princess_top_margin);
        }
        params1.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params1.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params1.gravity = Gravity.TOP | Gravity.LEFT;
        img_desk.setLayoutParams(params1);

        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) img_browser.getLayoutParams();
        if (style == 0){
        	img_browser.setImageResource(R.drawable.browser_fresh);
        	params2.topMargin = (int) getResources().getDimension(R.dimen.browser_view_fresh_top_margin);
        	params2.leftMargin = (int) getResources().getDimension(R.dimen.browser_view_fresh_left_margin);
        } else if (style == 1) {
        	img_browser.setImageResource(R.drawable.browser_princess);
        	params2.topMargin = (int) getResources().getDimension(R.dimen.browser_view_princess_top_margin);
        	params2.leftMargin = (int) getResources().getDimension(R.dimen.browser_view_princess_left_margin);
        }
        params2.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params2.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params2.gravity = Gravity.TOP | Gravity.LEFT;
        img_browser.setLayoutParams(params2);
        
		img_desk.setOnLongClickListener(mLongClickListener);
		img_browser.setOnLongClickListener(mLongClickListener);
    }

	private OnClickListener viewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mZoomMode) {
				mSingleClickListener.onClick(v);
			} else {
				if (v.getId() == img_browser.getId()) {
					Intent intent = new Intent();
					String [] pkg_cls_name = mContext.getResources().getStringArray(R.array.globe);
//						intent.setClassName(pkg_cls_name[0], pkg_cls_name[1]);
//						intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setAction(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.setPackage(pkg_cls_name[0]);
					PackageManager pm =mContext.getPackageManager();
					ResolveInfo bestMatch = pm.resolveActivity(intent, 0);
					Log.d("caoshiyu-globle", "bestMatch--->"+bestMatch);
					List<ResolveInfo> resolves = pm.queryIntentActivities(intent, 0);
					boolean found = false;
					for (int i = 0; i < resolves.size(); i++) {
						ResolveInfo info = resolves.get(i);
						Log.d("caoshiyu-globle", "infoinfoinf-->"+info.activityInfo.packageName+","+info.activityInfo.name);
						if(bestMatch.activityInfo.packageName.equals(info.activityInfo.packageName)
								&& bestMatch.activityInfo.name.equals(info.activityInfo.name)){
							found = true;
							break;
						}
					}
					if(found){
						intent.setClassName(bestMatch.activityInfo.packageName, bestMatch.activityInfo.name);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
						mContext.startActivity(intent);
					}else{
						try {
							Intent intent1 = new Intent();
							intent1.setClassName("com.android.browser",
									"com.android.browser.BrowserActivity");
							intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
							mContext.startActivity(intent1);
						}catch(Exception e){
							e.printStackTrace();
							Toast.makeText(mContext, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
						}
					}
/*					try {
						String [] pkg_cls_name = mContext.getResources().getStringArray(R.array.globe);
//						intent.setClassName(pkg_cls_name[0], pkg_cls_name[1]);
						intent.setPackage(pkg_cls_name[0]);
//						intent.addCategory(Intent.CATEGORY_BROWSABLE);
						intent.addCategory(Intent.CATEGORY_LAUNCHER);
						mContext.startActivity(intent);
					} catch (Exception e) {
						intent.setClassName("com.android.browser",
								"com.android.browser.BrowserActivity");
						mContext.startActivity(intent);
					}*/
				}
			}
		}
	};
}
