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
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.OfferListResponseModel
import com.citytratters.myPreferance.MyPreference
import com.citytratters.network.RestResponse
import com.citytratters.ui.activity.MainActivity
import com.citytratters.ui.activity.OfferDetailActivity
import com.citytratters.ui.adapter.OfferListAdapter
import com.citytratters.utils.AndroidUtils
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_offer.*
import kotlinx.android.synthetic.main.fragment_offer.swipeContainer


class OfferFragment : BaseFragment() {


    private var mOfferList: ArrayList<OfferListResponseModel.OfferListData>? = ArrayList()
    private var isPullToRefresh = false

    override fun getLayoutId(): Int {
        return R.layout.fragment_offer
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
                apiCallForGetOfferList()
            } else {
                swipeContainer.isRefreshing = false
            }
        }

        if (AndroidUtils.isNetworkAvailable(requireActivity())) {
            apiCallForGetOfferList()
        }

    }


    private fun apiCallForGetOfferList() {
        swipeContainer.isRefreshing = false
        var member_id = ""
        var badge_no = ""
        if (MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_LOGIN_ID
            ) != null && MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_LOGIN_ID
            ) != ""
        ){
            member_id = MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_BADGE_NO
            )
        }
        vmCommon.offerListing(AndroidUtils.getDeviceId(requireActivity()),member_id)
    }


    private fun subscribe() {
        vmCommon.eventOfferListing.observe(requireActivity()) {
            observationOfAPI(it, object : IResponseParser<OfferListResponseModel>(this) {
                override fun onSuccess(it: RestResponse<OfferListResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {

                            if (it.data!!.data != null && it.data!!.data.size > 0) {
                                swipeContainer.visibility = View.VISIBLE
                                llNoData.visibility = View.GONE
                                mOfferList = it.data!!.data
                                setAdapter(mOfferList)

                            } else {
                                swipeContainer.visibility = View.GONE
                                llNoData.visibility = View.VISIBLE
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

                override fun onError(it: RestResponse<OfferListResponseModel>) {
                    UiUtils.showAlertDialog(
                        requireActivity(),
                        it.getErrorMessage()
                    )
                }
            })
        }
    }

    private fun setAdapter(list: List<OfferListResponseModel.OfferListData>?) {

        rvOffer.visibility = View.VISIBLE

        rvOffer.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        val barListAdapter =
            OfferListAdapter(object : AdapterViewClickListener<OfferListResponseModel.OfferListData> {

                override fun onClickAdapterView(
                    objectAtPosition: OfferListResponseModel.OfferListData?,
                    viewType: Int,
                    position: Int
                ) {
                    when (viewType) {
                        0 -> {
                            if (objectAtPosition!!.is_locked != 1){
                                val intent = Intent(requireActivity(), OfferDetailActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                                intent.putExtra("pagetitle", objectAtPosition!!.title)
                                intent.putExtra("description", objectAtPosition!!.description)
                                intent.putExtra("image", objectAtPosition!!.image)
                                intent.putExtra("offerName", objectAtPosition!!.title)
                                intent.putExtra("end_day", objectAtPosition!!.end_day)
                                intent.putExtra("is_locked", objectAtPosition!!.is_locked)
                                intent.putExtra("id", objectAtPosition!!.id)
                                intent.putExtra("qrImage", objectAtPosition!!.qr_code)
                                intent.putExtra("is_redeemed", objectAtPosition!!.is_redeemed)
                                startActivity(intent)
                            }

                        }

                        1 -> {
                          /*  val intent = Intent(requireActivity(), WebviewActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                            intent.putExtra("pagetitle",objectAtPosition!!.bars_title)
                            intent.putExtra("isUrl","yes")
                            intent.putExtra("webUrl",objectAtPosition!!.booking_url)
                            startActivity(intent)*/
                        }
                    }
                }
            })

        rvOffer.adapter = barListAdapter
        barListAdapter.submitList(list)
    }


}