package top.heue.utils.base.util.system

import android.app.ActivityManager
import android.content.Context
import android.os.Process


object ProcessUtil {
    /**
     * 判断服务是否运行
     * @param serviceName 服务绝对名称，包含包名
     * @return 是否运行
     */
    fun isServiceRunning(context: Context, serviceName: String): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceName == service.service.className) {
                return true
            }
        }
        return false
    }

    /**
     * 判断是否主进程
     * @param context 上下文
     * @return true是主进程
     */
    fun isMainProcess(context: Context?): Boolean {
        return isPidOfProcessName(context!!, Process.myPid(), getMainProcessName(context))
    }

    /**
     * 获取主进程名
     * @param context 上下文
     * @return 主进程名
     */
    private fun getMainProcessName(context: Context): String? {
        return context.packageManager.getApplicationInfo(context.packageName, 0).processName
    }

    /**
     * 判断该进程ID是否属于该进程名
     * @param context
     * @param pid 进程ID
     * @param pName 进程名
     * @return true属于该进程名
     */
    private fun isPidOfProcessName(context: Context, pid: Int, pName: String?): Boolean {
        if (pName == null) return false
        var isMain = false
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //遍历所有进程
        for (process in am.runningAppProcesses) {
            if (process.pid == pid) {
                //进程ID相同时判断该进程名是否一致
                if (process.processName == pName) {
                    isMain = true
                }
                break
            }
        }
        return isMain
    }

    /**
     * 获取当前App所有进程
     */
    fun getRunningAppProcesses(context: Context): List<ActivityManager.RunningAppProcessInfo> {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return am.runningAppProcesses
    }
}