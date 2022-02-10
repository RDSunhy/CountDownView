package top.sunhy.countdownview.sample

import android.graphics.Color
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import top.sunhy.countdownview.CountDownViewConfig
import top.sunhy.countdownview.dp
import top.sunhy.countdownview.px
import top.sunhy.countdownview.sample.databinding.ActivityMainBinding
import top.sunhy.countdownview.sp
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        initView()
        initListener()
    }

    private fun initDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this
    }

    private fun initView() {
        toast = Toast(this).apply {
            setText("请输入合法色值")
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_LONG
        }

        val dayTime = 2 * 24 * 60 * 60 * 1000
        mBinding.countDownView.start(System.currentTimeMillis() + dayTime, 0)

        val defaultConfigs = mBinding.countDownView.config()
        mBinding.skTimeTextSize.progress = (defaultConfigs.timeTextSize / 0.35f).px.toInt()
        mBinding.swTimeTextBold.isChecked = defaultConfigs.isTimeTextBold
        mBinding.etTimeColor.setText(String.format("#%06X", defaultConfigs.timeTextColor))
        mBinding.skTimeTextPadding.progress = (defaultConfigs.timeBgPadding / 0.15f).px.toInt()
        mBinding.skSuffixTextSize.progress = (defaultConfigs.suffixTextSize / 0.35f).px.toInt()
        mBinding.swSuffixTextBold.isChecked = defaultConfigs.isSuffixTextBold
        mBinding.etSuffixColor.setText(String.format("#%06X", defaultConfigs.suffixTextColor))
        mBinding.skSuffixTextMargin.progress = (defaultConfigs.timeBgPadding / 0.15f).px.toInt()
        mBinding.swTimeBg.isChecked = defaultConfigs.isShowTimeBg
        mBinding.skTimeRadius.progress = (defaultConfigs.timeBgRadius / 0.5f).px.toInt()
        mBinding.etTimeBgColor.setText(String.format("#%06X", defaultConfigs.timeBgColor))
        mBinding.swShowDay.isChecked = defaultConfigs.isShowDay
        mBinding.swShowDaySuffix.isChecked = defaultConfigs.isShowDaySuffix
        mBinding.swShowHour.isChecked = defaultConfigs.isShowHour
        mBinding.swShowHourSuffix.isChecked = defaultConfigs.isShowHourSuffix
        mBinding.swShowMinute.isChecked = defaultConfigs.isShowMinute
        mBinding.swShowMinuteSuffix.isChecked = defaultConfigs.isShowMinuteSuffix
        mBinding.swShowSecond.isChecked = defaultConfigs.isShowSecond
        mBinding.swShowSecondSuffix.isChecked = defaultConfigs.isShowSecondSuffix
        mBinding.swShowMillisecond.isChecked = defaultConfigs.isShowMilliSecond
        mBinding.swShowMillisecondSuffix.isChecked = defaultConfigs.isShowMilliSecondSuffix

        mBinding.etDaySuffix.setText(defaultConfigs.daySuffixText)
        mBinding.etHourSuffix.setText(defaultConfigs.hourSuffixText)
        mBinding.etMinuteSuffix.setText(defaultConfigs.minuteSuffixText)
        mBinding.etSecondSuffix.setText(defaultConfigs.secondSuffixText)
        mBinding.etMillisecondSuffix.setText(defaultConfigs.milliSecondSuffixText)
    }

    private fun initListener() {
        mBinding.etTimeColor.addTextChangedListener {
            try {
                mBinding.countDownView.config().timeTextColor = it.toString().toColorInt()
                mBinding.countDownView.updateConfig()
            } catch (e: Exception) {
                showColorToast()
            }
        }

        mBinding.etSuffixColor.addTextChangedListener {
            try {
                mBinding.countDownView.config().suffixTextColor = it.toString().toColorInt()
                mBinding.countDownView.updateConfig()
            } catch (e: Exception) {
                showColorToast()
            }
        }

        mBinding.etTimeBgColor.addTextChangedListener {
            try {
                mBinding.countDownView.config().timeBgColor = it.toString().toColorInt()
                mBinding.countDownView.updateConfig()
            } catch (e: Exception) {
                showColorToast()
            }
        }

        mBinding.skTimeTextSize.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.countDownView.config().timeTextSize = (progress * 0.35f).sp
                mBinding.countDownView.updateConfig()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        mBinding.skSuffixTextSize.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.countDownView.config().suffixTextSize = (progress * 0.35f).sp
                mBinding.countDownView.updateConfig()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        mBinding.swTimeBg.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowTimeBg = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.skTimeRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.countDownView.config().timeBgRadius = (progress * 0.5f).dp.toInt()
                mBinding.countDownView.updateConfig()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        mBinding.swShowDay.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowDay = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.swShowHour.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowHour = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.swShowMinute.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowMinute = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.swShowSecond.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowSecond = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.swShowMillisecond.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowMilliSecond = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.swShowDaySuffix.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowDaySuffix = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.swShowHourSuffix.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowHourSuffix = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.swShowMinuteSuffix.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowMinuteSuffix = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.swShowSecondSuffix.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowSecondSuffix = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.swShowMillisecondSuffix.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isShowMilliSecondSuffix = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.etDaySuffix.addTextChangedListener {
            mBinding.countDownView.config().daySuffixText = it.toString()
            mBinding.countDownView.updateConfig()
        }

        mBinding.etHourSuffix.addTextChangedListener {
            mBinding.countDownView.config().hourSuffixText = it.toString()
            mBinding.countDownView.updateConfig()
        }

        mBinding.etMinuteSuffix.addTextChangedListener {
            mBinding.countDownView.config().minuteSuffixText = it.toString()
            mBinding.countDownView.updateConfig()
        }

        mBinding.etSecondSuffix.addTextChangedListener {
            mBinding.countDownView.config().secondSuffixText = it.toString()
            mBinding.countDownView.updateConfig()
        }

        mBinding.etMillisecondSuffix.addTextChangedListener {
            mBinding.countDownView.config().milliSecondSuffixText = it.toString()
            mBinding.countDownView.updateConfig()
        }

        mBinding.swTimeTextBold.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isTimeTextBold = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.swSuffixTextBold.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.countDownView.config().isSuffixTextBold = isChecked
            mBinding.countDownView.updateConfig()
        }

        mBinding.skTimeTextPadding.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.countDownView.config().timeBgPadding = (progress * 0.15f).dp.toInt()
                mBinding.countDownView.updateConfig()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        mBinding.skSuffixTextMargin.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.countDownView.config().suffixDayHorizontalMargin = (progress * 0.15f).dp.toInt()
                mBinding.countDownView.config().suffixHourHorizontalMargin = (progress * 0.15f).dp.toInt()
                mBinding.countDownView.config().suffixMinuteHorizontalMargin = (progress * 0.15f).dp.toInt()
                mBinding.countDownView.config().suffixSecondHorizontalMargin = (progress * 0.15f).dp.toInt()
                mBinding.countDownView.config().suffixMilliSecondHorizontalMargin = (progress * 0.15f).dp.toInt()
                mBinding.countDownView.updateConfig()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        mBinding.rgSuffixGravity.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rbTop -> {
                    mBinding.countDownView.config().suffixGravity = CountDownViewConfig.GRAVITY_TOP
                    mBinding.countDownView.updateConfig()
                }
                R.id.rbBottom -> {
                    mBinding.countDownView.config().suffixGravity = CountDownViewConfig.GRAVITY_BOTTOM
                    mBinding.countDownView.updateConfig()
                }
                R.id.rbCenter -> {
                    mBinding.countDownView.config().suffixGravity = CountDownViewConfig.GRAVITY_CENTER
                    mBinding.countDownView.updateConfig()
                }
            }
        }
    }

    private fun showColorToast(){
        toast?.show()
    }
}