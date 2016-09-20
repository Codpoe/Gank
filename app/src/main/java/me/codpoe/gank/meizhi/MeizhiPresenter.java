package me.codpoe.gank.meizhi;

import android.Manifest;
import android.content.Context;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import me.codpoe.gank.R;
import me.codpoe.gank.data.Repository;
import me.codpoe.gank.data.bean.MeizhiBean;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Codpoe on 2016/9/15.
 */
public class MeizhiPresenter implements MeizhiContract.Presenter {

    private Repository mRepository;
    private MeizhiContract.View mView;
    private Context mContext;

    public MeizhiPresenter(MeizhiContract.View view) {
        mView = view;
        mContext = (Context) mView;
        mView.setPresenter(this);
        mRepository = new Repository();
    }

    @Override
    public void start() {
        RxPermissions.getInstance(mContext)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {

                    }
                });
    }

    @Override
    public void loadMeizhi(final int page) {
        mRepository.getMeizhi(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<MeizhiBean.ResultsBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.showSuccess(mContext.getResources().getString(R.string.success));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<MeizhiBean.ResultsBean> resultsBeen) {
                        mView.showMeizhi(resultsBeen);
                    }
                });
    }
}
