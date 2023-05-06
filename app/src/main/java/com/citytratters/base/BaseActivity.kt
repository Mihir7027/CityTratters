package com.citytratters.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.citytratters.R
import com.citytratters.callbacks.IOnAPILoadingViewCallBacks
import com.citytratters.constants.MyConfig
import com.citytratters.myPreferance.MyPreference
import com.citytratters.network.RestResponse
import com.citytratters.ui.activity.MainActivity
import com.citytratters.ui.activity.SignInActivity
import com.citytratters.utils.AndroidUtils
import com.citytratters.viewmodel.ViewModelCommon
import com.telkomyellow.utils.UiUtils
import org.koin.android.viewmodel.ext.android.viewModel


abstract class BaseActivity  : AppCompatActivity(), IOnAPILoadingViewCallBacks {

    val vmBase: BaseViewModel<BaseRepository> by viewModel()
    val vmCommon: ViewModelCommon by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //updateLoggedInUserDataObj()
        setContentView(getLayoutId())
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals("pushNotification", ignoreCase = true)) {
                val title = intent.getStringExtra("title")
                val message = intent.getStringExtra("message")
                UiUtils.showAlertDialog(this@BaseActivity, message)
            }
        }
    }


    open fun removeAllDataFromPreference() {
        MyPreference.removeAllPreference(this@BaseActivity)
    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter("pushNotification"))
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    abstract fun getLayoutId(): Int

    private fun showProgressDialog(title: String?, message: String?, cancelable: Boolean = false) {
        UiUtils.showProgressDialog(this, title, message, cancelable)
    }

    private fun hideProgressDialog() {
        UiUtils.dismissProgressDialog()
    }


    public fun setUpHomeInToolbar(llHomeToolbar: LinearLayout) {
        if (MyConfig.APPSETTING.IS_HOME_ICON_IN_TOOLBAR == "0"){
            llHomeToolbar.visibility = View.GONE
        }else{
            llHomeToolbar.visibility = View.VISIBLE
        }
        llHomeToolbar.setOnClickListener {
            val intent = Intent(this@BaseActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }
    }

    fun showSnackBar(string: String?, positive: Boolean) {
        // UiUtils.showSnackBar(this, string, positive)
    }

    override fun <T> responseStatusSuccess(it: RestResponse<T>) {
        hideProgressDialog()
    }

    override fun <T> responseStatusError(it: RestResponse<T>) {
        hideProgressDialog()
        //showSnackBar(it.getErrorMessage(), false)

    }

    override fun responseStatusLoading() {
        showProgressDialog(null, AndroidUtils.getString(R.string.loading))
    }

    fun <T> observationOfAPI(it: RestResponse<T>?, callBack: IResponseParser<T>?) {
        Log.i("TAGTAGTAG", "Status " + it?.status)
        when (it?.status) {
            RestResponse.Status.LOADING -> {

                callBack?.onLoading()
            }

            RestResponse.Status.ERROR -> {

                callBack?.onError(it)
            }

            RestResponse.Status.SUCCESS -> {

                callBack?.onSuccess(it)
            }
            else -> {}
        }
    }

    open fun startMainActivity() {
        val intent = Intent(this@BaseActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    open fun startSignInActivity() {
        val intent = Intent(this@BaseActivity, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }



}