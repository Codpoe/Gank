package me.codpoe.gank.data;

import me.codpoe.gank.data.bean.MeizhiBean;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Codpoe on 2016/9/15.
 */
public interface Api {

    @GET("10/{page}")
    Observable<MeizhiBean> fetchMeizhi(@Path("page") int page);

}
