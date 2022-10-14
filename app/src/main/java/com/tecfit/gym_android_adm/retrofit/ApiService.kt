package com.tecfit.gym_android_adm.retrofit

import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.models.User
import retrofit2.http.GET
import retrofit2.Call


interface ApiService {

    @GET("user/all")
    fun getUsers():Call<List<User>>

    @GET("trainer/all")
    fun getTrainers(): Call<List<Trainer>>

}