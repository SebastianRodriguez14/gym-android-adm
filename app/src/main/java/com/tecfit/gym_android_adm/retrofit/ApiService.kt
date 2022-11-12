package com.tecfit.gym_android_adm.retrofit

import com.tecfit.gym_android_adm.models.File
import com.tecfit.gym_android_adm.models.Membership
import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.lang.reflect.Member


interface ApiService {

    @GET("user/all")
    fun getUsers():Call<List<User>>

    @GET("trainer/all")
    fun getTrainers(): Call<List<Trainer>>

    @POST("membership/saveWithUser")
    fun saveMembershipWithUser(@Body membership: Membership): Call<Membership>

    @Multipart
    @PUT("file/put")
    fun updateFile(@Part multipartFile:MultipartBody.Part, @Part("idFile") idFile:RequestBody):Call<File>

    @Multipart
    @POST("file/post")
    fun postFile(@Part multipartFile:MultipartBody.Part):Call<File>

    @POST("trainer/post")
    fun postTrainer(@Body trainer:Trainer):Call<Trainer>

    @PUT("trainer/put/{idTrainer}")
    fun putTrainer(@Body trainer:Trainer, @Path("idTrainer") idTrainer:Int):Call<Trainer>

}