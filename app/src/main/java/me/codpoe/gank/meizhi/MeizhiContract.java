package me.codpoe.gank.meizhi;

import java.util.List;

import me.codpoe.gank.base.BasePresenter;
import me.codpoe.gank.base.BaseView;
import me.codpoe.gank.data.bean.MeizhiBean;

/**
 * Created by Codpoe on 2016/9/15.
 */
public interface MeizhiContract {

    interface View extends BaseView<Presenter> {

        void showMeizhi(List<MeizhiBean.ResultsBean> meizhiList);
        void showSuccess(String success);
        void showError(String error);
    }

    interface Presenter extends BasePresenter {

        void loadMeizhi(int page);

    }
}
