package top.heue.utils.base.helper

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import top.heue.utils.base.base.BaseService


class BindHelper(private val context: Context, targetService: Class<*>) {


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            onCon?.invoke(name, service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            onDisCon?.invoke(name)
        }

    }

    private val intent = Intent(context, targetService)

    private var onCon: onCon? = null
    private var onDisCon: onDisCon? = null

    fun onConnected(onCon: onCon) = apply {
        this.onCon = onCon
    }

    fun onDisConnected(onDisCon: onDisCon) = apply {
        this.onDisCon = onDisCon
    }

    fun start(): Boolean =
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)

    fun stop() = context.unbindService(connection)

    companion object {
        /**
         * 通过IBinder获得Service实例
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> getService(service: IBinder): T {
            val binder = service as BaseService.MainBinder<*>
            return binder.service as T
        }

        /**
         * 判断某个服务是否在运行
        fun isServiceWork(context: Context, serviceName: String): Boolean {
            var isWork = false
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val list = activityManager.getRunningServices(5)
            list.forEach {
                val name = it.service.className
                if (name == serviceName) {
                    isWork = true
                }
            }
            return isWork
        }*/
    }
}

typealias onCon = (name: ComponentName, service: IBinder) -> Unit
typealias onDisCon = (name: ComponentName) -> Unit