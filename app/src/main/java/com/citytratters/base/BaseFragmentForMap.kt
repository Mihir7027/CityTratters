package com.citytratters.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.citytratters.R
import com.citytratters.callbacks.IOnAPILoadingViewCallBacks
import com.citytratters.network.RestResponse
import com.citytratters.utils.AndroidUtils
import com.citytratters.viewmodel.ViewModelCommon
import com.google.android.gms.maps.SupportMapFragment
import com.telkomyellow.utils.UiUtils
import org.koin.android.viewmodel.ext.android.viewModel

abstract class BaseFragmentForMap : SupportMapFragment(), IOnAPILoadingViewCallBacks {


    val vmCommon: ViewModelCommon by viewModel()

    var idNumber: String? = null
    var accessToken: String? = null
    var userName: String? = null
    var divisionId: String? = null
    var division: String? = null
    var companyName: String? = null
    var emailID: String? = null
    var phoneNumber: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        return view
    }

    abstract fun getLayoutId(): Int

    private fun showProgressDialog(title: String?, message: String?, cancelable: Boolean = false) {
        UiUtils.showProgressDialog(activity, title, message, cancelable)
    }

    private fun hideProgressDialog() {
        UiUtils.dismissProgressDialog()
    }


    fun showSnackBar(string: String?, positive: Boolean) {
        UiUtils.showSnackBar(activity, string, positive)
    }

    override fun <T> responseStatusSuccess(it: RestResponse<T>) {
        hideProgressDialog()
    }

    override fun <T> responseStatusError(it: RestResponse<T>) {
        hideProgressDialog()
        showSnackBar(it.getErrorMessage(), false)
    }

    override fun responseStatusLoading() {
        showProgressDialog(null, AndroidUtils.getString(R.string.loading))
    }


    fun <T> observationOfAPI(it: RestResponse<T>?, callBack: IResponseParser<T>?) {
        when (it?.status) {

            RestResponse.Status.LOADING -> {
                callBack?.onLoading()
            }

            RestResponse.Status.ERROR -> {

                val message = getString(R.string.response_error_message_for_inactive_user)
                if (message.equals(it.getErrorMessage(), true)) {
                    if (activity is BaseActivity) {
                        //(activity as BaseActivity).showTokenExpireDialog(getString(R.string.account_inactive_message))
                    } else {
                        callBack?.onError(it)
                    }
                } else {
                    callBack?.onError(it)
                }
            }

            RestResponse.Status.SUCCESS -> {
                callBack?.onSuccess(it)
            }
            else -> {}
        }
    }

}