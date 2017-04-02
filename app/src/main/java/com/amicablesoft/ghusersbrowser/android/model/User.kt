package com.amicablesoft.ghusersbrowser.android.model

import com.google.gson.annotations.SerializedName

/**
 * Created by bogdan on 4/1/17.
 */
data class UserShort(
    val id:Long,
    val login:String,
    @SerializedName("avatar_url") val avatarUrl:String
)

data class User(
    val id:Long,
    val login:String,
    val name:String?,
    @SerializedName("avatar_url") val avatarUrl:String,
    val company:String?,
    val location:String?,
    val bio:String?,
    val email:String?,
    val blog:String?,
    val following:Int,
    @SerializedName("public_repos") val publicRepos:Int,
    var followers:Int
)
