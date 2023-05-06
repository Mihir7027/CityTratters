package com.citytratters.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.telkomyellow.utils.UiUtils


abstract class BaseForSplashActivity : AppCompatActivity() {


    var city: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    fun showProgressDialog(title: String?, message: String?, cancelable: Boolean = false) {
        UiUtils.showProgressDialog(this, title, message, cancelable)
    }

    fun hideProgressDialog() {
        UiUtils.dismissProgressDialog()
    }

    fun showSnackBar(string: String?, positive: Boolean) {
        UiUtils.showSnackBar(this, string, positive)
    }


}