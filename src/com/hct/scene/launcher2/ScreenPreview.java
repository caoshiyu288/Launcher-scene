package com.hct.scene.launcher2;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hct.scene.launcher.R;

public class ScreenPreview extends LinearLayout implements
		View.OnLongClickListener {
	private Context mContext;
	private Launcher mLauncher;

	final private int mDuration = 400;
	final private float mMaxScale = 3.0F;
	final private int mAddingBmpTag = -1;
	final private int mMovingBmpTag = -2;

	private int mSelectIndex = -1;
	private int mScreenCountMax = 0;
	private static final int mScreenCountMin = 3;

	private boolean mMultiTouch = false;
	final private int mCoordinateOffset = 20;
	
	private int mOffsetY = 0;

	private PointF mPointDown1 = new PointF();
	private PointF mPointDown2 = new PointF();
	private PointF mPointUp1 = new PointF();
	private PointF mPointUp2 = new PointF();

	private boolean mMovingFlag = false;
	private float mMotionDownX;
	private float mMotionDownY;

	private ScreenPreviewWorkspace mSpWorkspace = null;
	private ScreenPreviewDeleteZone mSpDeletezone = null;
	private DragView mMovingBmpView = null;

	private int mAddingViewTagLast;
	private DisplayMetrics mDisplayMetrics = new DisplayMetrics();

	private boolean mFadeOutInAnimationFlag = false;

	public static int mStatusBarHeight = 25;
	private final Rect mRect = new Rect();

	public ScreenPreview(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	private class ItemClickListener implements OnClickListener {
		public void onClick(View v) {
			Integer tag = (Integer) v.getTag();

			if (mFadeOutInAnimationFlag) {
				return;
			}

			Log.v("HCTHCTHCT ItemClickListener", "HCTHCTHCT mAddingBmpTag = "
					+ mAddingBmpTag);

			if ((int) tag == mAddingBmpTag) {
				ImageView emptyView = createEmptyImageView();
				mSpWorkspace.addSubView(emptyView);

				updateLauncher();
			} else {
				mSelectIndex = tag;

				Log.v("HCTHCTHCT mSelectIndex = " + tag, "HCTHCTHCT tag = "
						+ tag);
				previewExit();
			}
		}
	}

	public boolean onLongClick(View v) {
		onMovingStart(v);

		return true;
	}

	private static int clamp(int val, int min, int max) {
		if (val < min) {
			return min;
		} else if (val >= max) {
			return max - 1;
		} else {
			return val;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();

		final int screenX = clamp((int) ev.getRawX(), 0,
				mDisplayMetrics.widthPixels);
		final int screenY = clamp((int) ev.getRawY(), 0,
				mDisplayMetrics.heightPixels);

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			mMovingFlag = false;
			mMotionDownX = screenX;
			mMotionDownY = screenY;
			break;
		}

		case MotionEvent.ACTION_POINTER_DOWN: {
			if (ev.getPointerCount() > 1) {
				mMultiTouch = true;
				mPointDown1.set(ev.getX(0), ev.getY(0));
				mPointDown2.set(ev.getX(1), ev.getY(1));
			}
			return true;
		}

		default: {
			if (mMovingFlag == true) {
				return true;
			}
		}
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_POINTER_DOWN: {
			return true;
		}

		case MotionEvent.ACTION_POINTER_UP: {
			if (event.getPointerCount() > 1) {
				mPointUp1.set(event.getX(0), event.getY(0));
				mPointUp2.set(event.getX(1), event.getY(1));

				int diffDownX = (int) Math.abs(mPointDown1.x - mPointDown2.x);
				int diffDownY = (int) Math.abs(mPointDown1.y - mPointDown2.y);
				int diffUpX = (int) Math.abs(mPointUp1.x - mPointUp2.x);
				int diffUpY = (int) Math.abs(mPointUp1.y - mPointUp2.y);

				if (((diffUpX - diffDownX) > mCoordinateOffset)
						&& ((diffUpY - diffDownY) > mCoordinateOffset)
						&& mMultiTouch == true) {
					previewExit();

					return true;
				}
			}
		}

		case MotionEvent.ACTION_MOVE: {
			onMoving((int) event.getRawX(), (int) event.getRawY() - mOffsetY);

			return true;
		}

		case MotionEvent.ACTION_UP: {
			onMovingExit((int) event.getRawX(), (int) event.getRawY() - mOffsetY);
			return true;
		}
		}
		/* when move action is interrupted, will case bug 20121122 */
		onMovingExit((int) event.getRawX(), (int) event.getRawY() - mOffsetY);

		return true;
	}

	private void previewReset() {
		this.setVisibility(View.GONE);

		mLauncher.hideAllView(false);
	}

	private Animation animLayoutFadeIn() {
		mFadeOutInAnimationFlag = true;
		AnimationSet animation = new AnimationSet(true);

		Animation animAlpha = new AlphaAnimation(0.5F, 1.0F);
		animAlpha.setDuration(mDuration);

		Animation animScale = new ScaleAnimation(mMaxScale, 1.0F, mMaxScale,
				1.0F, Animation.RELATIVE_TO_SELF, 0.5F,
				Animation.RELATIVE_TO_SELF, 0.5F);

		animScale.setDuration(mDuration);

		animation.addAnimation(animAlpha);
		animation.addAnimation(animScale);
		animation.setInterpolator(new LinearInterpolator());

		animation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				mFadeOutInAnimationFlag = false;
			}
		});

		return animation;
	}

	private Animation animLayoutFadeOut() {
		mFadeOutInAnimationFlag = true;
		AnimationSet animation = new AnimationSet(false);

		Animation animAlpha = new AlphaAnimation(1.0F, 0.0F);
		animAlpha.setDuration(mDuration);

		Animation animScale = new ScaleAnimation(1.0F, mMaxScale, 1.0F,
				mMaxScale, Animation.RELATIVE_TO_SELF, 0.5F,
				Animation.RELATIVE_TO_SELF, 0.5F);
		animScale.setDuration(mDuration);

		animation.addAnimation(animAlpha);
		animation.addAnimation(animScale);
		animation.setInterpolator(new LinearInterpolator());

		animation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				ScreenPreview.this.previewReset();

				if (-1 == mSelectIndex) {
					mSelectIndex = mLauncher.getWorkspace().getCurrentPage();
				}

				Log.v("HCTHCTHCT mSelectIndex", "HCTHCTHCT mSelectIndex = "
						+ mSelectIndex);

				mLauncher.getWorkspace(true).snapToPage(mSelectIndex);
				mSpWorkspace.resetSubViews(null);
				mFadeOutInAnimationFlag = false;
			}
		});

		return animation;
	}

	private Bitmap createEmptyBitmap() {
		final Bitmap bitmap = Bitmap.createBitmap(
				ScreenPreviewWorkspace.getOriginViewWidth(),
				ScreenPreviewWorkspace.getOriginViewHeight(),
				Bitmap.Config.ARGB_8888);
		return bitmap;
	}

	private ImageView createDefaultImgView() {
		ItemClickListener clickListener = new ItemClickListener();
		ImageView imgView;

		imgView = new ImageView(mContext);
		imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		imgView.setBackgroundResource(R.drawable.thumb_background_normal);
		imgView.setPadding(5, 5, 8, -5);
		imgView.setOnClickListener(clickListener);
		imgView.setOnLongClickListener(this);
		imgView.setVisibility(View.VISIBLE);

		return imgView;
	}

	private ImageView createEmptyImageView() {
		ImageView view = createDefaultImgView();

		Log.v("HCTHCTHCT createEmptyImageView",
				"HCTHCTHCT mAddingViewTagLast = " + mAddingViewTagLast);

		if (mAddingViewTagLast >= mLauncher.WORKSPACE_MAX_PAGE_NUM) {
			mAddingViewTagLast = mLauncher.WORKSPACE_MAX_PAGE_NUM - 1;
		}

		view.setTag(mAddingViewTagLast);
		mAddingViewTagLast++;

		return view;
	}

	private ImageView createAddingView() {
		Bitmap bmp;
		ImageView imgView;

		bmp = createEmptyBitmap();
		imgView = createDefaultImgView();
		imgView.setImageBitmap(bmp);
		imgView.setBackgroundResource(R.drawable.thumb_background_normal);
		imgView.setImageResource(R.drawable.thumb_adding_icon);
		Log.v("HCTHCTHCT createEmptyImageView", "HCTHCTHCT mAddingBmpTag = "
				+ mAddingBmpTag);

		imgView.setTag(mAddingBmpTag);

		return imgView;
	}

	private void createImageViews(ArrayList<Bitmap> bitmapThumb,
			ArrayList<ImageView> viewList) {
		viewList.clear();
		Bitmap bmp;
		ImageView imgView;
		for (int i = 0; i < bitmapThumb.size(); i++) {
			bmp = bitmapThumb.get(i);
			imgView = createDefaultImgView();
			imgView.setImageBitmap(bmp);
			imgView.setTag(i);
			viewList.add(imgView);
		}
	}

	public void showPreview(ArrayList<Bitmap> bitmapThumb, int screenCountMax) {
		mScreenCountMax = screenCountMax;
		mAddingViewTagLast = bitmapThumb.size() - 1;

		Log.v("HCTHCTHCT mAddingViewTagLast = " + mAddingViewTagLast,
				"HCTHCTHCT bitmapThumb = " + bitmapThumb.size());
		mSelectIndex = -1;
		mLauncher.hideAllView(true);

		mLauncher.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(mRect);
		mStatusBarHeight = mRect.top;

		ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();

		createImageViews(bitmapThumb, imageViewList);
		ImageView addingView = createAddingView();
		if (imageViewList.size() < mScreenCountMax) {
			ImageView newView = createEmptyImageView();
			ScreenPreviewWorkspace.copyImageView(addingView, newView);
			imageViewList.add(newView);
		}

		mSpDeletezone = (ScreenPreviewDeleteZone) findViewById(R.id.screenpreview_delete_zone);

		mSpWorkspace = (ScreenPreviewWorkspace) findViewById(R.id.screenpreview_workspace);
		mSpWorkspace.setAddingView(addingView);
		mSpWorkspace.setMaxViewNum(mScreenCountMax);
		mSpWorkspace.setParent(this);
		mSpWorkspace.resetSubViews(imageViewList);

		imageViewList.clear();

		this.setAnimation(animLayoutFadeIn());
		this.setVisibility(View.VISIBLE);
	}

	public void setLauncher(Launcher launcher) {
		mLauncher = launcher;

		Display mDisplay = mLauncher.getWindowManager().getDefaultDisplay();
		ScreenPreviewWorkspace.setOriginViewWidth(mDisplay.getWidth());
		ScreenPreviewWorkspace.setOriginViewHeight(mDisplay.getHeight());
	}

	public void previewExit() {
		this.startAnimation(animLayoutFadeOut());
	}

	public int getAddingViewTag() {
		Log.v("HCTHCTHCT mAddingBmpTag = " + mAddingBmpTag,
				"HCTHCTHCT mAddingBmpTag = " + mAddingBmpTag);
		return mAddingBmpTag;
	}

	private Bitmap getViewBitmap(View v) {
		v.clearFocus();
		v.setPressed(false);

		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);

		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);

		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null) {
			return null;
		}

		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(cacheBitmap);
		} catch (OutOfMemoryError err) {
		} finally {
			v.destroyDrawingCache();
			v.setWillNotCacheDrawing(willNotCache);
			v.setDrawingCacheBackgroundColor(color);
		}
		return bitmap;
	}

	private void createMovingBmp(ImageView imgView) {
		Bitmap bmp = getViewBitmap(imgView);
		mOffsetY = bmp.getHeight() >> 1 - mStatusBarHeight;

		int loc[] = new int[2];
		getLocationOnScreen(loc);
		int registrationX = ((int) mMotionDownX) - loc[0] + bmp.getWidth() / 2;
		int registrationY = ((int) mMotionDownY) - loc[1] + bmp.getHeight() / 2;
		DragView dragView = new DragView(mLauncher, bmp, registrationX,
				registrationY, 0, 0, bmp.getWidth(), bmp.getHeight());
		dragView.show((int) mMotionDownX, (int) mMotionDownX);

		mMovingBmpView = dragView;
		mMovingBmpView.setTag(mMovingBmpTag);
		mMovingBmpView.setVisibility(View.GONE);
	}

	private void onMovingStart(View v) {
		Integer tag = (Integer) v.getTag();

		if ((int) tag == mAddingBmpTag) {
			return;
		}

		createMovingBmp((ImageView) v);
		mSpWorkspace.setDragView(v);
		mMovingFlag = true;
	}

	private void onMoving(int x, int y) {
		if (mMovingBmpView == null) {
			return;
		}

		mMovingBmpView.setVisibility(View.VISIBLE);
		mMovingBmpView.move(x, y);

		ImageView hitView = mSpWorkspace.getHitChildView(x, y);
		if (hitView != null) {
			Integer tag = (Integer) hitView.getTag();
			if ((int) tag == mAddingBmpTag) {
				return;
			}

			mSpWorkspace.dragViewMoveTo(hitView);
		}

		if (mSpDeletezone.checkHit(x, y)) {
			if (mSpWorkspace.getChildCount() <= mScreenCountMin) {
				mSpDeletezone.onDragEnter(false);
			} else {
				mSpDeletezone.onDragEnter(true);
			}
		} else {
			mSpDeletezone.onDragExit();
		}
	}

	private boolean checkPageIsEmpty(View page) {
		Integer tag = (Integer) page.getTag();

		return mLauncher.checkPageIsEmpty((int) tag);
	}

	private void onMovingExit(int x, int y) {
		if (mMovingBmpView == null) {
			return;
		}

		mMovingBmpView.remove();
		mMovingBmpView = null;

		if (mSpDeletezone.enterd()
				&& mSpWorkspace.getChildCount() <= mScreenCountMin) {
			mSpWorkspace.dragViewDrop(false);
		} else if (mSpDeletezone.enterd()) {
			if (checkPageIsEmpty(mSpWorkspace.getDrageView())) {
				mSpWorkspace.dragViewDrop(true);
			} else {
				alertIfDeleteNotEmpty();
				mSpDeletezone.onDragDrop(null);

				return;
			}
		} else {
			mSpWorkspace.dragViewDrop(false);
		}

		mSpDeletezone.onDragDrop(null);

		updateLauncher();
		mSpWorkspace.resetSubViewsTag();
	}

	private void updateLauncher() {
		ArrayList<Integer> orders = new ArrayList<Integer>();
		mSpWorkspace.readOutOrder(orders);
		mLauncher.adjustAllPages(orders);
	}

	public void onActivityPause() {
		onMovingExit(0, 0);
	}

	/* 20120928, */
	private void alertIfDeleteNotEmpty() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

		builder.setMessage(mContext.getString(R.string.alart_msg_if_delete));
		builder.setCancelable(false);

		builder.setPositiveButton(mContext.getString(R.string.alart_yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mSpWorkspace.dragViewDrop(true);
						updateLauncher();
						mSpWorkspace.resetSubViewsTag();
					}
				});

		builder.setNegativeButton(mContext.getString(R.string.alart_no),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mSpWorkspace.dragViewDrop(false);
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}
}
