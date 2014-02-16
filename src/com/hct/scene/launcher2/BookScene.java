package com.hct.scene.launcher2;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.hct.scene.launcher.R;

public class BookScene extends FrameLayout {
	public BookScene(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BookScene(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BookScene(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void changeStyle(int style) {
		setBookShelf(style);
	}

	public void setBookShelf(int scene) {
		Resources res = getResources();
		switch (scene) {
		case 0:
			setBackgroundDrawable(res.getDrawable(R.drawable.bookshelf_fresh));
			break;
		case 1:
			setBackgroundDrawable(res.getDrawable(R.drawable.bookshelf_ranee));
			break;
		default:
			setBackgroundDrawable(res.getDrawable(R.drawable.bookshelf_fresh));
			break;
		}
	}
}
