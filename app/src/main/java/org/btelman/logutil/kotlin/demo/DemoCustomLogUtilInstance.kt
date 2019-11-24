package org.btelman.logutil.kotlin.demo

import org.btelman.logutil.kotlin.LogLevel
import org.btelman.logutil.kotlin.LogUtilInstance
import java.io.PrintWriter

/**
 * Created by Brendon on 11/24/2019.
 */
class DemoCustomLogUtilInstance(globalTag: String, logLevel: LogLevel?, printWriter: PrintWriter?,
                                val logCallback : ((String, Exception?)->Unit)?) :
    LogUtilInstance(globalTag, logLevel, printWriter) {
    override fun sendToStream(logLevelToLogAs: LogLevel, log: String, exception: Exception?) {
        //lets send this to the logCallback
        logCallback?.invoke(getLogPrefix(logLevelToLogAs) + log, exception)
        super.sendToStream(logLevelToLogAs, log, exception)
    }

    override fun getLogPrefix(logLevel: LogLevel): String {
        return "CUSTOM VARIABLES : "+super.getLogPrefix(logLevel)
    }
}