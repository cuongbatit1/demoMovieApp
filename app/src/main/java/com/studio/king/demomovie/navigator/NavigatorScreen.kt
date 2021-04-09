package com.studio.king.demomovie.navigator

import com.studio.king.demomovie.fragment.FragHome
import com.studio.king.demomovie.fragment.FragSplashScreen


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
//
//    fun showFragLogin(isBackStack: Boolean = false) {
//        val fragLogin = FragLogin.newInstance()
//        if (isBackStack) {
//            mFragNavigatorController.clearBackStack()
//            mFragNavigatorController.replaceFragment(fragLogin)
//        } else {
//            mFragNavigatorController.pushFragment(fragLogin)
//        }
//    }
}