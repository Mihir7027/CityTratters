package com.citytratters.ui.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.annotation.RequiresApi
import com.citytratters.R
import com.citytratters.base.BaseFragment
import com.citytratters.base.IResponseParser
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.LoginResponseModel
import com.citytratters.model.response.ResponseSuccess
import com.citytratters.model.response.SaveWalletResponseModel
import com.citytratters.myPreferance.MyPreference
import com.citytratters.network.RestResponse
import com.citytratters.ui.activity.FeesActivity
import com.citytratters.ui.activity.MainActivity
import com.citytratters.ui.activity.WebviewForHTMLContentActivity
import com.citytratters.utils.AndroidUtils
import com.google.android.gms.wallet.WalletConstants
import com.google.android.gms.wallet.WalletObjectsClient
import com.google.zxing.WriterException
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.fragment_digital_members_card.*
import kotlinx.android.synthetic.main.fragment_digital_members_card.etEmail
import java.text.SimpleDateFormat
import java.util.*


class DigitalMembersCardFragment : BaseFragment() {

    private lateinit var walletObjectsClient: WalletObjectsClient
    private var bitmap: Bitmap? = null
    private var qrgEncoder: QRGEncoder? = null
    private var inputValue: String? = ""

    var listPopupWindow: ListPopupWindow? = null
    lateinit var status: Array<String>
    var sensorManager: SensorManager? = null
    var sensor: Sensor? = null

    val c = Calendar.getInstance()
    var year = 0
    var month = 0
    var day = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_digital_members_card
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        status = resources.getStringArray(R.array.status_array);

