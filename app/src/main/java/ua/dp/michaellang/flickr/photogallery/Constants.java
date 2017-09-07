package ua.dp.michaellang.flickr.photogallery;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public interface Constants {
    String API_URL = "https://api.flickr.com/services/rest/";
    String FLICKR_PHOTOS_URL = "https://www.flickr.com/photos/";
    String FLICKR_PHOTO_URL = "https://farm%s.staticflickr.com/%s/%s_%s_b.jpg";
    String USER_AGENT = "Photo Gallery";

    String APP_KEY = "9bc8d0c7128a9a339f3e5fab49fcd45e";
    String APP_SECRET = "1c0104aedfa4a730";

    int PAGE_ITERATOR_SIZE = 60;
    double MAP_POSITION_OFFSET = 0.01;

    String PREF_SEARCH_QUERY = "pref_searchQuery";
    String PREF_LAST_RESULT_ID = "pref_lastResultId";
    String PREF_IS_ALARM_ON = "pref_isAlarmOn";

    String PREF_MAX_SEARCH_RADIUS = "pref_max_search_radius";
    String PREF_MAX_SEARCH_PHOTOS = "pref_max_search_photos";

    String ACTION_SHOW_NOTIFICATION = "ua.dp.michaellang.photogallery.SHOW_NOTIFICATION";
    String PERM_PRIVATE = "ua.dp.michaellang.photogallery.PRIVATE";


}
