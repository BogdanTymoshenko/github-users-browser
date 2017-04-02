package com.amicablesoft.ghusersbrowser.android.api.dto

import com.amicablesoft.ghusersbrowser.android.model.UserShort
import com.google.gson.annotations.SerializedName

data class UserSearchResultDto(
    @SerializedName("items") val users:List<UserShort>
)