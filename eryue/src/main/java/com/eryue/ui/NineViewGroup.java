package com.eryue.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.library.util.StringUtils;

public class NineViewGroup extends ViewGroup implements OnClickListener {

    public static final int KEY_INDEX_TAG = -109;

    private Context mContext;

    /**
     * 水平item之间的间隔
     */
    private int deltaX = 8;

    /**
     * 垂直item之间的间隔
     */
    private int deltaY = 8;

    private int dataCount;

    private float singleScale = 1;

    private float multiScale = 1;

    private boolean isLimit;

    private OnNineViewListener listener;

    public NineViewGroup(Context context) {
        super(context);
        init(context);
    }

    public NineViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        deltaX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                deltaX, getResources().getDisplayMetrics());
        deltaY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                deltaY, getResources().getDisplayMetrics());
    }

    /**
     * 设置九宫格的水平间隔
     *
     * @param deltaX
     */
    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    /**
     * 设施九宫格的垂直间隔
     *
     * @param deltaY
     */
    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int childCount = dataCount;
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        int heightSpec = 0;
        final int itemWidth = (width - 2 * deltaX) / 3;

        if (childCount == 1) {
            heightSpec = widthMeasureSpec;
        } else if (childCount > 1 && childCount <= 3) {
            heightSpec = MeasureSpec
                    .makeMeasureSpec(itemWidth + getPaddingTop()
                            + getPaddingBottom(), MeasureSpec.EXACTLY);
        } else if (childCount > 3) {
            int rowCount = ((childCount - 1) / 3) + 1;
            heightSpec = MeasureSpec.makeMeasureSpec(itemWidth * rowCount
                            + deltaY * (rowCount - 1) + getPaddingTop() + getPaddingBottom(),
                    MeasureSpec.EXACTLY);
        }

        setMeasuredDimension(widthMeasureSpec, heightSpec);

        if (childCount > 1) {
            int measItemWidht = MeasureSpec.makeMeasureSpec(
                    (int) (itemWidth * multiScale), MeasureSpec.EXACTLY);
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                child.measure(measItemWidht, measItemWidht);
            }
        } else {
            widthMeasureSpec = MeasureSpec
                    .makeMeasureSpec(
                            (int) (MeasureSpec.getSize(widthMeasureSpec) * singleScale),
                            MeasureSpec.EXACTLY);
            int height = 0;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i); // 获得每个对象的引用
                this.measureChild(child, widthMeasureSpec, widthMeasureSpec);
                height += child.getMeasuredHeight();
            }

            heightSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, heightSpec);
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int pxToDip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = dataCount;

        int startLeft = this.getPaddingLeft();
        int startTop = this.getPaddingTop();

        int lines = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (i % 3 == 0) {
                if (i != 0) {
                    lines++;
                    startLeft = this.getPaddingLeft();
                    startTop = this.getPaddingTop()
                            + (child.getMeasuredHeight() + deltaY) * lines;
                }
            } else {
                startLeft = startLeft + child.getMeasuredWidth() + deltaX;
            }
            child.layout(startLeft, startTop,
                    startLeft + child.getMeasuredWidth(),
                    startTop + child.getMeasuredHeight());
        }
    }

    public ScaleImageView getChildAt(int index) {
        return (ScaleImageView) super.getChildAt(index);
    }

    public ScaleImageView getImageView(int index) {
        return (ScaleImageView) super.getChildAt(index);
    }

    public int getDataCount() {
        return dataCount;
    }

    public void setDataCount(int dataCount) {

        //修改只有一张图的时候，回显示大图问题
        if(dataCount == 1){
            dataCount = 3;
        }

        this.dataCount = dataCount;

        int count = getChildCount();
        if (dataCount > count) {
            for (int i = 0; i < dataCount - count; i++) {
                ScaleImageView imageView = new ScaleImageView(getContext());
                imageView.setTag(KEY_INDEX_TAG, count + i);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setOnClickListener(this);
                addView(imageView);
            }
        } else if (dataCount < count) {
            for (int i = count - 1; i >= dataCount; i--) {
                ImageView imageView = (ImageView) getChildAt(i);
                imageView.setImageDrawable(null);
                // removeView(getChildAt(i));
                getChildAt(i).setVisibility(View.GONE);
            }
        }

        for (int i = 0; i < dataCount; i++) {
            ScaleImageView imageView = (ScaleImageView) getChildAt(i);
            imageView.setVisibility(View.VISIBLE);
            if (dataCount == 1) {
                imageView.setScaled(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_START);
            } else {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setSpicSideRate(0);
                imageView.setScaled(false);
            }
            imageView.setLimit(isLimit);
        }

        if (dataCount == 0) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }

        requestLayout();
        postInvalidate();
    }

    public class ScaleImageView extends ImageView {

        private float spicSideRate;

        private float minRate;

        private boolean isScaled;

        private boolean isLimit;

        public ScaleImageView(Context context) {
            super(context);
        }

        public ScaleImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            if (!isScaled) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }

            if (minRate > 0 && spicSideRate > 0 && spicSideRate < minRate) {
                spicSideRate = minRate;
            }

            if (spicSideRate > 0) {
                onMeasureSide(widthMeasureSpec, heightMeasureSpec);
            } else {
                onMeasureNormal(widthMeasureSpec, heightMeasureSpec);
            }
        }

        @SuppressWarnings("WrongCall")
        protected void onMeasureSide(int widthMeasureSpec, int heightMeasureSpec) {

            if (!isScaled) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }

            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                    getDefaultSize(0, heightMeasureSpec));

            //图片最大宽度，要距离最右边一个文字距离
            float width = getMeasuredWidth() - StringUtils.dipToPx(15);
            float height = getMeasuredHeight();

            if (isLimit) {
                if (spicSideRate >= 1) {
                    height = width / spicSideRate;
                } else if (spicSideRate > 0 && spicSideRate < 1) {
                    width = height * spicSideRate;
                }
            } else {
                height = width / spicSideRate;
            }

            Drawable drawable = getDrawable();
            if (null == drawable) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
            float drawableWidth = drawable.getIntrinsicWidth() * 2;
            float drawableHeight = drawable.getIntrinsicHeight() * 2;
            if (drawable != null && drawableWidth < width) {
                width = drawableWidth;
                height = drawableHeight;
                float rate = width / height;
                if (minRate > 0 && rate < minRate) {
                    height = width / minRate;
                }
            }

            if (drawable != null && width < drawableWidth) {
                float drawableRate = drawableWidth / drawableHeight;
                height = width / drawableRate;
            }


            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) width,
                    MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height,
                    MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @SuppressWarnings("WrongCall")
        protected void onMeasureNormal(int widthMeasureSpec,
                                       int heightMeasureSpec) {

            if (!isScaled) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }

            Drawable drawable = getDrawable();
            if (drawable == null) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }

            float imageWidth = drawable.getIntrinsicWidth();
            float imageHeight = drawable.getIntrinsicHeight();

            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                    getDefaultSize(0, heightMeasureSpec));

            float width = getMeasuredWidth();
            float height = getMeasuredHeight();

            if (isLimit) {
                if (imageWidth > imageHeight) {
                    height = width * imageHeight / imageWidth;
                } else {
                    width = height * imageWidth / imageHeight;
                }
            } else {
                height = width * imageHeight / imageWidth;
            }

            if (imageWidth * 2 < width && imageHeight * 2 < height) {
                width = imageWidth * 2;
                height = imageHeight * 2;
            }

            float rate = width / height;
            if (minRate > 0 && rate < minRate) {
                height = width / minRate;
            }

            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) width,
                    MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height,
                    MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        public boolean isScaled() {
            return isScaled;
        }

        public void setScaled(boolean isScaled) {
            this.isScaled = isScaled;
        }

        public boolean isLimit() {
            return isLimit;
        }

        public void setLimit(boolean isLimit) {
            this.isLimit = isLimit;
        }

        public void setSpicSideRate(float spicSideRate) {
            this.spicSideRate = spicSideRate;
        }

        public void setMinRate(float minRate) {
            this.minRate = minRate;
        }
    }

    public float getSingleScale() {
        return singleScale;
    }

    public void setSingleScale(float scale) {
        this.singleScale = scale;
    }

    public float getMultiScale() {
        return multiScale;
    }

    public void setMultiScale(float multiScale) {
        this.multiScale = multiScale;
    }

    public boolean isLimit() {
        return isLimit;
    }

    public void setLimit(boolean isLimit) {
        this.isLimit = isLimit;
    }

    public void setListener(OnNineViewListener listener) {
        this.listener = listener;
    }

    public interface OnNineViewListener {
        public void onItemClick(ViewGroup nineView, ImageView imageView,
                                int index);
    }

    @Override
    public void onClick(View v) {

        int index = -1;
        try {
            index = (Integer) v.getTag(KEY_INDEX_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listener != null) {
            listener.onItemClick(this, (ImageView) v, index);
        }
    }
}
