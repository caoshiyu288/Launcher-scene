package com.hct.scene.launcher2;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.mediatek.audioprofile.AudioProfileManager;

import com.hct.scene.launcher.R;

public class Door extends FrameLayout{

    private Context mContext; 
	public static final int style = 0;
	public static final int sliding_l2r = 0;
	public static final int sliding_r2l = 1;
	public static final int sliding_direction = sliding_r2l;

	private ImageView door;
	private ImageView door_handle;
	private AnimatorSet mDoorHandleAniSet;
	private ImageView door_frame;
	private AnimatorSet mWholeDoorAniSet;
	
	private FrameLayout doorAndHandle;
	
	private Runnable mRunnable;
	private Runnable mCompleteRunnable;
	private MediaPlayer mOpenDoorMediaPlayer;
	private MediaPlayer mCloseDoorMediaPlayer;
	private AudioProfileManager mProfileManager;
	
    public Door(Context context, AttributeSet attr){
   	    super(context,attr);
   	    mContext = context;
        addViews(context);
        mProfileManager = (AudioProfileManager)context.getSystemService(Context.AUDIOPROFILE_SERVICE);
   	}
    public void openDoorSounds(boolean open){
    	AudioManager am=(AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
    	if(am.isMusicActive() || am.isFmActive()) return;
		if(open){
			if(mOpenDoorMediaPlayer == null)
				mOpenDoorMediaPlayer = MediaPlayer.create(mContext, R.raw.open_door);
			if(mOpenDoorMediaPlayer != null && !isRefusedByMtkAudioProfile()){
//				int currentVolume = am.getStreamVolume(AudioManager.STREAM_VOICE_CALL);  
//				mOpenDoorMediaPlayer.setVolume(leftVolume, rightVolume);
				mOpenDoorMediaPlayer.start();
//				((Launcher)mContext).setVolumeControlStream(AudioManager.STREAM_MUSIC);
//				am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
//						AudioManager.FLAG_PLAY_SOUND);
			}
		}else{
			if(mCloseDoorMediaPlayer == null)
				mCloseDoorMediaPlayer = MediaPlayer.create(mContext, R.raw.close_door);
			if(mCloseDoorMediaPlayer != null && !isRefusedByMtkAudioProfile()){
//				am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
//						AudioManager.FLAG_PLAY_SOUND);
				mCloseDoorMediaPlayer.start();	
//				((Launcher)mContext).setVolumeControlStream(AudioManager.STREAM_MUSIC);
			}
		}
    }
    public void releaseDoorSounds(){
        if (mOpenDoorMediaPlayer != null) {
        	mOpenDoorMediaPlayer.release();
        	mOpenDoorMediaPlayer = null;
        }
        if(mCloseDoorMediaPlayer != null){
        	mCloseDoorMediaPlayer.release();
        	mCloseDoorMediaPlayer = null;
        }
    }
    public boolean isRefusedByMtkAudioProfile(){
    	String key = mProfileManager.getActiveProfileKey();
    	if(key.equals("mtk_audioprofile_silent") || key.equals("mtk_audioprofile_meeting")){
    		return true;
    	}
    	return false;
    }
    public void runDoorHandleAnim(){
    	if(mDoorHandleAniSet != null){
    		mDoorHandleAniSet.cancel();
    		mDoorHandleAniSet = null;
    	}
    	door_handle.setRotation(0);
    	door_frame.setAlpha(1f);
    	doorAndHandle.setRotationY(0f);
    	
        door_handle.setPivotX(77); //(width - 10);
        door_handle.setPivotY(8);
        Animator doorHandleAnim = ObjectAnimator.ofFloat(door_handle, "rotation", 0, -90);
        //doorHandleAnim = ObjectAnimator.ofFloat(door_handle, "rotation", -90, 0);
        mDoorHandleAniSet = new AnimatorSet();
        mDoorHandleAniSet.setDuration(500);
        mDoorHandleAniSet.addListener(amtla1);
        mDoorHandleAniSet.setStartDelay(500);
        mDoorHandleAniSet.play(doorHandleAnim);
        mDoorHandleAniSet.start();
    	
    }
    
    private void runWholeDoorAnim(){
    	if(mWholeDoorAniSet != null){
    		mWholeDoorAniSet.cancel();
    		mWholeDoorAniSet = null;
    	}
    	//door_frame.setAlpha(1f);
    	//doorAndHandle.setRotationY(0f);
        doorAndHandle.setPivotX(0);
        doorAndHandle.setPivotY(400);//(height/2);
        door.setPivotX(0);
        door.setPivotY(400);
        Animator doorAnim = ObjectAnimator.ofFloat(doorAndHandle, "rotationY", 0f, 90f);
        doorAnim.setDuration(1500);
        Animator doorFrameAnim = ObjectAnimator.ofFloat(door_frame, "alpha", 1f, 0f);
        doorFrameAnim.setDuration(1500);
        
        mWholeDoorAniSet = new AnimatorSet();
        //mWholeDoorAniSet.setDuration(2500);
        mWholeDoorAniSet.addListener(amtla4);
        mWholeDoorAniSet.playTogether(doorAnim, doorFrameAnim);
        mWholeDoorAniSet.start();
    }
    
    private void runCloseDoorHandleAnim(){
    	if(mDoorHandleAniSet != null){
    		mDoorHandleAniSet.cancel();
    		mDoorHandleAniSet = null;
    	}
        door_handle.setPivotX(77); //(width - 10);
        door_handle.setPivotY(8);
        Animator doorHandleAnim = ObjectAnimator.ofFloat(door_handle, "rotation", -90, 0);
        //doorHandleAnim = ObjectAnimator.ofFloat(door_handle, "rotation", -90, 0);
        mDoorHandleAniSet = new AnimatorSet();
        mDoorHandleAniSet.setDuration(500);
        mDoorHandleAniSet.addListener(amtla3);
        //mDoorHandleAniSet.setStartDelay(500);
        mDoorHandleAniSet.play(doorHandleAnim);
        mDoorHandleAniSet.start();
    	
    }
    
    public void runCloseWholeDoorAnim(){
    	if(mWholeDoorAniSet != null){
    		mWholeDoorAniSet.cancel();
    		mWholeDoorAniSet = null;
    	}
        door_handle.setPivotX(77); //(width - 10);
        door_handle.setPivotY(8);
    	door_handle.setRotation(-90);
    	door_frame.setAlpha(0f);
    	doorAndHandle.setRotationY(90f);

        doorAndHandle.setPivotX(0);
        doorAndHandle.setPivotY(400);//(height/2);
        door.setPivotX(0);
        door.setPivotY(400);
        Animator doorAnim = ObjectAnimator.ofFloat(doorAndHandle, "rotationY", 90f, 0f);
        doorAnim.setDuration(1500);
        Animator doorFrameAnim = ObjectAnimator.ofFloat(door_frame, "alpha", 0f, 1f);
        doorFrameAnim.setDuration(1500);
        
        mWholeDoorAniSet = new AnimatorSet();
        mWholeDoorAniSet.setStartDelay(500);
        mWholeDoorAniSet.addListener(amtla2);
        mWholeDoorAniSet.playTogether(doorAnim, doorFrameAnim);
        mWholeDoorAniSet.start();
    }
    
    
    public void setCloseDoorCallback(Runnable runnable){
    	mRunnable = runnable;
    }
    public void setCompleteCallback(Runnable runnable){
    	mCompleteRunnable = runnable;
    }
    
    private void addViews(Context context){
    	
    	door_frame = new ImageView(context);
        addView(door_frame);
        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) door_frame.getLayoutParams();
        door_frame.setImageResource(R.drawable.door_frame);
        params2.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params2.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params2.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        door_frame.setScaleType(ScaleType.FIT_XY);
        door_frame.setLayoutParams(params2);
    	
    	doorAndHandle = new FrameLayout(context);
    	
    	door = new ImageView(context);
    	doorAndHandle.addView(door);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) door.getLayoutParams();
        door.setImageResource(R.drawable.door);
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        door.setLayoutParams(params);
        door.setScaleType(ScaleType.FIT_XY);

