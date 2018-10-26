package com.hwy.textview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 作者: hewenyu
 * 日期: 2018/10/25 21:46
 * 说明: TextView 封装
 */
public class XTextView extends TextView {

    // region ---------- 常量 -----------
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
    private static final int INVALIDATE_CORNER_VALUE = -1;

    /**
     * 按压的状态
     */
    private static final int STATE_PRESS = 0;

    private static final int STATE_ENABLE = 1;

    // endregion ------------------------

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

    /**
     * 左上角
     */
    private int mCornerLeftTop = INVALIDATE_CORNER_VALUE;

    /**
     * 右上角
     */
    private int mCornerRightTop = INVALIDATE_CORNER_VALUE;

    /**
     * 右下角
     */
    private int mCornerRightBottom = INVALIDATE_CORNER_VALUE;

    /**
     * 左下角
     */
    private int mCornerLeftBottom = INVALIDATE_CORNER_VALUE;

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

    // region ---------- 颜色 -----------

    /**
     * 颜色状态
     */
    private int[][] mColorStates = new int[2][];

    /**
     * TextView 的状态：Press、enable
     */
    private int mState = STATE_PRESS;

    /**
     * 是否使用状态
     */
    private boolean useState = false;
    // 文字
    private int mInvalidStateTextColor = Color.TRANSPARENT;
    private int mValidStateTextColor = Color.TRANSPARENT;
    // 背景
    private int mInvalidStateBgColor = Color.TRANSPARENT;
    private int mValidStateBgColor = Color.TRANSPARENT;
    // 边框
    private int mInvalidStateBorderColor = Color.TRANSPARENT;
    private int mValidStateBorderColor = Color.TRANSPARENT;

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
        mCornerLeftTop = array.getDimensionPixelSize(R.styleable.XTextView_tvCornerLeftTop, INVALIDATE_CORNER_VALUE);
        mCornerRightTop = array.getDimensionPixelSize(R.styleable.XTextView_tvCornerRightTop, INVALIDATE_CORNER_VALUE);
        mCornerRightBottom = array.getDimensionPixelSize(R.styleable.XTextView_tvCornerRightBottom, INVALIDATE_CORNER_VALUE);
        mCornerLeftBottom = array.getDimensionPixelSize(R.styleable.XTextView_tvCornerLeftBottom, INVALIDATE_CORNER_VALUE);
        // endregion ----------------------

        // region --------- 边框 ----------
        mBorderSize = array.getDimensionPixelSize(R.styleable.XTextView_tvBorderSize, 0);
        mBorderColor = array.getColor(R.styleable.XTextView_tvBorderColor, Color.TRANSPARENT);
        mBorderDashWidth = array.getDimensionPixelSize(R.styleable.XTextView_tvBorderDashWidth, 0);
        mBorderDashGap = array.getDimensionPixelSize(R.styleable.XTextView_tvBorderDashGap, 0);
        // endregion ----------------------

