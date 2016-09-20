package me.codpoe.gank.meizhi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gank.R;
import me.codpoe.gank.app.App;
import me.codpoe.gank.data.bean.MeizhiBean;

/**
 * Created by Codpoe on 2016/9/15.
 */
public class MeizhiRvAdapter extends RecyclerView.Adapter<MeizhiRvAdapter.ViewHolder> {

    private Context mContext;
    private List<MeizhiBean.ResultsBean> mMeizhiList = new ArrayList<>();

    private OnItemClickListener mListener;

    public MeizhiRvAdapter(Context context, List<MeizhiBean.ResultsBean> meizhiList) {
        mContext = context;
        mMeizhiList = meizhiList;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return Math.round((float) App.SCREEN_WIDTH / (float) mMeizhiList.get(position).getHeight());
//    }


    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meizhi_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        /**
         * 大概、可能、或许解决了 StaggeredGridLayout 因「快速滚动」而出现的图片错乱问题，以及因「快速返回」而出现的图片错位问题
         */
        if (mMeizhiList.get(holder.getAdapterPosition()).getHeight() > 0) {
            holder.mMeizhiImg.getLayoutParams().height = mMeizhiList.get(holder.getAdapterPosition()).getHeight();
        }

        final String id = mMeizhiList.get(holder.getAdapterPosition()).getId();
        holder.mMeizhiImg.setTag(id);

        Glide.with(mContext)
                .load(mMeizhiList.get(position).getUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(App.SCREEN_WIDTH / 2, App.SCREEN_HEIGHT / 2) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (holder.getAdapterPosition() != RecyclerView.NO_POSITION && holder.mMeizhiImg.getTag().equals(id)) {
                            if (mMeizhiList.get(holder.getAdapterPosition()).getHeight() == 0) {
                                int width = resource.getWidth();
                                int height = resource.getHeight();
                                int realHeight = (int) ((App.SCREEN_WIDTH / 2) * ((float) height / width));
                                mMeizhiList.get(holder.getAdapterPosition()).setHeight(realHeight);
                                holder.mMeizhiImg.getLayoutParams().height = realHeight;
                            }
                            holder.mMeizhiImg.setImageBitmap(resource);
                        }
                    }
                });
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return mMeizhiList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // click interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.meizhi_img)
        ImageView mMeizhiImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
