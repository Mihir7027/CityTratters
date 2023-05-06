package com.citytratters.model.response

data class VerifyOTPResponseModel(
    val data: ArrayList<OTPResponseData>,
    val message: String,
    val status: Int
) {
    data class OTPResponseData(
        val address: String,
        val first_name: String,
        val hasphoto: Hasphoto,
        val membership_number: String,
        val mobile: String,
        val occupation: String,
        val preferred_name: String,
        val status_points: String,
        val surname: String,
        val tier_level: String
    ) {
        data class Hasphoto(
            val AccountType: Int,
            val BadgeNo: Int,
            val BirthDate: String,
            val BonusPoints: Int,
            val FinancialTo: String,
            val FirstName: String,
            val Gender: String,
            val HasPhoto: Boolean,
            val LastName: String,
            val Level: Int,
            val MemberId: Int,
            val Nationality: String,
            val NextOfKin: String,
            val NextOfKinHome: String,
            val NextOfKinWork: String,
            val Occupation: String,
            val PhotoLastUpdated: String,
            val PreferredName: String,
            val Proposer: String,
            val RateDescription: String,
            val RateNo: Int,
            val SMSOptIn: Boolean,
            val Seconder: String,
            val Status: Int,
            val Title: String
        )
    }
}