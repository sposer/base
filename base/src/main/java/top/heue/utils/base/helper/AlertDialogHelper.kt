package top.heue.utils.base.helper

import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListAdapter
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class AlertDialogHelper {

    constructor(context: Context, block: AlertDialogHelper.() -> Unit) {
        this.context = context
        block.invoke(this)
    }

    constructor(
        activity: AppCompatActivity,
        block: AlertDialogHelper.() -> Unit
    ) : this(activity as Context, block) {
        activity.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    dialog?.dismiss()
                }
            }
        })
    }

    private lateinit var context: Context
    private val builder: AlertDialog.Builder by lazy { AlertDialog.Builder(context)}
    private var dialog: AlertDialog? = null
    private var onShow: dialogBlock? = null
    private var onCancel: dialogBlock? = null
    private var onDismiss: dialogBlock? = null
    var root: ViewGroup? = null
        private set
    private var width: Int? = null
    private var height: Int? = null
    private var background: Drawable? = null
    private var backgroundRes: Int? = null
    private var gravity: Int? = null
    private var paddingStart: Int? = null
    private var paddingEnd: Int? = null
    private var paddingTop: Int? = null
    private var paddingBottom: Int? = null

    fun title(text: CharSequence?) = apply { builder.setTitle(text) }
    fun title(@StringRes res: Int, vararg formatArgs: Any) = apply {
        title(context.getString(res, formatArgs))
    }

    fun message(text: CharSequence?) = apply { builder.setMessage(text) }
    fun message(@StringRes res: Int, vararg formatArgs: Any) = apply {
        message(context.getString(res, formatArgs))
    }

    fun list(items: Array<CharSequence>, listener: clickBlock) =
        apply { builder.setItems(items, listener) }

    fun list(@ArrayRes res: Int, listener: clickBlock) = apply { builder.setItems(res, listener) }

    fun singleChoice(items: Array<CharSequence>, checked: Int, listener: clickBlock) =
        apply {
            builder.setSingleChoiceItems(items, checked, listener)
        }

    fun singleChoice(@ArrayRes items: Int, checked: Int, listener: clickBlock) =
        apply {
            builder.setSingleChoiceItems(items, checked, listener)
        }

    fun singleChoice(adapter: ListAdapter, checked: Int, listener: clickBlock) =
        apply {
            builder.setSingleChoiceItems(adapter, checked, listener)
        }

    /**
     * @param column 包含要在标签中显示的字符串的光标上的列名
     */
    fun singleChoice(cursor: Cursor, checked: Int, column: String, listener: clickBlock) =
        apply {
            builder.setSingleChoiceItems(cursor, checked, column, listener)
        }

    fun multiChoice(items: Array<CharSequence>, checked: BooleanArray, listener: multiBlock) =
        apply {
            builder.setMultiChoiceItems(items, checked, listener)
        }

    fun multiChoice(@ArrayRes res: Int, checked: BooleanArray, listener: multiBlock) = apply {
        builder.setMultiChoiceItems(res, checked, listener)
    }

    /**
     * @param isCheckedColumn 指定光标上用于确定是否选中复选框的列名。它必须返回一个整数值，其中1表示已选中，0表示未选中。
     *
     */
    fun multiChoice(
        cursor: Cursor,
        isCheckedColumn: String,
        labelColumn: String,
        listener: multiBlock
    ) = apply {
        builder.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener)
    }

    /**
     * 添加显示的界面
     */
    fun layout(view: View) = apply {
        if (root == null) {
            val v = LinearLayout(context)
            v.setPadding(8, 8, 8, 8)
            v.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1F)
            v.orientation = LinearLayout.VERTICAL
            root = v
        }
        root!!.addView(view)
    }

    fun width(width: Int) = apply { this.width = width }
    fun height(height: Int) = apply { this.height = height }

    /**
     * 正面
     */
    fun positive(text: CharSequence, listener: clickBlock) = apply {
        builder.setPositiveButton(text, listener)
    }

    fun positive(@StringRes res: Int, vararg formatArgs: Any, listener: clickBlock) =
        apply {
            positive(context.getString(res, formatArgs), listener)
        }

    fun positiveIcon(icon: Drawable) = apply { builder.setPositiveButtonIcon(icon) }

    /**
     * 反面
     */
    fun negative(text: CharSequence, listener: clickBlock) = apply {
        builder.setNegativeButton(text, listener)
    }

    fun negative(@StringRes res: Int, vararg formatArgs: Any, listener: clickBlock) =
        apply {
            negative(context.getString(res, formatArgs), listener)
        }

    fun negativeIcon(icon: Drawable) = apply { builder.setPositiveButtonIcon(icon) }

    /**
     * 中立
     */
    fun neutral(text: CharSequence, listener: clickBlock) = apply {
        builder.setNeutralButton(text, listener)
    }

    fun neutral(@StringRes res: Int, vararg formatArgs: Any, listener: clickBlock) =
        apply {
            neutral(context.getString(res, formatArgs), listener)
        }

    fun neutralIcon(icon: Drawable) = apply { builder.setPositiveButtonIcon(icon) }

    fun cancelable(cancelable: Boolean) = apply { builder.setCancelable(cancelable) }
    fun icon(icon: Drawable) = apply { builder.setIcon(icon) }
    fun icon(@DrawableRes res: Int) = apply { builder.setIcon(res) }
    fun background(background: Drawable) = apply { this.background = background }
    fun background(@DrawableRes res: Int) = apply { this.backgroundRes = res }
    fun gravity(gravity: Int) = apply { this.gravity = gravity }
    fun padding(
        paddingStart: Int? = null,
        paddingBottom: Int? = null,
        paddingEnd: Int? = null,
        paddingTop: Int? = null
    ) = apply {
        this.paddingStart = paddingStart
        this.paddingBottom = paddingBottom
        this.paddingEnd = paddingEnd
        this.paddingTop = paddingTop
    }

    fun onShow(listener: dialogBlock) = apply { onShow = listener }
    fun onCancel(listener: dialogBlock) = apply { onCancel = listener }
    fun onDismiss(listener: dialogBlock) = apply { onDismiss = listener }

    /**
     * 自定义view需show后才能设置内部监听
     */
    fun show(): AlertDialog {
        val dialog = builder.create()
        dialog.setOnShowListener { onShow?.invoke(this, it) }
        dialog.setOnCancelListener { onCancel?.invoke(this, it) }
        dialog.setOnDismissListener { onDismiss?.invoke(this, it) }
        root?.let { dialog.setView(root) }
        dialog.show()
        //设置宽高等
        val win = dialog.window ?: return dialog
        val attr = win.attributes
        attr.width = width ?: attr.width
        attr.height = height ?: attr.height
        gravity?.let { win.setGravity(it) }
        val decor = win.decorView
        paddingTop = paddingTop ?: decor.paddingTop
        paddingBottom = paddingBottom ?: decor.paddingBottom
        paddingStart = paddingStart ?: decor.paddingStart
        paddingEnd = paddingEnd ?: decor.paddingEnd
        decor.setPaddingRelative(paddingStart!!, paddingTop!!, paddingEnd!!, paddingBottom!!)
        background?.let { win.setBackgroundDrawable(it) }
        backgroundRes?.let { win.setBackgroundDrawableResource(it) }
        win.attributes = attr
        this.dialog = dialog
        return dialog
    }


    companion object {
        const val MATCH_PARENT = WindowManager.LayoutParams.MATCH_PARENT
        const val WRAP_CONTENT = WindowManager.LayoutParams.WRAP_CONTENT
    }

    ///*******扩展*******///
    var inputContent: HashMap<CharSequence, CharSequence?> = HashMap()
        private set

    fun input(
        tag: CharSequence = "default",
        hint: CharSequence? = null,
        text: CharSequence? = null,
        editable: Boolean = true
    ) = apply {
        val inputView = EditText(context)
        inputView.hint = hint
        inputView.setText(text)
        inputView.isEnabled = editable
        inputView.doOnTextChanged { text, _, _, _ ->
            inputContent[tag] = text
        }
        layout(inputView)
    }

    fun getContent(tag: CharSequence = "default"): CharSequence? = inputContent[tag]
}

typealias clickBlock = (dialog: DialogInterface, which: Int) -> Unit
typealias dialogBlock = (adh: AlertDialogHelper, dialog: DialogInterface) -> Unit
typealias multiBlock = (dialog: DialogInterface, which: Int, isChecked: Boolean) -> Unit