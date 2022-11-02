package top.heue.utils.base.util.view

import android.app.Activity
import android.app.Application
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

class KeyboardListener(rootView: View) {

    constructor(rootView: View, activity: AppCompatActivity) : this(rootView) {
        activity.lifecycle.addObserver(activityLifecycleObserver)
    }

    private var visibleHeight = 0
    private val activityLifecycleObserver by lazy {
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                unRegedit()
            }
        }
    }
    private val viewTreeObserver: ViewTreeObserver by lazy { rootView.viewTreeObserver }
    private var callback: ICallback? = null
    private val keyboardListener by lazy {
        ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val height: Int = r.height()
            if (visibleHeight == 0) {
                visibleHeight = height
                return@OnGlobalLayoutListener
            }
            if (height == visibleHeight) {
                return@OnGlobalLayoutListener
            }
            if (visibleHeight - height > 200) {
                //L.e("显示")
                visibleHeight = height
                callback?.onShow()
                return@OnGlobalLayoutListener
            }
            if (height - visibleHeight > 200) {
                //L.e("隐藏")
                visibleHeight = height
                callback?.onHide()
                return@OnGlobalLayoutListener
            }
        }
    }

    fun regedit(callback: ICallback) {
        this.callback = callback
    }

    fun unRegedit() {
        if (viewTreeObserver.isAlive)
            viewTreeObserver.removeOnGlobalLayoutListener(keyboardListener)
    }

    interface ICallback {
        fun onShow()
        fun onHide()
    }
}