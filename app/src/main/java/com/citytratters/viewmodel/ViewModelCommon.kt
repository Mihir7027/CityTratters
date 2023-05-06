package com.citytratters.viewmodel

import androidx.lifecycle.MutableLiveData
import com.citytratters.base.BaseViewModel
import com.citytratters.callbacks.APIResponseCallback
import com.citytratters.model.response.*
import com.citytratters.network.RestResponse
import com.citytratters.repository.RepositoryCommon

class ViewModelCommon(repository: RepositoryCommon) :
    BaseViewModel<RepositoryCommon>(repository) {

    var eventLogin = MutableLiveData<RestResponse<LoginResponseModel>>()
    var eventGetMoreDetail = MutableLiveData<RestResponse<GetMoreDetailResponseModel>>()
    var eventGetSubMenuDetail = MutableLiveData<RestResponse<SubMenuDetailResponseModel>>()
    var eventGetSubMenuList = MutableLiveData<RestResponse<SubMenuListResponseModel>>()
    var eventGetSubMenuListForTypeOne = MutableLiveData<RestResponse<SubMenuListForTypeOneResponseModel>>()
    var eventSendOTP = MutableLiveData<RestResponse<SendOTPResponseModel>>()
    var eventVerifyOTP = MutableLiveData<RestResponse<VerifyOTPResponseModel>>()
    var eventBarListing = MutableLiveData<RestResponse<BarListResponseModel>>()
    var eventOfferListing = MutableLiveData<RestResponse<OfferListResponseModel>>()
    var eventFeesListing = MutableLiveData<RestResponse<MemberFeesResponseModel>>()
    var eventSaveWallet = MutableLiveData<RestResponse<SaveWalletResponseModel>>()
    var eventUpdateUserDetail = MutableLiveData<RestResponse<UpdateUserDetailResponseModel>>()
    var eventGetBarsDetail = MutableLiveData<RestResponse<BarsDetailResponseModel>>()
    var eventGetBoxes = MutableLiveData<RestResponse<ResponseBoxPrice>>()
    var eventGetAllPrizes = MutableLiveData<RestResponse<ResponseGetAllPrice>>()
    var eventGetMyPrizes = MutableLiveData<RestResponse<ResponseMyPrize>>()
    var eventRedeemPrizes = MutableLiveData<RestResponse<ResponseSuccess>>()
    var eventAddCard = MutableLiveData<RestResponse<ResponseSuccess>>()
    var eventRedeemVoucher = MutableLiveData<RestResponse<ResponseSuccess>>()

    var eventRegisterToken = MutableLiveData<RestResponse<ResponseSuccess>>()
    var eventAddPrizes = MutableLiveData<RestResponse<ResponseSuccess>>()

    //Payment
    var eventGetCheckOutId = MutableLiveData<RestResponse<GetCheckOutIdResponseModel>>()


    fun apiLogin( badgeNo: String?,membershipNo:String) {

        eventLogin.value = RestResponse()
        repository.login(badgeNo,membershipNo,
            APIResponseCallback {
                eventLogin.value = it
            })
    }

    fun apiAddPrizes( device_id: String?,prize_id: String?,box_no: String?) {
        eventAddPrizes.value = RestResponse()
        repository.addPrizes(device_id,prize_id,box_no,
            APIResponseCallback {
                eventAddPrizes.value = it
            })
    }

    fun apiGetAllPrizes() {
        eventGetAllPrizes.value = RestResponse()
        repository.getAllPrizes(
            APIResponseCallback {
                eventGetAllPrizes.value = it
            })
    }


    fun apiGetMyPrizes( device_id: String?) {
        eventGetMyPrizes.value = RestResponse()
        repository.getMyPrizes(device_id,
            APIResponseCallback {
                eventGetMyPrizes.value = it
            })
    }

    fun apiRedeemPrizes( device_id: String?,id: String?) {

        eventRedeemPrizes.value = RestResponse()

        repository.redeemPrizes( device_id,id,
            APIResponseCallback {

                eventRedeemPrizes.value = it

            })
    }
    fun apiRedeemVoucher( device_id: String?,id: String?,member_id: String?,
                          date: String?) {

        eventRedeemVoucher.value = RestResponse()

        repository.redeemVoucher( device_id,id,member_id,date,
            APIResponseCallback {

                eventRedeemVoucher.value = it

            })
    }

    fun apiRegisterToken( device_id: String?,device_token: String?,device_type: String?) {

        eventRegisterToken.value = RestResponse()

        repository.registerToken(device_id, device_token,device_type,
            APIResponseCallback {

                eventRegisterToken.value = it

            })
    }

    fun apiGetBoxes(device_id: String?) {

        eventGetBoxes.value = RestResponse()

        repository.getBoxes( device_id,
            APIResponseCallback {
                eventGetBoxes.value = it

            })
    }


    fun apiGetBarsDetail( badgeNo: String?) {

        eventGetBarsDetail.value = RestResponse()
        repository.getBarsDetail(badgeNo,
            APIResponseCallback {
                eventGetBarsDetail.value = it
            })
    }

    fun apiMoreDetail(tag: String?) {

        eventGetMoreDetail.value = RestResponse()
        repository.moreDetail(tag,
            APIResponseCallback {
                eventGetMoreDetail.value = it
            })
    }
    fun apiSubMenuDetail(tag: String?) {

        eventGetSubMenuDetail.value = RestResponse()
        repository.subMenuDetail(tag,
            APIResponseCallback {
                eventGetSubMenuDetail.value = it
            })
    }

    fun apiSubMenuList(tag: String?, type:String?) {

        eventGetSubMenuList.value = RestResponse()
        repository.subMenuList(tag,type,
            APIResponseCallback {
            eventGetSubMenuList.value = it
            })
    }

    fun eventGetSubMenuListForTypeOne(tag: String?, type:String?) {

        eventGetSubMenuListForTypeOne.value = RestResponse()
        repository.subMenuListForTypeOne(tag,type,
            APIResponseCallback {
            eventGetSubMenuListForTypeOne.value = it
            })
    }

    fun apiUpdateUserDetail(mFieldMap: Map<String, String>) {

        eventUpdateUserDetail.value = RestResponse()

        repository.updateUserDetail(mFieldMap,
            APIResponseCallback {
                eventUpdateUserDetail.value = it
            })
    }

    fun apiSendOTP(memberID: String?, phoneNumber: String?) {

        eventSendOTP.value = RestResponse()

        repository.sendOTP(memberID,phoneNumber,
            APIResponseCallback {
                eventSendOTP.value = it
            })
    }

    fun apiVerifyOTP(memberID: String?,mobile: String?, otp: String?) {

        eventVerifyOTP.value = RestResponse()

        repository.verifyOTP(memberID,mobile,otp,
            APIResponseCallback {
                eventVerifyOTP.value = it
            })
    }

    fun barListing(memberID: String?) {

        eventBarListing.value = RestResponse()

        repository.barListing(memberID,
            APIResponseCallback {
                eventBarListing.value = it
            })
    }

    fun offerListing(deviceid: String?,memberID: String?) {

        eventOfferListing.value = RestResponse()

        repository.offerListing(deviceid,memberID,
            APIResponseCallback {
                eventOfferListing.value = it
            })
    }

    fun feesListing(badgeNo: String?) {
        eventFeesListing.value = RestResponse()
        repository.feesListing(badgeNo,
            APIResponseCallback {
                eventFeesListing.value = it
            })
    }


    fun getCheckOutID(totalAmount:String,orderId:String) {
        eventGetCheckOutId.value = RestResponse()
        repository.getCheckOutID(totalAmount,orderId,
            APIResponseCallback {
            eventGetCheckOutId.value = it
            })
    }


    fun addCard(firstName:String,lastName:String,dob:String,email:String,address1:String,mobile:String,member_type:String,promoCode:String) {
        eventAddCard.value = RestResponse()
        repository.addCard(firstName,lastName,dob,email,address1,mobile,member_type,promoCode,
            APIResponseCallback {
                eventAddCard.value = it
            })
    }

    fun apiSaveWallet(member_no: String?, Name: String?, barcode: String?, membershipType: String?, status_points: String?) {
        eventSaveWallet.value = RestResponse()
        repository.saveWallet(member_no,Name,barcode,membershipType,status_points,
            APIResponseCallback {
                eventSaveWallet.value = it
            })
    }

}