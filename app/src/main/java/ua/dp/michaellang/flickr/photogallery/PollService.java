package ua.dp.michaellang.flickr.photogallery;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import retrofit2.Response;
import rx.Subscriber;
import timber.log.Timber;
import ua.dp.michaellang.flickr.photogallery.entity.FlickrResponse;
import ua.dp.michaellang.flickr.photogallery.entity.Photo;
import ua.dp.michaellang.flickr.photogallery.network.FlickrMethods;
import ua.dp.michaellang.flickr.photogallery.utils.SPUtil;

import java.util.List;

/**
 * Date: 23.08.2017
 *
 * @author Michael Lang
 */
public class PollService extends IntentService {
    private static final String TAG = "PollService";

    private static final long POLL_INTERVAL = 1000*60;//AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    private Subscriber<Response<FlickrResponse>> subscriber;

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableAndConnected()) {
            return;
        }

        createSubscriber();
        String query = SPUtil.getStoredQuery(this);
        if (query == null) {
            FlickrMethods.getInstance()
                    .getRecent(subscriber, 1, 1);
        } else {
            FlickrMethods.getInstance()
                    .searchPhotos(subscriber, query, 1, 1);
        }
    }

    private void createSubscriber() {
        removeSubscriber();

        subscriber = new Subscriber<Response<FlickrResponse>>() {
            @Override
            public void onCompleted() {
                //stub
            }

            @Override
            public void onError(Throwable e) {
                //stub
            }

            @Override
            public void onNext(Response<FlickrResponse> recentPhotosResponse) {
                List<Photo> photos = recentPhotosResponse.body().getPhotos().getPhoto();
                checkResponse(photos);
            }
        };
    }

    private void checkResponse(List<Photo> photos) {
        if (photos.size() == 0) {
            return;
        }
        String resultId = photos.get(0).getId();

        String lastResultId = SPUtil.getLastResultId(PollService.this);
        if (resultId.equals(lastResultId)) {
            Timber.i("Got an old result: " + resultId);
        } else {
            Timber.i("Got a new result: " + resultId);
            Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
            sendOrderedBroadcast(intent, Constants.PERM_PRIVATE);
        }
        SPUtil.setLastResult(this, resultId);
    }

    private void removeSubscriber() {
        if (subscriber != null && subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
            Timber.d("PollService started.");
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
            Timber.d("PollService stopped.");
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeSubscriber();
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        return isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }
}