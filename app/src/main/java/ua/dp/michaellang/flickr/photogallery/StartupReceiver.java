package ua.dp.michaellang.flickr.photogallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import timber.log.Timber;
import ua.dp.michaellang.flickr.photogallery.utils.SPUtil;

/**
 * Date: 29.08.2017
 *
 * @author Michael Lang
 */
public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("Received broadcast intent: " + intent.getAction());
        boolean isAlarmOn = SPUtil.isAlarmOn(context);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            PollService.setServiceAlarm(context, isAlarmOn);
        }
    }
}
