package com.studio.king.demomovie.network.interceptor

import android.content.Context
import com.studio.king.demomovie.network.NoConnectivityException
import com.studio.king.demomovie.network.monitor.NetworkMonitor
import com.studio.king.demomovie.utils.LogUtil
import okhttp3.*

class NetworkInterceptor(context: Context, networkMonitor: NetworkMonitor) : Interceptor {
    private val networkMonitor: NetworkMonitor = networkMonitor
    private val context: Context = context


    override fun intercept(chain: Interceptor.Chain): Response {
        LogUtil.d("NetworkInterceptor intercept")
        return if (networkMonitor.isConnected()) {
            chain.proceed(chain.request())
        } else {
            LogUtil.d("key_no_internet_connection")
            throw NoConnectivityException()
        }
    }

}