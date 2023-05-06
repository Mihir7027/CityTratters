package com.citytratters.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citytratters.R
import com.citytratters.base.BaseActivity
import com.citytratters.base.IResponseParser
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.constants.MyConfig.SCREEN.subMenuDetailData
import com.citytratters.model.response.SubMenuDetailResponseModel
import com.citytratters.network.RestResponse
import com.citytratters.ui.adapter.SubMenuDetailAdapter
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.activity_whatson_detail.*
import kotlinx.android.synthetic.main.toolbar_center.*
import java.util.*
import kotlin.collections.ArrayList

class WhatonDetailActivity : BaseActivity() {

    private var mSubMenuListForTypeOne: ArrayList<SubMenuDetailResponseModel.SubMenuDetailData>? =
        ArrayList()
    private var title: String = ""
    private var pageTag: String = ""
    private var subTemplate: String = ""
    private var template: String = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_whatson_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        subscribe()
        llSideMenu.visibility = View.GONE
        llBack.visibility = View.VISIBLE

        llBack.setOnClickListener {
            onBackPressed()
        }

        title = intent.extras!!.get("title").toString()
        pageTag = intent.extras!!.get("pageTag").toString()
        template = intent.extras!!.get("template").toString()
        subTemplate = intent.extras!!.get("subTemplate").toString()

        tvPageTitle.text = title
        setUpHomeInToolbar(llHomeToolbar)
        apiCallForSubMenuDetail()

    }

    private fun apiCallForSubMenuDetail() {
        vmCommon.apiSubMenuDetail(pageTag)
    }

    private fun subscribe() {

        vmCommon.eventGetSubMenuDetail.observe(
            this@WhatonDetailActivity,
            androidx.lifecycle.Observer {
                observationOfAPI(it, object : IResponseParser<SubMenuDetailResponseModel>(this) {
                    override fun onSuccess(it: RestResponse<SubMenuDetailResponseModel>) {
                        super.onSuccess(it)
                        if (it.status != RestResponse.Status.ERROR) {
                            if (it.data!!.status == 1) {
                                mSubMenuListForTypeOne = it.data!!.data
                                if (mSubMenuListForTypeOne!!.size > 0) {
                                    setAdapterForTypeOne(mSubMenuListForTypeOne)
                                } else {
                                    tvNoData.visibility = View.VISIBLE
                                }
                            } else {
                                UiUtils.showAlertDialog(
                                    this@WhatonDetailActivity,
                                    it.data!!.message
                                )
                            }
                        } else {
                            UiUtils.showAlertDialog(
                                this@WhatonDetailActivity,
                                it.data!!.message
                            )
                        }
                    }

                    override fun onError(it: RestResponse<SubMenuDetailResponseModel>) {
                        super.onError(it)
                        UiUtils.showAlertDialog(
                            this@WhatonDetailActivity,
                            it.data!!.message
                        )
                    }
                })
            })


    }

    private fun setAdapterForTypeOne(list: List<SubMenuDetailResponseModel.SubMenuDetailData>?) {

        recyclerView.visibility = View.VISIBLE

        recyclerView.layoutManager =
            LinearLayoutManager(this@WhatonDetailActivity, RecyclerView.VERTICAL, false)

        val subMenuDetailAdapter =
            SubMenuDetailAdapter(object :
                AdapterViewClickListener<SubMenuDetailResponseModel.SubMenuDetailData> {

                override fun onClickAdapterView(
                    objectAtPosition: SubMenuDetailResponseModel.SubMenuDetailData?,
                    viewType: Int,
                    position: Int
                ) {
                    when (viewType) {
                        0 -> {
                            if (objectAtPosition!!.template.toLowerCase(Locale.ROOT) == getString(R.string.entertainment_condition)) {
                                val intent = Intent(
                                    this@WhatonDetailActivity,
                                    WebviewForHTMLContentActivity::class.java
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                subMenuDetailData = objectAtPosition
                                startActivity(intent)
                            } else {
                                if (objectAtPosition!!.url == "yes") {
                                    val intent = Intent(
                                        this@WhatonDetailActivity,
                                        WebviewActivity::class.java
                                    )
                                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                                    intent.putExtra("pagetitle", objectAtPosition!!.title)
                                    intent.putExtra("isUrl", objectAtPosition!!.url)
                                    if (objectAtPosition!!.link != null && objectAtPosition!!.link != "" ){
                                        intent.putExtra("webUrl", objectAtPosition!!.link)

                                    }else {
                                        intent.putExtra("webUrl", objectAtPosition!!.is_url)
                                    }
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(
                                        this@WhatonDetailActivity,
                                        WebviewActivity::class.java
                                    )
                                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                                    intent.putExtra("pagetitle", objectAtPosition!!.title)
                                    intent.putExtra("isUrl", objectAtPosition!!.url)
                                    intent.putExtra("webUrl", objectAtPosition!!.description)
                                    startActivity(intent)
                                }

                            }
                        }

                        1 -> {
                            if (objectAtPosition!!.template.toLowerCase(Locale.ROOT) == getString(R.string.entertainment_condition)) {
                                val intent =
                                    Intent(this@WhatonDetailActivity, WebviewActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                                intent.putExtra("pagetitle", objectAtPosition!!.title)
                                intent.putExtra("isUrl", "yes")
                                intent.putExtra("webUrl", objectAtPosition!!.link)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }, template)

        recyclerView.adapter = subMenuDetailAdapter
        subMenuDetailAdapter.submitList(list)
    }


}