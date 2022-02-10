package top.sunhy.countdownview

interface CountDownViewListener {
    fun onTick(view: CountDownView, millisUntilFinished: Long)
    fun onFinish(view: CountDownView)
}