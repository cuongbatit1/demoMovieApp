package com.studio.king.demomovie

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.WindowManager
import com.studio.king.demomovie.base.BaseActivity
import com.studio.king.demomovie.base.IOnBackPressed
import com.studio.king.demomovie.databinding.MainActivityBinding
import com.studio.king.demomovie.navigator.FragNavigatorController
import com.studio.king.demomovie.navigator.NavigatorScreen
import com.studio.king.demomovie.network.NoConnectivityException
import com.studio.king.demomovie.utils.LogUtil
import com.studio.king.demomovie.utils.showMessageDialog
import com.studio.king.demomovie.viewmodel.MainActivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<MainActivityBinding>() {

    companion object {
        var isMainInitialized: Boolean = false
    }

    private val mNavigatorScreen: NavigatorScreen by inject()
    private val mMainActivityViewModel: MainActivityViewModel by inject()


    private val mFragNavigatorController: FragNavigatorController by lazy {
        FragNavigatorController(supportFragmentManager, R.id.frame_body)
    }

    override fun onBindingCreated(savedInstanceState: Bundle?) {
        super.onBindingCreated(savedInstanceState)
        mNavigatorScreen.setNavController(mFragNavigatorController)


        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display?.let {
            val realSize = Point()
            Display::class.java.getMethod("getRealSize", Point::class.java).invoke(it, realSize)
            val pWidth = realSize.x
            val pHeight = realSize.y
            mMainActivityViewModel.mWidthApp = pWidth
            mMainActivityViewModel.mHeightApp = pHeight
            LogUtil.d("pWidth Main: $pWidth - $pHeight")
            LogUtil.d("Scacle Main: ${(pWidth.toFloat() / pHeight.toFloat())}")
            mMainActivityViewModel.mScaleApp = pWidth.toFloat() / pHeight.toFloat()
        }

        isMainInitialized = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT

//            window.navigationBarColor = ContextCompat.getColor(
//                this@MainActivity,
//                R.color.colorPrimary
//            )
        }

        mNavigatorScreen.showFragSplashScreen()
        val currentNightMode = Configuration.UI_MODE_NIGHT_MASK
        LogUtil.e("currentNightMode : $currentNightMode")
//        lifecycle.addObserver(PicassoProvider.get())

        loadDataFirst()

    }


    private fun loadDataFirst() {
        uiScope.launch {
            try {
                delay(2000)
                binding?.bgFrameMain?.setBackgroundResource(R.color.color_bg_fragment)
                mNavigatorScreen.showFragHome()
            } catch (e: Exception) {
                // Exception thrown in async WILL NOT be caught here
                // but propagated up to the scope
                if (e is NoConnectivityException) {
                    showMessageDialog(
                            this@MainActivity,
                            message = getString(R.string.key_no_internet_connection),
                            onOk = kotlinx.coroutines.Runnable { loadDataFirst() }
                    )
                } else {
                    showMessageDialog(this@MainActivity, message = e.toString())
                }
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMainActivityViewModel.saveState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
//        LogUtil.d("MainActivity onConfigurationChanged : " + mMainActivityViewModel.mThemeMode)
        recreate()
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onPostResume() {
        super.onPostResume()
        LogUtil.e("onPostResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        isMainInitialized = false
        mMainActivityViewModel.resetDataDestroy()
        MovieApplication.getRefWatcher(this@MainActivity)?.watch(this@MainActivity)
    }


    override fun onBackPressed() {
        val f = mFragNavigatorController.getCurrentFragment()
        (f as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            if (!it) {
                onBackPressedForce()
            }
            return
        }
        onBackPressedForce()
    }

    fun onBackPressedForce() {
        if (mFragNavigatorController.canGoBack()) {
            mFragNavigatorController.goBack()
        } else {
            check2sBack()
        }
    }

    fun isCountBackStack(): Boolean {
        return mFragNavigatorController.canGoBack()
    }
}