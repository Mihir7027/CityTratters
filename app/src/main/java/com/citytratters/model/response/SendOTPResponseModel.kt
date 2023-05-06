package com.citytratters.model.response

data class SendOTPResponseModel(
    val data: OTPResponseData,
    val message: String,
    val status: Int
) {
    data class OTPResponseData(
        val verification_code: String,
        val phone: String
    )
}