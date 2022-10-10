package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.fragments.adapter.UserAdapter
import com.tecfit.gym_android_adm.models.User
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListUserFragment : Fragment() {

    private lateinit var usersList:List<User>
    private lateinit var root:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root=inflater.inflate(R.layout.fragments_users,container,false)
        apiGetUsers()
        return root
    }

    private fun initRecyclerView(id:Int){
        val recyclerView = root.findViewById<RecyclerView>(id)

        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.adapter=UserAdapter(usersList)
    }

    private fun apiGetUsers(){
        val apiService:ApiService= RetrofitAdmin.getRetrofit().create(ApiService::class.java)
        val resultUsers:Call<List<User>> = apiService.getUsers()

        resultUsers.enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>,response: Response<List<User>>) {
                val listUsers=response.body()
                if(listUsers!=null){
                    usersList=listUsers
                    initRecyclerView(R.id.recyclerview_users)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                println("Error:getUsers() failure")
                println(t.message)
            }

        })


    }

}