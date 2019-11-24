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

import android.util.Log
import java.io.PrintWriter

/**
 * Instance that holds onto the LogUtil info. Most projects won't need to touch this class,
 * but can be useful for setting a library to log to a different file if File logging is needed.
 *
 * Uses the same log level that LogUtil uses if logLevel was not supplied or is null
 */
open class LogUtilInstance(
    protected var globalTag : String,
    protected var logLevel: LogLevel? = null,
    protected var printWriter : PrintWriter? = null
){
    /**
     * Send to a stream of some kind.
     * This sends to the local printWriter if it exists, but can also be overridden
     */
    protected open fun sendToStream(logLevelToLogAs: LogLevel, log: String, exception: Exception ?= null) {
        printWriter?.let { pw ->
            pw.print(getLogPrefix(logLevelToLogAs))
            pw.println(log)
            exception?.let {
                pw.println("***************")
                it.printStackTrace(pw)
                pw.println("***************")
            }
            pw.flush()
        }
    }

    /**
     * Get the data that will be at the beginning of each log
     */
    protected open fun getLogPrefix(logLevel: LogLevel): String {
        val prefixPrefix = System.currentTimeMillis().toString()
        return when(logLevel){
            LogLevel.VERBOSE -> "$prefixPrefix : V : "
            LogLevel.DEBUG -> "$prefixPrefix : D : "
            LogLevel.WARNING -> "$prefixPrefix : W : "
            LogLevel.ERROR -> "$prefixPrefix : E : "
            LogLevel.NONE -> ""
        }
    }

    fun log(logLevelToLogAs: LogLevel, message : String,
            exception: Exception? = null, overrideLogCheck : Boolean = false){
        if(!overrideLogCheck && !checkShouldLog(logLevelToLogAs)) return
        when(logLevelToLogAs){
            LogLevel.VERBOSE -> Log.v(globalTag, message)
            LogLevel.DEBUG -> Log.d(globalTag, message)
            LogLevel.WARNING -> Log.w(globalTag, message)
            LogLevel.ERROR -> {
                exception?.let {
                    Log.e(globalTag, message, exception)
                }?: Log.e(globalTag, message)
            }
            else -> {}
        }
        sendToStream(logLevelToLogAs, message, exception)
    }

    fun checkShouldLog(logLevelToLogAs: LogLevel): Boolean {
        if(logLevel == LogLevel.VERBOSE && logLevelToLogAs == LogLevel.VERBOSE) return true
        return logLevelToLogAs >= resolveLogLevel()
    }

    private fun resolveLogLevel() : LogLevel{
        return logLevel ?: LogUtil.logLevel
    }
}