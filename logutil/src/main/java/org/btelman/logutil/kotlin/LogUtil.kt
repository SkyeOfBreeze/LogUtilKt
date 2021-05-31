/*
 * Copyright (c) 2019 Brendon Telman
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.btelman.logutil.kotlin
import java.io.PrintWriter
import java.lang.NullPointerException

/**
 * Logging Util with log levels
 */
class LogUtil{
    private val tag : String
    private val logUtilInstance: LogUtilInstance

    /**
     * Initialize LogUtil based on a custom class name. If it does not exist, it will throw a NullPointerException
     * @throws NullPointerException
     */
    @Throws(NullPointerException::class)
    constructor(tag : String, logUtilInstanceName: String){
        this.tag = tag
        this.logUtilInstance = customLoggers[logUtilInstanceName]
            ?: throw NullPointerException("LogUtilInstance does not exist!")
    }

    /**
     * Initialize LogUtil with a tag. Optionally pass in a custom LogUtilInstance
     */
    constructor(tag : String, logUtilInstance: LogUtilInstance = default){
        this.tag = tag
        this.logUtilInstance = logUtilInstance
    }

    /**
     * Verbose logging. Logs to logcat then to a stream, usually being a PrintStream
     */
    inline fun v(logString : ()->String){
        if(checkShouldLog(LogLevel.VERBOSE)){
            v(logString())
        }
    }

    /**
     * Debug logging. Logs to logcat then to a stream, usually being a PrintStream
     */
    inline fun d(logString : ()->String){
        if(checkShouldLog(LogLevel.DEBUG)){
            d(logString())
        }
    }

    /**
     * Warning logging. Logs to logcat then to a stream, usually being a PrintStream
     */
    inline fun w(logString : ()->String){
        if(checkShouldLog(LogLevel.WARNING)){
            w(logString())
        }
    }

    /**
     * Error logging. Logs to logcat then to a stream, usually being a PrintStream
     */
    inline fun e(logString : ()->String){
        if(checkShouldLog(LogLevel.ERROR)){
            e(logString())
        }
    }

    /**
     * Error logging with an exception. Logs to logcat then to a stream, usually being a PrintStream
     */
    inline fun e(e : Exception, logString : ()->String){
        if(checkShouldLog(LogLevel.ERROR)){
            e(logString(), e)
        }
    }

    fun v(message : String){
        //no need to check for if we should log since log(... , ...) does that
        logUtilInstance.log(LogLevel.VERBOSE, buildMessage(message))
    }

    fun d(message : String){
        logUtilInstance.log(LogLevel.DEBUG, buildMessage(message))
    }

    fun w(message : String){
        logUtilInstance.log(LogLevel.WARNING, buildMessage(message))
    }

    fun e(message : String){
        logUtilInstance.log(LogLevel.ERROR, buildMessage(message))
    }

    fun e(message : String, exception: Exception){
        logUtilInstance.log(LogLevel.ERROR, buildMessage(message), exception)
    }

    /**
     * Check whether or not the log will show if we call the log function
     */
    fun checkShouldLog(logLevel: LogLevel): Boolean {
        return logUtilInstance.checkShouldLog(logLevel)
    }

    private fun buildMessage(message: String): String {
        return "$tag : $message"
    }

    companion object{
        var default = LogUtilInstance("LogUtil")
        var customLoggers = HashMap<String, LogUtilInstance>()

        fun init(TAG: String, logLev: LogLevel, pw: PrintWriter) {
            logLevel = logLev
            default = LogUtilInstance(TAG, null, pw)
        }

        fun addCustomLogUtilInstance(name : String, logUtilInstance: LogUtilInstance) {
            customLoggers[name] = logUtilInstance
        }

        var logLevel : LogLevel = LogLevel.WARNING
    }
}
