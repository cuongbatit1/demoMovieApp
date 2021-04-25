package com.studio.king.demomovie.viewmodel.repository

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.studio.king.demomovie.model.CastMovieModel
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

class DetailMovieRepository(private val apiService: ApiService) : KoinComponent {
    private val mGson: Gson by inject()
    private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO


    suspend fun getDetailMovie(idMovie : String) : Any?{
        return withContext(bgDispatcher) {
            val queryMap = HashMap<String, Any>()
            queryMap[KeyWordParam.API_KEY] = Constants.API_KEY
            queryMap[KeyWordParam.LANGUAGE] = Constants.EN_US
            return@withContext apiService.getDetailMovie(idMovie, queryMap)
        }
    }

    suspend fun getListRecommendations(idMovie : String, page : String) : Any?{
        return withContext(bgDispatcher) {
            val queryMap = HashMap<String, Any>()
            queryMap[KeyWordParam.API_KEY] = Constants.API_KEY
            queryMap[KeyWordParam.LANGUAGE] = Constants.EN_US
            queryMap[KeyWordParam.PAGE] = page
            return@withContext apiService.getListMovieRecommendations(idMovie, queryMap)
        }
    }

    suspend fun getListReview(idMovie : String, page : String) : Any?{
        return withContext(bgDispatcher) {
            val queryMap = HashMap<String, Any>()
            queryMap[KeyWordParam.API_KEY] = Constants.API_KEY
            queryMap[KeyWordParam.LANGUAGE] = Constants.EN_US
            queryMap[KeyWordParam.PAGE] = page
            return@withContext apiService.getListReview(idMovie, queryMap)
        }
    }

    suspend fun getListMovieCast(idMovie : String) : Any?{
        return withContext(bgDispatcher) {
            val queryMap = HashMap<String, Any>()
            queryMap[KeyWordParam.API_KEY] = Constants.API_KEY
            queryMap[KeyWordParam.LANGUAGE] = Constants.EN_US
            val dataResult = apiService.getListMovieCast(idMovie, queryMap)
            if (dataResult.has(KeyWordJson.CAST) && dataResult.get(KeyWordJson.CAST) is JsonArray) {
                val mJsonArray : JsonArray = dataResult.getAsJsonArray(KeyWordJson.CAST)
                return@withContext mGson.fromJson(mJsonArray.toString(), object : TypeToken<List<CastMovieModel>>() {}.type)
            }
            return@withContext dataResult
        }
    }
}