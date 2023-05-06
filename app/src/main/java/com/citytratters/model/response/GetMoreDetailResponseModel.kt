package com.citytratters.model.response

data class GetMoreDetailResponseModel(
    val data: ArrayList<MoreDetailData>,
    val message: String,
    val status: Int
) {
    data class MoreDetailData(
        val cms_id: String,
        val cms_perent: String,
        val cms_photo: String,
        val cms_slug: String,
        val cms_status: String,
        val cms_sub_menu_template: String,
        val cms_tag: String,
        val cms_template: String,
        val cms_title: String,
        val created_date: String,
        val modified_date: String
    )
}