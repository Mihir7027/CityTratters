package com.citytratters.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.base.BaseFragment
import com.citytratters.base.IResponseParser
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.GetMoreDetailResponseModel
import com.citytratters.model.response.SubMenuDetailResponseModel
import com.citytratters.model.response.SubMenuListForTypeOneResponseModel
import com.citytratters.model.response.SubMenuListResponseModel
import com.citytratters.network.RestResponse
import com.citytratters.ui.activity.MainActivity
import com.citytratters.ui.activity.WebviewActivity
import com.citytratters.ui.activity.WebviewForHTMLContentActivity
import com.citytratters.ui.activity.WhatonDetailActivity
import com.citytratters.ui.adapter.SubMenuDetailAdapter
import com.citytratters.ui.adapter.SubMenuListAdapter
import com.citytratters.ui.adapter.SubMenuListAdapterForTypeOne
import com.citytratters.utils.AndroidUtils
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_whatson.*
import kotlinx.android.synthetic.main.fragment_whatson.ivBG
import kotlinx.android.synthetic.main.fragment_whatson.recyclerView
import kotlinx.android.synthetic.main.fragment_whatson.tvNoData
import kotlinx.android.synthetic.main.fragment_whatson.tvPageTitle
import java.util.*
import kotlin.collections.ArrayList


class WhatsOnFragment : BaseFragment() {

    private var mSubMenuListForEntertainment: ArrayList<SubMenuDetailResponseModel.SubMenuDetailData>? =
        ArrayList()

    private var mSubMenuList: ArrayList<SubMenuListResponseModel.SubMenuData>? = ArrayList()
    private var mSubMenuListForTypeOne: ArrayList<SubMenuListForTypeOneResponseModel.SubMenuData>? =
        ArrayList()


    private var bgImageUrl: String = ""
    private var pageType: String = ""
    private var pageTitle: String = ""
    private var pageTag: String = ""


    override fun getLayoutId(): Int {
        return R.layout.fragment_whatson
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribe()
        pageTitle = getScreenTitle(MyConfig.SCREEN.SELECTEDSCREEN)
        pageTag = getScreenTag(MyConfig.SCREEN.SELECTEDSCREEN)
        (activity as MainActivity?)!!.setToolbarWithWhiteLogo()
        (activity as MainActivity?)!!.setUpToolbarForHomeIcon((activity as MainActivity?)!!.getToolbarHomeLayout())
        (activity as MainActivity?)!!.getToolbarHomeLayout().setOnClickListener {
            (activity as MainActivity?)!!.navigateToHome()
        }

        tvPageTitle.text = pageTitle
        (activity as MainActivity?)!!.setToolbarWithWhiteLogo()

        if (pageTitle != getString(R.string.entertainment)) {
            if (AndroidUtils.isNetworkAvailable(requireActivity())) {
                apiCallForMoreDetail()
            }
        } else {
            if (AndroidUtils.isNetworkAvailable(requireActivity())) {
                apiCallForSubMenuDetail()
            }
        }


    }

    private fun apiCallForMoreDetail() {
        vmCommon.apiMoreDetail(pageTag)
    }

    private fun apiCallForSubMenuList() {
        vmCommon.apiSubMenuList(pageTag, pageType)
    }

    private fun apiCallForSubMenuListForType1() {
        vmCommon.eventGetSubMenuListForTypeOne(pageTag, pageType)
    }

    private fun apiCallForSubMenuDetail() {
        vmCommon.apiSubMenuDetail(pageTag)
    }


    private fun subscribe() {
        vmCommon.eventGetMoreDetail.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<GetMoreDetailResponseModel>(this) {
                override fun onSuccess(it: RestResponse<GetMoreDetailResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            if (it.data != null && it.data!!.data.size > 0) {
                                pageType = it.data!!.data[0].cms_perent
                                tvPageTitle.text = it.data!!.data[0].cms_title
                                if (it.data!!.data[0].cms_perent == "1") {
                                    apiCallForSubMenuListForType1()
                                } else {
                                    bgImageUrl = it.data!!.data[0].cms_photo
                                    apiCallForSubMenuList()
                                }
                            } else {
                                tvNoData.visibility = View.VISIBLE
                                recyclerView.visibility = View.GONE
                                recyclerViewForTypeOne.visibility = View.GONE
                            }

                        } else {
                            UiUtils.showAlertDialog(
                                requireActivity(),
                                it.data!!.message
                            )
                        }
                    } else {
                        UiUtils.showAlertDialog(
                            requireActivity(),
                            it.data!!.message
                        )
                    }
                }

