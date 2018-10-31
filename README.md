# XTextView
项目开发过程中TextView经常会被使用到，本项目封装了一些TextView的常用功能，后续会更新EditText等常见控件的封装。。。

### XTextView

![XTextView](https://github.com/hewenyuAndroid/XTextView-Android/blob/master/app/screen/xtextview.gif)

XTextView 封装了TextView常见的一些功能，例如：我们需要将一个TextView设置成圆角，通常是在drawable目录下建一个shape类型的xml文件，如果量一多就会觉得很麻烦，XTextView里面只需要配置相关的属性即可；

### 相关属性
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

