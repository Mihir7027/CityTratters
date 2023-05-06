package com.citytratters.ui.activity


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.base.BaseActivity
import com.citytratters.base.IResponseParser
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.BarsDetailResponseModel
import com.citytratters.network.RestResponse
import com.citytratters.ui.adapter.BarsGalleryAdapter
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.activity_dining_detail.*
import kotlinx.android.synthetic.main.activity_dining_detail.tvPageTitle
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.activity_whatson_detail.*
import kotlinx.android.synthetic.main.fragment_dining.*
import kotlinx.android.synthetic.main.row_bar_list.view.*
import kotlinx.android.synthetic.main.toolbar_center.*
import java.util.*


class DiningDetailActivity : BaseActivity() {

    private var barID: String = ""
    private var pagetitle: String = ""
    private var description: String = ""
    private var icon: String = ""
    private var banner: String = ""
    private var dinner: String = ""
    private var lunch: String = ""
    private var bookurl: String = ""
    private var facebook: String = ""
    private var insta: String = ""
    private var yumcha: String = ""
    private var number: String = ""
    private var onlineMenuURl: String = ""
    private var mBarsGallery: ArrayList<BarsDetailResponseModel.BarsDetailData.Gallery>? =
        ArrayList()
    private var mBarsMenu: ArrayList<BarsDetailResponseModel.BarsDetailData.Menu>? = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.activity_dining_detail
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        llSideMenu.visibility = View.GONE
        llBack.visibility = View.VISIBLE

        setUpHomeInToolbar(llHomeToolbar)
        getDataFromIntent()
        setData()
        subscribe()

        apiCallForGetDiningDetail()
        tvOverView.setTextColor(
            ContextCompat.getColor(
                this@DiningDetailActivity,
                R.color.colorPrimary
            )
        );

        tvOverView.setOnClickListener {
            llOnlineMenu.visibility = View.GONE
            llGallery.visibility = View.GONE
            llOverview.visibility = View.VISIBLE
            tvOverView.setTextColor(
                ContextCompat.getColor(
                    this@DiningDetailActivity,
                    R.color.colorPrimary
                )
            );
            tvOnlineMenu.setTextColor(
                ContextCompat.getColor(
                    this@DiningDetailActivity,
                    R.color.black
                )
            );
            tvGallery.setTextColor(
                ContextCompat.getColor(
                    this@DiningDetailActivity,
                    R.color.black
                )
            );
        }

        tvGallery.setOnClickListener {
            llOnlineMenu.visibility = View.GONE
            llGallery.visibility = View.VISIBLE
            llOverview.visibility = View.GONE
            tvGallery.setTextColor(
                ContextCompat.getColor(
                    this@DiningDetailActivity,
                    R.color.colorPrimary
                )
            );
            tvOnlineMenu.setTextColor(
                ContextCompat.getColor(
                    this@DiningDetailActivity,
                    R.color.black
                )
            );
            tvOverView.setTextColor(
                ContextCompat.getColor(
                    this@DiningDetailActivity,
                    R.color.black
                )
            );

        }

