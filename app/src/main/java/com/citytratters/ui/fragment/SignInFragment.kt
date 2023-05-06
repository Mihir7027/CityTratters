package com.citytratters.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.citytratters.R
import com.citytratters.base.BaseFragment
import com.citytratters.base.IResponseParser
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.LoginResponseModel
import com.citytratters.model.response.SendOTPResponseModel
import com.citytratters.myPreferance.MyPreference
import com.citytratters.network.RestResponse
import com.citytratters.ui.activity.MainActivity
import com.citytratters.ui.activity.OTPVerificationActivity
import com.citytratters.utils.AndroidUtils
import com.telkomyellow.utils.UiUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_signin.*

class SignInFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return if(MyConfig.APPSETTING.IS_SIGN_IN_WITH_BG == "1"){
             R.layout.fragment_sign_in_with_background
        }else{
            R.layout.fragment_sign_in
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribe()
        (activity as MainActivity?)!!.setToolbarWithOutLogo()
        (activity as MainActivity?)!!.setUpToolbarForHomeIcon((activity as MainActivity?)!!.getToolbarHomeLayout())
        (activity as MainActivity?)!!.getToolbarHomeLayout().setOnClickListener {
            (activity as MainActivity?)!!.navigateToHome()
        }

        (activity as MainActivity?)!!.getToolbarHomeIcon().setImageResource(R.drawable.svg_home_primary);
        (activity as MainActivity?)!!.setCofeeSideMenu()
        tvLogin.setOnClickListener {
            if (isValid()) {
                apiCallForLogin()
            }

        }
    }



    private fun apiCallForLogin() {
        vmCommon.apiLogin(etEmail.text.trim().toString(),etEmail.text.trim().toString())
    }

    private fun apiCallForSendOTP(memberID: String, phoneNumber: String) {
        vmCommon.apiSendOTP(memberID, phoneNumber)
    }

    private fun subscribe() {
        vmCommon.eventLogin.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<LoginResponseModel>(this) {
                override fun onSuccess(it: RestResponse<LoginResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            val loginResponseModel: LoginResponseModel? = it.data!!

                            val address1: String = it.data!!.data.address
                            var addressList: List<String> = address1.split(",").map { it.trim() }

                            if (addressList!= null && addressList.size != 0){
                                for(i in addressList.indices){
                                    if(i == 0){
                                        MyPreference.setPreference(
                                            requireActivity(),
                                            MyConfig.SharedPreferences.PREF_KEY_ADDRESS_ZERO,
                                            addressList[i]
                                        )
                                    }
                                    if(i == 1){
                                        MyPreference.setPreference(
                                            requireActivity(),
                                            MyConfig.SharedPreferences.PREF_KEY_ADDRESS_ONE,
                                            addressList[i]
                                        )
                                    }
                                    if (i ==2){
                                        MyPreference.setPreference(
                                            requireActivity(),
                                            MyConfig.SharedPreferences.PREF_KEY_ADDRESS_TWO,
                                            addressList[i]
                                        )
                                    }
                                    if (i ==3){
                                        MyPreference.setPreference(
                                            requireActivity(),
                                            MyConfig.SharedPreferences.PREF_KEY_SUBURB,
                                            addressList[i]
                                        )
                                    }
                                    if (i ==4){
                                        MyPreference.setPreference(
                                            requireActivity(),
                                            MyConfig.SharedPreferences.PREF_KEY_STATE_CODE,
                                            addressList[i]
                                        )
                                    }
                                    if (i ==5){
                                        MyPreference.setPreference(
                                            requireActivity(),
                                            MyConfig.SharedPreferences.PREF_KEY_POSTAL_CODE,
                                            addressList[i]
                                        )
                                    }
                                }

                            }

                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED,
                                "false"
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_LOGIN_ID,
                                etEmail.text.toString().trim()
                            )

                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_PASSWORD,
                                etPassword.text.toString().trim()
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_MEMBER_ID,
                                loginResponseModel!!.data.member_id
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_SURNAME,
                                loginResponseModel!!.data.surname
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_BADGE_NO,
                                loginResponseModel!!.data.membership_number
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_FINANCE_TO,
                                loginResponseModel!!.data.FinancialTo
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_EMAIL,
                                loginResponseModel!!.data.email
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_FIRST_NAME,
                                loginResponseModel!!.data.first_name
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_STATUS_POINTS,
                                loginResponseModel!!.data.status_points.toString()
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_TIER_LEVEL,
                                loginResponseModel!!.data.tier_level
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_PREFERRED_NAME,
                                loginResponseModel!!.data.preferred_name
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_MOBILE_NUMBER,
                                loginResponseModel!!.data.mobile
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_PROFILE_IMG_URL,
                                loginResponseModel!!.data.image
                            )

                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_OCCUPATION,
                                loginResponseModel!!.data.occupation
                            )

                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_MOBILE_NUMBER,
                                loginResponseModel!!.data.mobile
                            )
                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_PROFILE_IMG_URL,
                                loginResponseModel!!.data.image
                            )

                            MyPreference.setPreference(
                                requireActivity(),
                                MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER_ORIGINAL,
                                loginResponseModel!!.data.membership_number
                            )
                            if (loginResponseModel!!.data.membership_number.length < 5) {
                                if (loginResponseModel!!.data.membership_number.length == 1) {
                                    MyPreference.setPreference(requireActivity(),MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER,"0000" + loginResponseModel!!.data.membership_number)
                                } else if (loginResponseModel!!.data.membership_number.length == 2) {
                                    MyPreference.setPreference(requireActivity(),MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER,"000" + loginResponseModel!!.data.membership_number)
                                } else if (loginResponseModel!!.data.membership_number.length == 3) {
                                    MyPreference.setPreference(requireActivity(),MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER,"00" + loginResponseModel!!.data.membership_number)
                                } else if (loginResponseModel!!.data.membership_number.length == 4) {
                                    MyPreference.setPreference(requireActivity(),MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER,"0" + loginResponseModel!!.data.membership_number)
                                }
                            } else {
                                MyPreference.setPreference(requireActivity(),MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER,loginResponseModel!!.data.membership_number)
                            }

                            if (loginResponseModel!!.data.membership_number.length < 5) {
                                if (loginResponseModel!!.data.membership_number.length == 1) {
                                    MyConfig.Global.mMemberID = "0000"+ loginResponseModel!!.data.membership_number
                                } else if (loginResponseModel!!.data.membership_number.length == 2) {
                                    MyConfig.Global.mMemberID = "000"+loginResponseModel!!.data.membership_number
                                } else if (loginResponseModel!!.data.membership_number.length == 3) {
                                    MyConfig.Global.mMemberID = "00"+loginResponseModel!!.data.membership_number
                                } else if (loginResponseModel!!.data.membership_number.length == 4) {
                                    MyConfig.Global.mMemberID = "0"+loginResponseModel!!.data.membership_number
                                }
                            } else {
                                MyConfig.Global.mMemberID = loginResponseModel!!.data.membership_number
                            }

                            /* val i = Intent(requireActivity(), WelcomeBackActivity::class.java)
                             startActivity(i)*/

                            var mobile:String = loginResponseModel!!.data.mobile
                            mobile = mobile.replace(" ", "",true)
                            mobile = mobile.replace("(", "",true)
                            mobile = mobile.replace(")", "",true)

                            if(etEmail.text.toString().trim().equals(MyConfig.APPSETTING.SKIP_OTP_ID,true)){
                                MyPreference.setPreference(
                                    requireActivity(),
                                    MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED,
                                    "true"
                                )
                                val intent = Intent(
                                    requireActivity(),
                                    MainActivity::class.java
                                )
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }else{
                                MyConfig.Global.mMemberIDWithoutPrefix = loginResponseModel!!.data.membership_number
                                apiCallForSendOTP(
                                    loginResponseModel!!.data.membership_number,
                                    mobile
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

                override  fun onError(it: RestResponse<LoginResponseModel>) {
                    super.onError(it)

                    UiUtils.showAlertDialog(
                        requireActivity(),
                        it.getErrorMessage()
                    )
                   /* MyPreference.setPreference(
                        requireActivity(),
                        MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED,
                        "true"
                    )
                    val intent = Intent(
                        requireActivity(),
                        MainActivity::class.java
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)*/

                }
            })
        })

        vmCommon.eventSendOTP.observe(viewLifecycleOwner, {
            observationOfAPI(it, object : IResponseParser<SendOTPResponseModel>(this) {
                override fun onSuccess(it: RestResponse<SendOTPResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {

                        if (it.data!!.status == 1) {

                            val intent = Intent(
                                requireActivity(),
                                OTPVerificationActivity::class.java
                            )
                            MyConfig.Global.OTP = it.data!!.data.verification_code
                            MyConfig.Global.mMemberIDWithoutPrefix

                            startActivity(intent)
                        } else {
                            showAlertDialog(
                                requireActivity(),
                                it.data!!.message
                            )
                        }

                    } else {
                        showAlertDialog(
                            requireActivity(),
                            it.data!!.message
                        )
                    }
                }
            })
        })
    }

    private fun showAlertDialog(
        context: Context?,
        message: String?
    ) {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
            ContextThemeWrapper(
                context,
                R.style.DialogTheme
            )
        )
        builder.setMessage(message)
            .setPositiveButton(
                AndroidUtils.getString(R.string.ok)
            ) { dialog, which ->
                dialog.cancel()
            }
            .show()
    }


    private fun isValid(): Boolean {
        var isValid = true
        if (etEmail.text!!.isEmpty()) {
            Toasty.error(
                requireActivity(),
                getString(R.string.enter_email),
                Toast.LENGTH_SHORT,
                true
            ).show();
            isValid = false
        }else if (etPassword.text!!.trim().isEmpty()) {
            Toasty.error(
                requireActivity(),
                getString(R.string.enter_password),
                Toast.LENGTH_SHORT,
                true
            ).show();
            isValid = false
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity?)!!.getToolbarHomeIcon().setImageResource(R.drawable.svg_home);
    }
}