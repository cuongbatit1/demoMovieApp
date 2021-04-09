package com.studio.king.demomovie.base

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionSet
import androidx.viewbinding.ViewBinding
import com.hold1.keyboardheightprovider.KeyboardHeightProvider
import com.studio.king.demomovie.R
import com.studio.king.demomovie.network.NoConnectivityException
import com.studio.king.demomovie.utils.LogUtil
import com.studio.king.demomovie.utils.showMessageDialog
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.lang.reflect.ParameterizedType
import java.text.DecimalFormat
import java.util.*

abstract class BaseFragment<B : ViewBinding>: Fragment(), KeyboardHeightProvider.KeyboardListener {

    companion object {
        const val TAG_ID = 0x01

        private const val KEY_SAVEINSTANCE_FRAGMENT = "SaveInstanceFragment"
    }

    //temple ViewBinding
    private var _binding: B? = null
    val binding: B?
        get() = _binding

    // https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
    protected val scopeJob by lazy { CoroutineScope(Job()) }
    protected val uiScope by lazy { CoroutineScope(Dispatchers.Main) }
    protected val uiDispatcher: CoroutineDispatcher by lazy { Dispatchers.Main }
    protected val bgDispatcher: CoroutineDispatcher by lazy { Dispatchers.IO }


//    protected val mMainActivityViewModel : MainActivityViewModel by inject()
//    protected val mNavigatorScreen: NavigatorScreen by inject()
//    protected val mNetwork: NetworkMonitor by inject()
    var fragId: Int = 0
    protected var isSavedState: Boolean = false
    val TAGNAME = javaClass.simpleName
    var isResume: Boolean = true

    protected val keyboardHeightProvider: KeyboardHeightProvider? by lazy {
        return@lazy if (activity is BaseActivity<*>) {
            val activityBase = activity as BaseActivity<*>
            activityBase.keyboardHeightProvider
        } else {
            null
        }
    }

    val transitionSet: TransitionSet by lazy {
        TransitionSet()
            .apply {
                addTransition(ChangeBounds())
                duration = 100
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtil.d("$TAGNAME ==> onAttach")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d("$TAGNAME ==> onCreate")
        isResume = false

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVEINSTANCE_FRAGMENT)) {
            isSavedState = savedInstanceState.getBoolean(KEY_SAVEINSTANCE_FRAGMENT)
            fragId = savedInstanceState.getInt("fragId")
            LogUtil.d("$TAGNAME ==> SaveInstance: $isSavedState")
            LogUtil.d("$TAGNAME ==> SaveInstance: $fragId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding?.root
        ?: ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.first() as Class<B>).getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        ).let { invoke ->

            if (view != null) {
                val parentViewGroup = view?.parent as ViewGroup?
                parentViewGroup?.removeAllViews()
            }
            return@let (invoke(null, inflater, container, false) as B).also {
                _binding = it
                onBindingCreated(savedInstanceState)
            }.root
        }

    protected open fun onBindingCreated(savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isResume = true
        LogUtil.d("$TAGNAME ==> onActivityCreated")
        keyboardHeightProvider?.addKeyboardListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        isResume = false
        LogUtil.d("$TAGNAME ==> onSaveInstanceState")
        outState.putBoolean(KEY_SAVEINSTANCE_FRAGMENT, true)
        outState.putInt("fragId", fragId)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.d("$TAGNAME ==> onDestroyView")
        keyboardHeightProvider?.removeKeyboardListener(this)

    }

    override fun onDetach() {
        super.onDetach()
        LogUtil.d("$TAGNAME ==> onDetach")
    }

    override fun onStart() {
        super.onStart()
        isResume = true
        LogUtil.d("$TAGNAME ==> onStart")
    }

    override fun onStop() {
        super.onStop()
        isResume = false
        LogUtil.d("$TAGNAME ==> onStop")
    }

    override fun onPause() {
        super.onPause()
        isResume = false
        LogUtil.d("$TAGNAME ==> onPause")
        keyboardHeightProvider?.onPause()
    }

    override fun onResume() {
        super.onResume()
        isResume = true
        LogUtil.d("$TAGNAME ==> onResume")
        keyboardHeightProvider?.onResume()
    }

    override fun onHeightChanged(height: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        isResume = false
        LogUtil.d("$TAGNAME ==> onDestroy")
        _binding = null
    }

    fun showDialogException(e: Exception) {
        if (e is NoConnectivityException) {
            activity?.let {
                showMessageDialog(
                    it,
                    message = getString(R.string.key_no_internet_connection)
                )
            }
        } else {
            activity?.let {
                showMessageDialog(it, message = e.toString())
            }

        }
    }
    fun showDialogAvailableSoon() {
        activity?.let {
            showMessageDialog(
                it,
                getString(R.string.txt_notification_title),
                getString(R.string.this_feature_will_be_available_soon)
            )
        }
    }


//    fun actionCheckInternet(actionBlock: () -> Unit) {
//        if (mNetwork.isConnected()) {
//            actionBlock.invoke()
//        } else {
//            activity?.let {
//                showMessageDialog(
//                    it,
//                    message = getString(R.string.key_no_internet_connection)
//                )
//            }
//        }
//    }

    fun openUrlCustome(url: String) {
        val urlnew = if (url.startsWith("https://") || url.startsWith("http://")) {
            url
        } else {
            "https://$url"
        }
        activity?.let {
            val providerName = CustomTabsClient.getPackageName(it, null)
            //                val store = SharedPreferencesTokenStore(mMainActivity)
            val customTabsIntent = CustomTabsIntent.Builder()

            customTabsIntent.setToolbarColor(resources.getColor(R.color.colorPrimaryDark))
            customTabsIntent.setSecondaryToolbarColor(resources.getColor(R.color.colorPrimary))
//            customTabsIntent.addDefaultShareMenuItem()
            customTabsIntent.setShowTitle(true)
            customTabsIntent.enableUrlBarHiding()
//            customTabsIntent.setCloseButtonIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_back_material))

            val intent = customTabsIntent.build()
            intent.intent.setPackage(providerName)
//                store.setVerifiedProvider(providerName, mMainActivity.getPackageManager())

            intent.launchUrl(it, Uri.parse(urlnew))

        }

    }
}