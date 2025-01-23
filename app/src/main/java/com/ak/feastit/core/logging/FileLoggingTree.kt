package com.ak.feastit.core.logging

import android.content.Context
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.util.Log
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.util.FileSize
import ch.qos.logback.core.util.StatusPrinter
import com.ak.feastit.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import timber.log.Timber
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Reference: [File Logging in Android with Timber](https://sureshjoshi.com/mobile/file-logging-in-android-with-timber)
 */
internal class FileLoggingTree(
    private val context: Context
): Timber.DebugTree() {

    private val superVisorJob = SupervisorJob()

    /**
     * Handle exception to display a message instead of crashing
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
//        handleError(exception)
    }
    private val uiScope
        get() = CoroutineScope(Dispatchers.IO + superVisorJob + exceptionHandler)
    private var logDirectoryIsReady = AtomicBoolean(false)
    private val mLogger = LoggerFactory.getLogger(FileLoggingTree::class.java)

//    TODO check for crashlytics
//    override fun createStackElementTag(element: StackTraceElement): String? {
//        return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
//        return super.createStackElementTag(element)
//    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE) return

        uiScope.launch {
            initialise()
            val logMessage = "$tag: $message"
            when(priority) {
                Log.DEBUG -> mLogger.debug(logMessage)
                Log.INFO -> mLogger.info(logMessage)
                Log.WARN -> mLogger.warn(logMessage)
                Log.ERROR -> mLogger.error(logMessage)
            }
        }
    }

    fun onStop() {
        superVisorJob.cancel(cause = CancellationException("App stopped"))
    }

    private fun initialise() {
        if (logDirectoryIsReady.get()) return
        createDirectory()
        if (File(getLogsDirectory()).exists()) kotlin.run {
            logDirectoryIsReady.set(true)
            configureLogger(getLogsDirectory())
        }
    }

    private fun configureLogger(logDirectory: String) {
        uiScope.launch {
            // reset the default context (which may already have been initialized)
            // since we want to reconfigure it
            val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
            loggerContext.reset()

            val rollingFileAppender = RollingFileAppender<ILoggingEvent>().also {
                it.context = loggerContext
                it.isAppend = true
                it.file = "$logDirectory/$LOG_PREFIX-latest.txt"
            }

            val fileNamingPolicy = SizeAndTimeBasedFNATP<ILoggingEvent>().also {
                it.context = loggerContext
                it.setMaxFileSize(FileSize(FileSize.MB_COEFFICIENT))
            }

            val rollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>().also {
                it.context = loggerContext
                it.fileNamePattern = "$logDirectory/$LOG_PREFIX.%d{yyyy-MM-dd}.%i.txt"
                it.maxHistory = 7
                it.timeBasedFileNamingAndTriggeringPolicy = fileNamingPolicy
                it.setParent(rollingFileAppender)
                it.start()
            }

            val encoder = PatternLayoutEncoder().also {
                it.context = loggerContext
                it.charset = Charset.forName("UTF-8")
                it.pattern = "%date %level [%thread] %msg%n"
                it.start()
            }

            rollingFileAppender.rollingPolicy = rollingPolicy
            rollingFileAppender.encoder = encoder
            rollingFileAppender.start()

            // add the newly created appenders to the root logger;
            // qualify Logger to disambiguate from org.slf4j.Logger
            val root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
            root.level = Level.DEBUG
            root.addAppender(rollingFileAppender)

            // print any status messages (warnings, etc) encountered in logback config
            StatusPrinter.print(loggerContext)
        }
    }

    private fun createDirectory() {
//        /data/user/0/com.sg.r27a.fast3.mock.debug/files/vega/logs
        listOf(getImagesDirectory(), getLogsDirectory()).forEach { path ->
            val directory = File(path)
            if (!directory.exists()) directory.mkdirs()
        }
    }

    // region TODO maybe have separate file system
    private fun getImagesDirectory() = getRoot() + "/images"
    private fun getLogsDirectory() = getRoot() + "/logs"

    private fun getRoot(): String {
        return if (VERSION.SDK_INT >= VERSION_CODES.Q) context.filesDir.path
        else Environment.getExternalStorageDirectory().path
    }
    // endregion

//    Reference: https://github.com/mihonapp/mihon/blob/82fd89cee65f6663a6eddd09c73eaff23d3c2947/app/src/main/java/eu/kanade/tachiyomi/util/CrashLogUtil.kt#L39
    private fun getDebugInfo(): String {
        /*return """
            App version: ${BuildConfig.VERSION_NAME} (${BuildConfig.FLAVOR}, ${BuildConfig.COMMIT_SHA}, ${BuildConfig.VERSION_CODE}
            Android version: ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT}); build ${Build.DISPLAY}
            Device brand: ${Build.BRAND}
            Device manufacturer: ${Build.MANUFACTURER}
            Device name: ${Build.DEVICE} (${Build.PRODUCT})
            Device model: ${Build.MODEL}
        """.trimIndent()*/
        return """
            App version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})
            Android version: ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT}); build ${Build.DISPLAY}
            Device brand: ${Build.BRAND}
            Device manufacturer: ${Build.MANUFACTURER}
            Device name: ${Build.DEVICE} (${Build.PRODUCT})
            Device model: ${Build.MODEL}
        """.trimIndent()
    }

    private companion object {
        const val LOG_PREFIX = "log"
    }
}