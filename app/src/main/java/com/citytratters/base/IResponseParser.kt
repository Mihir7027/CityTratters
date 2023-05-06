package com.citytratters.base

import com.citytratters.callbacks.IOnAPILoadingViewCallBacks
import com.citytratters.network.RestResponse


open class IResponseParser<T>(private val loadingViewCallBack: IOnAPILoadingViewCallBacks?) {

   /* fun onLogin() {

        loadingViewCallBack?.responseStatusLogin()

    }*/

    open fun onSuccess(it: RestResponse<T>) {
        loadingViewCallBack?.responseStatusSuccess(it)

    }

    open fun onError(it: RestResponse<T>) {
        loadingViewCallBack?.responseStatusError(it)

    }

    fun onLoading() {
        loadingViewCallBack?.responseStatusLoading()


    }
}