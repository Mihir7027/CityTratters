package com.citytratters.ui.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.*
import android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.citytratters.R
import com.citytratters.base.BaseActivity
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.toolbar_center.*


class WebviewActivity : BaseActivity() {

    private var pagetitle: String = ""
    private var webUrl: String = ""
    private var isUrl: String = ""

    private var isPayment:Boolean = false
    override fun getLayoutId(): Int {
        return R.layout.activity_webview
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        llSideMenu.visibility = View.GONE
        llBack.visibility = View.VISIBLE
        setUpHomeInToolbar(llHomeToolbar)
        tvPageTitle.text = intent.extras!!.get("pagetitle").toString()
        webUrl = intent.extras!!.get("webUrl").toString()

        if (intent.extras!!.get("isUrl") != null) {
            isUrl = intent.extras!!.get("isUrl").toString()
        }

        llBack.setOnClickListener {
            if (isPayment){
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("IS_FROM", "payment")
                startActivity(intent)
            }else{
                onBackPressed()

            }
        }
        webview.clearCache(true)
        webview.settings.javaScriptEnabled = true
        webview.settings.setPluginState(WebSettings.PluginState.ON);
        webview.settings.allowFileAccessFromFileURLs = true
        webview.settings.allowFileAccess = true
        webview.settings.allowUniversalAccessFromFileURLs = true
        webview.settings.loadsImagesAutomatically = true
        webview.settings.domStorageEnabled = true
        webview.settings.cacheMode = LOAD_CACHE_ELSE_NETWORK
        webview.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)

        webview.isHorizontalScrollBarEnabled = false


        if (isUrl == "yes") {
            if (webUrl.endsWith(".pdf")) {
                //webview.loadUrl("https://docs.google.com/viewer?url="+webUrl);
                // webview.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+webUrl);
                webview.loadUrl("https://docs.google.com/gview?&url=" + webUrl);
            } else {
                webview.loadUrl(webUrl)
            }
            webview.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    Log.e("DSSDSDS",url.toString())

                    if (url.toString().contains("cronullarsl/payment/status?type=1")){
                        isPayment = true
                    }
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    progressbar.visibility = View.GONE
                    webview.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (view!!.contentHeight == 0) {
                            webview.loadUrl(webUrl)
                        }
                    }, 2000)
                }

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val url: String = request?.url.toString()
                    view?.loadUrl(url)
                    if (progressbar != null) {
                        progressbar.visibility = View.VISIBLE
                    }
                    return true
                }

                override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
                    webView.loadUrl(url)
                    Log.e("DSSDSDS",url)
                    if (progressbar != null) {
                        progressbar.visibility = View.VISIBLE
                    }
                    return true
                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    Toast.makeText(this@WebviewActivity, "Got Error! $error", Toast.LENGTH_SHORT).show()

                    if (progressbar != null) {
                        progressbar.visibility = View.GONE
                    }
                }

            }
            progressbar.visibility = View.VISIBLE
            webview.visibility = View.GONE


        } else {
            progressbar.visibility = View.GONE
            webview.loadData(webUrl, "text/html", "UTF-8");
        }

    }


    private class Callback : WebViewClient() {

    }
}