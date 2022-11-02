package top.heue.utils.base.util.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

class VerifyUtil {

    //校验自身签名与预设字符串是否相同
    private static final int VALID = 0;
    private static final int INVALID = 1;

    public static int checkAppSignature(Context context) {
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                byte[] signatureBytes = signature.toByteArray();
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("移除这里", "本应用签名字符串:" + currentSignature);
                //compare signatures
                /*if (SIGNATURE.equals(currentSignature)){
                    return VALID;
                };*/
            }
        } catch (Exception e) {
            Log.e("VerifyUtil", e.getMessage());
        }
        return INVALID;
    }

    //验证安装程序
    private static final String PLAY_STORE_APP_ID = "com.android.vending";

    public static boolean verifyInstaller(final Context context) {
        final String installer = context.getPackageManager()
                .getInstallerPackageName(context.getPackageName());
        return installer != null
                && installer.startsWith(PLAY_STORE_APP_ID);
    }

    //环境检查
    private static String getSystemProperty(String name)
            throws Exception {
        @SuppressLint("PrivateApi") Class systemPropertyClazz = Class
                .forName("android.os.SystemProperties");
        return (String) systemPropertyClazz.getMethod("get", new Class[]{String.class})
                .invoke(systemPropertyClazz, new Object[]{name});
    }

    //获取虚拟机中运行的一些参数
    public static boolean checkEmulator() {
        try {
            boolean goldfish = getSystemProperty("ro.hardware").contains("goldfish");
            boolean emu = getSystemProperty("ro.kernel.qemu").length() > 0;
            boolean sdk = getSystemProperty("ro.product.model").equals("sdk");
            if (emu || goldfish || sdk) {
                return true;
            }
        } catch (Exception e) {
            Log.e("VerifyUtil", e.getMessage());
        }
        return false;
    }

    //检查是否debug模式
    public static boolean checkDebuggable(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * 是否为调试模式
     *
     * @param context 上下文
     */
    public static boolean loggable(Context context) {
        return checkDebuggable(context)
                && !checkEmulator()
                && !verifyInstaller(context);
    }
}
