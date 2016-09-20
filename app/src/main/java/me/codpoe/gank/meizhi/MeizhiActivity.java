package me.codpoe.gank.meizhi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gank.R;
import me.codpoe.gank.data.bean.MeizhiBean;
import me.codpoe.gank.meizhi.adapter.MeizhiRvAdapter;
import me.codpoe.gank.picture.PictureActivity;

public class MeizhiActivity extends AppCompatActivity implements MeizhiContract.View {

    @BindView(R.id.drawer_lay)
    DrawerLayout mDrawerLay;
    @BindView(R.id.coor_lay)
    CoordinatorLayout mCoorLay;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.refresh_lay)
    SwipeRefreshLayout mRefreshLay;
    @BindView(R.id.meizhi_rv)
    RecyclerView mMeizhiRv;
    @BindView(R.id.meizhi_fab)
    FloatingActionButton mMeizhiFab;

    private MeizhiContract.Presenter mPresenter;

    private List<MeizhiBean.ResultsBean> mMeizhiList;
    private MeizhiRvAdapter mMeizhiRvAdapter;
    private StaggeredGridLayoutManager mManager;
    private int mPage;
    private boolean hasRefreshed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meizhi_act);
        ButterKnife.bind(this);

        // set up presenter
        mPresenter = new MeizhiPresenter(MeizhiActivity.this);

        // set up toolbar
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        // set up drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLay, mToolbar, 0, 0);
        toggle.syncState();
        mDrawerLay.addDrawerListener(toggle);

        // set up nav view
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.meizhi_nav_menu_item:
                        break;
                    default:
                        break;
                }
                item.setChecked(true);
                mDrawerLay.closeDrawers();
                return true;
            }
        });

        // set up SwipeRefreshLayout
        mRefreshLay.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        mRefreshLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mPresenter.loadMeizhi(mPage);
            }
        });

        // set up recyclerView
        mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mMeizhiList = new ArrayList<>();
        mMeizhiRvAdapter = new MeizhiRvAdapter(this, mMeizhiList);
        mMeizhiRv.setLayoutManager(mManager);
        mMeizhiRv.setAdapter(mMeizhiRvAdapter);
        mMeizhiRvAdapter.setOnItemClickListener(new MeizhiRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = PictureActivity.newIntent(
                        MeizhiActivity.this,
                        mMeizhiList.get(position).getUrl(),
                        mMeizhiList.get(position).getId());
                startActivity(intent);
//                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MeizhiActivity.this, view, "meizhi").toBundle());
            }
        });
        mMeizhiRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] lastPositions = mManager.findLastVisibleItemPositions(new int[mManager.getSpanCount()]);
                if (Math.max(lastPositions[0], lastPositions[1]) >= mManager.getItemCount() - 4 && dy > 0) {
                    mPresenter.loadMeizhi(++mPage);
                }
            }
        });

        /**
         * set up fab
         * back to the top quickly
         */
        mMeizhiFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMeizhiRv.smoothScrollToPosition(0);
            }
        });

        // set up footer view


        mPresenter.start();

    }

    @Override
    public void showMeizhi(List<MeizhiBean.ResultsBean> meizhiList) {
        if (mPage == 1) {
            mMeizhiList.clear();
            mMeizhiRvAdapter.notifyDataSetChanged();
        }
        for (MeizhiBean.ResultsBean resultsBean : meizhiList) {
            mMeizhiList.add(resultsBean);
//            mMeizhiRvAdapter.notifyItemInserted(mMeizhiList.size() - 1);
            if (mMeizhiList.size() == 1) {
                mMeizhiRvAdapter.notifyDataSetChanged();
            } else {
                mMeizhiRvAdapter.notifyItemInserted(mMeizhiList.size() - 1);
            }
        }
    }

    @Override
    public void showSuccess(String success) {
        mRefreshLay.setRefreshing(false);
//        mLoadingBar.hide();
        if (mPage == 1) {
            Snackbar.make(mCoorLay, success, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showError(String error) {
        Snackbar.make(mCoorLay, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MeizhiContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasRefreshed) {
            hasRefreshed = true;
            mRefreshLay.setRefreshing(true);
            mPage = 1;
            mPresenter.loadMeizhi(mPage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }
}
