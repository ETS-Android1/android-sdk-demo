package com.geomoby.demoapp.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.util.Log
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.*
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.collections.ArrayList


object LogProvider {
    private const val TAG = "LogProvider"
    const val TEMPLATE = "yyyy.MM.dd HH:mm:ss"
    private const val SUPPORT_EMAIL = "vikravch@ukr.net"
    private const val FILE_NAME = "logs.txt"

    //File access and location
    private object FileAccess{
        private fun getFileDir(context: Context) = context.filesDir?.absolutePath

        fun getFilePath(context: Context) = File(
            getFileDir(context),
            FILE_NAME
        )

        fun getLogFilePath(context: Context) = File(
            getFileDir(context),
            FILE_NAME
        ).absolutePath
    }

    // Clear log file
    object ClearFile{
        fun clear(context: Context, filePath:String=FILE_NAME) {
            try {
                val outputStreamWriter =
                    if(filePath==""){
                        OutputStreamWriter(FileOutputStream(
                            FileAccess.getFilePath(
                                context
                            ).absolutePath,false))
                    } else {
                        OutputStreamWriter(FileOutputStream(FileAccess.getLogFilePath(context),false))
                    }
                outputStreamWriter.write("")
                outputStreamWriter.close()
            } catch (e: IOException) {
                Log.e(TAG,"Exception File write failed: $e")
            }
        }
    }

    //Write to log file
    object WriteFile{
        fun writeToLogFile(context: Context, message: String){
            writeToFile(message,context, FileAccess.getLogFilePath(context),append = true)
        }

        fun saveListToLogFile(context: Context, recordsList: ArrayList<String>?) {
            ClearFile.clear(context, FileAccess.getLogFilePath(context))
            val builder = StringBuilder()
            recordsList?.forEach { line->
                builder.append(line+"\n")
            }
            writeToLogFile(context, builder.toString())
        }

        private fun writeToFile(data: String, context: Context, filePath: String = "", append:Boolean = false) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val outputStreamWriter =
                        if(filePath.isEmpty()){
                            OutputStreamWriter(FileOutputStream(
                                FileAccess.getFilePath(
                                    context
                                ).absolutePath,append))
                        } else {
                            OutputStreamWriter(FileOutputStream(
                                filePath,append))
                        }
                    val bw = BufferedWriter(outputStreamWriter)
                    bw.write(data)
                    bw.newLine()
                    bw.close()
                    outputStreamWriter.close()
                } catch (e: IOException) {
                    Log.e(TAG,"Exception File write failed: $e")
                }
            }
        }
    }

    // Read from file
    object ReadFile{
        private const val LIMIT = 400

        fun readFromLogFile(context: Context):String{
            return readFromFile(context, FileAccess.getLogFilePath(context))
        }

        fun readListFromLogFile(context: Context): ArrayList<String>? {
            return readFromFileToList(FileAccess.getLogFilePath(context))?.reversed().limit(LIMIT)
        }

        private fun readFromFile(context: Context, filePath: String = "", linesLimit:Int=LIMIT): String {
            var ret = ""
            try {
                val inputStream =
                    if(filePath.isEmpty()){
                        FileInputStream(
                            FileAccess.getFilePath(
                                context
                            )
                        )
                    } else {
                        FileInputStream(filePath)
                    }

                if (inputStream != null) {
                    val inputStreamReader = InputStreamReader(inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    var receiveString: String?
                    var stringBuilder = StringBuilder()
                    val array = LinkedList<String>()
                    while (bufferedReader.readLine().also { receiveString = it } != null) {
                        array.add(0,"$receiveString\n")
                    }
                    var lines = 0
                    for (i in 0 until array.size){
                        stringBuilder.append(array[i])
                        if(linesLimit>400) break
                        lines++
                    }
                    inputStream.close()
                    ret = stringBuilder.toString()
                }
            } catch (e: FileNotFoundException) {
                Log.e(TAG,"login activity File not found: $e")
            } catch (e: IOException) {
                Log.e(TAG,"login activity Can not read file: $e")
            }
            return ret
        }
        private fun readFromFileToList(filePath: String = "", predicate:(String)->Boolean = {true}): ArrayList<String>? {
            try {
                val res = ArrayList<String>()
                val inputStream= FileInputStream(filePath)
                if (inputStream != null) {
                    val inputStreamReader = InputStreamReader(inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    var receiveString: String?

                    while (bufferedReader.readLine().also { receiveString = it } != null) {
                        receiveString?.let {
                            if(predicate(it)){
                                res.add(it)
                            }
                        }
                    }
                    inputStream.close()
                }
                return res;
            } catch (e: FileNotFoundException) {
                Log.e(TAG,"login activity File not found: $e")
            } catch (e: IOException) {
                Log.e(TAG,"login activity Can not read file: $e")
            }
            return null
        }
    }

    // Share log file
    object ShareFile{
        fun sendLogToEmail(activity: Activity, email: String = ""){
            val uri = FileProvider.getUriForFile(activity.applicationContext, "com.geomoby",
                File(
                    FileAccess.getLogFilePath(
                        activity
                    )
                )
            )
            val chooser = Intent.createChooser(
                getShareIntent(
                    activity,
                    email,
                    uri,
                    "Log file"
                ), "Share File")
            startChooser(
                activity,
                uri,
                chooser
            )
        }

        private fun getShareIntent(activity: Activity, email:String, uri: Uri, message:String):Intent{
            val intent = ShareCompat.IntentBuilder.from(activity)
                .setType("text/html")
                .addEmailTo( if(email.isEmpty())
                    SUPPORT_EMAIL
                else
                    email
                )
                .setSubject("Log from DemoApp")
                .setText(message)
                .setStream(uri)
                .setChooserTitle("Choose")
            val shareIntent = intent.getIntent()
            shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            return shareIntent
        }
        private fun startChooser(activity: Activity, uri: Uri, chooser: Intent?) {
            chooser?.let { intent ->
                val resInfoList: List<ResolveInfo> = activity.packageManager
                    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    activity.grantUriPermission(
                        packageName,
                        uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                activity.startActivity(intent)

            }
        }
        private fun zipFile(fileToZip:File):File{
            val res = File(fileToZip.parent,"compressed.zip")
            val fos = FileOutputStream(res)
            val zipOut = ZipOutputStream(fos)
            //val fileToZip = File(sourceFile)
            val fis = FileInputStream(fileToZip)
            val zipEntry = ZipEntry(fileToZip.name)
            zipOut.putNextEntry(zipEntry)
            val bytes = ByteArray(1024)
            var length: Int
            while (fis.read(bytes).also { length = it } >= 0) {
                zipOut.write(bytes, 0, length)
            }
            zipOut.close()
            fis.close()
            fos.close()
            return res
        }
    }

}

private fun <E> List<E>?.limit(limit: Int): java.util.ArrayList<E>? {
    val res = ArrayList<E>()
    var i = 1
    this?.forEach { item->
        res.add(item)
        if(i>limit) return@forEach
        i++
    }
    return res
}
