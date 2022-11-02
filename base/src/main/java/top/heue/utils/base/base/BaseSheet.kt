package top.heue.utils.base.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import top.heue.utils.base.R
import top.heue.utils.base.util.view.ScreenUtil

open class BaseSheet : BottomSheetDialogFragment() {
    private val className = this.javaClass.simpleName

    val coordinator: CoordinatorLayout by lazy { dialog?.findViewById(R.id.coordinator)!! }
    val container: FrameLayout by lazy { dialog?.findViewById(R.id.design_bottom_sheet)!! }
    val behavior: BottomSheetBehavior<*> by lazy { BottomSheetBehavior.from(container) }
    var isFullScreen = false
    var isBackgroundTransient = false
    var isFragmentHideable = true
    var isFragmentDraggable = true
    var isFragmentCancelable = true
    var isFragmentSkipCollapse = false
    var isCanceledOnTouchOutside = true
    var isAutoSize = false
    var isOutsideTransient = false
    var backgroundDimAmount = 0.5f
        get() = if (isOutsideTransient) 0f
        else field
    var peekHeight = 0
        get() {
            if (field == 0)
                field = ScreenUtil.dpToPx(requireContext(), 280f).toInt()
            return field
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //生命周期日志
        Log.d(className, "ON_ATTACH")
        lifecycle.addObserver(LifecycleEventObserver { _, event ->
            Log.d(className, event.name)
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        //折叠高度
        behavior.peekHeight = peekHeight
        //适合页面大小
        if (!isAutoSize) container.minimumHeight = behavior.peekHeight
        //页面撑满
        if (isFullScreen) {
            behavior.maxHeight = ScreenUtil.getScreenHeight(requireActivity())
            container.layoutParams.height = behavior.maxHeight
        }
        //防止点击穿透
        container.setOnTouchListener { _, _ -> true }
        //默认单个callback
        behavior.addBottomSheetCallback(callback)
        dialog?.window?.run {
            //背景透明
            if (isBackgroundTransient) setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDimAmount(backgroundDimAmount)
        }
        //点击外部是否退出
        dialog?.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
        //是否可隐藏
        behavior.isHideable = isFragmentHideable
        //是否跳过折叠
        behavior.skipCollapsed = isFragmentSkipCollapse
        //是否可以拖拽
        behavior.isDraggable = isFragmentDraggable
        //是否可退出
        isCancelable = isFragmentCancelable
    }

    override fun show(manager: FragmentManager, tag: String?) {
        var t = tag
        if (manager.findFragmentByTag(t) != null) {
            t = "$tag-${System.currentTimeMillis()}"
        }
        super.show(manager, t)
    }

    private val callback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            onStateChangeCallback?.invoke(bottomSheet, newState)
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            onSlideCallback?.invoke(bottomSheet, slideOffset)
        }
    }

    private var onStateChangeCallback: ((bottomSheet: View, newState: Int) -> Unit)? = null
    fun onStateChanged(block: (bottomSheet: View, newState: Int) -> Unit) {
        onStateChangeCallback = block
    }

    private var onSlideCallback: ((bottomSheet: View, slideOffset: Float) -> Unit)? = null
    fun onSlide(block: (bottomSheet: View, slideOffset: Float) -> Unit) {
        onSlideCallback = block
    }

    /** 展开*/
    fun expand() {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    /** 折叠*/
    fun collapse() {
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}