package com.citytratters.model.response

data class BarListResponseModel(
    val data: ArrayList<BarData>,
    val message: String,
    val status: Int
) {
    data class BarData(
        val bar_bg_image: String,
        val bars_description: String,
        val bars_icon_photo: String,
        val bars_id: String,
        val bars_title: String,
        val booking_url: String,
        val contact: String,
        val created_by: String,
        val created_date: String,
        val dinner_timing: String,
        val disclaimer: String,
        val email: String,
        val face_link: String,
        val inst_link: String,
        val lunch_timing: String,
        val modified_by: String,
        val modified_date: String,
        val other_timing: String,
        val sort: String,
        val status: String
    ) {
        data class BarBgImage(
            val bars_bg_image: String,
            val bars_bg_image_id: String,
            val bars_id: String,
            val created_by: String,
            val created_date: String,
            val modified_by: String,
            val modified_date: String,
            val status: String
        )
    }
}