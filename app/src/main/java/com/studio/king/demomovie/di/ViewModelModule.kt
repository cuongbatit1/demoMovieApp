package com.studio.king.demomovie.di

import com.studio.king.demomovie.viewmodel.DetailMovieViewModel
import com.studio.king.demomovie.viewmodel.HomeViewModel
import com.studio.king.demomovie.viewmodel.MainActivityViewModel
import com.studio.king.demomovie.viewmodel.repository.HomeRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // MainActivity Data
    single { MainActivityViewModel() }

    single { HomeRepository(get()) }

    viewModel { HomeViewModel(get()) }

    viewModel { DetailMovieViewModel(get()) }
}