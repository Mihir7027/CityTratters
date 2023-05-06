package com.citytratters.ui.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citytratters.R
import com.citytratters.base.BaseActivity
import com.citytratters.base.IResponseParser
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.GetCheckOutIdResponseModel
import com.citytratters.model.response.MemberFeesResponseModel
import com.citytratters.myPreferance.MyPreference
import com.citytratters.network.RestResponse
import com.citytratters.ui.adapter.FeesListAdapter
import com.citytratters.utils.AndroidUtils
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.checkout.meta.CheckoutStorePaymentDetailsMode
import com.oppwa.mobile.connect.exception.PaymentError
import com.oppwa.mobile.connect.provider.Connect
import com.oppwa.mobile.connect.provider.Transaction
import com.oppwa.mobile.connect.provider.TransactionType
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.activity_fees.*
import kotlinx.android.synthetic.main.activity_fees.termsAndConditionLink
import kotlinx.android.synthetic.main.activity_fees.tvSubmit
import kotlinx.android.synthetic.main.toolbar_center.*
import java.util.*


class FeesActivity : BaseActivity() {


    private var mFeesList: ArrayList<MemberFeesResponseModel.MemberFeesData>? = ArrayList()
    private var isPullToRefresh = false

    //payment
    private var checkOutId: String = ""
    private var checkoutSettings: CheckoutSettings? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_fees
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        llSideMenu.visibility = View.GONE
        llBack.visibility = View.VISIBLE
        setUpHomeInToolbar(llHomeToolbar)
        llBack.setOnClickListener {
            onBackPressed()
        }

