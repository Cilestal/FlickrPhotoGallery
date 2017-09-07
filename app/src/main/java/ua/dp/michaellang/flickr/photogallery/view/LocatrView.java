package ua.dp.michaellang.flickr.photogallery.view;

import android.location.Location;
import ua.dp.michaellang.flickr.photogallery.entity.Photo;

import java.util.List;

/**
 * Date: 01.09.2017
 *
 * @author Michael Lang
 */
public interface LocatrView {
    void onError(String mes);
    void onLocationDetected(Location location);
    void onPhotosLoaded(List<Photo> photos);

    void onConnected();
}
