package com.hwy.textview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.hwy.textview.listener.OnDrawableClickListener;

/**
 * 作者: hewenyu
 * 日期: 2018/10/26 20:02
 * 说明: EditText 封装
 */
public class XEditText extends EditText {

    // region ---------- 常量 --------------

    /**
     * 无效的Corner
     */
    private static final int INVALID_CORNER_VALUE = -1;

    // endregion ---------------------------

    // region ---------- 背景 --------------

    /**
     * 背景
     */
    private GradientDrawable mBackgroundDrawable;

    /**
     * 圆角半径
     */
    private float mCorner = 0;
    private float mCornerLeftTop = INVALID_CORNER_VALUE;
    private float mCornerRightTop = INVALID_CORNER_VALUE;
    private float mCornerRightBottom = INVALID_CORNER_VALUE;
    private float mCornerLeftBottom = INVALID_CORNER_VALUE;

    // 四个圆角的半径,左上角开始顺时针方向，每两个值标示一个圆角
    private float[] mCornerRadii = new float[8];

    /**
     * 边框大小
     */
    private int mBorderSize;
    /**
     * 边框颜色
     */
    private int mBorderColor;

    /**
     * 底部线条的大小
     */
    private int mBottomLineSize;
    /**
     * 底部线条的颜色
     */
    private int mLineColor;
    /**
     * 底部线条与左侧的间距
     */
    private int mBottomLineMarginLeft;
    /**
     * 底部线条与右侧的间距
     */
    private int mBottomLineMarginRight;

    private Rect mLineRect = new Rect();

    private Paint mLinePaint;

    // endregion ---------------------------


    // region ---------- 标签 --------------

    private Paint mTagPaint;

    /**
     * 标签文本
     */
    private String mTagText = "";
    /**
     * 标签文本大小
     */
    private int mTagTextSize = 0;
    /**
     * 标签文本颜色
     */
    private int mTagTextColor = Color.GRAY;
    /**
     * 类似drawablePadding
     */
    private int mTagPadding = 0;

    // 分割符，附加在tag后面
    private boolean useTagSeparator = false;
    /**
     * 默认分隔符
     */
    private String mTagSeparator = ":";

    /**
     * 分隔线的宽度
     */
    private int mSeparatorLineWidth;
    /**
     * 分隔线的高度
     */
    private int mSeparatorLineHeight;
    /**
     * 分隔线的颜色
     */
    private int mSeparatorLineColor;
    /**
     * 类似drawablePadding
     */
    private int mSeparatorLinePadding;

    private Paint mSeparatorPaint;

    // endregion ---------------------------


    // region ---------- 清除 --------------

    /**
     * 是否启用清除功能
     */
    private boolean useCross;

    /**
     * 清除按钮圆形背景色
     */
    private int mCrossBgColor;

    /**
     * 清除按钮圆形背景半径
     */
    private int mCrossRadius;

    /**
     * 清除按钮交叉的颜色
     */
    private int mCrossColor;

    /**
     * 清除按钮交叉的长度(相对于半径)
     */
    private int mCrossLength;

    /**
     * 清除按钮交叉的宽度
     */
    private int mCrossSize;

    /**
     * 类似drawablePadding
     */
    private int mCrossPadding;

    /**
     * 抖动的动画
     */
    private Animation translateAnimation;

    private Paint mCrossPaint;

    /**
     * 是否允许清除
     */
    private boolean arrowClear;

    // endregion ---------------------------

    // region ---------- drawable ----------

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

    private int mCurrentTouch = OnDrawableClickListener.INVALID_TOUCH;

    // endregion ---------------------------

    public XEditText(Context context) {
        this(context, null);
    }

