package com.zxw.madaily.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.zxw.madaily.R;

/**
 * Created by xzwszl on 2015/8/10.
 */
public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settiing);
    }
}
