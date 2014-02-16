package com.hct.scene.launcher2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.hct.scene.launcher.R;

// create by hct lijunyi, 20130129

public class BooksDisplay extends ViewGroup {
	private Indicator mIndicator;

	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	private static final int TOUCH_STATE_REST = 0x0;
	private static final int TOUCH_STATE_SCROLLING = 0x1;

	private static final int SNAP_VELOCITY = 150;

	private int mTouchState = TOUCH_STATE_REST;
	private float mLastMotionX;

	private int mTouchOffset;
	private AppsSetObserver mAppsSetObserver;

	private ListAdapter mAppsSetAdaper;

	private AppsAdapter mAppsAdapter;
	private ArrayList<ApplicationInfo> mAllAppsList = new ArrayList<ApplicationInfo>();

	public BooksSelector mParent;

	protected IconCache mIconCache;

	public BooksDisplay(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		this(context, attrs, 0);
	}

	public BooksDisplay(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

		initView(context);
	}

	public void setLayoutParent(BooksSelector view) {
		mParent = view;
	}

	private void initView(Context context) {
		mScroller = new Scroller(context);

		mTouchOffset = ViewConfiguration.get(getContext()).getScaledTouchSlop();

		mAppsAdapter = new AppsAdapter(getContext(), mAllAppsList);
		mAppsAdapter.setNotifyOnChange(false);

		LauncherApplication app = (LauncherApplication) context.getApplicationContext();
		mIconCache = app.getIconCache();
	}

	private void initIndicator() {
		ViewGroup parent = (ViewGroup) getParent();
		ViewGroup pparent = (ViewGroup) parent.getParent();

		mIndicator = (Indicator) pparent.findViewById(R.id.pageIndicator);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int childLeft = 0;
		int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);

			if (childView.getVisibility() != View.GONE) {
				int childWidth = childView.getMeasuredWidth();

				childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());

