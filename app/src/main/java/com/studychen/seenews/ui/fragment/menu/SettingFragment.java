package com.studychen.seenews.ui.fragment.menu;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.studychen.seenews.R;

/**
 * Created by tomchen on 2/27/16.
 */
public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
