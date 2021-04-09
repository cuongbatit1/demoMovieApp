package com.studio.king.demomovie.network.interceptor


import com.studio.king.demomovie.utils.LogUtil
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        LogUtil.d("HeaderInterceptor intercept")
        return chain.proceed(chain.request())
    }

}