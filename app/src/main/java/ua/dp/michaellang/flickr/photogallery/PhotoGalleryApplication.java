package ua.dp.michaellang.flickr.photogallery;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import timber.log.Timber;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public class PhotoGalleryApplication extends Application
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Constants.PREF_IS_ALARM_ON)) {
            boolean isAlarmOn = sharedPreferences.getBoolean(key, false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PollJobService.schedule(this, isAlarmOn);
            } else {
                PollService.setServiceAlarm(this, isAlarmOn);
            }
        }
    }
}
