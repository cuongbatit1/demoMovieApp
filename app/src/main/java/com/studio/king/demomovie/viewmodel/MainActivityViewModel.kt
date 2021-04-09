package com.studio.king.demomovie.viewmodel

import androidx.lifecycle.ViewModel
import com.studio.king.demomovie.utils.LogUtil

class MainActivityViewModel() : ViewModel() {

    companion object {
    }

    var mWidthApp: Int = 0
    var mHeightApp: Int = 0
    var mScaleApp = 0.0f


    //No save State

    fun resetDataDestroy() {

    }

    fun saveState() {

    }

    fun getState() {
        LogUtil.d("MainActivitySavedState getState")

    }









}