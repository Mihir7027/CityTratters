package com.citytratters.ui.fragment

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.citytratters.R
import com.citytratters.base.BaseFragment
import com.citytratters.ui.activity.MainActivity
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.fragment_whatson.*
import java.util.*


class TableOrderingFragment : BaseFragment() {

    private var webUrl: String = ""
    override fun getLayoutId(): Int {
        return R.layout.fragment_table_ordering
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        webUrl = "http://3.25.233.120/City Tattersalls/comming-soon"
        (activity as MainActivity?)!!.setUpToolbarForHomeIcon((activity as MainActivity?)!!.getToolbarHomeLayout())
        (activity as MainActivity?)!!.getToolbarHomeLayout().setOnClickListener {
            (activity as MainActivity?)!!.navigateToHome()
        }
        (activity as MainActivity?)!!.setToolbarWithWhiteLogo()

        webview.settings.javaScriptEnabled = true
        webview.loadUrl(webUrl)
        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressbar.visibility = View.GONE
                webview.visibility = View.VISIBLE
            }

        }
        progressbar.visibility = View.VISIBLE
        webview.visibility = View.GONE

    }


}