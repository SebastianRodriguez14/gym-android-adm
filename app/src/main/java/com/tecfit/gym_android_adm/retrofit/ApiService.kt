package com.tecfit.gym_android_adm.retrofit

import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("user/all")
    fun getUsers():Call<List<User>>

    @GET("trainer/all")
    fun getTrainers(): Call<List<Trainer>>


    @POST("trainer/post")
    fun postTrainer(@Body trainer: Trainer):Call<Trainer>

//    @Multipart
//    @POST("trainer/save")
//    fun postTrainer(@Part("name") name:String,@Part("lastname") lastname:String,@Part("description") description:String, @Part file: MultipartBody.Part):Call<Trainer>

}