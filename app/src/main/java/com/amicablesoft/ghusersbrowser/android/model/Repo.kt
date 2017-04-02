package com.amicablesoft.ghusersbrowser.android.model

import com.google.gson.annotations.SerializedName

data class Repo(
    val id:Long,
    val name:String,
    @SerializedName("description") val desc:String?,
    @SerializedName("language") val lang:String?,
    @SerializedName("watchers_count") val seen:Int,
    @SerializedName("stargazers_count") val stars:Int,
    @SerializedName("forks_count") val fork:Int,
    @SerializedName("html_url") val htmlUrl:String
)