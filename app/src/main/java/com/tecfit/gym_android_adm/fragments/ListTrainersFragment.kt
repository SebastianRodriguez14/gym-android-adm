package com.tecfit.gym_android_adm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tecfit.gym_android_adm.R
import com.tecfit.gym_android_adm.fragments.adapter.TrainerAdapter
import com.tecfit.gym_android_adm.models.Trainer
import com.tecfit.gym_android_adm.retrofit.ApiService
import com.tecfit.gym_android_adm.retrofit.RetrofitAdmin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListTrainersFragment: Fragment() {

    private lateinit var root:View
    private lateinit var trainersList:List<Trainer>
    private lateinit var addButton: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root=inflater.inflate(R.layout.fragment_trainers,container,false)
        apiGetTrainers()
        addButton = root.findViewById(R.id.btn_add_trainer)

        addButton.setOnClickListener{
            val dialog = BottomSheetDialog(root.context, R.style.BottonSheetDialog)
            val vista = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
            println("asd")
            dialog.setCancelable(true)
            dialog.setContentView(vista)

            dialog.show()
        }

        return root
    }





    private fun initRecyclerView(id:Int){
        val recyclerView=root.findViewById<RecyclerView>(id)
        recyclerView.layoutManager=LinearLayoutManager(root.context)
        recyclerView.adapter= TrainerAdapter(trainersList)
    }

    private fun apiGetTrainers(){
        val apiService:ApiService = RetrofitAdmin.getRetrofit().create(ApiService::class.java)

        val resultTrainers: Call<List<Trainer>> = apiService.getTrainers()

        resultTrainers.enqueue(object : Callback<List<Trainer>> {
            override fun onResponse(call: Call<List<Trainer>>, response: Response<List<Trainer>>){
                val listTrainers = response.body()
                if (listTrainers != null){
                    trainersList = listTrainers
                    initRecyclerView(R.id.recyclerview_trainers)
                }
            }
            override fun onFailure(call:Call<List<Trainer>>, t:Throwable){
                println("Error: getTrainers() failure")
                println(t.message)
            }
        })

    }
}