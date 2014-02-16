package com.hct.scene.launcher2;

import java.io.InputStream;
import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hct.scene.launcher.R;

public class HouseWorkspace extends Workspace implements View.OnClickListener{
	private static final String TAG = "HouseWorkspace";
	private LoadHouseWorkspaceIcons mHouseIconsContainer;
	public HouseWorkspace(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        
        mSpringLoadedShrinkFactor =  getResources().getInteger
        		(R.integer.config_houseWorkspaceSpringLoadShrinkPercentage) / 100.0f;
		mDotIndTranslateY = getResources().getDimensionPixelSize(R.dimen.dot_ind_margin_translateY)*1.0f;
	}

	public HouseWorkspace(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@Override
	protected void initWorkspace() {
        Context context = getContext();
        mCurrentPage = mDefaultPage;
        mCircleFlag = false;
        LauncherApplication app = (LauncherApplication)context.getApplicationContext();
        mIconCache = app.getIconCache();
        mExternalDragOutlinePaint.setAntiAlias(true);
        setWillNotDraw(false);
        setChildrenDrawnWithCacheEnabled(true);

        mChangeStateAnimationListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsSwitchingState = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsSwitchingState = false;
//                mWallpaperOffset.setOverrideHorizontalCatchupConstant(false);
                mAnimator = null;
                updateChildrenLayersEnabled();
            }
        };
        mSnapVelocity = 350;
        mWallpaperOffset = new WallpaperOffsetInterpolator();
        Display display = mLauncher.getWindowManager().getDefaultDisplay();
        mDisplayWidth = display.getWidth();
        mDisplayHeight = display.getHeight();
    }

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		Context context = getContext();
		
	}
	public void setWall(int style){
		int arrayId = 0;
		if(0 == style){
			arrayId = R.array.house_bkg;
		}else if(1 == style){
			arrayId = R.array.house_princess_bkg;
		}else{
			return;
		}
		/*
	    TypedArray bkgs = getResources().obtainTypedArray(arrayId);
	    final int count = bkgs.length();//getChildCount();
	    for (int i = 0; i < count; i++) {
	    	CellLayout layout =(CellLayout)getChildAt(i);
			layout.setBackgroundDrawable(null);
	    }
	    for (int i = 0; i < count; i++) {
			CellLayout layout =(CellLayout)getChildAt(i);
			layout.setBackgroundDrawable(bkgs.getDrawable(i));
		}
	    bkgs.recycle();
	    */
		String[] res = getResources().getStringArray(arrayId);
		if(res.length != getChildCount()) return;
		for (int i = 0; i < res.length; i++) {
			Bitmap b = null;
			try {
				b = readBitMap(getContext(), getResources().getIdentifier(res[i], "drawable", mLauncher.getPackageName()));
				Drawable d = new FastBitmapDrawable(b);
				CellLayout layout =(CellLayout)getChildAt(i);
				layout.setBackgroundDrawable(d);
//				if(!b.isRecycled()){
//					b.recycle();
//				}
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
    private void displayBriefMemory() {    
        final ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);    
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();   
        activityManager.getMemoryInfo(info);    
        Log.i("furniture","系统剩余内存:"+(info.availMem >> 10)+"k");   
        Log.i("furniture","系统是否处于低内存运行："+info.lowMemory);
        Log.i("furniture","当系统剩余内存低于"+info.threshold+"时就看成低内存运行");
        Log.i("furniture","runtime剩余内存是:"+Runtime.getRuntime().freeMemory()+",总内存是:"+
        		Runtime.getRuntime().totalMemory()+",max内存是:"+Runtime.getRuntime().maxMemory());
    } 

	@Override
	protected int getMaxPageNum() {
		return Launcher.MAX_HOUSE_SCREEN;
	}

	@Override
	protected int getDotCount() {
		return getPageCount()-2;
	}

	

	@Override
	protected int getDotId(int page) {
		return page - 1;
	}

	@Override
	protected int minPage() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	protected int maxPage() {
		// TODO Auto-generated method stub
		return getPageCount()-2;
	}

	@Override
	protected boolean limitScroll() {
		if(getScrollX() < (getChildOffset(1) - getRelativeChildOffset(1))
				|| getScrollX()> (getChildOffset(getPageCount()-2) - getRelativeChildOffset(getPageCount()-2))){
			return false;
		}
		return true;
	}

	@Override
	protected int getCutParts() {
		return getWidth();
	}

	@Override
	protected boolean isCancelDotClick() {
		return true;
	}

	@Override
	protected boolean hasHotseat() {
		return false;
	}

	@Override
	protected boolean expandPagesToDraw() {
		return isZoom();
	}

	@Override
	public void updateWallpaperOffsetImmediately() {
		// TODO Auto-generated method stub
		super.updateWallpaperOffsetImmediately();
	}

	@Override
	protected void syncWallpaperOffsetWithScroll() {

	}

	@Override
	protected void updateWallpaperOffsets() {
//		// TODO Auto-generated method stub
//		super.updateWallpaperOffsets();
	}

	@Override
	protected void onWallpaperTap(MotionEvent ev) {
		//not respond to wallpaper, but to house wallbg
//		if(isZoom()){
//			Log.d("furniture", "click on houseWorkspace");
//			mLauncher.getHouseStyleTransfer().setFocusView(this);
//			mLauncher.getHouseStyleTransfer().updateStyle(getResources().getString(R.string.wall),
//					R.array.house_wall, mLauncher.getHouseWall());
//		}
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
        final int count = getPageCount();
        for (int i = 0; i < count; i++) {
            getPageAt(i).setOnClickListener(l);
        }
	}

	@Override
	protected void restorePageStatus() {
	}

	@Override
	protected void initCellCountXY(int x, int y) {
		LauncherModel.updateHouseWorkspaceLayoutCells(x, y);
	}
	public void setHouseIconsContainer(LoadHouseWorkspaceIcons container){
		mHouseIconsContainer = container;
	}
	public LoadHouseWorkspaceIcons getHouseIconsContainer(){
		return mHouseIconsContainer;
	}
	@Override
	void addInScreen(View child, long container, int screen, int x, int y,
			int spanX, int spanY, boolean insert) {
		
        if (container == LauncherSettings.Favorites.CONTAINER_HOUSE) {
            if (screen < 0 || screen >= getChildCount()) {
                Log.e(TAG, "The screen must be >= 0 and < " + getChildCount()
                    + " (was " + screen + "); skipping child");
                return;
            }
        }
        final CellLayout layout;
        layout = (CellLayout) getChildAt(screen);
//        child.setOnKeyListener(new IconKeyEventListener());
        
        CellLayout.LayoutParams lp = (CellLayout.LayoutParams) child.getLayoutParams();
        if (lp == null) {
            lp = new CellLayout.LayoutParams(x, y, spanX, spanY);
        } else {
            lp.cellX = x;
            lp.cellY = y;
            lp.cellHSpan = spanX;
            lp.cellVSpan = spanY;
        }
        if (spanX < 0 && spanY < 0) {
            lp.isLockedToGrid = false;
        }
     // Get the canonical child id to uniquely represent this view in this screen
        int childId = LauncherModel.getCellLayoutChildId(container, screen, x, y, spanX, spanY);
        boolean markCellsAsOccupied = !(child instanceof Folder);
        if (!layout.addViewToCellLayout(child, insert ? 0 : -1, childId, lp, markCellsAsOccupied)) {
            Log.w(TAG, "Failed to add to item at (" + lp.cellX + "," + lp.cellY + ") to CellLayout");
        }
        if (!(child instanceof Folder)) {
            child.setHapticFeedbackEnabled(false);
//            child.setOnLongClickListener(mLongClickListener);
        }
//        if (child instanceof DropTarget) {
//            mDragController.addDropTarget((DropTarget) child);
//        }
	}

	void addInShelf(View v, ItemInfo item, boolean save){
		if(mHouseIconsContainer != null){
			mHouseIconsContainer.addAppsInScreen(v, item, save);
		}
	}

	void removeShelfShortcutInfo(ArrayList<ShortcutInfo> list) {
		if (mHouseIconsContainer != null) {
			mHouseIconsContainer.removeShortcutInfo(list);
		}
	}

	@Override
	void removeItems(ArrayList<ApplicationInfo> apps) {
		if(mHouseIconsContainer != null){
			mHouseIconsContainer.removeApps(apps);
		}
	}

	@Override
	void updateShortcuts(ArrayList<ApplicationInfo> apps) {
		if(mHouseIconsContainer != null){
			mHouseIconsContainer.updateApps(apps);
		}
	}

	@Override
	public void updateComponentUnreadChanged(ComponentName component,
			int unreadNum) {
	}

	@Override
	public void updateShortcutsAndFoldersUnread() {
		
	}

	@Override
	protected void notifyPageSwitchListener() {
//		super.notifyPageSwitchListener();
		//Log.d("furniture", "notifyPageSwitchListener----mCurrentPage=="+mCurrentPage);
		if(isPageMoving() && mHouseIconsContainer != null)
			mHouseIconsContainer.startShakeAnimate(false);
	}

	@Override
	protected void onPageBeginMoving() {
		super.onPageBeginMoving();
		CellLayout layout = (CellLayout) getChildAt(mCurrentPage);
		layout.updateAllCellsAnimation(false);
		mLauncher.getPetFurniture().setLastPage(mCurrentPage);
		mLauncher.getPetFurniture().stopAnimation(getMeasuredWidth());
	}

	@Override
	protected void onPageEndMoving() {
		super.onPageEndMoving();
		Log.d("furniture", "onPageEndMoving--"+this);
		CellLayout layout = (CellLayout) getChildAt(mCurrentPage);
		layout.updateAllCellsAnimation(true);
		mLauncher.getPetFurniture().setCurrentPage(mCurrentPage);
		mLauncher.getPetFurniture().onAnimation(getMeasuredWidth());
	}
	protected void notifyHouse(){
		//the pet run from leftside at the first time
		mLauncher.getPetFurniture().setLastPage(mCurrentPage-1);
		mLauncher.getPetFurniture().setCurrentPage(mCurrentPage);
		mLauncher.getPetFurniture().onAnimation(getMeasuredWidth());
	}
	@Override
	protected int getDotIndicatorBottomMargin() {
		return getResources().getDimensionPixelSize(R.dimen.houseworkspace_indicator_bottom_margin);
	}

	@Override
	void scroll3D(int screenCenter) {
		
	}
	
	@Override
	void scroll3D(int screenCenter, int style) {
	}

	public void startCatch(View cell){
		final View child = cell; 
		if(child instanceof Furniture){
			Log.d("furniture", "startCatch--child==="+child);
			Furniture furniture = (Furniture)child;
//			((Furniture)child).setHightFlag(true);
			drawHightLight(furniture);
			//search other same name furnitures 
			FurnitureInfo info = (FurnitureInfo) furniture.getTag();
			final int count = getChildCount();
			final int pageId = info.screen;
			for (int i = pageId-1; i <= pageId+1; i++) {
				if(i != pageId && i>=0 && i<count){
					CellLayout layoutSide = (CellLayout)getChildAt(i);
					Furniture furnitureSide = isSameNameFurniture(layoutSide, 
							info.title.toString());
					if(furnitureSide != null){
						drawHightLight(furnitureSide);
						break;
					}
				}
			}
			Log.d("furniture", "startcatch--style--"+info.style);
			mLauncher.getHouseStyleTransfer().setFocusView(furniture);
			mLauncher.getHouseStyleTransfer().updateStyle(info.title.toString(),
					furniture.getClass().getSimpleName(), info.style);
		}
		
	}
	public void changeAllSameFurnitureStyle(Furniture focusFurniture, int style){
		final int count = getChildCount();
		final FurnitureInfo info = (FurnitureInfo) focusFurniture.getTag();
		for (int i = 0; i < count; i++) {
			CellLayout layoutSide = (CellLayout)getChildAt(i);
			Furniture otherFurnitures = isSameNameFurniture(layoutSide, 
					info.title.toString());
			if(otherFurnitures != null && otherFurnitures != focusFurniture){
				otherFurnitures.changeStyle(style);
				otherFurnitures.updateDatabase(style);
			}
		}
	}
	private void drawHightLight(Furniture furniture){
		final Rect r = furniture.getHighLightFrame();
		r.offset(furniture.getLeft(), furniture.getTop());
		ViewGroup parent = (ViewGroup)furniture.getParent();
//		ViewGroup superParent = (ViewGroup)parent.getParent();
		r.offset(parent.getLeft(), parent.getTop());
		CellLayout layout = (CellLayout) furniture.getParent().getParent();
		layout.setHighLightRect(r);
	}
	private Furniture isSameNameFurniture(CellLayout layout, String title){
		CellLayoutChildren layoutChildren = layout.getChildrenLayout();
		final int count = layoutChildren.getChildCount();
		for (int i = 0; i < count; i++) {
			Furniture furniture = (Furniture)layoutChildren.getChildAt(i);
			FurnitureInfo info = (FurnitureInfo) furniture.getTag();
			if(info.title.equals(title)){
				return furniture;
			}
		}
		return null;
	}
	
	public void exitCatch(){
//		invalidate();
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			CellLayout layout = (CellLayout) getChildAt(i);
			/*
			CellLayoutChildren layoutChildren = layout.getChildrenLayout();
			final int cellCount = layoutChildren.getChildCount();
			for (int j = 0; j < cellCount; j++) {
				Furniture furniture = (Furniture)layoutChildren.getChildAt(j);
				if(furniture.getHightFlag()){
					furniture.setHightFlag(false);
					break;
				}
			}
			*/
			if(layout.getHightLightRect() != null){
				layout.setHighLightRect(null);
			}
		}
	}
	
	public void startCatchBeforeExit(View v){
		//first clear the residual hight frame
		exitCatch();
		while(!(v instanceof Furniture)){
			v = (View)v.getParent();
		}
		startCatch(v);
	}
	//click for furniture
	@Override
	public void onClick(View v) {
		if(isZoom()){
			startCatchBeforeExit(v);
		}
	}
	
	@Override
	protected void screenScrolledStandardUI(int screenCenter) {
		super.screenScrolledStandardUI(screenCenter);
		if(!isZoom()){
			//for pet furniture
			mLauncher.getPetFurniture().updateOffset(getScrollX() - getChildOffset(mCurrentPage));
		}
		
	}

}

interface LoadHouseWorkspaceIcons {
	public void addAppsInScreen(View v, ItemInfo item, boolean save);
	public void removeShortcutInfo(ArrayList<ShortcutInfo> apps);

	public void removeApps(ArrayList<ApplicationInfo> apps);
	public void updateApps(ArrayList<ApplicationInfo> apps);
	public void startShakeAnimate(boolean on);
	public boolean isAnimateOn();
}
