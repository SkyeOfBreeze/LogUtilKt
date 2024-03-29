package org.btelman.logutil.kotlin.demo

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.btelman.logutil.kotlin.LogUtil
import org.btelman.logutil.kotlin.MeasurementUtil
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    val logUtil = LogUtil("MainActivity")
    val measurementUtil = MeasurementUtil(logUtil)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onCreateMeasurementTitle = "onCreate"
        measurementUtil.start(onCreateMeasurementTitle) //testing start of measurement
        measurementUtil.measure("logTesting"){ //testing inline lambda for wrapping code in measure
            ///--inline calls--///

            //will only actually run the inside code if the log level is debug or lower
            // Setting to log level of debug should make it faster, since it does not run the inside code
            logUtil.v {
                //you can also throw in other code as well that is not related to logging
                foo()
                Thread.sleep(500) //simulate work in the lambda
                "OnCreate" //finally return the result
            }

            logUtil.d{ "Debug!!!" }
            logUtil.w{ "Warning!!!" }
            logUtil.e{ "Error!!!" }
            logUtil.e(Exception("Foo")){ "Error With Exception!!!" }

            ///--end inline calls--///

            ///--regular calls--///
            //Regular calls can be used when the lambdas are not really needed
            logUtil.v("Verbose non-lambda")
            logUtil.d("debug non-lambda")
            logUtil.w("warning non-lambda")
            logUtil.e("error non-lambda")
            logUtil.e("error non-lambda", Exception("bar"))
            ///--end regular calls--///
        }

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        measurementUtil.stop(onCreateMeasurementTitle) //testing end of measurement

        testCustomLogger()
    }

    private fun testCustomLogger() {
        val customClass = DemoCustomLogUtilInstance(
            "Custom", null, null){ message, exception ->
            debugTextView.text = "${debugTextView.text}\n${message}"
            exception?.let {
                debugTextView.text = "${debugTextView.text}\n$exception"
            }
        }
        LogUtil.addCustomLogUtilInstance("Custom1", customClass)
        val log1 = LogUtil("CustomTest1", "Custom1") //set it up using instance reference
        //LogUtil("CustomTest1", customClass) //Optionally set it up directly
        log1.v {
            //you can also throw in other code as well that is not related to logging
            foo()
            Thread.sleep(500) //simulate work in the lambda
            "OnCreate" //finally return the result
        }

        log1.d{ "Debug!!!" }
        log1.w{ "Warning!!!" }
        log1.e{ "Error!!!" }
        log1.e(Exception("Foo")){ "Error With Exception!!!" }
    }

    fun foo(){

    }
}
