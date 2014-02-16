package com.hct.scene.launcher2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.hct.scene.launcher.R;

public class CameraZoneFurniture extends Furniture{
    private Context mContext; 
    private ImageView img_bg;
    private ImageView camera;
    private ImageView zone;
    private ImageView renren;
    private ImageView sina;
    private ImageView tencent;
    private ImageView qzone;
    private final int cameraImageId = 1;
    private final int zoneImageId = 2;
    private final int renrenImageId = 3;
    private final int sinaImageId = 4;
    private final int qzoneImageId = 5;
    private final int tencentImageId = 6;
    private Intent mIntent;
    
	public CameraZoneFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CameraZoneFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CameraZoneFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		initViews(context);
	}

	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
    	Rect rect = new Rect();
    	img_bg.getHitRect(rect);
    	
		return rect;
	}

	@Override
	public void changeStyle(int style){
		setParams(mContext, style);
		super.changeStyle(style);
	}
	
	@Override
	public boolean isAnimationObj(){
		return false;
	}
	
	@Override
	public void onAnimation(int width){
	
		super.onAnimation(width);
	}

    private void initViews(Context context) {
		img_bg = new ImageView(context);
		addView(img_bg);
		camera = new ImageView(context);
        camera.setId(cameraImageId);
        addView(camera);
		zone = new ImageView(context);
        zone.setId(zoneImageId);
        addView(zone);
        
        renren = new ImageView(context);
        renren.setId(renrenImageId);
        addView(renren);
		sina = new ImageView(context);
		sina.setId(sinaImageId);
        addView(sina);
		tencent = new ImageView(context);
		tencent.setId(tencentImageId);
        addView(tencent);
		qzone = new ImageView(context);
		qzone.setId(qzoneImageId);
        addView(qzone);
        
		camera.setOnClickListener(viewClickListener);
		zone.setOnClickListener(viewClickListener);
		img_bg.setOnClickListener(viewClickListener);
		renren.setOnClickListener(viewClickListener);
		sina.setOnClickListener(viewClickListener);
		tencent.setOnClickListener(viewClickListener);
		qzone.setOnClickListener(viewClickListener);
    }
    
    private void setParams(Context context, int style){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) img_bg.getLayoutParams();
        if (style == 0){
        	img_bg.setImageResource(R.drawable.cameraandzone_fresh_bg);
        } else if (style == 1) {
        	img_bg.setImageResource(R.drawable.cameraandzone_princess_bg);
        }
        params.gravity = Gravity.CENTER;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        img_bg.setScaleType(ScaleType.FIT_XY);
        img_bg.setLayoutParams(params);

        params = (FrameLayout.LayoutParams) camera.getLayoutParams();
        if (style == 0){
        	camera.setImageResource(R.drawable.camera_fresh);
        	params.leftMargin = (int) getResources().getDimension(R.dimen.cameraview_fresh_left_margin);// 30;
        	params.topMargin = (int) getResources().getDimension(R.dimen.cameraview_fresh_top_margin);//10; 
        } else if (style == 1) {
        	camera.setImageResource(R.drawable.camera_princess);
        	params.leftMargin = (int) getResources().getDimension(R.dimen.cameraview_princess_left_margin);//17;
        	params.topMargin = (int) getResources().getDimension(R.dimen.cameraview_princess_top_margin); //77; 
        }
        params.gravity = Gravity.LEFT |Gravity.TOP;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        camera.setLayoutParams(params);

        params = (FrameLayout.LayoutParams) zone.getLayoutParams();
        if (style == 0){
        	zone.setImageResource(R.drawable.zone_fresh);
        	params.rightMargin = (int) getResources().getDimension(R.dimen.zoneview_fresh_right_margin);//15;
        	params.bottomMargin = (int) getResources().getDimension(R.dimen.zoneview_fresh_bottom_margin);//79;
        } else if (style == 1) {
        	zone.setImageResource(R.drawable.zone_princess);
        	params.rightMargin = (int) getResources().getDimension(R.dimen.zoneview_princess_right_margin);//28;
        	params.bottomMargin =(int) getResources().getDimension(R.dimen.zoneview_princess_bottom_margin);// 95;
        }
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        zone.setLayoutParams(params);
   
        params = (FrameLayout.LayoutParams) sina.getLayoutParams();
        if (style == 0){
        	sina.setImageResource(R.drawable.sina_fresh);
        	params.rightMargin = (int) getResources().getDimension(R.dimen.sina_fresh_right_margin);//15;
        	params.bottomMargin = (int) getResources().getDimension(R.dimen.sina_fresh_bottom_margin);//79;
        } else if (style == 1) {
        	sina.setImageResource(R.drawable.sina_princess);
        	params.rightMargin = (int) getResources().getDimension(R.dimen.sina_princess_right_margin);//28;
        	params.bottomMargin =(int) getResources().getDimension(R.dimen.sina_princess_bottom_margin);// 95;
        }
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        sina.setLayoutParams(params);
        
        params = (FrameLayout.LayoutParams) renren.getLayoutParams();
        if (style == 0){
        	renren.setImageResource(R.drawable.renren_fresh);
        	params.rightMargin = (int) getResources().getDimension(R.dimen.renren_fresh_right_margin);//15;
        	params.bottomMargin = (int) getResources().getDimension(R.dimen.renren_fresh_bottom_margin);//79;
        } else if (style == 1) {
        	renren.setImageResource(R.drawable.renren_princess);
        	params.rightMargin = (int) getResources().getDimension(R.dimen.renren_princess_right_margin);//28;
        	params.bottomMargin =(int) getResources().getDimension(R.dimen.renren_princess_bottom_margin);// 95;
        }
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        renren.setLayoutParams(params);
        
        params = (FrameLayout.LayoutParams) tencent.getLayoutParams();
        if (style == 0){
        	tencent.setImageResource(R.drawable.tencent_fresh);
        	params.rightMargin = (int) getResources().getDimension(R.dimen.tencent_fresh_right_margin);//15;
        	params.bottomMargin = (int) getResources().getDimension(R.dimen.tencent_fresh_bottom_margin);//79;
        } else if (style == 1) {
        	tencent.setImageResource(R.drawable.tencent_princess);
        	params.rightMargin = (int) getResources().getDimension(R.dimen.tencent_princess_right_margin);//28;
        	params.bottomMargin =(int) getResources().getDimension(R.dimen.tencent_princess_bottom_margin);// 95;
        }
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        tencent.setLayoutParams(params);
        
        params = (FrameLayout.LayoutParams) qzone.getLayoutParams();
        if (style == 0){
        	qzone.setImageResource(R.drawable.qzone_fresh);
        	params.rightMargin = (int) getResources().getDimension(R.dimen.qzone_fresh_right_margin);//15;
        	params.bottomMargin = (int) getResources().getDimension(R.dimen.qzone_fresh_bottom_margin);//79;
        } else if (style == 1) {
        	qzone.setImageResource(R.drawable.qzone_princess);
        	params.rightMargin = (int) getResources().getDimension(R.dimen.qzone_princess_right_margin);//28;
        	params.bottomMargin =(int) getResources().getDimension(R.dimen.qzone_princess_bottom_margin);// 95;
        }
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        qzone.setLayoutParams(params);
        
        
		camera.setOnLongClickListener(mLongClickListener);
		zone.setOnLongClickListener(mLongClickListener);
		img_bg.setOnLongClickListener(mLongClickListener);
		sina.setOnLongClickListener(mLongClickListener);
		renren.setOnLongClickListener(mLongClickListener);
		tencent.setOnLongClickListener(mLongClickListener);
		qzone.setOnLongClickListener(mLongClickListener);
    }

	private OnClickListener viewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mZoomMode){
				mSingleClickListener.onClick(v);
			}else{
				if (v.getId() == camera.getId()) {
					Log.d("liuyang", "camera--onTouchEvent--up");
					mIntent = new Intent();
					mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mIntent.setClassName("com.android.gallery3d",
							"com.android.camera.CameraLauncher");
					mContext.startActivity(mIntent);
				} else if (v.getId() == qzone.getId()) {
					Log.d("liuyang", "qzone--onTouchEvent--up");
					mIntent = new Intent();
					mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mIntent.setClassName("com.qzone",
							"com.tencent.sc.activity.SplashActivity");
					startActivitySafety(mIntent);
				} else if (v.getId() == sina.getId()) {
					Log.d("liuyang", "sina--onTouchEvent--up");
					mIntent = new Intent();
					mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mIntent.setClassName("com.sina.weibo",
							"com.sina.weibo.SplashActivity");
					startActivitySafety(mIntent);
				} else if (v.getId() == renren.getId()) {
					Log.d("liuyang", "renren--onTouchEvent--up");
					mIntent = new Intent();
					mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mIntent.setClassName("com.renren.mobile.android",
							"com.renren.mobile.android.ui.WelcomeScreen");
					startActivitySafety(mIntent);
				} else if (v.getId() == tencent.getId()) {
					Log.d("liuyang", "tencent--onTouchEvent--up");
					mIntent = new Intent();
					mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mIntent.setClassName("com.tencent.WBlog",
							"com.tencent.WBlog.activity.WBlogFirstRun");
					startActivitySafety(mIntent);
				}
			}
		}
	};
 
	private boolean startActivitySafety(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		try {
			mContext.startActivity(intent);
			return true;
		} catch (ActivityNotFoundException e) {
			Toast.makeText(getContext(), mContext.getString(R.string.activity_not_found), 200).show();
			return false;
		} catch (SecurityException e) {
			Toast.makeText(getContext(), mContext.getString(R.string.activity_not_found), 200).show();
			return false;
		}
	}
	
}
