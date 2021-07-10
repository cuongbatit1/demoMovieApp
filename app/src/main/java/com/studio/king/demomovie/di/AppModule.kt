package com.studio.king.demomovie.di

import com.google.gson.GsonBuilder
import com.studio.king.demomovie.navigator.NavigatorScreen
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.*

val appModule = module {

    single { androidApplication().resources }

    single {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.create()
    }
    single(named("time_zone_default_id")) { TimeZone.getDefault().id }

    // Navigator Screen push change Fragment
    single { NavigatorScreen() }


}

val appModules = listOf(appModule, networkModule, viewModelModule)