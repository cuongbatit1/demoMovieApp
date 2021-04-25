package com.studio.king.demomovie.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.studio.king.demomovie.MainActivity
import com.studio.king.demomovie.MovieApplication
import com.studio.king.demomovie.R
import com.studio.king.demomovie.adapter.DetailMovieAdapter
import com.studio.king.demomovie.base.BaseFragment
import com.studio.king.demomovie.databinding.FragDetailMovieBinding
import com.studio.king.demomovie.databinding.ToolbarHomeBinding
import com.studio.king.demomovie.model.*
import com.studio.king.demomovie.provider.PicassoProvider
import com.studio.king.demomovie.utils.LogUtil
import com.studio.king.demomovie.utils.TypeDataHome
import com.studio.king.demomovie.utils.showMessageDialog
import com.studio.king.demomovie.viewmodel.DetailMovieViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FragDetailMovie : BaseFragment<FragDetailMovieBinding>() {


    companion object : KoinComponent{
        const val KEY_DATA_MOVIE = "KEY_DATA_MOVIE"
        /**
         * Use this member method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param myObject as MyObject.
         * @return A new instance of fragment MyFragment.
         */
        fun newInstance(itemMovie : MovieModel? = null): FragDetailMovie {
            val fragment = FragDetailMovie()
            itemMovie?.copy()?.let {
//                fragment.mDetailMovieViewModel.mMovieModel = it
            }

            val args = Bundle()
            itemMovie?.let {
                val mGson : Gson by inject()
                args.putString(KEY_DATA_MOVIE, mGson.toJson(itemMovie))
            }
            fragment.arguments = args

            return fragment
        }
    }
    private var bindingToolbar : ToolbarHomeBinding? = null
    private lateinit var mMainActivity: MainActivity

    private val mDetailMovieViewModel : DetailMovieViewModel by viewModel()
    private val mGson : Gson by inject()

    private val mDetailMovieAdapter: DetailMovieAdapter by lazy {
        DetailMovieAdapter(::actionClickItem,::actionLoadMore, ::actionReadMore)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity != null) {
            mMainActivity = activity as MainActivity
        }
        if (arguments != null) {
            if (requireArguments().containsKey(KEY_DATA_MOVIE)) {
                requireArguments().getString(KEY_DATA_MOVIE)?.let {
                    mDetailMovieViewModel.mMovieModel = mGson.fromJson(it, MovieModel::class.java)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MovieApplication.getRefWatcher(mMainActivity)?.watch(this@FragDetailMovie)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mDetailMovieViewModel.saveState()
        super.onSaveInstanceState(outState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isSavedState && savedInstanceState != null) {
            // load data from savedInstanceState
        } else {
            mDetailMovieViewModel.initViewModel()
        }

        loadDataApi()
    }

    override fun onBindingCreated(savedInstanceState: Bundle?) {
        super.onBindingCreated(savedInstanceState)
//        bindingToolbar = binding?.viewToolbarHome
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isSavedState && savedInstanceState != null) {

        } else {

        }
//        buildUIToolbarHome()
//        buildUIBtnSearch()
        buildUIRecyclerView()
//        buildUISwipeRefreshLayout()
        buildObserveLoadingUI()
        buildObserveTopUI()
//
//        buildObserveUIHidePullToRefresh()
    }



    override fun onResume() {
        super.onResume()
    }


//    private fun buildUIToolbarHome() {
//        binding?.toolbarHome?.setPadding(binding?.toolbarHome?.paddingLeft ?: 0, mMainActivity.getStatusBarHeight(),
//            binding?.toolbarHome?.paddingRight ?: 0, binding?.toolbarHome?.paddingBottom ?: 0)
//    }
//
//    private fun buildUIBtnSearch() {
//        bindingToolbar?.btnSearchToolbarHome?.setSingleClick {
//            showDialogAvailableSoon()
//        }
//    }
//
    private fun buildUIRecyclerView() {
        binding?.recyclerViewDetail?.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            itemAnimator = null
            setRecycledViewPool(RecyclerView.RecycledViewPool())
        }

        if (binding?.recyclerViewDetail?.itemDecorationCount == 0) {
            binding?.recyclerViewDetail?.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view) // item position
                    if (position >= 0 && mDetailMovieViewModel.mArrayShowLayout != null && mDetailMovieViewModel.mArrayShowLayout!!.size > position) {
                        val item = mDetailMovieViewModel.mArrayShowLayout.let {
                            it?.get(position)
                        }
                        outRect.left = 0
                        outRect.right = 0
                        outRect.top = 0
                        outRect.bottom = 0
                    }
                }
            })
        }
        binding?.recyclerViewDetail?.adapter = mDetailMovieAdapter
    mDetailMovieViewModel.mArrayShowLayoutLive.observe(viewLifecycleOwner, observerArrayShowLayout)
    }

