package top.sunhy.countdownview

import android.graphics.Color
import android.view.Gravity

class CountDownViewConfig {

    companion object{
        const val GRAVITY_TOP = 0
        const val GRAVITY_CENTER = 1
        const val GRAVITY_BOTTOM = 2
    }

    //时间字体大小
    var timeTextSize = 14f.sp
    //后缀字体大小
    var suffixTextSize = 24f.sp

    //时间字体颜色
    var timeTextColor = Color.WHITE
    //时间背景颜色
    var timeBgColor = Color.BLACK
    //后缀字体颜色
    var suffixTextColor = Color.BLACK

    //时间字体是否加粗
    var isTimeTextBold = false
    //后缀字体是否加粗
    var isSuffixTextBold = false

    //后缀显示文字
    var daySuffixText = "天"
    var hourSuffixText = "时"
    var minuteSuffixText = "分"
    var secondSuffixText = "秒"
    var milliSecondSuffixText = ""

    //时间、后缀是否显示
    var isShowDay = true
    var isShowDaySuffix = true
    var isShowHour = true
    var isShowHourSuffix = true
    var isShowMinute = true
    var isShowMinuteSuffix = true
    var isShowSecond = true
    var isShowSecondSuffix = true
    var isShowMilliSecond = true
    var isShowMilliSecondSuffix = true

    //时间部分是否显示背景
    var isShowTimeBg = true

    //时间背景圆角
    var timeBgRadius = 2.dp
    //时间部分padding
    var timeBgPadding = 2.dp

    //后缀部分水平间距
    var suffixDayHorizontalMargin = 2.dp
    var suffixHourHorizontalMargin = 2.dp
    var suffixMinuteHorizontalMargin = 2.dp
    var suffixSecondHorizontalMargin = 2.dp
    var suffixMilliSecondHorizontalMargin = 2.dp

    //后缀位置
    var suffixGravity = GRAVITY_CENTER
}