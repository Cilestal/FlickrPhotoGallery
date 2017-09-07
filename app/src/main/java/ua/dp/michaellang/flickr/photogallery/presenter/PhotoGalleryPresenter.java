package ua.dp.michaellang.flickr.photogallery.presenter;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public interface PhotoGalleryPresenter extends BasePresenter {
    void loadRecentPhotos();
    void search(String query);

    void reset();
}
