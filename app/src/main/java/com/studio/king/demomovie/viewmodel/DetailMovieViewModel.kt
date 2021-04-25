package com.studio.king.demomovie.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.studio.king.demomovie.R
import com.studio.king.demomovie.model.*
import com.studio.king.demomovie.model.dto.CommentModelDTO
import com.studio.king.demomovie.model.dto.TrendingModelDTO
import com.studio.king.demomovie.utils.LogUtil
import com.studio.king.demomovie.utils.TypeDataHome
import com.studio.king.demomovie.utils.TypeLayoutString
import com.studio.king.demomovie.viewmodel.repository.DetailMovieRepository
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DetailMovieViewModel(private val mDetailMovieRepository: DetailMovieRepository) : ViewModel(), KoinComponent {

    companion object {
    }

    val bgScope = CoroutineScope(Dispatchers.IO)
    private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val mResources: Resources by inject()
    var mArrayShowLayout: MutableList<Any>? = null

    var mMovieModel : MovieModel? = null
    val typeStringLoadMorePopular = TypeLayoutString.TYPE_LOAD_MORE

    private val mLayoutRecommendations: LayoutUIModel by lazy {
        LayoutUIModel().apply {
            page = 1
            layoutType = 0
            typeData = TypeDataHome.TYPE_RECOMMENDATIONS
            isScrollEnd = false
        }
    }

    var isLoadDataRecommendations : Boolean = false

    private val mLayoutVideo: LayoutUIModel by lazy {
        LayoutUIModel().apply {
            page = 1
            layoutType = 1
            typeData = TypeDataHome.TYPE_VIDEO
            isScrollEnd = false
        }
    }

    private val mLayoutCast: LayoutCastModel by lazy {
        LayoutCastModel().apply {

            layoutType = 1
            typeData = TypeDataHome.TYPE_CAST
            offsetScrollX = 0
            currentPosition = 0
        }
    }

    var isLoadDataVideo : Boolean = false

    private val mLayoutUIReviewModel: LayoutUIReviewModel by lazy {
        LayoutUIReviewModel().apply {
            page = 1
            totalPage = null
            offsetScrollX = 0
            currentPosition = 0
        }
    }


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
        itemRate.star = mMovieModel?.vote_average
        itemRate.textTime = mMovieModel?.release_date
        itemRate.textIOS =  mMovieModel?.spoken_languages?.lastOrNull()?.iso_639_1
        itemRate.genres = mMovieModel?.genres?.lastOrNull()?.name
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



        mLayoutCast.listCast?.let {
            val titleSeriesCast = TitleModel(mResources.getString(R.string.key_series_cast))
            titleSeriesCast.isBackground = true
            titleSeriesCast.isIcon = false
            mArrayShowLayout?.add(titleSeriesCast)
            mArrayShowLayout?.add(mLayoutCast)
        }

        if (mLayoutVideo.listMovie != null) {
            val titleVideo = TitleModel(mResources.getString(R.string.key_video))
            titleVideo.isBackground = true
            titleVideo.isIcon = false
            mArrayShowLayout?.add(titleVideo)
            mArrayShowLayout?.add(mLayoutVideo)
        }

        if (mLayoutUIReviewModel.listReview != null) {
            val titleComments = TitleModel(mResources.getString(R.string.key_comments))
            titleComments.isBackground = true
            titleComments.isIcon = true
            mArrayShowLayout?.add(titleComments)
            mArrayShowLayout?.addAll(mLayoutUIReviewModel.listReview!!)
        }

        if (mLayoutRecommendations.listMovie != null) {
            val titleRecomendations = TitleModel(mResources.getString(R.string.key_recomendations))
            titleRecomendations.isBackground = true
            titleRecomendations.isIcon = true
            mArrayShowLayout?.add(titleRecomendations)
            mArrayShowLayout?.add(mLayoutRecommendations)
        }
    }

    fun updateUIShowLayout() {
        mArrayShowLayoutLive.value = mArrayShowLayout

    }

    fun updateUIShowLayoutTop() {
        mLoadTopLayoutLive.value = mMovieModel

    }

    private suspend fun resetDataPullToRefresh() {
        mLayoutRecommendations.apply {
            page = 1
            totalPage = null
            layoutType = 0
            typeData = TypeDataHome.TYPE_RECOMMENDATIONS
            isScrollEnd = false
            listMovie = null
            currentPosition = 0
            offsetScrollX = 0
            isScrollEnd = false
        }
        mLayoutVideo.apply {
            page = 1
            totalPage = null
            layoutType = 1
            typeData = TypeDataHome.TYPE_VIDEO
            isScrollEnd = false
            listMovie = null
            currentPosition = 0
            offsetScrollX = 0
            isScrollEnd = false
        }
        mLayoutUIReviewModel.apply {
            page = 1
            totalPage = null
            isScrollEnd = false
            listReview = null
            currentPosition = 0
            offsetScrollX = 0
            isScrollEnd = false
        }
        mLayoutCast.apply {
            layoutType = 1
            typeData = TypeDataHome.TYPE_CAST
            offsetScrollX = 0
            currentPosition = 0
            listCast = null
        }
    }

    suspend fun taskLoadData(): Any? {
        return withContext(bgDispatcher) {
            val task = async(bgDispatcher) {
                resetDataPullToRefresh()
                val dataResultMovieApi = async {
                    return@async taskLoadMovieDetailApi()
                }

                val dataResultListRecommendations = async {
                    return@async taskLoadListRecommendationsApi()
                }
                val dataResultListVideo = async {
                    return@async taskLoadListVideoApi()
                }
                val dataResultListCastApi = async {
                    return@async taskLoadListCastApi()
                }

                val dataResultListReview = async {
                    return@async taskLoadListReviewApi()
                }


                val dataTaskListMovieApi = dataResultMovieApi.await()
                val dataTaskListRecommendations = dataResultListRecommendations.await()
                val dataTaskListVideo = dataResultListVideo.await()
                val dataTaskListListCast = dataResultListCastApi.await()
                val dataTaskListReview = dataResultListReview.await()
                return@async null
            }
            val dataResult = task.await()


            setupDataUI()
            return@withContext null
        }
    }

    private suspend fun taskLoadMovieDetailApi(): Any? {
        return withContext(bgDispatcher) {
            if (mMovieModel?.id!= null) {
                val dataResult = mDetailMovieRepository.getDetailMovie(mMovieModel?.id!!)
                if (dataResult is MovieModel) {
                    mMovieModel = dataResult
                }
                return@withContext dataResult
            } else {
                return@withContext "No ID Movie"
            }
        }
    }

    suspend fun taskLoadListRecommendationsApi(): Any? {
        return withContext(bgDispatcher) {
            if (mMovieModel?.id!= null) {
                val dataResult = mDetailMovieRepository.getListRecommendations(mMovieModel?.id!!, "${mLayoutRecommendations.page ?: 1}")
                if (dataResult is TrendingModelDTO) {
                    if (mLayoutRecommendations.listMovie == null) {
                        mLayoutRecommendations.listMovie = mutableListOf()
                    }
                    if (mLayoutRecommendations.listMovie != null && mLayoutRecommendations.listMovie!!.contains(typeStringLoadMorePopular)) {
                        mLayoutRecommendations.listMovie?.remove(typeStringLoadMorePopular)
                    }
                    mLayoutRecommendations.isScrollEnd = false
                    dataResult.results?.let { mLayoutRecommendations.listMovie?.addAll(it) }
                    if (mLayoutRecommendations.totalPage == null) {
                        mLayoutRecommendations.totalPage = dataResult.total_pages
                    }
                    mLayoutRecommendations.page?.let {
                        if (mLayoutRecommendations.totalPage != null) {
                            if (it <= mLayoutRecommendations.totalPage!!) {
                                mLayoutRecommendations.page = it + 1
                            }
                        } else {
                            mLayoutRecommendations.page = it + 1
                        }
                    }
                }
                return@withContext dataResult
            } else {
                return@withContext "No ID Movie"
            }
        }
    }

    suspend fun taskLoadListVideoApi(): Any? {
        return withContext(bgDispatcher) {
            if (mMovieModel?.id!= null) {
                val dataResult = mDetailMovieRepository.getListRecommendations(mMovieModel?.id!!, "${mLayoutVideo.page ?: 1}")
                if (dataResult is TrendingModelDTO) {
                    if (mLayoutVideo.listMovie == null) {
                        mLayoutVideo.listMovie = mutableListOf()
                    }
                    if (mLayoutVideo.listMovie != null && mLayoutVideo.listMovie!!.contains(typeStringLoadMorePopular)) {
                        mLayoutVideo.listMovie?.remove(typeStringLoadMorePopular)
                    }
                    mLayoutVideo.isScrollEnd = false
                    dataResult.results?.let { mLayoutVideo.listMovie?.addAll(it) }
                    if (mLayoutVideo.totalPage == null) {
                        mLayoutVideo.totalPage = dataResult.total_pages
                    }
                    mLayoutVideo.page?.let {
                        if (mLayoutVideo.totalPage != null) {
                            if (it <= mLayoutVideo.totalPage!!) {
                                mLayoutVideo.page = it + 1
                            }
                        } else {
                            mLayoutVideo.page = it + 1
                        }
                    }
                }
                return@withContext dataResult
            } else {
                return@withContext "No ID Movie"
            }
        }
    }

    private suspend fun taskLoadListCastApi(): Any? {
        return withContext(bgDispatcher) {
            if (mMovieModel?.id!= null) {
                val dataResult = mDetailMovieRepository.getListMovieCast(mMovieModel?.id!!)
                if (dataResult is MutableList<*>) {
                    mLayoutCast?.listCast = dataResult as MutableList<CastMovieModel>?
                }
                return@withContext dataResult
            } else {
                return@withContext "No ID Movie"
            }
        }
    }

    suspend fun taskLoadListReviewApi(): Any? {
        return withContext(bgDispatcher) {
            if (mMovieModel?.id!= null) {
                val dataResult = mDetailMovieRepository.getListReview(mMovieModel?.id!!, "${mLayoutUIReviewModel.page ?: 1}")
                if (dataResult is CommentModelDTO) {
                    if (mLayoutUIReviewModel.listReview == null) {
                        mLayoutUIReviewModel.listReview = mutableListOf()
                    }
                    if (mLayoutUIReviewModel.listReview != null && mLayoutUIReviewModel.listReview!!.contains(typeStringLoadMorePopular)) {
                        mLayoutUIReviewModel.listReview?.remove(typeStringLoadMorePopular)
                    }
                    mLayoutUIReviewModel.isScrollEnd = false
                    dataResult.results?.let { mLayoutUIReviewModel.listReview?.addAll(it) }
                    if (mLayoutUIReviewModel.totalPage == null) {
                        mLayoutUIReviewModel.totalPage = dataResult.total_pages
                    }
                    mLayoutUIReviewModel.page?.let {
                        if (mLayoutUIReviewModel.totalPage != null) {
                            if (it <= mLayoutUIReviewModel.totalPage!!) {
                                mLayoutUIReviewModel.page = it + 1
                            }
                        } else {
                            mLayoutUIReviewModel.page = it + 1
                        }
                    }
                }
                return@withContext dataResult
            } else {
                return@withContext "No ID Movie"
            }
        }
    }


    // Start function Load more Recommendations
    fun checkLoadMoreRecommendations() : Boolean {
        return if (mLayoutRecommendations.page != null && mLayoutRecommendations.totalPage != null && mLayoutRecommendations.page!! > mLayoutRecommendations.totalPage!!) {
            true
        } else isLoadDataRecommendations
    }

    fun updateLoadMoreRecommendations() {
        isLoadDataRecommendations = true
        if (mLayoutRecommendations.listMovie != null && mLayoutRecommendations.listMovie!!.contains(typeStringLoadMorePopular)) {
            mLayoutRecommendations.listMovie?.remove(typeStringLoadMorePopular)
        }
        mLayoutRecommendations.listMovie?.add(typeStringLoadMorePopular)
        mLayoutRecommendations.isScrollEnd = true
    }

    fun getIndexRecommendations() : Int? {
        return mArrayShowLayout?.indexOf(mLayoutRecommendations)
    }
    // End function Load more Recommendations

    // Start function Load more Video
    fun checkLoadMoreVideo() : Boolean {
        return if (mLayoutVideo.page != null && mLayoutVideo.totalPage != null && mLayoutVideo.page!! > mLayoutVideo.totalPage!!) {
            true
        } else isLoadDataVideo
    }

    fun updateLoadMoreVideo() {
        isLoadDataVideo = true
        if (mLayoutVideo.listMovie != null && mLayoutVideo.listMovie!!.contains(typeStringLoadMorePopular)) {
            mLayoutVideo.listMovie?.remove(typeStringLoadMorePopular)
        }
        mLayoutVideo.listMovie?.add(typeStringLoadMorePopular)
        mLayoutVideo.isScrollEnd = true
    }

    fun getIndexVideo() : Int? {
        return mArrayShowLayout?.indexOf(mLayoutVideo)
    }
    // End function Load more Recommendations

}