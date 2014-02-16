package com.hct.scene.launcher2;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.hct.scene.launcher.R;

public class BobView extends View{
    private Drawable mBob;
    private Map<String, Drawable> mClockImage = new HashMap<String, Drawable>();
    public BobView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public BobView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }
    
    private void setupImage(Context context){
        mClockImage.clear();

        TypedArray imageType = context.getResources().obtainTypedArray(R.array.house1clocktype);
        TypedArray imageArray = context.getResources().obtainTypedArray(R.array.house1clock);

        for(int i=0;i<imageType.length();i++){
            String type = imageType.getString(i);
            Drawable image = imageArray.getDrawable(i);
            if(type != null){
                mClockImage.put(type, image);
            }   
        }

        imageType.recycle();
        imageArray.recycle();
    }
    
    private void init(Context context){
        setupImage(context);
        mBob = mClockImage.get("bob_princess");
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        mBob.setBounds(0, 0, mBob.getIntrinsicWidth(), mBob.getIntrinsicHeight());
        mBob.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
       setMeasuredDimension(mBob.getIntrinsicWidth(), mBob.getIntrinsicHeight());
    }
}
