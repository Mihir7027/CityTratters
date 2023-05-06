package com.citytratters.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.citytratters.base.BaseApplication
import com.citytratters.constants.MyConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.StringBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AndroidUtils {


    companion object {

        private var mLastClickTime: Long = 0
        private const val timeBetweenClick = 600 //in ns
        var PRIZE_COUNT = 0


        val KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates"

        /**
         * Returns true if requesting location updates, otherwise returns false.
         *
         * @param context The [Context].
         */

        @SuppressLint("HardwareIds")
        fun getDeviceId(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun toCamelCase(string: String?): String? {

            // Check if String is null
            if (string == null) {
                return null
            }
            var whiteSpace = true
            val builder = StringBuilder(string) // String builder to store string
            val builderLength = builder.length

            // Loop through builder
            for (i in 0 until builderLength) {
                val c = builder[i] // Get character at builders position
                if (whiteSpace) {

                    // Check if character is not white space
                    if (!Character.isWhitespace(c)) {
                        // Convert to title case and leave whitespace mode.
                        builder.setCharAt(i, Character.toTitleCase(c))
                        whiteSpace = false
                    }
                } else if (Character.isWhitespace(c)) {
                    whiteSpace = true // Set character is white space
                } else {
                    builder.setCharAt(i, Character.toLowerCase(c)) // Set character to lowercase
                }
            }
            return builder.toString() // Return builders text
        }

        fun requestingLocationUpdates(context: Context): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
        }

        fun isClickEnable(): Boolean {
            return if (SystemClock.elapsedRealtime() - mLastClickTime < timeBetweenClick) false else {
                mLastClickTime = SystemClock.elapsedRealtime()
                true
            }
        }

        fun twoDigitAfterDecimal(distance: Double): String? {
            try {
                return String.format(Locale.US, "%.2f", distance)
            }catch (e:java.lang.Exception){
                return distance.toString()
            }
        }

        fun threeDigitAfterDecimal(distance: Double): String? {
            try {
                return String.format(Locale.US, "%.3f", distance)
            }catch (e:java.lang.Exception){
                return distance.toString()
            }
        }

        fun changeDateFormat(
            inputDateString: String?,
            inputFormat: String?,
            outputFormat: String?
        ): String? {
            var outputDateString = ""
            if (inputDateString != null && inputDateString != "null" && inputDateString != "") {

                var format = SimpleDateFormat(inputFormat)
                var newDate: Date? = null
                try {
                    newDate = format.parse(inputDateString)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                format = SimpleDateFormat(outputFormat)
                outputDateString = format.format(newDate)

            }

            return outputDateString
        }

        fun requestRuntimePermission(
            activity: Activity?,
            title: String?,
            message: String?,
            permissions: Array<String>,
            requestCode: Int
        ): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var isAllpermissionGranted = true
                var shouldShowPermissionalRationale = false
                val permissionNotGranted: MutableList<String> =
                    ArrayList()
                for (i in permissions.indices) {
                    if (ContextCompat.checkSelfPermission(
                            activity!!,
                            permissions[i]
                        ) !== PackageManager.PERMISSION_GRANTED
                    ) {
                        isAllpermissionGranted = false
                        permissionNotGranted.add(permissions[i])
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity!!,
                            permissions[i]
                        )
                    ) {
                        shouldShowPermissionalRationale = true
                    }
                }
                if (isAllpermissionGranted) {
                    return true
                }
                if (shouldShowPermissionalRationale) {
                    val builder: AlertDialog.Builder
                    builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        AlertDialog.Builder(
                            activity,
                            android.R.style.ThemeOverlay_Material_Dialog_Alert
                        )
                    } else {
                        AlertDialog.Builder(activity)
                    }
                    builder.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(
                            android.R.string.yes
                        ) { dialog, which ->
                            ActivityCompat.requestPermissions(
                                activity!!,
                                permissionNotGranted.toTypedArray(),
                                requestCode
                            )
                        }
                        .setNegativeButton(
                            android.R.string.no
                        ) { dialog, which ->
// do nothing
                        }
                        .show()
                } else {
                    ActivityCompat.requestPermissions(
                        activity!!,
                        permissionNotGranted.toTypedArray(),
                        requestCode
                    )
                }
                false
            } else {
                true
            }
        }


        fun getAppVersion(context: Context): String? {
            var version = ""
            val manager = context.packageManager
            var info: PackageInfo? = null
            try {
                info = manager.getPackageInfo(context.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            version = info!!.versionName
            return version
        }

        fun isNetworkAvailable(mContext: Context): Boolean {

            /* getting systems Service connectivity manager */
            val mConnectivityManager = mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (mConnectivityManager != null) {
                val mNetworkInfos =
                    mConnectivityManager.allNetworkInfo
                if (mNetworkInfos != null) {
                    for (i in mNetworkInfos.indices) {
                        if (mNetworkInfos[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        /**
         * Stores the location updates state in SharedPreferences.
         * @param requestingLocationUpdates The location updates state.
         */
        fun setRequestingLocationUpdates(context: Context, requestingLocationUpdates: Boolean) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply()
        }

        /**
         * Returns the `location` object as a human readable string.
         * @param location  The [Location].
         */
        fun getLocationText(location: Location?): String {
            return if (location == null)
                "Unknown location"
            else
                "(" + location!!.getLatitude() + ", " + location!!.getLongitude() + ")"
        }


        fun replaceFragment(
            fragmentManager: androidx.fragment.app.FragmentManager?, @IdRes id: Int,
            fragment: androidx.fragment.app.Fragment,
            tag: String = fragment::class.java.name
        ) {
            Log.i("TAGTAG", "TAG " + tag)
            fragmentManager?.beginTransaction()
                ?.replace(id, fragment, tag)
                ?.commitAllowingStateLoss()
        }

        fun getCurrentDate(toDateFormat: String): String {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern(toDateFormat)
                var answer: String = current.format(formatter)
                return answer
            } else {
                var date = Date();
                val formatter = SimpleDateFormat(toDateFormat)
                val answer: String = formatter.format(date)
                return answer
            }
        }

        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive) {
                if (activity.currentFocus != null) {
                    inputMethodManager.hideSoftInputFromWindow(
                        activity.currentFocus!!.windowToken, 0
                    )
                }
            }
        }


        @JvmStatic
        fun getString(@StringRes id: Int, vararg objects: Any?) = if (objects.isEmpty()) {
            BaseApplication.getInstance().resources.getString(id)
        } else {
            BaseApplication.getInstance().resources.getString(id, *objects)
        }

        fun formatNumber(no: Int): String {
            return String.format("%02d", no)
        }


        fun getColor(@ColorRes id: Int) =
            ContextCompat.getColor(BaseApplication.getInstance(), id)

        fun convertToTimeFormat(
            fromDateFormat: String? = MyConfig.DateFormat.yyyy_MM_dd_T_HH_mm_ss,
            toDateFormat: String,
            dateValue: String?
        ): String {
            return try {


                val formatFrom = SimpleDateFormat(
                    fromDateFormat, Locale.US
                )
                //formatFrom.timeZone = TimeZone.getTimeZone("UTC")
                val date = formatFrom.parse(dateValue)
                val formatTo = SimpleDateFormat(
                    toDateFormat, Locale.US
                )

                formatTo.timeZone = TimeZone.getDefault()
                return formatTo.format(date)

            } catch (e: Exception) {
                ""
            }
        }

        fun convertToTimeFormatUsingUTC(
            fromDateFormat: String? = MyConfig.DateFormat.yyyy_MM_dd_T_HH_mm_ss,
            toDateFormat: String,
            dateValue: String?
        ): String {
            return try {


                val formatFrom = SimpleDateFormat(
                    fromDateFormat, Locale.US
                )
                formatFrom.timeZone = TimeZone.getTimeZone("UTC")
                val date = formatFrom.parse(dateValue)
                val formatTo = SimpleDateFormat(
                    toDateFormat, Locale.US
                )

                formatTo.timeZone = TimeZone.getDefault()
                return formatTo.format(date)

            } catch (e: Exception) {
                ""
            }
        }

        fun convertStringDateToMilliseconds(
            fromDateFormat: String? = MyConfig.DateFormat.yyyy_MM_dd_T_HH_mm_ss,
            dateValue: String?
        ): Long {
            return try {


                val formatFrom = SimpleDateFormat(
                    fromDateFormat, Locale.US
                )
                //formatFrom.timeZone = TimeZone.getTimeZone("UTC")
                val date = formatFrom.parse(dateValue)

                return date.time

            } catch (e: Exception) {
                0
            }
        }

        fun convertToTimeFormatForAttendance(
            fromDateFormat: String? = "hh:mm a dd MMM yyyy",
            toDateFormat: String,
            dateValue: String?
        ): String {
            return try {


                val formatFrom = SimpleDateFormat(
                    fromDateFormat, Locale.US
                )
                //formatFrom.timeZone = TimeZone.getTimeZone("UTC")
                val date = formatFrom.parse(dateValue)
                val formatTo = SimpleDateFormat(
                    toDateFormat, Locale.US
                )

                formatTo.timeZone = TimeZone.getDefault()
                return formatTo.format(date)

            } catch (e: Exception) {
                ""
            }
        }

        fun convertTimeFormatForGraph(
            fromDateFormat: String,
            toDateFormat: String,
            dateValue: String?
        ): String {
            return try {


                val formatFrom = SimpleDateFormat(
                    fromDateFormat, Locale.US
                )
                //formatFrom.timeZone = TimeZone.getTimeZone("UTC")
                val date = formatFrom.parse(dateValue)
                val formatTo = SimpleDateFormat(
                    toDateFormat, Locale.US
                )

                formatTo.timeZone = TimeZone.getDefault()
                return formatTo.format(date)

            } catch (e: Exception) {
                ""
            }
        }


        fun getDateAndTime(
            toDateFormat: String? = MyConfig.DateFormat.yyyy_MM_dd_T_HH_mm_ss,
            hr: Int,
            minute: Int,
            day: Int
        ): String {
            return try {

                var date = Date()
                var calendar = Calendar.getInstance()
                calendar.setTime(date)
                calendar.set(Calendar.DAY_OF_WEEK, day)
                calendar.set(Calendar.HOUR_OF_DAY, hr) // for hour
                calendar.set(Calendar.MINUTE, minute) // for 0 min
                calendar.set(Calendar.SECOND, 0) // for 0 sec
                System.out.println(calendar.time)

                val formatTo = SimpleDateFormat(
                    toDateFormat, Locale.US
                )

                formatTo.timeZone = TimeZone.getDefault()
                return formatTo.format(calendar.time)

            } catch (e: Exception) {
                ""
            }
        }

        fun getDateAndTime(
            toDateFormat: String? = MyConfig.DateFormat.yyyy_MM_dd_T_HH_mm_ss,
            hr: Int,
            minute: Int
        ): String {
            return try {

                var date = Date()
                var calendar = Calendar.getInstance()
                calendar.setTime(date)
                calendar.set(Calendar.HOUR_OF_DAY, hr) // for hour
                calendar.set(Calendar.MINUTE, minute) // for 0 min
                calendar.set(Calendar.SECOND, 0) // for 0 sec
                System.out.println(calendar.time)

                val formatTo = SimpleDateFormat(
                    toDateFormat, Locale.US
                )

                formatTo.timeZone = TimeZone.getDefault()
                return formatTo.format(calendar.time)

            } catch (e: Exception) {
                ""
            }
        }

        fun formatTimeFrom24to12(
            inputTime: String
        ): String {
            return try {

                val inputSimpleDateFormat = SimpleDateFormat("H:mm")
                val outputSimpleDateFormat = SimpleDateFormat("hh:mm a")
                val date = inputSimpleDateFormat.parse(inputTime)
                return outputSimpleDateFormat.format(date).toUpperCase()

            } catch (e: Exception) {
                ""
            }
        }

        fun formatTimeFrom12to24(
            inputTime: String
        ): String {
            return try {

                val inputSimpleDateFormat = SimpleDateFormat("hh:mm a")
                val outputSimpleDateFormat = SimpleDateFormat("HH:mm")
                val date = inputSimpleDateFormat.parse(inputTime)
                return outputSimpleDateFormat.format(date).toUpperCase()

            } catch (e: Exception) {
                ""
            }
        }


        fun getYesterdayDate(toDateFormat: String): String {

            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 val current = LocalDateTime.now()
                 val formatter = DateTimeFormatter.ofPattern(toDateFormat)
                 var answer: String = current.format(formatter)
                 return answer
             } else {*/
            // }
            var date = Date();
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, -1)
            val formatter = SimpleDateFormat(toDateFormat)
            val answer: String = formatter.format(calendar.time)
            return answer
        }

        fun getTomorrowDate(toDateFormat: String): String {

            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 val current = LocalDateTime.now()
                 val formatter = DateTimeFormatter.ofPattern(toDateFormat)
                 var answer: String = current.format(formatter)
                 return answer
             } else {*/
            // }
            var date = Date();
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val formatter = SimpleDateFormat(toDateFormat)
            val answer: String = formatter.format(calendar.time)
            return answer
        }

        fun getStringArray(@ArrayRes id: Int): Array<String>? {
            return BaseApplication.getInstance().resources.getStringArray(id)
        }


        fun getMultiParFromURI(path: String?, name: String?): MultipartBody.Part? {
            if (!path.isNullOrEmpty() && !name.isNullOrEmpty()) {


                try {
                    val imageUri = Uri.parse(path)
                    val file = File(imageUri.path)
                    val fileName = file.name

                    val contentType = "image/*".toMediaTypeOrNull()

                    val requestFile = RequestBody.create(
                        contentType,
                        file
                    )

                    return MultipartBody.Part.createFormData(name, fileName, requestFile)


                } catch (e: Exception) {
                }

            }

            return null
        }

    }
}