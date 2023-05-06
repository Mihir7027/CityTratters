package com.citytratters.model.response

data class MemberFeesResponseModel(
    val data: ArrayList<MemberFeesData>,
    val message: String,
    val status: Int
) {
    data class  MemberFeesData(
        val amountDue: Double,
        val amountGST: Double,
        val type: String,
        val curPaidTill: String,
        val expiryDate: String,
        val feeId: Int,
        val feeType: Int,
        var isSelected: Boolean = false,
    )
}