package com.hct.scene.launcher2;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.hct.scene.launcher.R;

public class SofaSingleFurniture extends Furniture{
	private MusicPlayerScene musicSofaScene;
	public SofaSingleFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initContent(context);
	}

	public SofaSingleFurniture(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SofaSingleFurniture(Context context) {
		this(context, null, 0);
	}
	
	private void initContent(Context context){
		musicSofaScene = new MusicPlayerScene(getContext());
		musicSofaScene.setOnClickListener(this);
		LayoutParams musicSofaParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM|Gravity.LEFT);
		this.addView(musicSofaScene,musicSofaParams);
	}
	@Override
	protected Rect getHighLightFrame() {
		return null;
	}

	@Override
	public void changeStyle(int style) {
		super.changeStyle(style);
		int id = 0;
		if(style == 0){
			id = R.drawable.music_sofa_fresh;
		}else if(style == 1){
			id = R.drawable.music_sofa_ranee;
		}
		musicSofaScene.setMusicSceneStyle(id);
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void setLongClickListener(OnLongClickListener listener) {
		super.setLongClickListener(listener);
		if(musicSofaScene != null){
//			musicSofaScene.setOnLongClickListener(listener);
		}
	}
}
