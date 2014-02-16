package com.hct.scene.launcher2;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.android.music.IMediaPlaybackService;
import com.hct.scene.launcher.R;
public class MusicFurniture extends Furniture{
	//private MusicScene musicScene;
	private static final String TAG = "MusicScene";
	private MusicPlayerScene musicListScene;
	private MusicPlayerScene musicSongEntry;
	private MusicPlayerScene musicPlayerScene;
	private MusicPlayerScene musicSofaScene;
	private MusicPlayerScene musicGuitarTop;
	
	private MusicalNoteScene musicalNoteBlue;
	private MusicalNoteScene musicalNoteBlueOther;
	private MusicalNoteScene musicalNotePurple;
	private MusicalNoteScene musicalNotePurpleOther;
	private MusicalNoteScene musicalNoteYellow;
	private MusicalNoteScene musicalNoteYellowOther;
	private MusicalNoteScene musicalNotePurpleShort;
	private MusicalNoteScene musicalNoteYellowShort;
	private MusicalNoteScene musicalNoteBlueShort;

	private IMediaPlaybackService myservice;
	public static final String SERVICE_ACTION = "com.android.music.MediaPlaybackService";
	public static final String TOGGLEPAUSE_ACTION = "com.android.music.musicservicecommand.togglepause";
	public static final String PAUSE_ACTION = "com.android.music.musicservicecommand.pause";
	public static final String PREVIOUS_ACTION = "com.android.music.musicservicecommand.previous";
	public static final String NEXT_ACTION = "com.android.music.musicservicecommand.next";
	public static final String SONG_NAME_TAG = "track";
	public static final String START_PLAY_SERVICE_ACTION = "android.intent.action.startplayservice";
    public static final String PLAYSTATE_CHANGED = "com.android.music.playstatechanged";
    public static final String META_CHANGED = "com.android.music.metachanged";
    public static final String QUEUE_CHANGED = "com.android.music.queuechanged";
    public static final String QUIT_PLAYBACK = "com.android.music.quitplayback";
    public static final String PLAYBACK_COMPLETE = "com.android.music.playbackcomplete";

