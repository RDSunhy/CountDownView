package top.sunhy.countdownview

import android.content.res.Resources

val Float.px: Float
    get(){
        val scale = Resources.getSystem().displayMetrics.density
        return (this / scale - 0.5f)
    }

val Int.px: Int
    get(){
        val scale = Resources.getSystem().displayMetrics.density
        return (this / scale - 0.5f).toInt()
    }

val Float.dp: Float
    get(){
        val scale = Resources.getSystem().displayMetrics.density
        return (this * scale + 0.5f)
    }

val Int.dp: Int
    get(){
        val scale = Resources.getSystem().displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

val Float.sp: Float
    get(){
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (this * fontScale + 0.5f)
    }

val Int.sp: Int
    get(){
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (this * fontScale + 0.5f).toInt()
    }