				childLeft += childWidth;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
		}

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		scrollTo(Bookstore.mPageIndex * width, 0);

		initIndicator();
		mIndicator.changeState(Bookstore.mPageIndex + 1, Bookstore.mPageMaxNumber);
	}

	public void snapToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		if (getScrollX() != (whichScreen * getWidth())) {
			final int delta = whichScreen * getWidth() - getScrollX();

			mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
			Bookstore.mPageIndex = whichScreen;
			invalidate();

			mIndicator.changeState(Bookstore.mPageIndex + 1, Bookstore.mPageMaxNumber);
		}
	}

	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;

		snapToScreen(destScreen);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (mTouchState == TOUCH_STATE_SCROLLING) {
			return false;
		}

		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();

		switch (action) {
		case MotionEvent.ACTION_MOVE: {
			final int xDiff = (int) Math.abs(mLastMotionX - x);

			if (xDiff > mTouchOffset) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}

			break;
		}

		case MotionEvent.ACTION_DOWN: {
			mLastMotionX = x;

			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
			break;
		}

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		}

		return mTouchState != TOUCH_STATE_REST;
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}

		mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			mLastMotionX = x;

			break;
		}

		case MotionEvent.ACTION_UP: {
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(500);
			int velocityX = (int) velocityTracker.getXVelocity();

			if (velocityX > SNAP_VELOCITY && Bookstore.mPageIndex > 0) {
				Bookstore.mPageIndex--;

				snapToScreen(Bookstore.mPageIndex);
			} else if (velocityX < -SNAP_VELOCITY && Bookstore.mPageIndex < getChildCount() - 1) {
				Bookstore.mPageIndex++;

				snapToScreen(Bookstore.mPageIndex);
			} else {
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}

			mTouchState = TOUCH_STATE_REST;

			break;
		}

		case MotionEvent.ACTION_CANCEL: {
			mTouchState = TOUCH_STATE_REST;

			break;
		}
		}

		return true;
	}

	private int findAppByComponent(ArrayList<ApplicationInfo> list, ApplicationInfo item) {
		ComponentName component = item.intent.getComponent();
		final int N = list.size();

		for (int i = 0; i < N; i++) {
			ApplicationInfo x = list.get(i);

			if (x.intent.getComponent().equals(component)) {
				return i;
			}
		}

		return -1;
	}

	public void setApps(ArrayList<ApplicationInfo> list) {
		mAllAppsList.clear();
		addApps(list);
	}

	public Comparator<ItemInfo> CELLLAYOUT_COMPARATOR = new Comparator<ItemInfo>() {
		@Override
		public int compare(ItemInfo lhs, ItemInfo rhs) {
			// TODO Auto-generated method stub
			if (lhs.cellY != rhs.cellY) {
				return lhs.cellY - rhs.cellY;
			} else {
				return lhs.cellX - rhs.cellX;
			}
		}
	};

	public void reorderApps() {
		if (Bookstore.mCancel) {
			Bookstore.mSelectIndex = LauncherModel.sSceneBookshelfItems.size();
		} else {
			Bookstore.mCancel = true;

			ArrayList<ApplicationInfo> mDefault = new ArrayList<ApplicationInfo>();

			Collections.sort(mAllAppsList, LauncherModel.APP_NAME_COMPARATOR);
			Collections.sort(LauncherModel.sSceneBookshelfItems, CELLLAYOUT_COMPARATOR);

			for (ItemInfo info : LauncherModel.sSceneBookshelfItems) {
				Intent intent = ((ShortcutInfo) info).intent;

				String className = intent.getComponent().getClassName();
				String packageName = intent.getComponent().getPackageName();

				for (ApplicationInfo item : mAllAppsList) {
					if (className.equals(item.componentName.getClassName())
							&& packageName.equals(item.componentName.getPackageName())) {
						mAllAppsList.remove(item);
						mDefault.add(item);

						break;
					}
				}
			}

			Bookstore.mSelectIndex = mDefault.size();

			mAllAppsList.addAll(0, mDefault);
		}

		mAppsAdapter.notifyDataSetChanged();

		Bookstore.mShortcutAdd.clear();
		Bookstore.mShortcutRemove.clear();
	}

	public void addApps(ArrayList<ApplicationInfo> list) {
		mAllAppsList.addAll(list);
		reorderApps();
	}

	public void removeApps(ArrayList<ApplicationInfo> list) {
		final int N = list.size();

		for (int i = 0; i < N; i++) {
			final ApplicationInfo item = list.get(i);
			int index = findAppByComponent(mAllAppsList, item);

			if (index >= 0) {
				mAllAppsList.remove(index);

				continue;
			}
		}

		reorderApps();
	}

	public void updateApps(ArrayList<ApplicationInfo> list) {
		final int count = getChildCount();

		for (int i = 0; i < count; i++) {
			final View view = getChildAt(i);

			Object tag = view.getTag();

			if (tag instanceof ShortcutInfo) {
				ShortcutInfo info = (ShortcutInfo) tag;

				final Intent intent = info.intent;
				final ComponentName name = intent.getComponent();
				if (info.itemType == LauncherSettings.Favorites.ITEM_TYPE_APPLICATION
						&& Intent.ACTION_MAIN.equals(intent.getAction()) && name != null) {
					final int appCount = list.size();

					for (int k = 0; k < appCount; k++) {
						ApplicationInfo app = list.get(k);

						if (app.componentName.equals(name)) {
							info.setIcon(mIconCache.getIcon(info.intent));

							break;
						}
					}
				}
			}
		}

		reorderApps();
	}

	private void firstLoad() {
		int pos = 0;
		int pIndex = 0;

		ArrayList<ApplicationInfo> cluster = new ArrayList<ApplicationInfo>();

		while (pos <= mAppsSetAdaper.getCount()) {
			if (getChildCount() == pIndex) {
				GridView appPage = new GridView(getContext());
				appPage.setNumColumns(Bookstore.mBookcaseCountX);
				appPage.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.book_selector_offset_ver));
				addView(appPage);
			}

			if (mAppsSetAdaper.getCount() == pos) {
				AppsAdapter appAdapter = new AppsAdapter(getContext(), (ArrayList<ApplicationInfo>) cluster.clone());
				appAdapter.setNotifyOnChange(false);
				((GridView) getChildAt(pIndex++)).setAdapter(appAdapter);
				cluster.clear();
				break;
			}

			if (0 == pos % (Bookstore.mBookcaseCountY * Bookstore.mBookcaseCountY) && 0 != pos) {
				AppsAdapter appAdapter = new AppsAdapter(getContext(), (ArrayList<ApplicationInfo>) cluster.clone());
				appAdapter.setNotifyOnChange(false);
				((GridView) getChildAt(pIndex++)).setAdapter(appAdapter);
				cluster.clear();
			}

			ApplicationInfo app = (ApplicationInfo) mAppsSetAdaper.getItem(pos);

			if (pos < Bookstore.mSelectIndex) {
				app.isSelect = true;
			} else {
				app.isSelect = false;
			}

			cluster.add(app);
			pos++;
		}

		Bookstore.mPageMaxNumber = pIndex;
		mParent.updateBtnInfo(Bookstore.mSelectIndex, Bookstore.mSelectMax);

		for (; pIndex < getChildCount();) {
			removeViewAt(getChildCount() - 1);
		}
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();

		mAppsSetObserver = new AppsSetObserver();
		setAdapter(mAppsAdapter);

		mAppsAdapter.setNotifyOnChange(true);
	}

	private class AppsSetObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			super.onChanged();

			firstLoad();
		}
	}

	public void setAdapter(ListAdapter adapter) {
		if (null != mAppsSetAdaper) {
			mAppsSetAdaper.unregisterDataSetObserver(mAppsSetObserver);
		}

		mAppsSetAdaper = adapter;

		if (null == mAppsSetAdaper) {
			removeAllViewsInLayout();

			return;
		}

		mAppsSetAdaper.registerDataSetObserver(mAppsSetObserver);
	}

	public class AppsAdapter extends ArrayAdapter<ApplicationInfo> {
		private final LayoutInflater mLayoutInflater;
		private ArrayList<ApplicationInfo> mApps;
		private Context mContext;

		public AppsAdapter(Context context, ArrayList<ApplicationInfo> apps) {
			super(context, 0, apps);

			mContext = context;
			mLayoutInflater = LayoutInflater.from(context);

			mApps = apps;
		}

		public ArrayList<ApplicationInfo> getItemList() {
			return mApps;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mApps.size();
		}

		@Override
		public ApplicationInfo getItem(int position) {
			// TODO Auto-generated method stub
			return mApps.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ApplicationInfo info = getItem(position);
			ViewHolder item;

			if (null == convertView) {
				item = new ViewHolder();

				convertView = mLayoutInflater.inflate(R.layout.scene_shelf_item, null);

				item.mItem = (TextView) convertView.findViewById(R.id.item);
				item.mFlags = (ImageView) convertView.findViewById(R.id.select);

				convertView.setBackgroundResource(R.drawable.bookshelf_background_item);

				convertView.setTag(item);
			} else {
				item = (ViewHolder) convertView.getTag();
			}

			Bitmap icon = info.iconBitmap;
			String label = info.title.toString();

			item.mItem.setText(label);
			item.mItem.setCompoundDrawablesWithIntrinsicBounds(null, new FastBitmapDrawable(icon), null, null);

			if (info.isSelect == true) {
				item.mFlags.setVisibility(View.VISIBLE);
			} else {
				item.mFlags.setVisibility(View.GONE);
			}

			convertView.setOnClickListener(new CustomClickListener(position));
			return convertView;
		}

		class ViewHolder {
			TextView mItem;
			ImageView mFlags;
		}

		private class CustomClickListener implements OnClickListener {
			private int mPosition;

			public CustomClickListener(int position) {
				mPosition = position;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int index = mPosition + Bookstore.mPageIndex * Bookstore.mBookcaseCountNumber;

				ImageView flags = (ImageView) v.findViewById(R.id.select);

				ShortcutInfo info = mAllAppsList.get(index).makeShortcut();
				info.container = LauncherSettings.Favorites.CONTAINER_BOOKSHELF;

				if (flags.getVisibility() == View.VISIBLE) {
					Bookstore.mSelectIndex--;

					flags.setVisibility(View.GONE);

					if (Bookstore.mShortcutAdd.contains(info)) {
						Bookstore.mShortcutAdd.remove(info);
					} else {
						Bookstore.mShortcutRemove.add(info);
					}
				} else {
					if (Bookstore.mSelectIndex >= Bookstore.mSelectMax) {
						String hints = String.format(mContext.getString(R.string.bookshelf_toast_hints),
								Bookstore.mSelectMax);

						Toast.makeText(mContext, hints, Toast.LENGTH_SHORT).show();
					} else {
						Bookstore.mSelectIndex++;

						flags.setVisibility(View.VISIBLE);

						Bookstore.mShortcutAdd.add(info);
					}
				}

				((BooksSelector) (v.getParent().getParent().getParent().getParent())).updateBtnInfo(
						Bookstore.mSelectIndex, Bookstore.mSelectMax);
			}
		}
	}
}
