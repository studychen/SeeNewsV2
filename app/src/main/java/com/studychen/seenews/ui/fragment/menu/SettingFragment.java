package com.studychen.seenews.ui.fragment.menu;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.orhanobut.logger.Logger;
import com.studychen.seenews.R;
import com.studychen.seenews.util.PrefUtils;


/**
 * Created by tomchen on 2/27/16.
 */
public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

//        SwitchCompat ss = (SwitchCompat) getActivity().findViewById(R.id.switch_compat);
//        ss.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Logger.d("SwitchCompat " + buttonView + " changed to " + isChecked);
//            }
//        });

final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager()
        .findPreference(getString(R.string.save_net_mode));

checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

    /**
     * @param preference The changed Preference.
     * @param newValue   The new value of the Preference.
     * @return True to update the state of the Preference with the new value.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        boolean checked = Boolean.valueOf(newValue.toString());
        PrefUtils.setSaveNetMode(checked);
        Logger.d("Pref " + preference.getKey() + " changed to " + newValue.toString());
        return true;

    }
});
    }
}
