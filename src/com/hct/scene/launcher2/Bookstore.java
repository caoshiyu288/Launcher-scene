package com.hct.scene.launcher2;

import java.util.ArrayList;

// create by hct lijunyi, 20130129

public class Bookstore {
	static int mSelectIndex = 0;
	static final int mSelectMax = 10;

	static int mPageIndex = 0;
	static int mPageMaxNumber = 0;

	static final int mBookcaseCountX = 4;
	static final int mBookcaseCountY = 4;

	static final int mBookcaseCountNumber = mBookcaseCountX * mBookcaseCountY;

	static ArrayList<ShortcutInfo> mShortcutAdd = new ArrayList<ShortcutInfo>();
	static ArrayList<ShortcutInfo> mShortcutRemove = new ArrayList<ShortcutInfo>();

	static boolean mCancel = true;
}
