package com.hct.scene.launcher2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hct.scene.launcher.R;

public class NotesFurniture extends Furniture {
	private ImageView mnote;

	public NotesFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		inflate(context);
	}

	public NotesFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		inflate(context);
	}

	public NotesFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		inflate(context);
	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
		if (mnote != null) {
			Rect r = mTempRect;
			mnote.getHitRect(r);
			r.left += 5;
			return r;
		}
		return null;
	}

	protected void inflate(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);

		inflater.inflate(R.layout.note_view, this);
		mnote = (ImageView) findViewById(R.id.note_view);
		mnote.setOnClickListener(note_click);

	}

	@Override
	public void changeStyle(int style) {
		// TODO Auto-generated method stub
		super.changeStyle(style);
		int mstyle = style;
		if (mstyle == 0) {
			mnote.setImageResource(R.drawable.fresh_note);
		} else {
			mnote.setImageResource(R.drawable.princess_note);
		}
	}

	@Override
	public void setLongClickListener(OnLongClickListener listener) {
		// TODO Auto-generated method stub
		super.setLongClickListener(listener);
		mnote.setOnLongClickListener(listener);
	}

	OnClickListener note_click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (mZoomMode) {

				mSingleClickListener.onClick(v);
			} else {
				Log.d("click", "inter--" + this);
				Intent intent = new Intent();
				intent.setClassName("com.mediatek.notebook", "com.mediatek.notebook.NotesList");
				// mcontext.startActivity(intent);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				try {
					getContext().startActivity(intent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getContext(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
				} catch (SecurityException e) {
					Toast.makeText(getContext(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
}
