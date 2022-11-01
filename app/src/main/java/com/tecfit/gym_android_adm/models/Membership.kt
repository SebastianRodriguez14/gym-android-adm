package com.tecfit.gym_android_adm.models

data class Membership(
     val start_date: String, val expiration_date:String, var payment:Double, val user: UserCustom
)