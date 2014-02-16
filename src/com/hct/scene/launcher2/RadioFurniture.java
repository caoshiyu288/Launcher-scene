package com.hct.scene.launcher2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.hct.scene.launcher.R;

public class RadioFurniture extends Furniture {
	private Context mcontext;
	private ImageView mImgL, mImgM, mImgR, mFm;
	public boolean mAnminPlay;
	private View mView;

	private MyAnimation mMyAnimationL, mMyAnimationM, mMyAnimationR;

	public RadioFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mcontext = context;
		inflate(mcontext);
	}

	public RadioFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mcontext = context;
		inflate(mcontext);
	}

	public RadioFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mcontext = context;
		inflate(mcontext);
	}

	@Override
	protected Rect getHighLightFrame() {
		if (mFm != null) {
			Rect r = mTempRect;
			mFm.getHitRect(r);
			r.bottom += 40;
			r.right += 40;

			return r;
		}
		return null;
	}

	protected void inflate(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);

		mView = inflater.inflate(R.layout.fm_view, this);

		mFm = (ImageView) mView.findViewById(R.id.fm_img);
		mFm.setOnClickListener(fm_click);

		mImgL = (ImageView) mView.findViewById(R.id.imgL);
		mImgL.setOnClickListener(click_anim);

		mImgM = (ImageView) mView.findViewById(R.id.imgM);
		mImgM.setOnClickListener(click_anim);

		mImgR = (ImageView) mView.findViewById(R.id.imgR);
		mImgR.setOnClickListener(click_anim);

		mMyAnimationL = new MyAnimation(mImgL);
		mMyAnimationM = new MyAnimation(mImgM);
		mMyAnimationR = new MyAnimation(mImgR);
	}

	@Override
	public void setLongClickListener(OnLongClickListener listener) {
		super.setLongClickListener(listener);
		if (mFm != null) {
			mFm.setOnLongClickListener(listener);
		}
	}

	OnClickListener fm_click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (mZoomMode) {
				mSingleClickListener.onClick(v);
			} else {
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.setClassName("com.mediatek.FMRadio", "com.mediatek.FMRadio.FMRadioActivity");
				mcontext.startActivity(intent);
			}
		}
	};

	View.OnClickListener click_anim = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.d("sanxi", "click_anim---v==" + v.getId() + "," + v);
			int id = v.getId();
			switch (id) {
			case R.id.imgL:

				mMyAnimationL.startAnimation();
				break;
			case R.id.imgM:
				mMyAnimationM.startAnimation();

				break;
			case R.id.imgR:
				mMyAnimationR.startAnimation();

				break;
			}
		}
	};

	public class MyAnimation {
		private ImageView img;

		private final float LcenterX = 26;

		private final float LcenterY = 2;

		private AnimatorSet animatorSet;

		public MyAnimation(ImageView src_img) {
			img = src_img;
			img.setPivotX(LcenterX);
			img.setPivotY(LcenterY);
			init();
		}

		public void init() {
			ObjectAnimator right1 = ObjectAnimator.ofFloat(img, "rotation", 0, 10);
			ObjectAnimator right2 = ObjectAnimator.ofFloat(img, "rotation", 10, -5);
			ObjectAnimator right3 = ObjectAnimator.ofFloat(img, "rotation", -5, 3);
			ObjectAnimator right4 = ObjectAnimator.ofFloat(img, "rotation", 3, -3);
			ObjectAnimator right5 = ObjectAnimator.ofFloat(img, "rotation", -3, 3);
			ObjectAnimator right6 = ObjectAnimator.ofFloat(img, "rotation", 3, 0);

			right1.setDuration(200);
			right2.setDuration(200);
			right2.setStartDelay(200);
			right3.setDuration(200);
			right3.setStartDelay(400);
			right4.setDuration(200);
			right4.setStartDelay(600);
			right5.setDuration(200);
			right5.setStartDelay(800);
			right6.setDuration(200);
			// right6.setStartDelay(1000);

			animatorSet = new AnimatorSet();
			animatorSet.play(right6).after(right5).after(right4).after(right3).after(right2).after(right1);
		}

		public void startAnimation() {
			stopAnimation();
			animatorSet.start();
		}

		public void stopAnimation() {
			animatorSet.cancel();
		}
	}

	@Override
	public void changeStyle(int style) {
		// TODO Auto-generated method stub
		super.changeStyle(style);
		// inflate(mcontext,1);
		int mstyle = style;
		if (mstyle == 0) {

			mFm.setImageResource(R.drawable.fresh_fm);
			mImgL.setImageResource(R.drawable.fresh_pend_1);
			mImgM.setImageResource(R.drawable.fresh_pend_2);
			mImgR.setImageResource(R.drawable.fresh_pend_3);

		} else {
			mFm.setImageResource(R.drawable.princess_fm);
			mImgL.setImageResource(R.drawable.princess_pend_1);
			mImgM.setImageResource(R.drawable.princess_pend_2);
			mImgR.setImageResource(R.drawable.princess_pend_3);

		}
	}

	@Override
	public boolean isAnimationObj() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onAnimation(int width) {
		// TODO Auto-generated method stub
		mMyAnimationL.startAnimation();
		mMyAnimationM.startAnimation();
		mMyAnimationR.startAnimation();
	}

	@Override
	public void stopAnimation(int width) {
		// TODO Auto-generated method stub
		mMyAnimationL.stopAnimation();
		mMyAnimationM.stopAnimation();
		mMyAnimationR.stopAnimation();
	}
}
