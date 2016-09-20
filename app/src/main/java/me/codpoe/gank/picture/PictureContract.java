package me.codpoe.gank.picture;

import android.net.Uri;

import me.codpoe.gank.base.BasePresenter;
import me.codpoe.gank.base.BaseView;
import rx.Observable;

/**
 * Created by Codpoe on 2016/9/16.
 */
public interface PictureContract {

    interface View extends BaseView<Presenter> {
        void showSheet();
        void showSuccess(String success);
        void showError(String error);
    }

    interface Presenter extends BasePresenter {
        void sharePic(Uri uri);
        void copyUrl(String url);
        Observable<Uri> savePic(String url, String id);
    }

}