	private static boolean isAnimationRun = false;
	private static int style = 0;
	public MusicFurniture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initMusicCase();
	}

	public MusicFurniture(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initMusicCase();
	}

	public MusicFurniture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initMusicCase();
	}
	private void initMusicCase(){

		musicSofaScene = new MusicPlayerScene(getContext());
		musicSofaScene.setOnClickListener(this);
		LayoutParams musicSofaParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM|Gravity.LEFT);
		this.addView(musicSofaScene,musicSofaParams);
		int id = R.drawable.music_sofa;
		musicSofaScene.setMusicSceneStyle(id);
		musicSofaScene.setVisibility(INVISIBLE);
		
		musicListScene = new MusicPlayerScene(getContext());
		musicListScene.setOnClickListener(this);
		FrameLayout.LayoutParams musicListParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		musicListParam.gravity = Gravity.LEFT|Gravity.BOTTOM;
		musicListParam.bottomMargin = 10;
		this.addView(musicListScene,musicListParam);
		
		musicSongEntry = new MusicPlayerScene(getContext());
		musicSongEntry.setOnClickListener(this);
		FrameLayout.LayoutParams musicSongParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		musicSongParam.gravity = Gravity.LEFT|Gravity.BOTTOM;
		musicSongParam.bottomMargin = 175;
		this.addView(musicSongEntry,musicSongParam);
		
		musicGuitarTop = new MusicPlayerScene(getContext());
		musicGuitarTop.setOnClickListener(this);
		FrameLayout.LayoutParams musicGuitarParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		musicGuitarParam.gravity = Gravity.RIGHT|Gravity.BOTTOM;
		musicGuitarParam.bottomMargin = 30;
		musicGuitarParam.rightMargin = 80;
		this.addView(musicGuitarTop,musicGuitarParam);
		
		musicPlayerScene = new MusicPlayerScene(getContext());
		musicPlayerScene.setOnClickListener(this);
		FrameLayout.LayoutParams musicPlayParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		musicPlayParam.gravity = Gravity.RIGHT|Gravity.BOTTOM;
		musicPlayParam.bottomMargin = 30;
		musicPlayParam.rightMargin = 80;
		this.addView(musicPlayerScene,musicPlayParam);
			
		musicalNoteBlue = new MusicalNoteScene(getContext());
		musicalNoteBlue.setMusicalNoteScene(R.drawable.note_blue);
		musicalNoteBlue.setDrawingCacheEnabled(true);
		
		musicalNoteBlueOther = new MusicalNoteScene(getContext());
		musicalNoteBlueOther.setMusicalNoteScene(R.drawable.note_blue);
		musicalNoteBlueOther.setDrawingCacheEnabled(true);
		
		musicalNotePurple = new MusicalNoteScene(getContext());
		musicalNotePurple.setMusicalNoteScene(R.drawable.note_purple);
		musicalNotePurple.setDrawingCacheEnabled(true);
		
		musicalNotePurpleOther = new MusicalNoteScene(getContext());
		musicalNotePurpleOther.setMusicalNoteScene(R.drawable.note_purple);
		musicalNotePurpleOther.setDrawingCacheEnabled(true);
		
		musicalNoteYellow = new MusicalNoteScene(getContext());
		musicalNoteYellow.setMusicalNoteScene(R.drawable.note_yellow); 
		musicalNoteYellow.setDrawingCacheEnabled(true);
		
		musicalNoteYellowOther = new MusicalNoteScene(getContext());
		musicalNoteYellowOther.setMusicalNoteScene(R.drawable.note_yellow); 
		musicalNoteYellowOther.setDrawingCacheEnabled(true);
		
		LayoutParams musicNoteParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT|Gravity.BOTTOM);
		musicNoteParam.rightMargin = 155;
		musicNoteParam.bottomMargin = 210;
		this.addView(musicalNoteBlue,musicNoteParam);
		this.addView(musicalNoteBlueOther,musicNoteParam);
		this.addView(musicalNotePurple,musicNoteParam);
		this.addView(musicalNotePurpleOther,musicNoteParam);
		this.addView(musicalNoteYellow,musicNoteParam);
		this.addView(musicalNoteYellowOther,musicNoteParam);
		
		musicalNotePurpleShort = new MusicalNoteScene(getContext());
		musicalNotePurpleShort.setMusicalNoteScene(R.drawable.note_purple);
		musicalNotePurpleShort.setDrawingCacheEnabled(true);
		
		musicalNoteBlueShort = new MusicalNoteScene(getContext());
		musicalNoteBlueShort.setMusicalNoteScene(R.drawable.note_blue);
		musicalNoteBlueShort.setDrawingCacheEnabled(true);
		
		musicalNoteYellowShort = new MusicalNoteScene(getContext());
		musicalNoteYellowShort.setMusicalNoteScene(R.drawable.note_yellow);
		musicalNoteYellowShort.setDrawingCacheEnabled(true);
		
		LayoutParams musicShortNoteParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT|Gravity.BOTTOM);
		musicShortNoteParam.rightMargin = 200;
		musicShortNoteParam.bottomMargin = 210;
		this.addView(musicalNotePurpleShort,musicShortNoteParam);
		this.addView(musicalNoteBlueShort,musicShortNoteParam);
		this.addView(musicalNoteYellowShort,musicShortNoteParam);
		
		musicalNoteBlue.setVisibility(INVISIBLE);
		musicalNoteBlueOther.setVisibility(INVISIBLE);
		musicalNotePurple.setVisibility(INVISIBLE);
		musicalNotePurpleOther.setVisibility(INVISIBLE);
		musicalNoteYellow.setVisibility(INVISIBLE);
		musicalNoteYellowOther.setVisibility(INVISIBLE);
		
		musicalNotePurpleShort.setVisibility(INVISIBLE);
		musicalNoteBlueShort.setVisibility(INVISIBLE);
		musicalNoteYellowShort.setVisibility(INVISIBLE);
	}
	@Override
	protected Rect getHighLightFrame() {
		// TODO Auto-generated method stub
		Rect rect = mTempRect;
		rect.left = musicSofaScene.getLeft()+2;
		rect.top = musicSofaScene.getTop();
		rect.right = musicSofaScene.getRight();
		rect.bottom = musicSofaScene.getBottom()-2;
		return rect;
	}
	
	@Override
	public void changeStyle(int style) {
		// TODO Auto-generated method stub
		super.changeStyle(style);
		musicSofaScene.setOnLongClickListener(mLongClickListener);
		musicListScene.setOnLongClickListener(mLongClickListener);
		musicPlayerScene.setOnLongClickListener(mLongClickListener);
		musicSongEntry.setOnLongClickListener(mLongClickListener);
		musicGuitarTop.setOnLongClickListener(mLongClickListener);
		this.style = style;
		//changeSofaStyle();
		changeListStyle();
		changePlayerStyle();
		changeSongStyle();
		changeGuitarTopStyle();
	}
	public void changeGuitarTopStyle(){
		int id = 0;
		if(style == 1){
			id = R.drawable.guitar_ranee_top;
		}else{
			id = R.drawable.guitar_fresh_top;
		}
		musicGuitarTop.setMusicSceneStyle(id);
	}
	public void changeSongStyle(){
		int id = R.drawable.song_fresh;
		musicSongEntry.setMusicSceneStyle(id);
	}
	public void changeListStyle(){
		int id = R.drawable.song_fresh_bottom;
		musicListScene.setMusicSceneStyle(id);
	}
	public void changeSofaStyle(){
		int id = R.drawable.music_sofa;
		musicSofaScene.setMusicSceneStyle(id);
	}
	public void changePlayerStyle(){
		int id = 0;
		if(style == 1){
			if(isMusicPlay()){
				id = R.drawable.guitar_ranee_play;
			}else{
				id = R.drawable.guitar_ranee_pause;
			}
		}else{
			if(isMusicPlay()){
				id = R.drawable.guitar_fresh_play;
			}else{
				id = R.drawable.guitar_fresh_pause;
			}
		}
		musicPlayerScene.setMusicSceneStyle(id);
	}
	@Override
	public boolean isAnimationObj() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onAnimation(int width) {
		// TODO Auto-generated method stub
		super.onAnimation(width);
		startEnterAnimation();
	}

	@Override
	public void stopAnimation(int width) {
		// TODO Auto-generated method stub
		super.stopAnimation(width);
		stopEnterAnimation();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mZoomMode){
			mSingleClickListener.onClick(v);
		}else{
			if(v.equals(musicSongEntry)){
				Intent intent = new Intent(); 
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.setAction("android.intent.action.MUSIC_PLAYER");
				getContext().startActivity(intent);
			} else if (v.equals(musicPlayerScene) && !(mAddApps != null &&
					mAddApps.isRefused())) {
				musicPlay();
			}
		}
	}
	private void setPlayAnimationState(){
		if(isExistPlayList()){
			if(isMusicPlay()){
				startPlayAnimation();
			}else{
				stopPlayAnimation();
			}
		}
	}
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			Log.d(TAG, "action = " + action);
			if(action.equals(PLAYSTATE_CHANGED)){
				if(hasWindowFocus()){
					setPlayAnimationState();
					changePlayerStyle();
				}
			}
		}
		
	};
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		bindmusicservice();
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PLAYSTATE_CHANGED);
        getContext().registerReceiver(mBroadcastReceiver, intentFilter);
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		getContext().unregisterReceiver(mBroadcastReceiver);
		getContext().unbindService(mserviceconnect);
		stopEnterAnimation();
		stopPlayAnimation();
	}
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasWindowFocus);
		if(hasWindowFocus){
			setPlayAnimationState();
			changePlayerStyle();
		}
	}

	private void musicPlay(){
		if (isCheckSDCard()) {
			Log.d(TAG, "opterate = isCheckSDCard() --> " + isCheckSDCard());
			return;
		}
		if (isExistPlayList() == false){
			Log.d(TAG, "opterate = isExistPlayList() --> " + isExistPlayList());
			return;
		}
		if (getMountMusicNum() == 0){
			Log.d(TAG, "opterate = getMountMusicNum() --> " + getMountMusicNum());
			return;
		}
		Intent localIntent = new Intent();
		localIntent.setClassName("com.android.music",SERVICE_ACTION);
		localIntent.setAction(TOGGLEPAUSE_ACTION);
		getContext().startService(localIntent);
/*
		if (isMusicServiceOpen()) {
			Log.d(TAG, "opterate = isMusicServiceOpen() --> " + isMusicServiceOpen());
			Intent intent = new Intent(TOGGLEPAUSE_ACTION);
			getContext().sendBroadcast(intent);
		} else {
			Log.d(TAG, "opterate = isMusicPlay() --> " + isMusicPlay());
			
			if (isMusicPlay()) {
				Intent intent = new Intent(PAUSE_ACTION);
				getContext().sendBroadcast(intent);
			} else {
				Intent intent = new Intent(TOGGLEPAUSE_ACTION);
				getContext().sendBroadcast(intent);
			}
			
		}
*/
	}
	private boolean isMusicPlay() {
		final AudioManager am = (AudioManager) getContext().getSystemService(
				Context.AUDIO_SERVICE);
		boolean musicplaying = false;
		if (am == null) {
			return false;
		}
		try {

			if (myservice != null)
				musicplaying = myservice.isPlaying();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return am.isMusicActive() && musicplaying;

	}
	private boolean isMusicServiceOpen() {

		ActivityManager amanager = (ActivityManager) getContext()
				.getSystemService(getContext().ACTIVITY_SERVICE);

		List<RunningServiceInfo> infos = amanager.getRunningServices(30);

		for (RunningServiceInfo info : infos) {
			if (info.service.getClassName().equals(SERVICE_ACTION)) {
				return true;
			}
		}
		return false;

	}
	private boolean isCheckSDCard() {

		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_SHARED)
				|| status.equals(Environment.MEDIA_UNMOUNTED)
				|| status.equals(Environment.MEDIA_REMOVED)) {
			return true;
		}
		return false;
	}
	private int getMountMusicNum() {
		int num = 0;
		long[] list = null;
		list = getAllSongs(getContext());
		if (list != null)
			num = list.length;
		return num; 
	}
	public static Cursor query(Context context, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder,
			int limit) {
		try {
			ContentResolver resolver = context.getContentResolver();
			if (resolver == null) {
				return null;
			}
			if (limit > 0) {
				uri = uri.buildUpon().appendQueryParameter("limit", "" + limit)
						.build();
			}
			return resolver.query(uri, projection, selection, selectionArgs,
					sortOrder);
		} catch (UnsupportedOperationException ex) {
			return null;
		}

	}

	public static Cursor query(Context context, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		return query(context, uri, projection, selection, selectionArgs,
				sortOrder, 0);
	}
	public static long[] getAllSongs(Context context) {
		Cursor c = query(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media._ID },
				MediaStore.Audio.Media.IS_MUSIC + "=1", null, null);
		
		try {
			if (c == null) {
				return null;
			}
			int len = c.getCount();
			long[] list = new long[len];
			for (int i = 0; i < len; i++) {
				c.moveToNext();
				list[i] = c.getLong(0);
			}

			return list;
		} finally {
			if (c != null) {
				c.close();
			}
		}
	}
	private boolean isExistPlayList() {
		long[] playlist = null;
		long audio_id = -1;
		boolean ret = false;
		if (myservice != null) {

			try {
				playlist = myservice.getQueue();
				audio_id = myservice.getAudioId();// getAudioId
				if(playlist != null){
					ret = true;
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				ret = false;
				e.printStackTrace();
			}

		}

		return ret;

	}
	private ServiceConnection mserviceconnect = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			myservice = null;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onServiceConnected");
			myservice = IMediaPlaybackService.Stub.asInterface(service);
		}
	};

	private void bindmusicservice() {

		Log.d(TAG, "bindmusicservice");
		Intent service = new Intent(SERVICE_ACTION);
		getContext().bindService(service, mserviceconnect,getContext().BIND_AUTO_CREATE);
	}
	Runnable purpleOtherRunnable = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			musicalNotePurpleOther.purpleTranslate();
			musicalNotePurpleOther.setVisibility(VISIBLE);
		}
	};
	Runnable buleRunnable = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			musicalNoteBlue.blueTranslate();
			musicalNoteBlue.setVisibility(VISIBLE);
		}
	};
	Runnable buleOtherRunnable = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			musicalNoteBlueOther.blueTranslate();
			musicalNoteBlueOther.setVisibility(VISIBLE);
		}
	};
	Runnable yellowRunnable = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			musicalNoteYellow.yellowTranslate();
			musicalNoteYellow.setVisibility(VISIBLE);
		}
	};
	Runnable yellowOtherRunnable = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			musicalNoteYellowOther.yellowTranslate();
			musicalNoteYellowOther.setVisibility(VISIBLE);
		}
	};
	public void startPlayAnimation(){
		stopPlayAnimation();
		Log.d(TAG, "start play animation ");
		isAnimationRun = true;
		musicalNotePurple.purpleTranslate();
		musicalNotePurple.setVisibility(VISIBLE); //5000
		getHandler().postDelayed(yellowRunnable, 1000); //6000
		getHandler().postDelayed(buleRunnable, 2000); //7000
		getHandler().postDelayed(purpleOtherRunnable, 2500);
		getHandler().postDelayed(yellowOtherRunnable, 4000);
		getHandler().postDelayed(buleOtherRunnable, 5500);
		stopEnterAnimation();
	}
	public void stopPlayAnimation(){
		Log.d(TAG, "stop play animation ");
		isAnimationRun = false;
		if(yellowRunnable != null){
			getHandler().removeCallbacks(yellowRunnable);
		}
		if(buleRunnable != null){
			getHandler().removeCallbacks(buleRunnable);
		}
		if(purpleOtherRunnable != null){
			getHandler().removeCallbacks(purpleOtherRunnable);
		}
		if(yellowOtherRunnable != null){
			getHandler().removeCallbacks(yellowOtherRunnable);
		}
		if(buleOtherRunnable != null){
			getHandler().removeCallbacks(buleOtherRunnable);
		}
		musicalNotePurple.purpleTranslateEnd();
		musicalNotePurpleOther.purpleTranslateEnd();
		musicalNoteBlue.blueTranslateEnd();
		musicalNoteBlueOther.blueTranslateEnd();
		musicalNoteYellow.yellowTranslateEnd();
		musicalNoteYellowOther.yellowTranslateEnd();
	}
	Runnable purpleShortRunnable = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			musicalNotePurpleShort.shortTranslate();
			musicalNotePurpleShort.setVisibility(VISIBLE);
		}
	};
	Runnable yellowShortRunnable = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			musicalNoteYellowShort.shortTranslate();
			musicalNoteYellowShort.setVisibility(VISIBLE);
		}
	};
	Runnable blueShortRunnable = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			musicalNoteBlueShort.shortTranslate();
			musicalNoteBlueShort.setVisibility(VISIBLE);
		}
	};
	public void startEnterAnimation(){
		if(isAnimationRun == false){
			stopEnterAnimation();
			Log.d(TAG, "startEnterAnimation ");
			musicalNotePurpleShort.shortTranslate();
			musicalNotePurpleShort.setVisibility(VISIBLE); //6000
			getHandler().postDelayed(yellowShortRunnable, 1500); //5000
			getHandler().postDelayed(blueShortRunnable, 3000);
		}
	}
	public void stopEnterAnimation(){
		Log.d(TAG, "stopEnterAnimation ");
		if(yellowShortRunnable != null){
			getHandler().removeCallbacks(yellowShortRunnable);
		}
		if(blueShortRunnable != null){
			getHandler().removeCallbacks(blueShortRunnable);
		}
		musicalNotePurpleShort.shortTranslateEnd();
		musicalNoteYellowShort.shortTranslateEnd();
		musicalNoteBlueShort.shortTranslateEnd();
	}
}
