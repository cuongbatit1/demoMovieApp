package com.studio.king.demomovie.fragment

import android.content.Context
import android.os.Bundle
import com.studio.king.demomovie.MainActivity
import com.studio.king.demomovie.MovieApplication
import com.studio.king.demomovie.base.BaseFragment
import com.studio.king.demomovie.databinding.FragSplashScreenBinding

class FragSplashScreen  : BaseFragment<FragSplashScreenBinding>() {


    companion object {

        /**
         * Use this member method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param myObject as MyObject.
         * @return A new instance of fragment MyFragment.
         */
        fun newInstance(): FragSplashScreen {
            val fragment = FragSplashScreen()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var mMainActivity: MainActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity != null) {
            mMainActivity = activity as MainActivity
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MovieApplication.getRefWatcher(mMainActivity)?.watch(this@FragSplashScreen)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isSavedState) {

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isSavedState && savedInstanceState != null) {

        } else {

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}