package com.citytratters.model.response


data class ResponseMyPrize(
    val status: Int,
    val message: String,
    val data: ArrayList<MyPrizeData>
){
    data class MyPrizeData(
        val id : Int,
        val title : String,
        val image : String,
        val image_main : String,
        val description : String,
        val expiry_date : String,
        val max_users_per_day : Int,
        val no_of_users_won : String,
        val is_open : Int,
        val won_date : String,
        val won_timestamp : Long,
        val redeem_hours : Int,
        val is_redeemed : Int,
        val qr_image : String,
        var couponNotAvailable : Int
    )
}