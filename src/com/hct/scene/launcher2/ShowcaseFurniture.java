package com.hct.scene.launcher2;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class ShowcaseFurniture extends Furniture {
	private ImageView mCover;

	private boolean mHasMeasured = false;
	private int mWindowWidth = 0;

	private int mImageIdCover;
	private int mOffsetX = 30;

	private LayoutInflater mLayoutInflater;

	enum WindowState {
		WINDOW_NORMAL, WINDOW_OPEN, WINDOW_CLOSE, WINDOW_SLIDE
	};

	private ValueAnimator mAnimOpen, mAnimClose;
	private ValueAnimator mAnimSlideOpen, mAnimSlideClose;
	private AnimatorSet mAnimatorSetSlide;

	public ShowcaseFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// TODO Auto-generated constructor stub
		mLayoutInflater = LayoutInflater.from(context);
		View view = mLayoutInflater.inflate(R.layout.scene_showcase, null);
		addView(view);

		mCover = (ImageView) view.findViewById(R.id.imgCover);

		mCover.setOnClickListener(this);

		ViewTreeObserver viewTree = getViewTreeObserver();
		viewTree.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				if (!mHasMeasured) {
					mHasMeasured = true;

					mWindowWidth = getMeasuredWidth();
					setupAnimation();
				}

				return true;
			}
		});
	}

	public ShowcaseFurniture(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ShowcaseFurniture(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isAnimationObj() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void stopAnimation(int width) {
		if (mAnimatorSetSlide != null && mAnimatorSetSlide.isRunning()) {
			mAnimatorSetSlide.cancel();
		}
	}

	@Override
	public void onAnimation(int width) {
		// TODO Auto-generated method stub
		startAnimation(WindowState.WINDOW_SLIDE);
	}

	@Override
	public void callbackAnimation() {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (mZoomMode) {
			mSingleClickListener.onClick(v);
		} else if (mAddApps != null && mAddApps.isRefused()) {
			return;
		} else {
			startAnimation(WindowState.WINDOW_OPEN);
		}
	}

	@Override
	public void changeStyle(int style) {
		// TODO Auto-generated method stub
		if (0 == style) {
			mImageIdCover = R.drawable.scene_wind_showcase_cover;
		} else if (1 == style) {
			mImageIdCover = R.drawable.scene_princess_showcase_cover;
		}

		if (0 != mImageIdCover) {
			mCover.setImageResource(mImageIdCover);
		}

		mCover.setOnLongClickListener(mLongClickListener);
	}

	public void startAnimation(WindowState state) {
		switch (state) {
		case WINDOW_OPEN: {
			mAnimOpen.cancel();
			mAnimOpen.start();

			break;
		}

		case WINDOW_CLOSE: {
			mAnimClose.cancel();
			mAnimClose.start();

			break;
		}

		case WINDOW_SLIDE: {
			mAnimatorSetSlide.cancel();
			mAnimatorSetSlide.start();

			break;
		}
		}
	}

	private void setupAnimation() {
		if (null == mAnimatorSetSlide) {
			mAnimatorSetSlide = new AnimatorSet();
		}

		mAnimOpen = ObjectAnimator.ofFloat(mCover, "x", 0, mWindowWidth).setDuration(400);
		mAnimOpen.addUpdateListener(new AnimationUpdate());
		mAnimOpen.addListener(new AnimationEnd());

		mAnimClose = ObjectAnimator.ofFloat(mCover, "x", mWindowWidth, 0).setDuration(400);
		mAnimClose.addUpdateListener(new AnimationUpdate());

		mAnimSlideOpen = ObjectAnimator.ofFloat(mCover, "x", 0, mOffsetX).setDuration(400);
		mAnimSlideOpen.addUpdateListener(new AnimationUpdate());

		mAnimSlideClose = ObjectAnimator.ofFloat(mCover, "x", mOffsetX, 0).setDuration(1500);
		mAnimSlideClose.addUpdateListener(new AnimationUpdate());
		mAnimSlideClose.setInterpolator(new BounceInterpolator());

		mAnimatorSetSlide.play(mAnimSlideOpen).before(mAnimSlideClose);
	}

	class AnimationUpdate implements ValueAnimator.AnimatorUpdateListener {
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			// TODO Auto-generated method stub
		}
	}

	class AnimationEnd implements AnimatorListener {
		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			// TODO Auto-generated method stub
			mAddApps.setView(ShowcaseFurniture.this);
			mAddApps.run();
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
		Rect rect = new Rect();

		rect.left = mCover.getLeft();
		rect.top = mCover.getTop();
		rect.right = mCover.getRight();
		rect.bottom = mCover.getBottom();

		return rect;
	}
}
