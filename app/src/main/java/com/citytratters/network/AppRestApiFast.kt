package com.citytratters.network

import com.citytratters.constants.MyConfig
import com.citytratters.model.response.*
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*


interface AppRestApiFast {

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_LOGIN)
    fun login(
        @Field("badge_no") badgeNo: String?,
        @Field("member_no") membershipNo: String?,
    ): Observable<Response<LoginResponseModel>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.ADD_PRIZES)
    fun addPrizes(
        @Field("device_id") device_id: String?,
        @Field("prize_id") prize_id: String?,
        @Field("box_no") box_no: String?
    ): Observable<Response<ResponseSuccess>>


    @FormUrlEncoded

    @POST(MyConfig.Endpoints.GET_ALL_PRIZES)
    fun getAllPrizes(
        @Field("demo") city: String?
    ): Observable<Response<ResponseGetAllPrice>>


    @FormUrlEncoded
    @POST(MyConfig.Endpoints.REGISTER_TOKEN)
    fun registerToken(
        @Field("device_id") device_id: String?,
        @Field("device_token") device_token: String?,
        @Field("device_type") device_type: String?
    ): Observable<Response<ResponseSuccess>>


    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_BARS_DETAIL)
    fun getBarsDetail(
        @Field("bars_id") barsid: String?,
    ): Observable<Response<BarsDetailResponseModel>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.GET_MY_PRIZES)
    fun getMyPrizes(
        @Field("device_id") device_id: String?
    ): Observable<Response<ResponseMyPrize>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.REDEEM_PRIZES)
    fun redeemPrizes(
        @Field("device_id") device_id: String?,
        @Field("id") id: String?
    ): Observable<Response<ResponseSuccess>>


    @FormUrlEncoded
    @POST(MyConfig.Endpoints.REDEEM_VOUCHER)
    fun redeemVoucher(
        @Field("device_id") device_id: String?,
        @Field("voucher_id") id: String?,
        @Field("member_id") member_id: String?,
        @Field("redeemed_date") redeemed_date: String?
    ): Observable<Response<ResponseSuccess>>


    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_GET_BOXES)
    fun getBoxes(
        @Field("device_id") device_id: String?
    ): Observable<Response<ResponseBoxPrice>>


    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_GET_MORE_DETAIL)
    fun moreDetail(
        @Field("tag") tag: String?,
    ): Observable<Response<GetMoreDetailResponseModel>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_SUB_MENU_DETAIL)
    fun subMenuDetail(
        @Field("tag") tag: String?,
    ): Observable<Response<SubMenuDetailResponseModel>>


    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_SUB_MENU_LIST)
    fun subMenuList(
        @Field("tag") tag: String?,
        @Field("type") type: String?,
    ): Observable<Response<SubMenuListResponseModel>>


    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_SUB_MENU_LIST)
    fun subMenuListForTypeOne(
        @Field("tag") tag: String?,
        @Field("type") type: String?,
    ): Observable<Response<SubMenuListForTypeOneResponseModel>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_UPDATE_USER_DETAIL)
    fun updateUserDetail(
        @FieldMap mFieldMap: Map<String, String>
    ): Observable<Response<UpdateUserDetailResponseModel>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_SEND_OTP)
    fun sendOTP(
        @Field("badge_no") badge_no: String?,
        @Field("mobile") mobile: String?
    ): Observable<Response<SendOTPResponseModel>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_VERIFY_OTP)
    fun verifyOTP(
        @Field("badge_no") badge_no: String?,
        @Field("mobile") mobile: String?,
        @Field("otp") code: String?
    ): Observable<Response<VerifyOTPResponseModel>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_BAR_LISTING)
    fun barListing(
        @Field("badge_no") badge_no: String?,
    ): Observable<Response<BarListResponseModel>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_OFFER_LISTING)
    fun offerListing(
        @Field("device_id") badge_no: String?,
    ): Observable<Response<OfferListResponseModel>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_OFFER_LISTING)
    fun offerListingWithMemberID(
        @Field("device_id") badge_no: String?,
        @Field("member_id") member_id: String?,
    ): Observable<Response<OfferListResponseModel>>

    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_FEES_LISTING)
    fun feesListing(
        @Field("badge_no") badge_no: String?,
    ): Observable<Response<MemberFeesResponseModel>>


    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_GET_CHECKOUT_ID)
    fun getCheckOutID(
        @Field("amount") amount: Int?,
        @Field("order_id") order_id: String?,
    ): Observable<Response<GetCheckOutIdResponseModel>>



    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_ADD_CARD)
    fun addCard(
        @Field("FirstName") firstName: String?,
        @Field("LastName") lastName: String?,
        @Field("dob") dob: String?,
        @Field("email") email: String?,
        @Field("address1") address1: String?,
        @Field("mobile") mobile: String?,
        @Field("member_type") member_type: String?,
        @Field("promo_code") promoCode: String?,
    ): Observable<Response<ResponseSuccess>>


    @FormUrlEncoded
    @POST(MyConfig.Endpoints.API_SAVE_WALLET)
    fun saveWallet(
        @Field("member_no") member_no: String?,
        @Field("Name") Name: String?,
        @Field("barcode") barcode: String?,
        @Field("membershipType") membershipType: String?,
        @Field("status_points") status_points: String?,
    ): Observable<Response<SaveWalletResponseModel>>


}