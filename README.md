# XTextView
项目开发过程中TextView经常会被使用到，本项目封装了一些TextView、EditText的常用功能后期会计需扩展这两个系列...
### 已完成的功能
1. XTextView
2. XEditText
3. FoldTextView
4. XRadioButton(数字提示功能)

### 引用方式
> compile 'com.hewenyu:XTextView:1.2.6'

### XTextView

![XTextView](https://github.com/hewenyuAndroid/XTextView-Android/blob/master/app/screen/xtextview.gif)

XTextView 封装了TextView常见的一些功能，例如：我们需要将一个TextView设置成圆角，通常是在drawable目录下建一个shape类型的xml文件，如果量一多就会觉得很麻烦，XTextView里面只需要配置相关的属性即可；

```Java
// 设置Drawable的点击事件（只有对应位置上的Drawable存在，且设置了监听才会有效）
 xTextView.setOnDrawableClickListener(new OnDrawableClickListener() {
    @Override
    public void onDrawableLeftClickListener(TextView view) {
        // drawableLeft的点击事件
    }

    @Override
    public void onDrawableRightClickListener(TextView view) {
        // drawableRight的点击事件
    }
});

```

| 形状/背景/状态相关属性        | 说明   |
| --------   | -----  | 
|tvShape     |TextView的形状，可选square/circle |
|tvCorner        |shape为square时有效，如果对某个角单独设置了corner，则以单独设置为准  | 
|tvCornerLeftTop        |左上角的圆角半径  | 
|tvCornerRightTop        |右上角的圆角半径  |
|tvCornerRightBottom        |右下角的圆角半径  |
|tvCornerLeftBottom        |坐下角的圆角半径  |
|tvBorderSize       |边框的大小     |
|tvBorderColor      |边框的颜色    |
|tvBorderDashWidth  |边框为虚线时，虚线的长度   |
|tvBorderDashGap    |边框为虚线时，虚线的间隔   |
|tvState            |状态，可选enable\press     |
|tvUseState         |是否启动状态功能           |
|tvStateTextColorInvalid    |状态无效时的文本颜色   |
|tvStateTextColorValid      |状态有效时的文本颜色   |
|tvStateBgColorInvalid      |状态无效时的背景颜色   |
|tvStateBgColorValid        |状态有效时的背景颜色   |
|tvStateBorderColorInvalid  |状态无效时的边框颜色   |
|tvStateBorderColorValid    |状态有效时的边框颜色   |
|tvLineColor                |上下左右线条的颜色（不同于border，这个可以用来做分隔线）     |
|tvBottomLineSize           |底部线条的大小         |
|tvBottomLineMarginLeft     |底部线条与左侧的间距（与padding等属性无关，下同）  |
|tvBottomLineMarginRight    |底部线条与右侧的间距   |
|tvTopLineSize              |顶部线条的大小         |
|tvTopLineMarginLeft        |顶部线条与左侧的间距   |
|tvTopLineMarginRight       |顶部线条与右侧的间距   |
|tvLeftLineSize             |左侧线条的大小         |
|tvLeftLineMarginTop        |左侧线条与顶部的间距   |
|tvLeftLineMarginBottom     |左侧线条与底部的间距   |
|tvRightLineSize            |右侧线条的大小         |
|tvRightLineMarginTop       |右侧线条与顶部的间距   |
|tvRightLineMarginBottom    |右侧线条与底部的间距   |


| tag/drawable/badge相关属性        | 说明   |
| --------   | -----  | 
|tvTagWidth      |Tag的宽度（left时有效） |
|tvTagText      |Tag显示的文本 |
|tvTagTextColor |Tag文本的颜色 |
|tvTagTextSize  |Tag文本的大小 |
|tvTagGravity   |Tag显示的位置:leftTop、topHorizontal、leftVertical、leftBottom|
|tvTagPadding   |Tag文本与实际文本类容的间距，仿照drawablePadding属性实现   |
|tvUseTagSeparator  |是否使用分隔符，默认不使用 |
|tvTagSeparator     |定义分隔符，默认 ":"       |
|tvBadgeText        |badge的数量，默认<1不显示badge |
|tvBadgeTextColor   |badge文本的颜色    |
|tvBadgeTextSize    |badge文本的大小    |
|tvBadgeBgColor     |badge背景颜色      |
|tvBadgeRadius      |badge背景的半径    |
|tvBadgeStyle       |badge显示的类型：数字(number)、红点(dot)   |
|tvBadgeLocal       |badge显示的位置：text、tag、drawable，drawable只支持左侧和顶部两个位置  |
|tvBadgeOffsetX     |badge X 轴上显示的偏移量   |
|tvBadgeOffsetY     |badge Y 轴上显示的偏移量   |
|tvDrawableDirection|指定方向上的drawable可以设置固定宽高   |
|tvDrawableWidth    |指定tvDrawableDirection标记drawable的宽度         |
|tvDrawableHeight   |指定tvDrawableDirection标记drawable的高度         |
|tvDrawableCenter   |设置了Drawable的TextView，内容是否居中显示，默认false        |




### XEditText

![XEditText](https://github.com/hewenyuAndroid/XTextView-Android/blob/master/app/screen/xedittext.gif)

XEditText 封装了常见的一些功能，包括：圆角、边框、Tag标签，分隔符、指定Drawable大小、设置DrawableLeft/DrawableRight的点击事件等

```Java
// 设置Drawable的点击事件（只有对应位置上的Drawable存在，且设置了监听才会有效）
 xEditText.setOnDrawableClickListener(new OnDrawableClickListener() {
    @Override
    public void onDrawableLeftClickListener(TextView view) {
        // drawableLeft的点击事件
    }

    @Override
    public void onDrawableRightClickListener(TextView view) {
        // drawableRight的点击事件
    }
});

```


| 形状/背景相关属性        | 说明   |
| --------   | -----  | 
|tvCorner        |如果对某个角单独设置了corner，则以单独设置为准  | 
|tvCornerLeftTop        |左上角的圆角半径  | 
|tvCornerRightTop        |右上角的圆角半径  |
|tvCornerRightBottom        |右下角的圆角半径  |
|tvCornerLeftBottom        |坐下角的圆角半径  |
|tvBorderSize       |边框的大小     |
|tvBorderColor      |边框的颜色    |
|tvLineColor                |上下左右线条的颜色（不同于border，这个可以用来做分隔线）     |
|tvBottomLineSize           |底部线条的大小         |
|tvBottomLineMarginLeft     |底部线条与左侧的间距（与padding等属性无关，下同）  |
|tvBottomLineMarginRight    |底部线条与右侧的间距   |


| tag/drawable相关属性        | 说明   |
| --------   | -----  | 
|tvTagText      |Tag显示的文本 |
|tvTagTextColor |Tag文本的颜色 |
|tvTagTextSize  |Tag文本的大小 |
|tvTagPadding   |Tag文本与实际文本类容的间距，仿照drawablePadding属性实现   |
|tvUseTagSeparator  |是否使用分隔符，默认不使用 |
|tvTagSeparator     |定义分隔符，默认 ":"       |
|tvSeparatorLineWidth   |定义分割线条的宽度（为0时，此系列无效）    |
|tvSeparatorLineHeight   |定义分割线条的高度（为0时，此系列无效）    |
|tvSeparatorLineColor   |定义分割线条的颜色    |
|tvSeparatorPadding   |定义分隔线与文本的间距，类似DrawablePadding    |
|tvDrawableDirection|指定方向上的drawable可以设置固定宽高   |
|tvDrawableWidth    |指定tvDrawableDirection标记drawable的宽度         |
|tvDrawableHeight   |指定tvDrawableDirection标记drawable的高度         |
|tvUseCross     |是否使用清除功能   |
|tvCrossBgColor |清除按钮的背景颜色 |
|tvCrossRadius  |清除按钮背景的半径 |
|tvCrossColor   |清除按钮交叉的颜色 |
|tvCrossLength  |清除按钮交叉的长度（相对于半径）|
|tvCrossSize    |清除按钮交叉的宽度 |
|tvCrossPadding |类似DrawablePadding|


### FoldTextView

![FoldTextView](https://github.com/hewenyuAndroid/XTextView-Android/blob/master/app/screen/foldtextview.gif)

FoldTextView 是一个可以折叠的TextView，类似微信朋友圈，如果显示的文本过长，可以折叠，同时支持展开全文，支持展开/收起动画，支持设置动画时长，支持列表显示中（ListView/RecyclerView），保存当前数据位置的展开/折叠状态；

```XML
<!-- 自定义属性 -->
<declare-styleable name="FoldTextView">
    <!-- 是否使用折叠功能，默认true，如果为false，则为TextView -->
    <attr name="useFold" format="boolean" />
    <!-- 折叠时最大的行数 -->
    <attr name="foldMaxLines" format="integer" />
    <!-- 折叠的文本 -->
    <attr name="foldText" format="string" />
    <!-- 打开的文本 -->
    <attr name="openText" format="string" />
    <!-- 折叠/打开文本的大小 -->
    <attr name="foldTextSize" format="dimension|reference" />
    <!-- 折叠/打开文本的颜色 -->
    <attr name="foldTextColor" format="color" />
    <!-- 折叠文本显示的位置 -->
    <attr name="foldGravity" format="enum">
        <enum name="left" value="0" />
        <enum name="horizontal" value="1" />
        <enum name="right" value="2" />
    </attr>
    <!-- 折叠文本的上下左右间距 -->
    <attr name="foldMarginTop" format="dimension|reference" />
    <!-- 左间距只有left时有效 -->
    <attr name="foldMarginLeft" format="dimension|reference" />
    <!-- 右间距只有right时有效 -->
    <attr name="foldMarginRight" format="dimension|reference" />
    <attr name="foldMarginBottom" format="dimension|reference" />
    <!-- 折叠动画的延时 -->
    <attr name="foldDuration" format="integer" />
</declare-styleable>
```

- 列表中的使用方式，只需要在ListView适配器的getView()中调用一行代码记录当前数据的状态即可（RecyclerView同理）
```Java
 @Override
public View getView(int position, View convertView, ViewGroup parent) {
    
    ... 省略代码 ...
    
    // 获取需要显示的数据
    NoticeBean bean = mDatas.get(position);
    // 如果需要缓存每个数据的状态，只需要调用此方法，将当前数据作为key传入即可
    viewHolder.tvContent.bindObjState(bean);
    
    ... 省略代码 ...

    return convertView;
}

```