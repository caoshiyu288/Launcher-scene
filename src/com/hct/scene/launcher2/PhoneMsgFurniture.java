
package com.hct.scene.launcher2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

public class PhoneMsgFurniture extends Furniture {

    private DesktopCallAndMsg mDesktopCallAndMsg;

    public PhoneMsgFurniture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public PhoneMsgFurniture(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public PhoneMsgFurniture(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        mDesktopCallAndMsg = new DesktopCallAndMsg(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM);
        addView(mDesktopCallAndMsg, layoutParams);
        mDesktopCallAndMsg.setClickListener(this);
    }

    @Override
    protected Rect getHighLightFrame() {
        // TODO Auto-generated method stub
        Rect rect = new Rect();
        mDesktopCallAndMsg.getHitRect(rect);
        rect.left += 2;
        rect.right -= 2;
        rect.bottom -=2;
        return rect;
    }

    @Override
    public void changeStyle(int style) {
        // TODO Auto-generated method stub
        mDesktopCallAndMsg.changeStyle(style);
        mDesktopCallAndMsg.setLongClickListener(mLongClickListener);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mZoomMode) {
            mSingleClickListener.onClick(v);
        } else {
            if (v.equals(mDesktopCallAndMsg.getCallNumView())) {
                if (mDesktopCallAndMsg.getCallNumView().getMissCallNum() > 0) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setClassName("com.android.contacts",
                            "com.android.contacts.activities.DialtactsActivity");
                    getContext().startActivity(intent);
                }
            } else if (v.equals(mDesktopCallAndMsg.getCallView())) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setClassName("com.android.contacts",
                        "com.android.contacts.activities.DialtactsActivity");
                getContext().startActivity(intent);
            } else if (v.equals(mDesktopCallAndMsg.getMsgNumView())) {
                if (mDesktopCallAndMsg.getMsgNumView().getUnreadMsg() > 0) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setClassName("com.android.mms", "com.android.mms.ui.BootActivity");
                    getContext().startActivity(intent);
                }
            } else if (v.equals(mDesktopCallAndMsg.getMsgView())) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setClassName("com.android.mms", "com.android.mms.ui.BootActivity");
                getContext().startActivity(intent);
            }
        }
    }

}
