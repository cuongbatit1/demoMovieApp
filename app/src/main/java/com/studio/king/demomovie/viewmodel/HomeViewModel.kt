package com.studio.king.demomovie.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.studio.king.demomovie.R
import com.studio.king.demomovie.model.GenresModel
import com.studio.king.demomovie.model.LayoutGenreUIModel
import com.studio.king.demomovie.model.LayoutUIModel
import com.studio.king.demomovie.model.TitleModel
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

class HomeViewModel(private val mHomeRepository: HomeRepository) : ViewModel(), KoinComponent {

    companion object {
    }

    val bgScope = CoroutineScope(Dispatchers.IO)
    private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val mResources: Resources by inject()
    var mArrayShowLayout: MutableList<Any>? = null

    private val mLayoutGenreUIModel: LayoutGenreUIModel by lazy { LayoutGenreUIModel() }

    private val mLayoutTrending: LayoutUIModel by lazy { LayoutUIModel().apply {
        layoutType = 1
        typeData = TypeDataHome.TYPE_TRENDING
    } }

    private val mLayoutPopular: LayoutUIModel by lazy {
        LayoutUIModel().apply {
            page = 1
            layoutType = 0
            typeData = TypeDataHome.TYPE_POPULAR
            isScrollEnd = false
        }
    }

    private val mLayoutTopRated: LayoutUIModel by lazy {
        LayoutUIModel().apply {
            page = 1
            layoutType = 0
            typeData = TypeDataHome.TYPE_TOP_RATED
            isScrollEnd = false
        }
    }

    private val mLayoutUpcoming: LayoutUIModel by lazy {
        LayoutUIModel().apply {
            page = 1
            layoutType = 0
            typeData = TypeDataHome.TYPE_UPCOMING
            isScrollEnd = false
        }
    }

    var isLoadDataPopular : Boolean = false
    var isLoadDataTopRated : Boolean = false
    var isLoadDataUpcoming : Boolean = false

    val typeStringLoadMorePopular = TypeLayoutString.TYPE_LOAD_MORE

