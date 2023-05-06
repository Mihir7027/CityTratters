package com.citytratters.model.response

data class LoginResponseModel(
    val  data: LoginResponseData,
    val message: String,
    val status: Int
) {
    data class LoginResponseData(
        val BirthDate: String,
        val DOB: String,
        val FinancialTo: String,
        val Gender: String,
        val email: String,
        val first_name: String,
        val hasphoto: Hasphoto,
        val address: String,
        val image: String,
        val member_id: String,
        val membership_number: String,
        val mobile: String,
        val occupation: String,
        val preferred_name: String,
        val status_points: Double,
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