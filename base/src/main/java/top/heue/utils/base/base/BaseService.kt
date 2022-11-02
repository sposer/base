package top.heue.utils.base.base

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

open class BaseService<S : BaseService<S>> : Service() {
    //private val className = this.javaClass.simpleName

    private lateinit var binder: MainBinder<S>

    class MainBinder<S>(val service: S) : Binder()

    override fun onCreate() {
        super.onCreate()
        binder = MainBinder(cast())
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    @Suppress("UNCHECKED_CAST")
    fun cast(): S {
        return this as S
    }
}