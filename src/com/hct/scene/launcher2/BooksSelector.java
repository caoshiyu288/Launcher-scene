package com.hct.scene.launcher2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hct.scene.launcher.R;
import com.hct.scene.launcher2.ShowcaseFurniture.WindowState;

// create by hct lijunyi, 20130129

public class BooksSelector extends RelativeLayout implements View.OnClickListener {
	private Indicator mIndicator;
	private Button mBtnOk;
	private BooksDisplay mDisplay;

	private ShowcaseFurniture mFurniture;

	private Launcher mLauncher;
	private HouseWorkspace mHouseWorkspace;

	public BooksSelector(Context context) {
		// TODO Auto-generated constructor stub
		this(context, null, 0);
	}

	public BooksSelector(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		this(context, attrs, 0);
	}

	public BooksSelector(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setLauncher(Launcher launcher) {
		mLauncher = launcher;
		mHouseWorkspace = mLauncher.getHouseWorkspace();
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();

		mDisplay = (BooksDisplay) findViewById(R.id.layoutBookcaseDisplay);
		mIndicator = (Indicator) findViewById(R.id.pageIndicator);
		mBtnOk = (Button) findViewById(R.id.btnOk);

		mDisplay.setLayoutParent(this);

		Bookstore.mSelectIndex = LauncherModel.sSceneBookshelfItems.size();

		updateIndicator(Bookstore.mPageIndex + 1, Bookstore.mPageMaxNumber);
		updateBtnInfo(Bookstore.mSelectIndex, Bookstore.mSelectMax);

		mBtnOk.setOnClickListener(this);
	}

	public void hideLayout() {
		setVisibility(View.INVISIBLE);

		if (null != mFurniture) {
			mFurniture.startAnimation(WindowState.WINDOW_CLOSE);
		}

		mDisplay.reorderApps();
	}

	public View getLayoutShow() {
		return mDisplay;
	}

	public void showLayout(View view) {
		mFurniture = (ShowcaseFurniture) view;

		setVisibility(View.VISIBLE);
	}

	public void updateIndicator(int index, int max) {
		if (null != mIndicator) {
			mIndicator.changeState(index, max);
		}
	}

	public void updateBtnInfo(int select, int max) {
		if (null != mBtnOk) {
			mBtnOk.setText(String.format(getResources().getString(R.string.bookshelf_btn_info), select, max));
		}
	}

	public void updateApps() {
		Bookstore.mCancel = false;

		mDisplay.reorderApps();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnOk: {
			mHouseWorkspace.removeShelfShortcutInfo(Bookstore.mShortcutRemove);

			for (ShortcutInfo info : Bookstore.mShortcutAdd) {
				final View shortcut = mLauncher.createHouseShortcut(info);
				mHouseWorkspace.addInShelf(shortcut, info, true);
			}

			mLauncher.hideBookSelector();

			break;
		}

		default: {
			break;
		}
		}
	}
}