        tvOnlineMenu.setOnClickListener {

            val intent = Intent(this@DiningDetailActivity, DiningMenuListingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            intent.putExtra("pagetitle", pagetitle)
            MyConfig.Global.mBarsMenuGlobal = mBarsMenu
            startActivity(intent)

            llOnlineMenu.visibility = View.VISIBLE
            llGallery.visibility = View.GONE
            llOverview.visibility = View.GONE
            tvOnlineMenu.setTextColor(
                ContextCompat.getColor(
                    this@DiningDetailActivity,
                    R.color.colorPrimary
                )
            );
            tvGallery.setTextColor(
                ContextCompat.getColor(
                    this@DiningDetailActivity,
                    R.color.black
                )
            );
            tvOverView.setTextColor(
                ContextCompat.getColor(
                    this@DiningDetailActivity,
                    R.color.black
                )
            );
        }

        llBack.setOnClickListener {
            onBackPressed()
        }

        ivFaceBook.setOnClickListener {
            val intent = Intent(this@DiningDetailActivity, WebviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            intent.putExtra("pagetitle", pagetitle)
            intent.putExtra("isUrl", "yes")
            intent.putExtra("webUrl", facebook)
            startActivity(intent)
        }

        ivInstagram.setOnClickListener {
            val intent = Intent(this@DiningDetailActivity, WebviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            intent.putExtra("pagetitle", pagetitle)
            intent.putExtra("isUrl", "yes")
            intent.putExtra("webUrl", insta)
            startActivity(intent)
        }

        tvBookNow.setOnClickListener {
            val intent = Intent(this@DiningDetailActivity, WebviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            intent.putExtra("pagetitle", pagetitle)
            intent.putExtra("isUrl", "yes")
            intent.putExtra("webUrl", bookurl)
            startActivity(intent)
        }
    }


    private fun apiCallForGetDiningDetail() {
        vmCommon.apiGetBarsDetail(barID)
    }


    private fun subscribe() {
        vmCommon.eventGetBarsDetail.observe(this, androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<BarsDetailResponseModel>(this) {
                override fun onSuccess(it: RestResponse<BarsDetailResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            val barsDetailResponseModel: BarsDetailResponseModel? = it.data!!
                            mBarsGallery = barsDetailResponseModel!!.data.gallery
                            mBarsMenu = barsDetailResponseModel!!.data.menu

                            if (mBarsGallery != null && mBarsGallery!!.size >0){
                                rvGallery.visibility= View.VISIBLE
                                tvNoDataFound.visibility = View.GONE
                                setAdapterForGallery(mBarsGallery)
                            }else{
                                tvNoDataFound.visibility = View.VISIBLE
                            }
                            
                            if (mBarsMenu != null && mBarsMenu!!.size >0){
                                onlineMenuURl  = mBarsMenu!![0].bars_url
                            }


                        } else {
                            UiUtils.showAlertDialog(
                                this@DiningDetailActivity,
                                it.data!!.message
                            )
                        }
                    } else {
                        UiUtils.showAlertDialog(
                            this@DiningDetailActivity,
                            it.data!!.message
                        )
                    }
                }
            })
        })

    }

    override fun onResume() {
        super.onResume()
        llOnlineMenu.visibility = View.GONE
        llGallery.visibility = View.GONE
        llOverview.visibility = View.VISIBLE
        tvOverView.setTextColor(
            ContextCompat.getColor(
                this@DiningDetailActivity,
                R.color.colorPrimary
            )
        );
        tvOnlineMenu.setTextColor(
            ContextCompat.getColor(
                this@DiningDetailActivity,
                R.color.black
            )
        );
        tvGallery.setTextColor(
            ContextCompat.getColor(
                this@DiningDetailActivity,
                R.color.black
            )
        );
    }
    private fun getDataFromIntent() {
        if (intent.extras!!.get("pagetitle").toString() != null && intent.extras!!.get("pagetitle")
                .toString() != ""
        ) {
            pagetitle = intent.extras!!.get("pagetitle").toString()
        }
        if (intent.extras!!.get("id").toString() != null && intent.extras!!.get("id")
                .toString() != ""
        ) {
            barID = intent.extras!!.get("id").toString()
        }

        if (intent.extras!!.get("description")
                .toString() != null && intent.extras!!.get("description").toString() != ""
        ) {
            description = intent.extras!!.get("description").toString()
        }

        if (intent.extras!!.get("icon").toString() != null && intent.extras!!.get("icon")
                .toString() != ""
        ) {
            icon = intent.extras!!.get("icon").toString()
        }
        if (intent.extras!!.get("banner").toString() != null && intent.extras!!.get("banner")
                .toString() != ""
        ) {
            banner = intent.extras!!.get("banner").toString()
        }
        if (intent.extras!!.get("dinner").toString() != null && intent.extras!!.get("dinner")
                .toString() != ""
        ) {
            dinner = intent.extras!!.get("dinner").toString()
        }
        if (intent.extras!!.get("lunch").toString() != null && intent.extras!!.get("lunch")
                .toString() != ""
        ) {
            lunch = intent.extras!!.get("lunch").toString()
        }
        if (intent.extras!!.get("bookurl").toString() != null && intent.extras!!.get("bookurl")
                .toString() != ""
        ) {
            bookurl = intent.extras!!.get("bookurl").toString()
        }
        if (intent.extras!!.get("facebook").toString() != null && intent.extras!!.get("facebook")
                .toString() != ""
        ) {
            facebook = intent.extras!!.get("facebook").toString()
        }
        if (intent.extras!!.get("insta").toString() != null && intent.extras!!.get("insta")
                .toString() != ""
        ) {
            insta = intent.extras!!.get("insta").toString()
        }
        if (intent.extras!!.get("yumcha").toString() != null && intent.extras!!.get("yumcha")
                .toString() != ""
        ) {
            yumcha = intent.extras!!.get("yumcha").toString()
        }
        if (intent.extras!!.get("number").toString() != null && intent.extras!!.get("number")
                .toString() != ""
        ) {
            number = intent.extras!!.get("number").toString()
        }


    }

    private fun setData() {
        tvPageTitle.text = pagetitle
        tvContactNumber.text = number


        tvLunchTime.text = lunch
        tvDinnerTime.text = dinner
        tvYumCha.text = yumcha

        if (lunch != ""){
            tvLunchTime.visibility = View.VISIBLE
            tvLunchTitle.visibility = View.VISIBLE
        }
        if (dinner != ""){
            tvDinnerTime.visibility = View.VISIBLE
            tvDinnerTitle.visibility = View.VISIBLE
        }
        if (yumcha != ""){
            tvYumCha.visibility = View.VISIBLE
            tvYumChaTitle.visibility = View.GONE
        }

        if(lunch == "" && dinner == "" && yumcha == ""){
            ivTime.visibility = View.INVISIBLE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvContent.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT);
        } else {
            tvContent.text = Html.fromHtml(description)
        }


        if (bookurl == "") {
            tvBookNow.visibility = View.GONE
        }else{
            tvBookNow.visibility = View.GONE
        }
        if (insta == "") {
            ivInstagram.visibility = View.GONE
        }
        if (facebook == "") {
            ivFaceBook.visibility = View.GONE
        }
        if (icon != "") {
            Glide
                .with(this@DiningDetailActivity)
                .load(icon)
                .centerCrop()
                .placeholder(R.drawable.default_img)
                .into(ivProfileImage)
        }
        if (banner != "") {
            Glide
                .with(this@DiningDetailActivity)
                .load(banner)
                .centerCrop()
                .placeholder(R.drawable.default_img)
                .into(ivBannerImage)
        }


    }

    private fun setAdapterForGallery(list: List<BarsDetailResponseModel.BarsDetailData.Gallery>?) {

        rvGallery.visibility = View.VISIBLE

        rvGallery.layoutManager =GridLayoutManager(this@DiningDetailActivity,2)

        val subMenuDetailAdapter =
            BarsGalleryAdapter(object :
                AdapterViewClickListener<BarsDetailResponseModel.BarsDetailData.Gallery> {

                override fun onClickAdapterView(
                    objectAtPosition: BarsDetailResponseModel.BarsDetailData.Gallery?,
                    viewType: Int,
                    position: Int
                ) {
                    when (viewType) {

                    }
                }
            })

        rvGallery.adapter = subMenuDetailAdapter
        subMenuDetailAdapter.submitList(list)
    }



}