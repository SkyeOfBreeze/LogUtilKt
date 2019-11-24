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

/**
 * Useful for measuring actions
 */
open class MeasurementUtil(val logUtil: LogUtil = LogUtil("MeasurementUtil")) {
    var list = HashMap<String, Long>()

    fun start(name : String){
        list[name] = System.currentTimeMillis()

        //Time already logged by LogUtil, so start time not needed here
        log("$name : Starting Timer")
    }

    fun stop(name : String){
        list[name]?.let { startTime ->
            val diff = System.currentTimeMillis()-startTime
            log("$name : Took $diff ms")
        }
    }

    open fun log(message : String){
        logUtil.d(message)
    }

    inline fun measure(name : String, func : ()->Unit){
        start(name)
        func()
        stop(name)
    }

    fun clearMeasurements(){
        list.clear()
    }
}