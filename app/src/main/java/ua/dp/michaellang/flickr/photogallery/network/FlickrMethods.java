package ua.dp.michaellang.flickr.photogallery.network;

import android.support.annotation.Nullable;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ua.dp.michaellang.flickr.photogallery.entity.FlickrResponse;
import ua.dp.michaellang.flickr.photogallery.utils.RetrofitUtil;

import java.util.Collections;
import java.util.Map;

import static ua.dp.michaellang.flickr.photogallery.Constants.APP_KEY;
import static ua.dp.michaellang.flickr.photogallery.Constants.PAGE_ITERATOR_SIZE;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public class FlickrMethods {
    private FlickrService service;

    private static class Nested {
        static FlickrMethods instance = new FlickrMethods();
    }

    private FlickrMethods() {
        service = RetrofitUtil.createService(FlickrService.class);
    }

    public static FlickrMethods getInstance() {
        return Nested.instance;
    }

    public void getRecent(Observer<Response<FlickrResponse>> observer,
            int perPage, int page) {
        service.getRecent(APP_KEY, perPage, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getRecent(Observer<Response<FlickrResponse>> observer) {
        service.getRecent(APP_KEY, PAGE_ITERATOR_SIZE, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void searchPhotos(Observer<Response<FlickrResponse>> observer,
            String query, String extras) {
        Map<String, String> map = Collections.singletonMap(FlickrService.ARGUMENT_TEXT, query);
        service.search(APP_KEY, extras, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void searchPhotos(Observer<Response<FlickrResponse>> observer,
            double latitude, double longitude, int perPage, @Nullable Double radius) {
        service.search(APP_KEY, latitude, longitude, perPage, radius)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void searchPhotos(Observer<Response<FlickrResponse>> observer, String query,
            int perPage, int page) {
        service.search(APP_KEY, query, perPage, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
