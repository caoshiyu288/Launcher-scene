package com.hct.scene.launcher2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hct.scene.launcher.R;

public class OnlyAppsPagedView extends AppsCustomizePagedView{
	protected boolean mShakeState = false;
	private boolean mEnableAnim = false;
	public OnlyAppsPagedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mEnableAnim = getResources().getBoolean(R.bool.config_housework_mainmenu_icon_anim);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void reset() {
        if (mCurrentPage != 0) {
            invalidatePageData(0);
        }
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected boolean testDataReady() {
		// TODO Auto-generated method stub
		return !mApps.isEmpty()&& mAppsHasSet;
	}

	@Override
	protected boolean beginDragging(View v) {
		//manage the shaking of icon
		startShakeAnimate(true);
		return true;
	}
	public void startShakeAnimate(boolean on){
		if(mShakeState == on) return;
		final int pageCount = getPageCount();
		for (int i = 0; i < pageCount; i++) {
			View layout = getChildAt(i);
			if(!(layout instanceof PagedViewCellLayout)) continue;
            PagedViewCellLayoutChildren childrenLayout = ((PagedViewCellLayout)layout).getChildrenLayout();
            final int childCount = childrenLayout.getChildCount();
            for (int j = 0; j < childCount; j++) {
				SceneShelfItem icon = (SceneShelfItem) childrenLayout.getChildAt(j);
				icon.setEnableAnim(mEnableAnim);
				if(on){
					icon.startAnimate(0,j%4);
				}
				else
					icon.cancelAnimate();
            }
            
		}
		if(on) mShakeState = true;
		else mShakeState = false;
	}
	public boolean isShake(){
		return mShakeState;
	}
	@Override
	protected View createApp(ApplicationInfo info, ViewGroup layout) {
        SceneShelfItem icon = (SceneShelfItem) mLayoutInflater.inflate(
                R.layout.scene_shelf_item, layout, false);
        icon.applyFromApplicationInfo(info, false, null);
        //icon.setOnClickListener(this);
        //icon.setOnLongClickListener(this);
        icon.mAppIcon.setOnClickListener(this);
        icon.mAppIcon.setOnLongClickListener(this);
        icon.setOnTouchListener(this);
        icon.mAppIcon.setOnKeyListener(this);
        icon.mRemove.setOnClickListener(this);
    	return icon;
	}

	@Override
	public void onClick(View v) {
		if(mShakeState){
			if(v.getParent() instanceof SceneShelfItem){
				SceneShelfItem parent = (SceneShelfItem) v.getParent();
				if(v == parent.mRemove){
					startShakeAnimate(false);
					//delete the app
					String packageName = null;
					if(v.getTag() instanceof ApplicationInfo){
						packageName = ((ApplicationInfo)v.getTag()).componentName.getPackageName();
					}else if(v.getTag() instanceof ShortcutInfo){
						packageName = ((ShortcutInfo)v.getTag()).intent.getComponent().getPackageName();
					}
					Uri packageURI = Uri.parse("package:"+packageName);
					Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
					mLauncher.startActivitySafely(uninstallIntent,"app uninstall");
				}
			}
			return;
		} 
		super.onClick(v);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	void scroll3D(int screenCenter, int style) {
	}
}
