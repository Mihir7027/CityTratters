package com.citytratters.model.response

data class GetCheckOutIdResponseModel(
    val data: CheckOutData,
    val message: String,
    val status: Int
) {
    data class CheckOutData(
        val checkout_id: String
    )
}