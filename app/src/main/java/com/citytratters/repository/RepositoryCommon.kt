package com.citytratters.repository

import com.citytratters.base.BaseRepository
import com.citytratters.callbacks.APIResponseCallback
import com.citytratters.model.response.*

class RepositoryCommon : BaseRepository() {


    fun login(
        badgeNo: String?,
        membershipNo: String?,
        callback: APIResponseCallback<LoginResponseModel>
    ) {
        sendApiCall(appRestService.login(badgeNo,membershipNo), callback)
    }


    fun addPrizes(
        device_id: String?,
        prize_id: String?,
        box_no: String?,
        callback: APIResponseCallback<ResponseSuccess>
    ) {
        sendApiCall(appRestService.addPrizes(device_id,prize_id,box_no), callback)
    }


    fun getAllPrizes(
        callback: APIResponseCallback<ResponseGetAllPrice>
    ) {
        sendApiCall(appRestService.getAllPrizes(), callback)
    }

    fun getMyPrizes(
        device_id: String?,
        callback: APIResponseCallback<ResponseMyPrize>
    ) {
        sendApiCall(appRestService.getMyPrizes(device_id), callback)
    }
    fun redeemPrizes(
        device_id: String?,
        id: String?,
        callback: APIResponseCallback<ResponseSuccess>
    ) {
        sendApiCall(appRestService.redeemPrizes(device_id,id), callback)
    }

    fun redeemVoucher(
        device_id: String?,
        id: String?,
        member_id: String?,
        date: String?,
        callback: APIResponseCallback<ResponseSuccess>
    ) {
        sendApiCall(appRestService.redeemVoucher(device_id,id,member_id,date), callback)
    }


    fun registerToken(
        device_id: String?,
        device_token: String?,
        device_type: String?,
        callback: APIResponseCallback<ResponseSuccess>
    ) {
        sendApiCall(appRestService.registerToken(device_id,device_token,device_type), callback)
    }

    fun getBarsDetail(
        badgeNo: String?,
        callback: APIResponseCallback<BarsDetailResponseModel>
    ) {
        sendApiCall(appRestService.getBarsDetail(badgeNo), callback)
    }

    fun getBoxes(
        device_id: String?,
        callback: APIResponseCallback<ResponseBoxPrice>
    ) {
        sendApiCall(appRestService.getBoxes(device_id), callback)
    }


    fun moreDetail(
        tag: String?,
        callback: APIResponseCallback<GetMoreDetailResponseModel>
    ) {
        sendApiCall(appRestService.moreDetail(tag), callback)
    }
    fun subMenuDetail(
        tag: String?,
        callback: APIResponseCallback<SubMenuDetailResponseModel>
    ) {
        sendApiCall(appRestService.subMenuDetail(tag), callback)
    }

    fun subMenuList(
        tag: String?,
        type:String?,
        callback: APIResponseCallback<SubMenuListResponseModel>
    ) {
        sendApiCall(appRestService.subMenuList(tag,type), callback)
    }
    fun subMenuListForTypeOne(
        tag: String?,
        type:String?,
        callback: APIResponseCallback<SubMenuListForTypeOneResponseModel>
    ) {
        sendApiCall(appRestService.subMenuListForTypeOne(tag,type), callback)
    }

    fun updateUserDetail(
        mFieldMap: Map<String, String>,
        callback: APIResponseCallback<UpdateUserDetailResponseModel>
    ) {
        sendApiCall(appRestService.updateUserDetail(mFieldMap), callback)
    }

    fun sendOTP(
        memberID: String?, phoneNumber: String?,
        callback: APIResponseCallback<SendOTPResponseModel>
    ) {
        sendApiCall(appRestService.sendOTP(memberID,phoneNumber), callback)
    }

    fun verifyOTP(
        memberID: String?, mobile: String?, otp: String?,
        callback: APIResponseCallback<VerifyOTPResponseModel>
    ) {
        sendApiCall(appRestService.verifyOTP(memberID,mobile,otp), callback)
    }


    fun barListing(
        memberID: String?,
        callback: APIResponseCallback<BarListResponseModel>
    ) {
        sendApiCall(appRestService.barListing(memberID), callback)
    }

    fun offerListing(
        device_id:  String?,
        memberID: String?,
        callback: APIResponseCallback<OfferListResponseModel>
    ) {
        sendApiCall(appRestService.offerListing(device_id,memberID), callback)
    }

    fun feesListing(
        badgeNo: String?,
        callback: APIResponseCallback<MemberFeesResponseModel>
    ) {
        sendApiCall(appRestService.feesListing(badgeNo), callback)
    }


    fun getCheckOutID(
        totalAmount:String,orderId:String,
        callback: APIResponseCallback<GetCheckOutIdResponseModel>
    ) {
        sendApiCall(appRestService.getCheckOutID(totalAmount,orderId), callback)
    }

    fun addCard(
        firstName:String,lastName:String,dob:String,email:String,address1:String,mobile:String,member_type:String,promoCode:String,
        callback: APIResponseCallback<ResponseSuccess>
    ) {
        sendApiCall(appRestService.addCard(firstName,lastName,dob,email,address1,mobile,member_type,promoCode), callback)
    }

    fun saveWallet(
        member_no: String?, Name: String?, barcode: String?, membershipType: String?, status_points: String?,
        callback: APIResponseCallback<SaveWalletResponseModel>
    ) {
        sendApiCall(appRestService.saveWallet(member_no,Name,barcode,membershipType,status_points), callback)
    }
}