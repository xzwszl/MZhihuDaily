package com.zxw.madaily.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxw.madaily.DailyApplication;
import com.zxw.madaily.R;

/**
 * Created by sony on 2015/9/10.
 */
public class ChangeablePreference extends Preference {
    public ChangeablePreference(Context context) {
        super(context);
    }

    public ChangeablePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeablePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView tv = (TextView) ((ViewGroup) ((ViewGroup) view).getChildAt(1)).getChildAt(0);
        if (tv != null) {
            tv.setTextColor(DailyApplication.mInstance.getAppResource().getColor(R.color.text_color));
        }

        tv = (TextView) ((ViewGroup) ((ViewGroup) view).getChildAt(1)).getChildAt(1);
        if (tv != null) {
            tv.setTextColor(DailyApplication.mInstance.getAppResource().getColor(R.color.text_color));
        }
    }
}
