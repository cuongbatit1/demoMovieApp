package com.studio.king.demomovie.navigator

import com.studio.king.demomovie.fragment.FragDetailMovie
import com.studio.king.demomovie.fragment.FragHome
import com.studio.king.demomovie.fragment.FragSplashScreen
import com.studio.king.demomovie.model.MovieModel


class NavigatorScreen(){

    private lateinit var mFragNavigatorController: FragNavigatorController


    fun setNavController(fragNavController: FragNavigatorController) {
        mFragNavigatorController = fragNavController
    }

    fun showFragSplashScreen() {
        val fragSplashScreen = FragSplashScreen.newInstance()
        mFragNavigatorController.clearBackStack()
        mFragNavigatorController.replaceFragment(fragSplashScreen)
    }

    fun showFragHome() {
        val frag = FragHome.newInstance()
        mFragNavigatorController.clearBackStack()
        mFragNavigatorController.replaceFragment(frag)
    }

    fun showFragDetailMovie(isBackStack: Boolean = false, itemMovie : MovieModel? = null) {
        val frag = FragDetailMovie.newInstance(itemMovie)
        if (isBackStack) {
            mFragNavigatorController.clearBackStack()
            mFragNavigatorController.replaceFragment(frag)
        } else {
            mFragNavigatorController.pushFragment(frag)
        }
    }
}