    // Live Show data observer
    val mArrayShowLayoutLive: MutableLiveData<MutableList<Any>?> by lazy { MutableLiveData(null) }
    val isShowLoadingLive: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    val mStatePullToRefreshLive: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }

    fun initViewModel() {

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
        if (mLayoutTrending.listMovie != null) {
            val titleTrending = TitleModel(mResources.getString(R.string.key_trending))
            mArrayShowLayout?.add(titleTrending)
            mArrayShowLayout?.add(mLayoutTrending)
        }

        if (mLayoutGenreUIModel.listGenres != null) {
            val titleGenre = TitleModel(mResources.getString(R.string.key_genre))
            mArrayShowLayout?.add(titleGenre)
            mArrayShowLayout?.add(mLayoutGenreUIModel)
        }

        if (mLayoutPopular.listMovie != null) {
            val titlePopular = TitleModel(mResources.getString(R.string.key_popular))
            mArrayShowLayout?.add(titlePopular)
            mArrayShowLayout?.add(mLayoutPopular)
        }

        if (mLayoutTopRated.listMovie != null) {
            val titleTopRated = TitleModel(mResources.getString(R.string.key_top_rated))
            mArrayShowLayout?.add(titleTopRated)
            mArrayShowLayout?.add(mLayoutTopRated)
        }

        if (mLayoutUpcoming.listMovie != null) {
            val titleUpcoming = TitleModel(mResources.getString(R.string.key_upcoming))
            mArrayShowLayout?.add(titleUpcoming)
            mArrayShowLayout?.add(mLayoutUpcoming)
        }
    }

    fun updateUIShowLayout() {

        mArrayShowLayoutLive.value = mArrayShowLayout

    }

    private suspend fun resetDataPullToRefresh() {

        mLayoutGenreUIModel.apply {
            currentPosition = 0
            offsetScrollX = 0
        }
        mLayoutTrending.apply {
            page = 1
            totalPage = null
            layoutType = 1
            typeData = TypeDataHome.TYPE_TRENDING
            isScrollEnd = false
            listMovie = null
            currentPosition = 0
            offsetScrollX = 0
            isScrollEnd = false
        }
        mLayoutPopular.apply {
            page = 1
            totalPage = null
            layoutType = 0
            typeData = TypeDataHome.TYPE_POPULAR
            isScrollEnd = false
            listMovie = null
            currentPosition = 0
            offsetScrollX = 0
            isScrollEnd = false
        }
        mLayoutTopRated.apply {
            page = 1
            totalPage = null
            layoutType = 0
            typeData = TypeDataHome.TYPE_POPULAR
            isScrollEnd = false
            listMovie = null
            currentPosition = 0
            offsetScrollX = 0
            isScrollEnd = false
        }
        mLayoutUpcoming.apply {
            page = 1
            totalPage = null
            layoutType = 0
            typeData = TypeDataHome.TYPE_POPULAR
            isScrollEnd = false
            listMovie = null
            currentPosition = 0
            offsetScrollX = 0
            isScrollEnd = false
        }
    }

    suspend fun taskLoadData(): Any? {
        return withContext(bgDispatcher) {
            val task = async(bgDispatcher) {
                resetDataPullToRefresh()
                val dataResultListGenresApi = async {
                    return@async taskLoadListGenresApi()
                }

                val dataResultListTrending = async {
                    return@async taskLoadListTrendingApi()
                }
                val dataResultListPopular = async {
                    return@async taskLoadListPopularApi()
                }
                val dataResultListTopRated = async {
                    return@async taskLoadListTopRatedApi()
                }

                val dataResultListUpcoming = async {
                    return@async taskLoadListUpcomingApi()
                }


                val dataTaskListGenresApi = dataResultListGenresApi.await()
                val dataTaskListTrending = dataResultListTrending.await()
                val dataTaskListPopular = dataResultListPopular.await()
                val dataTaskListTopRated = dataResultListTopRated.await()
                val dataTaskListUpcoming = dataResultListUpcoming.await()
                return@async null
            }
            val dataResult = task.await()


            setupDataUI()
            return@withContext dataResult
        }
    }

    private suspend fun taskLoadListGenresApi(): Any? {
        return withContext(bgDispatcher) {

            val dataResult = mHomeRepository.getListGenres()

            LogUtil.e("taskLoadListGenresApi dataResult: $dataResult")
            if (dataResult is MutableList<*>) {
                mLayoutGenreUIModel.listGenres = dataResult as MutableList<GenresModel>?
            }
            return@withContext dataResult
        }
    }

    private suspend fun taskLoadListTrendingApi(): Any? {
        return withContext(bgDispatcher) {

            val dataResult = mHomeRepository.getListTrending()
            if (dataResult is TrendingModelDTO) {
                mLayoutTrending.listMovie = dataResult.results as MutableList<Any>?
                if (mLayoutTrending.totalPage == null) {
                    mLayoutTrending.totalPage = dataResult.total_pages
                }
                mLayoutTrending.page?.let {
                    if (mLayoutTrending.totalPage != null) {
                        if (it < mLayoutTrending.totalPage!!) {
                            mLayoutTrending.page = it + 1
                        }
                    } else {
                        mLayoutTrending.page = it + 1
                    }
                }
            }
            return@withContext dataResult
        }
    }

    suspend fun taskLoadListPopularApi(): Any? {
        return withContext(bgDispatcher) {

            val dataResult = mHomeRepository.getListPopular("${mLayoutPopular.page ?: 1}")
            if (dataResult is TrendingModelDTO) {
                if (mLayoutPopular.listMovie == null) {
                    mLayoutPopular.listMovie = mutableListOf()
                }
                if (mLayoutPopular.listMovie != null && mLayoutPopular.listMovie!!.contains(typeStringLoadMorePopular)) {
                    mLayoutPopular.listMovie?.remove(typeStringLoadMorePopular)
                }
                mLayoutPopular.isScrollEnd = false
                dataResult.results?.let { mLayoutPopular.listMovie?.addAll(it) }
                if (mLayoutPopular.totalPage == null) {
                    mLayoutPopular.totalPage = dataResult.total_pages
                }
                mLayoutPopular.page?.let {
                    if (mLayoutPopular.totalPage != null) {
                        if (it < mLayoutPopular.totalPage!!) {
                            mLayoutPopular.page = it + 1
                        }
                    } else {
                        mLayoutPopular.page = it + 1
                    }
                }
            }
            return@withContext dataResult
        }
    }

    suspend fun taskLoadListTopRatedApi(): Any? {
        return withContext(bgDispatcher) {

            val dataResult = mHomeRepository.getListTopRated("${mLayoutTopRated.page ?: 1}")
            if (dataResult is TrendingModelDTO) {
                if (mLayoutTopRated.listMovie == null) {
                    mLayoutTopRated.listMovie = mutableListOf()
                }
                if (mLayoutTopRated.listMovie != null && mLayoutTopRated.listMovie!!.contains(typeStringLoadMorePopular)) {
                    mLayoutTopRated.listMovie?.remove(typeStringLoadMorePopular)
                }
                mLayoutTopRated.isScrollEnd = false
                dataResult.results?.let { mLayoutTopRated.listMovie?.addAll(it) }
                if (mLayoutTopRated.totalPage == null) {
                    mLayoutTopRated.totalPage = dataResult.total_pages
                }
                mLayoutTopRated.page?.let {
                    if (mLayoutTopRated.totalPage != null) {
                        if (it < mLayoutTopRated.totalPage!!) {
                            mLayoutTopRated.page = it + 1
                        }
                    } else {
                        mLayoutTopRated.page = it + 1
                    }
                }
            }
            return@withContext dataResult
        }
    }

    suspend fun taskLoadListUpcomingApi(): Any? {
        return withContext(bgDispatcher) {

            val dataResult = mHomeRepository.getListTopRated("${mLayoutUpcoming.page ?: 1}")
            if (dataResult is TrendingModelDTO) {
                if (mLayoutUpcoming.listMovie == null) {
                    mLayoutUpcoming.listMovie = mutableListOf()
                }
                if (mLayoutUpcoming.listMovie != null && mLayoutUpcoming.listMovie!!.contains(typeStringLoadMorePopular)) {
                    mLayoutUpcoming.listMovie?.remove(typeStringLoadMorePopular)
                }
                mLayoutUpcoming.isScrollEnd = false
                dataResult.results?.let { mLayoutUpcoming.listMovie?.addAll(it) }
                if (mLayoutUpcoming.totalPage == null) {
                    mLayoutUpcoming.totalPage = dataResult.total_pages
                }
                mLayoutUpcoming.page?.let {
                    if (mLayoutUpcoming.totalPage != null) {
                        if (it < mLayoutUpcoming.totalPage!!) {
                            mLayoutUpcoming.page = it + 1
                        }
                    } else {
                        mLayoutUpcoming.page = it + 1
                    }
                }
            }
            return@withContext dataResult
        }
    }

    // Start function Load more Popular
    fun checkLoadMorePopular() : Boolean {
        return if (mLayoutPopular.page != null && mLayoutPopular.totalPage != null && mLayoutPopular.page!! >= mLayoutPopular.totalPage!!) {
            true
        } else isLoadDataPopular
    }

    fun updateLoadMorePopular() {
        isLoadDataPopular = true
        if (mLayoutPopular.listMovie != null && mLayoutPopular.listMovie!!.contains(typeStringLoadMorePopular)) {
            mLayoutPopular.listMovie?.remove(typeStringLoadMorePopular)
        }
        mLayoutPopular.listMovie?.add(typeStringLoadMorePopular)
        mLayoutPopular.isScrollEnd = true
    }

    fun getIndexPopular() : Int? {
        return mArrayShowLayout?.indexOf(mLayoutPopular)
    }
    // End function Load more Popular

    // Start function Load more Top Rated
    fun checkLoadMoreTopRated() : Boolean {
        return if (mLayoutTopRated.page != null && mLayoutTopRated.totalPage != null && mLayoutTopRated.page!! >= mLayoutTopRated.totalPage!!) {
            true
        } else isLoadDataTopRated
    }

    fun updateLoadMoreTopRated() {
        isLoadDataTopRated = true
        if (mLayoutTopRated.listMovie != null && mLayoutTopRated.listMovie!!.contains(typeStringLoadMorePopular)) {
            mLayoutTopRated.listMovie?.remove(typeStringLoadMorePopular)
        }
        mLayoutTopRated.listMovie?.add(typeStringLoadMorePopular)
        mLayoutTopRated.isScrollEnd = true
    }

    fun getIndexTopRated() : Int? {
        return mArrayShowLayout?.indexOf(mLayoutTopRated)
    }
    // End function Load more Top Rated

    // Start function Load more Upcoming
    fun checkLoadMoreUpcoming() : Boolean {
        return if (mLayoutUpcoming.page != null && mLayoutUpcoming.totalPage != null && mLayoutUpcoming.page!! >= mLayoutUpcoming.totalPage!!) {
            true
        } else isLoadDataUpcoming
    }

    fun updateLoadMoreUpcoming() {
        isLoadDataUpcoming = true
        if (mLayoutUpcoming.listMovie != null && mLayoutUpcoming.listMovie!!.contains(typeStringLoadMorePopular)) {
            mLayoutUpcoming.listMovie?.remove(typeStringLoadMorePopular)
        }
        mLayoutUpcoming.listMovie?.add(typeStringLoadMorePopular)
        mLayoutUpcoming.isScrollEnd = true
    }

    fun getIndexUpcoming() : Int? {
        return mArrayShowLayout?.indexOf(mLayoutUpcoming)
    }
    // End function Load more Upcoming
}