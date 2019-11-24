# Log Util for Kotlin on Android #

This is a utility for logging in kotlin. This allows for optionally logging to a file or outputting the logs that use this class to other places using a custom class.

## Features ##

- Set an app wide log level or local log level using a custom implementation
- Inline kotlin functions to reduce overhead of aggressive logging if the log level is not selected
    ```
    logUtil.v{ 
        //will not get run if loglevel is set to debug
        val slowDebugCode = callDebugCode()
        "Debug!!! $slowDebugCode"
    }
    ```
- Use of regular calls that do not use inlining
    ```
    //will get run if loglevel is set to debug
    val slowDebugCode = callDebugCode()
    logUtil.v("Debug!!! $slowDebugCode")
    ```
