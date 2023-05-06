package com.citytratters.model.response

data class BarsDetailResponseModel(
    val data: BarsDetailData,
    val message: String,
    val status: Int
) {
    data class BarsDetailData(
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
        val gallery: ArrayList<Gallery>,
        val inst_link: String,
        val lunch_timing: String,
        val menu: ArrayList<Menu>,
        val modified_by: String,
        val modified_date: String,
        val other_timing: String,
        val sort: String,
        val status: String
    ) {
        data class Gallery(
            val bars_id: String,
            val bars_image_id: String,
            val bars_photo: String,
            val created_by: String,
            val created_date: String,
            val modified_by: String,
            val modified_date: String,
            val status: String
        )

        data class Menu(
            val bars_id: String,
            val bars_menu_description: String,
            val bars_menu_id: String,
            val bars_menu_type: String,
            val bars_url: String,
            val bars_url_name: String,
            val created_by: String,
            val created_date: String,
            val modified_by: String,
            val modified_date: String,
            val status: String
        )
    }
}