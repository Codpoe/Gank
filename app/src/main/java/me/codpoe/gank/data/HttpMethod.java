package me.codpoe.gank.data;

import java.util.concurrent.TimeUnit;

import me.codpoe.gank.data.bean.MeizhiBean;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Codpoe on 2016/9/15.
 */
public class HttpMethod {

    private static final String URL = "http://gank.io/api/data/福利/";

    private static HttpMethod sHttpMethod;
    private Api mApi;
    private Retrofit mRetrofit;

    private HttpMethod() {

        // set up OkHttpClient
        OkHttpClient client;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        client = builder.build();

        // set up Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApi = mRetrofit.create(Api.class);

    }

    public synchronized static HttpMethod getInstance() {
        if (sHttpMethod == null) {
            sHttpMethod = new HttpMethod();
        }
        return sHttpMethod;
    }

    public Observable<MeizhiBean> getMeizhiFromNet(int page) {

        return mApi.fetchMeizhi(page)
                .subscribeOn(Schedulers.io());

    }
}
