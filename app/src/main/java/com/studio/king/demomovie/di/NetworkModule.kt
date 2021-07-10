package com.studio.king.demomovie.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.studio.king.demomovie.BuildConfig
import com.studio.king.demomovie.network.api.ApiService
import com.studio.king.demomovie.network.interceptor.HeaderInterceptor
import com.studio.king.demomovie.network.interceptor.NetworkInterceptor
import com.studio.king.demomovie.network.monitor.NetworkMonitor
import com.studio.king.demomovie.utils.Constants
import okhttp3.*

import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(androidContext().cacheDir, "http-cache")
        Cache(httpCacheDirectory, cacheSize)
    }

    single { HeaderInterceptor() }
//
    single { NetworkMonitor(androidApplication()) }
//
    single { NetworkInterceptor(androidApplication(), get()) }



    single { val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        builder.addInterceptor(logging)
        builder.addInterceptor(get<NetworkInterceptor>())
//        builder.addInterceptor(get<HeaderInterceptor>())
        builder.cache(get())
        builder.connectTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
        builder.readTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
        builder.writeTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
//        builder.authenticator(object : Authenticator {
//            @Throws(IOException::class)
//            override fun authenticate(route: Route?, response: Response): Request? {
//                if (response.request.header("Authorization") != null) {
//                    return null // Give up, we've already attempted to authenticate.
//                }
//
//                LogUtil.e("Authenticating for response: $response")
//                LogUtil.e("Challenges: ${response.challenges()}")
//
//                val data = GlobicApplication.sInstance.mAccessToken?.let { it1 ->
//                    response.request.newBuilder()
//                        .header("Authorization", it1)
//                        .build()
//                }
//                LogUtil.e("data: ${data}")
//                return data
//            }
//        })
        builder.build() }

    single { Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
//        .addCallAdapterFactory(NetworkResponseAdapterFactory())
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(get())
        .build() }

    single {
        get<Retrofit>().create(ApiService::class.java)
    }


//    single { Picasso.Builder(get())
//            .downloader(get<OkHttp3Downloader>()).build().apply {
//                setIndicatorsEnabled(true)
//                isLoggingEnabled = true
//            }}
}