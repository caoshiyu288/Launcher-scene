package com.hct.scene.launcher2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class ComputerFurniture extends Furniture {
	private Context mcontext;
	private ImageView mvideo_top, mvideo_bot;

	public ComputerFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// TODO Auto-generated constructor stub
		mcontext = context;
		inflate(mcontext);
	}

	public ComputerFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);

		// TODO Auto-generated constructor stub
		mcontext = context;
		inflate(mcontext);
	}

	public ComputerFurniture(Context context) {
		super(context);

		// TODO Auto-generated constructor stub
		mcontext = context;
		inflate(mcontext);
	}

	protected void inflate(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.video_view, this);
		mvideo_top = (ImageView) findViewById(R.id.video_top);
		mvideo_bot = (ImageView) findViewById(R.id.video_bot);
		mvideo_top.setOnClickListener(vedio_click);
	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
		if (mvideo_bot != null) {
			Rect r = mTempRect;
			mvideo_bot.getHitRect(r);

			r.left += 2;
			r.right += 20;

			return r;
		}

		return null;
	}

	OnClickListener vedio_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (mZoomMode) {
				mSingleClickListener.onClick(v);
			} else {
				Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.setClassName("com.android.gallery3d", "com.android.gallery3d.app.Gallery");
				intent.setAction(Intent.ACTION_VIEW);
				intent.setType("vnd.android.cursor.dir/video");
				mcontext.startActivity(intent);
			}
		}
	};

	@Override
	public void changeStyle(int style) {
		// TODO Auto-generated method stub
		super.changeStyle(style);
		int mstyle = style;
		if (mstyle == 0) {
			mvideo_bot.setImageResource(R.drawable.fresh_tvdesk);
			mvideo_top.setImageResource(R.drawable.fresh_tv);
		} else {
			mvideo_bot.setImageResource(R.drawable.princess_tvdesk);
			mvideo_top.setImageResource(R.drawable.princess_tv);
		}
	}

	@Override
	public void setLongClickListener(OnLongClickListener listener) {
		// TODO Auto-generated method stub
		super.setLongClickListener(listener);
		
		mvideo_top.setOnLongClickListener(listener);
	}
}
