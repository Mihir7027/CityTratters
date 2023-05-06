package com.citytratters.model.response

data class ResponseGetAllPrice(
    val data: ArrayList<AllPriceData>,
    val message: String,
    val status: Int
) {
    data class AllPriceData(
        val description: String,
        val id: String,
        val image: String,
        val image_main: String,
        val qr_image: String,
        val title: String
    )
}