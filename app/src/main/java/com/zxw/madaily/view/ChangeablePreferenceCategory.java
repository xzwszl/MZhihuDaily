package com.zxw.madaily.view;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.zxw.madaily.DailyApplication;
import com.zxw.madaily.R;

/**
 * Created by sony on 2015/9/10.
 */
public class ChangeablePreferenceCategory extends PreferenceCategory {

    public ChangeablePreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChangeablePreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeablePreferenceCategory(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        if (view instanceof TextView) {
            ((TextView) view).setTextColor(DailyApplication.mInstance.getAppResource().getColor(R.color.text_color));
        }
    }
}
