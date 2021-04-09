package com.studio.king.demomovie.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.studio.king.demomovie.R
import com.studio.king.demomovie.model.*
import com.studio.king.demomovie.model.dto.TrendingModelDTO
import com.studio.king.demomovie.utils.LogUtil
import com.studio.king.demomovie.utils.TypeDataHome
import com.studio.king.demomovie.utils.TypeLayoutString
import com.studio.king.demomovie.viewmodel.repository.HomeRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.experimental.property.inject

class DetailMovieViewModel(private val mHomeRepository: HomeRepository) : ViewModel(), KoinComponent {

    companion object {
    }

    val bgScope = CoroutineScope(Dispatchers.IO)
    private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val mResources: Resources by inject()
    var mArrayShowLayout: MutableList<Any>? = null

    var mMovieModel : MovieModel? = null

    // Live Show data observer
    val mLoadTopLayoutLive: MutableLiveData<MovieModel?> by lazy { MutableLiveData(null) }
    val mArrayShowLayoutLive: MutableLiveData<MutableList<Any>?> by lazy { MutableLiveData(null) }
    val isShowLoadingLive: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    val mStatePullToRefreshLive: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }

    fun initViewModel() {
        updateUIShowLayoutTop()
    }
    //No save State

    fun resetDataDestroy() {

    }

    fun saveState() {

    }

    fun getState() {
        LogUtil.d("MainActivitySavedState getState")

    }


    private fun setupDataUI() {
        mArrayShowLayout = mutableListOf()
        mArrayShowLayout?.clear()
        val itemRate = RateDetailModel()
        itemRate.star = 4f
        itemRate.textTime = "December 2018"
        mArrayShowLayout?.add(itemRate)

        val itemTitleDetail = TitleDetailModel()
        itemTitleDetail.title = mMovieModel?.title
        itemTitleDetail.description = mMovieModel?.overview
//        itemTitleDetail.isShowReadMore = itemTitleDetail.description != null && itemTitleDetail.description!!.length > 200
        itemTitleDetail.isShowReadMore = true
        mArrayShowLayout?.add(itemTitleDetail)


        val titleYourRate = TitleModel(mResources.getString(R.string.key_your_rate))
        titleYourRate.isBackground = true
        titleYourRate.isIcon = false
        mArrayShowLayout?.add(titleYourRate)

        val itemWriteComment = WriteCommentDetailModel()
        itemWriteComment.star = 0f
        mArrayShowLayout?.add(itemWriteComment)

        val titleSeriesCast = TitleModel(mResources.getString(R.string.key_series_cast))
        titleSeriesCast.isBackground = true
        titleSeriesCast.isIcon = false
        mArrayShowLayout?.add(titleSeriesCast)

        val titleVideo = TitleModel(mResources.getString(R.string.key_video))
        titleVideo.isBackground = true
        titleVideo.isIcon = false
        mArrayShowLayout?.add(titleVideo)

        val titleComments = TitleModel(mResources.getString(R.string.key_comments))
        titleVideo.isBackground = true
        titleVideo.isIcon = true
        mArrayShowLayout?.add(titleComments)

        val titleRecomendations = TitleModel(mResources.getString(R.string.key_recomendations))
        titleRecomendations.isBackground = true
        titleRecomendations.isIcon = true
        mArrayShowLayout?.add(titleRecomendations)
    }

    fun updateUIShowLayout() {
        mArrayShowLayoutLive.value = mArrayShowLayout

    }

    fun updateUIShowLayoutTop() {
        mLoadTopLayoutLive.value = mMovieModel

    }

    private suspend fun resetDataPullToRefresh() {

    }

    suspend fun taskLoadData(): Any? {
        return withContext(bgDispatcher) {
//            val task = async(bgDispatcher) {
//                resetDataPullToRefresh()
//                val dataResultListGenresApi = async {
//                    return@async taskLoadListGenresApi()
//                }
//
//                val dataResultListTrending = async {
//                    return@async taskLoadListTrendingApi()
//                }
//                val dataResultListPopular = async {
//                    return@async taskLoadListPopularApi()
//                }
//                val dataResultListTopRated = async {
//                    return@async taskLoadListTopRatedApi()
//                }
//
//                val dataResultListUpcoming = async {
//                    return@async taskLoadListUpcomingApi()
//                }
//
//
//                val dataTaskListGenresApi = dataResultListGenresApi.await()
//                val dataTaskListTrending = dataResultListTrending.await()
//                val dataTaskListPopular = dataResultListPopular.await()
//                val dataTaskListTopRated = dataResultListTopRated.await()
//                val dataTaskListUpcoming = dataResultListUpcoming.await()
//                return@async null
//            }
//            val dataResult = task.await()


            setupDataUI()
            return@withContext null
        }
    }

}