        if (intent.data != null) {
            val uri = intent.data
            val abc = uri!!.getQueryParameter("resourcePath")
            Log.e("abc", abc!!)
            Toast.makeText(this@FeesActivity, abc, Toast.LENGTH_SHORT).show()
        }
        termsAndConditionLink.setOnClickListener {
            val intent = Intent(
               this@FeesActivity,
                WebviewForHTMLContentActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra("title",getString(R.string.terms_and_conditions))
            startActivity(intent)
        }

        subscribe()
        if (AndroidUtils.isNetworkAvailable(this)) {

                apiCallForGetFeesList()

        }

        tvSubmit.setOnClickListener {
            var isSelected:Boolean = false
            //Add membership no
            var url = MyConfig.Endpoints.BASE_URL_FOR_PAYMENT+ MyPreference.getPreference(this@FeesActivity,MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER_ORIGINAL)

            val i = MyConfig.SCREEN.SELECTEDFEESPOSITION
            if (MyConfig.SCREEN.SELECTEDFEESPOSITION == -1){
                UiUtils.showAlertDialog(
                    this@FeesActivity ,
                    "Please select any one Renewal plan."
                )
            }else if (!checkBox.isChecked){
                UiUtils.showAlertDialog(this,"Please accept terms and conditions")
            }else{
                var feeId = feesListAdapter.currentList[i].feeId
                var amount_due = feesListAdapter.currentList[i].amountDue
                var amount_gst = feesListAdapter.currentList[i].amountGST
                var feetype = feesListAdapter.currentList[i].feeType
                var expiryDate = feesListAdapter.currentList[i].expiryDate
                url = url+"&feeid="+feeId+
                        "&fee_type="+feetype+
                        "&amount_due="+amount_due+
                        "&amount_gst="+amount_gst+
                        "&renew_type="+feetype+
                        "&expiry_date="+expiryDate+
                        "&member_id="+MyPreference.getPreference(this@FeesActivity,MyConfig.SharedPreferences.PREF_KEY_MEMBER_ID)
                Log.e("URLLLL",url)

                val intent = Intent(this@FeesActivity, WebviewActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                intent.putExtra("pagetitle", "Renewal")
                intent.putExtra("isUrl","yes")
                intent.putExtra("webUrl", url)
                startActivity(intent)

            }

        }


    }

    private fun apiCallForGetFeesList() {

        vmCommon.feesListing(
            MyPreference.getPreference(
                this,
                MyConfig.SharedPreferences.PREF_KEY_MEMBER_ID
            )
        )
    }

    private fun apiCallForGetCheckOutID(totalAmount: String, orderId: String) {
        vmCommon.getCheckOutID(
            "3", orderId
        )
    }


    private fun subscribe() {
        vmCommon.eventFeesListing.observe(this) {
            observationOfAPI(it, object : IResponseParser<MemberFeesResponseModel>(this) {
                override fun onSuccess(it: RestResponse<MemberFeesResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {

                            if (it.data!!.data != null && it.data!!.data.size > 0) {
                                rvPayment.visibility = View.VISIBLE
                                tvSubmit.visibility = View.VISIBLE
                                tvNoData.visibility = View.GONE
                                mFeesList = it.data!!.data
                                setAdapter(mFeesList)
                            }else{
                                rvPayment.visibility = View.GONE
                                tvSubmit.visibility = View.GONE
                                tvNoData.visibility = View.VISIBLE
                            }
                        } else {
                            UiUtils.showAlertDialog(
                                this@FeesActivity,
                                it.data!!.message
                            )
                        }
                    } else {
                        UiUtils.showAlertDialog(
                            this@FeesActivity,
                            it.data!!.message
                        )
                    }
                }

                override fun onError(it: RestResponse<MemberFeesResponseModel>) {
                    UiUtils.showAlertDialog(
                        this@FeesActivity,
                        it.getErrorMessage()
                    )
                }
            })
        }

        vmCommon.eventGetCheckOutId.observe(this) {
            observationOfAPI(it, object : IResponseParser<GetCheckOutIdResponseModel>(this) {
                override fun onSuccess(it: RestResponse<GetCheckOutIdResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            if (it.data!!.data != null) {
                                checkOutId = it.data!!.data.checkout_id
                                startPayment(checkOutId)
                            }
                        } else {
                            UiUtils.showAlertDialog(
                                this@FeesActivity,
                                it.data!!.message
                            )
                        }
                    } else {
                        UiUtils.showAlertDialog(
                            this@FeesActivity,
                            it.data!!.message
                        )
                    }
                }

                override fun onError(it: RestResponse<GetCheckOutIdResponseModel>) {
                    UiUtils.showAlertDialog(
                        this@FeesActivity,
                        it.getErrorMessage()
                    )
                }
            })
        }
    }

    lateinit var  feesListAdapter:FeesListAdapter

    private fun setAdapter(list: List<MemberFeesResponseModel.MemberFeesData>?) {

        rvPayment.visibility = View.VISIBLE
        rvPayment.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        feesListAdapter =
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
                            var totalAmount = (objectAtPosition!!.amountGST.toDouble() + objectAtPosition!!.amountDue).toString()
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
        Toast.makeText(this, checkOutId, Toast.LENGTH_SHORT).show()
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
        val intent = checkoutSettings!!.createCheckoutActivityIntent(this)
        startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.scheme == "companyname") {
            val checkoutId = intent.data!!.getQueryParameter("id")
            Log.e("checkOutId", checkoutId!!)
        }
        setIntent(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            CheckoutActivity.RESULT_OK -> {
                val transaction: Transaction =
                    data!!.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_TRANSACTION)!!
                val resourcePath =
                    data!!.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH)
                if (transaction!!.transactionType == TransactionType.SYNC) {
                    Toast.makeText(this, "Success in SYNC", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Success in ASYNC", Toast.LENGTH_SHORT).show()
                }
            }
            CheckoutActivity.RESULT_CANCELED -> Toast.makeText(
                this,
                "CANCELED ",
                Toast.LENGTH_SHORT
            ).show()
            CheckoutActivity.RESULT_ERROR -> {
                val error: PaymentError =
                    data!!.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_ERROR)!!
                Toast.makeText(this, "Error $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

}