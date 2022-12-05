package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.databinding.FragmentListUserExpiryBinding
import com.tecfit.gym_android_adm.fragments.adapter.UserExpiryAdapter
import com.tecfit.gym_android_adm.models.User
import com.tecfit.gym_android_adm.models.custom.ArraysForClass
import com.tecfit.gym_android_adm.models.custom.UserToFinish
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListUserExpiryFragment : Fragment() {

    lateinit var binding: FragmentListUserExpiryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListUserExpiryBinding.inflate(layoutInflater)
        binding.recyclerviewUsersExpiry.layoutManager = LinearLayoutManager(context)
        apiGetUsersExpiry()



        return binding.root
    }

    private fun initRecyclerView(userToFinishList:List<UserToFinish>){
        binding.recyclerviewUsersExpiry.adapter = UserExpiryAdapter(userToFinishList, fragmentManager)
    }

    private fun apiGetUsersExpiry(){
        val apiService: ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultUsersExpiry: Call<List<UserToFinish>> = apiService.getUsersToFinishMembership()

        resultUsersExpiry.enqueue(object : Callback<List<UserToFinish>> {
            override fun onResponse(call: Call<List<UserToFinish>>, response: Response<List<UserToFinish>>) {
                val listUsersExpiry=response.body()
                if(listUsersExpiry!=null){
                    initRecyclerView(listUsersExpiry)
                }
            }

            override fun onFailure(call: Call<List<UserToFinish>>, t: Throwable) {
                println("Error:getUsersToFinishMembership() failure")
                apiGetUsersExpiry()
                println(t.message)
            }
        })
    }

}