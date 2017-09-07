package ua.dp.michaellang.flickr.photogallery;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import timber.log.Timber;
import ua.dp.michaellang.flickr.photogallery.ui.PhotoGalleryActivity;

/**
 * Date: 29.08.2017
 *
 * @author Michael Lang
 */
public class NotificationReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 104;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("Got a broadcast:" + intent.getAction());

        if (getResultCode() != Activity.RESULT_OK) {
            // Активность переднего плана отменила рассылку
            return;
        }

        Resources resources = context.getResources();
        Intent i = PhotoGalleryActivity.newIntent(context);

        PendingIntent resultPendingIntent = TaskStackBuilder.create(context)
                .addParentStack(PhotoGalleryActivity.class)
                .addNextIntent(i)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(resources.getString(R.string.new_pictures_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setContentText(resources.getString(R.string.new_pictures_text))
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
