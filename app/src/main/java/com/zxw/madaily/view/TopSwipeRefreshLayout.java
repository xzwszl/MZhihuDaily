package com.zxw.madaily.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by xzwszl on 7/23/2015.
 */
public class TopSwipeRefreshLayout extends SwipeRefreshLayout {

    private float mInitX;
    private float mInitY;
    private int mSlop;
    private float mHeight;

    public TopSwipeRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public TopSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){

        mSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mHeight = getResources().getDisplayMetrics().density *200;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mInitX = ev.getX();
                mInitY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                final float x = ev.getX();
                final float y = ev.getY();
                float xDiff = Math.abs(x - mInitX);
                float yDiff = Math.abs(y - mInitY);
                if ( xDiff> Math.max(mSlop, yDiff) && mInitY < mHeight) {
                    return false;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }
}

