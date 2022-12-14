package com.tecfit.gym_android_adm.retrofit

import com.tecfit.gym_android_adm.models.*
import com.tecfit.gym_android_adm.models.custom.ExpiryDate
import com.tecfit.gym_android_adm.models.custom.MembershipCustom
import com.tecfit.gym_android_adm.models.custom.MembershipRegister
import com.tecfit.gym_android_adm.models.custom.UserToFinish
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("user/all")
    fun getUsers():Call<List<User>>

    @GET("product/all")
    fun getProducts(): Call<List<Product>>

    @GET("user/search/{email}")
    fun getUSer(@Path("email") email: String): Call<User>

    @GET("trainer/all")
    fun getTrainers(): Call<List<Trainer>>

    @POST("membership/saveWithUser")
    fun saveMembershipWithUser(@Body membership: MembershipRegister): Call<Membership>

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

    @DELETE("trainer/delete/{id}")
    fun deleteTrainer(@Path("id") id: Int):Call<Void>

    @POST("product/post")
    fun postProduct(@Body product: Product):Call<Product>

    @PUT("product/put/{idProduct}")
    fun putProduct(@Body product: Product, @Path("idProduct") idProduct:Int):Call<Product>

    @POST("membership/save/{id}")
    fun postMembership(@Path("id") id: Int, @Body membership: MembershipCustom):Call<Membership>

    @GET("membership/check/{id_user}")
    fun getActiveMembershipByUser(@Path("id_user") id_user: Int): Call<Membership>

    @DELETE("product/delete/{id}")
    fun deleteProduct(@Path("id")id:Int):Call<Void>

    @GET("membership/toFinish")
    fun getUsersToFinishMembership():Call<List<UserToFinish>>

    @PUT("membership/update/{id_membership}")
    fun putExtendMembership(@Body expiryDate: ExpiryDate, @Path("id_membership") id_membership:Int):Call<Void>

}