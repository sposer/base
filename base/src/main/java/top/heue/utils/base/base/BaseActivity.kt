package top.heue.utils.base.base

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleEventObserver
import top.heue.utils.base.util.system.BarUtil
import top.heue.utils.base.util.system.SystemUtil
import java.lang.reflect.Method

open class BaseActivity : AppCompatActivity() {

    private val className = this.javaClass.simpleName
    var isAutoDarkBar = true

    protected open fun onCreate() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //生命周期日志
        lifecycle.addObserver(LifecycleEventObserver { _, event ->
            Log.d(className, event.name)
        })
        onCreate()
        if (isAutoDarkBar) {
            val mode = !SystemUtil.isDarkMode(this)
            BarUtil.setMode(this, mode)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d(className, "ON_DETACHED_FROM_WINDOW")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(className, "ON_SAVE_INSTANCE_STATE")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(className, "onRestoreInstanceState")
    }

    /**
     * 绑定ViewModel
     */


    //Service Cast
    /**
     * 通过IBinder获得Service实例
     */
    /*@Suppress("UNCHECKED_CAST")
    fun <T> getService(service: IBinder): T {
        val binder = service as BaseService.MainBinder<*>
        return binder.service as T
    }*/

    //Service Bind
    /*inline fun <reified C> bindService(connection: ServiceConnection) {
        val i = Intent(this, C::class.java)
        bindService(i, connection, BIND_AUTO_CREATE)
    }*/

    /**
     * Init A ServiceConnection
     */
    /*fun getSCon(onSCon: OnSCon?, onSDisCon: OnSDisCon?): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                onSCon?.invoke(name, service)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                onSDisCon?.invoke(name)
            }

        }
    }*/

    /**
     * 解决不显示menu icon的问题
     * @param menu
     * @param flag
     */
    fun setIconsVisible(menu: Menu?, flag: Boolean) {
        //判断menu是否为空
        if (menu != null) {
            try {
                //如果不为空,就反射拿到menu的setOptionalIconsVisible方法
                val method: Method = menu.javaClass
                    .getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
                //暴力访问该方法
                method.isAccessible = true
                //调用该方法显示icon
                method.invoke(menu, flag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

/*
typealias OnSCon = (name: ComponentName, service: IBinder) -> Unit
typealias OnSDisCon = (name: ComponentName) -> Unit*/
