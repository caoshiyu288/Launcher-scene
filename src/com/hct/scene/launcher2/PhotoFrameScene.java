package com.hct.scene.launcher2;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.hct.scene.launcher.R;
public class PhotoFrameScene extends FrameLayout{
	private static final String TAG = "PhotoFrameScene";
	public static final Uri CONTENT_URI = Uri.parse("content://com.android.gallery3d.gadget.settings");
	private static final String FIELD_PHOTO_BLOB = "photoBlob";
	private static final String FIELD_APPWIDGET_ID = "appWidgetId";
	private int mLayoutWidth;
	private int mLayoutHeight;
	private BitmapDrawable mDrawable;
	private Bitmap bitmap;
	public PhotoFrameScene(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		photoFrameInit();
	}

	public PhotoFrameScene(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		photoFrameInit();
	}

	public PhotoFrameScene(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		photoFrameInit();
	}
	private void photoFrameInit(){
		//setOnClickListener(this);
		updatePhotoFrame();
	}
	public void changeStyle(int style){
		setPhotoFrameScene(style);
	}
	private void setPhotoFrameScene(int scene){
		Resources res = getResources();
		switch(scene){
		case 0:
			setBackgroundDrawable(res.getDrawable(R.drawable.photoframe_fresh));
			break;
		case 1:
			setBackgroundDrawable(res.getDrawable(R.drawable.photoframe_ranee));
			break;
		default:
			setBackgroundDrawable(res.getDrawable(R.drawable.photoframe_fresh));
			break;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		mDrawable = (BitmapDrawable) getBackground();
		if (mDrawable != null) {
			Bitmap bitmap = mDrawable.getBitmap();
			mLayoutWidth = bitmap.getWidth();
			mLayoutHeight = bitmap.getHeight();
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		getContext().registerReceiver(mBroadcastReceiver, new IntentFilter("com.hct.action.GETPHOTOFRAME"));
	}
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		getContext().unregisterReceiver(mBroadcastReceiver);
	}
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if("com.hct.action.GETPHOTOFRAME".equals(intent.getAction())){
				 Log.d(TAG, "onReceive");
				 updatePhotoFrame();
				 postInvalidate();
			}
		}
	};
	private void updatePhotoFrame(){
		final ContentResolver cr = getContext().getContentResolver();
		Cursor c = cr.query(CONTENT_URI, null, null, null, null);
		if(c == null){
			return;
		}
		c.moveToFirst();
		while (!c.isAfterLast()) {
			int widgetId = c.getInt(c.getColumnIndexOrThrow(FIELD_APPWIDGET_ID));
			if(widgetId == PhotoFrameFurniture.photoFrameId){
				byte[] blobImage = c.getBlob(c.getColumnIndexOrThrow(FIELD_PHOTO_BLOB));
				if (blobImage != null) {
					bitmap = Bytes2Bimap(blobImage);
				}	
			}
			c.moveToNext();
		}
		c.close();
	}
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(bitmap != null){
			Matrix matrix = new Matrix();
			Log.d(TAG, "width = " + bitmap.getWidth() + "|| height = " + bitmap.getHeight());
		    float scaleWidth = ((float) mLayoutWidth - 20) / bitmap.getWidth();
			float scaleHeight = ((float) mLayoutHeight - 20) / bitmap.getHeight();
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			//canvas.clipRect(new Rect(10, 10, mLayoutWidth - 10, mLayoutHeight - 10));
			canvas.drawBitmap(newbm, 10, 10, new Paint());
		}
	}
}
