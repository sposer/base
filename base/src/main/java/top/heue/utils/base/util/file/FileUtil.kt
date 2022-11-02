package top.heue.utils.base.util.file

import android.annotation.SuppressLint
import android.os.Build
import android.os.Environment
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


object FileUtil {
    @SuppressLint("AnnotateVersionCheck")
    val SDK_STATE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    //根目录
    @SuppressLint("NewApi")
    val PATH =
        if (SDK_STATE) Environment.getStorageDirectory()
        else Environment.getExternalStorageDirectory()

    //存储的状态，是否可写
    @SuppressLint("NewApi")
    val STATE: Boolean = if (SDK_STATE) {
        Environment.isExternalStorageManager()
    } else {
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    fun inputStreamToString(inputStream: InputStream): String {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }
        inputStream.close()
        return result.toString(StandardCharsets.UTF_8.name())
    }

    fun read(path: String, charset: Charset = Charset.defaultCharset()): String {
        return read(File(path), charset)
    }

    fun read(file: File, charset: Charset = Charset.defaultCharset()): String {
        val result = StringBuffer("")
        val fis = FileInputStream(file)
        val isr = InputStreamReader(fis, charset)
        val br = BufferedReader(isr)
        var line: String?
        while (br.readLine().also { line = it } != null) {
            result.append("\n$line")
        }
        br.close()
        return result.toString()
    }

    fun write(
        path: String,
        content: String,
        append: Boolean = true,
        charset: Charset = Charset.defaultCharset()
    ) {
        write(File(path), content, append, charset)
    }

    fun write(
        to: File,
        from: InputStream,
        append: Boolean = true
    ) {
        if (to.exists()) to.delete()
        to.parent?.let {
            val p = File(it)
            if (!p.exists()) p.createNewFile()
        }
        to.createNewFile()
        val fos = FileOutputStream(to, append)
        val buffer = ByteArray(1024)
        val bos = BufferedOutputStream(fos)
        var len: Int
        while (from.read(buffer).also { len = it } != -1) {
            bos.write(buffer, 0, len)
        }
        bos.flush()
        bos.close()
        from.close()
    }

    fun write(
        file: File,
        content: String,
        append: Boolean = true,
        charset: Charset = Charset.defaultCharset()
    ) {
        if (!file.exists()) file.createNewFile()
        val fos = FileOutputStream(file, append)
        val osw = OutputStreamWriter(fos, charset)
        val bw = BufferedWriter(osw)
        bw.write(content)
        bw.flush()
        bw.close()
    }

    fun delete(file: File) {
        if (file.isFile) {
            file.delete()
            return
        }
        val files = file.listFiles()
        files?.forEach {
            delete(it)
        }
        file.delete()
    }
}