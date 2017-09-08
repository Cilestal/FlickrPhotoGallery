package ua.dp.michaellang.flickr.photogallery.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import timber.log.Timber;
import ua.dp.michaellang.flickr.photogallery.R;
import ua.dp.michaellang.flickr.photogallery.entity.Photo;
import ua.dp.michaellang.flickr.photogallery.presenter.LocatrPresenter;
import ua.dp.michaellang.flickr.photogallery.presenter.LocatrPresenterImpl;
import ua.dp.michaellang.flickr.photogallery.view.LocatrView;

import java.util.ArrayList;
import java.util.List;

import static ua.dp.michaellang.flickr.photogallery.Constants.MAP_POSITION_OFFSET;

/**
 * Date: 31.08.2017
 *
 * @author Michael Lang
 */
public class LocatrFragment extends SupportMapFragment
        implements LocatrView, GoogleMap.OnMarkerClickListener {
    private static final String DIALOG_PHOTO_PAGE = "DIALOG_PHOTO_PAGE";

    private static final String KEY_CURRENT_LOCATION = "CURRENT_LOCATION";
    private static final String KEY_PHOTOS = "PHOTOS";

    private GoogleMap mMap;

    private LocatrPresenter mPresenter;
    private ProgressDialog mProgressDialog;

    private LatLng mCurrentLocation;
    private ArrayList<Photo> mPhotos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        loadSavedState(savedInstanceState);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.dialog_progress_title));

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMarkerClickListener(LocatrFragment.this);

                addCurrentLocationCircle();
                updateMarkers();
            }
        });

        mPresenter = new LocatrPresenterImpl(getContext(), this);
    }

    private void loadSavedState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_CURRENT_LOCATION);
            mPhotos = savedInstanceState.getParcelableArrayList(KEY_PHOTOS);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onError(String mes) {
        mProgressDialog.dismiss();
    }

    @Override
    public void onLocationDetected(Location location) {
        mCurrentLocation = new LatLng(location.getLatitude(),
                location.getLongitude());

        mMap.clear();
        updateCameraPosition(location);
        addCurrentLocationCircle();
    }

    @Override
    public void onPhotosLoaded(List<Photo> photos) {
        if (photos instanceof ArrayList) {
            mPhotos = (ArrayList<Photo>) photos;
        } else {
            mPhotos = new ArrayList<>(photos);
        }
        mProgressDialog.dismiss();

        Timber.d("Found %d photos.", photos.size());

        updateMarkers();
    }

    private void updateCameraPosition(Location loc) {
        if (mMap == null) {
            return;
        }

        double latitude = loc.getLatitude();
        double longitude = loc.getLongitude();

        //масштабируем карту
        LatLng point1 = new LatLng(latitude - MAP_POSITION_OFFSET,
                longitude - MAP_POSITION_OFFSET);
        LatLng point2 = new LatLng(latitude + MAP_POSITION_OFFSET,
                longitude + MAP_POSITION_OFFSET);

        LatLngBounds bounds = new LatLngBounds(point1, point2);

/*        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(point1)
                .include(point2)
                .build();*/

        //move camera
        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.animateCamera(update);
    }

    /**
     * Adds circle to current location
     */
    private void addCurrentLocationCircle() {
        if (mMap == null || mCurrentLocation == null) {
            return;
        }

        int color;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getResources().getColor(R.color.color_map_circle, null);
        } else {
            color = getResources().getColor(R.color.color_map_circle);
        }

        int val = getResources().getInteger(R.integer.map_circle_radius);
        CircleOptions center = new CircleOptions()
                .radius(val)
                .strokeColor(color)
                .center(mCurrentLocation);
        mMap.addCircle(center);
    }

    private void updateMarkers() {
        if (mMap == null || mPhotos == null) {
            return;
        }

        for (int i = 0; i < mPhotos.size(); i++) {
            Photo photo = mPhotos.get(i);
            Double latitude = photo.getLatitude();
            Double longitude = photo.getLongitude();

            if (latitude == null || longitude == null) continue;

            LatLng pos = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(pos);
            mMap.addMarker(markerOptions)
                    .setTag(i);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(KEY_CURRENT_LOCATION, mCurrentLocation);
        bundle.putParcelableArrayList(KEY_PHOTOS, mPhotos);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                Intent intent = SettingsActivity.newIntent(getContext());
                startActivity(intent);
                return true;
            case R.id.menu_item_locate:
                mProgressDialog.show();
                mPresenter.search();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static LocatrFragment newInstance() {
        return new LocatrFragment();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mPhotos != null) {
            Object tag = marker.getTag();
            if (tag != null) {
                Photo photo = mPhotos.get((int) tag);
                PhotoFragment dialog = PhotoFragment
                        .newInstance(photo.getPhotoUrl(), photo.getPhotoPageUri());
                dialog.show(getFragmentManager(), DIALOG_PHOTO_PAGE);
            }
        }
        return false;
    }
}
