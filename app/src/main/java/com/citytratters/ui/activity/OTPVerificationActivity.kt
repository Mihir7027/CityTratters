package com.citytratters.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.citytratters.R
import com.citytratters.base.BaseActivity
import com.citytratters.base.IResponseParser
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.VerifyOTPResponseModel
import com.citytratters.myPreferance.MyPreference
import com.citytratters.network.RestResponse
import com.citytratters.utils.AndroidUtils
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.activity_otp_verification.*


class OTPVerificationActivity : BaseActivity() {

    private var memberID: String = ""

    private var otp1: String = ""
    private var otp2: String = ""
    private var otp3: String = ""
    private var otp4: String = ""

    companion object {
        fun getIntent(ctx: Context): Intent {
            return Intent(ctx, OTPVerificationActivity::class.java)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_otp_verification
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        subscribe()

        ivBack.setOnClickListener {
          finish()
        }

        //tvOTP1.setText(MyConfig.Global.OTP!!.get(0).toString())
        otp1 = MyConfig.Global.OTP!!.get(0).toString()
        otp2 = MyConfig.Global.OTP!!.get(1).toString()
        otp3 = MyConfig.Global.OTP!!.get(2).toString()
        otp4 = MyConfig.Global.OTP!!.get(3).toString()
       // tvOTP2.setText(MyConfig.Global.OTP!!.get(1).toString())
        //tvOTP3.setText(MyConfig.Global.OTP!!.get(2).toString())
        //tvOTP4.setText(MyConfig.Global.OTP!!.get(3).toString())
        tvVerify.setOnClickListener {
            val intent = Intent(
                this@OTPVerificationActivity,
                MainActivity::class.java
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        tvOTP1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty() && s.length == 1) {
                    tvOTP2.requestFocus()
                    otp1 = s.toString()
                }else{
                    otp1 = ""
                }
            }
        })
        tvOTP2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty() && s.length == 1) {
                    tvOTP3.requestFocus()
                    otp2 = s.toString()
                }else{
                    otp2 = ""
                }
            }
        })
        tvOTP3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty() && s.length == 1) {
                    tvOTP4.requestFocus()
                    otp3 = s.toString()
                }else{
                    otp3 = ""
                }
            }
        })
        tvOTP4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty() && s.length == 1) {
                    tvOTP4.clearFocus()
                    otp4 = s.toString()
                    AndroidUtils.hideSoftKeyboard(this@OTPVerificationActivity)
                }else{
                    otp4 = ""
                }
            }
        })
        tvVerify.setOnClickListener {
           // apiCallForVerifyOTP()

            if ((otp1+otp2+otp3+otp4).length == 4) {
                apiCallForVerifyOTP()
            } else {
                UiUtils.showAlertDialog(
                    this@OTPVerificationActivity,
                    getString(R.string.enter_valid_otp)
                )
            }

        }


    }


    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getDataFromIntent() {
        memberID = MyConfig.Global.mMemberIDWithoutPrefix.toString()
    }

    private fun apiCallForVerifyOTP() {
        vmCommon.apiVerifyOTP(memberID, MyPreference.getPreference(this@OTPVerificationActivity,
            MyConfig.SharedPreferences.PREF_KEY_MOBILE_NUMBER
        ), otp1+otp2+otp3+otp4)
    }

    private fun subscribe() {
        vmCommon.eventVerifyOTP.observe(this, androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<VerifyOTPResponseModel>(this) {
                override fun onSuccess(it: RestResponse<VerifyOTPResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            MyPreference.setPreference(
                                this@OTPVerificationActivity,
                                MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED,
                                "true"
                            )
                            val intent = Intent(
                                this@OTPVerificationActivity,
                                MainActivity::class.java
                            )
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            UiUtils.showAlertDialog(
                                this@OTPVerificationActivity,
                                it.data!!.message
                            )
                        }


                    } else {
                        UiUtils.showAlertDialog(
                            this@OTPVerificationActivity,
                            it.data!!.message
                        )
                    }
                }
            })
        })
    }
}
