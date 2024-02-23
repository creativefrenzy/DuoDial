package com.privatepe.app.model

import com.privatepe.app.response.TopReceiver.ProfileImage

class MyFollowerUserData(
    var id: Int,
    var profile_id: Int,
    var name: String?,
    var level: Int,
    var is_online: Int,
    val gender:String,
    var favorite_by_you_count: Int,
    var profile_images: ArrayList<ProfileImage>?
)