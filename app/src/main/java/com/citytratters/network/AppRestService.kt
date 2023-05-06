package com.citytratters.network

import com.citytratters.model.response.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class AppRestService(
    private val appRestApiFast: AppRestApiFast
) {

        fun login(badgeNo: String?,membershipNo: String?
        ): Observable<Response<LoginResponseModel>> {
            val apiCall = appRestApiFast.login(badgeNo,membershipNo)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun addPrizes(
            device_id: String?,
            prize_id: String?,
            box_no: String?
        ): Observable<Response<ResponseSuccess>> {
            val apiCall = appRestApiFast.addPrizes(device_id,prize_id,box_no)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun getAllPrizes(): Observable<Response<ResponseGetAllPrice>> {
            val apiCall = appRestApiFast.getAllPrizes("demo")
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }


        fun registerToken(
            device_id: String?,
            device_token: String?,
            device_type: String?
        ): Observable<Response<ResponseSuccess>> {
            val apiCall = appRestApiFast.registerToken(device_id,device_token,device_type)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

    fun getMyPrizes(
        device_id: String?
    ): Observable<Response<ResponseMyPrize>> {
        val apiCall = appRestApiFast.getMyPrizes(device_id)
        return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }

    fun redeemPrizes(
        device_id: String?,
        id: String?
    ): Observable<Response<ResponseSuccess>> {
        val apiCall = appRestApiFast.redeemPrizes(device_id,id)
        return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }
    fun redeemVoucher(
        device_id: String?,
        id: String?,
        member_id: String?,
        date: String?
    ): Observable<Response<ResponseSuccess>> {
        val apiCall = appRestApiFast.redeemVoucher(device_id,id,member_id,date)
        return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }


        fun getBarsDetail(badgeNo: String?
        ): Observable<Response<BarsDetailResponseModel>> {
            val apiCall = appRestApiFast.getBarsDetail(badgeNo)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }


        fun getBoxes(
            device_id: String?
        ): Observable<Response<ResponseBoxPrice>> {
            val apiCall = appRestApiFast.getBoxes(device_id)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }
        fun moreDetail(tag: String?
        ): Observable<Response<GetMoreDetailResponseModel>> {
            val apiCall = appRestApiFast.moreDetail(tag)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun subMenuDetail(tag: String?
        ): Observable<Response<SubMenuDetailResponseModel>> {
            val apiCall = appRestApiFast.subMenuDetail(tag)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun subMenuList(tag: String?,type: String?,
        ): Observable<Response<SubMenuListResponseModel>> {
            val apiCall = appRestApiFast.subMenuList(tag,type)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun subMenuListForTypeOne(tag: String?,type: String?,
        ): Observable<Response<SubMenuListForTypeOneResponseModel>> {
            val apiCall = appRestApiFast.subMenuListForTypeOne(tag,type)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun updateUserDetail( mFieldMap: Map<String, String>
        ): Observable<Response<UpdateUserDetailResponseModel>> {
            val apiCall = appRestApiFast.updateUserDetail(mFieldMap)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun sendOTP(memberID: String?, phoneNumber: String?
        ): Observable<Response<SendOTPResponseModel>> {
            val apiCall = appRestApiFast.sendOTP(memberID,phoneNumber)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun verifyOTP(memberID: String?,mobile: String?, otp: String?
        ): Observable<Response<VerifyOTPResponseModel>> {
            val apiCall = appRestApiFast.verifyOTP(memberID,mobile,otp)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun barListing(memberID: String?
        ): Observable<Response<BarListResponseModel>> {
            val apiCall = appRestApiFast.barListing(memberID)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }


        fun offerListing(deviceId: String?,memberID: String?
        ): Observable<Response<OfferListResponseModel>> {
            if (memberID == ""){
                val apiCall = appRestApiFast.offerListing(deviceId)
                return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

            }else{
                val apiCall = appRestApiFast.offerListingWithMemberID(deviceId,memberID)
                return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

            }
        }



        fun feesListing(badgeNo: String?
        ): Observable<Response<MemberFeesResponseModel>> {
            val apiCall = appRestApiFast.feesListing(badgeNo)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun getCheckOutID(totalAmount:String,orderId:String
        ): Observable<Response<GetCheckOutIdResponseModel>> {
            val apiCall = appRestApiFast.getCheckOutID(totalAmount.toInt(),orderId)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

        fun addCard(  firstName:String,lastName:String,dob:String,email:String,address1:String,mobile:String,member_type:String,promoCode:String
        ): Observable<Response<ResponseSuccess>> {
            val apiCall = appRestApiFast.addCard(firstName,lastName,dob,email,address1,mobile,member_type,promoCode)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }



        fun saveWallet(member_no: String?,Name: String?, barcode: String?, membershipType: String?, status_points: String?
        ): Observable<Response<SaveWalletResponseModel>> {
            val apiCall = appRestApiFast.saveWallet(member_no,Name,barcode,membershipType,status_points)
            return apiCall.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
        }

}
