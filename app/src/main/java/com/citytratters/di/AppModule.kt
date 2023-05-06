package com.citytratters.di

import com.citytratters.base.BaseRepository
import com.citytratters.base.BaseViewModel
import com.citytratters.repository.RepositoryCommon
import com.citytratters.viewmodel.ViewModelCommon
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel { BaseViewModel<BaseRepository>(get()) }
    single { BaseRepository() }

    viewModel { ViewModelCommon(get()) }
    single { RepositoryCommon() }
}