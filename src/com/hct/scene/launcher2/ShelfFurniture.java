package com.hct.scene.launcher2;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.hct.scene.launcher.R;

public class ShelfFurniture extends Furniture implements LoadHouseWorkspaceIcons, View.OnLongClickListener {
	private Context mContext;
	private LayoutInflater mLayoutInflater;

	private CellLayout mLayout;

	SceneShelfBackground mShelfBackground;
	SceneShelfDisplay mShelfDisplay;

	private final int mCountX = 2, mCountY = 5;

	private boolean mShakeState = false;

	protected IconCache mIconCache;

	public ShelfFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ShelfFurniture(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ShelfFurniture(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
		Rect rect = new Rect();

		rect.left = getLeft() + 2;
		rect.top = getTop();
		rect.right = getRight();
		rect.bottom = getBottom();

		return rect;
	}

	@Override
	public void changeStyle(int style) {
		// TODO Auto-generated method stub
		mShelfBackground = (SceneShelfBackground) findViewById(R.id.shelfBackground);
		mShelfDisplay = (SceneShelfDisplay) findViewById(R.id.shelfDisplay);

		mShelfBackground.changeStyle(style);

		// setOnLongClickListener(mLongClickListener);
	}

	private void addLayout() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
		params.setMargins(0, 20, 0, 0);
		mLayoutInflater = LayoutInflater.from(mContext);
		mLayout = (CellLayout) mLayoutInflater.inflate(R.layout.scene_shelf_celllayout, null);
		mLayout.setGridSize(mCountX, mCountY);
		mLayout.setOnClickListener(this);
		mLayout.setOnLongClickListener(mLongClickListener);
		mShelfDisplay.addView(mLayout, params);
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();

		addLayout();
	}

	@Override
	public void setLongClickListener(OnLongClickListener listener) {
		super.setLongClickListener(listener);
		if (mLayout != null)
			mLayout.setOnLongClickListener(listener);
	}

	private void initView(Context context) {
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);

		LauncherApplication app = (LauncherApplication) context.getApplicationContext();
		mIconCache = app.getIconCache();

		View view = mLayoutInflater.inflate(R.layout.scene_shelf_furniture, null);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT | Gravity.VERTICAL_GRAVITY_MASK);
		params.setMargins(25, 0, 20, 0);
		addView(view, params);
	}

	public void addInScreen(View child, long container, int x, int y, int spanX, int spanY, boolean save) {
		if (container == LauncherSettings.Favorites.CONTAINER_BOOKSHELF) {
			if (spanX >= mCountX || spanY >= mCountY) {
				return;
			}

			CellLayout.LayoutParams lp = (CellLayout.LayoutParams) child.getLayoutParams();

			if (lp == null) {
				lp = new CellLayout.LayoutParams(x, y, 1, 1);
			} else {
				lp.cellX = x;
				lp.cellY = y;
				lp.cellHSpan = 1;
				lp.cellVSpan = 1;
			}

			int childId = getChildId(x, y);

			if (!mLayout.addViewToCellLayout(child, -1, childId, lp, true)) {
			}

			if (save) {
				LauncherModel.addItemToDatabase(getContext(), (ItemInfo) child.getTag(), container, 0, x, y, false);
			}
		}
	}

	private int getChildId(int x, int y) {
		return (x + y * mCountX);
	}

	protected boolean findAndSetEmptyCells(ShortcutInfo item) {
		int[] emptyCell = new int[2];

		if (mLayout.findCellForSpan(emptyCell, 1, 1)) {
			item.cellX = emptyCell[0];
			item.cellY = emptyCell[1];

			return true;
		} else {
			return false;
		}
	}

	/**
	 * add apps to furniture
	 * */
	@Override
	public void addAppsInScreen(View v, ItemInfo shortcut, boolean save) {
		if (-1 == shortcut.cellX && -1 == shortcut.cellY) {
			findAndSetEmptyCells((ShortcutInfo) shortcut);
		}

		addInScreen(v, shortcut.container, shortcut.cellX, shortcut.cellY, shortcut.spanX, shortcut.spanY, save);
	}

	@Override
	public void removeShortcutInfo(ArrayList<ShortcutInfo> apps) {
		// TODO Auto-generated method stub
		for (ShortcutInfo info : apps) {
			removeItem(info);
		}
	}

	public void removeItem(ShortcutInfo item) {
		ComponentName cn = item.intent.getComponent();

		String className = cn.getClassName();
		String packageName = cn.getPackageName();

		final int childCount = mLayout.getChildrenChildCount();

		for (int i = 0; i < childCount; i++) {
			View view = mLayout.getChildrenChildAt(i);

			if (view.getTag() instanceof ShortcutInfo) {
				ShortcutInfo info = (ShortcutInfo) view.getTag();

				ComponentName cn2 = info.intent.getComponent();

				if (cn2.getClassName().equals(className) && cn2.getPackageName().equals(packageName)) {
					mLayout.removeView(findViewById(getChildId(info.cellX, info.cellY)));

					LauncherModel.deleteItemFromDatabase(getContext(), info);

					break;
				}
			}
		}
	}

	@Override
	public void removeApps(ArrayList<ApplicationInfo> apps) {
		// TODO Auto-generated method stub
		for (ApplicationInfo info : apps) {
			removeItem(info.makeShortcut());
		}
	}

	@Override
	public void updateApps(ArrayList<ApplicationInfo> apps) {
		// TODO Auto-generated method stub
		final int count = getChildCount();

		for (int i = 0; i < count; i++) {
			final View view = getChildAt(i);

			Object tag = view.getTag();

			if (tag instanceof ShortcutInfo) {
				ShortcutInfo info = (ShortcutInfo) tag;

				final Intent intent = info.intent;
				final ComponentName name = intent.getComponent();
				if (info.itemType == LauncherSettings.Favorites.ITEM_TYPE_APPLICATION && Intent.ACTION_MAIN.equals(intent.getAction())
						&& name != null) {
					final int appCount = apps.size();

					for (int k = 0; k < appCount; k++) {
						ApplicationInfo app = apps.get(k);

						if (app.componentName.equals(name)) {
							info.setIcon(mIconCache.getIcon(info.intent));

							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void startShakeAnimate(boolean on) {
		if (mShakeState == on)
			return;
		if (!(mLayout instanceof CellLayout))
			return;
		CellLayoutChildren childrenLayout = mLayout.getChildrenLayout();
		final int childCount = childrenLayout.getChildCount();
		for (int j = 0; j < childCount; j++) {
			SceneShelfItem icon = (SceneShelfItem) childrenLayout.getChildAt(j);
			if (on) {
				icon.startAnimate(0, j % 4);
			} else
				icon.cancelAnimate();
		}

		if (on)
			mShakeState = true;
		else
			mShakeState = false;
	}

	@Override
	public boolean isAnimateOn() {
		return mShakeState;
	}

	View createShortcut(ShortcutInfo info) {
		info.itemType = LauncherSettings.Favorites.ITEM_TYPE_APPLICATION;

		SceneShelfItem icon = (SceneShelfItem) mLayoutInflater.inflate(R.layout.scene_shelf_item, null);

		ApplicationInfo appInfo = null;
		icon.applyFromApplicationInfo(appInfo, false, null);
		icon.setBackgroundResource(R.drawable.bookshelf_background_item);
		icon.setOnClickListener(this);
		icon.setOnLongClickListener(this);

		return icon;
	}

	@Override
	public void onClick(View v) {
		if (mZoomMode) {
			mSingleClickListener.onClick(v);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}
}
