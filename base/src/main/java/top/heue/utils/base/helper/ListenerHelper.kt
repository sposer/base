package top.heue.utils.base.helper

import android.view.View

/**
 * 放在bean中的操作事件
 */
class ListenerHelper {

    constructor()
    constructor(block: ListenerHelper.() -> Unit) {
        block.invoke(this)
    }

    private var onClick: onClick = { _, _ -> }
    private var onLongClick: onLongClick = { _, _ -> false }

    fun onClick(block: onClick) {
        onClick = block
    }

    fun onLongClick(block: onLongClick) {
        onLongClick = block
    }

    fun asViewOnClickListener() = run {
        View.OnClickListener { v -> v?.let { onClick(it, null) } }
    }

    fun asViewOnLongClickListener() = run {
        View.OnLongClickListener { v -> v?.let { onLongClick(v, null) } ?: false }
    }

    fun onClick(view: View, any: Any? = null) = onClick.invoke(view, any)
    fun onLongClick(view: View, any: Any? = null) = onLongClick.invoke(view, any)
}

typealias onClick = (view: View, any: Any?) -> Unit
typealias onLongClick = (view: View, any: Any?) -> Boolean