package top.heue.utils.base.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleEventObserver

open class BaseFragment : Fragment() {

    private val className = this.javaClass.simpleName
    private val bundleFragmentsKey = "android:support:fragments";
    override fun onAttach(context: Context) {
        super.onAttach(context)
        //生命周期日志
        Log.d(className, "ON_ATTACH")
        lifecycle.addObserver(LifecycleEventObserver { _, event ->
            Log.d(className, event.name)
        })
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(className, "ON_DETACH")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(className, "ON_DESTROY_VIEW")
    }

    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(className, "ON_SAVE_INSTANCE_STATE")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(className, "ON_VIEW_STATE_RESTORED")
    }

    /*fun Context.startFragment(
        fragment: Fragment,
        title: String = "",
        tag: String = "",
        backAble: Boolean = true
    ) {
        FragmentContainer
            .with(fragment)
            .title(title)
            .tag(tag)
            .back(backAble)
            .start(this)
    }*/
}