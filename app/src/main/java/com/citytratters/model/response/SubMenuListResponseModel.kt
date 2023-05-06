package com.citytratters.model.response

data class SubMenuListResponseModel(
    val data: ArrayList<SubMenuData>,
    val message: String,
    val status: Int
) {
    data class SubMenuData(
        val id: String,
        val slug : String,
        val cat_id : String,
        val template: String,
        val sub_template: String,
        val title: String,
        val sub_title: String,
        val artist: String,
        val photo: String,
        val door_open: String,
        val buy: String,
        val description: String,
        val latitude: Double,
        val longitude: Double,
        val phone: String,
        val address: String,
        val postal_address: String,
        val video: String,
        val heading: String,
        val link: String,
        val status: String,
        val url: String,
        val email: String,
        val show_time: String,
        val thumbnail: String,
        val tag: String,
        val created_date: String,
        val modified_date: String
    )
}