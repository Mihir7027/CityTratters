package com.citytratters.ui.activity


import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.base.BaseActivity
import com.citytratters.base.IResponseParser
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.ResponseSuccess
import com.citytratters.myPreferance.MyPreference
import com.citytratters.network.RestResponse
import com.citytratters.utils.AndroidUtils
import kotlinx.android.synthetic.main.activity_offer_detail.*
import kotlinx.android.synthetic.main.activity_offer_detail.ivBannerImage
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.activity_webview.tvPageTitle
import kotlinx.android.synthetic.main.activity_webview_for_html_content.*
import kotlinx.android.synthetic.main.toolbar_center.*


class OfferDetailActivity : BaseActivity() {
    var id: String = "0"

    override fun getLayoutId(): Int {
        return R.layout.activity_offer_detail
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        llSideMenu.visibility = View.GONE
        llBack.visibility = View.VISIBLE
        setUpHomeInToolbar(llHomeToolbar)
        subscribe()
        id = intent.extras!!.get("id").toString()
        tvPageTitle.text = intent.extras!!.get("pagetitle").toString()

        if(intent.extras!!.get("end_day").toString() !=  null && intent.extras!!.get("end_day").toString() != "null" && intent.extras!!.get("end_day").toString() != "" ){
            tvExpireDate.text = "Expires " + AndroidUtils.changeDateFormat( intent.extras!!.get("end_day").toString() ,
                MyConfig.DateFormat.yyyy_mm_dd,MyConfig.DateFormat.dd_MM_yyyy)

        }
        if (intent.extras!!.get("is_redeemed") == 1){
         tvRedeem.visibility = View.GONE
        }


        tvOfferName.text = intent.extras!!.get("offerName").toString()
        tvDescription.text = intent.extras!!.get("description").toString()

        tvRedeem.setOnClickListener {
            if (intent.extras!!.get("is_redeemed").toString() == "1"){
                redeemDialog()
            }else{
                if (AndroidUtils.isNetworkAvailable(this@OfferDetailActivity)){
                    redeemVoucher(id.toInt())
                }else{
                    Toast.makeText(
                        this@OfferDetailActivity,
                        getString(R.string.no_network_message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        Glide
            .with(this@OfferDetailActivity)
            .load(intent.extras!!.get("image").toString())
            .centerCrop()
            .placeholder(R.drawable.default_img)
            .into(ivBannerImage);

        llBack.setOnClickListener {
            onBackPressed()
        }

    }
    var qrImage:String = ""
    fun redeemDialog() {

        val dialog = Dialog(this@OfferDetailActivity, R.style.Dialog)
        val layout: View =
            LayoutInflater.from(this@OfferDetailActivity).inflate(R.layout.dialog_reedem, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog.window != null) {
            dialog.window!!
                .setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.color_transparent)))
        }
        dialog.setContentView(layout)
        dialog.setCancelable(false)
        val ivWinner =
            dialog.findViewById<View>(R.id.ivWinner) as ImageView
        val tvCancel = dialog.findViewById<View>(R.id.tvCancel) as TextView
        val tvRedeem = dialog.findViewById<View>(R.id.tvRedeem) as TextView
        tvCancel.paintFlags = tvCancel.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        tvRedeem.paintFlags = tvRedeem.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        Glide.with(this).load(qrImage).placeholder(R.drawable.default_img)
            .error(R.drawable.default_img).into(ivWinner)

        tvCancel.visibility = View.GONE
        tvRedeem.setOnClickListener {
            redeemVoucher(id.toInt())
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun redeemVoucher(id: Int) {
        vmCommon.apiRedeemVoucher(AndroidUtils.getDeviceId(this@OfferDetailActivity), id.toString(),MyPreference.getPreference(this@OfferDetailActivity,MyConfig.SharedPreferences.PREF_KEY_BADGE_NO),AndroidUtils.getCurrentDate(MyConfig.DateFormat.yyyy_MM_dd_HH_mm_ss))
    }

    private fun subscribe() {
        vmCommon.eventRedeemVoucher.observe(this@OfferDetailActivity, {
            observationOfAPI(it, object : IResponseParser<ResponseSuccess>(this) {
                override fun onSuccess(it: RestResponse<ResponseSuccess>) {
                    super.onSuccess(it)

                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            Toast.makeText(
                                this@OfferDetailActivity,
                                it.data!!.message,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@OfferDetailActivity,
                                it.data!!.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@OfferDetailActivity,
                            it.data!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        })
    }
}