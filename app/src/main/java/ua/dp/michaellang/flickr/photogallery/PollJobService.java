package ua.dp.michaellang.flickr.photogallery;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import retrofit2.Response;
import rx.Subscriber;
import timber.log.Timber;
import ua.dp.michaellang.flickr.photogallery.entity.FlickrResponse;
import ua.dp.michaellang.flickr.photogallery.entity.Photo;
import ua.dp.michaellang.flickr.photogallery.network.FlickrMethods;
import ua.dp.michaellang.flickr.photogallery.utils.SPUtil;

import java.util.List;

/**
 * Date: 26.08.2017
 *
 * @author Michael Lang
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PollJobService extends JobService {

    private static final int JOB_ID = 1;

    private Subscriber<Response<FlickrResponse>> subscriber;

    private void createSubscriber(final JobParameters params) {
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
                checkResponse(params, photos);
            }
        };
    }

    private void checkResponse(final JobParameters params, List<Photo> photos) {
        if (photos.size() == 0) {
            return;
        }
        String resultId = photos.get(0).getId();

        String lastResultId = SPUtil.getLastResultId(this);
        if (resultId.equals(lastResultId)) {
            Timber.i("Got an old result: " + resultId);
        } else {
            Timber.i("Got a new result: " + resultId);
            Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
            sendOrderedBroadcast(intent, Constants.PERM_PRIVATE);
        }
        SPUtil.setLastResult(this, resultId);

        removeSubscriber();
        jobFinished(params, false);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        createSubscriber(params);

        Timber.d("Start PollJobService");

        String query = SPUtil.getStoredQuery(this);
        if (query == null) {
            FlickrMethods.getInstance()
                    .getRecent(subscriber, 1, 1);
        } else {
            FlickrMethods.getInstance()
                    .searchPhotos(subscriber, query, 1, 1);
        }
        return true; //продолжается в background
        //для остановки - jobFinished(params, false);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Timber.d("Stop PollJobService");
        removeSubscriber();
        return false;
    }

    private void removeSubscriber() {
        if (subscriber != null && subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }

    public static boolean hasBeenScheduled(Context context) {
        JobScheduler scheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == JOB_ID) {
                return true;
            }
        }
        return false;
    }

    public static void schedule(Context context, boolean isOn) {
        JobScheduler scheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if(isOn) {
            JobInfo jobInfo = new JobInfo.Builder(
                    JOB_ID, new ComponentName(context, PollJobService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPeriodic(1000 * 60 * 15)
                    .setPersisted(true)
                    .build();
            scheduler.schedule(jobInfo);
        } else {
            scheduler.cancel(JOB_ID);
        }
    }
}
