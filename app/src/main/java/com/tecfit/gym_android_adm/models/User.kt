package com.tecfit.gym_android_adm.models

data class User(
    val id_user:Int, val email:String, val password:String, val name:String, val lastname:String,
    val phone:String, val membership:Boolean, val image: File?
) {}