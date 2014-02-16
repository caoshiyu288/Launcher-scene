package com.hct.scene.launcher2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class ImageFurniture extends Furniture{
	private ImageView mphoto_icon;
	private Context mcontext;
	public ImageFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mcontext = context;
		inflate(mcontext);
	}

	public ImageFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mcontext = context;
		inflate(mcontext);
	}

	public ImageFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mcontext = context;
		inflate(mcontext);
	}

	@Override
	protected Rect getHighLightFrame() {
		if(mphoto_icon != null){
			Rect r = mTempRect;
			mphoto_icon.getHitRect(r);
			
			
			return r;
		}
		return null;
	}

	
	protected void inflate(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);

     //   inflater.inflate(R.layout.photo_view, null, true);
		inflater.inflate(R.layout.photo_view, this);
		mphoto_icon =(ImageView)this.findViewById(R.id.view_photo);
		mphoto_icon.setClickable(true);
		mphoto_icon.setOnClickListener(photo_click);
	}
	
    @Override
	public void setLongClickListener(OnLongClickListener listener) {
		super.setLongClickListener(listener);
		mphoto_icon.setOnLongClickListener(listener);
	}

	OnClickListener photo_click = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mZoomMode){
//				ImageFurniture.this.invalidate();
				mSingleClickListener.onClick(v);
			}else{
				Log.d("click", "inter--"+this);
				Intent intent =new Intent();  
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.setClassName("com.android.gallery3d", "com.android.gallery3d.app.Gallery");
				
				
				mcontext.startActivity(intent);
			}



	
		}

		
	};
	@Override
	public void changeStyle(int style) {
		// TODO Auto-generated method stub
		super.changeStyle(style);
		int mstyle = style;
		if(mstyle == 0){
		   mphoto_icon.setImageResource(R.drawable.fresh_photo);
		}
		else{
			mphoto_icon.setImageResource(R.drawable.princess_photo);
		}
		
	}

	
}
