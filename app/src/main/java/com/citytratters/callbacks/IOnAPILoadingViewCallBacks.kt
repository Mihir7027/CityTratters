package com.citytratters.callbacks

import com.citytratters.network.RestResponse


interface IOnAPILoadingViewCallBacks {
    fun <T> responseStatusSuccess(it: RestResponse<T>)
    fun <T> responseStatusError(it: RestResponse<T>)
    fun responseStatusLoading()
}