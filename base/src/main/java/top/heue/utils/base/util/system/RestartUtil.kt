package top.heue.utils.base.util.system

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper

object RestartUtil {

    fun restart(activity: Activity, delay: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            val i = activity.packageManager.getLaunchIntentForPackage(activity.packageName)!!
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.applicationContext.startActivity(i)
        }, delay)
        /*val reIntent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
        val intent = PendingIntent.getActivity(
            activity,
            0,
            reIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val manager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent)
        //activity.finish()
        android.os.Process.killProcess(android.os.Process.myPid())
        //System.runFinalizersOnExit(true);
        //exitProcess(0)*/
    }
}