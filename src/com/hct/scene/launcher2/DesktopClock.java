package com.hct.scene.launcher2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

public class DesktopClock extends FrameLayout {
	private static final int STYLEONE = 0;

	private static final int STYLETWO = 1;

	private ClockHead mClockHead;

	private BobView mBobView;

	private AnimatorSet ainSet;

	public DesktopClock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public DesktopClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public DesktopClock(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		mBobView = new BobView(context);
		LayoutParams bobParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.TOP
				| Gravity.CENTER_HORIZONTAL);
		bobParams.topMargin = 275;
		addView(mBobView, bobParams);
		mBobView.setVisibility(View.INVISIBLE);

		mClockHead = new ClockHead(context);
		LayoutParams clockHead = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
		addView(mClockHead, clockHead);
		// mClockHead.setOnClickListener(this);

		mBobView.setPivotX(34.5f);
		mBobView.setPivotY(0);
		ObjectAnimator aniOne = ObjectAnimator.ofFloat(mBobView, "rotation", 0, 15);
		ObjectAnimator aniTwo = ObjectAnimator.ofFloat(mBobView, "rotation", 15, -15);
		ObjectAnimator aniThr = ObjectAnimator.ofFloat(mBobView, "rotation", -15, 5);
		ObjectAnimator aniFor = ObjectAnimator.ofFloat(mBobView, "rotation", 5, 0);
		aniOne.setDuration(500);
		aniTwo.setDuration(500);
		aniTwo.setStartDelay(500);
		aniThr.setDuration(500);
		aniThr.setStartDelay(1000);
		aniFor.setDuration(500);
		ainSet = new AnimatorSet();
		ainSet.play(aniFor).after(aniThr).after(aniTwo).after(aniOne);
	}

	public void setClickListener(View.OnClickListener listener) {
		mClockHead.setOnClickListener(listener);
	}

	public void setLongClickListener(View.OnLongClickListener listener) {
		mClockHead.setOnLongClickListener(listener);
	}

	public void startAnimation() {
		mClockHead.startAnimation();
		ainSet.cancel();
		ainSet.start();
	}

	public void stopAnimation() {
		mClockHead.stopAnimation();
		ainSet.cancel();
	}

	public void changeStyle(int style) {
		if (style == STYLEONE) {
			mBobView.setVisibility(View.INVISIBLE);
		} else if (style == STYLETWO) {
			mBobView.setVisibility(View.VISIBLE);
		}

		mClockHead.changeStyle(style);
	}
}
