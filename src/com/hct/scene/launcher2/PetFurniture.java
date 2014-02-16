package com.hct.scene.launcher2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class PetFurniture extends Furniture {
	private Context mContext;
	// private AnimationDrawable mDrawable;
	private BitmapDrawable mDrawable;
	private int mLayoutWidth = 0;
	private int petRunEndPosX = 0;
	private ImageView img_pet;
	private AnimationDrawable petAnimation;

	private ObjectAnimator anim;
	private boolean petIsRunEnd = true;
	private boolean petIsRunPause = false;
	private long runAllTime = 3500;
	private int runDirection = 0; // 0:l2r,1:r2l
	private float currIdleX = 0;
	private static final String TAG = "liuyang-bbkui";

	public PetFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		// setLastPage(2-1);
		petRunEndPosX = (getMeasuredWidth() - mLayoutWidth) / 2;
		setBackgroundResource(R.drawable.pet_bg);
		addViews(mContext);
	}

	public PetFurniture(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public PetFurniture(Context context) {
		this(context, null, 0);

	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAnimationObj() {
		return true;
	}

	@Override
	public void stopAnimation(int width) {
		// TODO Auto-generated method stub
		super.stopAnimation(width);
		Log.v(TAG, "stopAnimation--mCurrentPage:" + mCurrentPage + "--mLastPage:" + mLastPage);

		// petIsRunPause = true;
		currIdleX = getX();
		Log.v(TAG, "stopAnimation:curr_x=" + currIdleX);
		if ((anim != null) && anim.isRunning()) {
			Log.v(TAG, "stopAnimation--anim.cancel()");
			long time = anim.getCurrentPlayTime();
			Log.v(TAG, "stopAnimation--anim.getCurrentPlayTime()=" + anim.getCurrentPlayTime());
			runAllTime -= time;
			anim.cancel();
			Log.v(TAG, "stopAnimation--set-petIsRunEnd-false");
			petIsRunEnd = false;
		} else {
			Log.v(TAG, "stopAnimation--set-petIsRunEnd-true");
			petIsRunEnd = true;
		}

		if (petAnimation != null && petAnimation.isRunning()) {
			Log.v(TAG, "stopAnimation--petAnimation.stop()");
			petAnimation.stop();
		}

		img_pet.setImageResource(R.anim.pet_idle);
		petAnimation = (AnimationDrawable) img_pet.getDrawable();
		petAnimation.start();
	}

	@Override
	protected void updateOffset(int offset) {
		// TODO Auto-generated method stub
		super.updateOffset(offset);
		// currIdleX = getX();
		// Log.v(TAG, "updateOffset:offset="+offset+",currIdleX=="+currIdleX);
		setX(currIdleX - offset);
	}

	@Override
	public void onAnimation(int width) {
		super.onAnimation(width);
		Log.v(TAG, "onAnimation--mCurrentPage:" + mCurrentPage + "--mLastPage:" + mLastPage);
		if (mCurrentPage == mLastPage) {
			if (petIsRunEnd) {
				runAnimation(width);
			} else {
				runResume(width);
			}
		} else {
			if ((anim != null) && anim.isRunning()) {
				Log.v(TAG, "onAnimation--anim.cancel()");
				anim.cancel();
			}
			if (petAnimation != null && petAnimation.isRunning()) {
				Log.v(TAG, "onAnimation--petAnimation.stop()");
				petAnimation.stop();
			}
			runAnimation(width);
		}
	}

	private void runAnimation(int width) {
		Log.v(TAG, "runAnimation--mCurrentPage:" + mCurrentPage + "--mLastPage:" + mLastPage);
		// petIsRunPause = false;
		if (mCurrentPage == mLastPage) {
			Log.v(TAG, "runAnimation--petAnimation.isRunning=" + petAnimation.isRunning());
			// if (!petAnimation.isRunning()){
			Log.v(TAG, "runAnimation--R.anim.pet_stop--1");
			img_pet.setImageResource(R.anim.pet_stop);
			// }
		} else if (mCurrentPage > mLastPage) {
			Log.v(TAG, "runAnimation--pet_run_l2r");
			runDirection = 0;
			runAllTime = 3500;
			if (mCurrentPage == 4) {
				petRunEndPosX = (width - mLayoutWidth) / 2 + 80;
			} else {
				petRunEndPosX = (width - mLayoutWidth) / 2;
			}
			this.setX(-mLayoutWidth / 2);
			anim = ObjectAnimator.ofFloat(this, "X", (-mLayoutWidth / 2), petRunEndPosX);
			img_pet.setImageResource(R.anim.pet_run_l2r);
			anim.setInterpolator(new LinearInterpolator());
			anim.addListener(amtla);
			anim.setDuration(runAllTime);
			anim.start();
		} else if (mCurrentPage < mLastPage) {
			Log.v(TAG, "runAnimation--pet_run_r2l");
			runDirection = 1;
			runAllTime = 3500;
			if (mCurrentPage == 4) {
				petRunEndPosX = (width - mLayoutWidth) / 2 + 80;
			} else {
				petRunEndPosX = (width - mLayoutWidth) / 2;
			}
			this.setX(width - mLayoutWidth / 2);
			anim = ObjectAnimator.ofFloat(this, "X", width - mLayoutWidth / 2, petRunEndPosX);
			img_pet.setImageResource(R.anim.pet_run_r2l);
			anim.setInterpolator(new LinearInterpolator());
			anim.addListener(amtla);
			anim.setDuration(runAllTime);
			anim.start();
		}

		petAnimation = (AnimationDrawable) img_pet.getDrawable();
		petAnimation.start();
	}

	private void runResume(int width) {
		// petIsRunPause = false;
		if (mCurrentPage == 4) {
			petRunEndPosX = (width - mLayoutWidth) / 2 + 80;
		} else {
			petRunEndPosX = (width - mLayoutWidth) / 2;
		}
		anim = ObjectAnimator.ofFloat(this, "X",
		/* currX, */
		petRunEndPosX);
		if (runDirection == 0) {
			img_pet.setImageResource(R.anim.pet_run_l2r);
		} else if (runDirection == 1) {
			img_pet.setImageResource(R.anim.pet_run_r2l);
		}
		Log.v(TAG, "runResume:runAllTime:" + runAllTime);
		if (runAllTime < 0)
			runAllTime = 0;
		anim.addListener(amtla);
		anim.setDuration(runAllTime);
		anim.start();
		petAnimation = (AnimationDrawable) img_pet.getDrawable();
		petAnimation.start();
	}

	private void addViews(Context context) {
		mDrawable = (BitmapDrawable) getBackground();
		if (mDrawable != null) {
			// Bitmap bm = mDrawable.getBitmap();
			mLayoutWidth = mDrawable.getMinimumWidth();
		}

		img_pet = new ImageView(context);
		addView(img_pet);
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) img_pet.getLayoutParams();
		params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
		params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		img_pet.setLayoutParams(params);
		img_pet.setVisibility(View.VISIBLE);
	}

	@Override
	public void setSingleClickListener(OnClickListener listener) {
		super.setSingleClickListener(listener);
		if (img_pet != null) {
			img_pet.setOnClickListener(mSingleClickListener);
		}
	}

	private AnimatorListenerAdapter amtla = new AnimatorListenerAdapter() {

		@Override
		public void onAnimationCancel(Animator animation) {
			// TODO Auto-generated method stub
			super.onAnimationCancel(animation);
			Log.v(TAG, "AnimatorListenerAdapter--onAnimationCancel");
			petIsRunPause = true;
			currIdleX = getX();
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			// TODO Auto-generated method stub
			super.onAnimationEnd(animation);
			Log.v(TAG, "AnimatorListenerAdapter--onAnimationEnd");
			if (!petIsRunPause) {
				Log.v(TAG, "AnimatorListenerAdapter--petRunStop");
				petIsRunEnd = true;
				petRunStop();
			} else {
				petIsRunPause = false;
			}
			currIdleX = getX();
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
			super.onAnimationRepeat(animation);
		}

		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
			super.onAnimationStart(animation);
		}
	};

	private void petShowStopAni() {
		if (mCurrentPage == mLastPage) {
			Log.v(TAG, "petShowStopAni--R.anim.pet_stop--2");
			img_pet.setImageResource(R.anim.pet_stop);
		} else {
			Log.v(TAG, "petShowStopAni--R.anim.pet_stop--3");
			img_pet.setImageResource(R.anim.pet_stop);
		}

		petAnimation = (AnimationDrawable) img_pet.getDrawable();
		petAnimation.start();
	}

	private void petRunStop() {
		if (mCurrentPage == mLastPage) {
			// return;
		}

		petAnimation.stop();

		if (runDirection == 0) {
			img_pet.setImageResource(R.drawable.pet_run_l2r_stop);
		} else if (runDirection == 1) {
			img_pet.setImageResource(R.drawable.pet_run_r2l_stop);
		}

		postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				petShowStopAni();
			}
		}, 1000);
	}
}
