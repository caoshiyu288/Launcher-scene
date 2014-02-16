package com.hct.scene.launcher2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
public class MusicalNoteScene extends View implements AnimationListener{
	private int mLayoutWidth;
	private int mLayoutHeight;
	private BitmapDrawable mDrawable;
	private Animation mPurpleTranslateAnimation;
	private Animation mBlueTranslateAnimation;
	private Animation mYellowTranslateAnimation;
	private Animation shortTranslateAnimation;
	private AnimatorSet purpleAnimatorSet;
	private AnimatorSet blueAnimatorSet;
	private AnimatorSet yellowAnimatorSet;
	private AnimatorSet shortAnimatorSet;
	private int isPlayAnimationEnd = 0;
	private static float scaleSize = 0.9f;
	public MusicalNoteScene(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MusicalNoteScene(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MusicalNoteScene(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void setMusicalNoteScene(int id){
		Resources res = getResources();
		setBackgroundDrawable(res.getDrawable(id));
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		mDrawable = (BitmapDrawable) getBackground();
		if (mDrawable != null) {
			Bitmap bitmap = mDrawable.getBitmap();
			mLayoutWidth = bitmap.getWidth();
			mLayoutHeight = bitmap.getHeight();
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(mLayoutWidth, mLayoutHeight);
	}
	public void purpleTranslateEnd(){
		isPlayAnimationEnd = 1;
		if(mPurpleTranslateAnimation != null){
			purpleAnimatorSet.end();
			mPurpleTranslateAnimation.cancel();
		}
	}
	public void blueTranslateEnd(){
		isPlayAnimationEnd = 1;
		if(mBlueTranslateAnimation != null){
			blueAnimatorSet.end();
			mBlueTranslateAnimation.cancel();
		}
	}
	public void yellowTranslateEnd(){
		isPlayAnimationEnd = 1;
		if(mYellowTranslateAnimation != null){
			yellowAnimatorSet.end();
			mYellowTranslateAnimation.cancel();
		}
	}
	public void purpleTranslate(){
		isPlayAnimationEnd = 0;
		mPurpleTranslateAnimation = new ArcTranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -5.5f, 
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 2); 
		mPurpleTranslateAnimation.setDuration(5000);
		mPurpleTranslateAnimation.setFillAfter(true);
		mPurpleTranslateAnimation.setAnimationListener(this);
		
		ValueAnimator fade = ObjectAnimator.ofFloat(this, "alpha", 0f, 0f);
		fade.setDuration(500);
		ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
		fadeOutAnim.setDuration(500);
		ValueAnimator fadeInAnim = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
		fadeInAnim.setDuration(500);
		
		ValueAnimator ScaleOutAnim = ObjectAnimator.ofFloat(this, "scaleX", 0.5f, scaleSize);
		ScaleOutAnim.setDuration(1000);
		ValueAnimator ScaleInAnim = ObjectAnimator.ofFloat(this, "scaleY", 0.5f, scaleSize);
		ScaleInAnim.setDuration(1000);
		
		ValueAnimator ScaleOutEndAnim = ObjectAnimator.ofFloat(this, "scaleX", scaleSize, 0.5f);
		ScaleOutEndAnim.setDuration(1000);
		ValueAnimator ScaleInEndAnim = ObjectAnimator.ofFloat(this, "scaleY", scaleSize, 0.5f);
		ScaleInEndAnim.setDuration(1000);
		
		purpleAnimatorSet = new AnimatorSet();
		purpleAnimatorSet.play(fade);
		purpleAnimatorSet.play(ScaleOutAnim).after(500);
		purpleAnimatorSet.play(ScaleInAnim).after(500);
		purpleAnimatorSet.play(fadeOutAnim).after(500);
	    
		purpleAnimatorSet.play(ScaleOutEndAnim).after(3000);
		purpleAnimatorSet.play(ScaleInEndAnim).after(3000);
		purpleAnimatorSet.play(fadeInAnim).after(4500);
	    
		purpleAnimatorSet.start();
	    startAnimation(mPurpleTranslateAnimation);
	}
	
	public void blueTranslate(){
		isPlayAnimationEnd = 0;
		mBlueTranslateAnimation = new ArcTranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -6, 
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1); 
		mBlueTranslateAnimation.setDuration(7000);
		mBlueTranslateAnimation.setFillAfter(true);
		mBlueTranslateAnimation.setAnimationListener(this);
		
		ValueAnimator fade = ObjectAnimator.ofFloat(this, "alpha", 0f, 0f);
		fade.setDuration(1000);
		ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
		fadeOutAnim.setDuration(500);
		ValueAnimator fadeInAnim = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
		fadeInAnim.setDuration(500);
		
		ValueAnimator ScaleOutAnim = ObjectAnimator.ofFloat(this, "scaleX", 0.5f, scaleSize);
		ScaleOutAnim.setDuration(1500);
		ValueAnimator ScaleInAnim = ObjectAnimator.ofFloat(this, "scaleY", 0.5f, scaleSize);
		ScaleInAnim.setDuration(1500);
		
		ValueAnimator ScaleOutEndAnim = ObjectAnimator.ofFloat(this, "scaleX", scaleSize, 0.6f);
		ScaleOutEndAnim.setDuration(2000);
		ValueAnimator ScaleInEndAnim = ObjectAnimator.ofFloat(this, "scaleY", scaleSize, 0.6f);
		ScaleInEndAnim.setDuration(2000);
		
		blueAnimatorSet = new AnimatorSet();
		blueAnimatorSet.play(fade);
		blueAnimatorSet.play(ScaleOutAnim).after(1000);
		blueAnimatorSet.play(ScaleInAnim).after(1000);
		blueAnimatorSet.play(fadeOutAnim).after(1000);
	    
		blueAnimatorSet.play(ScaleOutEndAnim).after(3000);
		blueAnimatorSet.play(ScaleInEndAnim).after(3000);
		blueAnimatorSet.play(fadeInAnim).after(6500);
	    
		blueAnimatorSet.start();
	    startAnimation(mBlueTranslateAnimation);
	}
	public void yellowTranslate(){
		isPlayAnimationEnd = 0;
		mYellowTranslateAnimation = new ArcTranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -6, 
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 4); 
		mYellowTranslateAnimation.setDuration(6000);
		mYellowTranslateAnimation.setFillAfter(true);
		mYellowTranslateAnimation.setAnimationListener(this);
		
		ValueAnimator fade = ObjectAnimator.ofFloat(this, "alpha", 0f, 0f);
		fade.setDuration(500);
		ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
		fadeOutAnim.setDuration(500);
		ValueAnimator fadeInAnim = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
		fadeInAnim.setDuration(600);
		
		ValueAnimator ScaleOutAnim = ObjectAnimator.ofFloat(this, "scaleX", 0.4f, scaleSize);
		ScaleOutAnim.setDuration(1500);
		ValueAnimator ScaleInAnim = ObjectAnimator.ofFloat(this, "scaleY", 0.4f, scaleSize);
		ScaleInAnim.setDuration(1500);
		
		ValueAnimator ScaleOutEndAnim = ObjectAnimator.ofFloat(this, "scaleX", scaleSize, 0.5f);
		ScaleOutEndAnim.setDuration(1500);
		ValueAnimator ScaleInEndAnim = ObjectAnimator.ofFloat(this, "scaleY", scaleSize, 0.5f);
		ScaleInEndAnim.setDuration(1500);
		
		yellowAnimatorSet = new AnimatorSet();
		yellowAnimatorSet.play(fade);
		yellowAnimatorSet.play(ScaleOutAnim).after(500);
		yellowAnimatorSet.play(ScaleInAnim).after(500);
		yellowAnimatorSet.play(fadeOutAnim).after(500);
	    
		yellowAnimatorSet.play(ScaleOutEndAnim).after(3000);
		yellowAnimatorSet.play(ScaleInEndAnim).after(3000);
		yellowAnimatorSet.play(fadeInAnim).after(5500);
		
		yellowAnimatorSet.start();
	    startAnimation(mYellowTranslateAnimation);
	}
	public void shortTranslate(){
		shortTranslateAnimation = new ArcShortTranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -10, 
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -3); 
		shortTranslateAnimation.setDuration(5000);
		shortTranslateAnimation.setFillAfter(true);
		
		ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
		fadeOutAnim.setDuration(1000);
		ValueAnimator fadeInAnim = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
		fadeInAnim.setDuration(800);
		
		ValueAnimator ScaleOutAnim = ObjectAnimator.ofFloat(this, "scaleX", 0f, scaleSize);
		ScaleOutAnim.setDuration(1500);
		ValueAnimator ScaleInAnim = ObjectAnimator.ofFloat(this, "scaleY", 0f, scaleSize);
		ScaleInAnim.setDuration(1500);
		
		ValueAnimator ScaleOutEndAnim = ObjectAnimator.ofFloat(this, "scaleX", scaleSize, 0f);
		ScaleOutEndAnim.setDuration(1000);
		ValueAnimator ScaleInEndAnim = ObjectAnimator.ofFloat(this, "scaleY", scaleSize, 0f);
		ScaleInEndAnim.setDuration(1000);
		
		shortAnimatorSet = new AnimatorSet();
		shortAnimatorSet.play(ScaleOutAnim);
		shortAnimatorSet.play(ScaleInAnim);
		shortAnimatorSet.play(fadeOutAnim);
	    
		shortAnimatorSet.play(ScaleOutEndAnim).after(1800);
		shortAnimatorSet.play(ScaleInEndAnim).after(1800);
		
		shortAnimatorSet.play(fadeInAnim).after(1500);
		
		shortAnimatorSet.start();
	    startAnimation(shortTranslateAnimation);
	}
	public void shortTranslateEnd(){
		if(shortTranslateAnimation != null){
			shortAnimatorSet.end();
			shortTranslateAnimation.cancel();
		}
	}
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if(isPlayAnimationEnd == 0){
			if(animation.equals(mPurpleTranslateAnimation)){
				purpleTranslate();
			}else if(animation.equals(mBlueTranslateAnimation)){
				blueTranslate();
			}else if(animation.equals(mYellowTranslateAnimation)){
				yellowTranslate();
			}
		}else{
			clearAnimation();
		}
	}

	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
