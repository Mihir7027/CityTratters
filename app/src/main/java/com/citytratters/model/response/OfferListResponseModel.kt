package com.citytratters.model.response

data class OfferListResponseModel(
    val data: ArrayList<OfferListData>,
    val message: String,
    val status: Int
) {
    data class OfferListData(
        val category: String,
        val category_icon: String,
        val category_id: String,
        val conditions: String,
        val description: String,
        val end_time: String,
        val facility: String,
        val redeemed_date: String,
        val id: String,
        val image: String,
        val end_day: String,
        val is_locked: Int,
        val is_redeemed: Int,
        val qr_code: String,
        val redeemable_days: String,
        val redemption_type: String,
        val start_time: String,
        val title: String,
        val validity: String,
        val weekday: String
    )
}