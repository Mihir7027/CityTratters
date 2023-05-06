package com.citytratters.model.response

data class SubMenuDetailResponseModel(
    val data: ArrayList<SubMenuDetailData>,
    val message: String,
    val status: Int
) {
    data class SubMenuDetailData(
        val address: String,
        val artist: String,
        val buy: String,
        val cat_id: String,
        val created_date: String,
        val description: String,
        val door_open: String,
        val email: String,
        val heading: String,
        val id: String,
        val latitude: String,
        val link: String,
        val is_url: String,
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
        val url: String,
        val video: String
    )
}