//    private fun buildUISwipeRefreshLayout() {
//        binding?.swipeRefreshLayoutHome?.setOnRefreshListener {
//            actionPullToRefresh()
//        }
//    }

    private fun buildObserveLoadingUI() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mDetailMovieViewModel.isShowLoadingLive.observe(viewLifecycleOwner, observerLoading)
        }
    }

    private fun buildObserveTopUI() {
        mDetailMovieViewModel.mLoadTopLayoutLive.observe(viewLifecycleOwner, observerTopLayout)
    }


    private fun buildObserveUIHidePullToRefresh() {
        mDetailMovieViewModel.mStatePullToRefreshLive.observe(viewLifecycleOwner, observerPullToRefresh)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDetailMovieViewModel.mArrayShowLayoutLive.removeObserver(observerArrayShowLayout)
        mDetailMovieViewModel.isShowLoadingLive.removeObserver(observerLoading)
        mDetailMovieViewModel.mStatePullToRefreshLive.removeObserver(observerPullToRefresh)
        mDetailMovieViewModel.mLoadTopLayoutLive.removeObserver(observerTopLayout)
    }


    // Observer of fragment

    private val observerArrayShowLayout: Observer<MutableList<Any>?> by lazy {
        Observer<MutableList<Any>?> {
            LogUtil.d("Observer observerArrayShowLayout")
            mDetailMovieAdapter.mList = it
            mDetailMovieAdapter.notifyDataSetChanged()
        }
    }

    private val observerLoading: Observer<Boolean> by lazy {
        Observer<Boolean> { isValue ->
            LogUtil.d("Observer ObserverIsShowLoading $isValue")
            if (isValue) {
                mMainActivity.showProgressDialog()
            } else {
                mMainActivity.hideProgressDialog()
            }
        }
    }

    private val observerPullToRefresh: Observer<Boolean> by lazy {
        Observer<Boolean> {
            LogUtil.i("mHidePullToRefreshShared: $it")
//            if (it) {
//                if (binding?.swipeRefreshLayoutHome?.isRefreshing == false) {
//                    binding?.swipeRefreshLayoutHome?.isRefreshing = true
//                } else {
//                    binding?.swipeRefreshLayoutHome?.isRefreshing = false
//                    binding?.swipeRefreshLayoutHome?.isRefreshing = true
//                }
//            } else {
//                binding?.swipeRefreshLayoutHome?.isRefreshing = false
//            }
        }
    }

    private val observerTopLayout: Observer<MovieModel?> by lazy {
        Observer<MovieModel?> {
            LogUtil.i("observerTopLayout: $it")
            if (it?.getPosterPath() != null) {
                PicassoProvider.get().load(it.getPosterPath()).fit().placeholder(R.drawable.placeholder).error(
                    R.drawable.image_error).into(binding?.imageFAB)
            } else {
                binding?.imageFAB?.setImageResource(R.drawable.image_error)
            }

            if (it?.getBackdropPath() != null) {
                PicassoProvider.get().load(it.getBackdropPath()).fit().placeholder(R.drawable.placeholder).error(
                    R.drawable.image_error).into(binding?.imageBgTop)
            } else {
                binding?.imageBgTop?.setImageResource(R.drawable.image_error)
            }
        }
    }

    // end Observer of fragment

    private fun loadDataApi() {
        uiScope.launch {
            try {
                mDetailMovieViewModel.isShowLoadingLive.value = true
                val data = mDetailMovieViewModel.taskLoadData()
                mDetailMovieViewModel.updateUIShowLayoutTop()
                mDetailMovieViewModel.updateUIShowLayout()
                mDetailMovieViewModel.isShowLoadingLive.value = false

            } catch (e: Exception) {
                // Exception thrown in async WILL NOT be caught here
                // but propagated up to the scope
                mDetailMovieViewModel.isShowLoadingLive.value = false
                showDialogException(e)
            }
        }
    }

    private fun actionPullToRefresh() {
        uiScope.launch {
            try {
                mDetailMovieViewModel.mStatePullToRefreshLive.value = true
                val data = mDetailMovieViewModel.taskLoadData()
                mDetailMovieViewModel.updateUIShowLayout()
                mDetailMovieViewModel.mStatePullToRefreshLive.value = false
            } catch (e: Exception) {
                // Exception thrown in async WILL NOT be caught here
                // but propagated up to the scope
                showDialogException(e)
                mDetailMovieViewModel.mStatePullToRefreshLive.value = false
            }
        }
    }


    private fun actionLoadMore(data: Any?) {
        if (data is LayoutUIModel) {
            when (data.typeData) {
                TypeDataHome.TYPE_RECOMMENDATIONS -> {
                    loadMoreRecommendations()
                }
                TypeDataHome.TYPE_VIDEO -> {
                    loadMoreVideo()
                }
            }
        }
    }

    private fun actionClickItem(data: Any?) {
        if (data is MovieModel) {
            showMessageDialog(mMainActivity, message = data.title)
        } else if (data is GenresModel) {
            showDialogAvailableSoon()
        }
    }

    private fun actionReadMore(data: Any?) {

    }

    private fun loadMoreRecommendations() {
        LogUtil.e("loadMoreRecommendations")
        uiScope.launch {
            try {
                if (!mDetailMovieViewModel.checkLoadMoreRecommendations()) {
                    LogUtil.e("loadMoreUpcoming call api ")
                    val indexLoad = mDetailMovieViewModel.getIndexRecommendations()
                    mDetailMovieViewModel.updateLoadMoreRecommendations()
                    if (indexLoad != null && indexLoad >= 0) {
                        mDetailMovieAdapter.notifyItemChanged(indexLoad)
                    } else {
                        mDetailMovieAdapter.notifyDataSetChanged()
                    }
                    val data = mDetailMovieViewModel.taskLoadListRecommendationsApi()
                    if (indexLoad != null && indexLoad >= 0) {
                        mDetailMovieAdapter.notifyItemChanged(indexLoad)
                    } else {
                        mDetailMovieAdapter.notifyDataSetChanged()
                    }
                    mDetailMovieViewModel.isLoadDataRecommendations = false
                }
            } catch (e: Exception) {
                // Exception thrown in async WILL NOT be caught here
                // but propagated up to the scope
                showDialogException(e)
                mDetailMovieViewModel.isLoadDataRecommendations = false
            }
        }
    }

    private fun loadMoreVideo() {
        LogUtil.e("loadMoreVideo")
        uiScope.launch {
            try {
                if (!mDetailMovieViewModel.checkLoadMoreVideo()) {
                    LogUtil.e("loadMoreUpcoming call api ")
                    val indexLoad = mDetailMovieViewModel.getIndexVideo()
                    mDetailMovieViewModel.updateLoadMoreVideo()
                    if (indexLoad != null && indexLoad >= 0) {
                        mDetailMovieAdapter.notifyItemChanged(indexLoad)
                    } else {
                        mDetailMovieAdapter.notifyDataSetChanged()
                    }
                    val data = mDetailMovieViewModel.taskLoadListVideoApi()
                    if (indexLoad != null && indexLoad >= 0) {
                        mDetailMovieAdapter.notifyItemChanged(indexLoad)
                    } else {
                        mDetailMovieAdapter.notifyDataSetChanged()
                    }
                    mDetailMovieViewModel.isLoadDataVideo = false
                }
            } catch (e: Exception) {
                // Exception thrown in async WILL NOT be caught here
                // but propagated up to the scope
                showDialogException(e)
                mDetailMovieViewModel.isLoadDataVideo = false
            }
        }
    }
}