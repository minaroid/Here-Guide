package com.george888.mina.hereguide.ui.settingsFragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.george888.mina.hereguide.R;

/**
 * Created by minageorge on 1/24/18.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    private String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preference_screen);
    }


}