package com.tecfit.gym_android_adm.models.custom

import com.tecfit.gym_android_adm.models.UserCustom

data class MembershipRegister(val id_membership:Int, var start_date: String, var expiration_date: String, val payment:Double, val user: UserCustom)
