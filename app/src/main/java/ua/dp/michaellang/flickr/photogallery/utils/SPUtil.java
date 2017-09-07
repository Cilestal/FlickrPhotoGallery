package ua.dp.michaellang.flickr.photogallery.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import static ua.dp.michaellang.flickr.photogallery.Constants.*;

/**
 * Date: 23.08.2017
 *
 * @author Michael Lang
 */
public class SPUtil {

    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }

    public static String getLastResultId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_RESULT_ID, null);
    }

    public static void setLastResult(Context context, String lastResultId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_RESULT_ID, lastResultId)
                .apply();
    }

    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }

    public static double getMaxSearchRadius(Context context) {
        String radiusString = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_MAX_SEARCH_RADIUS, "5000");
        return Double.parseDouble(radiusString) / 1000;
    }

    public static int getMaxSearchCount(Context context) {
        String maxPhotosString = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_MAX_SEARCH_PHOTOS, "100");
        return Integer.parseInt(maxPhotosString);
    }
}
