package com.hct.scene.launcher2;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class MusicPlayerScene extends FrameLayout {
	public MusicPlayerScene(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MusicPlayerScene(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MusicPlayerScene(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setMusicSceneStyle(int id) {
		Resources res = getResources();
		setBackgroundDrawable(res.getDrawable(id));
	}
}
