package ua.dp.michaellang.flickr.photogallery.network;

import android.support.annotation.Nullable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;
import ua.dp.michaellang.flickr.photogallery.entity.FlickrResponse;

import java.util.Map;

/**
 * Date: 04.08.2017
 *
 * @author Michael Lang
 */
public interface FlickrService {
    String EXTRAS_URL_S = "url_s";

    String ARGUMENT_USER_ID = "user_id";
    String ARGUMENT_TAGS = "tags";
    String ARGUMENT_TAG_MODE = "tag_mode";
    String ARGUMENT_TEXT = "text";
    String ARGUMENT_MIN_UPLOAD_DATE = "min_upload_date";
    String ARGUMENT_MAX_UPLOAD_DATE = "max_upload_date";
    String ARGUMENT_MIN_TAKEN_DATE = "min_taken_date";
    String ARGUMENT_MAX_TAKEN_DATE = "max_taken_date ";
    String ARGUMENT_SORT = "sort";
    String ARGUMENT_QUERY = "page";
    String ARGUMENT_PER_PAGE = "per_page";
    String ARGUMENT_PRIVACY_FILTER = "privacy_filter";
    String ARGUMENT_LAT = "lat";
    String ARGUMENT_RADIUS = "radius";
    String ARGUMENT_LON = "lon";

    String SORT_DATE_POSTED_ASC = "date-posted-asc";
    String SORT_DATE_POSTED_DESC = "date-posted-desc";
    String SORT_DATE_TAKEN_ASC = "date-taken-asc";
    String SORT_DATE_TAKEN_DESC = "date-taken-desc";
    String SORT_INTERESTINGNESS_DESC = "interestingness-desc";
    String SORT_INTERESTINGNESS_ASC = "interestingness-asc";
    String SORT_RELEVANCE = "relevance";

    int PRIVACY_FILTER_PUBLIC = 1;
    int PRIVACY_FILTER_FRINEDS = 2;
    int PRIVACY_FILTER_FAMILY = 3;
    int PRIVACY_FILTER_FRIENDS_AND_FAMILY = 4;
    int PRIVACY_FILTER_PRIVATE = 5;

    @GET("?method=flickr.photos.getRecent&format=json&nojsoncallback=1&extras=url_s")
    Observable<Response<FlickrResponse>> getRecent(
            @Query("api_key") String key,
            @Query("per_page") int perPage,
            @Query("page") int page
    );

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1")
    Observable<Response<FlickrResponse>> search(
            @Query("api_key") String key,
            @Query("extras") String extras,
            @Nullable @QueryMap Map<String, String> arguments
    );

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&extras=geo")
    Observable<Response<FlickrResponse>> search(
            @Query("api_key") String key,
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("per_page") int perPage,
            @Nullable @Query("radius") Double radius
    );

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&extras=url_s")
    Observable<Response<FlickrResponse>> search(
            @Query("api_key") String key,
            @Query("text") String query,
            @Query("per_page") int perPage,
            @Query("page") int page
    );
}