    	door_handle = new ImageView(context);
    	doorAndHandle.addView(door_handle);
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) door_handle.getLayoutParams();
        door_handle.setImageResource(R.drawable.door_handle);
        params1.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params1.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params1.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        params1.rightMargin = (int) getResources().getDimension(R.dimen.door_handle_right_margin);
        params1.bottomMargin = (int) getResources().getDimension(R.dimen.door_handle_bottom_margin);
        door_handle.setLayoutParams(params1);
        
        addView(doorAndHandle);
    }
    
    private AnimatorListenerAdapter amtla1 = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            // TODO Auto-generated method stub
            super.onAnimationEnd(animation);
            runWholeDoorAnim();
        }
    };

    private AnimatorListenerAdapter amtla2 = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            // TODO Auto-generated method stub
            super.onAnimationEnd(animation);
            openDoorSounds(false);
            runCloseDoorHandleAnim();
        }
    };
    
    private AnimatorListenerAdapter amtla3 = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            // TODO Auto-generated method stub
            super.onAnimationEnd(animation);
//            postDelayed(mRunnable, 500);
            if(mRunnable != null)mRunnable.run();
//            releaseDoorSounds();
        }
    };
    
    private AnimatorListenerAdapter amtla4 = new AnimatorListenerAdapter() {
        @Override
		public void onAnimationStart(Animator animation) {
			super.onAnimationStart(animation);
			openDoorSounds(true);
		}

		@Override
        public void onAnimationEnd(Animator animation) {
            // TODO Auto-generated method stub
            super.onAnimationEnd(animation);
            setVisibility(View.INVISIBLE);
//            releaseDoorSounds();
            postDelayed(mCompleteRunnable, 1000);
        }
    };
}