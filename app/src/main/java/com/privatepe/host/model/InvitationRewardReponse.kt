package com.privatepe.host.model

import com.google.gson.annotations.SerializedName

data class InvitationRewardReponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("result")
    val result: Result,
    @SerializedName("success")
    val success: Boolean
) {
    data class Result(
        @SerializedName("commissionlist")
        val commissionlist: List<Commissionlist>,
        @SerializedName("current_page")
        val currentPage: Int?,
        @SerializedName("data")
        val `data`: List<Data>,
        @SerializedName("first_page_url")
        val firstPageUrl: String?,
        @SerializedName("from")
        val from: Int?,
        @SerializedName("last_page")
        val lastPage: Int,
        @SerializedName("last_page_url")
        val lastPageUrl: String?,
        @SerializedName("my_invitation_reward_amount")
        val myInvitationRewardAmount: Int,
        @SerializedName("cards")
        val cards: Int,
        @SerializedName("next_page_url")
        val nextPageUrl: Any,
        @SerializedName("path")
        val path: String?,
        @SerializedName("per_page")
        val perPage: Int?,
        @SerializedName("prev_page_url")
        val prevPageUrl: Any,
        @SerializedName("to")
        val to: Int?,
        @SerializedName("total")
        val total: Int?
    ) {
        data class Commissionlist(
            @SerializedName("coin")
            val coin: Int?,
            @SerializedName("commission_ratio")
            val commissionRatio: Int?
        )

        data class Data(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("monthly_referal_earn")
            val monthlyReferalEarn: Int?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("profile_id")
            val profileId: Int?,
            @SerializedName("profile_images_default")
            val profileImagesDefault: ProfileImagesDefault?,
            @SerializedName("rich_level")
            val richLevel: Int
        ) {
            data class ProfileImagesDefault(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("image_name")
                val imageName: String?,
                @SerializedName("user_id")
                val userId: Int?
            )
        }
    }
}