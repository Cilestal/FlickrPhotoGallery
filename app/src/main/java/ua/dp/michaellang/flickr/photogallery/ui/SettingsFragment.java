package ua.dp.michaellang.flickr.photogallery.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import ua.dp.michaellang.flickr.photogallery.R;

/**
 * Date: 29.08.2017
 *
 * @author Michael Lang
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
}
