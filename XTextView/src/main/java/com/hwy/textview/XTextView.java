package com.hwy.textview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 作者: hewenyu
 * 日期: 2018/10/25 21:46
 * 说明: TextView 封装
 */
public class XTextView extends TextView {

    /**
     * 方形
     */
    private static final int SHAPE_SQUARE = 0;

    /**
     * 圆形
     */
    private static final int SHAPE_CIRCLE = 1;

    /**
     * 圆角默认的无效值
     */
    private static final int DEFAULT_CORNER_VALUE = -1;

    // region ---------- 背景 -----------

    /**
     * 默认方形
     */
    private int mBackgroundShape = SHAPE_SQUARE;

    /**
     * 背景
     */
    private GradientDrawable mNormalBackground;

    // endregion ------------------------

    // region ---------- 圆角 -----------
    /**
     * 四周的圆角（方形时有效）
     * 如果同时设置了下面四个的值，那么对应位置的圆角值就会被单独设置值的覆盖
     */
    private int mCorner = 0;

    private int mCornerLeftTop = DEFAULT_CORNER_VALUE;

    private int mCornerRightTop = DEFAULT_CORNER_VALUE;

    private int mCornerRightBottom = DEFAULT_CORNER_VALUE;

    private int mCornerLeftBottom = DEFAULT_CORNER_VALUE;

    // 四个圆角的半径,左上角开始顺时针方向，每两个值标示一个圆角
    private float[] mCornerRadii = new float[8];

    // endregion -----------------------

    // region ---------- 边框 -----------

    /**
     * 边框粗细
     */
    private int mBorderSize = 0;

    /**
     * 边框颜色
     */
    private int mBorderColor = Color.TRANSPARENT;

    /**
     * 边框虚线时线段的长度
     */
    private int mBorderDashWidth = 0;

    /**
     * 边框虚线时线段之间的间距
     */
    private int mBorderDashGap = 0;

    // endregion -----------------------

    public XTextView(Context context) {
        this(context, null);
    }

    public XTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XTextView);
        // 形状
        mBackgroundShape = array.getInt(R.styleable.XTextView_tvShape, SHAPE_SQUARE);

        // region --------- 圆角 ----------
        mCorner = array.getDimensionPixelSize(R.styleable.XTextView_tvCorner, 0);
        mCornerLeftTop = array.getDimensionPixelSize(R.styleable.XTextView_tvCornerLeftTop, DEFAULT_CORNER_VALUE);
        mCornerRightTop = array.getDimensionPixelSize(R.styleable.XTextView_tvCornerRightTop, DEFAULT_CORNER_VALUE);
        mCornerRightBottom = array.getDimensionPixelSize(R.styleable.XTextView_tvCornerRightBottom, DEFAULT_CORNER_VALUE);
        mCornerLeftBottom = array.getDimensionPixelSize(R.styleable.XTextView_tvCornerLeftBottom, DEFAULT_CORNER_VALUE);
        // endregion ----------------------

        // region --------- 边框 ----------
        mBorderSize = array.getDimensionPixelSize(R.styleable.XTextView_tvBorderSize, 0);
        mBorderColor = array.getColor(R.styleable.XTextView_tvBorderColor, Color.TRANSPARENT);
        mBorderDashWidth = array.getDimensionPixelSize(R.styleable.XTextView_tvBorderDashWidth, 0);
        mBorderDashGap = array.getDimensionPixelSize(R.styleable.XTextView_tvBorderDashGap, 0);
        // endregion ----------------------

        // region --------- 名称 ----------

        // endregion ----------------------

        array.recycle();

        init();
    }

    private void init() {

        // 更新背景
        updateBackground();

    }

    /**
     * 更新背景
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void updateBackground() {

        // 初始化背景
        if (mNormalBackground == null) {
            mNormalBackground = new GradientDrawable();
            Drawable drawable = getBackground();
            if (drawable instanceof ColorDrawable) {
                mNormalBackground.setColor(((ColorDrawable) drawable).getColor());
            }
            setBackground(mNormalBackground);
        }

        // 设置形状
        if (mBackgroundShape == SHAPE_SQUARE) {
            // 方形
            mNormalBackground.setShape(GradientDrawable.RECTANGLE);
        } else if (mBackgroundShape == SHAPE_CIRCLE) {
            // 圆形
            mNormalBackground.setShape(GradientDrawable.OVAL);
        }

        // 设置圆角
        setCorner();
        // 设置边框
        setBorder();

    }

    /**
     * 设置边框
     */
    private void setBorder() {
        if (mBorderSize < 0) {
            mBorderSize = 0;
        }

        mNormalBackground.setStroke(mBorderSize, mBorderColor, mBorderDashWidth, mBorderDashGap);
    }

    /**
     * 设置圆角，方形时才会调用此方法
     */
    private void setCorner() {

        if (mBackgroundShape == SHAPE_CIRCLE) {
            // 圆形直接跳过
            return;
        }

        if (mCorner < 0) {
            mCorner = 0;
        }

        // 左上角
        mCornerRadii[0] = mCornerLeftTop > DEFAULT_CORNER_VALUE ? mCornerLeftTop : mCorner;
        mCornerRadii[1] = mCornerLeftTop > DEFAULT_CORNER_VALUE ? mCornerLeftTop : mCorner;
        // 右上角
        mCornerRadii[2] = mCornerRightTop > DEFAULT_CORNER_VALUE ? mCornerRightTop : mCorner;
        mCornerRadii[3] = mCornerRightTop > DEFAULT_CORNER_VALUE ? mCornerRightTop : mCorner;
        // 右下角
        mCornerRadii[4] = mCornerRightBottom > DEFAULT_CORNER_VALUE ? mCornerRightBottom : mCorner;
        mCornerRadii[5] = mCornerRightBottom > DEFAULT_CORNER_VALUE ? mCornerRightBottom : mCorner;
        // 左下角
        mCornerRadii[6] = mCornerLeftBottom > DEFAULT_CORNER_VALUE ? mCornerLeftBottom : mCorner;
        mCornerRadii[7] = mCornerLeftBottom > DEFAULT_CORNER_VALUE ? mCornerLeftBottom : mCorner;

        mNormalBackground.setCornerRadii(mCornerRadii);
    }

}
