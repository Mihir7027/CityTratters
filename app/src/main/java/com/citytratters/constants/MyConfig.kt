package com.citytratters.constants

import com.citytratters.model.response.BarsDetailResponseModel
import com.citytratters.model.response.SubMenuDetailResponseModel
import com.citytratters.model.response.SubMenuListForTypeOneResponseModel
import java.util.ArrayList

class MyConfig {

    object APPSETTING {
        //set 0 for no color and set 1 for set color(colorPrimary)
        const val IS_BG_COLOR_IN_SIDE_MENU = "1"
        const val IS_BG_COLOR_IN_HOME_SCREEN_MENU = "1"
        const val IS_SIGN_IN_WITH_BG = ""
        const val IS_HOME_ICON_IN_TOOLBAR = "0"
        const val SKIP_OTP_ID = "18"
        const val HOME_FRAGMENT_PATH = "com.citytratters.ui.fragment.HomeFragment"
        const val FLURRY_API_KEY = "DFQD3BVGTSS5NHBP6D6S"
    }

    object Endpoints {

        //Live
        //const val BASE_URL = "http://3.25.233.120/cabravale/api/"
        const val BASE_URL = "http://3.25.233.120/city-tattersalls/api/"
        const val BASE_URL_FOR_PAYMENT = "http://3.25.233.120/city-tattersalls/payment-new?badge_no="

        //Testing
        //const val BASE_URL = "http://10.10.1.52:8087/api/"
        const val API_LOGIN = "login"
        const val ADD_PRIZES = "addprize"
        const val GET_ALL_PRIZES = "getallprizes"
        const val REGISTER_TOKEN = "registertoken"
        const val API_GET_BOXES = "getboxes"
        const val API_BARS_DETAIL = "bardetail"
        const val GET_MY_PRIZES = "getmyprizes"
        const val REDEEM_PRIZES  = "redeemprizes"
        const val REDEEM_VOUCHER  = "redeemvoucher"
        const val API_GET_MORE_DETAIL = "moredetail"
        const val API_SUB_MENU_DETAIL = "submenudetail"
        const val API_SUB_MENU_LIST  = "submenulist"
        const val API_UPDATE_USER_DETAIL = "update"
        const val API_SEND_OTP = "sendotp"
        const val API_VERIFY_OTP = "verifyotp"
        const val API_BAR_LISTING = "barlisting"
        const val API_OFFER_LISTING = "offerlist"
        const val API_FEES_LISTING = "getmemberfees"
        const val API_GET_CHECKOUT_ID = "tillpaymentcheckout"
        const val API_ADD_CARD = "newupdate"
        const val API_SAVE_WALLET = "php/index.php"


    }

    object REQUEST_CODE {
        const val REQUEST_CODE_CAMERA_IMAGE = 101
        const val REQUEST_CODE_GALLERY_IMAGE = 102
        const val REQUEST_CODE_DOCUMENT = 103
        const val REQUEST_CODE_PERMISSION = 104
        const val LOCATION_REQUEST_CODE = 105
        const val REQUEST_CHECK_SETTINGS = 106
        const val REQUEST_CODE_OTHER_LOGIN_FLOW = 107
    }
    object SCREEN {
        var SELECTEDFEESPOSITION = -1

        var SELECTEDSCREEN = ""
        var ISCARD = "0"
        var subMenuDetailData: SubMenuDetailResponseModel.SubMenuDetailData? = null
        var subMenuListForTypeOne: SubMenuListForTypeOneResponseModel.SubMenuData?? = null

        const val WHATSON = "WHATSON"
        const val ENTERTAINMENT = "ENTERTAINMENT"
        const val PRAMOTION = "PRAMOTION"
        const val FUNCTION = "FUNCTION"
        const val EVENT = "EVENT"
        const val CONTACTUS = "CONTACTUS"
        const val MEMBERSHIP = "MEMBERSHIP"
        const val PICKABOX = "PICKABOX"
    }


    object DateFormat {
        const val yyyy_mm_dd = "yyyy-MM-dd" //2019-09-05
        const val yyyy_mm_ddTHHMM = "yyyy-MM-dd'T'hh:mm" //2019-09-05
        const val MM_dd_yyyy = "MM/dd/yyyy" // 11/16/1971
        const val MM_dd_yyyy_hh_mm_aa = "dd MMM yyyy, hh:mm aa" //16 Nov 1971, 11:25 PM
        const val hh_mm = "HH:mm" //11:25
        const val dd_mmm_yyyy= "dd MMM yyyy" //16 Nov 1971, 11:25 PM

        const val hh_mm_aa = "hh:mm aa" //11:25 PM
        const val yyyy_MM_dd_T_HH_mm_ss = "yyyy-MM-dd'T'HH:mm:ss"
        const val yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss"//1971-11-16T23:25:00.000Z
        const val dd_MM_yyyy = "dd MMM yyyy" // 16/11/1971
    }

    object RoleName {
        const val Dealer = "dealer"
        const val Distributor = "distributor"
        const val Customer = "customer"
    }

    object RoleId {
        const val Admin = "1.0"
        const val Distributor = "2.0"
        const val Dealer = "3.0"
        const val Customer = "4.0"
    }

    object OrderStatusId {
        const val New = 0
        const val Pending = 1
        const val Accepted = 2
        const val Completed = 3
        const val Forwarded = 4
        const val Canceled = 5
    }

    object Constant {
        const val DivisionID = "1.0"
        const val YearID = "1.0"
    }
    object IntentKeys {
        const val IK_MEMBER_ID = "memberID"
        const val YearID = "1.0"
    }
    object Global {
        var mMemberID: String? = ""
        var mMemberIDWithoutPrefix: String? = ""
        var OTP: String? = ""

        var mBarsMenuGlobal: ArrayList<BarsDetailResponseModel.BarsDetailData.Menu>? = ArrayList()

    }


    object SharedPreferences {
        const val PREF_KEY_IS_OTP_VERIFIED = "isOtpVerified"
        const val PREF_KEY_LOGIN_ID = "loginId"
        const val PREF_KEY_PASSWORD = "password"
        const val PREF_KEY_MEMBERSHIP_NUMBER = "membershipNumber"
        const val PREF_KEY_MEMBERSHIP_NUMBER_ORIGINAL = "membershipNumberOriginal"
        const val PREF_KEY_CARD_NUMBER = "membershipCardNumber"
        const val PREF_KEY_MEMBER_ID = "memberId"
        const val PREF_KEY_BADGE_NO = "badgeNo"
        const val PREF_KEY_FINANCE_TO = "financeTo"

        const val PREF_KEY_SURNAME = "surname"
        const val PREF_KEY_FIRST_NAME = "firstName"
        const val PREF_KEY_STATUS_POINTS = "statusPoints"
        const val PREF_KEY_TIER_LEVEL = "tierLevel"
        const val PREF_KEY_PREFERRED_NAME = "preferredName"
        const val PREF_KEY_MOBILE_NUMBER = "mobileNumber"
        const val PREF_KEY_PROFILE_IMG_URL = "profileImgUrl"
        const val PREF_KEY_POSTAL_CODE = "postalCode"
        const val PREF_KEY_EMAIL = "email"
        const val PREF_KEY_STATE_CODE = "stateCode"
        const val PREF_KEY_OCCUPATION = "occupation"
        const val PREF_KEY_ADDRESS = "address"
        const val PREF_KEY_ADDRESS_ONE = "address1"
        const val PREF_KEY_ADDRESS_ZERO = "address0"
        const val PREF_KEY_ADDRESS_TWO = "address2"
        const val PREF_KEY_SUBURB= "suburb"
    }

}
