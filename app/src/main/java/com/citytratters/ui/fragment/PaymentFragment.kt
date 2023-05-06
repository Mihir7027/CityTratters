package com.citytratters.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citytratters.R
import com.citytratters.base.BaseFragment
import com.citytratters.base.IResponseParser
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.GetCheckOutIdResponseModel
import com.citytratters.model.response.MemberFeesResponseModel
import com.citytratters.myPreferance.MyPreference
import com.citytratters.network.RestResponse
import com.citytratters.ui.activity.MainActivity
import com.citytratters.ui.adapter.FeesListAdapter
import com.citytratters.utils.AndroidUtils
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.checkout.meta.CheckoutStorePaymentDetailsMode
import com.oppwa.mobile.connect.provider.Connect
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_dining.swipeContainer
import kotlinx.android.synthetic.main.fragment_payment.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableSet


open class PaymentFragment : BaseFragment() {

    private var mFeesList: ArrayList<MemberFeesResponseModel.MemberFeesData>? = ArrayList()
    private var isPullToRefresh = false

    //payment
    private var checkOutId: String = ""
    private var checkoutSettings: CheckoutSettings? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_payment
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribe()
        (activity as MainActivity?)!!.setUpToolbarForHomeIcon((activity as MainActivity?)!!.getToolbarHomeLayout())
        (activity as MainActivity?)!!.getToolbarHomeLayout().setOnClickListener {
            (activity as MainActivity?)!!.navigateToHome()
        }

        swipeContainer.setOnRefreshListener {
            isPullToRefresh = true
            if (AndroidUtils.isNetworkAvailable(requireActivity())) {
                apiCallForGetFeesList()
            } else {
                swipeContainer.isRefreshing = false
            }
        }

        if (AndroidUtils.isNetworkAvailable(requireActivity())) {
            apiCallForGetFeesList()
        }

    }


    private fun apiCallForGetFeesList() {
        swipeContainer.isRefreshing = false
        vmCommon.feesListing(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER
            )
        )
    }

    private fun apiCallForGetCheckOutID(totalAmount: String, orderId: String) {
        vmCommon.getCheckOutID(
            "3", orderId
        )
    }


    private fun subscribe() {
        vmCommon.eventFeesListing.observe(requireActivity(), {
            observationOfAPI(it, object : IResponseParser<MemberFeesResponseModel>(this) {
                override fun onSuccess(it: RestResponse<MemberFeesResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {

                            if (it.data!!.data != null && it.data!!.data.size > 0) {
                                mFeesList = it.data!!.data
                                setAdapter(mFeesList)
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

                override fun onError(it: RestResponse<MemberFeesResponseModel>) {
                    UiUtils.showAlertDialog(
                        requireActivity(),
                        it.getErrorMessage()
                    )
                }
            })
        })

        vmCommon.eventGetCheckOutId.observe(requireActivity(), {
            observationOfAPI(it, object : IResponseParser<GetCheckOutIdResponseModel>(this) {
                override fun onSuccess(it: RestResponse<GetCheckOutIdResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 200) {
                            if (it.data!!.data != null) {
                                checkOutId = it.data!!.data.checkout_id
                                startPayment(checkOutId)
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

                override fun onError(it: RestResponse<GetCheckOutIdResponseModel>) {
                    UiUtils.showAlertDialog(
                        requireActivity(),
                        it.getErrorMessage()
                    )
                }
            })
        })
    }

    private fun setAdapter(list: List<MemberFeesResponseModel.MemberFeesData>?) {

        rvPayment.visibility = View.VISIBLE
        rvPayment.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        val feesListAdapter =
            FeesListAdapter(object :
                AdapterViewClickListener<MemberFeesResponseModel.MemberFeesData> {
                override fun onClickAdapterView(
                    objectAtPosition: MemberFeesResponseModel.MemberFeesData?,
                    viewType: Int,
                    position: Int
                ) {
                    when (viewType) {
                        0 -> {
                        }

                        1 -> {
                            var totalAmount =
                                (objectAtPosition!!.amountGST.toDouble() + objectAtPosition!!.amountDue).toString()
                            var orderId = (objectAtPosition!!.feeId).toString()
                            apiCallForGetCheckOutID(totalAmount, orderId)
                        }
                    }
                }
            })

        rvPayment.adapter = feesListAdapter
        feesListAdapter.submitList(list)
    }


    fun startPayment(checkOutId: String) {
        openCheckoutUI(checkOutId)
        Toast.makeText(requireActivity(), checkOutId, Toast.LENGTH_SHORT).show()
    }

    private fun openCheckoutUI(checkOutId: String) {
        val paymentBrands: MutableSet<String> = HashSet()
        paymentBrands.add("VISA")
        paymentBrands.add("MASTER")
        checkoutSettings = CheckoutSettings(checkOutId, paymentBrands, Connect.ProviderMode.TEST)
        checkoutSettings!!.setShowDetectedBrands(true)
        checkoutSettings!!.paymentBrands
        checkoutSettings!!.setStorePaymentDetailsMode(CheckoutStorePaymentDetailsMode.PROMPT)
        checkoutSettings!!.shopperResultUrl =
            getString(R.string.checkout_ui_callback_scheme) + "://callback"
        val intent = checkoutSettings!!.createCheckoutActivityIntent(requireActivity())
        startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT)
    }





}