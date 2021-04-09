package com.studio.king.demomovie.utils

import com.studio.king.demomovie.BuildConfig

object Constants {
    const val BASE_URL = "https://api.themoviedb.org"
    const val BASE_API = "/3"
    const val API_KEY = "a7b3c9975791294647265c71224a88ad"
    const val EN_US = "en-US"

    const val DATABASE_NAME = "globic.db"
    const val SECURE = "globic@321"
    const val TIME_OUT : Long = 120
    const val REG_PASSWORD = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,16}\$"
    const val SELECTED_DEVICE = "SELECTED_DEVICE"
    const val TITLE = "TITLE"
    const val FILE_PATH = "FILE_PATH"
    const val CHECK_HEART_RATE = 10 // check heart <= 10 no send to server and no render UI Chart
    const val TIME_RANGER_DAY = 1 // 2 mode 15 minutes or 1 minutes

    const val RANGER_Y_MIX_MAX_CHART = 20
    const val MODE_OFFLINE_DB = true
}

interface TypeLayoutHome {
    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_LIST_MOVIE = 1
        const val TYPE_LIST_GENRE = 2
        const val TYPE_LIST_TRENDING = 3
    }
}

interface TypeLayoutDetailMovie {
    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_RATE = 1
        const val TYPE_TITLE_DETAIL = 2
        const val TYPE_WRITE_A_COMMENT = 3
    }
}

interface TypeLayoutMovie {
    companion object {
        const val TYPE_MOVIE = 0
        const val TYPE_LOAD_MORE = 1
    }
}

enum class TypeDataHome {
    TYPE_POPULAR,
    TYPE_TOP_RATED,
    TYPE_UPCOMING,
    TYPE_TRENDING
}

interface KeyWordParam {
    companion object {
        const val API_KEY = "api_key"
        const val LANGUAGE = "language"
        const val PAGE = "page"
    }
}

interface KeyWordJson {
    companion object {
        const val GENRES = "genres"
    }
}

interface TypeLayoutString {
    companion object {
        const val TYPE_LOAD_MORE = "TYPE_LOAD_MORE"
    }
}
