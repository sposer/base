package top.heue.utils.base.util.view

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics


object ScreenUtil {
    fun dpToPx(context: Context, dpValue: Float): Float {
        // 获取屏幕密度
        val scale: Float = context.resources.displayMetrics.density
        // 结果+0.5是为了int取整时更接近
        return dpValue * scale + 0.5f
    }

    fun getScreenHeight(activity: Activity): Int {
        return getScreenSize(activity)[1]
    }

    /**
     * 获取屏幕大小，不包含系统栏部分
     * @return 数字数组，a[0]为宽度，a[1]为长度
     */
    fun getScreenSize(activity: Activity): IntArray {
        val outMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            val size = activity.windowManager.maximumWindowMetrics.bounds
            return intArrayOf(size.width(), size.height())
        }
        activity.windowManager.defaultDisplay.getMetrics(outMetrics)
        return intArrayOf(outMetrics.widthPixels, outMetrics.heightPixels)
    }
}