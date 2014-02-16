package com.hct.scene.launcher2;
import java.util.Random;

import android.graphics.PointF;
import android.view.animation.Animation;
import android.view.animation.Transformation;
 
// http://www.math.ubc.ca/~cass/gfx/bezier.html
 
public class ArcTranslateAnimation extends Animation {
	private int mFromXType = ABSOLUTE;
	private int mToXType = ABSOLUTE;
 
	private int mFromYType = ABSOLUTE;
	private int mToYType = ABSOLUTE;
 
	private float mFromXValue = 0.0f;
	private float mToXValue = 0.0f;
 
	private float mFromYValue = 0.0f;
	private float mToYValue = 0.0f;
 
	private float mFromXDelta;
	private float mToXDelta;
	private float mFromYDelta;
	private float mToYDelta;
 
	private PointF mStart;
	private PointF mControl1;
	private PointF mControl2;
	private PointF mControl3;
	private PointF mControl4;
	private PointF mEnd;
	Random random;
	/**
	 * Constructor to use when building a ArcTranslateAnimation from code
	 * 
	 * @param fromXDelta
	 *            Change in X coordinate to apply at the start of the animation
	 * @param toXDelta
	 *            Change in X coordinate to apply at the end of the animation
	 * @param fromYDelta
	 *            Change in Y coordinate to apply at the start of the animation
	 * @param toYDelta
	 *            Change in Y coordinate to apply at the end of the animation
	 */
	public ArcTranslateAnimation(float fromXDelta, float toXDelta,
			float fromYDelta, float toYDelta) {
		mFromXValue = fromXDelta;
		mToXValue = toXDelta;
		mFromYValue = fromYDelta;
		mToYValue = toYDelta;
 
		mFromXType = ABSOLUTE;
		mToXType = ABSOLUTE;
		mFromYType = ABSOLUTE;
		mToYType = ABSOLUTE;
	}
 
	/**
	 * Constructor to use when building a ArcTranslateAnimation from code
	 * 
	 * @param fromXType
	 *            Specifies how fromXValue should be interpreted. One of
	 *            Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
	 *            Animation.RELATIVE_TO_PARENT.
	 * @param fromXValue
	 *            Change in X coordinate to apply at the start of the animation.
	 *            This value can either be an absolute number if fromXType is
	 *            ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
	 * @param toXType
	 *            Specifies how toXValue should be interpreted. One of
	 *            Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
	 *            Animation.RELATIVE_TO_PARENT.
	 * @param toXValue
	 *            Change in X coordinate to apply at the end of the animation.
	 *            This value can either be an absolute number if toXType is
	 *            ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
	 * @param fromYType
	 *            Specifies how fromYValue should be interpreted. One of
	 *            Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
	 *            Animation.RELATIVE_TO_PARENT.
	 * @param fromYValue
	 *            Change in Y coordinate to apply at the start of the animation.
	 *            This value can either be an absolute number if fromYType is
	 *            ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
	 * @param toYType
	 *            Specifies how toYValue should be interpreted. One of
	 *            Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
	 *            Animation.RELATIVE_TO_PARENT.
	 * @param toYValue
	 *            Change in Y coordinate to apply at the end of the animation.
	 *            This value can either be an absolute number if toYType is
	 *            ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
	 */
	public ArcTranslateAnimation(int fromXType, float fromXValue, int toXType,
			float toXValue, int fromYType, float fromYValue, int toYType,
			float toYValue) {
 
		mFromXValue = fromXValue;
		mToXValue = toXValue;
		mFromYValue = fromYValue;
		mToYValue = toYValue;
 
		mFromXType = fromXType;
		mToXType = toXType;
		mFromYType = fromYType;
		mToYType = toYType;
	}
 
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float dx = calcBezier(interpolatedTime, mStart.x, mControl1.x, mControl2.x,mControl3.x, mControl4.x, mEnd.x);
		float dy = calcBezier(interpolatedTime, mStart.y, mControl1.y, mControl2.y,mControl3.y, mControl4.y, -mToYDelta/8);
	//	Log.d(TAG, "mStart.x = " + mStart.x + ", mControl.x = " + mControl.x + ", mControls.x = " + mControls.x + ", mEnd.x = " + mEnd.x);
	//	Log.d(TAG, "mStart.y = " + mStart.y + ", mControl.y = " + mControl.y + ", mControls.y = " + mControls.y + ", mEnd.y = " + mEnd.y);
	//	Log.d(TAG, "dx = " + dx + ", dy = " + dy + ", interpolatedTime = " + interpolatedTime);
		t.getMatrix().setTranslate(dx, dy);
	}
 
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mFromXDelta = resolveSize(mFromXType, mFromXValue, width, parentWidth);
		mToXDelta = resolveSize(mToXType, mToXValue, width, parentWidth);
		mFromYDelta = resolveSize(mFromYType, mFromYValue, height, parentHeight);
		mToYDelta = resolveSize(mToYType, mToYValue, height, parentHeight);
 
		mStart = new PointF(mFromXDelta, mFromYDelta);
		mEnd = new PointF(mToXDelta, mToYDelta);

		mControl1 = new PointF(mToXDelta*2/5,mToYDelta/8);
		mControl2 = new PointF(mToXDelta*1/2,mToYDelta/4);
		mControl3 = new PointF(mToXDelta*3/5,-mToYDelta/2);
		mControl4 = new PointF(mToXDelta*4/5,-mToYDelta);		
	}
 
	/**
	 * Calculate the position on a quadratic bezier curve by given three points
	 * and the percentage of time passed.
	 * 
	 * from http://en.wikipedia.org/wiki/B%C3%A9zier_curve
	 * 
	 * @param interpolatedTime
	 *            the fraction of the duration that has passed where 0 <= time
	 *            <= 1
	 * @param p0
	 *            a single dimension of the starting point
	 * @param p1
	 *            a single dimension of the control point
	 * @param p2
	 *            a single dimension of the ending point
	 */
	private long calcBezier(float interpolatedTime, float p0, float p1, float p2, float p3,float p4, float p5) {
		//n = 2
		/*
		return Math.round((Math.pow((1 - interpolatedTime), 2) * p0)
				+ (2 * (1 - interpolatedTime) * interpolatedTime * p1)
				+ (Math.pow(interpolatedTime, 2) * p2));
		*/
		//n = 3
		/*
		return (long)(Math.pow((1-interpolatedTime), 3)*p0 
			+3*Math.pow((1-interpolatedTime), 2)*interpolatedTime*p1
			+3*Math.pow((1-interpolatedTime), 2)*interpolatedTime*p2
			+Math.pow(interpolatedTime,3)*p3);
		*/
		//n = 5
		return (long)(Math.pow((1-interpolatedTime), 5)*p0 
				+5*Math.pow((1-interpolatedTime), 4)*interpolatedTime*p1
				+10*Math.pow((1-interpolatedTime), 3)*Math.pow(interpolatedTime, 2)*p2
				+10*Math.pow((1-interpolatedTime), 2)*Math.pow(interpolatedTime, 3)*p3
				+5*Math.pow(interpolatedTime, 4)*(1-interpolatedTime)*p4
				+Math.pow(interpolatedTime, 5)*p5);
	}
 
}
