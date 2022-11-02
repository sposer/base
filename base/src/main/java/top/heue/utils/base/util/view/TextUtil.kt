package top.heue.utils.base.util.view

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.TextView
import androidx.annotation.ColorInt


/** RelativeSizeSpan, ForegroundColorSpan, ClickableSpan
 * TextPaint:
 * setFakeBoldText(true);//加粗
 * setUnderlineText(false);//去下划线
 * setColor(Color.MAGENTA);//字体颜色
 *
 */
class TextUtil {

    constructor()

    constructor(textView: TextView) : this() {
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = 0
        this.textView = textView
    }

    private lateinit var textView: TextView
    private var ssb: SpannableStringBuilder = SpannableStringBuilder()

    /**
     * @param str 添加的字符
     * @param objects CharacterStyle
     */
    fun append(str: String?, vararg objects: Any): TextUtil {
        if (str == null || str.isEmpty()) {
            return this
        }
        if (objects.isEmpty()) {
            ssb.append(str)
            return this
        }
        val begin = ssb.length
        ssb.append(str)
        val end = ssb.length
        for (element in objects) {
            ssb.setSpan(element, begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return this
    }

    fun inject(textView: TextView) {
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = 0
        textView.text = ssb
    }

    fun inject() {
        if (this::textView.isInitialized)
            textView.text = ssb
    }

    abstract class Clickable : ClickableSpan() {
        open var isUnderlineText = false
        @ColorInt
        open var textColor: Int = Color.GRAY
        open var isFakeBoldText = false

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = this.isUnderlineText
            ds.color = this.textColor
            ds.isFakeBoldText = this.isFakeBoldText
        }
    }
}

val TextView.textUtil: TextUtil get() = TextUtil(this)