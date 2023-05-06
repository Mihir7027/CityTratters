package com.citytratters.model.response

data class SubMenuListForTypeOneResponseModel(
    val data: ArrayList<SubMenuData>,
    val message: String,
    val status: Int
) {
    data class SubMenuData(
        val address: String,
        val artist: String,
        val buy: String,
        val cat_id: String,
        val created_date: String,
        val door_open: String,
        val email: String,
        val heading: String,
        val id: String,
        val is_url: String,
        val latitude: String,
        val longitude: String,
        val modified_date: String,
        val phone: String,
        val photo: String,
        val postal_address: String,
        val show_time: String,
        val slug: String,
        val status: String,
        val sub_title: String,
        val template: String,
        val thumbnail: String,
        val title: String,
        val link: String,
        val url: String,
        val description: String,
        val video: String
    )
}