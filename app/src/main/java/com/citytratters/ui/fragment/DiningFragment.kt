package com.citytratters.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citytratters.R
import com.citytratters.base.BaseFragment
import com.citytratters.base.IResponseParser
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.model.response.BarListResponseModel
import com.citytratters.network.RestResponse
import com.citytratters.ui.activity.DiningDetailActivity
import com.citytratters.ui.activity.MainActivity
import com.citytratters.ui.activity.WebviewActivity
import com.citytratters.ui.adapter.BarListAdapter
import com.citytratters.utils.AndroidUtils
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_dining.*


class DiningFragment : BaseFragment() {

    private var mBarsList: ArrayList<BarListResponseModel.BarData>? = ArrayList()
    private var isPullToRefresh = false

    override fun getLayoutId(): Int {
        return R.layout.fragment_dining
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribe()
        (activity as MainActivity?)!!.setToolbarWithWhiteLogo()
        (activity as MainActivity?)!!.setUpToolbarForHomeIcon((activity as MainActivity?)!!.getToolbarHomeLayout())
        (activity as MainActivity?)!!.getToolbarHomeLayout().setOnClickListener {
            (activity as MainActivity?)!!.navigateToHome()
        }

        swipeContainer.setOnRefreshListener {
            isPullToRefresh = true
            if (AndroidUtils.isNetworkAvailable(requireActivity())) {
                apiCallForGetBarList()
            } else {
                swipeContainer.isRefreshing = false
            }
        }

        if (AndroidUtils.isNetworkAvailable(requireActivity())) {
            apiCallForGetBarList()
        }

    }


    private fun apiCallForGetBarList() {
        swipeContainer.isRefreshing = false
        vmCommon.barListing("")
    }


    private fun subscribe() {
        vmCommon.eventBarListing.observe(requireActivity(), {
            observationOfAPI(it, object : IResponseParser<BarListResponseModel>(this) {
                override fun onSuccess(it: RestResponse<BarListResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {

                            if (it.data!!.data != null && it.data!!.data.size > 0) {
                                mBarsList = it.data!!.data
                                setAdapter(mBarsList)

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

                override fun onError(it: RestResponse<BarListResponseModel>) {
                    UiUtils.showAlertDialog(
                        requireActivity(),
                        it.getErrorMessage()
                    )
                }
            })
        })
    }

    private fun setAdapter(list: List<BarListResponseModel.BarData>?) {

        rvBarlist.visibility = View.VISIBLE

        rvBarlist.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        val barListAdapter =
            BarListAdapter(object : AdapterViewClickListener<BarListResponseModel.BarData> {

                override fun onClickAdapterView(
                    objectAtPosition: BarListResponseModel.BarData?,
                    viewType: Int,
                    position: Int
                ) {
                    when (viewType) {
                        0 -> {
                            val intent = Intent(requireActivity(), DiningDetailActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                            intent.putExtra("pagetitle", objectAtPosition!!.bars_title)
                            intent.putExtra("id", objectAtPosition!!.bars_id)
                            intent.putExtra("description", objectAtPosition!!.bars_description)
                            intent.putExtra("icon", objectAtPosition.bars_icon_photo)
                            intent.putExtra("banner", objectAtPosition.bar_bg_image)
                            intent.putExtra("dinner", objectAtPosition.dinner_timing)
                            intent.putExtra("lunch", objectAtPosition.lunch_timing)
                            intent.putExtra("bookurl", objectAtPosition.booking_url)
                            intent.putExtra("facebook", objectAtPosition.face_link)
                            intent.putExtra("insta", objectAtPosition.inst_link)
                            intent.putExtra("yumcha", objectAtPosition.other_timing)
                            intent.putExtra("number", objectAtPosition.contact)
                            startActivity(intent)
                        }

                        1 -> {
                            val intent = Intent(requireActivity(), WebviewActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                            intent.putExtra("pagetitle",objectAtPosition!!.bars_title)
                            intent.putExtra("isUrl","yes")
                            intent.putExtra("webUrl",objectAtPosition!!.booking_url)
                            startActivity(intent)
                        }
                    }
                }
            })

        rvBarlist.adapter = barListAdapter
        barListAdapter.submitList(list)
    }


}