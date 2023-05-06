package com.citytratters.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.base.BaseActivity
import com.citytratters.constants.MyConfig
import com.citytratters.utils.AndroidUtils
import kotlinx.android.synthetic.main.activity_webview_for_html_content.*
import kotlinx.android.synthetic.main.toolbar_center.*
import java.lang.Exception


class WebviewForHTMLContentActivity : BaseActivity() {
    companion object {
        fun getIntent(ctx: Context): Intent {
            return Intent(ctx, WebviewForHTMLContentActivity::class.java)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_webview_for_html_content
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        llSideMenu.visibility = View.GONE
        llBack.visibility = View.VISIBLE
        llBack.setOnClickListener {
            MyConfig.SCREEN.subMenuDetailData= null
            MyConfig.SCREEN.subMenuListForTypeOne= null
            onBackPressed()
        }
        val title: String? =  intent.extras?.getString("title")
        setUpHomeInToolbar(llHomeToolbar)
        if (MyConfig.SCREEN.subMenuDetailData != null){
            tvPageTitle.text = MyConfig.SCREEN.subMenuDetailData!!.title
            tvDate.text = AndroidUtils.changeDateFormat(MyConfig.SCREEN.subMenuDetailData!!.show_time,MyConfig.DateFormat.yyyy_mm_ddTHHMM,MyConfig.DateFormat.MM_dd_yyyy_hh_mm_aa)
            tvDoorsOpen.text = AndroidUtils.toCamelCase("Doors open @"+ AndroidUtils.changeDateFormat( MyConfig.SCREEN.subMenuDetailData!!.door_open,MyConfig.DateFormat.yyyy_mm_ddTHHMM,MyConfig.DateFormat.dd_MM_yyyy))
            tvSubTitle.text = MyConfig.SCREEN.subMenuDetailData!!.sub_title
            webview.loadData(MyConfig.SCREEN.subMenuDetailData!!.description.replace("&#39;","'"), "text/html", "UTF-8");

            if (MyConfig.SCREEN.subMenuDetailData!!?.link == null || MyConfig.SCREEN.subMenuDetailData!!?.link == ""){
                tvBookNow.visibility = View.GONE
            }

            tvBookNow.setOnClickListener {
                val intent = Intent(this@WebviewForHTMLContentActivity, WebviewActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                intent.putExtra("pagetitle", MyConfig.SCREEN.subMenuDetailData!!.title)
                intent.putExtra("isUrl", "yes")
                intent.putExtra("webUrl", MyConfig.SCREEN.subMenuDetailData!!.link)
                startActivity(intent)
            }
            Glide
                .with(this@WebviewForHTMLContentActivity)
                .load(MyConfig.SCREEN.subMenuDetailData!!.photo)
                .centerCrop()
                .error(R.drawable.default_img)
                .placeholder(R.drawable.default_img)
                .into(ivBannerImage);

        }
        else if(title.equals(getString(R.string.terms_and_conditions))){
            try {
                val bundle: Bundle? = intent.extras
                val webData = "<!DOCTYPE html>The Club’s Annual Report is available to view online.<br/>If you would like to receive a copy of the Annual Report (when available) please email info@cronullarsl.com.au<br/><br/>Gaming and Privacy Statement<br/>Cronulla RSL is registered under the Registered Clubs Act 1976 and we comply with the Privacy Act 1998. We respect your right to privacy and are committed to protecting your personal information. We are bound by the Australian Privacy Principles (‘APPs’) contained in the Australian Privacy Act 1988 (Cth). We will only use your personal information for the purpose in which you have provided it or where we are required or authorised to do so by law.<br/>Primarily, we will use your personal information in order to contact you regarding promotions and offers that we believe will be of particular interest to you. If you do not wish to be contacted, wish to change or cease the type of offers you are receiving, or if you wish to change the method in which we contact you, you can notify us at any time and we will update your preferences.<br/>Your personal information, including information obtained by us as a result of you placing your membership card in a gaming or other club machine (not ATM’s), may be used by the Club for marketing purposes to improve services to you with the latest information about those services, and any new related services and promotions. Your signature denotes acceptance to receive promotional material including gaming information. If you do not wish to receive this information, please notify Club Reception or email info@cronullarsl.com.au<br/><br/>You can view our Privacy Policy on our website, www.cronullarsl.com.au.<br/><br/>Help is close at hand. GambleAware. Visit www.gambleaware.nsw.gov.au or call 1800 585 858<br/><br/> the best interests of its members, guests and the community, Cronulla RSL promotes the Responsible Service of Alcohol<br/><br/>Declaration: I hereby make an application for membership at Cronulla RSL Memorial Club Ltd.</html>"
                tvPageTitle.text = title
                webview.loadDataWithBaseURL(null, webData, "text/html", "UTF-8", null);
                llMain.visibility = View.GONE
            }catch (E:Exception){
                Toast.makeText(this, "Something went wrong \n please try again", Toast.LENGTH_SHORT).show()
                finish()
            }
        }else{
            tvPageTitle.text = MyConfig.SCREEN.subMenuListForTypeOne!!.title
            tvDate.text = AndroidUtils.changeDateFormat(MyConfig.SCREEN.subMenuListForTypeOne!!.show_time,MyConfig.DateFormat.yyyy_mm_ddTHHMM,MyConfig.DateFormat.MM_dd_yyyy_hh_mm_aa)
            tvDoorsOpen.text = AndroidUtils.toCamelCase("Doors Open @"+ AndroidUtils.changeDateFormat( MyConfig.SCREEN.subMenuListForTypeOne!!.door_open,MyConfig.DateFormat.yyyy_mm_ddTHHMM,MyConfig.DateFormat.dd_MM_yyyy))
            tvSubTitle.text = MyConfig.SCREEN.subMenuListForTypeOne!!.sub_title
            webview.loadData(MyConfig.SCREEN.subMenuListForTypeOne!!.description.replace("&#39;","'"), "text/html", "UTF-8");
            tvBookNow.setOnClickListener {
                val intent = Intent(this@WebviewForHTMLContentActivity, WebviewActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                intent.putExtra("pagetitle", MyConfig.SCREEN.subMenuListForTypeOne!!.title)
                intent.putExtra("isUrl", "yes")
                intent.putExtra("webUrl", MyConfig.SCREEN.subMenuListForTypeOne!!.link)
                startActivity(intent)
            }
            Glide
                .with(this@WebviewForHTMLContentActivity)
                .load( MyConfig.SCREEN.subMenuListForTypeOne!!.photo)
                .centerCrop()
                .error(R.drawable.default_img)
                .placeholder(R.drawable.default_img)
                .into(ivBannerImage);
        }



    }


}
