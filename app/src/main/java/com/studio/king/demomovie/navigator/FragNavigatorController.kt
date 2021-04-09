package com.studio.king.demomovie.navigator

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.studio.king.demomovie.R
import com.studio.king.demomovie.base.BaseFragment
import com.studio.king.demomovie.utils.LogUtil
import kotlinx.coroutines.InternalCoroutinesApi

class FragNavigatorController(private val fragmentManager : FragmentManager, @IdRes private val containerId: Int) {

    private var mFrag: BaseFragment<*>? = null
    private var mIsAddBackStack: Boolean? = null
    private var mIsClearStack: Boolean? = null

    fun replaceRemoveFragment(
        f: BaseFragment<*>
    ) {

        val ft = fragmentManager.beginTransaction()
        ft.setCustomAnimations(
            R.anim.abc_fade_in,
            R.anim.abc_fade_out,
            R.anim.abc_fade_in,
            R.anim.abc_fade_out
        )
        getCurrentFragment()?.let {
            fragmentManager.popBackStack()
        }

        ft.replace(R.id.frame_body, f, f.TAGNAME)
        ft.addToBackStack(f.javaClass.simpleName)
        ft.commitAllowingStateLoss()
    }

    fun replaceFragment(
        f: BaseFragment<*>
    ) {
        val ft = fragmentManager.beginTransaction()
        ft.setCustomAnimations(
            R.anim.abc_fade_in,
            R.anim.abc_fade_out,
            R.anim.abc_fade_in,
            R.anim.abc_fade_out
        )

        ft.replace(R.id.frame_body, f, f.TAGNAME)
        ft.commitAllowingStateLoss()
    }

    fun pushFragment(
        f: BaseFragment<*>
    ) {
        val ft = fragmentManager.beginTransaction()
        ft.setCustomAnimations(
            R.anim.abc_fade_in,
            R.anim.abc_fade_out,
            R.anim.abc_fade_in,
            R.anim.abc_fade_out
        )
        ft.replace(containerId, f, f.TAGNAME)

        ft.addToBackStack(f.javaClass.simpleName)

        ft.commitAllowingStateLoss()
    }

    fun clearBackStack() {

        val manager = fragmentManager
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
        for (fragment in fragmentManager.fragments) {
            fragmentManager.fragments.remove(fragment)
            fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
    }

    fun currentStack() : Int {
        return fragmentManager.backStackEntryCount
    }
    fun popFragment() {
        fragmentManager.popBackStack()
    }

    fun popFragmentByName(nameFragment : String) : Boolean {
        if (fragmentManager.backStackEntryCount > 0) {
            LogUtil.e("pop: $nameFragment")
            fragmentManager.popBackStack(nameFragment, 0)
            return true
        }
        return false
    }

    fun findFragmentByName(nameFragment : String) : Fragment? {
        return fragmentManager.findFragmentByTag(nameFragment)
    }

    fun getCurrentFragment() : Fragment? {
        return fragmentManager.findFragmentById(containerId)
    }
    fun canGoBack() : Boolean {
        return fragmentManager.backStackEntryCount > 0
    }

    fun goBack() {
        fragmentManager.popBackStack()
    }

    fun popFragmentRoot() : Boolean {
        if (fragmentManager.backStackEntryCount > 0) {
            for (i in 0 until fragmentManager.backStackEntryCount) {
                goBack()
            }
            return true
        }
        return false
    }

//    fun postResume() {
//        val fragCurrent = supportFragmentManager.findFragmentById(R.id.frame_body)
//        this.isPostResume = fragCurrent !is FragSplashScreen
//
//        Logger.dt("onPostResume $isPostResume", 4)
//        if (mFrag != null && mIsAddBackStack != null && mIsClearStack != null) {
//            Logger.dt("onPostResume change Fragment: ${mFrag?.javaClass?.simpleName}", 4)
//            val ft = supportFragmentManager.beginTransaction()
//            if (mIsClearStack!!) {
//                clearBackStack()
//            }
//            ft.setCustomAnimations(
//                R.anim.abc_fade_in,
//                R.anim.abc_fade_out,
//                R.anim.abc_fade_in,
//                R.anim.abc_fade_out
//            )
//            //ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom, R.anim.abc_slide_in_top, R.anim.abc_slide_out_top)
//            ft.replace(R.id.frame_body, mFrag!!, mFrag!!.TAGNAME)
//            if (mIsAddBackStack!!) {
//                Logger.dt("mFrag.tag :" + mFrag!!.javaClass.simpleName, 4)
//                ft.addToBackStack(mFrag!!.javaClass.simpleName)
//            }
//            ft.commit()
//            mFrag = null
//            mIsAddBackStack = null
//            mIsClearStack = null
//        }
//    }

}