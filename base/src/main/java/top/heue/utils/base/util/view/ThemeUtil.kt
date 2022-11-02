package top.heue.utils.base.util.view

import android.app.Activity
import android.util.TypedValue

object ThemeUtil {

    fun getAttrValue(activity: Activity, resId: Int): TypedValue {
        val value = TypedValue()
        activity.theme.resolveAttribute(
            resId,
            value,
            true
        )
        return value
    }
}