                override fun onError(it: RestResponse<GetMoreDetailResponseModel>) {
                    super.onError(it)
                    UiUtils.showAlertDialog(
                        requireActivity(),
                        it.data!!.message
                    )
                }
            })
        })

        vmCommon.eventGetSubMenuDetail.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<SubMenuDetailResponseModel>(this) {
                override fun onSuccess(it: RestResponse<SubMenuDetailResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {

                        } else {
                            UiUtils.showAlertDialog(
                                requireActivity(),
                                it.data!!.message
                            )
                        }
                    } else {
                        UiUtils.showAlertDialog(
                            requireActivity(),
                            it.data!!.message
                        )
                    }
                }

                override fun onError(it: RestResponse<SubMenuDetailResponseModel>) {
                    super.onError(it)
                    UiUtils.showAlertDialog(
                        requireActivity(),
                        it.data!!.message
                    )
                }
            })
        })

        vmCommon.eventGetSubMenuList.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<SubMenuListResponseModel>(this) {
                override fun onSuccess(it: RestResponse<SubMenuListResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            if (pageType == "0") {
                                Glide
                                    .with(activity!!)
                                    .load(R.drawable.splash_bg)
                                    .centerCrop()
                                    .placeholder(R.drawable.default_img)
                                    .into(ivBG)
                                tvTransparent.visibility = View.VISIBLE
                            } else {
                                Glide
                                    .with(activity!!)
                                    .load(bgImageUrl)
                                    .centerCrop()
                                    .placeholder(R.drawable.default_img)
                                    .into(ivBG)
                                tvTransparent.visibility = View.GONE
                            }


                            if (it.data!!.data != null && it.data!!.data.size > 0) {
                                mSubMenuList = it.data!!.data
                                setAdapter(mSubMenuList)

                            }

                        } else {
                            UiUtils.showAlertDialog(
                                requireActivity(),
                                it.data!!.message
                            )
                        }
                    } else {
                        UiUtils.showAlertDialog(
                            requireActivity(),
                            it.data!!.message
                        )
                    }
                }

                override fun onError(it: RestResponse<SubMenuListResponseModel>) {
                    super.onError(it)
                    UiUtils.showAlertDialog(
                        requireActivity(),
                        it.data!!.message
                    )
                }
            })
        })

        vmCommon.eventGetSubMenuListForTypeOne.observe(requireActivity(), {
            observationOfAPI(
                it,
                object : IResponseParser<SubMenuListForTypeOneResponseModel>(this) {
                    override fun onSuccess(it: RestResponse<SubMenuListForTypeOneResponseModel>) {
                        super.onSuccess(it)
                        if (it.status != RestResponse.Status.ERROR) {
                            if (it.data!!.status == 1) {
                                if (pageType == "0") {
                                    Glide
                                        .with(activity!!)
                                        .load(R.drawable.opening_screen_bg)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_img)
                                        .into(ivBG)
                                    tvTransparent.visibility = View.VISIBLE
                                } else {
                                    Glide
                                        .with(activity!!)
                                        .load(bgImageUrl)
                                        .centerCrop()
                                        .placeholder(R.drawable.default_img)
                                        .into(ivBG)
                                    tvTransparent.visibility = View.GONE

                                }


                                if (it.data!!.data != null && it.data!!.data.size > 0) {
                                    mSubMenuListForTypeOne = it.data!!.data
                                    ivBG.visibility = View.GONE
                                    setAdapterForTypeOne(mSubMenuListForTypeOne)
                                }

                            } else {
                                UiUtils.showAlertDialog(
                                    requireActivity(),
                                    it.data!!.message
                                )
                            }
                        } else {
                            UiUtils.showAlertDialog(
                                requireActivity(),
                                it.data!!.message
                            )
                        }
                    }

                    override fun onError(it: RestResponse<SubMenuListForTypeOneResponseModel>) {
                        super.onError(it)
                        UiUtils.showAlertDialog(
                            requireActivity(),
                            it.data!!.message
                        )
                    }
                })
        })


        vmCommon.eventGetSubMenuDetail.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                observationOfAPI(it, object : IResponseParser<SubMenuDetailResponseModel>(this) {
                    override fun onSuccess(it: RestResponse<SubMenuDetailResponseModel>) {
                        super.onSuccess(it)
                        if (it.status != RestResponse.Status.ERROR) {
                            if (it.data!!.status == 1) {
                                mSubMenuListForEntertainment = it.data!!.data
                                if (mSubMenuListForEntertainment!!.size > 0) {
                                    setAdapterForEntertainment(mSubMenuListForEntertainment)
                                } else {
                                    tvNoData.visibility = View.VISIBLE
                                }
                            } else {
                                UiUtils.showAlertDialog(
                                    requireActivity(),
                                    it.data!!.message
                                )
                            }
                        } else {
                            UiUtils.showAlertDialog(
                                requireActivity(),
                                it.data!!.message
                            )
                        }
                    }

                    override fun onError(it: RestResponse<SubMenuDetailResponseModel>) {
                        super.onError(it)
                        UiUtils.showAlertDialog(
                            requireActivity(),
                            it.data!!.message
                        )
                    }
                })
            })

    }

    private fun setAdapter(list: List<SubMenuListResponseModel.SubMenuData>?) {

        recyclerView.visibility = View.VISIBLE

        recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        val subMenuListAdapter =
            SubMenuListAdapter(object :
                AdapterViewClickListener<SubMenuListResponseModel.SubMenuData> {

                override fun onClickAdapterView(
                    objectAtPosition: SubMenuListResponseModel.SubMenuData?,
                    viewType: Int,
                    position: Int
                ) {
                    when (viewType) {
                        0 -> {
                            val intent = Intent(requireActivity(), WhatonDetailActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            intent.putExtra("title", objectAtPosition!!.title)
                            intent.putExtra("template", objectAtPosition!!.template)
                            intent.putExtra("subTemplate", objectAtPosition!!.sub_template)
                            intent.putExtra("pageTag", objectAtPosition!!.tag)
                            startActivity(intent)
                        }
                    }
                }
            })

        recyclerView.adapter = subMenuListAdapter
        subMenuListAdapter.submitList(list)
    }

    private fun setAdapterForTypeOne(list: List<SubMenuListForTypeOneResponseModel.SubMenuData>?) {

        recyclerViewForTypeOne.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        recyclerViewForTypeOne.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        val subMenuListAdapterForTypeOne =
            SubMenuListAdapterForTypeOne(object :
                AdapterViewClickListener<SubMenuListForTypeOneResponseModel.SubMenuData> {

                override fun onClickAdapterView(
                    objectAtPosition: SubMenuListForTypeOneResponseModel.SubMenuData?,
                    viewType: Int,
                    position: Int
                ) {
                    when (viewType) {
                        0 -> {
                            if (objectAtPosition!!.template.toLowerCase(Locale.ROOT) == getString(R.string.entertainment_condition)) {
                                val intent = Intent(
                                    requireActivity(),
                                    WebviewForHTMLContentActivity::class.java
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                MyConfig.SCREEN.subMenuListForTypeOne = objectAtPosition
                                startActivity(intent)
                            }else{
                                if (objectAtPosition!!.url == "yes") {
                                    val intent = Intent(requireActivity(), WebviewActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                                    intent.putExtra("pagetitle", objectAtPosition!!.title)
                                    intent.putExtra("isUrl", objectAtPosition!!.url)
                                    intent.putExtra("webUrl", objectAtPosition!!.is_url)
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(requireActivity(), WebviewActivity::class.java)
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
                                    Intent(requireActivity(), WebviewActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                                intent.putExtra("pagetitle", objectAtPosition!!.title)
                                intent.putExtra("isUrl", "yes")
                                intent.putExtra("webUrl", objectAtPosition!!.link)
                                startActivity(intent)
                            }
                        }
                    }
                }
            })

        recyclerViewForTypeOne.adapter = subMenuListAdapterForTypeOne
        subMenuListAdapterForTypeOne.submitList(list)
    }


    private fun setAdapterForEntertainment(list: List<SubMenuDetailResponseModel.SubMenuDetailData>?) {

        recyclerView.visibility = View.GONE
        recyclerViewForTypeOne.visibility = View.GONE
        recyclerViewForEntertainment.visibility = View.VISIBLE

        recyclerViewForEntertainment.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

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
                            val intent = Intent(
                                requireActivity(),
                                WebviewForHTMLContentActivity::class.java
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            MyConfig.SCREEN.subMenuDetailData = objectAtPosition
                            startActivity(intent)
                        }

                        1 -> {
                            if (objectAtPosition!!.template.toLowerCase(Locale.ROOT) == getString(R.string.entertainment_condition)) {
                                val intent =
                                    Intent(requireActivity(), WebviewActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                                intent.putExtra("pagetitle", objectAtPosition!!.title)
                                intent.putExtra("isUrl", "yes")
                                intent.putExtra("webUrl", objectAtPosition!!.link)
                                startActivity(intent)
                            }
                        }


                    }
                }
            }, getString(R.string.entertainment_condition))

        recyclerViewForEntertainment.adapter = subMenuDetailAdapter
        subMenuDetailAdapter.submitList(list)
    }


}