package com.citytratters.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.annotation.RequiresApi
import com.citytratters.R
import com.citytratters.base.BaseFragment
import com.citytratters.base.IResponseParser
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.LoginResponseModel
import com.citytratters.model.response.UpdateUserDetailResponseModel
import com.citytratters.myPreferance.MyPreference
import com.citytratters.network.RestResponse
import com.citytratters.ui.activity.MainActivity
import com.telkomyellow.utils.UiUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_update_profile.*
import java.util.*


class UpdateProfileFragment : BaseFragment() {
    lateinit var stateCode: Array<String>

    var listPopupWindow: ListPopupWindow? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_update_profile
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

        stateCode = resources.getStringArray(R.array.state_code_array);
        tvSubmit.setOnClickListener {
            if (isValid()) {
                apiCallForUpdateDetail()
            }
        }
        etState.setOnClickListener {
            setUpListPopup()
        }

    }

    private fun setUpListPopup()
    {
        listPopupWindow = ListPopupWindow(
            requireActivity()
        )
        listPopupWindow!!.setAdapter(
            ArrayAdapter<Any?>(
                requireActivity(),
                R.layout.row_state_code, stateCode
            )
        )
        listPopupWindow!!.anchorView =  etState
        listPopupWindow!!.isModal = true
        listPopupWindow!!.setOnItemClickListener { adapterView, view, i, l ->
            if (stateCode[i].equals(getString(R.string.nsw), ignoreCase = true)) {
                etState.setText(getString(R.string.nsw))
            }else if (stateCode[i].equals(getString(R.string.vic), ignoreCase = true)) {
                etState.setText(getString(R.string.vic))
            }else if (stateCode[i].equals(getString(R.string.qld), ignoreCase = true)) {
                etState.setText(getString(R.string.qld))
            }else if (stateCode[i].equals(getString(R.string.sa), ignoreCase = true)) {
                etState.setText(getString(R.string.sa))
            }else if (stateCode[i].equals(getString(R.string.tas), ignoreCase = true)) {
                etState.setText(getString(R.string.tas))
            }else if (stateCode[i].equals(getString(R.string.wa), ignoreCase = true)) {
                etState.setText(getString(R.string.wa))
            }
            listPopupWindow!!.dismiss()
        }
        listPopupWindow!!.show()
    }



    private fun apiCallForUpdateDetail() {
        val mFieldMap: MutableMap<String, String> = HashMap()

        mFieldMap["badge_no"] = MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER_ORIGINAL
        )
        mFieldMap["email"] = etEmail.text.toString().trim()
        mFieldMap["pin"] = MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_PASSWORD
        )
        mFieldMap["state"] = etState.text.toString().trim()
        mFieldMap["occupation"] = etOccupation.text.toString().trim()
        mFieldMap["phone"] = etContactNumber.text.toString().trim()
        mFieldMap["address1"] = etAddressOne.text.toString().trim()
        mFieldMap["address2"] = etAddressTwo.text.toString().trim()
        mFieldMap["postcode"] = etPostalCode.text.toString().trim()
        mFieldMap["suburb"] = etSuburb.text.toString().trim()

        vmCommon.apiUpdateUserDetail(mFieldMap)
    }

    private fun apiCallForLogin() {
        vmCommon.apiLogin(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER_ORIGINAL
            ),MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER_ORIGINAL
            )
        )
    }


    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun subscribe() {
        vmCommon.eventUpdateUserDetail.observe(requireActivity()) {
            observationOfAPI(it, object : IResponseParser<UpdateUserDetailResponseModel>(this) {
                override fun onSuccess(it: RestResponse<UpdateUserDetailResponseModel>) {
                    super.onSuccess(it)

                    if (it.status != RestResponse.Status.ERROR) {

                        if (it.data!!.status == 1) {
                            etEmail.setText("")
                            etContactNumber.setText("")
                            etOccupation.setText("")
                            etAddressOne.setText("")
                            etAddressTwo.setText("")
                            etPostalCode.setText("")
                            etState.setText("")
                            etSuburb.setText("")
                            apiCallForLogin()
                            Toasty.success(
                                requireActivity(),
                                getString(R.string.detail_successfully_updated)
                            ).show()

                        } else {
                            UiUtils.showAlertDialog(
                                requireActivity(),
                                it.data!!.message
                            )
                        }
                    } else {
                        UiUtils.showAlertDialog(
                            requireActivity(),
                            getString(R.string.server_error)
                        )
                    }
                }
            })
        }


        vmCommon.eventLogin.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<LoginResponseModel>(this) {
                override fun onSuccess(it: RestResponse<LoginResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            val loginResponseModel: LoginResponseModel? = it.data!!

                            val address1: String = it.data!!.data.address
                            var addressList: List<String> = address1.split(" ").map { it.trim() }

                            if (addressList != null && addressList.size != 0) {
                                for (i in addressList.indices) {
                                    if (i == 1) {
                                        MyPreference.setPreference(
                                            requireActivity(),
                                            MyConfig.SharedPreferences.PREF_KEY_ADDRESS_ONE,
                                            addressList[i]
                                        )
                                    }
                                    if (i == 2) {
                                        MyPreference.setPreference(
                                            requireActivity(),
                                            MyConfig.SharedPreferences.PREF_KEY_ADDRESS_TWO,
                                            addressList[i]
                                        )
                                    }
                                    if (i == 3) {
                                        MyPreference.setPreference(
                                            requireActivity(),
                                            MyConfig.SharedPreferences.PREF_KEY_SUBURB,
                                            addressList[i]
                                        )
                                    }

                                    if (i == 4) {
                                        MyPreference.setPreference(
                                            requireActivity(),
                                            MyConfig.SharedPreferences.PREF_KEY_STATE_CODE,
                                            addressList[i]
                                        )
                                    }
                                    if (i == 5) {
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
                                MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER,
                                loginResponseModel!!.data.membership_number
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
                                MyConfig.SharedPreferences.PREF_KEY_CARD_NUMBER,
                                loginResponseModel!!.data.cardNumber
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
                            setData()
                            MyConfig.Global.mMemberID = loginResponseModel!!.data.membership_number
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
            })
        })

    }

    private fun isValid(): Boolean {
        var isValid = true

        if (etEmail.text.toString().trim() == ""){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Enter email address")
        }else if (etEmail.text.toString().trim() != "" && !isValidEmail(etEmail.text.toString().trim())){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Enter correct email address")
        }else if (etContactNumber.text.toString().trim() == ""){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Enter mobile number")
        }else if(etContactNumber.text.toString().trim() != "" && !isValidPhoneNumber(etContactNumber.text.toString().trim())){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Enter correct mobile number")
        }

        return isValid
    }



    private fun setData() {

        etEmail.setText(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_EMAIL
            ).trim()
        )

        etContactNumber.setText(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_MOBILE_NUMBER
            ).trim()
        )
        etOccupation.setText(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_OCCUPATION
            ).trim()
        )

        etAddressOne.setText(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_ADDRESS_ZERO
            ).trim()
        )
        etAddressTwo.setText(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_ADDRESS_ONE
            ).trim()
        )
        etSuburb.setText(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_ADDRESS_TWO
            ).trim()
        )
        etState.setText(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_SUBURB
            ).trim()
        )
        etPostalCode.setText(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_STATE_CODE
            ).trim()
        )


    }



    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    var stringRegex = "^(?:\\+?(61))? ?(?:\\((?=.*\\)))?(0?[2-57-8])\\)? ?(\\d\\d(?:[- ](?=\\d{3})|(?!\\d\\d[- ]?\\d[- ]))\\d\\d[- ]?\\d[- ]?\\d{3})\$"
    fun isValidPhoneNumber(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            return target!!.matches(Regex(stringRegex))
        }
    }


}