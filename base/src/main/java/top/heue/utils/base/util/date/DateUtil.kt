package top.heue.utils.base.util.date

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    val current get() = Date()
    val normal get() = format(System.currentTimeMillis(), DateMode.DATE_NORMAL)

    fun format(timeMills: Long) = Date(timeMills)

    fun current(vararg mode: DateMode, locale: Locale = Locale.CHINA): String = run {
        val modeText = StringBuffer("")
        mode.forEach { modeText.append(it) }
        format(System.currentTimeMillis(), modeText.toString(), locale)
    }

    fun format(timeMills: Long, vararg mode: DateMode, locale: Locale = Locale.CHINA): String =
        run {
            val modeText = StringBuffer("")
            mode.forEach { modeText.append(it.pattern) }
            format(timeMills, modeText.toString(), locale)
        }

    fun format(timeMills: Long, mode: String, locale: Locale = Locale.CHINA): String = run {
        val formatter = formatter(mode, locale)
        formatter.format(timeMills)
    }

    private fun formatter(mode: String, locale: Locale) = SimpleDateFormat(mode, locale)

    enum class DateMode(val pattern: String) {
        YEAR_SHORT("yy"),
        YEAR("yyyy"),
        MONTH("MM"),
        MONTH_WHAT("MMM"),
        DAY("dd"),
        DAY_YEAR("D"),
        DAY_WHAT("E"),
        HOUR_24("HH"),
        HOUR_12("hh"),
        MINUTE("mm"),
        SECOND("ss"),
        MILLISECOND("SSS"),
        DATE_YMD("yyyyMMdd"),
        DATE_NORMAL("yyyyMMddHHmmss"),
        DATE_ALL("yyyyMMddHHmmssSSS")
    }

    class Date(var time: Long = System.currentTimeMillis()) {

        val year = format(time, DateMode.YEAR)
        val month = format(time, DateMode.MONTH)
        val day = format(time, DateMode.DAY)

        val hour = format(time, DateMode.HOUR_24)
        val minute = format(time, DateMode.MINUTE)
        val second = format(time, DateMode.SECOND)

        val mills = format(time, DateMode.MILLISECOND)
    }
}