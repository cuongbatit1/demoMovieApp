package com.studio.king.demomovie.viewmodel.repository

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.studio.king.demomovie.model.GenresModel
import com.studio.king.demomovie.network.api.ApiService
import com.studio.king.demomovie.utils.Constants
import com.studio.king.demomovie.utils.KeyWordJson
import com.studio.king.demomovie.utils.KeyWordParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeRepository(private val apiService: ApiService) : KoinComponent {
    private val mGson: Gson by inject()
    private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getListGenres() : Any?{
        return withContext(bgDispatcher) {
            val queryMap = HashMap<String, Any>()
            queryMap[KeyWordParam.API_KEY] = Constants.API_KEY
            queryMap[KeyWordParam.LANGUAGE] = Constants.EN_US
            val dataResult = apiService.getListGenres(queryMap)
            if (dataResult.has(KeyWordJson.GENRES) && dataResult.get(KeyWordJson.GENRES) is JsonArray) {
                val mJsonArray : JsonArray = dataResult.getAsJsonArray(KeyWordJson.GENRES)
                return@withContext mGson.fromJson(mJsonArray.toString(), object : TypeToken<List<GenresModel>>() {}.type)
            }
            return@withContext dataResult
        }
    }

    suspend fun getListTrending() : Any?{
        return withContext(bgDispatcher) {
            val queryMap = HashMap<String, Any>()
            queryMap[KeyWordParam.API_KEY] = Constants.API_KEY
            return@withContext apiService.getListTrendingWeek(queryMap)
        }
    }

    suspend fun getListPopular(page : String) : Any?{
        return withContext(bgDispatcher) {
            val queryMap = HashMap<String, Any>()
            queryMap[KeyWordParam.API_KEY] = Constants.API_KEY
            queryMap[KeyWordParam.LANGUAGE] = Constants.EN_US
            queryMap[KeyWordParam.PAGE] = page
            return@withContext apiService.getListMoviePopular(queryMap)
        }
    }

    suspend fun getListTopRated(page : String) : Any?{
        return withContext(bgDispatcher) {
            val queryMap = HashMap<String, Any>()
            queryMap[KeyWordParam.API_KEY] = Constants.API_KEY
            queryMap[KeyWordParam.LANGUAGE] = Constants.EN_US
            queryMap[KeyWordParam.PAGE] = page
            return@withContext apiService.getListMovieTopRated(queryMap)
        }
    }

    suspend fun getListUpcoming(page : String) : Any?{
        return withContext(bgDispatcher) {
            val queryMap = HashMap<String, Any>()
            queryMap[KeyWordParam.API_KEY] = Constants.API_KEY
            queryMap[KeyWordParam.LANGUAGE] = Constants.EN_US
            queryMap[KeyWordParam.PAGE] = page
            return@withContext apiService.getListMovieUpcoming(queryMap)
        }
    }
}