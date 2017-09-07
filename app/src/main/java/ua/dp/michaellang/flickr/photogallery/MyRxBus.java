package ua.dp.michaellang.flickr.photogallery;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Date: 06.09.2017
 *
 * @author Michael Lang
 */
public class MyRxBus {

    private static MyRxBus sInstance;

    private final Subject<Object, Object> mSubject
            = new SerializedSubject<>(PublishSubject.create());

    public static MyRxBus getInstance() {
        if (sInstance == null) {
            sInstance = new MyRxBus();
        }
        return sInstance;
    }

    /**
     * Pass any event down to event listeners.
     */
    public void publish(Object message) {
        mSubject.onNext(message);
    }

    /**
     * Subscribe to this Observable. On event, do something
     * e.g. replace a fragment
     */
    public Observable<Object> getEvents() {
        return mSubject;
    }

    public boolean hasObservers(){
        return mSubject.hasObservers();
    }
}
