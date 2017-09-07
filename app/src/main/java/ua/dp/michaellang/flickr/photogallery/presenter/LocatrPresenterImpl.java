package ua.dp.michaellang.flickr.photogallery.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import retrofit2.Response;
import rx.Subscriber;
import timber.log.Timber;
import ua.dp.michaellang.flickr.photogallery.entity.FlickrResponse;
import ua.dp.michaellang.flickr.photogallery.entity.Photo;
import ua.dp.michaellang.flickr.photogallery.network.FlickrMethods;
import ua.dp.michaellang.flickr.photogallery.utils.SPUtil;
import ua.dp.michaellang.flickr.photogallery.view.LocatrView;

import java.util.List;

/**
 * Date: 01.09.2017
 *
 * @author Michael Lang
 */
public class LocatrPresenterImpl implements LocatrPresenter {
    private GoogleApiClient mClient;
    private LocatrView mView;
    private Context mContext;
    private Subscriber<Response<FlickrResponse>> subscriber;

    public LocatrPresenterImpl(Context context, LocatrView view) {
        mContext = context;
        mView = view;

        mClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        mView.onConnected();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        mView.onError("ERROR STUB!");
                    }
                })
                .build();
    }

    private void createSubscriber() {
        onStop();

        subscriber = new Subscriber<Response<FlickrResponse>>() {
            @Override
            public void onCompleted() {
                // STUB
            }

            @Override
            public void onError(Throwable e) {
                mView.onError(e.getMessage());
            }

            @Override
            public void onNext(Response<FlickrResponse> response) {
                if (response.isSuccessful()) {
                    List<Photo> photos = response.body().getPhotos().getPhoto();
                    mView.onPhotosLoaded(photos);
                } else {
                    mView.onError("ERROR STUB!");
                }
            }
        };
    }

    @Override
    public void onStart() {
        mClient.connect();
    }

    @Override
    public void onStop() {
        if (mClient.isConnected()) {
            mClient.disconnect();
        }

        if (subscriber != null && subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }

    @Override
    public void search() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);

        if (checkSelfPermission()) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mView.onError("ERROR STUB!");
            return;
        }

        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Timber.i("Got a fix: " + location);
                        mView.onLocationDetected(location);
                        searchPhotos(location);

                        mClient.reconnect();
                    }
                });
    }

    private boolean checkSelfPermission() {
        return ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED;
    }

    private void searchPhotos(Location location) {
        createSubscriber();

        double radius = SPUtil.getMaxSearchRadius(mContext);
        int count = SPUtil.getMaxSearchCount(mContext);

        FlickrMethods.getInstance()
                .searchPhotos(subscriber, location.getLatitude(),
                        location.getLongitude(), count, radius);
    }
}
