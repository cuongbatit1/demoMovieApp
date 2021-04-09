package com.studio.king.demomovie.base

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hold1.keyboardheightprovider.KeyboardHeightProvider
import com.kaopiz.kprogresshud.KProgressHUD
import com.studio.king.demomovie.R
import com.studio.king.demomovie.utils.LogUtil
import com.studio.king.demomovie.utils.snackBarCustom
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.lang.reflect.ParameterizedType


open class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    val TAGNAME = javaClass.simpleName
    protected var isSavedState: Boolean = false

    companion object {
        private const val KEY_SAVEINSTANCE = "SaveInstance"
    }

    //temple ViewBinding Activity
    private var _binding: B? = null
    val binding: B?
        get() = _binding

    // https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
    protected val uiScope = CoroutineScope(Dispatchers.Main)
    protected val uiDispatcher: CoroutineDispatcher = Dispatchers.Main
    protected val bgDispatcher: CoroutineDispatcher = Dispatchers.IO

    private var doubleBackToExitPressedOnce = false

    var isPostResume: Boolean = false
    protected var isResume: Boolean = true

    private val progressDialog: KProgressHUD by lazy {
        KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setAnimationSpeed(2)
            .setCancellable(false)
    }

    protected val mOnTouchListener by lazy {
        View.OnTouchListener { _, _ -> true }
    }
//    private val progressDialog: DialogLoadingGif by lazy {
//        DialogLoadingGif(this@ActivityBase)
//    }

    val keyboardHeightProvider: KeyboardHeightProvider by lazy { KeyboardHeightProvider(this@BaseActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d("$TAGNAME ==> onCreate")

//        UtilLaPoste.setLocale(this, getString(R.string.language_key))
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVEINSTANCE)) {
            isSavedState = savedInstanceState.getBoolean(KEY_SAVEINSTANCE)
            LogUtil.d("$TAGNAME ==> SaveInstance: $isSavedState")
        }
        isResume = true
        //init viewBinding Activity
        val bindingClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.first() as Class<B>
        val inflate = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java
        )
        _binding = inflate(null, layoutInflater) as B
        setContentView(binding?.root)
        onBindingCreated(savedInstanceState)
//        db.collection("member/_user1_user2/message")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Logger.dt("addOnSuccessListener : ${document.id} => ${document.data}", 4)
//                }
//            }
//            .addOnFailureListener { exception ->
//                Logger.dt( "Error getting documents." + exception, 4)
//            }
    }

    protected open fun onBindingCreated(savedInstanceState: Bundle?) {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        isResume = false
        LogUtil.d("$TAGNAME ==> onSaveInstanceState")
        outState.putBoolean(KEY_SAVEINSTANCE, true)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        LogUtil.d("$TAGNAME ==> onRestoreInstanceState")
    }

    override fun onStart() {
        isResume = true
        super.onStart()
        LogUtil.d("$TAGNAME ==> onStart")

    }

    override fun onRestart() {
        super.onRestart()
        LogUtil.d("$TAGNAME ==> onRestart")
    }

    override fun onResume() {
        isResume = true
        super.onResume()
        LogUtil.d("$TAGNAME ==> onResume")
    }

    override fun onPause() {
        isResume = false
        super.onPause()
        LogUtil.d("$TAGNAME ==> onPause")
    }

    override fun onDestroy() {
        isResume = false
        super.onDestroy()
        LogUtil.d("$TAGNAME ==> onDestroy")
        _binding = null
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * Show progress dialog full Screen.
     *
     * @param context
     */
    fun showProgressDialog() {
        try {
            if (!progressDialog.isShowing) {
                progressDialog.show()
            }
        } catch (e: Exception) {

        }
    }

    /**
     * Hide progress Dialog.
     */
    fun hideProgressDialog() {
        try {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        } catch (e: Exception) {
        }
    }


    protected fun clearBackStack() {

        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            manager.popBackStackImmediate(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
//        if (manager.backStackEntryCount > 0) {
//            val first = manager.getBackStackEntryAt(0)
//            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        }
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.fragments.remove(fragment)
            supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
    }

    protected fun check2sBack() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        snackBarCustom(window.decorView.rootView.findViewById(android.R.id.content), getString(R.string.key_please_click_back_again_to_exit))

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

}