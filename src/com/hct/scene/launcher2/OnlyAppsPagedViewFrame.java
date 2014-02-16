package com.hct.scene.launcher2;

import com.hct.scene.launcher.R;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.view.KeyEvent;
import android.view.View;

public class OnlyAppsPagedViewFrame extends FrameLayout implements LauncherTransitionable, View.OnKeyListener{
	private OnlyAppsPagedView mContent;
    private boolean mInTransition;
    private boolean mResetAfterTransition;
    private Animator mLauncherTransition;
	public OnlyAppsPagedViewFrame(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public OnlyAppsPagedViewFrame(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public OnlyAppsPagedViewFrame(Context context) {
		this(context, null, 0);

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mContent = (OnlyAppsPagedView) findViewById(R.id.only_apps_content);
		setOnKeyListener(this);
	}
	
    void reset() {
        if (mInTransition) {
            // Defer to after the transition to reset
            mResetAfterTransition = true;
        } else {
            // Reset immediately
            mContent.reset();
        }
    }
    private void enableAndBuildHardwareLayer() {
        // isHardwareAccelerated() checks if we're attached to a window and if that
        // window is HW accelerated-- we were sometimes not attached to a window
        // and buildLayer was throwing an IllegalStateException
        if (isHardwareAccelerated()) {
            // Turn on hardware layers for performance
            setLayerType(LAYER_TYPE_HARDWARE, null);

            // force building the layer, so you don't get a blip early in an animation
            // when the layer is created layer
            buildLayer();
        }
    }
    
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
        if (mLauncherTransition != null) {
            enableAndBuildHardwareLayer();
            mLauncherTransition.start();
            mLauncherTransition = null;
        } 
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	public boolean onLauncherTransitionStart(Launcher l, Animator animation,
			boolean toWorkspace) {
        mInTransition = true;
        boolean delayLauncherTransitionUntilLayout = false;
        boolean animated = (animation != null);
        mLauncherTransition = null;
        if (animated && mContent.getVisibility() == GONE) {
            mLauncherTransition = animation;
            delayLauncherTransitionUntilLayout = true;
        }
        mContent.setVisibility(VISIBLE);
        // The wrong question, for the animation to ensure display of status is correct
        mContent.setCurrentPage(mContent.getCurrentPage());
        if (!toWorkspace) {
            // Make sure the current page is loaded (we start loading the side pages after the
            // transition to prevent slowing down the animation)
        	mContent.loadAssociatedPages(mContent.getCurrentPage(), true);
        }
        if (animated && !delayLauncherTransitionUntilLayout) {
            enableAndBuildHardwareLayer();
        }

        if (!toWorkspace && !LauncherApplication.isScreenLarge()) {
//        	mContent.showDotIndicator();
        }
        if (mResetAfterTransition) {
        	mContent.reset();
            mResetAfterTransition = false;
        }
        return delayLauncherTransitionUntilLayout;
    }

	@Override
	public void onLauncherTransitionEnd(Launcher l, Animator animation,
			boolean toWorkspace) {
        mInTransition = false;
        if (animation != null) {
            setLayerType(LAYER_TYPE_NONE, null);
        }

        if (!toWorkspace) {
            // Dismiss the workspace cling and show the all apps cling (if not already shown)
//            l.dismissWorkspaceCling(null);
//            mContent.showAllAppsCling();

            // Make sure adjacent pages are loaded (we wait until after the transition to
            // prevent slowing down the animation)
        	mContent.loadAssociatedPages(mContent.getCurrentPage());
        	mContent.updateCurrentDotIndicator();
        }
    }
    boolean isTransitioning() {
        return mInTransition;
    }
    
    public void setContentVisibility(int visibility) {
    	mContent.setVisibility(visibility);
    }
    
    public int getContentVisibility() {
    	return mContent.getVisibility();
    }

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(getVisibility() != VISIBLE) return false;
		Log.d("furniture", "keyCode==="+keyCode+", event==="+event);
		if(v == this && mContent.isShake()){
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				mContent.startShakeAnimate(false);
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				break;
			default:
				return false;
			}
			return true;
		}
		return false;
	}
    
    
}
