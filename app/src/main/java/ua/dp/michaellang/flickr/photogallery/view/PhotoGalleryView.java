package ua.dp.michaellang.flickr.photogallery.view;

import ua.dp.michaellang.flickr.photogallery.entity.Photos;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public interface PhotoGalleryView {
    void onLoadingStart();
    void onStartNewLoad();
    void onLoadComplete();
    void onError(Throwable e);
    void onPhotosLoaded(Photos photos, boolean hasMoreData);
}
