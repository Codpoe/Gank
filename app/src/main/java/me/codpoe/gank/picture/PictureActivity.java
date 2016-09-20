package me.codpoe.gank.picture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.codpoe.gank.R;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureActivity extends AppCompatActivity implements PictureContract.View {

    @BindView(R.id.pic_img)
    ImageView mPicImg;
    @BindView(R.id.share_btn)
    Button mShareBtn;
    @BindView(R.id.copy_url_btn)
    Button mCopyUrlBtn;
    @BindView(R.id.save_btn)
    Button mSaveBtn;
    @BindView(R.id.scroll)
    NestedScrollView mScroll;
    @BindView(R.id.pic_coorLay)
    CoordinatorLayout mPicCoorLay;

    private static final String EXTRA_IMG_URL = "img_url";
    private static final String EXTRA_IMG_ID = "img_id";

    private PictureContract.Presenter mPresenter;
    private PhotoViewAttacher mAttacher;
    private String mImgUrl;
    private String mImgId;
    private Uri mUri;

    public static Intent newIntent(Context context, String url, String id) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(EXTRA_IMG_URL, url);
        intent.putExtra(EXTRA_IMG_ID, id);
        return intent;
    }

    public void parseIntent() {
        mImgUrl = getIntent().getStringExtra(EXTRA_IMG_URL);
        mImgId = getIntent().getStringExtra(EXTRA_IMG_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_act);
        ButterKnife.bind(this);
        parseIntent();

        // set up presenter
        mPresenter = new PicturePresenter(this);

        // set up photo view
        Glide.with(this)
                .load(mImgUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mPicImg.setImageBitmap(resource);
                        mAttacher = new PhotoViewAttacher(mPicImg, true);
                        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                            @Override
                            public void onViewTap(View view, float x, float y) {
                                hideSheetOrBack();
                            }
                        });
                        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                showSheet();
                                return true;
                            }
                        });
                    }
                });

    }

    @Override
    public void showSheet() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(mScroll);
        if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            Log.d("test", "sheet");
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void hideSheetOrBack() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(mScroll);
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            onBackPressed();
        }
    }

    @Override
    public void showSuccess(String success) {
        Snackbar.make(mPicCoorLay, success, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String error) {
        Snackbar.make(mPicCoorLay, error, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick({R.id.share_btn, R.id.copy_url_btn, R.id.save_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_btn:
                if (mUri == null) {
                    mPresenter.savePic(mImgUrl, mImgId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Uri>() {
                                @Override
                                public void onCompleted() {
                            }
                                @Override
                                public void onError(Throwable e) {
                                    showError(getResources().getString(R.string.save_error) + e.getMessage());
                                }
                                @Override
                                public void onNext(Uri uri) {
                                    mUri = uri;
                                    mPresenter.sharePic(mUri);
                                }
                            });
                } else {
                    mPresenter.sharePic(mUri);
                }
                break;
            case R.id.copy_url_btn:
                mPresenter.copyUrl(mImgUrl);
                break;
            case R.id.save_btn:
                mPresenter.savePic(mImgUrl, mImgId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Uri>() {
                            @Override
                            public void onCompleted() {

                            }
                            @Override
                            public void onError(Throwable e) {
                                showError(getResources().getString(R.string.save_error) + e.getMessage());
                            }

                            @Override
                            public void onNext(Uri uri) {
                                showSuccess(getResources().getString(R.string.save_success_to) + new File(uri.getPath()).getAbsolutePath());
                                mUri = uri;
                            }
                        });
                break;
        }
    }

    @Override
    public void setPresenter(PictureContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter = null;
        mAttacher.cleanup();
    }

}
