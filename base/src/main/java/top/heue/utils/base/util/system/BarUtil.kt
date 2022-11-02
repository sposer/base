package top.heue.utils.base.util.system

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import java.lang.reflect.Field
import java.lang.reflect.Method


object BarUtil {
    /**获得状态栏高度*/
    fun getStatusBarHeight(context: Context): Int {
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    fun setMode(activity: Activity, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = activity.window.decorView.windowInsetsController
            if (dark) {
                controller?.setSystemBarsAppearance(
                    (WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS),
                    (WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
                )
            } else {
                controller?.setSystemBarsAppearance(
                    0,
                    (WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
                )
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!setMiuiUI(activity, dark))
                if (!setFlymeUI(activity, dark))
                    setCommonUI(activity, dark)
        }
    }

    /**
     * Sets common ui.
     *
     * @param activity the activity
     */
    //设置6.0的字体
    private fun setCommonUI(activity: Activity, dark: Boolean): Boolean {
        var result = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                // 沉浸式
                //activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                //非沉浸式
                activity.window.decorView
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                //非沉浸式
                activity.window.decorView
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            result = true
        }
        return result
    }

    /**
     * Sets flyme ui.
     *
     * @param activity the activity
     * @param dark     the dark
     */
    //设置Flyme的字体
    private fun setFlymeUI(activity: Activity, dark: Boolean): Boolean {
        var result = false
        try {
            val window = activity.window
            val lp = window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
            result = true
        } catch (e: java.lang.Exception) {
        }
        return result
    }

    /**
     * Sets miui ui.
     *
     * @param activity the activity
     * @param dark     the dark
     */
    //设置MIUI字体
    @SuppressLint("PrivateApi")
    private fun setMiuiUI(activity: Activity, dark: Boolean): Boolean {
        var result = false
        try {
            val window: Window = activity.window
            val clazz: Class<*> = activity.window.javaClass
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field: Field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag: Int = field.getInt(layoutParams)
            val extraFlagField: Method = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
            result = true
        } catch (e: Exception) {
        }
        return result
    }
}