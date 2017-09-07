package ua.dp.michaellang.flickr.photogallery.utils;

import android.support.annotation.NonNull;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static ua.dp.michaellang.flickr.photogallery.Constants.API_URL;

/**
 * Date: 20.07.2017
 *
 * @author Michael Lang
 */
public class RetrofitUtil {

    private volatile static Retrofit sBaseRetrofit;

    private static Retrofit getBaseRetrofit() {
        if (sBaseRetrofit == null) {
            OkHttpClient client = new OkHttpClient();
            sBaseRetrofit = initRetrofit(client, API_URL);
        }

        return sBaseRetrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        return getBaseRetrofit().create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        OkHttpClient client = new OkHttpClient();

        return initRetrofit(client, baseUrl).create(serviceClass);
    }

    @NonNull
    private static Retrofit initRetrofit(OkHttpClient client, String baseUrl) {
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

}
