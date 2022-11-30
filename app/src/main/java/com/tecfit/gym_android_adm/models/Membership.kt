package com.tecfit.gym_android_adm.models

import java.util.*

data class Membership(
     val id_membership:Int, var start_date: Date, var expiration_date: Date, val payment:Double
)