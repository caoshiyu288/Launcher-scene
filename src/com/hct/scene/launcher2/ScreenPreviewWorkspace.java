package com.hct.scene.launcher2;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class ScreenPreviewWorkspace extends ViewGroup {
	private int mDragViewPos = 0;

	private final Rect mRect = new Rect();

	private static final int ANIMATION_DURATION = 250;
	private static ItemsAnimation mItemsAnimation;

	private boolean mLockMovingFlag = false;

	private ImageView mAddingView;
	private int mMaxViewNum;
	ScreenPreview mParent = null;
	private boolean mNeedLayoutAfterAnimation = false;

	private static ArrayList<Integer> mOrder = new ArrayList<Integer>();
	private static int mMaxRows = 0, mMaxCols = 0;
	private static final int mMarginVer = 3, mMarginHor = 3;
	private static final int mMarginTop = 5, mMarginBottom = 5;
	private static int mImageOriginWidth = 0, mImageOriginHeight = 0;
	private static int mImageThumbWidth = 0, mImageThumbHeight = 0;
	private static final float ITEM_SCALE_MAX = 0.46f;

	private void imageResize() {
		int statusBarH = ScreenPreview.mStatusBarHeight;
		final Resources resources = getResources();
		int deleteZoneHeight = resources
				.getDimensionPixelSize(R.dimen.delete_zone_size);
		int height = mImageOriginHeight - statusBarH - deleteZoneHeight;
		float scale = 1.0f;

		height -= (mMarginTop + mMarginBottom + mMarginVer * (mMaxRows - 1));
		mImageThumbHeight = (int) (height / mMaxRows);
		scale = (float) mImageThumbHeight / (float) height;
		scale -= 0.02;

		if (scale > ITEM_SCALE_MAX) {
			scale = ITEM_SCALE_MAX;
			mImageThumbHeight = (int) (height * scale);
			scale -= 0.02;
		}

		mImageThumbWidth = (int) (mImageOriginWidth * scale);
	}

	public void resetOrder(int number) {
		mOrder.clear();

		if (number <= 2) {
			mOrder.add(number);

			mMaxRows = 1;
			mMaxCols = number;
		} else if ((number > 2) && (number <= 4)) {
			mOrder.add(number - 2);
			mOrder.add(2);

			mMaxRows = 2;
			mMaxCols = 2;
		} else if ((number > 4) && (number <= 6)) {
			mOrder.add(2);
			mOrder.add(number - (2 * 2));
			mOrder.add(2);

			mMaxRows = 3;
			mMaxCols = 2;
		} else if ((number > 6) && (number <= 9)) {
			int max = (int) Math.ceil((number - 3) / 2);

			mOrder.add(max);
			mOrder.add(3);
			mOrder.add(number - max - 3);

			mMaxRows = 3;
			mMaxCols = 3;
		}

		imageResize();
	}

	public static void setOriginViewWidth(int width) {
		mImageOriginWidth = width;
	}

	public static int getOriginViewWidth() {
		return mImageOriginWidth;
	}

	public static void setOriginViewHeight(int height) {
		mImageOriginHeight = height;
	}

	public static int getOriginViewHeight() {
		return mImageOriginHeight;
	}

	public ScreenPreviewWorkspace(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setVisibility(VISIBLE);

		mItemsAnimation = new ItemsAnimation();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// final int width = MeasureSpec.getSize(widthMeasureSpec);
		// final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int height = MeasureSpec.getSize(heightMeasureSpec);
		// final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		final Resources resources = getResources();
		int deleteZoneHeight = resources
				.getDimensionPixelSize(R.dimen.delete_zone_size);

		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height
				- deleteZoneHeight, MeasureSpec.AT_MOST));

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mLockMovingFlag = false;

		if (mItemsAnimation.isRunning())
			return;

		if (getChildCount() == 0)
			return;
		resetOrder(getChildCount());

		ArrayList<Integer> order = mOrder;
		int itemWidth = mImageThumbWidth;
		int itemHheight = mImageThumbHeight;

		if (order == null || order.size() == 0) {
			return;
		}

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i)
					.measure(
							MeasureSpec.makeMeasureSpec(itemWidth,
									MeasureSpec.EXACTLY),
							MeasureSpec.makeMeasureSpec(itemHheight,
									MeasureSpec.EXACTLY));
		}

		int childIndex = 0;
		View child = null;
		int startX = 0;
		int startY = mMarginTop;
		int width = 0;
		int height = 0;
		for (int i = 0; i < order.size(); i++) {
			LineSize size = getLineWidth(childIndex, childIndex + order.get(i)
					- 1);
			startX = (getMeasuredWidth() - size.mLineWidth) / 2;
			for (int j = 0; j < order.get(i); j++) {
				child = getChildAt(childIndex);
				childIndex++;
				width = child.getMeasuredWidth();
				height = child.getMeasuredHeight();
				child.layout(startX, startY, startX + width, startY + height);
				startX += width + mMarginHor;
			}

			startY += size.mLineHeight;
		}
	}

	private LineSize getLineWidth(int startIndex, int endIndex) {
		LineSize size = new LineSize();
		View child;
		int width = 0;
		int maxHeight = 0;
		for (int i = startIndex; i <= endIndex; i++) {
			child = getChildAt(i);
			width += child.getMeasuredWidth();
			width += mMarginHor;
			if (child.getMeasuredHeight() > maxHeight) {
				maxHeight = child.getMeasuredHeight();
			}
		}
		width -= mMarginHor;
		maxHeight += mMarginVer;
		size.mLineWidth = width;
		size.mLineHeight = maxHeight;
		return size;
	}

	private boolean addingSubViewExist() {
		int addingTag = mParent.getAddingViewTag();
		Integer tag;

		for (int i = 0; i < getChildCount(); i++) {
			tag = (Integer) getChildAt(i).getTag();

			if ((int) tag == addingTag)
				return true;
		}

		return false;
	}

	public void readOutOrder(ArrayList<Integer> orders) {
		orders.clear();

		for (int i = 0; i < getChildCount(); i++) {
			Integer tag = (Integer) getChildAt(i).getTag();

			if ((int) tag == mParent.getAddingViewTag()) {
				continue;
			}

			orders.add(tag);
		}
	}

	public void setAddingView(ImageView view) {
		mAddingView = view;
	}

	public void setMaxViewNum(int max) {
		mMaxViewNum = max;
	}

	public void setParent(ScreenPreview parent) {
		mParent = parent;
	}

	public void addSubView(ImageView child) {
		int count = getChildCount();

		if (count + 1 > mMaxViewNum) {
			copyImageView(child, (ImageView) getChildAt(count - 1));
		} else {
			addView(child);
			moveSubView(count - 1, count);
		}
	}

	public void resetSubViews(ArrayList<ImageView> subViewList) {
		removeAllViews();

		if (subViewList == null) {
			return;
		}

		for (int i = 0; i < subViewList.size(); i++) {
			ImageView imgView = subViewList.get(i);
			addView(imgView);
		}

		forceLayout();
	}

	public void resetSubViewsTag() {
		int addingTag = mParent.getAddingViewTag();
		Integer tag;

		for (int i = 0; i < getChildCount(); i++) {
			tag = (Integer) getChildAt(i).getTag();

			if ((int) tag == addingTag) {
				continue;
			}

			getChildAt(i).setTag(i);
		}
	}

	public void setDragView(View view) {
		mDragViewPos = indexOfChild(view);

		if (mDragViewPos == -1) {
			mDragViewPos = 0;

			return;
		}

		ImageView dragView = (ImageView) view;
		dragView.setAlpha(0.0f);
		dragView.getBackground().setAlpha(0);
	}

	public void setDragViewIndex(int index) {
		setDragView(getChildAt(index));
	}

	public int getDragViewIndex() {
		return mDragViewPos;
	}

	public ImageView getDrageView() {
		return (ImageView) getChildAt(mDragViewPos);
	}

	public void dragViewMoveTo(View view) {

		int index = indexOfChild(view);
		if (index >= 0) {
			dragViewMoveTo(index);
		}
	}

	public void dragViewMoveTo(int index) {
		if (mLockMovingFlag) {
			return;
		}

		if (mDragViewPos == index || mItemsAnimation.isRunning() == true) {
			return;
		}

		int from = mDragViewPos, to = index;
		mDragViewPos = index;

		ImageView fromitem = (ImageView) getChildAt(from);
		ImageView toItem = (ImageView) getChildAt(to);
		moveSubView(from, to);

		fromitem.setAlpha(1.0f);
		fromitem.getBackground().setAlpha(0xff);
		toItem.setAlpha(0.0f);
		toItem.getBackground().setAlpha(0);

		mItemsAnimation.startItemsAnimation(from, to);
	}

	public static void copyImageView(ImageView fromView, ImageView toView) {
		toView.setImageDrawable(fromView.getDrawable());
		toView.setBackgroundDrawable(fromView.getBackground());
		toView.setTag(fromView.getTag());
	}

	public void dragViewDrop(boolean isDelete) {

		ImageView dragView = (ImageView) getChildAt(mDragViewPos);

		if (isDelete) {
			if (addingSubViewExist()) {
				moveSubView(mDragViewPos, getChildCount() - 1);
				removeViewAt(getChildCount() - 1);
			} else {
				moveSubView(mDragViewPos, getChildCount() - 1);
				copyImageView(mAddingView,
						(ImageView) getChildAt(getChildCount() - 1));
			}
		}

		if (indexOfChild(dragView) >= 0) {
			dragView.setAlpha(1.0f);
			dragView.getBackground().setAlpha(0xff);
		}

		if (mItemsAnimation.isRunning()) {
			mNeedLayoutAfterAnimation = true;
		} else {
			requestLayout();
		}
	}

	public ImageView getHitChildView(int x, int y) {
		Rect childFrame = mRect;
		ImageView child;

		for (int i = 0; i < getChildCount(); i++) {
			child = (ImageView) getChildAt(i);
			child.getHitRect(childFrame);

			if (childFrame.contains(x, y)) {
				return child;
			}
		}

		return null;
	}

	private class LineSize {
		public int mLineWidth;
		public int mLineHeight;
	}

	private void moveSubView(int from, int to) {
		int step = 1;

		if (from > to) {
			step = -1;
		}

		ImageView fromitem = (ImageView) getChildAt(from);
		Drawable frombmp = fromitem.getDrawable();
		Drawable frombg = fromitem.getBackground();
		Integer fromTag = (Integer) fromitem.getTag();

		for (int i = from; i != to + step && i + step >= 0
				&& i + step < getChildCount(); i += step) {
			ImageView item = (ImageView) getChildAt(i);
			ImageView itemNext = (ImageView) getChildAt(i + step);
			item.setImageDrawable(itemNext.getDrawable());
			item.setBackgroundDrawable(itemNext.getBackground());
			item.setTag(itemNext.getTag());
		}
		ImageView toItem = (ImageView) getChildAt(to);
		toItem.setImageDrawable(frombmp);
		toItem.setBackgroundDrawable(frombg);
		toItem.setTag(fromTag);
	}

	private class ItemsAnimation {
		private int mIsRunning = 0;
		private int mDragFromIndex = -1;
		private int mDragToIndex = -1;

		public boolean isRunning() {
			if (mIsRunning == 0) {
				return false;
			} else {
				return true;
			}
		}

		private void setItemAnimation(int fromPos, int itemPos) {
			final int fromx = getChildAt(fromPos).getLeft();
			final int fromy = getChildAt(fromPos).getTop();
			final int tox = getChildAt(itemPos).getLeft();
			final int toy = getChildAt(itemPos).getTop();

			final int gapx = fromx - tox;
			final int gapy = fromy - toy;

			TranslateAnimation trans = new TranslateAnimation(
					Animation.ABSOLUTE, gapx, Animation.ABSOLUTE, 0,
					Animation.ABSOLUTE, gapy, Animation.ABSOLUTE, 0);
			trans.setDuration(ANIMATION_DURATION);
			trans.setAnimationListener(new ItemAnimation(getChildAt(itemPos)));

			getChildAt(itemPos).startAnimation(trans);
		}

		public void startItemsAnimation(int dragFromPos, int dragToPos) {
			if (dragFromPos == dragToPos) {
				return;
			}

			if (mIsRunning != 0) {
				return;
			}

			mDragFromIndex = dragFromPos;
			mDragToIndex = dragToPos;

			int from = mDragFromIndex;
			int to = mDragToIndex;

			int step = 1;

			if (to < from) {
				step = -1;
			}

			while (from != to) {
				mIsRunning++;
				setItemAnimation(from + step, from);
				from += step;
			}
		}

		private class ItemAnimation implements AnimationListener {
			public ItemAnimation(View target) {
			}

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				if (mIsRunning == 1) {
					if (mNeedLayoutAfterAnimation) {
						ScreenPreviewWorkspace.this.requestLayout();
						mNeedLayoutAfterAnimation = false;
					}
				}
				mIsRunning--;
			}
		}

	}
}