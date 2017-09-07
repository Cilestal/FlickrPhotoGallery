package ua.dp.michaellang.flickr.photogallery.presenter;

import retrofit2.Response;
import rx.Subscriber;
import ua.dp.michaellang.flickr.photogallery.entity.FlickrResponse;
import ua.dp.michaellang.flickr.photogallery.network.FlickrMethods;
import ua.dp.michaellang.flickr.photogallery.view.PhotoGalleryView;

import static ua.dp.michaellang.flickr.photogallery.Constants.PAGE_ITERATOR_SIZE;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public class PhotoGalleryPresenterImpl implements PhotoGalleryPresenter {
    private Subscriber<Response<FlickrResponse>> subscriber;
    private PhotoGalleryView mView;

    private int mPage = 1;

    public PhotoGalleryPresenterImpl(PhotoGalleryView view) {
        mView = view;
    }

    private void createSubscriber() {
        onStop();

        subscriber = new Subscriber<Response<FlickrResponse>>() {
            @Override
            public void onCompleted() {
                mPage++;
                mView.onLoadComplete();
            }

            @Override
            public void onError(Throwable e) {
                mView.onError(e);
            }

            @Override
            public void onNext(Response<FlickrResponse> response) {
                if(response.isSuccessful()) {
                    FlickrResponse body = response.body();
                    int pages = body.getPhotos().getPages();
                    boolean hasMoreData = mPage < pages;
                    mView.onPhotosLoaded(body.getPhotos(), hasMoreData);
                }
            }
        };
    }

    @Override
    public void loadRecentPhotos() {
        createSubscriber();
        mView.onLoadingStart();
        FlickrMethods.getInstance()
                .getRecent(subscriber, PAGE_ITERATOR_SIZE, mPage);
    }

    @Override
    public void search(String query) {
        createSubscriber();
        mView.onLoadingStart();
        FlickrMethods.getInstance()
                .searchPhotos(subscriber, query, PAGE_ITERATOR_SIZE, mPage);
    }

    @Override
    public void reset() {
        mPage = 1;
        mView.onStartNewLoad();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (subscriber != null && subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }
}
