package com.hct.scene.launcher2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hct.scene.launcher.R;

public class SceneShelfItem extends RelativeLayout implements View.OnClickListener{
	PagedViewIcon mAppIcon;
	ImageView mRemove;
	ImageView mSelect;
	static final int SPACE = 2;
	AnimatorSet mShakeSet;
	Context mContext;
	boolean mEnableAnim = true;
	public SceneShelfItem(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}

	public SceneShelfItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public SceneShelfItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
    @Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mAppIcon = (PagedViewIcon) findViewById(R.id.item);
		mRemove = (ImageView) findViewById(R.id.remove);
//		mRemove.setOnClickListener(this);
		mSelect = (ImageView) findViewById(R.id.select);
	}

	@Override
	public void setTag(Object tag) {
		super.setTag(tag);
        mAppIcon.setTag(tag);
        mRemove.setTag(tag);
        mSelect.setTag(tag);
	}

	public void applyFromApplicationInfo(ApplicationInfo info, boolean scaleUp,
            HolographicOutlineHelper holoOutlineHelper) {
        mAppIcon.applyFromApplicationInfo(info, scaleUp, holoOutlineHelper);
        setTag(info);
    }
	public void applyFromShortcutInfo(ShortcutInfo info, IconCache iconCache) {
		Bitmap b = info.getIcon(iconCache);
		mAppIcon.applyFromShortcutInfo(info, iconCache);
		setTag(info);
	}
	
    @Override
	public void onClick(View v) {
    	if(v == mRemove){
    		String packageName = null;
	    	if(getTag() instanceof ApplicationInfo){
	    		packageName = ((ApplicationInfo)getTag()).componentName.getPackageName();
	    	}else if(getTag() instanceof ShortcutInfo){
	    		packageName = ((ShortcutInfo)getTag()).intent.getComponent().getPackageName();
	    	}
    		Uri packageURI = Uri.parse("package:"+packageName);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
//			mLauncher.startActivitySafely(uninstallIntent,"app uninstall");
			mContext.startActivity(uninstallIntent);
    	}
	}
    private boolean isApplicationApp(){
    	return getTag() instanceof ApplicationInfo;
    }
    private boolean isShortcutApp(){
    	return getTag() instanceof ShortcutInfo;
    }
	public void startAnimate(int delay, int direction){
		//checkout wheather downloaded by user
    	if(isApplicationApp() && isDownload()){
    		mRemove.setImageResource(R.drawable.bookshelf_item_delete);
    		mRemove.setVisibility(VISIBLE);
    	}else if(isShortcutApp()){
    		mRemove.setImageResource(R.drawable.bookshelf_item_remove);
    		mRemove.setVisibility(VISIBLE);
    	}
    	if(!mEnableAnim) return;
    	mShakeSet = new AnimatorSet();
//    	final AnimatorSet set = new AnimatorSet();
//    	mShakeSet = set;
    	int[] track = new int[4];
    	int[] XY = new int[4];
    	//right-->down-->left-->up
    	track[0] = 0x4;
    	track[1] = track[0]|0x9;
    	track[2] = ~track[0];
    	track[3] = ~track[1];
    	int direc = generateRandom(4);
    	switch (direc) {
		case 0:
			//right-->down-->left-->up
			XY[0] = track[0];
			XY[1] = track[1];
			XY[2] = track[2];
			XY[3] = track[3];
			break;
		case 1:
			//down-->left-->up-->right
			XY[0] = track[1];
			XY[1] = track[2];
			XY[2] = track[3];
			XY[3] = track[0];
			break;
		case 2:
			//left-->up-->right-->down
			XY[0] = track[2];
			XY[1] = track[3];
			XY[2] = track[0];
			XY[3] = track[1];
			break;
		case 3:
			//up-->right-->down-->left
			XY[0] = track[3];
			XY[1] = track[0];
			XY[2] = track[1];
			XY[3] = track[2];
			break;
		default:
			break;
		}
    	Animator[] trackAnimator = new Animator[4];
//    	Log.d("furniture", "startAnimate--direction--"+direc+",XY--"+XY[0]);
    	for (int i = 0; i < 4; i++) {
    		trackAnimator[i] = translateAnimate(this, ((XY[i]&0x8)>>3) * SPACE, ((XY[i]&0x4)>>2) *SPACE, 
        			((XY[i]&0x2)>>1) * SPACE, ((XY[i]&0x1)>>0) * SPACE);
		}
//    	//right
//    	Animator rightAni = translateAnimate(this, 0, SPACE, 0, 0);
//    	//down
//    	Animator downAni = translateAnimate(this, SPACE, SPACE, 0, SPACE);
//    	//left
//    	Animator leftAni = translateAnimate(this, SPACE, 0, SPACE, SPACE);
//    	//up
//    	Animator upAni = translateAnimate(this, 0, 0, SPACE, 0);
    	
    	mShakeSet.addListener(mShakeAnimListener);
    	mShakeSet.setStartDelay(delay);
//    	set.playSequentially(rightAni,downAni,leftAni,upAni);
    	mShakeSet.playSequentially(trackAnimator[0],trackAnimator[1],trackAnimator[2],trackAnimator[3]);
    	mShakeSet.start();
    }
    public void cancelAnimate(){
    	if(mRemove.getVisibility() == VISIBLE){
    		mRemove.setVisibility(GONE);
    	}
    	if(!mEnableAnim) return;
    	if(mShakeSet != null){
    		mShakeSet.cancel();
    		mShakeSet = null;
    	}
    }
    public void setEnableAnim(boolean enable){
    	mEnableAnim = enable;
    }
    private Animator translateAnimate(final View v, final int startX, final int endX, final int startY, final int endY){
    	ValueAnimator va = ValueAnimator.ofFloat(0f, 1f);
    	va.setDuration(150);
    	va.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float r = ((Float)animation.getAnimatedValue()).floatValue();
				float x = (1-r)*startX + r*endX;
				float y = (1-r)*startY + r*endY;
				v.setTranslationX(x);
				v.setTranslationY(y);
			}
		});
    	return va;
    }
    private AnimatorListenerAdapter mShakeAnimListener = new AnimatorListenerAdapter() {
		@Override
		public void onAnimationEnd(Animator animation) {
			super.onAnimationEnd(animation);
			Log.d("furniture", "shake animater end~~~");
			if(mShakeSet != null){
				mShakeSet.setStartDelay(0);
				mShakeSet.start();
			}
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			super.onAnimationCancel(animation);
			Log.d("furniture", "shake animater cancel~~~");
			SceneShelfItem.this.setTranslationX(0f);
			SceneShelfItem.this.setTranslationY(0f);
		}
		
	};
    
	private int generateRandom(int num){
		int n = num;
		int i = (int)(Math.random()*n);
		i = i<n ? i : i-1;
		return i;
	}
	
	private boolean isDownload(){
		android.content.pm.ApplicationInfo  info = null;
	    try {
	    	String packageName = null;
	    	if(getTag() instanceof ApplicationInfo){
	    		packageName = ((ApplicationInfo)getTag()).componentName.getPackageName();
	    	}else if(getTag() instanceof ShortcutInfo){
	    		packageName = ((ShortcutInfo)getTag()).intent.getComponent().getPackageName();
	    	}
			info = getContext().getPackageManager().getApplicationInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
//	    if(info == null) return false;
	    if((info.flags&info.FLAG_SYSTEM) <= 0){
	    	return true;
	    }
		return false;
	}
}
