package top.heue.utils.base.helper

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.content.UriPermission
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.documentfile.provider.DocumentFile
import java.io.*


class SAFHelper(private val activity: ComponentActivity) {

    companion object {
        /**
         * inputStream转string
         */
        fun inputStreamToString(inputStream: InputStream): String {
            val stringBuilder = StringBuilder("")
            inputStream.use { stream ->
                BufferedReader(
                    InputStreamReader(stream)
                ).use { reader ->
                    var line: String?
                    try {
                        while (reader.readLine().also { line = it } != null) {
                            stringBuilder.appendLine(line)
                        }
                    } catch (e: Exception) {
                    }
                }
            }
            return stringBuilder.toString()
        }
    }

    /**
     * 读取uri文件为stream
     */
    fun readFileToString(uri: Uri): String {
        val stream = activity.contentResolver.openInputStream(uri) ?: return ""
        return inputStreamToString(stream)
    }

    /**
     * 复制uri文件到File
     */
    fun copyFile(uri: Uri, file: File): File {
        activity.contentResolver.openInputStream(uri).use { inputStream ->
            val bis = BufferedInputStream(inputStream, 16)
            val bos = BufferedOutputStream(FileOutputStream(file, false), 16)
            var size = 0
            val buffer = ByteArray(10240)
            while (bis.read(buffer).also { size = it } != -1) {
                bos.write(buffer, 0, size)
            }
            bos.flush()
            bis.close()
            bos.close()
        }
        return file
    }

    /**
     * 获取文件名称
     */
    fun getFileRealNameFromUri(fileUri: Uri?): String {
        if (fileUri == null) return ""
        val documentFile = DocumentFile.fromSingleUri(activity, fileUri) ?: return ""
        return documentFile.name!!
    }

    /** 获取文件路径 */
    fun getFilePath(fileUri: Uri?): String {
        if (fileUri == null) return ""
        var path = ""
        if ("file".equals(fileUri.scheme, ignoreCase = true)) { //使用第三方应用打开
            //String path结果
            path = fileUri.path.toString()
        } else {
            if (DocumentsContract.isDocumentUri(activity, fileUri)) {
                val docId = DocumentsContract.getDocumentId(fileUri)
                if ("com.android.providers.media.documents" == fileUri.authority) {
                    val id = docId.split(":").toTypedArray()[1]
                    val selection = MediaStore.Images.Media._ID + "=" + id
                    path = getPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        selection
                    ).toString()
                } else if ("com.android.providers.downloads.documents" == fileUri.authority) {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(docId)
                    )
                    path = getPath(contentUri, "").toString()
                }
            } else if ("content".equals(fileUri.scheme, ignoreCase = true)) {
                path = getPath(fileUri, "").toString()
            }
        }
        return path
    }

    private fun getPath(uri: Uri, selection: String): String {
        var path = ""
        val cursor: Cursor =
            activity.contentResolver.query(uri, null, selection, null, null)!!
        if (cursor.moveToFirst()) {
            val p = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            if (p >= 0)
                path = cursor.getString(p)
        }
        cursor.close()
        return path
    }

    /**
     * I,O详细参考ActivityResultContracts
     */
    class Grant<I, O>(private val activity: ComponentActivity) {
        private var launcher: ActivityResultLauncher<I>? = null
        private var onResult: ((out: O?) -> Unit)? = null
        private val packageName: String = activity.packageName
        private val takeFlags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        companion object {
            fun <I, O> with(activity: ComponentActivity) = Grant<I, O>(activity)
        }

        /**
         * 设置访问的目标
         */
        fun target(contract: ActivityResultContract<I, O>) = apply {
            launcher = activity.registerForActivityResult(contract) {
                onResult?.invoke(it)
            }
        }

        /**
         * 事件结果回调
         */
        fun onResult(onResult: (((out: O?) -> Unit))) = apply {
            this.onResult = onResult
        }

        /**
         * 持有该Uri
         */
        private fun takeUriPermission(activity: ComponentActivity, treeUri: Uri) {
            activity.contentResolver.takePersistableUriPermission(treeUri, takeFlags)
        }

        /**
         * 持有该Uri
         */
        private fun takeUri(uri: Uri) {
            //持久化当前Uri权限
            activity.grantUriPermission(packageName, uri, takeFlags)
            takeUriPermission(activity, uri)
        }

        /**
         * 释放以前授权过的Uri
         */
        fun releaseAllGrantedUri() {
            //释放之前的所有文件权限
            val pup = listUri(activity.contentResolver)
            for (up in pup) {
                activity.revokeUriPermission(up.uri, takeFlags)
            }
        }

        /**
         * 释放以前授权过的Uri
         */
        fun releaseGrantedUri(uri: Uri) {
            //释放指定文件权限
            activity.revokeUriPermission(uri, takeFlags)
        }

        /**
         * 开始请求操作
         */
        fun get(input: I? = null) = run {
            if (launcher == null) throw UninitializedPropertyAccessException("未调用target()设置启动目的")
            launcher!!.launch(input)
        }

        /**
         * 列出所有授权过的Uri
         */
        fun listUri(contentResolver: ContentResolver): MutableList<UriPermission> =
            contentResolver.persistedUriPermissions
    }
}