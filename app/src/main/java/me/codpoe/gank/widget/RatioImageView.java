package me.codpoe.gank.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Codpoe on 2016/9/18.
 */
public class RatioImageView extends ImageView {

    private int mWidth;
    private int mHeight;


    public RatioImageView(Context context) {
        super(context);
    }


    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = (int)((double)(mWidth) * (Math.random() + 0.5f));

        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        w = oldw;
        h = oldh;
    }
}
