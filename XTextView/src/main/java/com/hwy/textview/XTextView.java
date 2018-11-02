package com.hwy.textview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

import com.hwy.textview.listener.OnDrawableClickListener;

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

    private static final int TAG_GRAVITY_LEFT_TOP = 0;
    private static final int TAG_GRAVITY_TOP_HORIZONTAL = 1;
    private static final int TAG_GRAVITY_LEFT_VERTICAL = 2;
    private static final int TAG_GRAVITY_LEFT_BOTTOM = 3;

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
    private float mCorner = 0;

    /**
     * 左上角
     */
    private float mCornerLeftTop = INVALIDATE_CORNER_VALUE;

    /**
     * 右上角
     */
    private float mCornerRightTop = INVALIDATE_CORNER_VALUE;

    /**
     * 右下角
     */
    private float mCornerRightBottom = INVALIDATE_CORNER_VALUE;

    /**
     * 左下角
     */
    private float mCornerLeftBottom = INVALIDATE_CORNER_VALUE;

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

    // region ---------- 四周线条 -------

    private Paint mLinePaint;

    private Rect mLineRect;

    /**
     * 线条的颜色
     */
    private int mLineColor = Color.TRANSPARENT;

    private int mBottomLineSize = 0;
    private int mBottomLineMarginLeft = 0;
    private int mBottomLineMarginRight = 0;

    private int mTopLineSize = 0;
    private int mTopLineMarginLeft = 0;
    private int mTopLineMarginRight = 0;

    private int mLeftLineSize = 0;
    private int mLeftLineMarginTop = 0;
    private int mLeftLineMarginBottom = 0;

    private int mRightLineSize = 0;
    private int mRightLineMarginTop = 0;
    private int mRightLineMarginBottom = 0;

    // endregion -----------------------

    // region ---------- 标签 -----------

    private Paint mTagPaint;

    private String mTagText = "";
    private int mTagTextColor = Color.TRANSPARENT;
    private int mTagTextSize = 0;

    private int mTagGravity = TAG_GRAVITY_LEFT_TOP;

    private boolean useTagSeparator = false;

    private String mTagSeparator = ":";

    private int mTagPadding = 0;

    /**
     * tag 文本的长/高 + tagPadding，加到padding的值里面
     */
    private int mTagTextLength;


    // endregion -----------------------

    // region ---------- 徽章 -----------

    private static final int BADGE_NUMBER = 0;

    private static final int BADGE_DOT = 1;

    private static final int LOCAL_TEXT = 0;

    private static final int LOCAL_TAG = 1;

    private static final int LOCAL_DRAWABLE = 2;

    private Paint mBadgePaint;

    /**
     * 徽章的文本内容
     */
    private int mBadgeText = 0;

    /**
     * 徽章的文本大小
     */
    private int mBadgeTextSize = 0;

    /**
     * 徽章的文本颜色
     */
    private int mBadgeTextColor = Color.WHITE;

    /**
     * 徽章的背景颜色
     */
    private int mBadgeBgColor = Color.RED;

    /**
     * 圆形的半径
     */
    private int mBadgeRadius = 0;

    /**
     * 徽章四周的间距
     */
    private int mBadgeOffsetX = 0;
    private int mBadgeOffsetY = 0;

    /**
     * 徽章显示的风格
     */
    private int mBadgeStyle = BADGE_NUMBER;

    /**
     * 徽章显示的位置
     */
    private int mBadgeLocal = LOCAL_TEXT;

    // endregion -----------------------

    // region ---------- drawable -------

    private static final int DIRECTION_LEFT = 0;
    private static final int DIRECTION_TOP = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_BOTTOM = 3;

    private int mDrawableWidth;

    private int mDrawableHeight;

    /**
     * 指定方向的Drawable，固定宽高
     */
    private int mDrawableDirection = DIRECTION_LEFT;

    /**
     * 设置了Drawable的资源是否居中
     */
    private boolean mDrawableCenter = false;

    private int mCurrentTouch = OnDrawableClickListener.INVALID_TOUCH;

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

        // region --------- 四周线条 ------
        mLineColor = array.getColor(R.styleable.XTextView_tvLineColor, Color.TRANSPARENT);
        // bottomLine
        mBottomLineSize = array.getDimensionPixelSize(R.styleable.XTextView_tvBottomLineSize, 0);
        mBottomLineMarginLeft = array.getDimensionPixelSize(R.styleable.XTextView_tvBottomLineMarginLeft, 0);
        mBottomLineMarginRight = array.getDimensionPixelSize(R.styleable.XTextView_tvBottomLineMarginRight, 0);
        // topLine
        mTopLineSize = array.getDimensionPixelSize(R.styleable.XTextView_tvTopLineSize, 0);
        mTopLineMarginLeft = array.getDimensionPixelSize(R.styleable.XTextView_tvTopLineMarginLeft, 0);
        mTopLineMarginRight = array.getDimensionPixelSize(R.styleable.XTextView_tvTopLineMarginRight, 0);
        // leftLine
        mLeftLineSize = array.getDimensionPixelSize(R.styleable.XTextView_tvLeftLineSize, 0);
        mLeftLineMarginTop = array.getDimensionPixelSize(R.styleable.XTextView_tvLeftLineMarginTop, 0);
        mLeftLineMarginBottom = array.getDimensionPixelSize(R.styleable.XTextView_tvLeftLineMarginBottom, 0);
        // rightLine
        mRightLineSize = array.getDimensionPixelSize(R.styleable.XTextView_tvRightLineSize, 0);
        mRightLineMarginTop = array.getDimensionPixelSize(R.styleable.XTextView_tvRightLineMarginTop, 0);
        mRightLineMarginBottom = array.getDimensionPixelSize(R.styleable.XTextView_tvRightLineMarginBottom, 0);
        // endregion ----------------------

        // region --------- 标签 ----------
        mTagText = array.getString(R.styleable.XTextView_tvTagText);
        mTagTextColor = array.getColor(R.styleable.XTextView_tvTagTextColor, Color.GRAY);
        mTagTextSize = array.getDimensionPixelSize(R.styleable.XTextView_tvTagTextSize, 0);
        mTagSeparator = array.getString(R.styleable.XTextView_tvTagSeparator);
        if (TextUtils.isEmpty(mTagSeparator)) {
            mTagSeparator = ":";
        }
        useTagSeparator = array.getBoolean(R.styleable.XTextView_tvUseTagSeparator, false);
        mTagPadding = array.getDimensionPixelSize(R.styleable.XTextView_tvTagPadding, 0);
        mTagGravity = array.getInt(R.styleable.XTextView_tvTagGravity, TAG_GRAVITY_LEFT_TOP);

        // endregion ----------------------

        // region --------- 徽章 ----------

        mBadgeText = array.getInt(R.styleable.XTextView_tvBadgeText, 0);
        mBadgeTextColor = array.getColor(R.styleable.XTextView_tvBadgeTextColor, Color.WHITE);
        mBadgeTextSize = array.getDimensionPixelSize(R.styleable.XTextView_tvBadgeTextSize, 10);
        mBadgeBgColor = array.getColor(R.styleable.XTextView_tvBadgeBgColor, Color.RED);
        mBadgeRadius = array.getDimensionPixelSize(R.styleable.XTextView_tvBadgeRadius, 20);
        mBadgeStyle = array.getInt(R.styleable.XTextView_tvBadgeStyle, BADGE_NUMBER);
        mBadgeLocal = array.getInt(R.styleable.XTextView_tvBadgeLocal, LOCAL_TEXT);
        mBadgeOffsetX = array.getDimensionPixelSize(R.styleable.XTextView_tvBadgeOffsetX, 0);
        mBadgeOffsetY = array.getDimensionPixelSize(R.styleable.XTextView_tvBadgeOffsetY, 0);

        // endregion ----------------------

        // region --------- drawable --------

        mDrawableHeight = array.getDimensionPixelSize(R.styleable.XTextView_tvDrawableHeight, 0);
        mDrawableWidth = array.getDimensionPixelSize(R.styleable.XTextView_tvDrawableWidth, 0);
        mDrawableCenter = array.getBoolean(R.styleable.XTextView_tvDrawableCenter, false);
        mDrawableDirection = array.getInt(R.styleable.XTextView_tvDrawableDirection, DIRECTION_LEFT);

        // endregion ----------------------
        array.recycle();

        init();
    }

    private void init() {

        mLinePaint = new Paint();
        mLinePaint.setDither(true);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(mLineColor);
        mLineRect = new Rect();

        mTagPaint = new Paint();
        mTagPaint.setColor(mTagTextColor);
        mTagPaint.setTextSize(mTagTextSize);
        mTagPaint.setStyle(Paint.Style.FILL);
        mTagPaint.setDither(true);
        mTagPaint.setAntiAlias(true);

        mBadgePaint = new Paint();
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setDither(true);
        mBadgePaint.setStyle(Paint.Style.FILL);
        mBadgePaint.setTextSize(mBadgeTextSize);
        mBadgePaint.setTextAlign(Paint.Align.CENTER);

        mTagPaint = new Paint();
        mTagPaint.setDither(true);
        mTagPaint.setAntiAlias(true);
        mTagPaint.setTextSize(mTagTextSize);
        mTagPaint.setColor(mTagTextColor);

        // 更新背景
        updateBackground();
    }


    /**
     * 更新背景
     */
    private void updateBackground() {

        // 初始化背景
        if (mNormalBackground == null) {
            mNormalBackground = new GradientDrawable();
            Drawable drawable = getBackground();
            if (drawable instanceof ColorDrawable) {
                mNormalBackground.setColor(((ColorDrawable) drawable).getColor());
            } else {
                mNormalBackground.setColor(Color.TRANSPARENT);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(mNormalBackground);
            }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

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
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentTouch = OnDrawableClickListener.INVALID_TOUCH;

                // drawable上的触摸事件
                if (hasDrawableLeftRange(event)) {
                    mCurrentTouch = OnDrawableClickListener.TOUCH_DRAWABLE_LEFT;
                    return true;
                }

                if (hasDrawableRightRange(event)) {
                    mCurrentTouch = OnDrawableClickListener.TOUCH_DRAWABLE_RIGHT;
                    return true;
                }


                break;
            case MotionEvent.ACTION_UP:

                if (mCurrentTouch != -1) {
                    return parseDrawableTouch(event);
                }

                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 解析Drawable上的触摸事件
     *
     * @param event
     * @return
     */
    private boolean parseDrawableTouch(MotionEvent event) {

        if (mCurrentTouch == OnDrawableClickListener.TOUCH_DRAWABLE_LEFT) {
            if (hasDrawableLeftRange(event)) {
                onDrawableClickListener.onDrawableLeftClickListener(this);
                return true;
            }
        } else if (mCurrentTouch == OnDrawableClickListener.TOUCH_DRAWABLE_RIGHT) {
            if (hasDrawableRightRange(event)) {
                onDrawableClickListener.onDrawableRightClickListener(this);
                return true;
            }
        }

        return false;
    }

    /**
     * 触摸事件位于 drawableRight 上
     *
     * @param event
     * @return
     */
    private boolean hasDrawableRightRange(MotionEvent event) {
        if (onDrawableClickListener == null) {
            return false;
        }

        Drawable dRight = getCompoundDrawables()[2];
        if (dRight == null) {
            return false;
        }

        return event.getX() < getWidth() - getPaddingRight()
                && event.getX() > getWidth() - getPaddingRight() - dRight.getIntrinsicWidth() - getCompoundDrawablePadding()
                && event.getY() > 0
                && event.getY() < getHeight();
    }

    /**
     * 触摸事件位于drawableLeft上
     *
     * @param event
     * @return
     */
    private boolean hasDrawableLeftRange(MotionEvent event) {

        if (onDrawableClickListener == null) {
            return false;
        }

        Drawable dLeft = getCompoundDrawables()[0];
        if (dLeft == null) {
            return false;
        }

        return event.getX() >= getPaddingLeft()
                && event.getX() < getPaddingLeft() + dLeft.getIntrinsicWidth() + getCompoundDrawablePadding()
                && event.getY() > 0
                && event.getY() < getHeight();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createFixedSizeDrawable();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawableToCenter(canvas);

        super.onDraw(canvas);

        // 绘制四周线条
        drawLines(canvas);
        // 绘制Tag
        drawTagText(canvas);
        // 绘制Badge
        drawBadge(canvas);

    }

    /**
     * 带Drawable的内容是否居中显示
     *
     * @param canvas
     */
    private void drawableToCenter(Canvas canvas) {

        if (!mDrawableCenter) {
            return;
        }

        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawable;
            if ((drawable = drawables[0]) != null) { // drawableLeft
                // 设置文本垂直对齐
                setGravity(Gravity.CENTER_VERTICAL);
                // 左边的 Drawable 不为空时，计算需要绘制的内容的宽度
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawable.getIntrinsicWidth();
                // 计算总内容的宽度
                float bodyWidth = textWidth + drawablePadding + drawableWidth;

                // 移动画布
                canvas.translate((getWidth() - bodyWidth) / 2 - getPaddingLeft(), 0);

            } else if ((drawable = drawables[1]) != null) { // drawableRight
                // 设置文本水平对齐
                setGravity(Gravity.CENTER_HORIZONTAL);
                // 上面的drawable 不为空时，计算需要绘制的内容的高度
                Rect rect = new Rect();
                getPaint().getTextBounds(getText().toString(), 0, getText().toString().length(), rect);
                int textHeight = rect.height();
                int drawablePadding = getCompoundDrawablePadding();
                int drawableHeight = drawable.getIntrinsicHeight();
                // 计算总内容的高度
                float bodyHeight = textHeight + drawablePadding + drawableHeight;
                canvas.translate(0, (getHeight() - bodyHeight) / 2 - getPaddingTop());
            }
        }
    }

    /**
     * 绘制指定大小的Drawable
     */
    private void createFixedSizeDrawable() {

        Drawable[] drawables = getCompoundDrawables();

        Bitmap bitmap = null;
        Drawable drawable = null;

        if (drawables[0] != null
                && (drawables[0] instanceof BitmapDrawable)
                && mDrawableDirection == DIRECTION_LEFT
                && mDrawableWidth != 0
                && mDrawableHeight != 0) {

            bitmap = ((BitmapDrawable) drawables[0]).getBitmap();
            drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, mDrawableWidth, mDrawableHeight));
            setCompoundDrawablesWithIntrinsicBounds(drawable, drawables[1], drawables[2], drawables[3]);
        }

        if (drawables[1] != null
                && (drawables[1] instanceof BitmapDrawable)
                && mDrawableDirection == DIRECTION_TOP
                && mDrawableWidth != 0
                && mDrawableHeight != 0) {

            bitmap = ((BitmapDrawable) drawables[1]).getBitmap();
            drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, mDrawableWidth, mDrawableHeight));
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawable, drawables[2], drawables[3]);
        }

        if (drawables[2] != null
                && (drawables[2] instanceof BitmapDrawable)
                && mDrawableDirection == DIRECTION_RIGHT
                && mDrawableWidth != 0
                && mDrawableHeight != 0) {

            bitmap = ((BitmapDrawable) drawables[2]).getBitmap();
            drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, mDrawableWidth, mDrawableHeight));
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
        }

        if (drawables[3] != null
                && (drawables[3] instanceof BitmapDrawable)
                && mDrawableDirection == DIRECTION_BOTTOM
                && mDrawableWidth != 0
                && mDrawableHeight != 0) {

            bitmap = ((BitmapDrawable) drawables[3]).getBitmap();
            drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, mDrawableWidth, mDrawableHeight));
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawable);
        }

        drawable = null;
        bitmap = null;

    }

    /**
     * 缩放图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public Bitmap getBitmap(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    /**
     * 绘制徽章
     *
     * @param canvas
     */
    private void drawBadge(Canvas canvas) {

        if (mBadgeText <= 0) {
            return;
        }

        int cx = 0;
        int cy = 0;

        mBadgePaint.setColor(mBadgeBgColor);

        if (mBadgeLocal == LOCAL_TEXT) {
            if (TextUtils.isEmpty(getText())) {
                return;
            }

            if ((getGravity() & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == Gravity.RIGHT) {    // 设置了右对齐

                cx = getPaddingRight() - mBadgeRadius;

                Drawable dRight = getCompoundDrawables()[2];
                if (dRight != null) {
                    cx += dRight.getIntrinsicWidth() + getCompoundDrawablePadding();
                }

                cx = getMeasuredWidth() - cx;

            } else {    // 左对齐
                cx = getPaddingLeft() + getTextBound(getText().toString(), getPaint()).width() + mBadgeRadius;
                Drawable dLeft = getCompoundDrawables()[0];
                if (dLeft != null) {
                    cx += dLeft.getIntrinsicWidth() + getCompoundDrawablePadding();
                }
                if (getTagText().length() > 0) {
                    String tempTag = useTagSeparator ? mTagText + mTagSeparator : mTagText;
                    cx += getTextBound(tempTag, mTagPaint).width() + getTagPadding();
                }
            }

            cy = getBaseline() - getLineHeight() * 2 / 3;

        } else if (mBadgeLocal == LOCAL_TAG) {
            if (getTagText().length() < 1) {
                return;
            }

            String tempTag = useTagSeparator ? mTagText + mTagSeparator : mTagText;

            // tag位于左侧时的cx
            cx = getPaddingLeft() + getTextBound(tempTag, mTagPaint).width() + mBadgeRadius;
            Drawable dLeft = getCompoundDrawables()[0];
            if (dLeft != null) {
                cx += dLeft.getIntrinsicWidth() + getCompoundDrawablePadding();
            }

            if (mTagGravity == TAG_GRAVITY_LEFT_TOP) {  // 左上角
                cy = getBaseline() - getLineHeight() * 2 / 3;
            } else if (mTagGravity == TAG_GRAVITY_LEFT_VERTICAL) {  // 左侧垂直居中
                cy = (int) (getBaseline() + ((getLineCount() - 1) / 2f) * getLineHeight()) - getLineHeight() * 2 / 3;
            } else if (mTagGravity == TAG_GRAVITY_LEFT_BOTTOM) {  // 左右底部
                cy = getBaseline() + (getLineCount() - 1) * getLineHeight() - getLineHeight() * 2 / 3;
            } else { // 顶部水平居中等不绘制
                return;
            }

        } else if (mBadgeLocal == LOCAL_DRAWABLE) {

            Drawable[] drawables = getCompoundDrawables();
            if (drawables[0] == null && drawables[1] == null) { // 只绘制左侧和顶部
                return;
            }

            if (drawables[0] != null) {
                int w = mDrawableWidth != 0 ? mDrawableWidth : drawables[0].getIntrinsicWidth();
                int h = mDrawableHeight != 0 ? mDrawableHeight : drawables[0].getIntrinsicHeight();
                cx = getPaddingLeft() + w;
                cy = getPaddingTop() + (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / 2 - h / 2;
            } else if (drawables[1] != null) {
                int w = mDrawableWidth != 0 ? mDrawableWidth : drawables[1].getIntrinsicWidth();
                int h = mDrawableHeight != 0 ? mDrawableHeight : drawables[1].getIntrinsicHeight();
                cx = getCompoundPaddingLeft() + (getMeasuredWidth() - getCompoundPaddingLeft() - getPaddingRight()) / 2 + w / 2;
                cy = getPaddingTop();
            } else {
                // 右侧/底部的不绘制
                return;
            }

        }

        // 增加 X、Y的偏移量
        cx += mBadgeOffsetX + getScrollX();
        cy += mBadgeOffsetY = getScrollY();

        canvas.drawCircle(cx, cy, mBadgeRadius, mBadgePaint);

        /**
         * 绘制文本
         */
        if (mBadgeStyle == BADGE_NUMBER) {
            mBadgePaint.setColor(mBadgeTextColor);
            String tempBadge = mBadgeText + "";
            int baseLine = cy + getPaintBaseLine(mBadgePaint);

            canvas.drawText(tempBadge, cx, baseLine, mBadgePaint);
        }

    }

    /**
     * 获取Paint基线
     *
     * @param paint
     * @return
     */
    private int getPaintBaseLine(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return (int) Math.abs(metrics.descent + metrics.ascent) / 2;
    }

    /**
     * 绘制Tag
     *
     * @param canvas
     */
    private void drawTagText(Canvas canvas) {

        if (TextUtils.isEmpty(mTagText)) {
            return;
        }

        String tempTag = useTagSeparator ? mTagText + mTagSeparator : mTagText;

        if (mTagGravity == TAG_GRAVITY_LEFT_TOP
                || mTagGravity == TAG_GRAVITY_LEFT_VERTICAL
                || mTagGravity == TAG_GRAVITY_LEFT_BOTTOM) {
            mTagPaint.setTextAlign(Paint.Align.LEFT);
            // 左侧
            Drawable dLeft = getCompoundDrawables()[0];
            int startX = getPaddingLeft();
            if (dLeft != null) {
                startX += dLeft.getIntrinsicWidth() + getCompoundDrawablePadding();
            }

            int baseLine = 0;

            if (mTagGravity == TAG_GRAVITY_LEFT_TOP || getLineCount() == 1) {
                baseLine = getBaseline();
            } else if (mTagGravity == TAG_GRAVITY_LEFT_VERTICAL) {
                baseLine = (int) (getBaseline() + ((getLineCount() - 1) / 2f) * getLineHeight());
            } else if (mTagGravity == TAG_GRAVITY_LEFT_BOTTOM) {
                baseLine = getBaseline() + (getLineCount() - 1) * getLineHeight();
            }

            canvas.drawText(tempTag, startX + getScrollX(), baseLine, mTagPaint);

        } else if (mTagGravity == TAG_GRAVITY_TOP_HORIZONTAL) {
            // 顶部
            mTagPaint.setTextAlign(Paint.Align.CENTER);

            int startX = 0;

            int w = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

            Drawable dRight = getCompoundDrawables()[2];
            if (dRight != null) {
                w = w - dRight.getIntrinsicWidth() - getCompoundDrawablePadding();
            }

            Drawable dLeft = getCompoundDrawables()[0];
            if (dLeft != null) {
                w = w - dLeft.getIntrinsicWidth() - getCompoundDrawablePadding();
                startX = getPaddingLeft() + dLeft.getIntrinsicWidth() + getCompoundDrawablePadding() + w / 2;
            } else {
                startX = getPaddingLeft() + w / 2;
            }

            int baseLine = getPaddingTop();
            Drawable dTop = getCompoundDrawables()[1];
            if (dTop != null) {
                baseLine += dTop.getIntrinsicHeight() + getCompoundDrawablePadding();
            }

            baseLine += getTextBound(tempTag, mTagPaint).height();
            canvas.drawText(tempTag, startX + getScrollX(), baseLine, mTagPaint);
        }

    }

    // 增加顶部Tag的间距
    @Override
    public int getExtendedPaddingTop() {
        if (!TextUtils.isEmpty(mTagText) && mTagGravity == TAG_GRAVITY_TOP_HORIZONTAL) {

            String tempTag = useTagSeparator ? mTagText + mTagSeparator : mTagText;
            mTagTextLength = getTextBound(tempTag, mTagPaint).height() + mTagPadding;

        } else {
            mTagTextLength = 0;
        }


        return super.getExtendedPaddingTop() + mTagTextLength;
    }


    // 增加左侧Tag的间距
    @Override
    public int getCompoundPaddingLeft() {
        if (!TextUtils.isEmpty(mTagText)
                && (mTagGravity == TAG_GRAVITY_LEFT_TOP
                || mTagGravity == TAG_GRAVITY_LEFT_VERTICAL
                || mTagGravity == TAG_GRAVITY_LEFT_BOTTOM)) {

            String tempTag = useTagSeparator ? mTagText + mTagSeparator : mTagText;
            mTagTextLength = getTextBound(tempTag, mTagPaint).width() + mTagPadding;

        } else {
            mTagTextLength = 0;
        }

        return super.getCompoundPaddingLeft() + mTagTextLength;
    }


    /**
     * 绘制四周的线条
     */
    private void drawLines(Canvas canvas) {

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (mBottomLineSize > 0) {
            mLineRect.left = mBottomLineMarginLeft + getScrollX();
            mLineRect.right = width - mBottomLineMarginRight + getScrollX();
            mLineRect.top = height - mBottomLineSize + getScrollY();
            mLineRect.bottom = height + getScrollY();
            canvas.drawRect(mLineRect, mLinePaint);
        }
        if (mTopLineSize > 0) {
            mLineRect.left = mTopLineMarginLeft + getScrollX();
            mLineRect.right = width - mTopLineMarginRight + getScrollX();
            mLineRect.top = 0 + getScrollY();
            mLineRect.bottom = mTopLineSize + getScrollY();
            canvas.drawRect(mLineRect, mLinePaint);
        }
        if (mLeftLineSize > 0) {
            mLineRect.left = 0 + getScrollX();
            mLineRect.right = mLeftLineSize + getScrollX();
            mLineRect.top = mLeftLineMarginTop + getScrollY();
            mLineRect.bottom = height - mLeftLineMarginBottom + getScrollY();
            canvas.drawRect(mLineRect, mLinePaint);
        }
        if (mRightLineSize > 0) {
            mLineRect.left = width - mRightLineSize + getScrollX();
            mLineRect.right = width + getScrollX();
            mLineRect.top = mRightLineMarginTop + getScrollY();
            mLineRect.bottom = height - mRightLineMarginBottom + getScrollY();
            canvas.drawRect(mLineRect, mLinePaint);
        }

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

    /**
     * 获取字体的边框
     *
     * @param text
     * @param paint
     * @return
     */
    public Rect getTextBound(String text, Paint paint) {
        Rect rect = new Rect();
        if (!TextUtils.isEmpty(text)) {
            paint.getTextBounds(text, 0, text.length(), rect);
        }
        return rect;
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        requestLayout();
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
    public void setCorner(float corner) {
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
    public void setCorner(float leftTop, float rightTop, float rightBottom, float leftBottom) {
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

    /**
     * 设置tag
     *
     * @param tagText
     */
    public void setTagText(CharSequence tagText) {
        if (TextUtils.isEmpty(tagText)) {
            tagText = "";
        }
        this.mTagText = tagText.toString();
        invalidate();
    }

    public String getTagText() {
        if (TextUtils.isEmpty(mTagText)) {
            mTagText = "";
        }
        return this.mTagText;
    }

    /**
     * 设置Tag文本的大小
     *
     * @param tagTextSize
     */
    public void setTagTextSize(int tagTextSize) {
        if (tagTextSize < 0) {
            tagTextSize = 0;
        }
        this.mTagTextSize = tagTextSize;
        mTagPaint.setTextSize(mTagTextSize);
        invalidate();
    }

    public int getTagTextSize() {
        return this.mTagTextSize;
    }

    /**
     * 设置Tag文本的颜色
     *
     * @param tagTextColor
     */
    public void setTagTextColor(int tagTextColor) {
        this.mTagTextColor = tagTextColor;
        invalidate();
    }

    public int getTagTextColor() {
        return this.mTagTextColor;
    }

    /**
     * 设置Tag的分隔符
     *
     * @param tagSeparator
     */
    public void setTagSeparator(CharSequence tagSeparator) {
        if (TextUtils.isEmpty(tagSeparator)) {
            tagSeparator = ":";
        }
        this.mTagSeparator = tagSeparator.toString();
        invalidate();
    }

    public String getTagSeparator() {
        return this.mTagSeparator;
    }

    /**
     * 是否使用Tag分隔符
     *
     * @param useTagSeparator
     */
    public void setUseTagSeparator(boolean useTagSeparator) {
        this.useTagSeparator = useTagSeparator;
        invalidate();
    }

    /**
     * 设置Tag与文本之间的间距
     *
     * @param tagPadding
     */
    public void setTagPadding(int tagPadding) {
        this.mTagPadding = tagPadding;
        invalidate();
    }

    public int getTagPadding() {
        return this.mTagPadding;
    }

    /**
     * 设置Tag的位置
     *
     * @param gravity
     */
    public void setTagGravity(TagGravity gravity) {
        this.mTagGravity = gravity.getGravity();
        invalidate();
    }

    /**
     * 设置Badge
     *
     * @param count
     */
    public void setBadgeText(int count) {
        this.mBadgeText = count;
        invalidate();
    }

    public int getBadgeText() {
        return mBadgeText;
    }

    public void setBadgeTextColor(int badgeTextColor) {
        this.mBadgeTextColor = badgeTextColor;
        invalidate();
    }

    public int getBadgeTextColor() {
        return mBadgeTextColor;
    }

    public void setBadgeTextSize(int badgeTextSize) {
        this.mBadgeTextSize = badgeTextSize;
        mBadgePaint.setTextSize(mBadgeTextSize);
        invalidate();
    }

    public int getBadgeTextSize() {
        return this.mBadgeTextSize;
    }

    public void setBadgeOffsetX(int offsetX) {
        this.mBadgeOffsetX = offsetX;
        invalidate();
    }

    public int getBadgeOffsetX() {
        return mBadgeOffsetX;
    }

    public void setBadgeOffsetY(int offsetY) {
        this.mBadgeOffsetY = offsetY;
        invalidate();
    }

    public int getBadgeOffsetY() {
        return mBadgeOffsetY;
    }

    public void setBadgeRadius(int radius) {
        this.mBadgeRadius = radius;
        invalidate();
    }

    public int getBadgeRadius() {
        return this.mBadgeRadius;
    }

    public void setBadgeBgColor(int badgeBgColor) {
        this.mBadgeBgColor = badgeBgColor;
        invalidate();
    }

    public int getBadgeBgColor() {
        return this.mBadgeBgColor;
    }

    public void setBadgeLocal(BadgeLocal local) {
        this.mBadgeLocal = local.getLocal();
        invalidate();
    }

    public void setBadgeStyle(BadgeStyle style) {
        this.mBadgeStyle = style.getStyle();
        invalidate();
    }

    public void setDrawableSize(int drawableWidth, int drawableHeight) {
        this.mDrawableWidth = drawableWidth;
        this.mDrawableHeight = drawableHeight;
        requestLayout();
    }

    public void setDrawableDirection(DrawableDirection drawableDirection) {
        this.mDrawableDirection = drawableDirection.getDirection();
        requestLayout();
    }

    // endregion -----------------------------------


    // region ------------ enum ---------------

    /**
     * Tag显示的位置
     */
    public enum TagGravity {

        LEFT_TOP(XTextView.TAG_GRAVITY_LEFT_TOP),
        TOP_HORIZONTAL(XTextView.TAG_GRAVITY_TOP_HORIZONTAL),
        LEFT_VERTICAL(XTextView.TAG_GRAVITY_LEFT_VERTICAL),
        LEFT_BOTTOM(XTextView.TAG_GRAVITY_LEFT_BOTTOM);

        int gravity;

        TagGravity(int gravity) {
            this.gravity = gravity;
        }

        public int getGravity() {
            return this.gravity;
        }

    }


    /**
     * 徽章绘制的位置
     */
    public enum BadgeLocal {
        LEFT(LOCAL_TEXT),
        TAG(LOCAL_TAG),
        DRAWABLE(LOCAL_DRAWABLE);

        int local;

        BadgeLocal(int local) {
            this.local = local;
        }

        public int getLocal() {
            return local;
        }
    }

    /**
     * 徽章绘制的风格
     */
    public enum BadgeStyle {
        NUMBER(BADGE_NUMBER),
        DOT(BADGE_DOT);

        int style;

        BadgeStyle(int style) {
            this.style = style;
        }

        public int getStyle() {
            return style;
        }
    }

    /**
     * Drawable指定方向固定大小
     */
    public enum DrawableDirection {
        LEFT(DIRECTION_LEFT),
        TOP(DIRECTION_TOP),
        RIGHT(DIRECTION_RIGHT),
        BOTTOM(DIRECTION_BOTTOM);

        int direction;

        DrawableDirection(int direction) {
            this.direction = direction;
        }

        public int getDirection() {
            return direction;
        }
    }

    // endregion -----------------------

    // region ----------- 回调函数 ----------------

    private OnDrawableClickListener onDrawableClickListener;

    public void setOnDrawableClickListener(OnDrawableClickListener onDrawableClickListener) {
        this.onDrawableClickListener = onDrawableClickListener;
    }

    // endregion ---------------------------------

}
