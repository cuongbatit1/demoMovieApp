package com.studio.king.demomovie.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.studio.king.demomovie.MainActivity
import com.studio.king.demomovie.MovieApplication
import com.studio.king.demomovie.adapter.HomeAdapter
import com.studio.king.demomovie.base.BaseFragment
import com.studio.king.demomovie.databinding.FragHomeBinding
import com.studio.king.demomovie.databinding.ToolbarHomeBinding
import com.studio.king.demomovie.model.GenresModel
import com.studio.king.demomovie.model.LayoutUIModel
import com.studio.king.demomovie.model.MovieModel
import com.studio.king.demomovie.navigator.NavigatorScreen
import com.studio.king.demomovie.utils.LogUtil
import com.studio.king.demomovie.utils.TypeDataHome
import com.studio.king.demomovie.utils.setSingleClick
import com.studio.king.demomovie.utils.showMessageDialog
import com.studio.king.demomovie.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragHome : BaseFragment<FragHomeBinding>() {


    companion object {

        /**
         * Use this member method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param myObject as MyObject.
         * @return A new instance of fragment MyFragment.
         */
        fun newInstance(): FragHome {
            val fragment = FragHome()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
    private val mNavigatorScreen: NavigatorScreen by inject()

    private var bindingToolbar : ToolbarHomeBinding? = null
    private lateinit var mMainActivity: MainActivity

    private val mHomeViewModel: HomeViewModel by viewModel()

    private val mHomeAdapter: HomeAdapter by lazy {
        HomeAdapter(::actionClickItem,::actionLoadMore)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity != null) {
            mMainActivity = activity as MainActivity
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MovieApplication.getRefWatcher(mMainActivity)?.watch(this@FragHome)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mHomeViewModel.saveState()
        super.onSaveInstanceState(outState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isSavedState && savedInstanceState != null) {
            // load data from savedInstanceState
        } else {
            mHomeViewModel.initViewModel()
        }

        loadDataApi()
    }

    override fun onBindingCreated(savedInstanceState: Bundle?) {
        super.onBindingCreated(savedInstanceState)
        bindingToolbar = binding?.viewToolbarHome
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isSavedState && savedInstanceState != null) {

        } else {

        }
        buildUIToolbarHome()
        buildUIBtnSearch()
        buildUIRecyclerView()
        buildUISwipeRefreshLayout()
        buildObserveLoadingUI()

        buildObserveUIHidePullToRefresh()
    }



    override fun onResume() {
        super.onResume()
    }


    private fun buildUIToolbarHome() {
        binding?.toolbarHome?.setPadding(binding?.toolbarHome?.paddingLeft ?: 0, mMainActivity.getStatusBarHeight(),
            binding?.toolbarHome?.paddingRight ?: 0, binding?.toolbarHome?.paddingBottom ?: 0)
    }

    private fun buildUIBtnSearch() {
        bindingToolbar?.btnSearchToolbarHome?.setSingleClick {
            showDialogAvailableSoon()
        }
    }

    private fun buildUIRecyclerView() {
        binding?.recyclerViewHome?.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            itemAnimator = null
            setRecycledViewPool(RecyclerView.RecycledViewPool())
        }

        if (binding?.recyclerViewHome?.itemDecorationCount == 0) {
            binding?.recyclerViewHome?.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view) // item position
                    if (position >= 0 && mHomeViewModel.mArrayShowLayout != null && mHomeViewModel.mArrayShowLayout!!.size > position) {
                        val item = mHomeViewModel.mArrayShowLayout.let {
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
        binding?.recyclerViewHome?.adapter = mHomeAdapter
        mHomeViewModel.mArrayShowLayoutLive.observe(viewLifecycleOwner, observerArrayShowLayout)
    }

    private fun buildUISwipeRefreshLayout() {
        binding?.swipeRefreshLayoutHome?.setOnRefreshListener {
            actionPullToRefresh()
        }
    }

    private fun buildObserveLoadingUI() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mHomeViewModel.isShowLoadingLive.observe(viewLifecycleOwner, observerLoading)
        }
    }


    private fun buildObserveUIHidePullToRefresh() {
        mHomeViewModel.mStatePullToRefreshLive.observe(viewLifecycleOwner, observerPullToRefresh)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHomeViewModel.mArrayShowLayoutLive.removeObserver(observerArrayShowLayout)
        mHomeViewModel.isShowLoadingLive.removeObserver(observerLoading)
        mHomeViewModel.mStatePullToRefreshLive.removeObserver(observerPullToRefresh)
    }


    // Observer of fragment

    private val observerArrayShowLayout: Observer<MutableList<Any>?> by lazy {
        Observer<MutableList<Any>?> {
            LogUtil.d("Observer observerArrayShowLayout")
            mHomeAdapter.mList = it
            mHomeAdapter.notifyDataSetChanged()
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
            if (it) {
                if (binding?.swipeRefreshLayoutHome?.isRefreshing == false) {
                    binding?.swipeRefreshLayoutHome?.isRefreshing = true
                } else {
                    binding?.swipeRefreshLayoutHome?.isRefreshing = false
                    binding?.swipeRefreshLayoutHome?.isRefreshing = true
                }
            } else {
                binding?.swipeRefreshLayoutHome?.isRefreshing = false
            }
        }
    }

    // end Observer of fragment

    private fun loadDataApi() {
        uiScope.launch {
            try {
                mHomeViewModel.isShowLoadingLive.value = true
                val data = mHomeViewModel.taskLoadData()
                mHomeViewModel.updateUIShowLayout()
                mHomeViewModel.isShowLoadingLive.value = false

            } catch (e: Exception) {
                // Exception thrown in async WILL NOT be caught here
                // but propagated up to the scope
                mHomeViewModel.isShowLoadingLive.value = false
                showDialogException(e)
            }
        }
    }

    private fun actionPullToRefresh() {
        uiScope.launch {
            try {
                mHomeViewModel.mStatePullToRefreshLive.value = true
                val data = mHomeViewModel.taskLoadData()
                mHomeViewModel.updateUIShowLayout()
                mHomeViewModel.mStatePullToRefreshLive.value = false
            } catch (e: Exception) {
                // Exception thrown in async WILL NOT be caught here
                // but propagated up to the scope
                showDialogException(e)
                mHomeViewModel.mStatePullToRefreshLive.value = false
            }
        }
    }


    private fun actionLoadMore(data: Any?) {
        if (data is LayoutUIModel) {
            when (data.typeData) {
                TypeDataHome.TYPE_UPCOMING -> {
                    loadMoreUpcoming()
                }
                TypeDataHome.TYPE_TOP_RATED -> {
                    loadMoreTopRated()
                }
                TypeDataHome.TYPE_POPULAR -> {
                    loadMorePopular()
                }
            }
        }
    }

    private fun actionClickItem(data: Any?) {
        if (data is MovieModel) {
            mNavigatorScreen.showFragDetailMovie(itemMovie = data)
        } else if (data is GenresModel) {
            showDialogAvailableSoon()
        }
    }

    private fun loadMorePopular() {
        LogUtil.e("loadMorePopular")
        uiScope.launch {
            try {
                if (!mHomeViewModel.checkLoadMorePopular()) {
                    LogUtil.e("loadMorePopular call api ")
                    val indexLoad = mHomeViewModel.getIndexPopular()
                    mHomeViewModel.updateLoadMorePopular()
                    if (indexLoad != null && indexLoad >= 0) {
                        mHomeAdapter.notifyItemChanged(indexLoad)
                    } else {
                        mHomeAdapter.notifyDataSetChanged()
                    }
                    val data = mHomeViewModel.taskLoadListPopularApi()
                    if (indexLoad != null && indexLoad >= 0) {
                        mHomeAdapter.notifyItemChanged(indexLoad)
                    } else {
                        mHomeAdapter.notifyDataSetChanged()
                    }
                    mHomeViewModel.isLoadDataPopular = false
                }
            } catch (e: Exception) {
                // Exception thrown in async WILL NOT be caught here
                // but propagated up to the scope
                showDialogException(e)
                mHomeViewModel.isLoadDataPopular = false
            }
        }
    }

    private fun loadMoreTopRated() {
        LogUtil.e("loadMoreTopRated")
        uiScope.launch {
            try {
                if (!mHomeViewModel.checkLoadMoreTopRated()) {
                    LogUtil.e("loadMoreTopRated call api ")
                    val indexLoad = mHomeViewModel.getIndexTopRated()
                    mHomeViewModel.updateLoadMoreTopRated()
                    if (indexLoad != null && indexLoad >= 0) {
                        mHomeAdapter.notifyItemChanged(indexLoad)
                    } else {
                        mHomeAdapter.notifyDataSetChanged()
                    }
                    val data = mHomeViewModel.taskLoadListTopRatedApi()
                    if (indexLoad != null && indexLoad >= 0) {
                        mHomeAdapter.notifyItemChanged(indexLoad)
                    } else {
                        mHomeAdapter.notifyDataSetChanged()
                    }
                    mHomeViewModel.isLoadDataTopRated = false
                }
            } catch (e: Exception) {
                // Exception thrown in async WILL NOT be caught here
                // but propagated up to the scope
                showDialogException(e)
                mHomeViewModel.isLoadDataTopRated = false
            }
        }
    }

    private fun loadMoreUpcoming() {
        LogUtil.e("loadMoreUpcoming")
        uiScope.launch {
            try {
                if (!mHomeViewModel.checkLoadMoreUpcoming()) {
                    LogUtil.e("loadMoreUpcoming call api ")
                    val indexLoad = mHomeViewModel.getIndexUpcoming()
                    mHomeViewModel.updateLoadMoreUpcoming()
                    if (indexLoad != null && indexLoad >= 0) {
                        mHomeAdapter.notifyItemChanged(indexLoad)
                    } else {
                        mHomeAdapter.notifyDataSetChanged()
                    }
                    val data = mHomeViewModel.taskLoadListUpcomingApi()
                    if (indexLoad != null && indexLoad >= 0) {
                        mHomeAdapter.notifyItemChanged(indexLoad)
                    } else {
                        mHomeAdapter.notifyDataSetChanged()
                    }
                    mHomeViewModel.isLoadDataUpcoming = false
                }
            } catch (e: Exception) {
                // Exception thrown in async WILL NOT be caught here
                // but propagated up to the scope
                showDialogException(e)
                mHomeViewModel.isLoadDataUpcoming = false
            }
        }
    }
}