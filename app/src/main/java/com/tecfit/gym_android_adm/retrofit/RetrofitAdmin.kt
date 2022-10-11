package com.tecfit.gym_android_adm.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitAdmin {

    companion object {
        private val BASE_URL = "https://api-tecfit.herokuapp.com/tec_fit/"

        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }
}