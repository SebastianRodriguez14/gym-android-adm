package com.tecfit.gym_android_adm.retrofit

import com.tecfit.gym_android_adm.models.Membership
import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.lang.reflect.Member


interface ApiService {

    @GET("user/all")
    fun getUsers():Call<List<User>>

    @GET("user/search/{email}")
    fun getUSer(@Path("email") email: String): Call<User>

    @GET("trainer/all")
    fun getTrainers(): Call<List<Trainer>>

    @POST("membership/saveWithUser")
    fun saveMembershipWithUser(@Body membership: Membership): Call<Membership>

}