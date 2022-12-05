package com.tecfit.gym_android_adm.models.custom

import com.tecfit.gym_android_adm.models.User
import java.util.*

data class UserToFinish (val user: User, val id_membership:Int, var start_date:Date, var expiration_date:Date, val payment:Double ) {
}