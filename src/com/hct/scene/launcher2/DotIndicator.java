package com.hct.scene.launcher2;

import com.hct.scene.launcher.R;

import android.R.color;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DotIndicator extends LinearLayout implements OnClickListener, View.OnLongClickListener {
	private static final String TAG = "com.hct.scene.launcher2.DotIndicator";
	private int mDotCount = 0;
	private int mDotMargin;
	private int mDotPadding;
	private int mFocusDot = -1;
	private AnimatorSet mScaleAnimation;
	private PagedView mPagedView;
	private DragLayer mDragLayer;
	private Rect mTempRect = new Rect();
	private final int[] mCoordinatesTemp = new int[2];
	private static final int INVALID_POINTER = -1;
	private int mActivePointerId = INVALID_POINTER;
	private float mDownMotionX;
	private float mLastMotionX;
	private float mLastMotionY;
	private float mLastMotionXRemainder;
	private float mTotalMotionX;
	private int mTouchX;
	private DotGesture mDotGesture = new DotGesture();

	private enum Style {
		DOT, SLIDE_BLOCK
	};

	private Style mStyle = Style.DOT;
	private float mTouchSlop;
	private Drawable mSlideBlockBg;
	// private Bitmap mSlideBlockBg;
	private Drawable mDotBlock;
	private int mSlideBlockHeight;
	private int mPagedViewScaleTotalWidth;
	private int mPagedViewCutWidth = 0;
	private boolean mCancelClick;

	private static final float PAGEDVIEW_OVER_SCROLL = 0.0f;
	private VelocityTracker mVelocityTracker;
	private boolean mLongPress;

	public DotIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDotMargin = context.getResources().getDimensionPixelSize(R.dimen.dot_inner_margin);
		mDotPadding = context.getResources().getDimensionPixelSize(R.dimen.dot_inner_padding);
		mSlideBlockHeight = context.getResources().getDimensionPixelSize(R.dimen.dot_slide_block_bg_height);
		mSlideBlockBg = getResources().getDrawable(R.drawable.dot_indicator_background);
		// mSlideBlockBg = BitmapFactory.decodeResource(getResources(),
		// R.drawable.dot_indicator_background);
		mDotBlock = getResources().getDrawable(R.drawable.dot_block);
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop() * 0.2f;
		// mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
	}

	public DotIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DotIndicator(Context context) {
		this(context, null, 0);
	}

	public void setPagedView(PagedView view) {
		mPagedView = view;
	}

	public void setDragLayer(DragLayer dragLayer) {
		mDragLayer = dragLayer;
	}

	public void updateAllDots(int count, int currentPage) {
		if (mDotCount == count)
			return;
		Log.d("furniture", "updateAllDots-count-currentPage-" + count + "," + currentPage);
		this.removeAllViews();
		mDotCount = 0;
		// mDotCount = count;
		for (int i = 0; i < count; i++) {
			addDot();
		}
		setFocusDot(currentPage);
	}

	public void resetLayout(int count, int currentPage) {
		// removeAllViewsInLayout();
		Log.d("furniture", "resetLayout-count-currentPage-" + count + "," + currentPage);
		removeAllViews();
		mDotCount = 0;
		for (int i = 0; i < count; i++) {
			addDot();
		}
		setFocusDot(currentPage);
		// requestLayout();
		// invalidate();
	}

	public void adjustLayout(int bottomMargin) {
		FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) this.getLayoutParams();
		param.bottomMargin = bottomMargin;
		setLayoutParams(param);
	}

	private void setFocusDot(int currentPage) {
		scaleDot(-1, currentPage, false);
		mFocusDot = currentPage;
	}

	public void updateFocusDot(int nextId, boolean doubleDot) {
		if (nextId == -1)
			return;
		scaleDot(mFocusDot, nextId, doubleDot);
		mFocusDot = nextId;
	}

	// public void restoreAllDots(int currentPage){
	// if(mFocusDot == currentPage) return;
	// final int count = getChildCount();
	// for (int i = 0; i < count; i++) {
	// Dot d = (Dot) getChildAt(i);
	// d.setScaleX(1.0f);
	// d.setScaleY(1.0f);
	// }
	// }
	private Dot initDot(int id) {
		Dot d = new Dot(getContext());
		d.setImageResource(R.drawable.ic_dot);
		d.setOnClickListener(this);
		d.setOnLongClickListener(this);
		d.setId(id);
		return d;
	}

	private void addDot(Dot d) {
		this.addView(d);
		LayoutParams params = (LayoutParams) d.getLayoutParams();
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER_VERTICAL;
		params.setMargins(mDotMargin, 0, mDotMargin, 0);
		d.setLayoutParams(params);
	}

	public void addDot(int id) {
		addDot(initDot(id));
	}

	public void addDot() {
		addDot(initDot(mDotCount));
		mDotCount++;
	}

	public void removeDot() {
		this.removeViewAt(mDotCount - 1);
		mDotCount--;
	}

	@Override
	public void onClick(View v) {
		if (isCancelClick()) {
			return;
		}
		if (v instanceof Dot) {
			Dot d = (Dot) v;
			final int id = v.getId();
			if (mFocusDot == id)
				return;
			mPagedView.snapToPage(id);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (isCancelClick()) {
			return false;
		}
		mLongPress = true;
		return true;
	}

	private int findDot(int x, int y) {
		int[] coordinates = mCoordinatesTemp;
		Rect r = mTempRect;
		mDragLayer.getLocationInDragLayer(this, coordinates);
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			v.getHitRect(r);
			r.offset(coordinates[0], coordinates[1]);
			if (r.contains(x, y)) {
				return i;
			}
		}
		return -1;
	}

	private void acquireVelocityTrackerAndAddMovement(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
	}

	private void releaseVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		Log.i(TAG, "DotIndicator--onInterceptTouchEvent=" + action);
		// acquireVelocityTrackerAndAddMovement(ev);
		if (getChildCount() <= 0) {
			return super.onInterceptTouchEvent(ev);
		}
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mDownMotionX = mLastMotionX = ev.getX();
			mLastMotionXRemainder = 0;
			mTotalMotionX = 0;
			mActivePointerId = ev.getPointerId(0);
			Log.d(TAG, "onInterceptTouchEvent-down--mDownMotionX==" + mDownMotionX + ",mActivePointerId=" + mActivePointerId);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mActivePointerId != INVALID_POINTER) {
				determineScrollingStart(ev);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			changeStyle(Style.DOT);
			mActivePointerId = INVALID_POINTER;
			// releaseVelocityTracker();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			break;
		default:
			break;
		}

		return mStyle != Style.DOT;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.i(TAG, "DotIndicator--onTouchEvent=" + ev.getAction());
		if (getChildCount() <= 0) {
			return super.onTouchEvent(ev);
		}
		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mDownMotionX = mLastMotionX = ev.getX();
			mLastMotionXRemainder = 0;
			mTotalMotionX = 0;
			mActivePointerId = ev.getPointerId(0);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mStyle == Style.SLIDE_BLOCK) {
				final int pointerIndex = ev.findPointerIndex(mActivePointerId);
				final float x = ev.getX(pointerIndex);
				final float deltaX = mLastMotionX + mLastMotionXRemainder - x;
				mTotalMotionX += Math.abs(deltaX);
				if (Math.abs(deltaX) >= 1.0f) {
					// mTouchX -= deltaX;
					mTouchX = Math.round(x);
					invalidate();
					int toX;
					if (limitSlide()) {
						toX = (mTouchX - mDotBlock.getIntrinsicWidth() / 2) * proportionPagedViewWithIndicator() + getCutParts();
						// mPagedView.scrollBy(
						// -(proportionPagedViewWithIndicator()* mTouchX), 0);
						Log.w(TAG, ", new scrollx==" + mPagedView.getScrollX() + ", mTouchX==" + mTouchX);
					} else if (mTouchX < mDotBlock.getIntrinsicWidth() / 2) {
						toX = 0 + getCutParts();
					} else {
						toX = mPagedViewScaleTotalWidth;
					}
					mPagedView.scrollTo(toX, 0);
					Log.d(TAG, ",onTouchEvent--x==" + x + ", deltax==" + deltaX + ",proportionPagedViewWithIndicator()=="
							+ proportionPagedViewWithIndicator());
					mLastMotionX = x;
					mLastMotionXRemainder = deltaX - Math.round(deltaX);
				} else {
					mPagedView.awakenScrollBars();
				}
			} else {
				determineScrollingStart(ev);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mStyle == Style.SLIDE_BLOCK) {
				final int activePointerId = mActivePointerId;
				final int pointerIndex = ev.findPointerIndex(activePointerId);
				final int x = Math.round(ev.getX(pointerIndex));
				final int y = Math.round(ev.getY(pointerIndex));
				mTotalMotionX += Math.abs(mLastMotionX + mLastMotionXRemainder - x);

				if (true) {
					mPagedView.setDotIndSnapPage(true);
					mPagedView.snapToDestination();
				}
			}
			changeStyle(Style.DOT);
			mActivePointerId = INVALID_POINTER;
			// releaseVelocityTracker();
			break;
		case MotionEvent.ACTION_CANCEL:
			changeStyle(Style.DOT);
			mActivePointerId = INVALID_POINTER;
			// releaseVelocityTracker();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			break;
		default:
			break;
		}

		return true;
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == mActivePointerId) {
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mLastMotionX = mDownMotionX = ev.getX(newPointerIndex);
			mLastMotionY = ev.getY(newPointerIndex);
			mLastMotionXRemainder = 0;
			mActivePointerId = ev.getPointerId(newPointerIndex);
		}
	}

	public void setCutParts(int cut) {
		mPagedViewCutWidth = cut;
	}

	private int getCutParts() {
		return mPagedViewCutWidth;
	}

	public void cancelClick(boolean flag) {
		mCancelClick = flag;
	}

	private boolean isCancelClick() {
		return mCancelClick;
	}

	private void determineScrollingStart(MotionEvent ev) {
		final int pointerIndex = ev.findPointerIndex(mActivePointerId);
		final float x = ev.getX(pointerIndex);

		if (mLongPress) {
			mLongPress = false;
			// mPagedView.scalePagedView(TRANSITION_SCALE_FACTOR);
			mPagedViewScaleTotalWidth = getPagedViewSlideWidth() - getCutParts();
			mTouchX = Math.round(x);
			Log.d(TAG, "determineScrollingStart-x==" + x + ", getLeft()==" + getLeft() + ",mTouchX==" + mTouchX
					+ ",mPagedView.getWidth()==" + mPagedView.getWidth() + ",mPagedView.getMeasureWidth()=" + mPagedView.getMeasuredWidth()
					+ ", spaceing==" + mPagedView.mPageSpacing + ", getRawX()==" + ev.getRawX());
			mTotalMotionX += Math.abs(mLastMotionX - x);
			mLastMotionX = x;
			mLastMotionXRemainder = 0;
			int toX;
			if (limitSlide()) {
				toX = (mTouchX - mDotBlock.getIntrinsicWidth() / 2) * proportionPagedViewWithIndicator() + getCutParts();
			} else if (mTouchX < mDotBlock.getIntrinsicWidth() / 2) {
				toX = 0 + getCutParts();
			} else {
				toX = mPagedViewScaleTotalWidth;
			}
			Log.d(TAG, ", toX===" + toX);
			changeStyle(Style.SLIDE_BLOCK);
			// mPagedView.onPageBeginMoving();
			mPagedView.scrollTo(toX, 0);
		}

	}

	private int getPagedViewSlideWidth() {
		return (mPagedView.getChildCount() - 1) * mPagedView.getWidth() + 2 * getPagedViewOverScroll();
	}

	private int proportionPagedViewWithIndicator() {
		return (mPagedViewScaleTotalWidth) / (getWidth() - mDotBlock.getIntrinsicWidth() - mDotBlock.getIntrinsicWidth() / 2);
	}

	private int getPagedViewOverScroll() {
		return Math.round(mPagedView.getWidth() * PAGEDVIEW_OVER_SCROLL);
	}

	private boolean limitSlide() {
		return mTouchX >= (mDotBlock.getIntrinsicWidth() / 2) && mTouchX < (getWidth() - mDotBlock.getIntrinsicWidth());
	}

	private void scaleDot(int fromId, int toId, final boolean doubleAni) {
		Log.i(TAG, "fromId ==" + fromId + ", toId==" + toId);
		if (fromId == toId)
			return;
		if (mScaleAnimation != null) {
			mScaleAnimation.cancel();
			mScaleAnimation = null;
		}
		final View fromDot = getChildAt(fromId);
		final boolean isFromDotExit = fromDot != null;
		final View toDot = getChildAt(toId);
		final boolean isToDotExit = toDot != null;
		final float maxScaleX = 1.8f;
		final float maxScaleY = 1.2f;
		ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(250);
		scaleAnimator.addUpdateListener(new LauncherAnimatorUpdateListener() {
			@Override
			void onAnimationUpdate(float a, float b) {
				if (doubleAni && isFromDotExit) {
					fromDot.setScaleX(a * maxScaleX + b * 1.0f);
					fromDot.setScaleY(a * maxScaleY + b * 1.0f);
				}
				if (isToDotExit) {
					toDot.setScaleX(a * 1.0f + b * maxScaleX);
					toDot.setScaleY(a * 1.0f + b * maxScaleY);
				}
			}
		});

		scaleAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationCancel(Animator animation) {
				if (doubleAni && isFromDotExit) {
					fromDot.setScaleX(1.0f);
					fromDot.setScaleY(1.0f);
				}

				if (isToDotExit) {
					toDot.setScaleX(maxScaleX);
					toDot.setScaleY(maxScaleY);
				}
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (true) {
					if (doubleAni && isFromDotExit) {
						fromDot.setScaleX(1.0f);
						fromDot.setScaleY(1.0f);
					}
					if (isToDotExit) {
						toDot.setScaleX(maxScaleX);
						toDot.setScaleY(maxScaleY);
					}
				}
			}

			@Override
			public void onAnimationStart(Animator animation) {
				if (doubleAni && isFromDotExit) {
					fromDot.setScaleX(maxScaleX);
					fromDot.setScaleY(maxScaleY);
				}
				if (isToDotExit) {
					toDot.setScaleX(1.0f);
					toDot.setScaleY(1.0f);

				}
			}

		});

		mScaleAnimation = new AnimatorSet();
		mScaleAnimation.play(scaleAnimator).after(50);
		mScaleAnimation.start();
	}

	private void hideAllDots() {
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			v.setVisibility(INVISIBLE);
		}
	}

	private void unhideAllDots() {
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			v.setVisibility(VISIBLE);
		}
	}

	private void changeStyle(Style style) {
		if (style == mStyle)
			return;
		final Style oldStyle = mStyle;
		final boolean oldStyleIsDot = (oldStyle == Style.DOT);
		final boolean oldStyleIsSlideBlock = (oldStyle == Style.SLIDE_BLOCK);
		mStyle = style;
		boolean isDotStyle = (style == Style.DOT);
		boolean isSlideBlockStyle = (style == Style.SLIDE_BLOCK);
		if (oldStyleIsDot && isSlideBlockStyle) {
			hideAllDots();
			mPagedView.showPageNumber(true);
		} else if (oldStyleIsSlideBlock && isDotStyle) {
			unhideAllDots();
			mPagedView.showPageNumber(false);
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		Log.d(TAG, "dispatchDraw~~");
		if (mStyle == Style.SLIDE_BLOCK) {
			canvas.save();
			final Drawable bg = mSlideBlockBg;// getResources().getDrawable(R.drawable.dot_indicator_background);
			final int startY = (getHeight() - mSlideBlockHeight) / 2;
			bg.setBounds(0, startY, getWidth(), startY + mSlideBlockHeight);
			bg.draw(canvas);
			canvas.restore();
			Log.d(TAG, "dispatchDraw--getWidth==" + getWidth() + ",getMeasureWidth==" + getMeasuredWidth() + ", getChild(0).getWidth=="
					+ getChildAt(0).getWidth() + ",mTouchX==" + mTouchX + ", block-width==" + mDotBlock.getIntrinsicWidth());
			final Drawable d = mDotBlock;// getResources().getDrawable(R.drawable.dot_block);
			final int blockWidth = d.getIntrinsicWidth();
			final int blockHeight = d.getIntrinsicHeight();
			int x;
			if (limitSlide()) {
				x = mTouchX - blockWidth / 2;
			} else if (mTouchX < mDotBlock.getIntrinsicWidth() / 2) {
				x = 0;
			} else {
				x = getWidth() - blockWidth;
			}
			d.setBounds(x, startY + 1, x + blockWidth, startY + mSlideBlockHeight - 1);
			d.draw(canvas);
		}

	}

	class Dot extends ImageView {
		public Dot(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			setParams();
		}

		public Dot(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		public Dot(Context context) {
			this(context, null, 0);
		}

		public void setParams() {
			this.setPadding(mDotPadding, 10, mDotPadding, 10);
		}
	}

	class DotGesture extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			final int activePointerId = mActivePointerId;
			final int pointerIndex = e.findPointerIndex(activePointerId);
			final int x = Math.round(e.getX(pointerIndex));
			final int y = Math.round(e.getY(pointerIndex));
			int id = findDot(x, y);
			if (id != -1) {
				if (mFocusDot == id)
					return false;
				mPagedView.snapToPage(id);
			}
			changeStyle(Style.DOT);
			mActivePointerId = INVALID_POINTER;
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			changeStyle(Style.SLIDE_BLOCK);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			Log.i(TAG, "onScroll--distanceX==" + distanceX + ",distanceY==" + distanceY);
			changeStyle(Style.SLIDE_BLOCK);
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			mActivePointerId = e.getPointerId(0);
			return true;
		}

	}
}
