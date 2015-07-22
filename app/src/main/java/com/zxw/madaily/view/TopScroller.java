package com.zxw.madaily.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by xzwszl on 7/22/2015.
 */
public class TopScroller extends Scroller {

    private int mDuration = 1000;

    public TopScroller(Context context) {
        super(context);
    }

    public TopScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }
}