    public XEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public XEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XEditText);
        mCorner = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvCorner, 0);
        mCornerLeftTop = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvCornerLeftTop, INVALID_CORNER_VALUE);
        mCornerRightTop = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvCornerRightTop, INVALID_CORNER_VALUE);
        mCornerRightBottom = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvCornerRightBottom, INVALID_CORNER_VALUE);
        mCornerLeftBottom = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvCornerLeftBottom, INVALID_CORNER_VALUE);

        mBorderSize = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvBorderSize, 0);
        mBorderColor = typedArray.getColor(R.styleable.XEditText_tvBorderColor, Color.TRANSPARENT);

        mLineColor = typedArray.getColor(R.styleable.XEditText_tvLineColor, Color.TRANSPARENT);
        mBottomLineSize = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvBottomLineSize, 0);
        mBottomLineMarginLeft = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvBottomLineMarginLeft, 0);
        mBottomLineMarginRight = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvBottomLineMarginRight, 0);

        mTagText = typedArray.getString(R.styleable.XEditText_tvTagText);
        if (TextUtils.isEmpty(mTagText)) {
            mTagText = "";
        }
        mTagTextColor = typedArray.getColor(R.styleable.XEditText_tvTagTextColor, Color.GRAY);
        mTagTextSize = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvTagTextSize, 0);
        mTagSeparator = typedArray.getString(R.styleable.XEditText_tvTagSeparator);
        if (TextUtils.isEmpty(mTagSeparator)) {
            mTagSeparator = ":";
        }
        useTagSeparator = typedArray.getBoolean(R.styleable.XEditText_tvUseTagSeparator, false);
        mTagPadding = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvTagPadding, 0);

        mSeparatorLineWidth = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvSeparatorWidth, 0);
        mSeparatorLineHeight = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvSeparatorHeight, 0);
        mSeparatorLineColor = typedArray.getColor(R.styleable.XEditText_tvSeparatorColor, Color.TRANSPARENT);
        mSeparatorLinePadding = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvSeparatorPadding, 0);

        useCross = typedArray.getBoolean(R.styleable.XEditText_tvUseCross, false);
        mCrossBgColor = typedArray.getColor(R.styleable.XEditText_tvCrossBgColor, Color.GRAY);
        mCrossRadius = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvCrossRadius, 10);
        mCrossColor = typedArray.getColor(R.styleable.XEditText_tvCrossColor, Color.WHITE);
        mCrossLength = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvCrossLength, 6);
        mCrossSize = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvCrossSize, 2);
        mCrossPadding = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvCrossPadding, 0);

        mDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvDrawableHeight, 0);
        mDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.XEditText_tvDrawableWidth, 0);
        mDrawableDirection = typedArray.getInt(R.styleable.XEditText_tvDrawableDirection, DIRECTION_LEFT);

        typedArray.recycle();

        mLinePaint = new Paint();
        mLinePaint.setDither(true);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.FILL);

        mTagPaint = new Paint();
        mTagPaint.setColor(mTagTextColor);
        mTagPaint.setTextSize(mTagTextSize);
        mTagPaint.setStyle(Paint.Style.FILL);
        mTagPaint.setDither(true);
        mTagPaint.setAntiAlias(true);

        mSeparatorPaint = new Paint();
        mSeparatorPaint.setAntiAlias(true);
        mSeparatorPaint.setDither(true);
        mSeparatorPaint.setStyle(Paint.Style.FILL);
        mSeparatorPaint.setColor(mSeparatorLineColor);

        mCrossPaint = new Paint();
        mCrossPaint.setDither(true);
        mCrossPaint.setAntiAlias(true);
        mCrossPaint.setStyle(Paint.Style.FILL);

        updateBackground();

        if (useCross) {
            setSingleLine(true);
        }

        updateInputType();

    }

    @Override
    public void setInputType(int type) {
        if (type == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            // 解决显示密码时，字体不一致的问题
            setTypeface(Typeface.DEFAULT);
        }
        super.setInputType(type);
    }

    /**
     * 更新密码显示
     */
    private void updateInputType() {
        if (isPassword()) {
            // 解决密码显示明文的问题
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            // 解决显示密码时，字体不一致的问题
            setTypeface(Typeface.DEFAULT);
        } else {
            setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    /**
     * 判断是否是密码
     *
     * @return
     */
    private boolean isPassword() {
        final int variation =
                getInputType() & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        if (variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)) {
            // textPassword
            return true;
        }
        if (variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)) {
            return true;
        }
        if (variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)) {
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void updateBackground() {

        if (mBackgroundDrawable == null) {
            mBackgroundDrawable = new GradientDrawable();
            mBackgroundDrawable.setShape(GradientDrawable.RECTANGLE);
            Drawable drawable = getBackground();
            if (drawable instanceof ColorDrawable) {
                mBackgroundDrawable.setColor(((ColorDrawable) drawable).getColor());
            } else {
                mBackgroundDrawable.setColor(Color.TRANSPARENT);
            }
            setBackground(mBackgroundDrawable);
        }

        setCorner();
        setBorder();

    }

    /**
     * 设置边框
     */
    private void setBorder() {
        if (mBorderSize <= 0) {
            return;
        }

        mBackgroundDrawable.setStroke(mBorderSize, mBorderColor);
    }

    /**
     * 设置圆角
     */
    private void setCorner() {
        if (mCorner < 0) {
            mCorner = 0;
        }

        // 左上角
        mCornerRadii[0] = mCornerLeftTop > INVALID_CORNER_VALUE ? mCornerLeftTop : mCorner;
        mCornerRadii[1] = mCornerLeftTop > INVALID_CORNER_VALUE ? mCornerLeftTop : mCorner;
        // 右上角
        mCornerRadii[2] = mCornerRightTop > INVALID_CORNER_VALUE ? mCornerRightTop : mCorner;
        mCornerRadii[3] = mCornerRightTop > INVALID_CORNER_VALUE ? mCornerRightTop : mCorner;
        // 右下角
        mCornerRadii[4] = mCornerRightBottom > INVALID_CORNER_VALUE ? mCornerRightBottom : mCorner;
        mCornerRadii[5] = mCornerRightBottom > INVALID_CORNER_VALUE ? mCornerRightBottom : mCorner;
        // 左下角
        mCornerRadii[6] = mCornerLeftBottom > INVALID_CORNER_VALUE ? mCornerLeftBottom : mCorner;
        mCornerRadii[7] = mCornerLeftBottom > INVALID_CORNER_VALUE ? mCornerLeftBottom : mCorner;

        mBackgroundDrawable.setCornerRadii(mCornerRadii);
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createFixedSizeDrawable();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                arrowClear = false;
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

                // tag 部分的区域触摸不做反馈
                if (event.getX() < getCompoundPaddingLeft()) {
                    return false;
                }

                // 处理清除按钮的触摸事件
                if (useCross    // 允许显示清除按钮
                        && hasFocus()   // 获得焦点后
                        && hasCrossRange(event)) {  // down事件的位置在清除按钮的范围内
                    arrowClear = true;
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:

                if (arrowClear) {
                    if (hasCrossRange(event)) {
                        setText("");
                        return true;
                    }
                }

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

    /**
     * x轴方向是否满足清除
     *
     * @param event
     * @return
     */
    private boolean hasCrossRange(MotionEvent event) {
        return event.getX() >= getWidth() - getCompoundPaddingRight()
                && event.getX() < getWidth() - getCompoundPaddingRight() + getCrossValidSize()
                && event.getY() >= 0
                && event.getY() <= getHeight();
    }

    /**
     * 获取清除按钮的长度
     *
     * @return
     */
    private int getCrossValidSize() {
        return mCrossPadding + 2 * mCrossRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLines(canvas);
        drawTagText(canvas);
        drawSeparator(canvas);
        drawCross(canvas);

    }

    /**
     * 绘制清除按钮
     *
     * @param canvas
     */
    private void drawCross(Canvas canvas) {
        if (!hasFocus() || !useCross || getText().toString().length() <= 0) {
            return;
        }

        int cx = getWidth() - getCompoundPaddingRight() + mCrossPadding + mCrossRadius + getScrollX();
        int cy = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;

        mCrossPaint.setColor(mCrossBgColor);
        canvas.drawCircle(cx, cy, mCrossRadius, mCrossPaint);

        mCrossPaint.setColor(mCrossColor);
        canvas.save();
        canvas.rotate(45, cx, cy);
        canvas.drawRect(cx - mCrossSize / 2, cy - mCrossLength, cx + mCrossSize / 2, cy + mCrossLength, mCrossPaint);
        canvas.rotate(-90, cx, cy);
        canvas.drawRect(cx - mCrossSize / 2, cy - mCrossLength, cx + mCrossSize / 2, cy + mCrossLength, mCrossPaint);
        canvas.restore();

    }

    /**
     * 绘制分隔线
     *
     * @param canvas
     */
    private void drawSeparator(Canvas canvas) {
        if (mSeparatorLineWidth <= 0 || mSeparatorLineHeight <= 0) {
            return;
        }

        int separatorLength = mSeparatorLineWidth + mSeparatorLinePadding;

        int left = getCompoundPaddingLeft() - separatorLength + getScrollX();
        int top = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2 - mSeparatorLineHeight / 2;
        int right = left + mSeparatorLineWidth;
        int bottom = top + mSeparatorLineHeight;

        canvas.drawRect(left, top, right, bottom, mSeparatorPaint);
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

        mTagPaint.setTextAlign(Paint.Align.LEFT);
        // 左侧
        Drawable dLeft = getCompoundDrawables()[0];
        int startX = getPaddingLeft();
        if (dLeft != null) {
            startX += dLeft.getIntrinsicWidth() + getCompoundDrawablePadding();
        }
        int baseLine = getBaseline();
        canvas.drawText(tempTag, startX + getScrollX(), baseLine, mTagPaint);

    }

    // 增加左侧Tag的间距
    @Override
    public int getCompoundPaddingLeft() {
        // tag 模块的长度
        int tagTextLength = 0;
        if (!TextUtils.isEmpty(mTagText)) {
            String tempTag = useTagSeparator ? mTagText + mTagSeparator : mTagText;
            tagTextLength = getTextBound(tempTag, mTagPaint).width() + mTagPadding;
        }

        // separator 模块的长度
        int separatorLength = 0;
        if (mSeparatorLineWidth > 0 && mSeparatorLineHeight > 0) {
            separatorLength += mSeparatorLineWidth + mSeparatorLinePadding;
        }

        return super.getCompoundPaddingLeft() + tagTextLength + separatorLength;
    }

    @Override
    public int getCompoundPaddingRight() {

        // 清除按钮的长度
        int crossLength = 0;
        if (useCross) {
            crossLength = getCrossValidSize();
        }

        return super.getCompoundPaddingRight() + crossLength;
    }

    /**
     * 绘制底部线条
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (mBottomLineSize > 0) {
            mLineRect.left = mBottomLineMarginLeft + getScrollX();
            mLineRect.top = height - mBottomLineSize;
            mLineRect.right = width - mBottomLineMarginRight + getScrollX();
            mLineRect.bottom = height;
            canvas.drawRect(mLineRect, mLinePaint);
        }
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

    /**
     * 输入框左右抖动
     *
     * @param counts 左右抖动的次数
     */
    public void startShake(int counts) {
        if (translateAnimation == null) {
            translateAnimation = new TranslateAnimation(0, 10, 0, 0);
            translateAnimation.setInterpolator(new CycleInterpolator(counts));
            translateAnimation.setDuration(500);
        }
        if (translateAnimation != null && translateAnimation.hasStarted()) {
            translateAnimation.cancel();
        }
        startAnimation(translateAnimation);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        requestLayout();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        super.setText(text, type);
    }

    @Override
    public void setBackgroundColor(int color) {
        mBackgroundDrawable.setColor(color);
    }

    public void setCorner(float corner) {
        this.mCorner = corner;
        setCorner();
    }

    public float getCorner() {
        return this.mCorner;
    }

    public void setCorner(float leftTop, float rightTop, float rightBottom, float leftBottom) {
        this.mCornerLeftTop = leftTop;
        this.mCornerRightTop = rightTop;
        this.mCornerRightBottom = rightBottom;
        this.mCornerLeftBottom = leftBottom;
        setCorner();
    }

    public void setBorderSize(int borderSize) {
        this.mBorderSize = borderSize;
        setBorder();
    }

    public int getBorderSize() {
        return this.mBorderSize;
    }

    public void setBorderColor(int borderColor) {
        this.mBorderColor = borderColor;
        setBorder();
    }

    public int getBorderColor() {
        return this.mBorderColor;
    }

    public void setLineColor(int lineColor) {
        this.mLineColor = lineColor;
        mLinePaint.setColor(lineColor);
        invalidate();
    }

    public int getLineColor() {
        return this.mLineColor;
    }

    public void setBottomLineSize(int bottomLineSize) {
        this.mBottomLineSize = bottomLineSize;
        invalidate();
    }

    public int getBottomLineSize() {
        return this.mBottomLineSize;
    }

    public void setBottomLineMarginLeft(int marginLeft) {
        this.mBottomLineMarginLeft = marginLeft;
        invalidate();
    }

    public int getBottomLineMarginLeft() {
        return this.mBottomLineMarginLeft;
    }

    public void setBottomLineMarginRight(int marginRight) {
        this.mBottomLineMarginRight = marginRight;
        invalidate();
    }

    public int getBottomLineMarginRight() {
        return this.mBottomLineMarginRight;
    }

    public void setTagText(CharSequence tagText) {
        if (TextUtils.isEmpty(tagText)) {
            tagText = "";
        }
        this.mTagText = tagText.toString();
        invalidate();
    }

    public String getTagText() {
        return this.mTagText;
    }

    public void setTagTextColor(int tagTextColor) {
        this.mTagTextColor = tagTextColor;
        mTagPaint.setColor(tagTextColor);
        invalidate();
    }

    public int getTagTextColor() {
        return this.mTagTextColor;
    }

    public void setTagTextSize(int tagTextSize) {
        this.mTagTextSize = tagTextSize;
        mTagPaint.setTextSize(tagTextSize);
        invalidate();
    }

    public int getTagTextSize() {
        return this.mTagTextSize;
    }

    public void setTagPadding(int tagPadding) {
        this.mTagPadding = tagPadding;
        invalidate();
    }

    public int getTagPadding() {
        return this.mTagPadding;
    }

    public void setUseTagSeparator(boolean useTagSeparator) {
        this.useTagSeparator = useTagSeparator;
        invalidate();
    }

    public boolean isUseTagSeparator() {
        return this.useTagSeparator;
    }

    public void setTagSeparator(String separator) {
        if (TextUtils.isEmpty(separator)) {
            separator = ":";
        }
        this.mTagSeparator = separator;
        invalidate();
    }

    public String getTagSeparator() {
        return this.mTagSeparator;
    }

    public void setSeparatorLineWidth(int width) {
        this.mSeparatorLineWidth = width;
        invalidate();
    }

    public int getSeparatorLineWidth() {
        return this.mSeparatorLineWidth;
    }

    public void setSeparatorLineHeight(int height) {
        this.mSeparatorLineHeight = height;
        invalidate();
    }

    public int getSeparatorLineHeight() {
        return this.mSeparatorLineHeight;
    }

    public void setSeparatorLineColor(int color) {
        this.mSeparatorLineColor = color;
        mSeparatorPaint.setColor(color);
        invalidate();
    }

    public int getSeparatorLineColor() {
        return this.mSeparatorLineColor;
    }

    public void setSeparatorLinePadding(int padding) {
        this.mSeparatorLinePadding = padding;
        invalidate();
    }

    public int getSeparatorLinePadding() {
        return this.mSeparatorLinePadding;
    }

    public void setUseCross(boolean useCross) {
        if (useCross) {
            setSingleLine(true);
        }
        this.useCross = useCross;
        invalidate();
    }

    public boolean isUseCross() {
        return this.useCross;
    }

    public void setCrossBgColor(int crossBgColor) {
        this.mCrossBgColor = crossBgColor;
        invalidate();
    }

    public int getCrossBgColor() {
        return this.mCrossBgColor;
    }

    public void setCrossRadius(int crossRadius) {
        this.mCrossRadius = crossRadius;
        invalidate();
    }

    public int getCrossRadius() {
        return this.mCrossRadius;
    }

    public void setCrossColor(int crossColor) {
        this.mCrossColor = crossColor;
        invalidate();
    }

    public int getCrossColor() {
        return this.mCrossColor;
    }

    public void setCrossSize(int size) {
        this.mCrossSize = size;
        invalidate();
    }

    public int getCrossSize() {
        return this.mCrossSize;
    }

    public void setCrossLength(int length) {
        this.mCrossLength = length;
        invalidate();
    }

    public int getCrossLength() {
        return this.mCrossLength;
    }

    public void setCrossPadding(int padding) {
        this.mCrossPadding = padding;
        invalidate();
    }

    public int getCrossPadding() {
        return this.mCrossPadding;
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

    // region ----------- 回调函数 ----------------

    private OnDrawableClickListener onDrawableClickListener;

    public void setOnDrawableClickListener(OnDrawableClickListener onDrawableClickListener) {
        this.onDrawableClickListener = onDrawableClickListener;
    }

    // endregion ---------------------------------

}
