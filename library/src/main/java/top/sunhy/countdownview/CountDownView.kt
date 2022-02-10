package top.sunhy.countdownview

import android.content.Context
import android.graphics.*
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.core.graphics.withSave
import kotlin.math.max

class CountDownView : View {

    private val mTimeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTimeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mSuffixTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mDayTimeRect = RectF()
    private var mHourTimeRect = RectF()
    private var mMinuteTimeRect = RectF()
    private var mSecondTimeRect = RectF()
    private var mMilliSecondTimeRect = RectF()

    private var mDaySuffixRect = RectF()
    private var mHourSuffixRect = RectF()
    private var mMinuteSuffixRect = RectF()
    private var mSecondSuffixRect = RectF()
    private var mMilliSecondSuffixRect = RectF()

    private val mConfig = CountDownViewConfig()

    private var mTimer: CountDownTimer? = null
    private var mRemainTime = 0L

    private var dayTimeText = "00"
    private var hourTimeText = "00"
    private var minuteTimeText = "00"
    private var secondTimeText = "00"
    private var milliSecondTimeText = "00"

    private var mWidth = 0
    private var mHeight = 0

    private var listener: CountDownViewListener? = null

    init {

    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val attrs = context.obtainStyledAttributes(attrs, R.styleable.sunhy_countdown_view)
        mConfig.timeTextSize = attrs.getDimension(R.styleable.sunhy_countdown_view_timeTextSize, 14f.sp)
        mConfig.suffixTextSize = attrs.getDimension(R.styleable.sunhy_countdown_view_suffixTextSize, 14f.sp)

        mConfig.timeTextColor = attrs.getColor(R.styleable.sunhy_countdown_view_timeTextColor, Color.WHITE)
        mConfig.timeBgColor = attrs.getColor(R.styleable.sunhy_countdown_view_timeBgColor, Color.BLACK)
        mConfig.suffixTextColor = attrs.getColor(R.styleable.sunhy_countdown_view_suffixTextColor, Color.BLACK)

        mConfig.isTimeTextBold = attrs.getBoolean(R.styleable.sunhy_countdown_view_isTimeTextBold, false)
        mConfig.isSuffixTextBold = attrs.getBoolean(R.styleable.sunhy_countdown_view_isSuffixTextBold, false)

        mConfig.daySuffixText = attrs.getString(R.styleable.sunhy_countdown_view_daySuffixText)?:"天"
        mConfig.hourSuffixText = attrs.getString(R.styleable.sunhy_countdown_view_hourSuffixText)?:"时"
        mConfig.minuteSuffixText = attrs.getString(R.styleable.sunhy_countdown_view_minuteSuffixText)?:"分"
        mConfig.secondSuffixText = attrs.getString(R.styleable.sunhy_countdown_view_secondSuffixText)?:"秒"
        mConfig.milliSecondSuffixText = attrs.getString(R.styleable.sunhy_countdown_view_milliSecondSuffixText)?:""

        mConfig.isShowDay = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowDay, true)
        mConfig.isShowDaySuffix = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowDaySuffix, true)
        mConfig.isShowHour = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowHour, true)
        mConfig.isShowHourSuffix = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowHourSuffix, true)
        mConfig.isShowMinute = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowMinute, true)
        mConfig.isShowMinuteSuffix = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowMinuteSuffix, true)
        mConfig.isShowSecond = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowSecond, true)
        mConfig.isShowSecondSuffix = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowSecondSuffix, true)
        mConfig.isShowMilliSecond = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowMilliSecond, false)
        mConfig.isShowMilliSecondSuffix = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowMilliSecondSuffix, false)
        mConfig.isShowTimeBg = attrs.getBoolean(R.styleable.sunhy_countdown_view_isShowTimeBg, true)

        mConfig.timeBgRadius = attrs.getDimension(R.styleable.sunhy_countdown_view_timeBgRadius, 2f.dp).toInt()
        mConfig.timeBgPadding = attrs.getDimension(R.styleable.sunhy_countdown_view_timeBgPadding, 2f.dp).toInt()

        mConfig.suffixDayHorizontalMargin = attrs.getDimension(R.styleable.sunhy_countdown_view_suffixDayHorizontalMargin, 2f.dp).toInt()
        mConfig.suffixHourHorizontalMargin = attrs.getDimension(R.styleable.sunhy_countdown_view_suffixHourHorizontalMargin, 2f.dp).toInt()
        mConfig.suffixMinuteHorizontalMargin = attrs.getDimension(R.styleable.sunhy_countdown_view_suffixMinuteHorizontalMargin, 2f.dp).toInt()
        mConfig.suffixSecondHorizontalMargin = attrs.getDimension(R.styleable.sunhy_countdown_view_suffixSecondHorizontalMargin, 2f.dp).toInt()
        mConfig.suffixMilliSecondHorizontalMargin = attrs.getDimension(R.styleable.sunhy_countdown_view_suffixMilliSecondHorizontalMargin, 2f.dp).toInt()

        if (attrs.hasValue(R.styleable.sunhy_countdown_view_suffixGravity)) {
            mConfig.suffixGravity = attrs.getInt(R.styleable.sunhy_countdown_view_suffixGravity, CountDownViewConfig.GRAVITY_CENTER)
        }

        attrs.recycle()

        initPaint()
    }

    private fun initPaint() {
        //时间text画笔
        mTimeTextPaint.textSize = mConfig.timeTextSize
        mTimeTextPaint.textAlign = Paint.Align.CENTER
        mTimeTextPaint.color = mConfig.timeTextColor
        mTimeTextPaint.typeface = if (mConfig.isTimeTextBold) {
            Typeface.DEFAULT_BOLD
        } else {
            Typeface.DEFAULT
        }

        //时间背景画笔
        mTimeBgPaint.color = mConfig.timeBgColor
        mTimeBgPaint.style = Paint.Style.FILL

        //后缀text画笔
        mSuffixTextPaint.textSize = mConfig.suffixTextSize
        mSuffixTextPaint.textAlign = Paint.Align.CENTER
        mSuffixTextPaint.color = mConfig.suffixTextColor
        mSuffixTextPaint.typeface = if (mConfig.isSuffixTextBold) {
            Typeface.DEFAULT_BOLD
        } else {
            Typeface.DEFAULT
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //测量宽高
        mWidth = 0
        mHeight = 0

        measureDayPart()
        measureHourPart()
        measureMinutePart()
        measureSecondPart()
        measureMilliSecondPart()

        setMeasuredDimension(
            mWidth + paddingLeft + paddingRight,
            mHeight + paddingTop + paddingBottom
        )
    }


    override fun onDraw(canvas: Canvas) {
        val timeTextdy =
            (mTimeTextPaint.fontMetrics.bottom - mTimeTextPaint.fontMetrics.top) / 2 - mTimeTextPaint.fontMetrics.bottom
        val suffixTextdy =
            (mSuffixTextPaint.fontMetrics.bottom - mSuffixTextPaint.fontMetrics.top) / 2 - mSuffixTextPaint.fontMetrics.bottom

        drawDayPart(canvas, timeTextdy, suffixTextdy)
        drawHourPart(canvas, timeTextdy, suffixTextdy)
        drawMinutePart(canvas, timeTextdy, suffixTextdy)
        drawSecondPart(canvas, timeTextdy, suffixTextdy)
        drawMilliSecondPart(canvas, timeTextdy, suffixTextdy)

    }

    private fun calculateTime(): Boolean {
        //位数变化时重新测量
        var isNeedRequestLayout = false
        //天
        if (mConfig.isShowDay) {
            val dayTime = mRemainTime / (24 * 60 * 60 * 1000)
            val newDayTimeText = if (dayTime < 10) {
                "0$dayTime"
            } else {
                "$dayTime"
            }
            if (newDayTimeText.length != dayTimeText.length) {
                isNeedRequestLayout = true
            }
            dayTimeText = newDayTimeText
        }

        //时
        if (mConfig.isShowHour) {
            val hourTime = if (mConfig.isShowDay) {
                mRemainTime % (24 * 60 * 60 * 1000) / (60 * 60 * 1000)
            } else { //不展示天数时 直接转换为小时数
                mRemainTime / (60 * 60 * 1000)
            }
            val newHourTimeText = if (hourTime < 10) {
                "0$hourTime"
            } else {
                "$hourTime"
            }
            if (newHourTimeText.length != hourTimeText.length) {
                isNeedRequestLayout = true
            }
            hourTimeText = newHourTimeText
        }

        //分
        if (mConfig.isShowMinute) {
            val minuteTime = if (mConfig.isShowHour) {
                mRemainTime % (60 * 60 * 1000) / (60 * 1000)
            } else { //不展小时数时 直接转换为分钟数
                mRemainTime / (60 * 1000)
            }
            val newMinuteTimeText = if (minuteTime < 10) {
                "0$minuteTime"
            } else {
                "$minuteTime"
            }
            if (newMinuteTimeText.length != minuteTimeText.length) {
                isNeedRequestLayout = true
            }
            minuteTimeText = newMinuteTimeText
        }

        //秒
        if (mConfig.isShowSecond) {
            val secondTime = if (mConfig.isShowMinute) {
                mRemainTime % (60 * 1000) / (1000)
            } else { //不展分钟数时 直接转换为秒数
                mRemainTime / 1000
            }
            val newSecondTimeText = if (secondTime < 10) {
                "0$secondTime"
            } else {
                "$secondTime"
            }
            if (newSecondTimeText.length != secondTimeText.length) {
                isNeedRequestLayout = true
            }
            secondTimeText = newSecondTimeText
        }

        //毫秒
        if (mConfig.isShowMilliSecond) {
            val milliSecondTime = if (mConfig.isShowSecond) {
                mRemainTime % 1000 / 10
            } else { //不展秒数时 直接转换为毫秒数
                mRemainTime / 10
            }
            val newMilliSecondTimeText = if (milliSecondTime < 10) {
                "0$milliSecondTime"
            } else {
                "$milliSecondTime"
            }
            if (newMilliSecondTimeText.length != milliSecondTimeText.length) {
                isNeedRequestLayout = true
            }
            milliSecondTimeText = newMilliSecondTimeText
        }
        /*Log.e(
            "CountDownView timeText",
            "$dayTimeText  $hourTimeText  $minuteTimeText  $secondTimeText $milliSecondTimeText"
        )*/

        return isNeedRequestLayout
    }

    private fun drawDayPart(canvas: Canvas, timeTextdy: Float, suffixTextdy: Float) {
        if (!mConfig.isShowDay) {
            return
        }

        val startX = paddingLeft.toFloat()
        var startY = paddingTop + (mHeight - mDayTimeRect.height()) / 2

        Log.e("drawDayPart", "startX: ${startX}, startY: ${startY}")

        //画时间背景、文字
        canvas.withSave {
            translate(startX, startY)
            if (mConfig.isShowTimeBg) {
                drawRoundRect(
                    mDayTimeRect,
                    mConfig.timeBgRadius.toFloat(),
                    mConfig.timeBgRadius.toFloat(),
                    mTimeBgPaint
                )
            }
            drawText(
                dayTimeText,
                mDayTimeRect.centerX(),
                mDayTimeRect.centerY() + timeTextdy,
                mTimeTextPaint
            )
        }

        //画后缀文字
        if (mConfig.isShowDaySuffix) {
            startY = when(mConfig.suffixGravity){
                CountDownViewConfig.GRAVITY_TOP -> paddingTop.toFloat()
                CountDownViewConfig.GRAVITY_BOTTOM -> paddingTop + (mHeight - mDaySuffixRect.height())
                else -> paddingTop + (mHeight - mDaySuffixRect.height()) / 2
            }
            canvas.withSave {
                translate(startX + mDayTimeRect.width(), startY)
                drawText(
                    mConfig.daySuffixText,
                    mDaySuffixRect.centerX(),
                    mDaySuffixRect.centerY() + suffixTextdy,
                    mSuffixTextPaint
                )
            }
        }
    }

    private fun drawHourPart(canvas: Canvas, timeTextdy: Float, suffixTextdy: Float) {
        if (!mConfig.isShowHour) {
            return
        }

        val startX = paddingLeft + mDayTimeRect.width() + mDaySuffixRect.width()
        var startY = paddingTop + (mHeight - mHourTimeRect.height()) / 2

        //画时间背景、文字
        canvas.withSave {
            translate(startX, startY)
            if (mConfig.isShowTimeBg) {
                drawRoundRect(
                    mHourTimeRect,
                    mConfig.timeBgRadius.toFloat(),
                    mConfig.timeBgRadius.toFloat(),
                    mTimeBgPaint
                )
            }
            drawText(
                hourTimeText,
                mHourTimeRect.centerX(),
                mHourTimeRect.centerY() + timeTextdy,
                mTimeTextPaint
            )
        }

        //画后缀文字
        if (mConfig.isShowHourSuffix) {
            startY = when(mConfig.suffixGravity){
                CountDownViewConfig.GRAVITY_TOP -> paddingTop.toFloat()
                CountDownViewConfig.GRAVITY_BOTTOM -> paddingTop + (mHeight - mHourSuffixRect.height())
                else -> paddingTop + (mHeight - mHourSuffixRect.height()) / 2
            }
            canvas.withSave {
                translate(startX + mHourTimeRect.width(), startY)
                if (mConfig.isShowHourSuffix) {
                    drawText(
                        mConfig.hourSuffixText,
                        mHourSuffixRect.centerX(),
                        mHourSuffixRect.centerY() + suffixTextdy,
                        mSuffixTextPaint
                    )
                }
            }
        }
    }

    private fun drawMinutePart(canvas: Canvas, timeTextdy: Float, suffixTextdy: Float) {
        if (!mConfig.isShowMinute) {
            return
        }

        val startX = paddingLeft + mDayTimeRect.width() +
                mDaySuffixRect.width() +
                mHourTimeRect.width() +
                mHourSuffixRect.width()
        var startY = paddingTop + (mHeight - mMinuteTimeRect.height()) / 2

        //画时间背景、文字
        canvas.withSave {
            translate(startX, startY)
            if (mConfig.isShowTimeBg) {
                drawRoundRect(
                    mMinuteTimeRect,
                    mConfig.timeBgRadius.toFloat(),
                    mConfig.timeBgRadius.toFloat(),
                    mTimeBgPaint
                )
            }
            drawText(
                minuteTimeText,
                mMinuteTimeRect.centerX(),
                mMinuteTimeRect.centerY() + timeTextdy,
                mTimeTextPaint
            )
        }

        //画后缀文字
        if (mConfig.isShowMinuteSuffix) {
            startY = when(mConfig.suffixGravity){
                CountDownViewConfig.GRAVITY_TOP -> paddingTop.toFloat()
                CountDownViewConfig.GRAVITY_BOTTOM -> paddingTop + (mHeight - mMinuteSuffixRect.height())
                else -> paddingTop + (mHeight - mMinuteSuffixRect.height()) / 2
            }
            canvas.withSave {
                translate(startX + mMinuteTimeRect.width(), startY)
                if (mConfig.isShowMinuteSuffix) {
                    drawText(
                        mConfig.minuteSuffixText,
                        mMinuteSuffixRect.centerX(),
                        mMinuteSuffixRect.centerY() + suffixTextdy,
                        mSuffixTextPaint
                    )
                }
            }
        }
    }

    private fun drawSecondPart(canvas: Canvas, timeTextdy: Float, suffixTextdy: Float) {
        if (!mConfig.isShowSecond) {
            return
        }

        val startX = paddingLeft + mDayTimeRect.width() +
                mDaySuffixRect.width() +
                mHourTimeRect.width() +
                mHourSuffixRect.width() +
                mMinuteTimeRect.width() +
                mMinuteSuffixRect.width()
        var startY = paddingTop + (mHeight - mSecondTimeRect.height()) / 2

        //画时间背景、文字
        canvas.withSave {
            translate(startX, startY)
            if (mConfig.isShowTimeBg) {
                drawRoundRect(
                    mSecondTimeRect,
                    mConfig.timeBgRadius.toFloat(),
                    mConfig.timeBgRadius.toFloat(),
                    mTimeBgPaint
                )
            }
            drawText(
                secondTimeText,
                mSecondTimeRect.centerX(),
                mSecondTimeRect.centerY() + timeTextdy,
                mTimeTextPaint
            )
        }

        //画后缀文字
        if (mConfig.isShowSecondSuffix) {
            startY = when(mConfig.suffixGravity){
                CountDownViewConfig.GRAVITY_TOP -> paddingTop.toFloat()
                CountDownViewConfig.GRAVITY_BOTTOM -> paddingTop + (mHeight - mSecondSuffixRect.height())
                else -> paddingTop + (mHeight - mSecondSuffixRect.height()) / 2
            }
            canvas.withSave {
                translate(startX + mSecondTimeRect.width(), startY)
                if (mConfig.isShowSecondSuffix) {
                    drawText(
                        mConfig.secondSuffixText,
                        mSecondSuffixRect.centerX(),
                        mSecondSuffixRect.centerY() + suffixTextdy,
                        mSuffixTextPaint
                    )
                }
            }
        }
    }

    private fun drawMilliSecondPart(canvas: Canvas, timeTextdy: Float, suffixTextdy: Float) {
        if (!mConfig.isShowMilliSecond) {
            return
        }

        val startX = paddingLeft + mDayTimeRect.width() +
                mDaySuffixRect.width() +
                mHourTimeRect.width() +
                mHourSuffixRect.width() +
                mMinuteTimeRect.width() +
                mMinuteSuffixRect.width() +
                mSecondTimeRect.width() +
                mSecondSuffixRect.width()
        var startY = paddingTop + (mHeight - mMilliSecondTimeRect.height()) / 2

        //画时间背景、文字
        canvas.withSave {
            translate(startX, startY)
            if (mConfig.isShowTimeBg) {
                drawRoundRect(
                    mMilliSecondTimeRect,
                    mConfig.timeBgRadius.toFloat(),
                    mConfig.timeBgRadius.toFloat(),
                    mTimeBgPaint
                )
            }
            drawText(
                milliSecondTimeText,
                mMilliSecondTimeRect.centerX(),
                mMilliSecondTimeRect.centerY() + timeTextdy,
                mTimeTextPaint
            )
        }

        //画后缀文字
        if (mConfig.isShowMilliSecondSuffix) {
            startY = when(mConfig.suffixGravity){
                CountDownViewConfig.GRAVITY_TOP -> paddingTop.toFloat()
                CountDownViewConfig.GRAVITY_BOTTOM -> paddingTop + (mHeight - mMilliSecondSuffixRect.height())
                else -> paddingTop + (mHeight - mMilliSecondSuffixRect.height()) / 2
            }
            canvas.withSave {
                translate(startX + mMilliSecondTimeRect.width(), startY)
                if (mConfig.isShowMilliSecondSuffix) {
                    drawText(
                        mConfig.milliSecondSuffixText,
                        mMilliSecondSuffixRect.centerX(),
                        mMilliSecondSuffixRect.centerY() + suffixTextdy,
                        mSuffixTextPaint
                    )
                }
            }
        }
    }

    private fun measureDayPart() {
        mDayTimeRect = RectF()
        mDaySuffixRect = RectF()
        if (!mConfig.isShowDay) {
            return
        }
        //测量时间文字rect
        val timeTextWidth = mTimeTextPaint.measureText("0") * dayTimeText.length
        val timeTextHeight =
            mTimeTextPaint.fontMetrics.descent - mTimeTextPaint.fontMetrics.ascent + mTimeTextPaint.fontMetrics.leading
        mDayTimeRect.left = 0f
        mDayTimeRect.top = 0f
        if (mConfig.isShowTimeBg) {
            mDayTimeRect.right = timeTextWidth + mConfig.timeBgPadding * 2
            mDayTimeRect.bottom = timeTextHeight + mConfig.timeBgPadding * 2
        } else {
            mDayTimeRect.right = timeTextWidth
            mDayTimeRect.bottom = timeTextHeight
        }


        if (mConfig.isShowDaySuffix&& !mConfig.secondSuffixText.isNullOrEmpty()) {
            //测量后缀文字rect
            val suffixTextWidht = mSuffixTextPaint.measureText(mConfig.daySuffixText)
            val suffixTextHeight =
                mSuffixTextPaint.fontMetrics.descent - mSuffixTextPaint.fontMetrics.ascent + mSuffixTextPaint.fontMetrics.leading
            mDaySuffixRect.left = 0f
            mDaySuffixRect.top = 0f
            mDaySuffixRect.right =
                suffixTextWidht + mConfig.suffixDayHorizontalMargin
            mDaySuffixRect.bottom = suffixTextHeight
            mWidth += mConfig.suffixDayHorizontalMargin
        }

        mWidth += (mDayTimeRect.width() + mDaySuffixRect.width()).toInt()
        mHeight = max(
            mHeight,
            max(mDayTimeRect.height().toInt(), mDaySuffixRect.height().toInt())
        )
    }

    private fun measureHourPart() {
        mHourTimeRect = RectF()
        mHourSuffixRect = RectF()
        if (!mConfig.isShowHour) {
            return
        }
        //测量时间文字rect
        val timeTextWidth = mTimeTextPaint.measureText("0") * hourTimeText.length
        val timeTextHeight =
            mTimeTextPaint.fontMetrics.descent - mTimeTextPaint.fontMetrics.ascent + mTimeTextPaint.fontMetrics.leading
        mHourTimeRect.left = 0f
        mHourTimeRect.top = 0f
        if (mConfig.isShowTimeBg) {
            mHourTimeRect.right = timeTextWidth + mConfig.timeBgPadding * 2
            mHourTimeRect.bottom = timeTextHeight + mConfig.timeBgPadding * 2
        } else {
            mHourTimeRect.right = timeTextWidth
            mHourTimeRect.bottom = timeTextHeight
        }


        if (mConfig.isShowHourSuffix&& !mConfig.hourSuffixText.isNullOrEmpty()) {
            //测量后缀文字rect
            val suffixTextWidht = mSuffixTextPaint.measureText(mConfig.hourSuffixText)
            val suffixTextHeight =
                mSuffixTextPaint.fontMetrics.descent - mSuffixTextPaint.fontMetrics.ascent + mSuffixTextPaint.fontMetrics.leading
            mHourSuffixRect.left = 0f
            mHourSuffixRect.top = 0f
            mHourSuffixRect.right =
                suffixTextWidht + mConfig.suffixHourHorizontalMargin
            mHourSuffixRect.bottom = suffixTextHeight
        }

        mWidth += (mHourTimeRect.width() + mHourSuffixRect.width()).toInt()
        mHeight = max(
            mHeight,
            max(mHourTimeRect.height().toInt(), mHourSuffixRect.height().toInt())
        )
    }

    private fun measureMinutePart() {
        mMinuteTimeRect = RectF()
        mMinuteSuffixRect = RectF()
        if (!mConfig.isShowMinute) {
            return
        }
        //测量时间文字rect
        val timeTextWidth = mTimeTextPaint.measureText("0") * minuteTimeText.length
        val timeTextHeight =
            mTimeTextPaint.fontMetrics.descent - mTimeTextPaint.fontMetrics.ascent + mTimeTextPaint.fontMetrics.leading
        mMinuteTimeRect.left = 0f
        mMinuteTimeRect.top = 0f
        if (mConfig.isShowTimeBg) {
            mMinuteTimeRect.right = timeTextWidth + mConfig.timeBgPadding * 2
            mMinuteTimeRect.bottom = timeTextHeight + mConfig.timeBgPadding * 2
        } else {
            mMinuteTimeRect.right = timeTextWidth
            mMinuteTimeRect.bottom = timeTextHeight
        }


        if (mConfig.isShowMinuteSuffix && !mConfig.minuteSuffixText.isNullOrEmpty()) {
            //测量后缀文字rect
            val suffixTextWidht = mSuffixTextPaint.measureText(mConfig.minuteSuffixText)
            val suffixTextHeight =
                mSuffixTextPaint.fontMetrics.descent - mSuffixTextPaint.fontMetrics.ascent + mSuffixTextPaint.fontMetrics.leading
            mMinuteSuffixRect.left = 0f
            mMinuteSuffixRect.top = 0f
            mMinuteSuffixRect.right =
                suffixTextWidht + mConfig.suffixMinuteHorizontalMargin
            mMinuteSuffixRect.bottom = suffixTextHeight
        }

        mWidth += (mMinuteTimeRect.width() + mMinuteSuffixRect.width()).toInt()
        mHeight =
            max(mHeight, max(mMinuteTimeRect.height().toInt(), mMinuteSuffixRect.height().toInt()))
    }

    private fun measureSecondPart() {
        mSecondTimeRect = RectF()
        mSecondSuffixRect = RectF()
        if (!mConfig.isShowSecond) {
            return
        }
        //测量时间文字rect
        val timeTextWidth = mTimeTextPaint.measureText("0") * secondTimeText.length
        val timeTextHeight =
            mTimeTextPaint.fontMetrics.descent - mTimeTextPaint.fontMetrics.ascent + mTimeTextPaint.fontMetrics.leading
        mSecondTimeRect.left = 0f
        mSecondTimeRect.top = 0f
        if (mConfig.isShowTimeBg) {
            mSecondTimeRect.right = timeTextWidth + mConfig.timeBgPadding * 2
            mSecondTimeRect.bottom = timeTextHeight + mConfig.timeBgPadding * 2
        } else {
            mSecondTimeRect.right = timeTextWidth
            mSecondTimeRect.bottom = timeTextHeight
        }


        if (mConfig.isShowSecondSuffix && !mConfig.secondSuffixText.isNullOrEmpty()) {
            //测量后缀文字rect
            val suffixTextWidht = mSuffixTextPaint.measureText(mConfig.secondSuffixText)
            val suffixTextHeight =
                mSuffixTextPaint.fontMetrics.descent - mSuffixTextPaint.fontMetrics.ascent + mSuffixTextPaint.fontMetrics.leading
            mSecondSuffixRect.left = 0f
            mSecondSuffixRect.top = 0f
            mSecondSuffixRect.right =
                suffixTextWidht + mConfig.suffixSecondHorizontalMargin
            mSecondSuffixRect.bottom = suffixTextHeight
        }

        mWidth += (mSecondTimeRect.width() + mSecondSuffixRect.width()).toInt()
        mHeight = max(
            mHeight,
            max(mSecondTimeRect.height().toInt(), mSecondSuffixRect.height().toInt())
        )
    }

    private fun measureMilliSecondPart() {
        mMilliSecondTimeRect = RectF()
        mMilliSecondSuffixRect = RectF()
        if (!mConfig.isShowMilliSecond) {
            return
        }
        //测量时间文字rect
        val timeTextWidth = mTimeTextPaint.measureText("0") * milliSecondTimeText.length
        val timeTextHeight =
            mTimeTextPaint.fontMetrics.descent - mTimeTextPaint.fontMetrics.ascent + mTimeTextPaint.fontMetrics.leading
        mMilliSecondTimeRect.left = 0f
        mMilliSecondTimeRect.top = 0f
        if (mConfig.isShowTimeBg) {
            mMilliSecondTimeRect.right = timeTextWidth + mConfig.timeBgPadding * 2
            mMilliSecondTimeRect.bottom = timeTextHeight + mConfig.timeBgPadding * 2
        } else {
            mMilliSecondTimeRect.right = timeTextWidth
            mMilliSecondTimeRect.bottom = timeTextHeight
        }


        if (mConfig.isShowMilliSecondSuffix && !mConfig.milliSecondSuffixText.isNullOrEmpty()) {
            //测量后缀文字rect
            val suffixTextWidht = mSuffixTextPaint.measureText(mConfig.milliSecondSuffixText)
            val suffixTextHeight =
                mSuffixTextPaint.fontMetrics.descent - mSuffixTextPaint.fontMetrics.ascent + mSuffixTextPaint.fontMetrics.leading
            mMilliSecondSuffixRect.left = 0f
            mMilliSecondSuffixRect.top = 0f
            mMilliSecondSuffixRect.right =
                suffixTextWidht + mConfig.suffixMilliSecondHorizontalMargin
            mMilliSecondSuffixRect.bottom = suffixTextHeight
        }
        mWidth += (mMilliSecondTimeRect.width() + mMilliSecondSuffixRect.width()).toInt()
        mHeight = max(
            mHeight,
            max(mMilliSecondTimeRect.height().toInt(), mMilliSecondSuffixRect.height().toInt())
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mTimer?.cancel()
    }

    fun start(millisecond: Long) {
        if (millisecond < 0L) {
            this.visibility = GONE
            return
        }
//        val countDownInterval: Long = if (mConfig.isShowMilliSecond) {
//            10
//        } else {
//            1000
//        }

        val countDownInterval: Long = 10

        mTimer?.cancel()
        mTimer = object : CountDownTimer(millisecond, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                listener?.onTick(this@CountDownView, millisUntilFinished)
                mRemainTime = millisUntilFinished
                if (calculateTime()) {
                    requestLayout()
                } else {
                    postInvalidate()
                }
            }

            override fun onFinish() {
                listener?.onFinish(this@CountDownView)
                mRemainTime = 0
                calculateTime()
                postInvalidate()
            }

        }
        mTimer?.start()
    }

    fun start(startTime: Long, endTime: Long){
        if (startTime == 0L && endTime == 0L) {
            this.visibility = GONE
            return
        }

        var cdTime = if (startTime > 0 && startTime > System.currentTimeMillis()) {
            startTime
        } else if (endTime > System.currentTimeMillis()) {
            endTime
        } else {
            0L
        }

        if (cdTime > 0) {
            this.visibility = VISIBLE
            start(cdTime - System.currentTimeMillis())
        } else {
            this.visibility = GONE
        }

        tag = cdTime
    }

    fun stop(){
        mTimer?.cancel()
    }

    fun setOnCallbackListener(l: CountDownViewListener) {
        this.listener = l
    }

    fun config(): CountDownViewConfig {
        return mConfig
    }

    fun updateConfig() {
        initPaint()
        calculateTime()
        requestLayout()
        invalidate()
    }

}