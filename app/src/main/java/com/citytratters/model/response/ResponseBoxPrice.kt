package com.citytratters.model.response

data class ResponseBoxPrice(
    val data: ArrayList<BoxData>,
    val message: String,
    val status: Int
) {
    data class BoxData(
        val description: String,
        val expiry_date: String,
        val id: Int,
        val image: String,
        val image_main: String,
        val is_open: Int,
        val max_users_per_day: Int,
        val no_of_users_won: Int,
        val title: String
    )
}