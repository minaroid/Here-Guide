package com.george888.mina.hereguide.ui.settingsFragment;

import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.george888.mina.hereguide.R;

/**
 * Created by minageorge on 1/24/18.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    private String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preference_screen);
        ListPreference  listPreference = (ListPreference) findPreference(getString(R.string.pref_dis_key));
        listPreference.setOnPreferenceChangeListener(this);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String []arr= getResources().getStringArray(R.array.dist_titles);
        if(newValue.toString().equals("km")){
            preference.setSummary(arr[0]);
        }
        else {
            preference.setSummary(arr[1]);
        }
        preference.setSummary(newValue.toString().toUpperCase());
        return true;
    }
}