        // region --------- 颜色 ----------
        mState = array.getInt(R.styleable.XTextView_tvState, STATE_PRESS);
        useState = array.getBoolean(R.styleable.XTextView_tvUseState, false);
        mInvalidStateTextColor = array.getColor(R.styleable.XTextView_tvStateTextColorInvalid, Color.TRANSPARENT);
        mValidStateTextColor = array.getColor(R.styleable.XTextView_tvStateTextColorValid, Color.TRANSPARENT);
        mInvalidStateBgColor = array.getColor(R.styleable.XTextView_tvStateBgColorInvalid, Color.TRANSPARENT);
        mValidStateBgColor = array.getColor(R.styleable.XTextView_tvStateBgColorValid, Color.TRANSPARENT);
        mInvalidStateBorderColor = array.getColor(R.styleable.XTextView_tvStateBorderColorInvalid, Color.TRANSPARENT);
        mValidStateBorderColor = array.getColor(R.styleable.XTextView_tvStateBorderColorValid, Color.TRANSPARENT);

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
        // 设置颜色
        setColor();

    }

    /**
     * 设置文字/背景状态颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setColor() {
        if (!useState) {
            return;
        }

        setClickable(true);

        if (mState == STATE_PRESS) {
            // 按压状态
            mColorStates[0] = new int[]{android.R.attr.state_pressed};
            mColorStates[1] = new int[]{-android.R.attr.state_pressed};
        } else if (mState == STATE_ENABLE) {
            // enable状态
            mColorStates[0] = new int[]{android.R.attr.state_enabled};
            mColorStates[1] = new int[]{-android.R.attr.state_enabled};
        } else {
            return;
        }

        // 文字颜色
        if (mValidStateTextColor != Color.TRANSPARENT
                || mInvalidStateTextColor != Color.TRANSPARENT) {

            int[] tempText = new int[]{
                    mValidStateTextColor,
                    mInvalidStateTextColor
            };

            ColorStateList tempTextState = new ColorStateList(mColorStates, tempText);
            setTextColor(tempTextState);
        }

        // 背景颜色
        if (mValidStateBgColor != Color.TRANSPARENT
                || mInvalidStateBgColor != Color.TRANSPARENT) {

            int[] tempBg = new int[]{
                    mValidStateBgColor,
                    mInvalidStateBgColor
            };

            ColorStateList tempBgState = new ColorStateList(mColorStates, tempBg);
            mNormalBackground.setColor(tempBgState);
        }

        // 边框颜色
        if ((mValidStateBorderColor != Color.TRANSPARENT
                || mInvalidStateBorderColor != Color.TRANSPARENT) && mBorderSize > 0) {

            int[] tempBorder = new int[]{
                    mValidStateBorderColor,
                    mInvalidStateBorderColor
            };

            ColorStateList tempBorderState = new ColorStateList(mColorStates, tempBorder);
            mNormalBackground.setStroke(mBorderSize, tempBorderState);
        }

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
        mCornerRadii[0] = mCornerLeftTop > INVALIDATE_CORNER_VALUE ? mCornerLeftTop : mCorner;
        mCornerRadii[1] = mCornerLeftTop > INVALIDATE_CORNER_VALUE ? mCornerLeftTop : mCorner;
        // 右上角
        mCornerRadii[2] = mCornerRightTop > INVALIDATE_CORNER_VALUE ? mCornerRightTop : mCorner;
        mCornerRadii[3] = mCornerRightTop > INVALIDATE_CORNER_VALUE ? mCornerRightTop : mCorner;
        // 右下角
        mCornerRadii[4] = mCornerRightBottom > INVALIDATE_CORNER_VALUE ? mCornerRightBottom : mCorner;
        mCornerRadii[5] = mCornerRightBottom > INVALIDATE_CORNER_VALUE ? mCornerRightBottom : mCorner;
        // 左下角
        mCornerRadii[6] = mCornerLeftBottom > INVALIDATE_CORNER_VALUE ? mCornerLeftBottom : mCorner;
        mCornerRadii[7] = mCornerLeftBottom > INVALIDATE_CORNER_VALUE ? mCornerLeftBottom : mCorner;

        mNormalBackground.setCornerRadii(mCornerRadii);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setColor();
    }

    @Override
    public void setTextColor(int color) {
        this.useState = false;
        super.setTextColor(color);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(text)) {
            // 防止空指针异常
            text = "";
        }
        super.setText(text, type);
    }

    // region --------------- get/set -------------------

    /**
     * 设置文字状态颜色
     *
     * @param validStateColor
     * @param invalidStateColor
     */
    public void setTextColor(int validStateColor, int invalidStateColor) {
        this.useState = true;
        this.mValidStateTextColor = validStateColor;
        this.mInvalidStateTextColor = invalidStateColor;
        setColor();
    }

    /**
     * 设置背景颜状态颜色
     *
     * @param validStateColor
     * @param invalidStateColor
     */
    public void setBackgroundColor(int validStateColor, int invalidStateColor) {
        this.useState = true;
        this.mValidStateBgColor = validStateColor;
        this.mInvalidStateBgColor = invalidStateColor;
        setColor();
    }

    /**
     * 设置边框状态的颜色
     *
     * @param validStateColor
     * @param invalidStateColor
     */
    public void setBorderColor(int validStateColor, int invalidStateColor) {
        this.useState = true;
        this.mValidStateBorderColor = validStateColor;
        this.mInvalidStateBorderColor = invalidStateColor;
        setColor();
    }

    /**
     * 是否使用状态颜色
     *
     * @param useState
     */
    public void useState(boolean useState) {
        this.useState = useState;
        if (!useState) {
            if (mState == STATE_PRESS) {
                setTextColor(mInvalidStateTextColor);
                setBackgroundColor(mInvalidStateBgColor);
            } else if (mState == STATE_ENABLE) {
                setTextColor(mValidStateTextColor);
                setBackgroundColor(mValidStateBgColor);
            }
        }
    }

    /**
     * 设置圆角
     *
     * @param corner
     */
    public void setCorner(int corner) {
        this.mCorner = corner;
        mCornerLeftTop = INVALIDATE_CORNER_VALUE;
        mCornerRightTop = INVALIDATE_CORNER_VALUE;
        mCornerRightBottom = INVALIDATE_CORNER_VALUE;
        mCornerLeftBottom = INVALIDATE_CORNER_VALUE;
        setCorner();
    }

    /**
     * 设置圆角
     *
     * @param leftTop
     * @param rightTop
     * @param rightBottom
     * @param leftBottom
     */
    public void setCorner(int leftTop, int rightTop, int rightBottom, int leftBottom) {
        mCornerLeftTop = leftTop;
        mCornerRightTop = rightTop;
        mCornerRightBottom = rightBottom;
        mCornerLeftBottom = leftBottom;
        setCorner();
    }

    /**
     * 设置边框颜色
     *
     * @param borderColor
     */
    public void setBorderColor(int borderColor) {
        this.mBorderColor = borderColor;
        setBorder();
    }

    /**
     * 设置边框大小
     *
     * @param borderSize
     */
    public void setBorderSize(int borderSize) {
        this.mBorderSize = borderSize;
        setBorder();
    }

    /**
     * 设置边框虚线的线段长度
     *
     * @param dashWidth
     */
    public void setBorderDashWidth(int dashWidth) {
        this.mBorderDashWidth = dashWidth;
        setBorder();
    }

    /**
     * 设置边框虚线的间距
     *
     * @param dashGap
     */
    public void setBorderDashGap(int dashGap) {
        this.mBorderDashGap = dashGap;
        setBorder();
    }

    // endregion -----------------------------------

}
