package com.studio.king.demomovie.utils

import android.util.Log
import com.studio.king.demomovie.BuildConfig

object LogUtil {
    private var LOG_ENABLED = BuildConfig.DEBUG
    private const val TAG = "DEMO_MOVE_LOG"


    fun d(){
        if (LOG_ENABLED) {
            Log.d(TAG, getCallerClassName())
        }
    }

    fun d(message: String) {
        if (LOG_ENABLED) {
            Log.d(TAG, getCallerClassName() + message)
        }

    }

    fun i(message: String) {
        if (LOG_ENABLED) {
            Log.i(TAG, getCallerClassName() + message)
        }

    }


    fun e(message: String) {
        if (LOG_ENABLED) {
            Log.e(TAG, getCallerClassName() + message)
        }

    }

    private fun getCallerClassName(): String {

        val callerElement = Thread.currentThread().stackTrace[4]
        var callerClassName = callerElement.className
        if (callerClassName.lastIndexOf(".") > 0) {
            callerClassName = callerClassName.substring(callerClassName.lastIndexOf(".") + 1)
        }
        if (callerClassName.indexOf("$") > 0) {
            callerClassName = callerClassName.substring(0, callerClassName.indexOf("$"))
        }

        var callerMethodName = callerElement.methodName;

        return "$callerClassName->$callerMethodName: ";
    }

}