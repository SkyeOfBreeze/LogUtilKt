package org.btelman.logutil.kotlin.demo

import android.app.Application
import org.btelman.logutil.kotlin.LogLevel
import org.btelman.logutil.kotlin.LogUtil
import org.btelman.logutil.kotlin.MeasurementUtil
import java.io.File

/**
 * Sample Application that shows how to set up log util
 */
class DemoApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // Create a printWriter. Completely optional, and probably not desired for production builds
        // unless stored for crash reports
        val printWriter = File(cacheDir, "Demo-${System.currentTimeMillis()}").printWriter()

        // Initialize LogUtil with a custom tag and log level, and optional printWriter from above
        // Optionally, this setup could look for a custom variable that changes depending on
        //  build flavor/debug flag, to change the log level
        LogUtil.init("LogUtilDemo", LogLevel.VERBOSE, printWriter)

        MeasurementUtil().also { util ->
            util.measure("MeasurementTest"){
                //bad idea to use Thread.sleep on the main thread, but good for testing
                Thread.sleep(1000)
            }
        }
    }
}
