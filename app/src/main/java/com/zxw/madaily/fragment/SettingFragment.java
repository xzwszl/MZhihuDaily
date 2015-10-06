package com.zxw.madaily.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.zxw.madaily.R;
import com.zxw.madaily.db.NewsHandler;
import com.zxw.madaily.db.ReadHandler;
import com.zxw.madaily.db.TableHandler;
import com.zxw.madaily.tool.ViewUtils;

/**
 * Created by xzwszl on 2015/8/10.
 */
public class SettingFragment extends PreferenceFragment {

    public static final String  AUTO_DOWNLOAD = "auto_download";
    public static final String  NO_PICTURE = "no_picture";
    private  final String  CLEAR_CACHE = "clear_cache";
    private  final  String VERSION_CHECK = "version_check";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settiing);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if (CLEAR_CACHE.equals(preference.getKey())) {
            // clear database
            clearDatabase();
            ViewUtils.showMessage(getActivity(), getString(R.string.clear_success));

        } else if (VERSION_CHECK.equals(preference.getKey())){
            ViewUtils.showMessage(getActivity(), getString(R.string.version_latest));
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void clearDatabase() {
        new TableHandler(getActivity().getApplicationContext(), TableHandler.TABLE_CONTENT).deleteAll();
        new TableHandler(getActivity().getApplicationContext(), TableHandler.TABLE_THEMES).deleteAll();
        new NewsHandler(getActivity().getApplicationContext()).deleteAll();
        new ReadHandler(getActivity().getApplicationContext()).deleteAll();
    }
}
