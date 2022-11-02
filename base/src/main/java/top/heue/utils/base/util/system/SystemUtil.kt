package top.heue.utils.base.util.system

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.WindowManager
import java.util.*

object SystemUtil {
    // 华为
    const val BRAND_HUAWEI = "Huawei"

    // 荣耀
    const val BRAND_HONOR = "HONOR"

    // 华为 NOVA
    const val BRAND_NOVA = "nova"

    // 小米
    const val BRAND_XIAOMI = "xiaomi"

    // OPPO
    const val BRAND_REDMI = "Redmi"

    // vivo
    const val BRAND_VIVO = "vivo"

    // 魅族
    const val BRAND_MEIZU = "Meizu"

    // 索尼
    const val BRAND_SONY = "sony"

    // 三星
    const val BRAND_SAMSUNG = "samsung"

    // OPPO
    const val BRAND_OPPO = "OPPO"

    // 乐视
    const val BRAND_Letv = "letv"

    // 一加
    const val BRAND_OnePlus = "OnePlus"

    // 锤子
    const val BRAND_SMARTISAN = "smartisan"

    // 联想
    const val BRAND_LENOVO = "lenovo"

    // LG
    const val BRAND_LG = "lg"

    // HTC
    const val BRAND_HTC = "htc"

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    fun getSystemLanguage(): String? {
        return Locale.getDefault().language
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    fun getSystemLanguageList(): Array<Locale?>? {
        return Locale.getAvailableLocales()
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    fun getSystemVersion(): String? {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    fun getSystemModel(): String? {
        return Build.MODEL
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    fun getDeviceBrand(): String? {
        return Build.BRAND
    }

    /**
     * 获取是否深色模式
     * 浅色：0x11
     * @param context 当前运行上下文
     */
    fun isDarkMode(context: Context): Boolean {
        val mode = context.applicationContext.resources.configuration.uiMode
        return mode == 0x21
    }

    /**
     * 设置状态栏和导航栏字体颜色
     * @param activity 应用界面活动
     * @param isDark 是否深色，true则为深色图标
     */
    fun setBarDarkMode(activity: Activity, isDark: Boolean) {
        BarUtil.setMode(activity, isDark)
    }

    /**
     * 全屏显示，隐藏系统栏
     * @param activity 当前界面Activity
     */
    fun hideBar(activity: Activity) {
        val lp: WindowManager.LayoutParams = activity.window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
        }
        activity.window.attributes = lp
        SystemBarUtil.hideSystemBar(activity)
    }

    /**
     * 全屏显示，不隐藏系统栏
     * @param activity 当前界面Activity
     */
    fun underBar(
        activity: Activity,
        drawable: Drawable,
        contentView: View?,
        contentDrawable: Drawable? = null
    ) {
        val d = contentDrawable ?: activity.window.decorView.background
        contentView?.background = d
        activity.window.setBackgroundDrawable(drawable)
    }

    /**
     * 判断服务是否运行
     * @param serviceName 服务绝对名称，包含包名
     * @return 是否运行
     */
    fun isServiceRunning(context: Context, serviceName: String): Boolean {
        return ProcessUtil.isServiceRunning(context, serviceName)
    }

    /**
     * 是否主进程
     */
    fun isMainProcess(context: Context): Boolean {
        return ProcessUtil.isMainProcess(context)
    }

    /**
     * 重启应用
     */
    fun restart(activity: Activity) {
        RestartUtil.restart(activity, 1000)
    }

    /**是否调试模式*/
    fun loggable(context: Context): Boolean {
        return VerifyUtil.loggable(context)
    }

}