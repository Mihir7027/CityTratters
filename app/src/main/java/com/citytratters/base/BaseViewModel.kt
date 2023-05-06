package com.citytratters.base

import androidx.lifecycle.ViewModel
import com.citytratters.utils.LogUtils

open class BaseViewModel<T : BaseRepository?>(var repository: T): ViewModel() {

    override fun onCleared() {
        LogUtils.d(null, "on cleared called and repository is $repository")
//        repository?.onCleared()
        super.onCleared()
    }
}