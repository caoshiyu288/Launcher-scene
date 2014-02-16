package com.hct.scene.launcher2;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ScreenPreviewDeleteZone extends ImageView {
	private boolean mEnteredFlag = false;

	private static final int TRANSITION_DURATION = 250;

	private TransitionDrawable mTransition;

	public ScreenPreviewDeleteZone(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mTransition = (TransitionDrawable) getDrawable();
		setVisibility(VISIBLE);
	}

	public boolean checkHit(int x, int y) {
		Rect frame = new Rect();

		getHitRect(frame);

		if (y > getTop()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean enterd() {
		return mEnteredFlag;
	}

	public void onDragEnter(boolean allowDelete) {
		if (!allowDelete) {
			return;
		}

		if (!mEnteredFlag) {
			mTransition.resetTransition();
			mTransition.reverseTransition(TRANSITION_DURATION);
			mEnteredFlag = true;
		}
	}

	public void onDragExit() {
		if (mEnteredFlag) {
			mTransition.reverseTransition(TRANSITION_DURATION);
			mTransition.resetTransition();
			mEnteredFlag = false;
		}
	}

	public void onDragDrop(View dragView) {
		if (mEnteredFlag) {
			mTransition.reverseTransition(TRANSITION_DURATION);
			mTransition.resetTransition();
			mEnteredFlag = false;
		}
	}
}