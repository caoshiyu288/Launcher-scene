package com.hct.scene.launcher2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hct.scene.launcher.R;

// create by hct lijunyi, 20130126

public class SceneShelfBackground extends RelativeLayout {
	private LayoutInflater mLayoutInflater;

	private final String mViewTag = "VIEW_TAG_";

	// image list or board && background
	private List<Drawable> mBoard = new ArrayList<Drawable>();
	private List<Drawable> mBoardBackground = new ArrayList<Drawable>();

	private CharSequence[] values;

	private boolean mShowDefaultBoard = true;
	private boolean mShowBackground = true;
	private int mBoardNumber = 0;

	public SceneShelfBackground(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}

	public SceneShelfBackground(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public SceneShelfBackground(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// TODO Auto-generated constructor stub
		initView(context, attrs);
	}

	public void loadResource(int style) {
		int boardId = R.array.shelf_board_wind;
		int boardBackgroundId = R.array.shelf_board_background;

		if (1 == style) {
			boardId = R.array.shelf_board_princess;
		}

		if (mShowDefaultBoard) {
			boardId = R.array.shelf_board_default;
		}

		TypedArray resBoard = getResources().obtainTypedArray(boardId);
		TypedArray resBackground = getResources().obtainTypedArray(boardBackgroundId);

		mBoard.clear();
		mBoardBackground.clear();

		for (int i = 0; i < resBoard.length(); i++) {
			Drawable board = resBoard.getDrawable(i);
			mBoard.add(i, board);
		}

		for (int i = 0; i < resBackground.length(); i++) {
			Drawable background = resBackground.getDrawable(i);
			mBoardBackground.add(i, background);
		}

		if (resBoard.length() < mBoardNumber) {
			int step = mBoardNumber - resBoard.length();

			for (int i = 0; i < step; i++) {
				Drawable board = resBoard.getDrawable(0);
				mBoard.add(board);
			}
		}

		if (resBackground.length() < mBoardNumber) {
			int step = mBoardNumber - resBackground.length();

			for (int i = 0; i < step; i++) {
				Drawable background = resBackground.getDrawable(0);
				mBoardBackground.add(background);
			}
		}

		resBoard.recycle();
		resBackground.recycle();
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();

		setupView();
	}

	public void changeStyle(int style) {
		loadResource(style);
		updateView();
	}

	private void initView(Context context, AttributeSet attrs) {
		mLayoutInflater = LayoutInflater.from(context);

		TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.Bookshelf, 0, 0);

		mBoardNumber = type.getInteger(R.styleable.Bookshelf_board_number, mBoardNumber);
		mShowDefaultBoard = type.getBoolean(R.styleable.Bookshelf_default_board, mShowDefaultBoard);
		mShowBackground = type.getBoolean(R.styleable.Bookshelf_background_show, mShowBackground);

		if (!mShowDefaultBoard && mShowBackground) {
			values = context.getResources().getTextArray(R.array.booard_coodinate_shelf);
		} else {
			values = context.getResources().getTextArray(R.array.booard_coodinate_selector);
		}

		type.recycle();

		if (0 == mBoardNumber) {
			throw new IllegalStateException("board_number must be defined, now mBoardNumber = 0!!!");
		}
	}

	private void updateView() {
		for (int i = 0; i < mBoardNumber; i++) {
			ImageView board = (ImageView) findViewWithTag(mViewTag + Integer.toString(i));

			if (null != board) {
				board.setImageDrawable(mBoard.get(i));
			}
		}
	}

	public int px2dp(float px) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	private void setupView() {
		loadResource(0);

		final int height = getResources().getDimensionPixelSize(R.dimen.bookshelf_item_height);

		for (int i = 0; i < mBoardNumber; i++) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, height);

			params.setMargins(0, px2dp(Integer.parseInt((String) values[i])), 0, 0);

			View view = mLayoutInflater.inflate(R.layout.scene_shelf_board_item, null);
			addView(view, params);

			if (mShowBackground) {
				ImageView background = (ImageView) view.findViewById(R.id.imgBackground);
				background.setImageDrawable(mBoardBackground.get(i));
			}

			ImageView board = (ImageView) view.findViewById(R.id.imgBoard);
			board.setTag(mViewTag + Integer.toString(i));
			board.setImageDrawable(mBoard.get(i));
		}
	}
}
