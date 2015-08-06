package com.zxw.madaily.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

import com.zxw.madaily.R;

/**
 * Created by xzwszl on 2015/8/6.
 */
public class IndicatorView extends View {

    private int mWidth;
    private float radius;
    private Paint mPaint;
    private float loacation;
    private float mDensity;
    private int count;
    private int direction = 0; // 0 unset, -1 left, 1 rignht;
    private float start;

    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDensity = getResources().getDisplayMetrics().density;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        radius = 5 * mDensity;
        count = 5;
        start = -1;

    }

    public void setLoacation(int position, float percent) {

        position = position % count;

        if (direction == 0 && percent > 0.5) {
            direction = -1;
        } else if (direction == 0 && percent < 0.5){
            direction = 1;
        }

        if (position == count-1) percent = 0;

        if (direction == -1) {
            loacation = (percent -1) * 3 * radius + start + position * 3 * radius;
        } else if (direction == 1) {
            loacation = percent * 3 * radius + start + position * 3 * radius;
        }

        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        if (start == -1) {
            start = getWidth() / 2 - (count-1) * 3 / 2 * radius;
            loacation = start;
        }

        float s = start - 3 * radius;
        for (int i = 0;i < count; i++) {

            s += 3 * radius;

            mPaint.setColor(getResources().getColor(R.color.indicator_white));
            canvas.drawCircle(s,radius,radius,mPaint);

        }

        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(loacation, radius, radius, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
