package com.hwy.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

/**
 * 作者: hewenyu
 * 日期: 2018/11/29 23:17
 * 说明: RadioButton(目前只支持drawableTop时右上角显示数字)
 */
public class XRadioButton extends RadioButton {

    private static final int BADGE_NUMBER = 0;

    private static final int BADGE_DOT = 1;

    // region -------- badge ---------

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
     * Drawable是否居中显示
     */
    private boolean mDrawableCenter = true;

    // endregion ---------------------

    private int mWidth, mHeight;

    /**
     * 显示的最大数量
     */
    private int mBadgeMaxValue = 100;

    /**
     * 显示的数量溢出时显示的内容
     */
    private String mOverflowBadge = "99+";

    /**
     * 徽章显示的风格
     */
    private int mBadgeStyle = BADGE_NUMBER;

    public XRadioButton(Context context) {
        this(context, null);
    }

    public XRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XRadioButton);
        mBadgeStyle = array.getInt(R.styleable.XRadioButton_tvBadgeStyle, BADGE_NUMBER);
        mBadgeText = array.getInt(R.styleable.XRadioButton_tvBadgeText, 0);
        mBadgeTextSize = array.getDimensionPixelSize(R.styleable.XRadioButton_tvBadgeTextSize, 0);
        mBadgeTextColor = array.getColor(R.styleable.XRadioButton_tvBadgeTextColor, Color.WHITE);
        mBadgeBgColor = array.getColor(R.styleable.XRadioButton_tvBadgeBgColor, Color.RED);
        mBadgeRadius = array.getDimensionPixelSize(R.styleable.XRadioButton_tvBadgeRadius, 0);
        mBadgeOffsetX = array.getDimensionPixelSize(R.styleable.XRadioButton_tvBadgeOffsetX, 0);
        mBadgeOffsetY = array.getDimensionPixelSize(R.styleable.XRadioButton_tvBadgeOffsetY, 0);
        mDrawableCenter = array.getBoolean(R.styleable.XRadioButton_tvDrawableCenter, true);
        mBadgeMaxValue = array.getInt(R.styleable.XRadioButton_tvBadgeMaxValue, 100);
        mOverflowBadge = array.getString(R.styleable.XRadioButton_tvBadgeOverflow);
        if (TextUtils.isEmpty(mOverflowBadge)) {
            mOverflowBadge = "99+";
        }
        array.recycle();

        init();
    }

    private void init() {
        mBadgePaint = new Paint();
        mBadgePaint.setDither(true);
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);
        mBadgePaint.setTextAlign(Paint.Align.CENTER);
        mBadgePaint.setTextSize(mBadgeTextSize);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawableToCenter(canvas);
        super.onDraw(canvas);

        drawBadge(canvas);

    }

    /**
     * 绘制徽章
     *
     * @param canvas
     */
    private void drawBadge(Canvas canvas) {

        if (mBadgeText < 1) {
            return;
        }

        Drawable[] drawables = getCompoundDrawables();
        Drawable dLeft = drawables[0];
        Drawable dTop = drawables[1];
        if (dTop == null && dLeft == null) {
            return;
        }

        int cx = 0;
        int cy = 0;

        if (dLeft != null) {
            cx = getPaddingLeft() + dLeft.getIntrinsicWidth();
            cy = getPaddingTop() + (mHeight - getPaddingTop() - getPaddingBottom()) / 2 - dLeft.getIntrinsicHeight() / 2;
        } else if (dTop != null) {
            cx = mWidth / 2 + dTop.getIntrinsicWidth() / 2;
            cy = getPaddingTop();
        }

        // 增加 X、Y的偏移量
        cx += mBadgeOffsetX + getScrollX();
        cy += mBadgeOffsetY + getScrollY();

        mBadgePaint.setColor(mBadgeBgColor);
        canvas.drawCircle(cx, cy, mBadgeRadius, mBadgePaint);

        // 绘制数字内容
        if (mBadgeStyle == BADGE_NUMBER) {
            String temp = mBadgeText < mBadgeMaxValue ? String.valueOf(mBadgeText) : mOverflowBadge;
            mBadgePaint.setColor(mBadgeTextColor);
            canvas.drawText(temp, cx, cy + getPaintBaseLine(mBadgePaint), mBadgePaint);
        }

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
     * 设置徽章显示的风格
     *
     * @param badgeStyle
     */
    public void setBadgeStyle(BadgeStyle badgeStyle) {
        mBadgeStyle = badgeStyle.getStyle();
        init();
    }

    /**
     * 设置徽章文本内容
     *
     * @param number
     */
    public void setBadgeNumber(int number) {
        this.mBadgeText = number;
        invalidate();
    }

    public int getBadgeNumber() {
        return this.mBadgeText;
    }

    public void setBadgeTextSize(int size) {
        this.mBadgeTextSize = size;
        mBadgePaint.setTextSize(size);
        invalidate();
    }

    public void setBadgeTextColor(int color) {
        this.mBadgeTextColor = color;
        invalidate();
    }

    public void setBadgeOverflowText(String overflowText) {
        if (TextUtils.isEmpty(overflowText)) {
            overflowText = "99+";
        }
        this.mOverflowBadge = overflowText;
        invalidate();
    }

    public String getOverflowBadge() {
        return mOverflowBadge;
    }

    public void setBadgeMaxValue(int maxValue) {
        this.mBadgeMaxValue = maxValue;
        invalidate();
    }

    public int getBadgeMaxValue() {
        return this.mBadgeMaxValue;
    }

    public void setDrawableCenter(boolean isCenter) {
        this.mDrawableCenter = isCenter;
        invalidate();
    }

    public void setBadgeBgColor(int color) {
        this.mBadgeBgColor = color;
        invalidate();
    }

    public void setBadgeRadius(int radius) {
        this.mBadgeRadius = radius;
        invalidate();
    }

    public void setBadgeOffsetX(int offsetX) {
        this.mBadgeOffsetX = offsetX;
        invalidate();
    }

    public int getBadgeOffsetX() {
        return this.mBadgeOffsetX;
    }

    public void setBadgeOffsetY(int offsetY) {
        this.mBadgeOffsetY = offsetY;
        invalidate();
    }

    public int getBadgeOffsetY() {
        return this.mBadgeOffsetY;
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

}
