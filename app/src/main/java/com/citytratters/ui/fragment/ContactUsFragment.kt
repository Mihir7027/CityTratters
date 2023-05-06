package com.citytratters.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.citytratters.R
import com.citytratters.base.BaseFragment
import com.citytratters.base.IResponseParser
import com.citytratters.model.response.GetMoreDetailResponseModel
import com.citytratters.model.response.SubMenuListResponseModel
import com.citytratters.network.RestResponse
import com.citytratters.ui.activity.MainActivity
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_contact_us.*


class ContactUsFragment : BaseFragment() {

    private var lat: Double = 0.00
    private var long: Double = 0.00

    override fun getLayoutId(): Int {
        return R.layout.fragment_contact_us
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity?)!!.setToolbarWithWhiteLogo()
        (activity as MainActivity?)!!.setUpToolbarForHomeIcon((activity as MainActivity?)!!.getToolbarHomeLayout())
        (activity as MainActivity?)!!.getToolbarHomeLayout().setOnClickListener {
            (activity as MainActivity?)!!.navigateToHome()
        }
        subscribe()
        apiCallForMoreDetail()


        tvContactNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:"+tvContactNumber.text.toString())
            startActivity(intent)
        }
        ivCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:"+tvContactNumber.text.toString())
            startActivity(intent)
        }
        tvMail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+tvMail.text.toString()))
            startActivity(Intent.createChooser(emailIntent, ""))

        }

        ivMail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+tvMail.text.toString()))
            startActivity(Intent.createChooser(emailIntent, ""))

        }


    }


    private fun apiCallForMoreDetail() {
        vmCommon.apiMoreDetail("contactus")
    }

    private fun subscribe() {
        vmCommon.eventGetMoreDetail.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<GetMoreDetailResponseModel>(this) {
                override fun onSuccess(it: RestResponse<GetMoreDetailResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            if (it.data != null && it.data!!.data.size > 0) {
                                tvPageTitle.text = it.data!!.data[0].cms_title
                                vmCommon.apiSubMenuList(
                                    it.data!!.data[0].cms_tag,
                                    it.data!!.data[0].cms_perent
                                )
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

        vmCommon.eventGetSubMenuList.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<SubMenuListResponseModel>(this) {
                override fun onSuccess(it: RestResponse<SubMenuListResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            if (it.data!!.data != null && it.data!!.data.size > 0) {

                                tvContactNumber.text = it.data!!.data[0].phone
                                tvMail.text = it.data!!.data[0].email
                                tvAddress.text = it.data!!.data[0].address
                                tvTitleTest.setText(it.data!!.data[0].title.toString())
                                lat = it.data!!.data[0].latitude
                                long = it.data!!.data[0].longitude

                                var mapString =
                                    "<div style=\"width: 100%\"><iframe width=\"100%\" height=\"100%\" frameborder=\"0\" scrolling=\"yes\" marginheight=\"0\" marginwidth=\"0\" src=\"https://maps.google.com/maps?width=100%25&amp;height=400&amp;hl=en&amp;q=${lat},${long}+(${it.data!!.data[0].title})&amp;t=&amp;z=17&amp;ie=UTF8&amp;iwloc=B&amp;output=embed\"></iframe></div>"

                                webviewForMap.getSettings().setJavaScriptEnabled(true)
                                webviewForMap.setVerticalScrollBarEnabled(true)
                                webviewForMap.setHorizontalScrollBarEnabled(true)
                                webviewForMap.loadData(
                                    mapString,
                                    "text/html; charset=utf-8",
                                    "UTF-8"
                                )


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

    }


}