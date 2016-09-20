package me.codpoe.gank.data;

import java.util.List;

import me.codpoe.gank.data.bean.MeizhiBean;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Codpoe on 2016/9/15.
 */
public class Repository {

    public Observable<List<MeizhiBean.ResultsBean>> getMeizhi(int page) {

            return HttpMethod.getInstance().getMeizhiFromNet(page)
                    .subscribeOn(Schedulers.io())
                    .flatMap(new Func1<MeizhiBean, Observable<MeizhiBean.ResultsBean>>() {
                        @Override
                        public Observable<MeizhiBean.ResultsBean> call(MeizhiBean meizhiBean) {
                            return Observable.from(meizhiBean.getResults());
                        }
                    })
                    .toList();

    }

}