        (activity as MainActivity?)!!.setToolbarWithWhiteLogo()
        (activity as MainActivity?)!!.setUpToolbarForHomeIcon((activity as MainActivity?)!!.getToolbarHomeLayout())
        (activity as MainActivity?)!!.getToolbarHomeLayout().setOnClickListener {
            (activity as MainActivity?)!!.navigateToHome()
        }
        termsAndConditionLink.setOnClickListener {
            val intent = Intent(
                requireActivity(),
                WebviewForHTMLContentActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra("title",getString(R.string.terms_and_conditions))
            startActivity(intent)
        }
        if (MyConfig.SCREEN.ISCARD == "1"
        ) {
            llCard.visibility = View.VISIBLE
            llAddCard.visibility = View.GONE
        } else {
            llCard.visibility = View.GONE
            llAddCard.visibility = View.VISIBLE
            tvTitle.text = "Membership Application"
        }

        etMembershipType.setOnClickListener {
            setUpListPopup()
        }


        btnRenewal.setOnClickListener {

            val intent = Intent(requireActivity(), FeesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

        tvSubmit.setOnClickListener {
            if (isValid()) {
                apiCallForAddCard()
            }
        }

        var selectedYear = 0
        var selectedMonth = 0
        var selectedDay = 0

        etDOB.setOnClickListener {
            if (day == 0) {
                year = c.get(Calendar.YEAR)
                month = c.get(Calendar.MONTH)
                day = c.get(Calendar.DAY_OF_MONTH)
            } else {
                year = selectedYear
                month = selectedMonth
                day = selectedDay
            }
            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    etDOB.setText("" + dayOfMonth + "/" + monthOfYear + 1 + "/" + year)
                    selectedYear = year
                    selectedMonth = monthOfYear
                    selectedDay = dayOfMonth
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        subscribe()
        btnSave.setOnClickListener {
            apiCallForSaveWallet()
        }

        var expiryDate:String =  MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_FINANCE_TO
        ).toString().trim()
        expiryDate =AndroidUtils.changeDateFormat(
            inputDateString =expiryDate,
            inputFormat = MyConfig.DateFormat.yyyy_mm_ddTHHMM,
            outputFormat = "yyyy"
        ).toString()

        if (isPlanExpired(expiryDate)){
            btnRenewal.visibility = View.VISIBLE
        }else{
            btnRenewal.visibility = View.GONE
        }
    }

    private fun apiCallForAddCard() {
        vmCommon.addCard(
            tvFname.text.toString().trim(),
            etLName.text.toString().trim(),
            etDOB.text.toString().trim(),
            etEmail.text.toString().trim(),
            etAddress.text.toString().trim(),
            etMobile.text.toString().trim(),
            etMembershipType.text.toString().trim(),
            etPromoCode.text.toString().trim()
        )
    }


    private fun apiCallForSaveWallet() {

        vmCommon.apiSaveWallet(
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER
            ),
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_FIRST_NAME
            ).toUpperCase() + " " + MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_SURNAME
            ).toUpperCase(),
            inputValue,
            MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_TIER_LEVEL
            ),
            tvValue.text.trim().toString()
        )
    }

    private fun subscribe() {
        vmCommon.eventSaveWallet.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<SaveWalletResponseModel>(this) {
                override fun onSuccess(it: RestResponse<SaveWalletResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 200) {
                            val url = it.data!!.data
                            //Log.e("url",url)
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(url)
                            startActivity(i)
                        } else {
                            UiUtils.showAlertDialog(
                                activity!!,
                                it.data!!.message
                            )
                        }


                    } else {
                        UiUtils.showAlertDialog(
                            activity!!,
                            it.data!!.message
                        )
                    }
                }

                override fun onError(it: RestResponse<SaveWalletResponseModel>) {
                    super.onError(it)
                    UiUtils.showAlertDialog(
                        activity!!,
                        it.getErrorMessage()
                    )
                }
            })
        })

        vmCommon.eventAddCard.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<ResponseSuccess>(this) {
                override fun onSuccess(it: RestResponse<ResponseSuccess>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
                            (activity as MainActivity?)!!.replaceFragment(HomeFragment())
                        } else {
                            UiUtils.showAlertDialog(
                                activity!!,
                                it.data!!.message
                            )
                        }
                    } else {
                        UiUtils.showAlertDialog(
                            activity!!,
                            it.data!!.message
                        )
                    }
                }

                override fun onError(it: RestResponse<ResponseSuccess>) {
                    super.onError(it)
                    UiUtils.showAlertDialog(
                        activity!!,
                        it.getErrorMessage()
                    )
                }
            })
        })

        vmCommon.eventLogin.observe(requireActivity(), androidx.lifecycle.Observer {
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
                                MyConfig.SharedPreferences.PREF_KEY_MEMBER_ID,
                                loginResponseModel!!.data.member_id
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

                            setData()
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


    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(gyroListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED
            ) == "true"
        ) {
            vmCommon.apiLogin(MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_LOGIN_ID
            ),MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_LOGIN_ID
            ))

            // setData()
        }
    }

    override fun onStop() {
        super.onStop()
        sensorManager!!.unregisterListener(gyroListener)
    }

    var gyroListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, acc: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            cvCard.rotationX = y
            cvCard.rotationY = x
            cvCard.cardElevation = 22F


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Toast.makeText(requireActivity(), "Saved", Toast.LENGTH_LONG)
                            .show()

                    }
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(
                            requireActivity(),
                            "Canceled",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    else -> {
                        val errorCode = data!!.getIntExtra(
                            WalletConstants.EXTRA_ERROR_CODE, -1
                        )
                        Toast.makeText(
                            requireActivity(),
                            "error---->" + errorCode,
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }
            }
        }
    }


    private fun isValid():Boolean{
        var isValid = true
        if (etFName.text.toString().trim() == ""){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Enter first name")
        }else if (etLName.text.toString().trim() == ""){
            isValid = false

            UiUtils.showAlertDialog(requireActivity(),"Enter last name")
        }else if (etDOB.text.toString().trim() == ""){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Select date of birth")
        }else if (etMobile.text.toString().trim() == ""){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Enter mobile number")
        }else if(etMobile.text.toString().trim() != "" && !isValidPhoneNumber(etMobile.text.toString().trim())){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Enter correct mobile number")

        }else if (etEmail.text.toString().trim() == ""){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Enter email address")
        }else if (etEmail.text.toString().trim() != "" && !isValidEmail(etEmail.text.toString().trim())){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Enter correct email address")
        }else if (etAddress.text.toString().trim() == ""){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Enter address")
        }else if (etMembershipType.text.toString().trim() == ""){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Select membership type")
        }
        else if (!checkBox.isChecked){
            isValid = false
            UiUtils.showAlertDialog(requireActivity(),"Please accept terms and conditions")
        }

        return  isValid
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


    private fun setData() {
        var pointForInputValue:String = ""

        if (MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_STATUS_POINTS
            ) != null && MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_STATUS_POINTS
            ).toString() != ""
        ) {
            pointForInputValue =  AndroidUtils.twoDigitAfterDecimal(
                MyPreference.getPreference(
                    requireActivity(),
                    MyConfig.SharedPreferences.PREF_KEY_STATUS_POINTS
                ).toDouble()
            ).toString()
        }

        inputValue =
            ";01191" + MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER
            ) + "?"

        Log.e("inputValue", inputValue!!)
        val data = inputValue!!.toByteArray(charset("UTF-8"))
        //val base64 = Base64.encodeToString(data, Base64.DEFAULT)

        tvName.text = MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_FIRST_NAME
        ).toUpperCase() + " " + MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_SURNAME
        ).toUpperCase()

        tvIdNumber.text = MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER
        )

        tvMobile.text = MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_MOBILE_NUMBER
        )
        tvFname.text = MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_FIRST_NAME
        )
        lName.text = MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_SURNAME
        )
        tvEmail.text =
            MyPreference.getPreference(requireActivity(), MyConfig.SharedPreferences.PREF_KEY_EMAIL)
        tvAddress.text = MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_ADDRESS_ZERO
        ) + " " + MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_ADDRESS_ONE
        ) + " " + MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_ADDRESS_TWO
        ) + " " + MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_SUBURB
        ) + " " + MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_STATE_CODE
        )
        tvTier.text = MyPreference.getPreference(
            requireActivity(),
            MyConfig.SharedPreferences.PREF_KEY_TIER_LEVEL
        )


        if (MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_FINANCE_TO
            ).toString().trim() != null && MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_FINANCE_TO
            ).toString().trim() != ""  && MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_FINANCE_TO
            ).toString().length > 3 ){
            tvYear.setText(

                MyPreference.getPreference(
                    requireActivity(),
                    MyConfig.SharedPreferences.PREF_KEY_FINANCE_TO
                ).toString().trim().substring(0,4)
            )
        }

        if (MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_STATUS_POINTS
            ) != null && MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_STATUS_POINTS
            ).toString() != ""
        ) {
            tvPoint.text =  AndroidUtils.twoDigitAfterDecimal(
                MyPreference.getPreference(
                    requireActivity(),
                    MyConfig.SharedPreferences.PREF_KEY_STATUS_POINTS
                ).toDouble()
            ).toString()
        }
        if (MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_STATUS_POINTS
            ) != null && MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_STATUS_POINTS
            ).toString() != ""
        ) {
            tvValue.text = "$" + " " + MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_STATUS_POINTS
            ).toDouble()?.div(
                10000
            )?.let {
                AndroidUtils.threeDigitAfterDecimal(
                    it
                ).toString()
            }
        }

        if (MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_PROFILE_IMG_URL
            ) != null && MyPreference.getPreference(
                requireActivity(),
                MyConfig.SharedPreferences.PREF_KEY_PROFILE_IMG_URL
            ).toString() != ""
        ) {
            val imageBytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                java.util.Base64.getDecoder().decode(
                    MyPreference.getPreference(
                        requireActivity(),
                        MyConfig.SharedPreferences.PREF_KEY_PROFILE_IMG_URL
                    )
                        .toString()
                )
            } else {
                Base64.decode(
                    MyPreference.getPreference(
                        requireActivity(),
                        MyConfig.SharedPreferences.PREF_KEY_PROFILE_IMG_URL
                    )
                        .toString(), Base64.DEFAULT
                ) // Unresolved reference: decode
            }

            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ivProfileImage.setImageBitmap(decodedImage)

        }



        try {
            val qrgEncoder = QRGEncoder(inputValue, null, QRGContents.Type.TEXT, 100)
            val bitmap = qrgEncoder.encodeAsBitmap()
            ivQRCode.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }


    private fun setUpListPopup() {
        listPopupWindow = ListPopupWindow(
            requireActivity()
        )
        listPopupWindow!!.setAdapter(
            ArrayAdapter<Any?>(
                requireActivity(),
                com.citytratters.R.layout.row_status, status
            )
        )
        listPopupWindow!!.setAnchorView(etMembershipType)
        listPopupWindow!!.setModal(true)
        listPopupWindow!!.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l ->
            etMembershipType.setText(status[i])
            listPopupWindow!!.dismiss()
        })
        listPopupWindow!!.show()
    }


    private fun isPlanExpired(getMyTime:String):Boolean{

        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy")
        val getCurrentDateTime: String = sdf.format(c.time)
       // val getMyTime = "05/19/2016 09:45 PM "
        Log.d("getCurrentDateTime", getCurrentDateTime)
        Log.d("getCurrentDateTime", getMyTime)
        Log.d("getCurrentDateTime", (getCurrentDateTime >= getMyTime).toString())

        return Integer.parseInt(getCurrentDateTime) >= Integer.parseInt(getMyTime)

    }
}